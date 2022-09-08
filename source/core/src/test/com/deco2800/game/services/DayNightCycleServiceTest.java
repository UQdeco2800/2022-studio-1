package com.deco2800.game.services;

import com.deco2800.game.extensions.GameExtension;
import com.deco2800.game.services.configs.DayNightCycleConfig;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(GameExtension.class)
public class DayNightCycleServiceTest {

    private DayNightCycleService dayNightCycleService;

    private DayNightCycleConfig config;

    @BeforeEach
    void beforeEach() {
        config = new DayNightCycleConfig();
        config.dawnLength = 100;
        config.dayLength = 400;
        config.duskLength = 100;
        config.nightLength = 500;
        config.maxDays = 1;
        ServiceLocator.registerTimeSource(new GameTime());
        var gameTime = Mockito.spy(ServiceLocator.getTimeSource());
        dayNightCycleService = Mockito.spy(new DayNightCycleService(gameTime, config));
    }

    @Test
    public void shouldAdvanceToDayWhenAtDawn() {

        dayNightCycleService.getEvents().addListener(DayNightCycleService.EVENT_PART_OF_DAY_PASSED,
                (DayNightCycleStatus partOfDay) -> {
            if (partOfDay == DayNightCycleStatus.DAY && dayNightCycleService.getLastCycleStatus() == DayNightCycleStatus.DAWN) {
                dayNightCycleService.stop();
               Mockito.verify(dayNightCycleService).setPartOfDayTo(DayNightCycleStatus.DAY);
            }
        });
        assertEquals(DayNightCycleStatus.NONE, dayNightCycleService.getCurrentCycleStatus());
        this.dayNightCycleService.start().join();
    }

    @Test
    public void shouldCompleteFullCyclePassingThroughAllPartsOfDay() {
        List<DayNightCycleStatus> partsOfDay = new ArrayList<>();

        this.dayNightCycleService.getEvents().addListener(DayNightCycleService.EVENT_PART_OF_DAY_PASSED,
                (DayNightCycleStatus day) -> {
                    partsOfDay.add(day);
                });
        this.dayNightCycleService.start().join();

        // Test that the day parts are DAWN,DAY,DUSK,NIGHT as specified in Enum skipping NONE
        int i = 0;
        for (var value : Arrays.stream(DayNightCycleStatus.values()).skip(1).toArray()) {
            assertEquals(value, partsOfDay.get(i));
                i++;
        }
    }

    @Test
    public void shouldCompleteFullDayRestartingAtDawn () {
        this.config.maxDays = 2; // override default from 1 to 2
        AtomicBoolean dayPassed = new AtomicBoolean(false);
        this.dayNightCycleService.getEvents().addListener(DayNightCycleService.EVENT_DAY_PASSED,
                (Integer day) -> {
            dayPassed.set(true);
        });
        this.dayNightCycleService.getEvents().addListener(DayNightCycleService.EVENT_PART_OF_DAY_PASSED,
                (DayNightCycleStatus dayPart) -> {
            if (dayPassed.get() && this.dayNightCycleService.getCurrentCycleStatus() == DayNightCycleStatus.DAWN) {
                this.dayNightCycleService.stop();
                // There would now be two dawns
                Mockito.verify(dayNightCycleService, Mockito.atLeast(2)).setPartOfDayTo(DayNightCycleStatus.DAWN);
            }
        });
        this.dayNightCycleService.start().join();

    }

    @Test
    public void shouldStopWhenDaysAreCompleted() {
        this.config.maxDays = 3;
        this.dayNightCycleService.start().join();
        assertTrue(this.dayNightCycleService.hasEnded());
    }

    @Test
    public void shouldGoThroughAllNumberOfDays() throws InterruptedException {
        this.config.maxDays = 3;
        AtomicInteger days = new AtomicInteger(0);
        this.dayNightCycleService.getEvents().addListener(DayNightCycleService.EVENT_DAY_PASSED,
                (Integer dayNumber) -> {
            days.incrementAndGet();
        });
        dayNightCycleService.start().join();
        Thread.sleep(300); // fixes flakyness

        assertEquals(3, dayNightCycleService.getCurrentDayNumber());
        assertEquals(this.config.maxDays, days.get());
    }

    @Test
    public void shouldStopDayTimeWhenPaused() {
        this.dayNightCycleService.start();
        this.dayNightCycleService.pause();

        try {
            Thread.sleep(300);
        } catch(InterruptedException e) {
            System.err.println(e.getMessage());
        }

        assertTrue(this.dayNightCycleService.getTimer().getTime() > 300);
        assertTrue(this.dayNightCycleService.getCurrentDayMillis() < 300);
    }

    @Test
    public void shouldResumeDayTimeWhenResumed() {
        CompletableFuture<Object> job = this.dayNightCycleService.start();
        this.dayNightCycleService.pause();

        assertTrue(this.dayNightCycleService.getCurrentDayMillis() < 1000);

        this.dayNightCycleService.start();
        job.join();

        assertTrue(this.dayNightCycleService.getCurrentDayMillis() > 1000);
    }

    @Test
    public void shouldResumeDayTimeAfterBeingPausedAndResumedMultipleTimes() {
        CompletableFuture<Object> job = this.dayNightCycleService.start();
        this.dayNightCycleService.pause();

        assertTrue(this.dayNightCycleService.getCurrentDayMillis() < 300);

        this.dayNightCycleService.start();

        try {
            Thread.sleep(300);
        } catch(InterruptedException e) {
            System.err.println(e.getMessage());
        }

        this.dayNightCycleService.pause();

        try {
            Thread.sleep(300);
        } catch(InterruptedException e) {
            System.err.println(e.getMessage());
        }

        assertTrue(this.dayNightCycleService.getCurrentDayMillis() < 400);

        this.dayNightCycleService.start();

        try {
            Thread.sleep(300);
        } catch(InterruptedException e) {
            System.err.println(e.getMessage());
        }

        this.dayNightCycleService.pause();

        try {
            Thread.sleep(300);
        } catch(InterruptedException e) {
            System.err.println(e.getMessage());
        }

        assertTrue(this.dayNightCycleService.getCurrentDayMillis() < 700);

        this.dayNightCycleService.start();

        job.join();

        assertTrue(this.dayNightCycleService.getCurrentDayMillis() > 1000);
    }
}
