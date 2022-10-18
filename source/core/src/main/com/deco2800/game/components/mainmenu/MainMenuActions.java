package com.deco2800.game.components.mainmenu;

import com.deco2800.game.AtlantisSinks;
import com.deco2800.game.components.Component;
import com.deco2800.game.services.ServiceLocator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class listens to events relevant to the Main Menu Screen and does something when one of the
 * events is triggered.
 */
public class MainMenuActions extends Component {
  private static final Logger logger = LoggerFactory.getLogger(MainMenuActions.class);
  private final AtlantisSinks game;

  public MainMenuActions(AtlantisSinks game) {
    this.game = game;
  }

  @Override
  public void create() {
    entity.getEvents().addListener("start", this::onStart);
    entity.getEvents().addListener("load", this::onLoad);
    entity.getEvents().addListener("exit", this::onExit);
    entity.getEvents().addListener("settings", this::onSettings);
    entity.getEvents().addListener("guideBook", this::onGuidebook);
  }

  /**
   * Swaps to the Main Game screen.
   */
  private void onStart() {
    logger.info("Start game");
    ServiceLocator.getAchievementHandler().resetOneRunAchievements(false);
    if (AtlantisSinks.playPrologue) {
      game.setScreen(AtlantisSinks.ScreenType.STORY_LINE_PROLOGUE);
    } else {
      game.setScreen(AtlantisSinks.ScreenType.MAIN_GAME);
    }
  }

  /**
   * Generate Main Game with load param
   */
  private void onLoad() {
    logger.info("Load game");
    game.setScreen(AtlantisSinks.ScreenType.MAIN_GAME_LOAD);
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
    game.setSettingsScreen(AtlantisSinks.ScreenType.MAIN_MENU);
  }

  private void onGuidebook() {
    logger.info("Launching guidebook screen");
    game.setScreen(AtlantisSinks.ScreenType.GUIDEBOOK);
  }
}
