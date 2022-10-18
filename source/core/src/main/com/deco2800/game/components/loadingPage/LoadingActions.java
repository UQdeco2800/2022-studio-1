package com.deco2800.game.components.loadingPage;

import com.deco2800.game.AtlantisSinks;
import com.deco2800.game.components.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LoadingActions extends Component {

    private static final Logger logger = LoggerFactory.getLogger(LoadingActions.class);
    private final AtlantisSinks game;
    public LoadingActions(AtlantisSinks game) {
        this.game = game;
    }

    @Override
    public void create() {
        entity.getEvents().addListener("loadingStart", this::loadingBegin);
        entity.getEvents().addListener("loadingDone", this::loadingFinish);
    }

    private void loadingBegin() {
        logger.debug("Loading Started: Setting screen to load screen");
        game.setScreen(AtlantisSinks.ScreenType.LOAD_PAGE);
    }


    /**
     * Swaps to the Main Game screen.
     */
    private void loadingFinish() {
        logger.debug("Loading complete: Setting screen to main game");
        game.setScreen(AtlantisSinks.ScreenType.MAIN_GAME);
    }

}
