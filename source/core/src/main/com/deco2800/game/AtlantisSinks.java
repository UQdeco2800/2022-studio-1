package com.deco2800.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.deco2800.game.files.UserSettings;
import com.deco2800.game.screens.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.badlogic.gdx.Gdx.app;

/**
 * Entry point of the non-platform-specific game logic. Controls which screen is
 * currently running.
 * The current screen triggers transitions to other screens. This works
 * similarly to a finite state
 * machine (See the State Pattern).
 */
public class AtlantisSinks extends Game {
  private static final Logger logger = LoggerFactory.getLogger(AtlantisSinks.class);
  private ScreenType screenType;

  public static boolean gameRunning = false;
  public static boolean playPrologue = true;

  @Override
  public void create() {
    logger.info("Creating game");
    loadSettings();

    // Sets background to light yellow
    Gdx.gl.glClearColor(248f / 255f, 249 / 255f, 178 / 255f, 1);

    // start of the game, sets playerStatus to null to create a new caretaker object
    // in the first mainGameScreen
    setScreen(ScreenType.MAIN_MENU);
  }

  /**
   * Loads the game's settings.
   */
  private void loadSettings() {
    logger.debug("Loading game settings");
    UserSettings.Settings settings = UserSettings.get();
    UserSettings.applySettings(settings);
  }

  /**
   * Sets the game's screen to a new screen of the provided type.
   * 
   * @param screenType screen type
   */
  public void setScreen(ScreenType screenType) {
    logger.info("Setting game screen to {}", screenType);
    Screen currentScreen = getScreen();
    if (currentScreen != null) {
      currentScreen.dispose();
    }

    if (screenType == ScreenType.MAIN_GAME) {
      Gdx.gl.glClearColor(44f / 255f, 49 / 255f, 120 / 255f, 1);
    }  else if (screenType == ScreenType.GUIDEBOOK) {
      Gdx.gl.glClearColor(216f / 255f, 189f / 255f, 151f / 255f, 1);
    } else {
      Gdx.gl.glClearColor(248f / 255f, 249 / 255f, 178 / 255f, 1);
    }
    setScreen(newScreen(screenType, null));
  }

  public void setSettingsScreen(ScreenType prevScreen) {
    logger.info("Setting game screen to {}", ScreenType.SETTINGS);
    Screen currentScreen = getScreen();
    if (currentScreen != null) {
      currentScreen.dispose();
    }

    Gdx.gl.glClearColor(248f / 255f, 249 / 255f, 178 / 255f, 1);

    setScreen(newScreen(ScreenType.SETTINGS, prevScreen));
  }

  @Override
  public void dispose() {
    logger.debug("Disposing of current screen");
    getScreen().dispose();
  }

  /**
   * Create a new screen of the provided type.
   * 
   * @param screenType screen type
   * @return new screen
   */
  private Screen newScreen(ScreenType screenType, ScreenType prevScreen) {
    gameRunning = screenType == ScreenType.MAIN_GAME;
    this.screenType = screenType;
    switch (screenType) {
      case MAIN_MENU:
        return new MainMenuScreen(this);
      case STORY_LINE_PROLOGUE:
        return new PrologueScreen(this);
      case MAIN_GAME:
        return new MainGameScreen(this, false);
      case SETTINGS:
        return new SettingsScreen(this, prevScreen);
      case FIRST_NIGHT:
        return new FirstNightScreen(this);
      case STORY_LINE_EPILOGUE:
        return new EpilogueScreen(this);
      case GUIDEBOOK:
        return new GuidebookScreen(this);
      case MAIN_GAME_LOAD:
        return new MainGameScreen(this, true);
      default:
        return null;
    }
  }

  public enum ScreenType {
    MAIN_MENU, STORY_LINE_PROLOGUE, MAIN_GAME, SETTINGS, FIRST_NIGHT, STORY_LINE_EPILOGUE, GUIDEBOOK,
    MAIN_GAME_LOAD
  }

  /**
   * Exit the game.
   */
  public void exit() {
    app.exit();
  }

  public ScreenType getScreenType() {
    return screenType;
  }
}
