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
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(GameExtension.class)
public class DayNightCycleServiceTest {

    private DayNightCycleService dayNightCycleService;

    private DayNightCycleConfig config;

    @BeforeEach
    void beforeEach() {
        config = new DayNightCycleConfig();
        config.dawnLength = 500;
        config.dayLength = 2000;
        config.duskLength = 500;
        config.nightLength = 3000;
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
        assertEquals(DayNightCycleStatus.DAWN, dayNightCycleService.getCurrentCycleStatus());
        this.dayNightCycleService.start().join();
    }

    @Test void shouldCompleteFullCyclePassingThroughAllPartsOfDay() {
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
        AtomicInteger daysCount = new AtomicInteger(0);
        this.dayNightCycleService.getEvents().addListener(DayNightCycleService.EVENT_DAY_PASSED,
                (Integer day) -> {
            System.out.println("Called!");
        });

        //this.dayNightCycleService.start().join();

        assertEquals(2, daysCount.get());

    }

}