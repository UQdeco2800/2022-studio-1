package com.deco2800.game.screens;

import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.deco2800.game.AtlantisSinks;
import com.deco2800.game.areas.AtlantisSinksGameArea;
import com.deco2800.game.areas.GameService;
import com.deco2800.game.areas.MainArea;
import com.deco2800.game.areas.terrain.TerrainFactory;
import com.deco2800.game.components.gamearea.PerformanceDisplay;
import com.deco2800.game.components.maingame.MainGameActions;
import com.deco2800.game.components.maingame.MainGameExitDisplay;
import com.deco2800.game.components.maingame.MainGameInterface;
import com.deco2800.game.components.maingame.MainGameBuildingInterface;
import com.deco2800.game.entities.Entity;
import com.deco2800.game.entities.EntityService;
import com.deco2800.game.entities.StructureService;
import com.deco2800.game.entities.factories.RenderFactory;
import com.deco2800.game.files.FileLoader;
import com.deco2800.game.input.InputComponent;
import com.deco2800.game.input.InputDecorator;
import com.deco2800.game.input.InputService;
import com.deco2800.game.memento.CareTaker;
import com.deco2800.game.physics.PhysicsEngine;
import com.deco2800.game.physics.PhysicsService;
import com.deco2800.game.rendering.DayNightCycleComponent;
import com.deco2800.game.rendering.RenderService;
import com.deco2800.game.rendering.Renderer;
import com.deco2800.game.services.DayNightCycleService;
import com.deco2800.game.services.GameTime;
import com.deco2800.game.services.ResourceService;
import com.deco2800.game.services.ServiceLocator;
import com.deco2800.game.services.configs.DayNightCycleConfig;
import com.deco2800.game.ui.terminal.Terminal;
import com.deco2800.game.ui.terminal.TerminalDisplay;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The game screen containing the main game.
 *
 * <p>
 * Details on libGDX screens:
 * https://happycoding.io/tutorials/libgdx/game-screens
 */
public class MainGameScreen extends ScreenAdapter {
  private static final Logger logger = LoggerFactory.getLogger(MainGameScreen.class);

  private static final String[] mainGameTextures = {
      "images/uiElements/exports/heart.png",
      "images/uiElements/exports/coin.png",
      "images/healthBar.png",
      "images/uiElements/exports/crystal.png",
      "images/uiElements/exports/stoneSuperior.png",
      "images/atlantisBasicBackground.png"
  };

  private static final Vector2 CAMERA_POSITION = new Vector2(60f, 0f);

  private static final String[] mainGameTextureAtlases = {
      "images/anim_demo/stonequarr.atlas", "images/anim_demo/mainchar.atlas" };

  private final AtlantisSinks game;
  private final Renderer renderer;
  private final PhysicsEngine physicsEngine;
  private CareTaker playerStatus;

  public MainGameScreen(AtlantisSinks game, CareTaker playerStatus) {
    this.game = game;

    // creates new caretaker if no caretaker object exists
    if (playerStatus == null) {
      this.playerStatus = new CareTaker();
    } else {
      this.playerStatus = playerStatus;
    }

    logger.debug("Initialising main game screen services");
    ServiceLocator.registerTimeSource(new GameTime());

    var dayNightCycleService = new DayNightCycleService(ServiceLocator.getTimeSource(),
            FileLoader.readClass(DayNightCycleConfig.class, "configs/DayNight.json"));
    ServiceLocator.registerDayNightCycleService(dayNightCycleService);

    PhysicsService physicsService = new PhysicsService();
    ServiceLocator.registerPhysicsService(physicsService);
    physicsEngine = physicsService.getPhysics();

    ServiceLocator.registerInputService(new InputService());
    ServiceLocator.registerResourceService(new ResourceService());

    ServiceLocator.registerEntityService(new EntityService());
    ServiceLocator.registerRenderService(new RenderService());
    ServiceLocator.registerStructureService(new StructureService());
    ServiceLocator.registerGameService(new GameService());
    var dayNightCycleComponent = new DayNightCycleComponent();
    ServiceLocator.getRenderService().setDayNightCycleComponent(dayNightCycleComponent);
    ServiceLocator.getInputService().register(dayNightCycleComponent);


    renderer = RenderFactory.createRenderer();
    renderer.getCamera().getEntity().setPosition(CAMERA_POSITION);
    renderer.getDebug().renderPhysicsWorld(physicsEngine.getWorld());

    loadAssets();

    logger.debug("Initialising main game screen entities");
    TerrainFactory terrainFactory = new TerrainFactory(renderer.getCamera());

    // Singleton MainArea responsible for controlling current map and entities
    MainArea.getInstance().setMainArea(new AtlantisSinksGameArea(terrainFactory));

    createUI();
    ServiceLocator.getDayNightCycleService().start();
  }

  @Override
  public void render(float delta) {
    physicsEngine.update();
    ServiceLocator.getEntityService().update();
    renderer.render();
  }

  @Override
  public void resize(int width, int height) {
    renderer.resize(width, height);
    logger.trace("Resized renderer: ({} x {})", width, height);
  }

  @Override
  public void pause() {
    ServiceLocator.getDayNightCycleService().pause();
    logger.info("Game paused");
  }

  @Override
  public void resume() {
    ServiceLocator.getDayNightCycleService().start();
    logger.info("Game resumed");
  }

  @Override
  public void dispose() {
    logger.debug("Disposing main game screen");

    renderer.dispose();
    unloadAssets();

    ServiceLocator.getEntityService().dispose();
    ServiceLocator.getRenderService().dispose();
    ServiceLocator.getResourceService().dispose();

    ServiceLocator.clear();
  }

  private void loadAssets() {
    logger.debug("Loading assets");
    ResourceService resourceService = ServiceLocator.getResourceService();
    resourceService.loadTextures(mainGameTextures);
    resourceService.loadTextureAtlases(mainGameTextureAtlases);
    ServiceLocator.getResourceService().loadAll();
  }

  private void unloadAssets() {
    logger.debug("Unloading assets");
    ResourceService resourceService = ServiceLocator.getResourceService();
    resourceService.unloadAssets(mainGameTextures);
    resourceService.unloadAssets(mainGameTextureAtlases);
  }

  /**
   * Creates the main game's ui including components for rendering ui elements to
   * the screen and
   * capturing and handling ui input.
   */
  private void createUI() {
    logger.debug("Creating ui");
    Stage stage = ServiceLocator.getRenderService().getStage();
    InputComponent inputComponent = ServiceLocator.getInputService().getInputFactory().createForTerminal();

    Entity ui = new Entity();
    ui.addComponent(new InputDecorator(stage, 10))
        .addComponent(new PerformanceDisplay())
        .addComponent(new MainGameActions(this.game, this.playerStatus, MainArea.getInstance().getGameArea().getPlayer()))
        .addComponent(new MainGameExitDisplay())
        .addComponent(new MainGameInterface())
        .addComponent(new MainGameBuildingInterface())
        .addComponent(new Terminal())
        .addComponent(inputComponent)
        .addComponent(new TerminalDisplay());
    ServiceLocator.getEntityService().registerNamed("ui", ui);
  }
}
