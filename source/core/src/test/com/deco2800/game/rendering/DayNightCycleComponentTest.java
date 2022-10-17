package com.deco2800.game.rendering;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.deco2800.game.extensions.GameExtension;
import com.deco2800.game.services.DayNightCycleService;
import com.deco2800.game.services.DayNightCycleStatus;
import com.deco2800.game.services.GameTime;
import com.deco2800.game.services.ServiceLocator;
import com.deco2800.game.services.configs.DayNightCycleConfig;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(GameExtension.class)
@ExtendWith(MockitoExtension.class)
class DayNightCycleComponentTest {

    @Spy
    private DayNightCycleComponent dayNightCycleComponent;
    @Mock
    private SpriteBatch batch;

    @BeforeEach
    void beforeEach() {
        DayNightCycleConfig config = new DayNightCycleConfig();
        config.dawnLength = 150;
        config.dayLength = 600;
        config.duskLength = 150;
        config.nightLength = 300;
        config.maxDays = 1;
        ServiceLocator.registerTimeSource(new GameTime());
        var gameTime = Mockito.spy(ServiceLocator.getTimeSource());
        ServiceLocator.registerDayNightCycleService(Mockito.spy(new DayNightCycleService(gameTime, config)));
    }

    @Test
    public void shouldShadeDawnWithBrightAmbientColour() {
        dayNightCycleComponent.onPartOfDayChange(DayNightCycleStatus.DAWN);
        assertEquals(DayNightCycleComponent.bright, dayNightCycleComponent.getAmbientColour());
    }

    @Test
    public void shouldShadeDayWithBrightAmbientColour() {
        dayNightCycleComponent.onPartOfDayChange(DayNightCycleStatus.DAY);
        assertEquals(DayNightCycleComponent.bright, dayNightCycleComponent.getAmbientColour());
    }

    @Test
    public void shouldShadeDuskWithBrightAmbientColour() {
        dayNightCycleComponent.onPartOfDayChange(DayNightCycleStatus.DUSK);
        assertEquals(DayNightCycleComponent.bright, dayNightCycleComponent.getAmbientColour());
    }

    @Test
    public void shouldFade() {
        dayNightCycleComponent.render(batch);
        Mockito.verify(dayNightCycleComponent).fade();
    }

    @Test
    public void shouldUseDarkShaderWhenNight() {
        Mockito.when(dayNightCycleComponent.shouldFade()).thenReturn(true);
        dayNightCycleComponent.setPartOfDay(DayNightCycleStatus.NIGHT);
        dayNightCycleComponent.fade();
        assertEquals(DayNightCycleComponent.dark, dayNightCycleComponent.getAmbientColour());
    }

}
