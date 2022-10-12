package com.deco2800.game.rendering;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.deco2800.game.extensions.GameExtension;
import com.deco2800.game.services.DayNightCycleStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(GameExtension.class)
@ExtendWith(MockitoExtension.class)
class DayNightCycleComponentTest {

    @Mock
    private DayNightCycleComponent dayNightCycleComponent;
    @Mock
    private SpriteBatch batch;

    @BeforeEach
    void beforeEach() {
        dayNightCycleComponent = new DayNightCycleComponent();
    }

    @Test
    public void shouldSetShader() {
        dayNightCycleComponent.render(batch);
        verify(batch, times(1)).setShader(dayNightCycleComponent.getDayNightCycleShader());
    }

    @Test
    public void shouldSetIntensityToDawnIntensity() {
        dayNightCycleComponent.onPartOfDayChange(DayNightCycleStatus.DAWN);
        assertEquals(DayNightCycleComponent.DAWN_INTENSITY, dayNightCycleComponent.getIntensity());
    }

    @Test
    public void shouldSetIntensityToDayIntensity() {
        dayNightCycleComponent.onPartOfDayChange(DayNightCycleStatus.DAY);
        assertEquals(DayNightCycleComponent.DAY_INTENSITY, dayNightCycleComponent.getIntensity());
    }

    @Test
    public void shouldSetIntensityToDuskIntensity() {
        dayNightCycleComponent.onPartOfDayChange(DayNightCycleStatus.DUSK);
        assertEquals(DayNightCycleComponent.DUSK_INTENSITY, dayNightCycleComponent.getIntensity());
    }

    @Test
    public void shouldSetIntensityToNightIntensity() {
        dayNightCycleComponent.onPartOfDayChange(DayNightCycleStatus.NIGHT);
        assertEquals(DayNightCycleComponent.NIGHT_INTENSITY, dayNightCycleComponent.getIntensity());
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
    public void shouldShadeNightWithDarkAmbientColour() {
        dayNightCycleComponent.onPartOfDayChange(DayNightCycleStatus.NIGHT);
        assertEquals(DayNightCycleComponent.dark, dayNightCycleComponent.getAmbientColour());
    }


}
