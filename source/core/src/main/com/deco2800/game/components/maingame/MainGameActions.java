package com.deco2800.game.components.maingame;

import com.deco2800.game.AtlantisSinks;
import com.deco2800.game.components.Component;
import com.deco2800.game.rendering.Renderer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class listens to events relevant to the Main Game Screen and does
 * something when one of the
 * events is triggered.
 */
public class MainGameActions extends Component {
  private static final Logger logger = LoggerFactory.getLogger(MainGameActions.class);
  private AtlantisSinks game;

  public MainGameActions(AtlantisSinks game) {
    this.game = game;
  }

  @Override
  public void create() {
    entity.getEvents().addListener("exit", this::onExit);
    entity.getEvents().addListener("shop", this::openShop);
  }

  /**
   * Swaps to the Main Menu screen.
   */
  private void onExit() {
    logger.info("Exiting main game screen");
    game.setScreen(AtlantisSinks.ScreenType.MAIN_MENU);
  }

  private void openShop() {
    logger.info("Entering shop");
    game.setScreen(GdxGame.ScreenType.SHOP);
  }

  private void openBuildingShop() {
    logger.info("Entering Building Shop");
    game.setScreen(GdxGame.ScreenType.BUILD_SHOP);
  }
}
