package com.deco2800.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.deco2800.game.files.UserSettings;
import com.deco2800.game.memento.CareTaker;
import com.deco2800.game.screens.MainGameScreen;
import com.deco2800.game.screens.MainMenuScreen;
import com.deco2800.game.screens.SettingsScreen;
import com.deco2800.game.screens.ShopScreen;
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

  @Override
  public void create() {
    logger.info("Creating game");
    loadSettings();

    // Sets background to light yellow
    Gdx.gl.glClearColor(248f / 255f, 249 / 255f, 178 / 255f, 1);

    setScreen(ScreenType.MAIN_MENU, null);
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
  public void setScreen(ScreenType screenType, CareTaker playerStatus) {
    logger.info("Setting game screen to {}", screenType);
    Screen currentScreen = getScreen();
    if (currentScreen != null) {
      currentScreen.dispose();
    }

    if (screenType == ScreenType.MAIN_GAME) {
      Gdx.gl.glClearColor(44f / 255f, 49 / 255f, 120 / 255f, 1);
    } else {
      Gdx.gl.glClearColor(248f / 255f, 249 / 255f, 178 / 255f, 1);
    }
    setScreen(newScreen(screenType, playerStatus));
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
  private Screen newScreen(ScreenType screenType, CareTaker playerStatus) {
    switch (screenType) {
      case MAIN_MENU:
        return new MainMenuScreen(this);
      case MAIN_GAME:
        return new MainGameScreen(this, playerStatus);
      case SETTINGS:
        return new SettingsScreen(this);
      case SHOP:
        return new ShopScreen(this, playerStatus);
      default:
        return null;
    }
  }

  public enum ScreenType {
    MAIN_MENU, MAIN_GAME, SETTINGS, SHOP
  }

  /**
   * Exit the game.
   */
  public void exit() {
    app.exit();
  }
}
