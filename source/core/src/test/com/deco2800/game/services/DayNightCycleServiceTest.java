package com.deco2800.game.services;

import com.deco2800.game.extensions.GameExtension;
import com.deco2800.game.services.configs.DayNightCycleConfig;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InOrder;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(GameExtension.class)
public class DayNightCycleServiceTest {

    private DayNightCycleService dayNightCycleService;

    private DayNightCycleConfig config;

    @BeforeEach
    void beforeEach() {
        ServiceLocator.registerTimeSource(new GameTime());
        var gameTime = Mockito.spy(ServiceLocator.getTimeSource());
        dayNightCycleService = Mockito.spy(new DayNightCycleService(gameTime, config));
        config = new DayNightCycleConfig();
        config.dawnLength = 500;
        config.dayLength = 2000;
        config.duskLength = 500;
        config.nightLength = 3000;
    }

    @Test
    public void shouldAdvanceToDayWhenAtDawn() throws InterruptedException {

        assertEquals(DayNightCycleStatus.DAWN, dayNightCycleService.getCurrentCycleStatus());
        this.dayNightCycleService.start();
        Mockito.verify(this.dayNightCycleService).setPartOfDayTo(DayNightCycleStatus.DAY);
    }

    @Test void shouldCompleteFullCyclePassingThroughAllPartsOfDay() {
        this.dayNightCycleService.start();
        InOrder inOrder = Mockito.inOrder(this.dayNightCycleService);

        inOrder.verify(this.dayNightCycleService).setPartOfDayTo(DayNightCycleStatus.DAWN);
        inOrder.verify(this.dayNightCycleService).setPartOfDayTo(DayNightCycleStatus.DAY);
        inOrder.verify(this.dayNightCycleService).setPartOfDayTo(DayNightCycleStatus.DUSK);
        inOrder.verify(this.dayNightCycleService).setPartOfDayTo(DayNightCycleStatus.NIGHT);
    }

    @Test
    public void shouldCompleteFullDayRestartingAtDawn () throws InterruptedException {
        this.dayNightCycleService.start();
        // Wait long enough for day cycle to complete
        Thread.sleep(config.dawnLength + config.dayLength + config.duskLength + config.nightLength);
        this.dayNightCycleService.pause();
        assertEquals(DayNightCycleStatus.DAWN, this.dayNightCycleService.getCurrentCycleStatus());
    }

}