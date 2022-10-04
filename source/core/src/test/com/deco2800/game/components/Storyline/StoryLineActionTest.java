package com.deco2800.game.components.storyline;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.badlogic.gdx.math.Vector2;
import com.deco2800.game.AtlantisSinks;
import com.deco2800.game.components.Component;
import com.deco2800.game.components.storyline.storyLineAction;
import com.deco2800.game.extensions.GameExtension;
import com.deco2800.game.screens.PrologueScreen;
import com.deco2800.game.services.ServiceLocator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import com.deco2800.game.entities.Entity;


@ExtendWith(GameExtension.class)
class StoryLineActionTest {
    @Test
    void PrologueSkipTest() {
        storyLineAction action = new storyLineAction(null);
        Entity entity = action.getEntity();
        AtlantisSinks game = action.getGame();  ///// maybe input new game into storylineaction

        game.setScreen(AtlantisSinks.ScreenType.STORY_LINE_PROLOGUE);

        entity.getEvents().trigger("skip");

        assertEquals(game.getScreenType(), AtlantisSinks.ScreenType.MAIN_GAME);
    }

    @Test
    void EpilogueSkipTest(){
        storyLineAction action = new storyLineAction(null);
        Entity entity = action.getEntity();
        AtlantisSinks game = action.getGame(); ///// maybe input new game into storylineaction

        game.setScreen(AtlantisSinks.ScreenType.STORY_LINE_EPILOGUE);

        entity.getEvents().trigger("skip");

        assertEquals(game.getScreenType(), AtlantisSinks.ScreenType.MAIN_MENU);
    }

}
