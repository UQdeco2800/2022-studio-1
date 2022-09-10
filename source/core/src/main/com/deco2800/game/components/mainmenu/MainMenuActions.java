package com.deco2800.game.components.mainmenu;

import com.badlogic.gdx.Game;
import com.deco2800.game.AtlantisSinks;
import com.deco2800.game.components.Component;
import com.deco2800.game.memento.CareTaker;
import com.deco2800.game.services.GameTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class listens to events relevant to the Main Menu Screen and does something when one of the
 * events is triggered.
 */
public class MainMenuActions extends Component {
  private static final Logger logger = LoggerFactory.getLogger(MainMenuActions.class);
  private AtlantisSinks game;

  public MainMenuActions(AtlantisSinks game) {
    this.game = game;
  }

  @Override
  public void create() {
    entity.getEvents().addListener("start", this::onStart);
    entity.getEvents().addListener("load", this::onLoad);
    entity.getEvents().addListener("exit", this::onExit);
    entity.getEvents().addListener("settings", this::onSettings);
  }

  /**
   * Swaps to the Main Game screen.
   */
  private void onStart() {
    logger.info("Start game");
    if (AtlantisSinks.playEpilogue) {
      game.setScreen(AtlantisSinks.ScreenType.STORY_LINE, null);
    } else {
      game.setScreen(AtlantisSinks.ScreenType.MAIN_GAME, new CareTaker());
    }
  }

  /**
   * Intended for loading a saved game state.
   * Load functionality is not actually implemented.
   */
  private void onLoad() {
    logger.info("Load game");
  }

  /**
   * Exits the game.
   */
  private void onExit() {
    logger.info("Exit game");
    game.exit();
  }

  /**
   * Swaps to the Settings screen.
   */
  private void onSettings() {
    logger.info("Launching settings screen");
    game.setSettingsScreen(AtlantisSinks.ScreenType.MAIN_MENU, null);
  }
}
