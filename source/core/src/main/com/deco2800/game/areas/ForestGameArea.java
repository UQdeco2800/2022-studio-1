package com.deco2800.game.areas;

import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.deco2800.game.areas.terrain.EnvironmentalCollision;
import com.deco2800.game.areas.terrain.TerrainTile;
import com.deco2800.game.components.player.InventoryComponent;
import com.deco2800.game.components.CombatStatsComponent;
import com.deco2800.game.components.maingame.MainGameActions;
import com.deco2800.game.files.SaveGame;
import com.deco2800.game.rendering.DayNightCycleComponent;
import com.deco2800.game.screens.MainGameScreen;
import com.deco2800.game.services.DayNightCycleService;
import com.deco2800.game.services.DayNightCycleStatus;
import com.deco2800.game.utils.math.RandomUtils;
import com.deco2800.game.entities.factories.*;
import com.deco2800.game.memento.CareTaker;
import com.sun.tools.javac.Main;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.deco2800.game.areas.terrain.TerrainComponent;
import com.deco2800.game.areas.terrain.TerrainFactory;
import com.deco2800.game.areas.terrain.TerrainFactory.TerrainType;
import com.deco2800.game.components.CombatStatsComponent;
import com.deco2800.game.components.Environmental.EnvironmentalComponent;
import com.deco2800.game.components.Environmental.ValueTuple;
import com.deco2800.game.components.Environmental.EnvironmentalComponent.EnvironmentalObstacle;
import com.deco2800.game.entities.Entity;
import com.deco2800.game.components.gamearea.GameAreaDisplay;
import com.deco2800.game.services.ResourceService;
import com.deco2800.game.services.ServiceLocator;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

/** Forest area for the demo game with trees, a player, and some enemies. */
public class ForestGameArea extends GameArea {
  private static final Logger logger = LoggerFactory.getLogger(ForestGameArea.class);
  private static final int NUM_GHOSTS = 2;
  private static final GridPoint2 PLAYER_SPAWN = new GridPoint2(60, 60);
  private static final GridPoint2 STRUCTURE_SPAWN = new GridPoint2(65, 65);
  private static final float WALL_WIDTH = 0.1f;
  private static final int MAX_ENVIRONMENTAL_OBJECTS = 7;
  private static final int MIN_NUM_TREES = 3;
  private static final int MAX_NUM_TREES = 5;
  private static final int MIN_NUM_ROCKS = 2;
  private static final int MAX_NUM_ROCKS = 3;
  private static final int MIN_NUM_CRABS = 1;
  private static final int MAX_NUM_CRABS = 3;
  private static final int MIN_NUM_EELS = 1;
  private static final int MAX_NUM_EELS = 1;
  private static final int BOSS_DAY = 2;
  private static final int MAX_NUM_STARFISH = 3;

  private static final String[] forestTextures = {
      "images/box_boy.png",
      "images/box_boy_leaf.png",
      "images/Centaur_Back_left.png",
      "images/Centaur_Back_right.png",
      "images/Centaur_left.png",
      "images/Centaur_right.png",
      "images/landscape_objects/leftPalmTree.png",
      "images/landscape_objects/rightPalmTree.png",
      "images/landscape_objects/groupPalmTrees.png",
      "images/ghost_king.png",
      "images/500_grassTile.png",
      "images/500_waterFullTile.png",
      "images/500_waterAndDirtFullTile.png",
      "images/waterFinalVersion.png",
      "images/fullSizedDirt.png",
      "images/waterDirtMerged.png",
      "images/trial3GrassTile.png",
      "images/wallTransparent.png",
      "images/landscape_objects/almond-tree-60x62.png",
      "images/landscape_objects/fig-tree-60x62.png",
      "images/landscape_objects/limestone-boulder-60x60.png",
      "images/landscape_objects/marble-stone-60x40.png",
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
      "images/Wall-right.png",
      "images/mini_tower.png",
      "images/trap.png",
      "images/turret.png",
      "images/tower.png",
      "images/65x33_tiles/beachV1.png",
      "images/65x33_tiles/dayWaterTile.png",
      "images/65x33_tiles/groundTileV1.png",
      "images/65x33_tiles/seaweedV4.png",
      "images/65x33_tiles/seaweedV5.png",
      "images/Eel_Bright_SW.png",
      "images/Eel_Bright_NE.png",
      "images/Eel_Bright_NW.png",
      "images/Eel_Bright_SW.png",
      "images/shipRack.png",
      "images/shipRackFront.png",
          "images/TOWER1I.png",
          "images/TOWER1II.png",
          "images/TOWER1III.png",
          "images/TOWER2I.png",
          "images/TOWER2II.png",
          "images/TOWER2III.png",
          "images/TOWER3I.png",
          "images/TOWER3II.png",
          "images/TOWER3III.png",
      "images/shipWreckBack.png",
      "images/shipWreckFront.png",
      "images/ElectricEel.png",
      "images/starfish.png"
  };

  private static final String[] forestTextureAtlases = {
          "images/terrain_iso_grass.atlas", "images/ghost.atlas", "images/ghostKing.atlas", "images/eel_animations/eel.atlas"
  };

  // Sound effect files
  private static final String[] soundEffects = {
      "sounds/sword_swing.mp3", "sounds/footsteps_grass_single.mp3", "sounds/hurt.mp3"
  };
  // Music files
  private static final String backgroundMusic = "sounds/bgm_dusk.mp3";
  private static final String backgroundSounds = "sounds/BgCricket.mp3";
  private static final String[] forestMusic = { backgroundMusic, backgroundSounds };
  // private EnvironmentalCollision entityMapping;

  // private EnvironmentalCollision entityMapping;

  private final TerrainFactory terrainFactory;
  private Entity player;
  private Entity crystal;
  private int dayNum = 1;

  public ForestGameArea(TerrainFactory terrainFactory) {
    super();
    this.terrainFactory = terrainFactory;

    ServiceLocator.getDayNightCycleService().getEvents().addListener(DayNightCycleService.EVENT_DAY_PASSED,
        this::dayChange);
    ServiceLocator.getDayNightCycleService().getEvents().addListener(DayNightCycleService.EVENT_PART_OF_DAY_PASSED,
        this::spawnSetEnemies);

  }

  /**
   * Create the game area, including terrain, static entities (trees), dynamic
   * entities (player)
   */
  @Override
  public void create() {




    loadAssets();
    ServiceLocator.getGameService().setUpEntities(120);

    displayUI();

    spawnTerrain();

    entityMapping = new EnvironmentalCollision(terrain);

    // EntityMapping must be made AFTER spawn Terrain and BEFORE any environmental
    // objects are created

    this.crystal = spawnCrystal(terrainFactory.getMapSize().x / 2, terrainFactory.getMapSize().y / 2);

    this.player = spawnPlayer();

    spawnEnvironmentalObjects();

    playMusic();
    SaveGame.saveGameState(1);
  }

  private void displayUI() {
    Entity ui = new Entity();
    ui.addComponent(new GameAreaDisplay("Box Forest"));
    spawnEntity(ui);
  }

  @Override
  public Entity getPlayer() {
    return this.player;
  }

  private void spawnTerrain() {
    // Background terrain
    terrain = terrainFactory.createTerrain(TerrainType.FOREST_DEMO_ISO);

    Entity terrainEntity = new Entity().addComponent(terrain);
    this.areaEntities.add(terrainEntity);
    ServiceLocator.getEntityService().registerNamed("terrain", terrainEntity);
    ServiceLocator.getEntityService().addEntity(terrainEntity);

    GridPoint2 tileBounds = terrain.getMapBounds(0);
    // spawnWorldBorders();
  }

  private void spawnWorldBorders() {
    ArrayList enemySpawnPos = new ArrayList<GridPoint2>();

    GridPoint2 mapSize = terrainFactory.getMapSize();

    TiledMapTileLayer tiledMapTileLayer = terrain.getTileMapTileLayer(0);

    for (int x = 1; x < mapSize.x - 1; x++) {
      for (int y = 1; y < mapSize.y - 1; y++) {
        TerrainTile tile = (TerrainTile) tiledMapTileLayer.getCell(x, y).getTile();

        TerrainTile above = (TerrainTile) tiledMapTileLayer.getCell(x, y + 1).getTile();
        TerrainTile below = (TerrainTile) tiledMapTileLayer.getCell(x, y - 1).getTile();
        TerrainTile left = (TerrainTile) tiledMapTileLayer.getCell(x - 1, y).getTile();
        TerrainTile right = (TerrainTile) tiledMapTileLayer.getCell(x + 1, y).getTile();
        TerrainTile rightAbove = (TerrainTile) tiledMapTileLayer.getCell(x + 1, y + 1).getTile();
        TerrainTile rightBelow = (TerrainTile) tiledMapTileLayer.getCell(x + 1, y - 1).getTile();
        TerrainTile leftAbove = (TerrainTile) tiledMapTileLayer.getCell(x - 1, y + 1).getTile();
        TerrainTile leftBelow = (TerrainTile) tiledMapTileLayer.getCell(x - 1, y - 1).getTile();

        // spawns walls and sets enemy spawn locations behind borders
        if (tile.getName().equals("sand") || tile.getName().equals("grass")) {
          if (above.getName().equals("water")) {
            createBorderWall(x, y + 1);
          }
          if (below.getName().equals("water")) {
            createBorderWall(x, y - 1);
          }
          if (left.getName().equals("water")) {
            createBorderWall(x - 1, y);
          }
          if (right.getName().equals("water")) {
            createBorderWall(x + 1, y);
          }
          if (rightAbove.getName().equals("water")) {
            createBorderWall(x + 1, y + 1);
          }
          if (rightBelow.getName().equals("water")) {
            createBorderWall(x + 1, y - 1);
          }
          if (leftAbove.getName().equals("water")) {
            createBorderWall(x - 1, y + 1);
          }
          if (leftBelow.getName().equals("water")) {
            createBorderWall(x - 1, y + 1);
          }
        }
      }
    }
  }

  private void createBorderWall(int x, int y) {
    System.out.printf("Spawning word border\n");
    GridPoint2 pos = new GridPoint2(x, y);
    Entity wall = ObstacleFactory.createWall(0.1f, 0.1f);
    if (isWallHere(pos)) {
      return;
    }
    spawnEntityAt(wall, new GridPoint2(x, y), false, true);
  }

  /**
   * spawns environmental objects based off semi-random bounds
   * 
   * @param numObjects the number of objects to be spawned
   * @param type       the type of object, from
   *                   EnvironmentalComponent.EnvironmentalType enum
   */
  private void spawnEnvironmentalObject(int numObjects, EnvironmentalComponent.EnvironmentalObstacle type) {

    for (int i = 0; i < numObjects; i++) {
      // Must be maxPos, minPos NOT minPos, maxPos
      GridPoint2 randomPos = terrain.getLandTiles().get(MathUtils.random(0, terrain.getLandTiles().size() - 1));
      Entity envObj;
      switch (type) {
        case TREE:
          envObj = ObstacleFactory.createTree();
          break;
        case VINE:
          envObj = ObstacleFactory.createVine();
          break;
        case SPIKY_BUSH:
          envObj = ObstacleFactory.createSpikyTree();
          break;
        case SPEED_ARTEFACT:
          envObj = ObstacleFactory.createAoeSpeedArtefact();
          break;
        case KNOCKBACK_TOWER:
          envObj = ObstacleFactory.createBillboard();
          break;
        case STONE_PILLAR:
          envObj = ObstacleFactory.createPillar();
          break;
        case GEYSER:
          envObj = ObstacleFactory.createGeyser();
          break;
        case WOODEN_FENCE:
          envObj = ObstacleFactory.createWoodenFence();
          break;
        case SHIPWRECK_BACK:
          envObj = ObstacleFactory.createShipwreckBack();
          break;
        case SHIPWRECK_FRONT:
          envObj = ObstacleFactory.createShipwreckFront();
          break;
        case SHELL:
          envObj = ObstacleFactory.createShell();
          break;
        case ROCK:
          // falls through to default
        default:
          envObj = ObstacleFactory.createRock();
      }

      int counter = 0;
      // check for possible collision and reroll location until valid
      while (this.entityMapping.wouldCollide(envObj, randomPos.x, randomPos.y)
          || entityMapping.isNearWater(randomPos.x, randomPos.y)) {
        randomPos = terrain.getLandTiles().get(MathUtils.random(0, terrain.getLandTiles().size() - 1));

        // safety to avoid infinite looping on loading screen.
        // If cant spawn the object then space has ran out on map
        if (counter > 1000) {
          return;
        }
        counter++;
      }
      this.entityMapping.addEntity(envObj);

      spawnEntityAt(envObj, randomPos, true, true);
    }
  }

  /**
   * Generate the environment objects. This is responsible for rocks, trees and
   * other related Environmental Types.
   * Object numbers must fall within set bounds.
   */
  private void spawnEnvironmentalObjects() {
    spawnEnvironmentalObject(1, EnvironmentalComponent.EnvironmentalObstacle.SHIPWRECK_BACK);
    spawnEnvironmentalObject(1, EnvironmentalComponent.EnvironmentalObstacle.SHIPWRECK_FRONT);
    spawnEnvironmentalObject(3, EnvironmentalComponent.EnvironmentalObstacle.TREE);
    spawnEnvironmentalObject(1, EnvironmentalComponent.EnvironmentalObstacle.SPEED_ARTEFACT);
    spawnEnvironmentalObject(1, EnvironmentalComponent.EnvironmentalObstacle.STONE_PILLAR);
    spawnEnvironmentalObject(3, EnvironmentalComponent.EnvironmentalObstacle.SHELL);
    /*
     * // semi random rocks and trees
     * int numTrees = MIN_NUM_TREES + (int) (Math.random() * ((MAX_NUM_TREES -
     * MIN_NUM_TREES) + 1));
     * spawnEnvironmentalObject(numTrees,
     * EnvironmentalComponent.EnvironmentalObstacle.TREE);
     * int objectsRemaining = MAX_ENVIRONMENTAL_OBJECTS - numTrees;
     * 
     * int numRocks = MIN_NUM_ROCKS + (int) (Math.random() * ((MAX_NUM_ROCKS -
     * MIN_NUM_ROCKS) + 1));
     * spawnEnvironmentalObject(numRocks,
     * EnvironmentalComponent.EnvironmentalObstacle.ROCK);
     * objectsRemaining = MAX_ENVIRONMENTAL_OBJECTS - numRocks;
     * 
     * 
     * 
     * // Remaining number of objects can be spawned off raw percentage?
     * // placeholder functions below:
     * int numVines = 4;
     * spawnEnvironmentalObject(numVines,
     * EnvironmentalComponent.EnvironmentalObstacle.VINE);
     * objectsRemaining = objectsRemaining - numVines;
     * 
     * spawnEnvironmentalObject(1,
     * EnvironmentalComponent.EnvironmentalObstacle.KNOCKBACK_TOWER);
     * 
     * 
     * spawnEnvironmentalObject(2,
     * EnvironmentalComponent.EnvironmentalObstacle.SPIKY_BUSH);
     * spawnEnvironmentalObject(1,
     * EnvironmentalComponent.EnvironmentalObstacle.GEYSER);
     * spawnEnvironmentalObject(1,
     * EnvironmentalComponent.EnvironmentalObstacle.WOODEN_FENCE);
     */
  }

  /**
   * removes an entity at a specific tile coordinate
   * goes through areaEntities to find entity in that position
   * check if entity is an environment object
   * put inside separate list first to avoid ConcurrentModificationException
   * 
   * @param removeTile The tile where environment entities is removed
   * @return a tuple containing resource type and its value
   */
  public ValueTuple<EnvironmentalComponent.ResourceTypes, Integer> removeEnvironmentalObject(GridPoint2 removeTile) {
    Vector2 removeLoc = terrain.tileToWorldPosition(removeTile);
    List<Entity> found = new ArrayList<Entity>();
    ValueTuple<EnvironmentalComponent.ResourceTypes, Integer> values = new ValueTuple<>(
        EnvironmentalComponent.ResourceTypes.NONE, 0);
    for (Entity entity : this.areaEntities) {
      if (entity.getPosition() == removeLoc &&
          entity.getComponent(EnvironmentalComponent.class) != null) {
        found.add(entity);
        values = new ValueTuple<>(
            entity.getComponent(EnvironmentalComponent.class).getType(),
            entity.getComponent(EnvironmentalComponent.class).getResourceAmount());
      }
    }
    this.areaEntities.removeAll(found);
    for (Entity entity : found) {
      entity.dispose();
    }
    return values;
  }

  private Entity spawnPlayer() {
    Entity newPlayer = PlayerFactory.loadPlayer();
    ServiceLocator.getEntityService().registerNamed("player", newPlayer);

    spawnEntityAt(newPlayer, PLAYER_SPAWN, true, true);
    return newPlayer;
  }

  private Entity spawnCrystal(int x_pos, int y_pos) {
    Entity crystal = CrystalFactory.createCrystal("images/crystal.png", "crystal");
    while (this.entityMapping.wouldCollide(crystal, x_pos, y_pos)) {
      x_pos++;
    }
    System.out.println("Crystal Position: " + x_pos + " " + y_pos);
    crystal.setPosition(terrain.tileToWorldPosition(x_pos, y_pos));
    ServiceLocator.getEntityService().addEntity(crystal);
    this.entityMapping.addEntity(crystal);

    return crystal;
  }

  private void dayChange(int dayNum) {
    this.dayNum = dayNum;
    System.out.println("DayNum is" + this.dayNum);

    int crystalHealth = crystal.getComponent(CombatStatsComponent.class).getHealth();

    if (crystalHealth < 500) {
      if (terrain.getCurrentMapLvl() == 0) {
        // GAME OVER
      } else {
        terrain.decrementMapLvl();
      }
    }

  }

  /**
   * Spawns crabs at certain part of the day
   */
  private void spawnSetEnemies(DayNightCycleStatus partOfDay) {
    switch (partOfDay) {
      case DAWN:
        break;
      case DAY:
        break;
      case DUSK:
        break;
      case NIGHT:
        for (int i = 0; i < MathUtils.random(MIN_NUM_CRABS, MAX_NUM_CRABS); i++) {
          spawnPirateCrabEnemy();
        }
        for (int i = 0; i < MathUtils.random(MIN_NUM_EELS, MAX_NUM_EELS); i++) {
          spawnElectricEelEnemy();
        }
        if (dayNum == BOSS_DAY) {
          spawnMeleeBoss();
        }
        break;
    }
  }

  /**
   * Spawn the boss
   */
  private void spawnMeleeBoss() {
    Entity boss = NPCFactory.createMeleeBoss(player);
    boss.setName("Mr. Zero");
    spawnEnemy(boss);
  }

  public int count = 0;

  /**
   * Spawns a Pirate Crab entity at a randomised position within the game world
   */
  private void spawnPirateCrabEnemy() {
    Entity pirateCrabEnemy = NPCFactory.createPirateCrabEnemy(crystal);
    pirateCrabEnemy.setName("Mr. Crabs");
    this.entityMapping.addEntity(pirateCrabEnemy);
    spawnEnemy(pirateCrabEnemy);
  }

  /**
   * Spawns an enemy on the map at a random position surrounding the island
   * 
   * @param entity the entity to spawn
   */
  private void spawnEnemy(Entity entity) {
    ServiceLocator.getEntityService().registerNamed("Enemy@" + entity.getId(), entity);
    GridPoint2 randomPos = terrainFactory.getSpawnableTiles(terrain.getCurrentMapLvl())
        .get(MathUtils.random(0, terrainFactory.getSpawnableTiles(terrain.getCurrentMapLvl()).size() - 1));

    spawnEntityAt(entity, randomPos, true, true);
  }

  private void spawnElectricEelEnemy() {
    Entity ElectricEelEnemy = NPCFactory.createElectricEelEnemy(player, crystal);
    ElectricEelEnemy.setName("Mr. Electricity");
    this.entityMapping.addEntity(ElectricEelEnemy);
    spawnEnemy(ElectricEelEnemy);
  }

  // Spawn the starfish as ranged enemy
  private void spawnNinjaStarfish() {
    Entity ninjaStarfish = NPCFactory.createStarFish(player, crystal);
    int waterWidth = (terrain.getMapBounds(0).x - terrainFactory.getIslandSize().x) / 2;

    //Get the position from 2D coordinates
    GridPoint2 minPos = new GridPoint2(waterWidth + 2, waterWidth + 2);
    GridPoint2 maxPos = new GridPoint2(terrainFactory.getIslandSize().x + waterWidth - 4,
        terrainFactory.getIslandSize().x + waterWidth - 4);
    GridPoint2 randomPos = RandomUtils.random(minPos, maxPos);

    // Create the starfish entity
    // Check if the day night cycle has started
    while (!ServiceLocator.getDayNightCycleService().hasStarted()) {
      // Check the current status is night
      if (!ServiceLocator.getDayNightCycleService().getCurrentCycleStatus().equals(DayNightCycleStatus.NIGHT)) {
        spawnEntityAt(ninjaStarfish, randomPos, true, true);
        break;
      } else {
        // Remove ninja starfish in other situations
        removeEntity(ninjaStarfish);
        // Restart the while loop again
        spawnNinjaStarfish();
      }
    }
    // Register ninja starfish in the world
    ServiceLocator.getEntityService().addEntity(ninjaStarfish);
    ServiceLocator.getEntityService().register(ninjaStarfish);
  }

  private void playMusic() {
    // Background Music
    Music music = ServiceLocator.getResourceService().getAsset(backgroundMusic, Music.class);
    music.setLooping(true);
    music.setVolume(0.3f);
    music.play();

    // Background Ambience
    Music ambience = ServiceLocator.getResourceService().getAsset(backgroundSounds, Music.class);
    ambience.setLooping(true);
    ambience.setVolume(0.1f);
    ambience.play();
  }

  private void loadAssets() {
    logger.debug("Loading assets");
    ResourceService resourceService = ServiceLocator.getResourceService();
    resourceService.loadTextures(forestTextures);
    resourceService.loadTextureAtlases(forestTextureAtlases);
    resourceService.loadSounds(soundEffects);
    resourceService.loadMusic(forestMusic);

    while (!resourceService.loadForMillis(10)) {
      // This could be upgraded to a loading screen
      logger.info("Loading... {}%", resourceService.getProgress());
    }
  }

  private void unloadAssets() {
    logger.debug("Unloading assets");
    ResourceService resourceService = ServiceLocator.getResourceService();
    resourceService.unloadAssets(forestTextures);
    resourceService.unloadAssets(forestTextureAtlases);
    resourceService.unloadAssets(soundEffects);
    resourceService.unloadAssets(forestMusic);
  }

  @Override
  public void dispose() {
    super.dispose();
    ServiceLocator.getResourceService().getAsset(backgroundMusic, Music.class).stop();
    ServiceLocator.getResourceService().getAsset(backgroundSounds, Music.class).stop();
    this.unloadAssets();
  }

  public EnvironmentalCollision getEntityMapping() {
    return entityMapping;
  }
}