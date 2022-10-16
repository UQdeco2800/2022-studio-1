package com.deco2800.game.screens;

import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.deco2800.game.AtlantisSinks;
import com.deco2800.game.components.mainmenu.MainMenuActions;
import com.deco2800.game.components.mainmenu.MainMenuDisplay;
import com.deco2800.game.entities.Entity;
import com.deco2800.game.entities.EntityService;
import com.deco2800.game.entities.factories.RenderFactory;
import com.deco2800.game.input.InputDecorator;
import com.deco2800.game.input.InputService;
import com.deco2800.game.rendering.RenderService;
import com.deco2800.game.rendering.Renderer;
import com.deco2800.game.services.AchievementHandler;
import com.deco2800.game.services.ResourceService;
import com.deco2800.game.services.ServiceLocator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * The game screen containing the main menu.
 */
public class MainMenuScreen extends ScreenAdapter {
  private static final Logger logger = LoggerFactory.getLogger(MainMenuScreen.class);
  private final AtlantisSinks game;
  private final Renderer renderer;

  private Table rootTable;
  private float time;

  private static String[] mainMenuTextures = {
      "images/uiElements/exports/title.png"
  };

  public MainMenuScreen(AtlantisSinks game) {
    this.game = game;
    this.time = 0f;

    ArrayList<String> mainTextures = new ArrayList<>(List.of(mainMenuTextures));
    for (int i = 1; i <= 55; i++) {
      mainTextures.add("images/atlantis_background/atlantis_background (" + i + ").png");
    }

    mainMenuTextures = mainTextures.toArray(new String[0]);

    logger.debug("Initialising main menu screen services");
    ServiceLocator.registerInputService(new InputService());
    ServiceLocator.registerResourceService(new ResourceService());
    ServiceLocator.registerEntityService(new EntityService());
    ServiceLocator.registerRenderService(new RenderService());
    ServiceLocator.registerAchievementHandler(new AchievementHandler());

    renderer = RenderFactory.createRenderer();

    loadAssets();
    createUI();
  }

  @Override
  public void render(float delta) {
    this.time += delta;
    if (this.time > 0.4f) {
      ServiceLocator.getEntityService().getNamedEntity("menu").getComponent(MainMenuDisplay.class).nextFrame();
      rootTable = ServiceLocator.getEntityService().getNamedEntity("menu").getComponent(MainMenuDisplay.class)
          .getDisplay();
      ServiceLocator.getEntityService().getNamedEntity("menu").getComponent(MainMenuDisplay.class).updateDisplay();
      this.time = delta;
    }
    ServiceLocator.getEntityService().update();
    renderer.render();
  }

  @Override
  public void resize(int width, int height) {
    if (rootTable != null)
      rootTable.remove();
    rootTable = ServiceLocator.getEntityService().getNamedEntity("menu").getComponent(MainMenuDisplay.class).display();
    renderer.resize(width, height);
    logger.trace("Resized renderer: ({} x {})", width, height);
  }

  @Override
  public void pause() {
    logger.info("Game paused");
  }

  @Override
  public void resume() {
    logger.info("Game resumed");
  }

  @Override
  public void dispose() {
    logger.debug("Disposing main menu screen");

    renderer.dispose();
    unloadAssets();
    ServiceLocator.getRenderService().dispose();
    ServiceLocator.getEntityService().dispose();

    ServiceLocator.clear();
  }

  private void loadAssets() {
    logger.debug("Loading assets");
    ResourceService resourceService = ServiceLocator.getResourceService();
    resourceService.loadTextures(mainMenuTextures);
    ServiceLocator.getResourceService().loadAll();
  }

  private void unloadAssets() {
    logger.debug("Unloading assets");
    ResourceService resourceService = ServiceLocator.getResourceService();
    resourceService.unloadAssets(mainMenuTextures);
  }

  /**
   * Creates the main menu's ui including components for rendering ui elements to
   * the screen and
   * capturing and handling ui input.
   */
  private void createUI() {
    logger.debug("Creating ui");
    Stage stage = ServiceLocator.getRenderService().getStage();
    Entity ui = new Entity();
    ui.addComponent(new MainMenuDisplay())
        .addComponent(new InputDecorator(stage, 10))
        .addComponent(new MainMenuActions(game));
    ServiceLocator.getEntityService().registerNamed("menu", ui);
    rootTable = ServiceLocator.getEntityService().getNamedEntity("menu").getComponent(MainMenuDisplay.class)
        .getDisplay();
  }
}
