package com.deco2800.game.components.shop;

import com.deco2800.game.GdxGame;
import com.deco2800.game.components.Component;
import com.deco2800.game.rendering.Renderer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class listens to events relevant to the Main Game Screen and does
 * something when one of the
 * events is triggered.
 */
public class ShopActions extends Component {
    private static final Logger logger = LoggerFactory.getLogger(ShopActions.class);
    private GdxGame game;

    private Renderer renderer;

    public ShopActions(GdxGame game) {
        this.game = game;
    }

    @Override
    public void create() {
        entity.getEvents().addListener("exit", this::onExit);
        entity.getEvents().addListener("buildShop", this::onBuildShop);
    }

    /**
     * Swaps to the Main Menu screen.
     */
    private void onExit() {
        logger.info("Exiting shop screen");
        game.setScreen(GdxGame.ScreenType.MAIN_GAME);
    }

    /**
     * Swaps to the Build Shop screen.
     */
    private void onBuildShop() {
        logger.info("Entering Build shop screen");
        game.setScreen(GdxGame.ScreenType.BUILD_SHOP);
    }

}
