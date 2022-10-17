package com.deco2800.game.components;


import com.deco2800.game.AtlantisSinks;
import com.deco2800.game.components.loadingPage.LoadingActions;
import com.deco2800.game.extensions.GameExtension;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.Assert.assertTrue;

@ExtendWith(GameExtension.class)
@RunWith(MockitoJUnitRunner.class)
public class loadingScreenActions extends Component {

    @Mock
    private AtlantisSinks game;
    @Mock
    private LoadingActions actions;

    @BeforeEach
    void beforeEach() {
        actions = new LoadingActions(game);
    }

    /**
     * Testing if the GuidebookActions class is null
     *
     * @return true if not null and false if null
     */
    boolean testNull() {
        return actions.getClass() != null;
    }

    /**
     * Testing to ensure the class is not null and initiated
     */
    @Test
    public void notNullTest() {
        assertTrue(testNull());
    }

    /**
     * Ensures that the creation of event handlers is working
     */
    @Test
    public void createTest() {
        actions.create();
    }
}
