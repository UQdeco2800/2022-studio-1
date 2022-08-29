package com.deco2800.game.areas;

import com.deco2800.game.entities.factories.StructureFactory;
import com.deco2800.game.utils.math.RandomUtils;
import com.deco2800.game.components.CombatStatsComponent;
import com.deco2800.game.entities.factories.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.Vector2;
import com.deco2800.game.areas.terrain.TerrainFactory;
import com.deco2800.game.areas.terrain.TerrainFactory.TerrainType;
import com.deco2800.game.components.Environmental.EnvironmentalComponent;
import com.deco2800.game.components.Environmental.ValueTuple;
import com.deco2800.game.entities.Entity;
import com.deco2800.game.areas.terrain.EnvironmentalCollision;
import com.deco2800.game.components.gamearea.GameAreaDisplay;

import com.deco2800.game.services.ResourceService;
import com.deco2800.game.services.ServiceLocator;

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

  private static final String[] forestTextures = {
      "images/box_boy_leaf.png",
      "images/tree.png",
      "images/ghost_king.png",
      "images/ghost_1.png",
      "images/grass_1.png",
      "images/grass_2.png",
      "images/grass_3.png",
      "images/hex_grass_1.png",
      "images/hex_grass_2.png",
      "images/hex_grass_3.png",
      "images/iso_grass_1.png",
      "images/iso_grass_2.png",
      "images/iso_grass_3.png",
      "images/water version 2.png",
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
      "images/landscape_objects/billboard.png",
      "images/landscape_objects/chalice.png",
      "images/landscape_objects/pillar.png",
      "images/landscape_objects/wooden-fence-60x60.png",
      "images/pirate_crab_NE.png",
      "images/pirate_crab_NW.png",
      "images/pirate_crab_SE.png",
      "images/pirate_crab_SW.png",
      "images/crystal.png",
      "images/stoneQuarryTest.png"
  };

  private static final String[] forestTextureAtlases = {
      "images/terrain_iso_grass.atlas", "images/ghost.atlas", "images/ghostKing.atlas"
  };
  private static final String[] forestSounds = { "sounds/sword_swing.mp3" };
  public static final String[] walkSound = { "sounds/footsteps_grass_single.mp3" };
  private static final String backgroundMusic = "sounds/bgm_dusk.mp3";

  private static final String[] forestMusic = { backgroundMusic };
  private EnvironmentalCollision entityMapping;

  private final TerrainFactory terrainFactory;

  private Entity player;
  private Entity crystal;

  public ForestGameArea(TerrainFactory terrainFactory) {
    super();
    this.terrainFactory = terrainFactory;
  }

  /**
   * Create the game area, including terrain, static entities (trees), dynamic
   * entities (player)
   */
  @Override
  public void create() {
    loadAssets();

    displayUI();

    spawnTerrain();

    // EntityMapping must be made AFTER spawn Terrain and BEFORE any environmental
    // objects are created
    this.entityMapping = new EnvironmentalCollision(terrain);

    crystal = spawnCrystal(59, 59);

    player = spawnPlayer();

    spawnPirateCrabEnemy();

    spawnEnvironmentalObjects();

    playMusic();
  }

  private void displayUI() {
    Entity ui = new Entity();
    ui.addComponent(new GameAreaDisplay("Box Forest"));
    spawnEntity(ui);
  }

  private void spawnTerrain() {
    // Background terrain
    terrain = terrainFactory.createTerrain(TerrainType.FOREST_DEMO_ISO);
    spawnEntity(new Entity().addComponent(terrain));

    // Terrain walls
    float tileSize = terrain.getTileSize();
    System.out.println(tileSize);
    GridPoint2 tileBounds = terrain.getMapBounds(0);
    Vector2 worldBounds = new Vector2(tileBounds.x * tileSize, tileBounds.y * tileSize);

    spawnWorldBorders(worldBounds, tileBounds);
  }

  private void spawnWorldBorders(Vector2 worldBounds, GridPoint2 tileBounds) {
    /*
     * Entity leftWall = ObstacleFactory.createWall(15.5f, 0.5f);
     * spawnEntityAt(leftWall, new GridPoint2(45, 45), false, false);
     */
  }

  /**
   * spawns environmental objects based off semi-random bounds
   * 
   * @param numObjects the number of objects to be spawned
   * @param type       the type of object, from
   *                   EnvironmentalComponent.EnvironmentalType enum
   */
  private void spawnEnvironmentalObject(int numObjects, EnvironmentalComponent.EnvironmentalObstacle type) {
    GridPoint2 minPos = new GridPoint2(46, 48);
    GridPoint2 maxPos = terrain.getMapBounds(0);

    for (int i = 0; i < numObjects; i++) {
      GridPoint2 randomPos = RandomUtils.random(minPos, maxPos);
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
          minPos = new GridPoint2(50, 50);
          maxPos = new GridPoint2(60, 60);
          envObj = ObstacleFactory.createBillboard();
          break;
        case STONE_PILLAR:
          minPos = new GridPoint2(50, 50);
          maxPos = new GridPoint2(65, 65);
          envObj = ObstacleFactory.createPillar();
          break;
        case GEYSER:
          envObj = ObstacleFactory.createGeyser();
          break;
        case WOODEN_FENCE:
          envObj = ObstacleFactory.createWoodenFence();
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
        randomPos = RandomUtils.random(minPos, maxPos);

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
    spawnEnvironmentalObject(1, EnvironmentalComponent.EnvironmentalObstacle.STONE_PILLAR);

    // semi random rocks and trees
    int numTrees = MIN_NUM_TREES + (int) (Math.random() * ((MAX_NUM_TREES - MIN_NUM_TREES) + 1));
    spawnEnvironmentalObject(numTrees, EnvironmentalComponent.EnvironmentalObstacle.TREE);
    int objectsRemaining = MAX_ENVIRONMENTAL_OBJECTS - numTrees;

    int numRocks = MIN_NUM_ROCKS + (int) (Math.random() * ((MAX_NUM_ROCKS - MIN_NUM_ROCKS) + 1));
    spawnEnvironmentalObject(numTrees, EnvironmentalComponent.EnvironmentalObstacle.ROCK);
    objectsRemaining = MAX_ENVIRONMENTAL_OBJECTS - numRocks;

    // Remaining number of objects can be spawned off raw percentage?
    // placeholder functions below:
    int numVines = objectsRemaining;
    spawnEnvironmentalObject(numVines, EnvironmentalComponent.EnvironmentalObstacle.VINE);
    objectsRemaining = objectsRemaining - numVines;

    spawnEnvironmentalObject(1, EnvironmentalComponent.EnvironmentalObstacle.KNOCKBACK_TOWER);
    spawnEnvironmentalObject(1, EnvironmentalComponent.EnvironmentalObstacle.SPEED_ARTEFACT);
    spawnEnvironmentalObject(2, EnvironmentalComponent.EnvironmentalObstacle.SPIKY_BUSH);
    spawnEnvironmentalObject(1, EnvironmentalComponent.EnvironmentalObstacle.GEYSER);
    spawnEnvironmentalObject(1, EnvironmentalComponent.EnvironmentalObstacle.WOODEN_FENCE);
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
    Entity newPlayer = PlayerFactory.createPlayer();
    spawnEntityAt(newPlayer, PLAYER_SPAWN, true, true);
    return newPlayer;
  }

  private Entity spawnCrystal(int x_pos, int y_pos) {
    Entity crystal = CrystalFactory.createCrystal();
    while (this.entityMapping.wouldCollide(crystal, x_pos, y_pos)) {
      x_pos++;
    }
    this.entityMapping.addEntity(crystal);
    spawnEntityAt(crystal, new GridPoint2(x_pos, y_pos), true, true);
    return crystal;
  }

  /**
   * Spawns a Pirate Crab entity at a randomised position within the game world
   */
  private void spawnPirateCrabEnemy() {
    Entity pirateCrabEnemy = NPCFactory.createPirateCrabEnemy(player);

    GridPoint2 minPos = new GridPoint2(0, 0);
    GridPoint2 maxPos = terrain.getMapBounds(0);
    GridPoint2 randomPos = RandomUtils.random(minPos, maxPos);

    int counter = 0;

    while (this.entityMapping.wouldCollide(pirateCrabEnemy, randomPos.x, randomPos.y)
        || entityMapping.isNearWater(randomPos.x, randomPos.y)) {
      randomPos = RandomUtils.random(minPos, maxPos);
      if (counter > 1000) {
        return;
      }
      counter++;
    }

    spawnEntityAt(pirateCrabEnemy, randomPos, true, true);
  }

  private void playMusic() {
    Music music = ServiceLocator.getResourceService().getAsset(backgroundMusic, Music.class);
    music.setLooping(true);
    music.setVolume(0.3f);
    music.play();
  }

  private void loadAssets() {
    logger.debug("Loading assets");
    ResourceService resourceService = ServiceLocator.getResourceService();
    resourceService.loadTextures(forestTextures);
    resourceService.loadTextureAtlases(forestTextureAtlases);
    resourceService.loadSounds(forestSounds);
    resourceService.loadSounds(walkSound);
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
    resourceService.unloadAssets(forestSounds);
    resourceService.unloadAssets(walkSound);
    resourceService.unloadAssets(forestMusic);
  }

  @Override
  public void dispose() {
    super.dispose();
    ServiceLocator.getResourceService().getAsset(backgroundMusic, Music.class).stop();
    this.unloadAssets();
  }
}
