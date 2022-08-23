package com.deco2800.game.areas;

import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Octree.Collider;
import com.deco2800.game.areas.terrain.TerrainFactory;
import com.deco2800.game.areas.terrain.TerrainFactory.TerrainType;
import com.deco2800.game.entities.Entity;
import com.deco2800.game.entities.factories.NPCFactory;
import com.deco2800.game.entities.factories.ObstacleFactory;
import com.deco2800.game.entities.factories.PlayerFactory;
import com.deco2800.game.physics.components.ColliderComponent;
import com.deco2800.game.physics.components.PhysicsComponent;
import com.deco2800.game.utils.math.GridPoint2Utils;
import com.deco2800.game.utils.math.RandomUtils;
import com.deco2800.game.services.ResourceService;
import com.deco2800.game.services.ServiceLocator;
import com.deco2800.game.components.gamearea.GameAreaDisplay;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/** Forest area for the demo game with trees, a player, and some enemies. */
public class ForestGameArea extends GameArea {
  private static final Logger logger = LoggerFactory.getLogger(ForestGameArea.class);

  private static final int NUM_GHOSTS = 2;
  private static final GridPoint2 PLAYER_SPAWN = new GridPoint2(60, 60);
  private static final float WALL_WIDTH = 0.1f;

  private static final int MAX_ENVIROMENTAL_OBJECTS = 20;
  private static final int MIN_NUM_TREES = 3;
  private static final int MAX_NUM_TREES = 12;
  private static final int MIN_NUM_ROCKS = 5;
  private static final int MAX_NUM_ROCKS = 8;

  private static final String[] forestTextures = {
      "images/box_boy_leaf.png",
      "images/tree.png",
      "images/ghost_king.png",
      "images/ghost_1.png",
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
      "images/rock_placeholder_image.png"
  };
  private static final String[] forestTextureAtlases = {
      "images/terrain_iso_grass.atlas", "images/ghost.atlas", "images/ghostKing.atlas"
  };
  private static final String[] forestSounds = { "sounds/Impact4.ogg" };
  private static final String backgroundMusic = "sounds/BGM_03_mp3.mp3";
  private static final String[] forestMusic = { backgroundMusic };

  private final TerrainFactory terrainFactory;

  private Entity player;

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

    spawnEnvironmentalObjects();

    player = spawnPlayer();

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
   * Spawns trees based off semi random bounds
   * 
   * @param numTrees Number of trees to spawn
   */
  private void spawnTrees(int numTrees) {
    GridPoint2 minPos = new GridPoint2(0, 0);
    GridPoint2 maxPos = terrain.getMapBounds(0).sub(2, 2);

    for (int i = 0; i < numTrees; i++) {
      GridPoint2 randomPos = RandomUtils.random(minPos, maxPos);
      Entity tree = ObstacleFactory.createTree();
      spawnEntityAt(tree, randomPos, true, false);
    }
  }

  /**
   * Spawns rocks based of semi random bounds
   * 
   * @param numRocks Number of rocks to spawn
   */
  private void spawnRocks(int numRocks) {
    GridPoint2 minPos = new GridPoint2(0, 0);
    GridPoint2 maxPos = terrain.getMapBounds(0).sub(2, 2);

    for (int i = 0; i < numRocks; i++) {
      GridPoint2 randomPos = RandomUtils.random(minPos, maxPos);
      Entity Rock = ObstacleFactory.createRock();
      spawnEntityAt(Rock, randomPos, true, false);
    }
  }

  /**
   * Generate the environment objects. This is responsible for rocks, trees and
   * other related Environmental Types.
   * Object numbers must fall within set bounds.
   */
  private void spawnEnvironmentalObjects() {
    int numTrees = MIN_NUM_TREES + (int) (Math.random() * ((MAX_NUM_TREES - MIN_NUM_TREES) + 1));
    System.out.println(numTrees);
    spawnTrees(numTrees);
    int objectsRemaining = MAX_ENVIROMENTAL_OBJECTS - numTrees;

    int numRocks = MIN_NUM_ROCKS + (int) (Math.random() * ((MAX_NUM_ROCKS - MIN_NUM_ROCKS) + 1));
    spawnRocks(numRocks);
    objectsRemaining = MAX_ENVIROMENTAL_OBJECTS - numRocks;

    // Remaining number of objects can be spawned off raw percentage?

  }

  private Entity spawnPlayer() {
    Entity newPlayer = PlayerFactory.createPlayer();
    spawnEntityAt(newPlayer, PLAYER_SPAWN, true, true);
    return newPlayer;
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
    resourceService.unloadAssets(forestMusic);
  }

  @Override
  public void dispose() {
    super.dispose();
    ServiceLocator.getResourceService().getAsset(backgroundMusic, Music.class).stop();
    this.unloadAssets();
  }
}
