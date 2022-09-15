package com.deco2800.game.components.storyline.prologue;

import com.deco2800.game.AtlantisSinks;
import com.deco2800.game.components.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class listens to events relevant to the story line Screen and does something when one of the
 * events is triggered.
 */
public class prologueAction extends Component {
    private static final Logger logger = LoggerFactory.getLogger(prologueAction.class);
    private final AtlantisSinks game;
    public prologueAction(AtlantisSinks game) {
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
        logger.info("Start game");
        AtlantisSinks.playEpilogue = false;
        game.setScreen(AtlantisSinks.ScreenType.MAIN_GAME);
    }

}