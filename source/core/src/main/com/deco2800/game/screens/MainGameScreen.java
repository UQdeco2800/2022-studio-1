package com.deco2800.game.screens;

import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.deco2800.game.AtlantisSinks;
import com.deco2800.game.areas.ForestGameArea;
import com.deco2800.game.areas.MainArea;
import com.deco2800.game.areas.terrain.TerrainFactory;
import com.deco2800.game.components.DayNightClockComponent;
import com.deco2800.game.components.achievements.AchievementInterface;
import com.deco2800.game.components.achievements.AchievementPopupComponent;
import com.deco2800.game.components.gamearea.PerformanceDisplay;
import com.deco2800.game.components.maingame.*;
import com.deco2800.game.components.shop.ArtefactShopDisplay;
import com.deco2800.game.components.shop.BuildingShopDisplay;
import com.deco2800.game.components.shop.EquipmentsShopDisplay;
import com.deco2800.game.components.shop.ShopInterface;
import com.deco2800.game.entities.*;
import com.deco2800.game.entities.factories.RenderFactory;
import com.deco2800.game.files.FileLoader;
import com.deco2800.game.input.InputComponent;
import com.deco2800.game.input.InputDecorator;
import com.deco2800.game.input.InputService;
import com.deco2800.game.physics.PhysicsEngine;
import com.deco2800.game.physics.PhysicsService;
import com.deco2800.game.rendering.DayNightCycleComponent;
import com.deco2800.game.rendering.RenderService;
import com.deco2800.game.rendering.Renderer;
import com.deco2800.game.services.*;
import com.deco2800.game.services.configs.DayNightCycleConfig;
import com.deco2800.game.ui.terminal.Terminal;
import com.deco2800.game.ui.terminal.TerminalDisplay;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;

/**
 * The game screen containing the main game.
 *
 * <p>
 * Details on libGDX screens:
 * https://happycoding.io/tutorials/libgdx/game-screens
 */
public class MainGameScreen extends ScreenAdapter {
  private static final Logger logger = LoggerFactory.getLogger(MainGameScreen.class);

  private static final Vector2 CAMERA_POSITION = new Vector2(960f, 5f);

  private static final String[] mainGameTextureAtlases = {
      "images/anim_demo/stonequarr.atlas", "images/anim_demo/woodresource.atlas", "images/anim_demo/main.atlas"
  };

  private final AtlantisSinks game;
  private final Renderer renderer;
  private final PhysicsEngine physicsEngine;

  public MainGameScreen(AtlantisSinks game, Boolean loadGame) {

    this.game = game;
    logger.debug("Initialising main game screen services");
    ServiceLocator.registerTimeSource(new GameTime());

    var dayNightCycleService = new DayNightCycleService(ServiceLocator.getTimeSource(),
        FileLoader.readClass(DayNightCycleConfig.class, "configs/DayNight.json"));
    ServiceLocator.registerDayNightCycleService(dayNightCycleService);

    PhysicsService physicsService = new PhysicsService();
    ServiceLocator.registerPhysicsService(physicsService);
    physicsEngine = physicsService.getPhysics();

    ServiceLocator.registerInputService(new InputService());
    //ServiceLocator.registerResourceService(new ResourceService());
    ServiceLocator.registerUGSService(new UGS());
    ServiceLocator.registerEntityService(new EntityService());
    ServiceLocator.registerRangeService(new RangeService());
    ServiceLocator.registerRenderService(new RenderService());
    ServiceLocator.registerStructureService(new StructureService());
    var dayNightCycleComponent = new DayNightCycleComponent();
    ServiceLocator.getRenderService().setDayNightCycleComponent(dayNightCycleComponent);
    ServiceLocator.registerResourceManagementService(new ResourceManagementService());
    ServiceLocator.registerAchievementHandler(new AchievementHandler());
    ServiceLocator.registerNpcService(new NpcService());

    renderer = RenderFactory.createRenderer();
    renderer.getCamera().getEntity().setPosition(CAMERA_POSITION);
    renderer.getDebug().renderPhysicsWorld(physicsEngine.getWorld());
    loadAssets();

    logger.debug("Initialising main game screen entities");
    TerrainFactory terrainFactory = new TerrainFactory(renderer.getCamera());

    // Singleton MainArea responsible for controlling current map and entities
    // MainArea.getInstance().setMainArea(new
    // AtlantisSinksGameArea(terrainFactory));
    MainArea.getInstance().setMainArea(new ForestGameArea(terrainFactory, loadGame));

    createUI(loadGame);

    /*
     * Achievements setup
     * WARNING: must be done after UI is created
     */
    ServiceLocator.getAchievementHandler().connectPopupListeners();
    ServiceLocator.getAchievementHandler().triggerOnLoadPopups();
  }

  @Override
  public void render(float delta) {
    physicsEngine.update();
    ServiceLocator.getEntityService().update();
    ServiceLocator.getStructureService().update();
    renderer.render();

    // delete entities MUST BE DONE HERE DUE TO CONCURRENCY ISSUES
    if (ServiceLocator.getEntityService() != null) {
      if (!ServiceLocator.getEntityService().getCurrentWorldStep()) {

        for (Entity e : ServiceLocator.getEntityService().getToDestroyEntities()) {
          e.dispose();
        }
      }
    }
    ServiceLocator.getEntityService().toDestroyEntities = new ArrayList<>();
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
//    unloadAssets();
//
//    ServiceLocator.getEntityService().dispose();
//    ServiceLocator.getRenderService().dispose();
//    ServiceLocator.getResourceService().dispose();
//
//    ServiceLocator.clear();
  }

  private void loadAssets() {
    logger.debug("Loading assets");
    ResourceService resourceService = ServiceLocator.getResourceService();
    resourceService.loadTextureAtlases(mainGameTextureAtlases);
    ServiceLocator.getResourceService().loadAll();
  }

  private void unloadAssets() {
    logger.debug("Unloading assets");
    ResourceService resourceService = ServiceLocator.getResourceService();
    resourceService.unloadAssets(mainGameTextureAtlases);
  }

  /**
   * Creates the main game's ui including components for rendering ui elements to
   * the screen and
   * capturing and handling ui input.
   */
  private void createUI(boolean loadGame) {
    logger.debug("Creating ui");
    Stage stage = ServiceLocator.getRenderService().getStage();
    InputComponent inputComponent = ServiceLocator.getInputService().getInputFactory().createForTerminal();

    Entity ui = new Entity();
    ui.addComponent(new InputDecorator(stage, 10))
        .addComponent(new PerformanceDisplay())
        .addComponent(new MainGameActions(this.game, MainArea.getInstance().getGameArea().getPlayer()))
        .addComponent(new MainGameExitDisplay())
        .addComponent(new MainGameInterface())
        .addComponent(new MainGameBuildingInterface())
        .addComponent(new MainGameNpcInterface())
        .addComponent(new DayNightClockComponent())
        .addComponent(new Terminal())
        .addComponent(new MainGameTutorials())
        .addComponent(new EpilogueLayover())
        .addComponent(new AchievementPopupComponent())
        .addComponent(inputComponent)
        .addComponent(new TerminalDisplay())
        .addComponent(new AchievementInterface())
        .addComponent(new ShopInterface())
        .addComponent(new ArtefactShopDisplay())
        .addComponent(new BuildingShopDisplay())
        .addComponent(new EquipmentsShopDisplay());

    ServiceLocator.getEntityService().registerNamed("ui", ui);

    if (loadGame) {
      ui.getComponent(DayNightClockComponent.class).loadFromSave();
    }
  }
}
