package com.deco2800.game.screens;

import com.deco2800.game.AtlantisSinks;
import com.deco2800.game.screens.GuidebookScreen;
import com.deco2800.game.extensions.GameExtension;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static junit.framework.TestCase.assertTrue;

@ExtendWith(GameExtension.class)
@RunWith(MockitoJUnitRunner.class)
public class guidebookScreenTest {
    @Mock
    GuidebookScreen screen;
    @Mock
    AtlantisSinks game;

    @BeforeEach
    void beforeEach() {
        screen = new GuidebookScreen(game);
    }

    /**
     *
     * @return True if the class is null
     *         False is the class is not null
     */
    public boolean testingNull() {
        if (screen.getClass() != null) {
            return true;
        }
        return false;
    }

    /**
     * Tests if the class is not nul
     */
    @Test
    public void notNullTest() {
        assertTrue(testingNull());
    }

    // @Test
    // public void resizeTest() {
    // mock(screen.getClass());
    // verify(screen, times(1)).resize(100, 100);
    // }

}
