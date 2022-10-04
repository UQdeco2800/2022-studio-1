package com.deco2800.game.components.storyline;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.deco2800.game.AtlantisSinks;
import com.deco2800.game.extensions.GameExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import com.deco2800.game.entities.Entity;


@ExtendWith(GameExtension.class)
@ExtendWith(MockitoExtension.class)
class StoryLineActionTest {
    @Mock AtlantisSinks game; 


    @Test
    void PrologueSkipTest() {
        storyLineAction action = new storyLineAction(game);
        Entity entity = action.getEntity();

        game.setScreen(AtlantisSinks.ScreenType.STORY_LINE_PROLOGUE);

        entity.getEvents().trigger("skip");

        assertEquals(game.getScreenType(), AtlantisSinks.ScreenType.MAIN_GAME);
    }

    @Test
    void EpilogueSkipTest(){
        storyLineAction action = new storyLineAction(game);
        Entity entity = action.getEntity();

        game.setScreen(AtlantisSinks.ScreenType.STORY_LINE_EPILOGUE);

        entity.getEvents().trigger("skip");

        assertEquals(game.getScreenType(), AtlantisSinks.ScreenType.MAIN_MENU);
    }

}
