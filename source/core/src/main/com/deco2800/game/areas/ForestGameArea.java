package com.deco2800.game.areas;

import com.badlogic.gdx.math.*;
import com.badlogic.gdx.utils.Array;
import com.deco2800.game.ai.tasks.AITaskComponent;
import com.deco2800.game.areas.terrain.EnvironmentalCollision;
import com.deco2800.game.components.CombatStatsComponent;
import com.deco2800.game.components.infrastructure.ResourceType;
import com.deco2800.game.components.npc.BossAnimationController;
import com.deco2800.game.files.SaveGame;
import com.deco2800.game.services.DayNightCycleService;
import com.deco2800.game.services.DayNightCycleStatus;
import com.deco2800.game.entities.factories.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.badlogic.gdx.audio.Music;
import com.deco2800.game.areas.terrain.TerrainFactory;
import com.deco2800.game.areas.terrain.TerrainFactory.TerrainType;
import com.deco2800.game.components.Environmental.EnvironmentalComponent;
import com.deco2800.game.components.Environmental.ValueTuple;
import com.deco2800.game.components.Environmental.EnvironmentalComponent.EnvironmentalObstacle;
import com.deco2800.game.entities.Entity;
import com.deco2800.game.components.gamearea.GameAreaDisplay;
import com.deco2800.game.services.ResourceService;
import com.deco2800.game.services.ServiceLocator;

import java.security.SecureRandom;
import java.util.*;

/** Forest area for the demo game with trees, a player, and some enemies. */
public class ForestGameArea extends GameArea {
  public static final String TITLE_FONT = "title";
  public static final String LARGE_FONT = "large";
  public static final String SMALL_FONT = "small";
  public static final String BUTTON_FONT = "button";
  public static final String BLACK = "black";
  public static final String WHITE = "white";
  public static final String TERRAIN = "terrain";

  private static final Logger logger = LoggerFactory.getLogger(ForestGameArea.class);
  private static final GridPoint2 PLAYER_SPAWN = new GridPoint2(59, 59);
  // private static final GridPoint2 NPC_SPAWN = new GridPoint2(60, 60);
  private static final GridPoint2[] NPC_SPAWNS = { new GridPoint2(61, 60),
      new GridPoint2(60, 61),
      new GridPoint2(59, 60),
      new GridPoint2(60, 59) };
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
  private static final int BOSS_DAY = 3;
  private static final int MIN_NUM_STARFISH = 1;
  private static final int MAX_NUM_STARFISH = 3;

  private Music music;
  private Music ambience;

  private static final String BACKGROUND_MUSIC = "sounds/bgm_dusk.mp3";
  private static final String BACKGROUND_SOUNDS = "sounds/BgCricket.mp3";
  private static final String SHOP_MUSIC = "sounds/shopping_backgroundmusic-V1.mp3";

  // private EnvironmentalCollision entityMapping;

  // private EnvironmentalCollision entityMapping;

  private final TerrainFactory terrainFactory;
  // private Entity player;
  // private Entity crystal;
  private int dayNum = 1;
  private Boolean loadGame;

  // Number of NPCs currently on the map.
  private int NPCNum = ServiceLocator.getNpcService().getNpcNum();

  // List of NPCs that have spawned.
  private List<Entity> activeNPCs = new ArrayList<Entity>();

  public ForestGameArea(TerrainFactory terrainFactory, Boolean loadGame) {
    super();
    this.loadGame = loadGame;
    this.terrainFactory = terrainFactory;
  }

  /**
   * Create the game area, including terrain, static entities (trees), dynamic
   * entities (player)
   */
  @Override
  public void create() {
    displayUI();
    playMusic();
    spawnTerrain();
    ServiceLocator.getUGSService().generateUGS();

    entityMapping = new EnvironmentalCollision(terrain);

    // EntityMapping must be made AFTER spawn Terrain and BEFORE any environmental
    // objects are created
    // logger.info("Terrain map size ==> {}", terrainFactory.getMapSize());
    this.crystal = spawnCrystal(terrainFactory.getMapSize().x / 2, terrainFactory.getMapSize().y / 2);

    this.player = spawnPlayer();

    // spawnNPCharacter();
    if (this.loadGame) {
      SaveGame.loadGameState();
    } else {
      spawnEnvironmentalObjects();
    }
    ServiceLocator.getDayNightCycleService().getEvents().addListener(DayNightCycleService.EVENT_DAY_PASSED,
        this::dayChange);
    ServiceLocator.getDayNightCycleService().getEvents().addListener(DayNightCycleService.EVENT_PART_OF_DAY_PASSED,
        this::spawnSetEnemies);
    ServiceLocator.getDayNightCycleService().getEvents().addListener(DayNightCycleService.EVENT_PART_OF_DAY_PASSED,
        this::spawnNPC);
  }

  private void displayUI() {
    Entity ui = new Entity();
    ui.addComponent(new GameAreaDisplay("Atlantis Sinks"));
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

      if (type == EnvironmentalObstacle.ROCK || type == EnvironmentalObstacle.SHIPWRECK_BACK
          || type == EnvironmentalObstacle.SHIPWRECK_FRONT) {
        randomPos = new GridPoint2(MathUtils.random(20, 100), MathUtils.random(20, 100));
        ServiceLocator.getUGSService().setEntity(randomPos, envObj, envObj.getName());
      } else {
        while (!ServiceLocator.getUGSService().setEntity(randomPos, envObj, envObj.getName())
            || entityMapping.isNearWater(randomPos.x, randomPos.y)) {
          randomPos = terrain.getLandTiles().get(MathUtils.random(0, terrain.getLandTiles().size() - 1));

          // safety to avoid infinite looping on loading screen.
          // If cant spawn the object then space has ran out on map
          if (counter > 1000) {
            return;
          }
          counter++;
        }
      }
    }
  }

  /**
   * Generate the environment objects. This is responsible for rocks, trees and
   * other related Environmental Types.
   * Object numbers must fall within set bounds.
   */
  private void spawnEnvironmentalObjects() {
    spawnEnvironmentalObject(30, EnvironmentalComponent.EnvironmentalObstacle.SHIPWRECK_BACK);
    spawnEnvironmentalObject(30, EnvironmentalComponent.EnvironmentalObstacle.SHIPWRECK_FRONT);
    spawnEnvironmentalObject(3, EnvironmentalComponent.EnvironmentalObstacle.TREE);
    spawnEnvironmentalObject(1, EnvironmentalComponent.EnvironmentalObstacle.SPEED_ARTEFACT);
    spawnEnvironmentalObject(1, EnvironmentalComponent.EnvironmentalObstacle.STONE_PILLAR);
    spawnEnvironmentalObject(3, EnvironmentalComponent.EnvironmentalObstacle.SHELL);
    spawnEnvironmentalObject(40, EnvironmentalComponent.EnvironmentalObstacle.ROCK);
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
    ServiceLocator.getUGSService().setEntity(PLAYER_SPAWN, newPlayer, "player");
    return newPlayer;
  }

  private Entity spawnCrystal(int x_pos, int y_pos) {
    Entity crystal = CrystalFactory.createCrystal("images/crystal.png", "crystal");
    while (this.entityMapping.wouldCollide(crystal, x_pos, y_pos)) {
      x_pos++;
    }
    crystal.setPosition(terrain.tileToWorldPosition(x_pos, y_pos));
    ServiceLocator.getEntityService().addEntity(crystal);
    this.entityMapping.addEntity(crystal);
    GridPoint2 tileCoords = new GridPoint2(x_pos, y_pos);
    ServiceLocator.getUGSService().setEntity(tileCoords, crystal, crystal.getName());

    // spawnWorldBorders(-1, 0);

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
        // int oldLvl = terrain.getCurrentMapLvl();
        terrain.decrementMapLvl();
        // int newLvl = terrain.getCurrentMapLvl();
        // spawnWorldBorders(oldLvl, newLvl);
      }
    }

  }

  private void destroyWorldBorders(int lvl) {
    for (Entity entity : this.entityMapping.getEntities()) {
      if (Objects.equals(entity.getName(), "wall")) {
        entity.dispose();
      }
    }
  }

  private void spawnWorldBorders(int oldLvl, int newLvl) {

    if (oldLvl != -1) {
      destroyWorldBorders(oldLvl);
    }

    ArrayList<ArrayList<GridPoint2>> max = terrainFactory.getBordersPositionList();

    ArrayList<GridPoint2> borderLevel = max.get(newLvl);

    Iterator itr = borderLevel.iterator();

    for (Iterator it = itr; it.hasNext();) {
      GridPoint2 pos = (GridPoint2) it.next();
      Entity wall = ObstacleFactory.createWall(20f, 1f, 1f);
      this.entityMapping.addEntity(wall);
      spawnEntityAt(wall, pos, false, false);
    }

    /*
     * for (int x = 1; x < mapSize.x - 1; x++) {
     * for (int y = 1; y < mapSize.y - 1; y++) {
     * 
     * TerrainTile tile = (TerrainTile) tiledMapTileLayer.getCell(x, y).getTile();
     * 
     * if (tile.getName() == "water") {
     * Entity wall = ObstacleFactory.createWall(1f, 0.5f);
     * 
     * this.entityMapping.addEntity(wall);
     * super.spawnEntityAt(wall, new GridPoint2(x, y), false, false);
     * }
     */
    /*
     * TerrainTile above = (TerrainTile) tiledMapTileLayer.getCell(x, y +
     * 1).getTile();
     * TerrainTile below = (TerrainTile) tiledMapTileLayer.getCell(x, y -
     * 1).getTile();
     * TerrainTile left = (TerrainTile) tiledMapTileLayer.getCell(x - 1,
     * y).getTile();
     * TerrainTile right = (TerrainTile) tiledMapTileLayer.getCell(x + 1,
     * y).getTile();
     * TerrainTile rightAbove = (TerrainTile) tiledMapTileLayer.getCell(x + 1, y +
     * 1).getTile();
     * TerrainTile rightBelow = (TerrainTile) tiledMapTileLayer.getCell(x + 1, y -
     * 1).getTile();
     * TerrainTile leftAbove = (TerrainTile) tiledMapTileLayer.getCell(x - 1, y +
     * 1).getTile();
     * TerrainTile leftBelow = (TerrainTile) tiledMapTileLayer.getCell(x - 1, y -
     * 1).getTile();
     */

    /*
     * if (tile.getName().equals("grass")) {
     * if (above.getName().equals("water")) {
     * createBorderWall(x, y + 1);
     * }
     * if (below.getName().equals("cliff") || below.getName().equals("cliffLeft")) {
     * createBorderWall(x, y - 1);
     * }
     * if (left.getName().equals("water")) {
     * createBorderWall(x - 1, y);
     * }
     * if (right.getName().equals("cliff") || right.getName().equals("cliffRight"))
     * {
     * createBorderWall(x + 1, y);
     * }
     * if (rightAbove.getName().equals("water") ||
     * rightAbove.getName().equals("cliffRight")
     * || rightAbove.getName().equals("cliff")) {
     * createBorderWall(x + 1, y + 1);
     * }
     * if (rightBelow.getName().equals("cliff")) {
     * createBorderWall(x + 1, y - 1);
     * }
     * if (leftAbove.getName().equals("water")) {
     * createBorderWall(x - 1, y + 1);
     * }
     * if (leftBelow.getName().equals("water") ||
     * leftBelow.getName().equals("cliff")
     * || leftBelow.getName().equals("cliffLeft")) {
     * createBorderWall(x - 1, y + 1);
     * }
     * }
     * }
     * }
     */
  }

  /*
   * private void createBorderWall(int x, int y) {
   * //Fix this to match Luke's stuff
   * Entity wall = ObstacleFactory.createWall(1f, 0.5f);
   * super.spawnEntityAt(wall, new GridPoint2(x, y), false, false);
   * }
   */

  /**
   * Spawns NPCs during the day and removes them at night.
   * NPCs spawn based on the number of buildings you have.
   * If NPCs exist from the previous day, spawn them again at dawn.
   *
   * @param partOfDay the current part of the day.
   */
  private void spawnNPC(DayNightCycleStatus partOfDay) {
    int StructuresNum = ServiceLocator.getStructureService().getAllNamedEntities().size();
    switch (partOfDay) {

      case DAWN:
        // Spawns NPCs that already existed
        if (activeNPCs.size() > 0) {
          // For each existing NPC, spawn them again
          for (Entity npc : activeNPCs) {
            spawnNPCharacter();
          }
        }
        break;

      case DAY:
        break;

      case DUSK:
        if (NPCNum != StructuresNum) {
          for (int i = NPCNum; i < StructuresNum; i++) {
            spawnNPCharacter();
          }
        }
        break;

      case NIGHT:
        // Dispose of NPCs
        for (int i = 0; i < NPCNum; i++) {
          Entity NPC = ServiceLocator.getNpcService().getNamedEntity(String.valueOf(i));
          NPC.dispose();
        }
        // Set NPC number to 0 and update this in NPCService
        NPCNum = 0;
        ServiceLocator.getNpcService().setNpcNum(NPCNum);
        break;

      default:
    }
  }

  /**
   * Spawns crabs at certain part of the day
   */
  private void spawnSetEnemies(DayNightCycleStatus partOfDay) {
    switch (partOfDay) {
      case DAY:
        break;
      case NIGHT:
        for (int i = 0; i < MathUtils.random(MIN_NUM_CRABS, MAX_NUM_CRABS); i++) {
          spawnPirateCrabEnemy();
          // spawnElectricEelEnemy();
          // spawnNinjaStarfishEnemy();
        }
        if (dayNum == BOSS_DAY) {
          spawnMeleeBoss();
        }
        break;
      default:
    }
  }

  /**
   * Spawn the boss
   */
  private void spawnMeleeBoss() {
    Entity boss = NPCFactory.createMeleeBoss(player);
    boss.setName("Mr. Zero");
    spawnEnemy(boss);
    boss.getComponent(BossAnimationController.class).startAnimation(crystal);
  }

  public int count = 0;

  /**
   * Spawns a Pirate Crab entity at a randomised position within the game world
   */
  private void spawnPirateCrabEnemy() {
    Entity pirateCrabEnemy = NPCFactory.createPirateCrabEnemy(crystal);
    pirateCrabEnemy.setName("Mr. Crabs@" + pirateCrabEnemy.getId());
    pirateCrabEnemy.setCollectable(true);
    pirateCrabEnemy.setResourceType(ResourceType.GOLD);
    pirateCrabEnemy.setResourceAmount(50);
    levelUp(pirateCrabEnemy);
    this.entityMapping.addEntity(pirateCrabEnemy);
    spawnEnemy(pirateCrabEnemy);
  }

  /**
   * Spawns an enemy on the map at a random position surrounding the island
   *
   * @param entity the entity to spawn
   */
  private void spawnEnemy(Entity entity) {
    GridPoint2 randomPos = terrainFactory.getSpawnableTiles(terrain.getCurrentMapLvl())
        .get(MathUtils.random(0, terrainFactory.getSpawnableTiles(terrain.getCurrentMapLvl()).size() - 1));

    spawnEntityAt(entity, randomPos, true, true);
    ServiceLocator.getUGSService().setEntity(randomPos, entity, "Enemy@" + entity.getId());
    entity.getComponent(AITaskComponent.class).updateMovementTask();
  }

  private void levelUp(Entity entity) {
    switch (dayNum) {
      case 1:
        entity.getComponent(CombatStatsComponent.class).setLevel(1);
        entity.getComponent(CombatStatsComponent.class).setMaxHealth(10);
        entity.getComponent(CombatStatsComponent.class).setHealth(10);
        entity.getComponent(CombatStatsComponent.class).setBaseAttack(10);
        entity.getComponent(CombatStatsComponent.class).setBaseDefense(1);
        break;
      case 2:
        entity.getComponent(CombatStatsComponent.class).setLevel(2);
        entity.getComponent(CombatStatsComponent.class).setMaxHealth(20);
        entity.getComponent(CombatStatsComponent.class).setHealth(20);
        entity.getComponent(CombatStatsComponent.class).setBaseAttack(20);
        entity.getComponent(CombatStatsComponent.class).setBaseDefense(2);
        break;
      case 3:
        entity.getComponent(CombatStatsComponent.class).setLevel(3);
        entity.getComponent(CombatStatsComponent.class).setMaxHealth(30);
        entity.getComponent(CombatStatsComponent.class).setHealth(30);
        entity.getComponent(CombatStatsComponent.class).setBaseAttack(30);
        entity.getComponent(CombatStatsComponent.class).setBaseDefense(3);
        break;
      default:
        System.out.println("Level is invalid");
    }
  }

  private void spawnElectricEelEnemy() {
    Entity ElectricEelEnemy = NPCFactory.createElectricEelEnemy(player, crystal);
    ElectricEelEnemy.setName("Mr. Electricity");
    levelUp(ElectricEelEnemy);
    ElectricEelEnemy.setCollectable(true);
    ElectricEelEnemy.setResourceType(ResourceType.GOLD);
    ElectricEelEnemy.setResourceAmount(50);
    this.entityMapping.addEntity(ElectricEelEnemy);
    spawnEnemy(ElectricEelEnemy);
  }

  @Override
  public boolean isWallHere(GridPoint2 pos) {
    boolean mapEntities = super.isWallHere(pos);
    Vector2 worldPos = terrain.tileToWorldPosition(pos);
    int level = terrain.getLayer();
    for (GridPoint2 wall : terrain.getWalls().get(level)) {
      if (wall.x == worldPos.x && wall.y == worldPos.y) {
        return true;
      }
    }

    return mapEntities;
  }

  // Spawn the starfish as ranged enemy
  private void spawnNinjaStarfishEnemy() {
    Entity ninjaStarfishEnemy = NPCFactory.createStarFishEnemy(player, crystal);
    ninjaStarfishEnemy.setName("Mr. Starfish");
    levelUp(ninjaStarfishEnemy);
    ninjaStarfishEnemy.setCollectable(true);
    ninjaStarfishEnemy.setResourceType(ResourceType.GOLD);
    ninjaStarfishEnemy.setResourceAmount(50);
    this.entityMapping.addEntity(ninjaStarfishEnemy);
    spawnEnemy(ninjaStarfishEnemy);
  }

  /**
   * Spawns an interactable NPC on the island
   */
  private void spawnNPCharacter() {

    Entity NPC = NPCFactory.createSpecialNPC();
    ServiceLocator.getNpcService().registerNamed(String.valueOf(NPCNum), NPC);
    this.entityMapping.addEntity(NPC);
    int index = (int) (new SecureRandom().nextInt(NPC_SPAWNS.length));
    spawnEntityAt(NPC, NPC_SPAWNS[index], true, true);
    ServiceLocator.getUGSService().setEntity(NPC_SPAWNS[index], NPC, "NPC@" + NPC.getId());

    NPCNum++;
    ServiceLocator.getNpcService().setNpcNum(NPCNum);
  }

  private void playMusic() {
    // Background Music
    music = ServiceLocator.getResourceService().getAsset(BACKGROUND_MUSIC, Music.class);
    music.setLooping(true);
    music.setVolume(0.3f);
    music.play();

    // Background Ambience
    ambience = ServiceLocator.getResourceService().getAsset(BACKGROUND_SOUNDS, Music.class);
    ambience.setLooping(true);
    ambience.setVolume(0.1f);
    ambience.play();
  }

  public void playShopMusic() {
    ServiceLocator.getResourceService().getAsset(BACKGROUND_MUSIC, Music.class).stop();
    ServiceLocator.getResourceService().getAsset(BACKGROUND_SOUNDS, Music.class).stop();
    Music music = ServiceLocator.getResourceService().getAsset(SHOP_MUSIC, Music.class);
    music.setLooping(true);
    music.setVolume(0.3f);
    music.play();
  }

  public void exitShop() {
    ServiceLocator.getResourceService().getAsset(SHOP_MUSIC, Music.class).stop();
    playMusic();
  }

  private void loadAssets() {
    logger.debug("Loading assets");
  }

  private void unloadAssets() {
    logger.debug("Unloading assets");
    ResourceService resourceService = ServiceLocator.getResourceService();

  }

  @Override
  public void dispose() {
    super.dispose();
    ServiceLocator.getResourceService().getAsset(BACKGROUND_MUSIC, Music.class).stop();
    ServiceLocator.getResourceService().getAsset(BACKGROUND_SOUNDS, Music.class).stop();
    ServiceLocator.getResourceService().getAsset(SHOP_MUSIC, Music.class).stop();
    this.unloadAssets();
  }

  public EnvironmentalCollision getEntityMapping() {
    return entityMapping;
  }
}