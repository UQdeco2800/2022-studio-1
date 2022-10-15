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
    public void shouldSetIntensityToDawnIntensity() {
        dayNightCycleComponent.onPartOfDayChange(DayNightCycleStatus.DAWN);
        assertEquals(DayNightCycleComponent.DAWN_INTENSITY, dayNightCycleComponent.getIntensity());
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


}
