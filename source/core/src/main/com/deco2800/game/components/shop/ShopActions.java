package com.deco2800.game.components.shop;

import com.deco2800.game.AtlantisSinks;
import com.deco2800.game.components.Component;
import com.deco2800.game.rendering.Renderer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class listens to events relevant to the Main Game Screen and does something when one of the
 * events is triggered.
 */
public class ShopActions extends Component {
    private static final Logger logger = LoggerFactory.getLogger(ShopActions.class);
    private AtlantisSinks game;

    private Renderer renderer;

    public ShopActions(AtlantisSinks game) {
        this.game = game;
    }

    @Override
    public void create() {
        entity.getEvents().addListener("exit", this::onExit);
    }

    /**
     * Swaps to the Main Menu screen.
     */
    private void onExit() {
        logger.info("Exiting shop screen");
        game.setScreen(AtlantisSinks.ScreenType.MAIN_GAME);
    }

}
