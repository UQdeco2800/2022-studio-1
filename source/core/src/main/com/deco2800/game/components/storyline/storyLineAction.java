package com.deco2800.game.components.storyline;

import com.deco2800.game.AtlantisSinks;
import com.deco2800.game.components.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class storyLineAction extends Component {

    private static final Logger logger = LoggerFactory.getLogger(storyLineAction.class);
    private final AtlantisSinks game;
    public storyLineAction(AtlantisSinks game) {
        this.game = game;
    }

    @Override
    public void create() {
        entity.getEvents().addListener("skip", this::onSkip);
    }

    /**
     * Swaps to the Main Game screen.
     */
    private void onSkip() {
        if (game.getScreenType() == AtlantisSinks.ScreenType.STORY_LINE_PROLOGUE) {
            logger.info("Start game");
            AtlantisSinks.playPrologue = false;
            game.setScreen(AtlantisSinks.ScreenType.MAIN_GAME);
        } else if (game.getScreenType() == AtlantisSinks.ScreenType.STORY_LINE_EPILOGUE) {
            logger.info("Game over - redirect to Main Menu");
            game.setScreen(AtlantisSinks.ScreenType.MAIN_MENU);
        }
    }

}
