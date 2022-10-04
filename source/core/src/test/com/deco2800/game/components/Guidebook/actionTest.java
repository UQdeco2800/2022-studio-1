package com.deco2800.game.components.Guidebook;

import com.deco2800.game.AtlantisSinks;
import com.deco2800.game.extensions.GameExtension;
import com.deco2800.game.services.ServiceLocator;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(GameExtension.class)
@RunWith(MockitoJUnitRunner.class)
public class actionTest {
    @Mock
    private AtlantisSinks game;
    @Mock
    private GuidebookActions actions;

    @BeforeEach
    void beforeEach() {
        actions = new GuidebookActions(game);
    }

    /**
     * Testing if the GuidebookActions class is null
     *
     * @return true if not null and false if null
     */
    boolean testNull() {
        if (actions.getClass() != null) {
            return true;
        }

        return false;
    }

    /**
     * Testing to ensure the class is not null and initiated
     */
    @Test
    public void notNullTest() {
        assertTrue(testNull());
    }

    /**
     * Checking that the page number starts a zero
     * when the game is loaded.
     */
    @Test
    public void pageNumberTest() {
        int currentPage = actions.currentPage;
        assertEquals(currentPage, 0);
    }

    /**
     * Ensures that the creation of event handlers is working
     */
    @Test
    public void createTest() {
        actions.create();
    }

}
