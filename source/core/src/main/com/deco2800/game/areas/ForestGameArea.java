package com.deco2800.game.areas;

import com.badlogic.gdx.math.*;
import com.deco2800.game.areas.terrain.EnvironmentalCollision;
import com.deco2800.game.components.CombatStatsComponent;
import com.deco2800.game.components.npc.BossAnimationController;
import com.deco2800.game.files.SaveGame;
import com.deco2800.game.services.DayNightCycleService;
import com.deco2800.game.services.DayNightCycleStatus;
import com.deco2800.game.utils.math.RandomUtils;
import com.deco2800.game.entities.factories.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.badlogic.gdx.audio.Music;
import com.deco2800.game.areas.terrain.TerrainFactory;
import com.deco2800.game.areas.terrain.TerrainFactory.TerrainType;
import com.deco2800.game.components.Environmental.EnvironmentalComponent;
import com.deco2800.game.components.Environmental.ValueTuple;
import com.deco2800.game.entities.Entity;
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
  private static final int BOSS_DAY = 2;
  private static final int MAX_NUM_STARFISH = 3;

  private static final String[] forestTextures = {
      "images/Centaur_Back_left.png",
      "images/Centaur_Back_right.png",
      "images/Centaur_left.png",
      "images/Centaur_right.png",
      "images/landscape_objects/leftPalmTree.png",
      "images/landscape_objects/rightPalmTree.png",
      "images/landscape_objects/groupPalmTrees.png",
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
      "images/65x33_tiles/water.png",
      "images/65x33_tiles/water_night.png",
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
      "images/starfish.png",
      "images/NpcPlaceholder.png",
      "images/NPC convo.png",
      "images/npc1.png",
      "images/npcs/NPC-V2.2.png",
      "images/npcs/NPC-V2.1.png"
  };

  private static final String[] forestTextureAtlases = {
      "images/terrain_iso_grass.atlas", "images/ghost.atlas", "images/ghostKing.atlas",
      "images/eel_animations/eel.atlas", "images/final_boss_animations/final_boss.atlas",
      "images/npc_animations/NPC1sprite.atlas", "images/npc_animations/npc.atlas"
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
  private Boolean loadGame;

  private int NPCNum = ServiceLocator.getNpcService().getNpcNum();

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

    loadAssets();
    ServiceLocator.getGameService().setUpEntities(120);
    ServiceLocator.getUGSService().generateUGS();

    displayUI();

    spawnTerrain();

    entityMapping = new EnvironmentalCollision(terrain);

    // EntityMapping must be made AFTER spawn Terrain and BEFORE any environmental
    // objects are created

    this.crystal = spawnCrystal(terrainFactory.getMapSize().x / 2, terrainFactory.getMapSize().y / 2);

    this.player = spawnPlayer();

    if (this.loadGame) {
      SaveGame.loadGameState();
    } else {
      // spawnEnvironmentalObjects();
    }
    ServiceLocator.getDayNightCycleService().getEvents().addListener(DayNightCycleService.EVENT_DAY_PASSED,
        this::dayChange);
    ServiceLocator.getDayNightCycleService().getEvents().addListener(DayNightCycleService.EVENT_PART_OF_DAY_PASSED,
        this::spawnSetEnemies);
    ServiceLocator.getDayNightCycleService().getEvents().addListener(DayNightCycleService.EVENT_PART_OF_DAY_PASSED,
        this::spawnNPC);

    // playMusic();
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
      String tileCoords = ServiceLocator.getUGSService().generateCoordinate(randomPos.x, randomPos.y);
      ServiceLocator.getUGSService().setEntity(tileCoords, envObj);
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
    String tileCoords = ServiceLocator.getUGSService().generateCoordinate(PLAYER_SPAWN.x, PLAYER_SPAWN.y);
    ServiceLocator.getUGSService().setEntity(tileCoords, newPlayer);
    return newPlayer;
  }

  private Entity spawnCrystal(int x_pos, int y_pos) {
    Entity crystal = CrystalFactory.createCrystal("images/crystal.png", "crystal");
    while (this.entityMapping.wouldCollide(crystal, x_pos, y_pos)) {
      x_pos++;
    }
    // System.out.println("Crystal Position: " + x_pos + " " + y_pos);
    crystal.setPosition(terrain.tileToWorldPosition(x_pos, y_pos));
    ServiceLocator.getEntityService().addEntity(crystal);
    this.entityMapping.addEntity(crystal);
    String tileCoords = ServiceLocator.getUGSService().generateCoordinate(x_pos, y_pos);
    ServiceLocator.getUGSService().setEntity(tileCoords, crystal);
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
   * Spawns NPCs during the day and removes them at night.
   * 
   * @param partOfDay the current part of the day.
   */
  private void spawnNPC(DayNightCycleStatus partOfDay) {
    int StructuresNum = ServiceLocator.getStructureService().getAllNamedEntities().size();
    // System.out.println("struct:"+StructuresNum);
    switch (partOfDay) {
      case DAWN:
      case DAY:
      case DUSK:

        if (NPCNum != StructuresNum) {
          // System.out.println(NPCNum);
          for (int i = NPCNum; i < StructuresNum; i++) {
            // System.out.println("spawned");
            spawnNPCharacter();
          }
        }
        break;
      case NIGHT:
        // Dispose NPCs
        for (int i = 0; i < NPCNum; i++) {
          Entity NPC = ServiceLocator.getNpcService().getNamedEntity(String.valueOf(i));
          NPC.dispose();
        }

        NPCNum = 0;
        ServiceLocator.getNpcService().setNpcNum(NPCNum);
        break;
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
    boss.getComponent(BossAnimationController.class).startAnimation(crystal);
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
    String tileCoords = ServiceLocator.getUGSService().generateCoordinate(randomPos.x, randomPos.y);
    ServiceLocator.getUGSService().setEntity(tileCoords, entity);
  }

  private void spawnElectricEelEnemy() {
    Entity ElectricEelEnemy = NPCFactory.createElectricEelEnemy(player, crystal);
    ElectricEelEnemy.setName("Mr. Electricity");
    this.entityMapping.addEntity(ElectricEelEnemy);
    spawnEnemy(ElectricEelEnemy);
  }

  @Override
  public boolean isWallHere(GridPoint2 pos) {
    boolean mapEntities = super.isWallHere(pos);
    Vector2 worldPos = terrain.tileToWorldPosition(pos);

    for (Entity wall : terrain.getWalls()) {
      if (wall.getPosition().x == worldPos.x && wall.getPosition().y == worldPos.y) {
        return true;
      }
    }

    return mapEntities;
  }

  // Spawn the starfish as ranged enemy
  private void spawnNinjaStarfish() {
    Entity ninjaStarfish = NPCFactory.createStarFish(player, crystal);
    int waterWidth = (terrain.getMapBounds(0).x - terrainFactory.getIslandSize().x) / 2;

    // Get the position from 2D coordinates
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

  private void spawnNPCharacter() {
    Entity NPC;
    if (NPCNum % 3 == 0) {
      NPC = NPCFactory.createNormalNPC();
    } else {
      NPC = NPCFactory.createSpecialNPC();
    }

    ServiceLocator.getNpcService().registerNamed(String.valueOf(NPCNum), NPC);
    this.entityMapping.addEntity(NPC);
    int index = (int) ((Math.random() * (NPC_SPAWNS.length)));
    spawnEntityAt(NPC, NPC_SPAWNS[index], true, true);
    NPCNum++;
    ServiceLocator.getNpcService().setNpcNum(NPCNum);
    // NPC.setPosition(terrainFactory.getMapSize().x / 3,
    // terrainFactory.getMapSize().y / 3);
    // ServiceLocator.getEntityService().addEntity(NPC);

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