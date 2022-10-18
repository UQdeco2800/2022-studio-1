package com.deco2800.game.screens;

import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.deco2800.game.AtlantisSinks;
import com.deco2800.game.components.mainmenu.MainMenuActions;
import com.deco2800.game.components.mainmenu.MainMenuDisplay;
import com.deco2800.game.components.mainmenu.loader;
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
  private Table loadTable;
  private float time;
  private boolean loadAssetsSwitch = true;
  private boolean loadComplete = false;

  private static String[] mainMenuScreenTextures = {
          "images/uiElements/exports/title.png",
          "loadingAssets/loading_screen.png",
          "loadingAssets/load_frame.png",
          "loadingAssets/load_bar.png"
  };
  private static String[] mainMenuTextures = {
          "images/uiElements/exports/title.png",
          "images/Centaur_Back_left.png",
          "images/Centaur_Back_right.png",
          "images/Centaur_left.png",
          "images/Centaur_right.png",
          "images/landscape_objects/leftPalmTree.png",
          "images/landscape_objects/rightPalmTree.png",
          "images/landscape_objects/groupPalmTrees.png",
          "images/landscape_objects/almond-tree-60x62.png",
          "images/landscape_objects/fig-tree-60x62.png",
          "images/landscape_objects/vines.png",
          "images/landscape_objects/cypress-tree-60x100.png",
          "images/landscape_objects/geyser.png",
          "images/boss_enemy_angle1.png",
          "images/landscape_objects/billboard.png",
          "images/landscape_objects/chalice.png",
          "images/landscape_objects/pillar.png",
          "images/landscape_objects/wooden-fence-60x60.png",
          "images/65x33_tiles/shell.png",
          "images/pirate_crab_NE.png",
          "images/pirate_crab_NW.png",
          "images/pirate_crab_SE.png",
          "images/pirate_crab_SW.png",
          "images/crystal.png",
          "images/crystal_level2.png",
          "images/crystal_level3.png",
          "images/65x33_tiles/sand.png",
          "images/65x33_tiles/sand_night.png",
          "images/65x33_tiles/seaweed_1.png",
          "images/65x33_tiles/seaweed_1_night.png",
          "images/65x33_tiles/seaweed_2.png",
          "images/65x33_tiles/seaweed_2_night.png",
          "images/65x33_tiles/seaweed_3.png",
          "images/65x33_tiles/seaweed_3_night.png",
          "images/65x33_tiles/shorelineBottom.png",
          "images/65x33_tiles/shorelineTop.png",
          "images/65x33_tiles/shorelineBottomRight.png",
          "images/65x33_tiles/shorelineBottomLeft.png",
          "images/65x33_tiles/shorelineTopRight.png",
          "images/65x33_tiles/shorelineTopLeft.png",
          "images/65x33_tiles/shorelineLeft.png",
          "images/65x33_tiles/shorelineRight.png",
          "images/65x33_tiles/shorelineBottom_night.png",
          "images/65x33_tiles/shorelineTop_night.png",
          "images/65x33_tiles/shorelineBottomRight_night.png",
          "images/65x33_tiles/shorelineBottomLeft_night.png",
          "images/65x33_tiles/shorelineTopRight_night.png",
          "images/65x33_tiles/shorelineTopLeft_night.png",
          "images/65x33_tiles/shorelineLeft_night.png",
          "images/65x33_tiles/shorelineRight_night.png",
          "images/65x33_tiles/water0.png",
          "images/65x33_tiles/water1.png",
          "images/65x33_tiles/water2.png",
          "images/65x33_tiles/water3.png",
          "images/65x33_tiles/water_night0.png",
          "images/65x33_tiles/water_night1.png",
          "images/65x33_tiles/water_night2.png",
          "images/65x33_tiles/water_night3.png",
          "images/65x33_tiles/invalidTile.png",
          "images/65x33_tiles/validTile.png",
          "images/seastack1.png",
          "images/seastack2.png",
          "images/Eel_Bright_SW.png",
          "images/Eel_Bright_NE.png",
          "images/Eel_Bright_NW.png",
          "images/Eel_Bright_SW.png",
          "images/shipRack.png",
          "images/shipRackFront.png",
          "images/shipWreckBack.png",
          "images/shipWreckFront.png",
          "images/ElectricEel.png",
          "images/eel_projectile.png",
          "images/starfish.png",
          "images/NpcPlaceholder.png",
          "images/NPC convo.png",
          "images/npc1.png",
          "images/npcs/NPC-V2.2.png",
          "images/npcs/NPC-V2.1.png",
          "images/npcs/npc_blacksmith_draft",
          "images/guardianLegacy1left.png",
          "images/guardianLegacy1right.png",
          "images/cornerWall1.png",
          "images/cornerWall2.png",
          "images/cornerWall3.png",
          "images/cornerWall4.png",
          "images/wallRight.png",
          "images/wallLeft.png",
          "images/turret.png",
          "images/attack_towers/lv1GuardianLeft.png",
          "images/attack_towers/animations/towerLevel2.png",
          "images.attack_towers/lv1GuardianRight.png",
          "images/uiElements/exports/heart.png",
          "images/uiElements/exports/coin.png",
          "images/healthBar.png",
          "images/empty_healthbar.png",
          "images/uiElements/exports/crystal.png",
          "images/icon_stone.png",
          "images/atlantisBasicBackground.png",
          "images/icon_wood.png",
          "images/clock_sprites/clock_day1_1.png",
          "images/clock_sprites/clock_day1_2.png",
          "images/clock_sprites/clock_day1_3.png",
          "images/clock_sprites/clock_day1_4.png",
          "images/clock_sprites/clock_day1_5.png",
          "images/clock_sprites/clock_day1_6.png",
          "images/clock_sprites/clock_day1_7.png",
          "images/clock_sprites/clock_day1_8.png",
          "images/clock_sprites/clock_day2_1.png",
          "images/clock_sprites/clock_day2_2.png",
          "images/clock_sprites/clock_day2_3.png",
          "images/clock_sprites/clock_day2_4.png",
          "images/clock_sprites/clock_day2_5.png",
          "images/clock_sprites/clock_day2_6.png",
          "images/clock_sprites/clock_day2_7.png",
          "images/clock_sprites/clock_day2_8.png",
          "images/clock_sprites/clock_day3_1.png",
          "images/clock_sprites/clock_day3_2.png",
          "images/clock_sprites/clock_day3_3.png",
          "images/clock_sprites/clock_day3_4.png",
          "images/clock_sprites/clock_day3_5.png",
          "images/clock_sprites/clock_day3_6.png",
          "images/clock_sprites/clock_day3_7.png",
          "images/clock_sprites/clock_day3_8.png",
          "images/clock_sprites/clock_day4_1.png",
          "images/clock_sprites/clock_day4_2.png",
          "images/clock_sprites/clock_day4_3.png",
          "images/clock_sprites/clock_day4_4.png",
          "images/clock_sprites/clock_day4_5.png",
          "images/clock_sprites/clock_day4_6.png",
          "images/clock_sprites/clock_boss.png",
          "images/anim_demo/woodresourcebuilding.png",
          "images/storyLine/skipButton.png",
          "images/storyLine/textBox.png",
          "images/crystalhealth3.png",
          "images/crystalhealth4.png",
          "images/crystalIcon.png",
          "images/upgrade500.2.png",
          "images/upgrade1500.2.png",
          "images/attack_towers/tow1_1_l.png",
          "images/upgradeFail500.png",
          "images/upgradeFail1500.png",
          "images/tutorials/crystalLevelPopUp.png",
          "images/crystal2.0.png",
          "images/crystal1.png",
          "images/crystal2.png",
          "images/crystal3.png",
          "images/attack_towers/tow1_1_r.png",
          "images/attack_towers/tow1_2_l.png",
          "images/attack_towers/tow1_2_r.png",
          "images/attack_towers/tow1_3_l.png",
          "images/attack_towers/tow1_3_r.png",
          "images/attack_towers/tempStructures/temp_tow1_1_l.png",
          "images/attack_towers/tempStructures/temp_tow1_1_r.png",
          "images/attack_towers/elecball.png",
          "images/65x33_tiles/wall_left.png",
          "images/65x33_tiles/wall_right.png",
          "images/65x33_tiles/temp_wall_left.png",
          "images/65x33_tiles/temp_wall_right.png",
          "images/attack_towers/tempStructures/temp_tower3lv1Left.png",
          "images/attack_towers/tempStructures/temp_tower3lv1Right.png",
          "images/attack_towers/tower3lv1Left.png",
          "images/attack_towers/tower3lv1Right.png",
          "images/attack_towers/tower3lv2Left.png",
          "images/attack_towers/tower3lv2Right.png",
          "images/attack_towers/tower3lv3Left.png",
          "images/attack_towers/tower3lv3Right.png",
          "images/attack_towers/Attack_Structure2_lev1.png",
          "images/attack_towers/Attack_Structure2_lev2.png",
          "images/attack_towers/Attack_Structure2_lev3.png",
          "images/attack_towers/tempStructures/temp_Attack_Structure2_lev1.png",
          "images/attack_towers/tempStructures/temp_Attack_Structure2_lev1.png",

  };

  private static final String[] forestTextureAtlases = {
          "images/terrain_iso_grass.atlas",
          "images/ghost.atlas", "images/ghostKing.atlas",
          "images/eel_animations/eel.atlas",
          "images/eel_animations/eel.atlas",
          "images/starfish_animation/starfish.atlas",
          "images/final_boss_animations/final_boss.atlas",
          "images/crab_animations/crab_animation.atlas",
          "images/npc_animations/NPC1sprite.atlas",
          "images/npc_animations/npc.atlas",
          "images/crystal_animation/crystal_damaged.atlas",
          "images/crystal_animation/p_crystal_damaged.atlas",
          "images/attack_towers/animations/towerLevel2.atlas"
  };

  // Sound effect files
  private static final String[] soundEffects = {
          "sounds/sword_swing.mp3", "sounds/footsteps_grass_single.mp3", "sounds/hurt.mp3"
  };
  // Music files
  private static final String backgroundMusic = "sounds/bgm_dusk.mp3";
  private static final String backgroundSounds = "sounds/BgCricket.mp3";
  private static final String shopMusic = "sounds/shopping_backgroundmusic-V1.mp3";
  private static final String guideMusic = "sounds/guidebookMusic.mp3";
  private static final String[] shopPopUpMusic = { shopMusic, guideMusic };
  private static final String[] forestMusic = { backgroundMusic, backgroundSounds };

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

    loadScreenAsset();
    createUI();
  }

  @Override
  public void render(float delta) {
    this.time += delta;

    if (this.time > 0.4f && loadAssetsSwitch) {
      ServiceLocator.getEntityService().getNamedEntity("menu").getComponent(MainMenuDisplay.class).nextFrame();
      loadTable = ServiceLocator.getEntityService().getNamedEntity("menu").getComponent(loader.class)
              .getLoadDisplay();
      ServiceLocator.getEntityService().getNamedEntity("menu").getComponent(loader.class).loadUpdate();
      this.time = delta;
      loadAssetsSwitch = false;
      loadAssets();
    }

    if (this.time > 0.4f && !loadComplete) {
      AssetManager assetManager = ServiceLocator.getResourceService().getAssetManager();
      float currProgress = assetManager.getProgress() * 100;
      logger.info("Loading... {}%", currProgress);
      ServiceLocator.getEntityService().getNamedEntity("menu").getEvents()
              .trigger("loadStatUpdate", currProgress);
      if (assetManager.isFinished()) {
        loadComplete = true;
      } else {
        try {
          assetManager.update(1);
        } catch (Exception e) {
          logger.error(e.getMessage());
        }
      }
    }

    if (this.time > 0.4f && loadComplete) {

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
    if (!loadAssetsSwitch) {
      rootTable = ServiceLocator.getEntityService().getNamedEntity("menu").getComponent(MainMenuDisplay.class).display();
    } else {
      rootTable = ServiceLocator.getEntityService().getNamedEntity("menu").getComponent(loader.class).displayLoadScreen();
    }
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
  }
  private void loadScreenAsset() {
    logger.debug("Loading assets");
    ResourceService resourceService = ServiceLocator.getResourceService();
    resourceService.loadTextures(mainMenuScreenTextures);
    ServiceLocator.getResourceService().loadAll();
  }

  private void loadAssets() {
    logger.debug("Loading assets");
    ResourceService resourceService = ServiceLocator.getResourceService();
    resourceService.loadTextures(mainMenuTextures);
    ServiceLocator.getResourceService().loadAll();
    resourceService.loadTextureAtlases(forestTextureAtlases);
    resourceService.loadSounds(soundEffects);
    resourceService.loadMusic(forestMusic);
    resourceService.loadMusic(shopPopUpMusic);

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
        .addComponent(new loader())
        .addComponent(new InputDecorator(stage, 10))
        .addComponent(new MainMenuActions(game));
    ServiceLocator.getEntityService().registerNamed("menu", ui);
    rootTable = ServiceLocator.getEntityService().getNamedEntity("menu").getComponent(MainMenuDisplay.class)
        .getDisplay();
    loadTable = ServiceLocator.getEntityService().getNamedEntity("menu").getComponent(loader.class)
        .getLoadDisplay();
  }
}
