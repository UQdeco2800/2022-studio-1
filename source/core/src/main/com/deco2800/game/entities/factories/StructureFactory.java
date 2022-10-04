package com.deco2800.game.entities.factories;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Interpolation.SwingOut;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.deco2800.game.achievements.AchievementType;
import com.deco2800.game.areas.ForestGameArea;
import com.deco2800.game.components.CombatStatsComponent;
import com.deco2800.game.components.HealthBarComponent;
import com.deco2800.game.components.RangeAttackComponent;
import com.deco2800.game.components.infrastructure.ResourceCostComponent;
import com.deco2800.game.components.infrastructure.TrapComponent;
import com.deco2800.game.components.player.InventoryComponent;
import com.deco2800.game.entities.Entity;
import com.deco2800.game.entities.EntityService;
import com.deco2800.game.entities.StructureService;
import com.deco2800.game.entities.configs.BaseEntityConfig;
import com.deco2800.game.entities.configs.BaseStructureConfig;
import com.deco2800.game.entities.configs.StructureConfig;
import com.deco2800.game.files.FileLoader;
import com.deco2800.game.physics.PhysicsLayer;
import com.deco2800.game.physics.PhysicsUtils;
import com.deco2800.game.physics.components.ColliderComponent;
import com.deco2800.game.physics.components.HitboxComponent;
import com.deco2800.game.physics.components.PhysicsComponent;
import com.deco2800.game.rendering.TextureRenderComponent;
import com.deco2800.game.services.AchievementHandler;
import com.deco2800.game.services.ServiceLocator;

import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

import javax.sound.sampled.SourceDataLine;


/**
 * Factory to create structure entities with predefined components.
 *
 * <p>Each structure entity type should have a creation method that returns a corresponding entity.
 * Predefined entity properties can be loaded from configs stored as json files which are defined in
 * "StructureConfigs".
 *
 * <p>If needed, this factory can be separated into more specific factories for entities with
 * similar characteristics.
 */
public class StructureFactory {
  private static final StructureConfig configs =
      FileLoader.readClass(StructureConfig.class, "configs/structure.json");
  private static int REFUNDMULTIPLIER = 80;
  /**
   * Creates a wall entity.
   *
   * @return specialised Wall entity
   */
  public static Entity createWall(String name) {
    Entity wall = createBaseStructure("images/Wall-right.png", name);
    BaseStructureConfig config = configs.wall;


    wall.addComponent(new CombatStatsComponent(config.health, config.baseAttack))
            .addComponent(new ResourceCostComponent(config.gold));
    return wall;
  }

/**
 * Creates a trap entity
 *
 * @return entity
 */
public static Entity createTrap(String name) {
  //TODO change trap texture
  Entity trap = createBaseStructure("images/trap.png", name);
  BaseStructureConfig config = configs.trap;

  trap.addComponent(new CombatStatsComponent(config.health, config.baseAttack))
          .addComponent(new TrapComponent(PhysicsLayer.NPC, 1.5f))
          .addComponent(new ResourceCostComponent(config.gold));
  return trap;
}

  /**
   * Creates a tower1 entity.
   * @param level of the tower to create
   * @return entity
   */
  public static Entity createTower1(int level, String name) {
    //TODO Change string constant
    String TOWER1I = "images/TOWER1I.png";
    String TOWER1II = "images/TOWER1II.png";
    String TOWER1III = "images/TOWER1III.png";

    Entity tower1;
    BaseStructureConfig config;

    switch(level) {
      case 2: //Represents the first upgraded version of the tower
        tower1 = createBaseStructure(TOWER1II, name);
        config = configs.tower1I;
        tower1.addComponent(new CombatStatsComponent(config.health, config.baseAttack, 2, 2))
                .addComponent(new RangeAttackComponent(PhysicsLayer.NPC, 10f, 100f))
                .addComponent(new ResourceCostComponent(config.gold));
        return tower1;

        case 3: //Represents the second upgraded version of the tower
          tower1 = createBaseStructure(TOWER1III, name);
          config = configs.tower1II;
          tower1.addComponent(new CombatStatsComponent(config.health, config.baseAttack, 3, 3))
                  .addComponent(new RangeAttackComponent(PhysicsLayer.NPC, 10f, 100f))
                  .addComponent(new ResourceCostComponent(config.gold, config.stone));
          return tower1;
      default:
        tower1 = createBaseStructure(TOWER1I, name);
        config = configs.tower1;

        tower1.addComponent(new CombatStatsComponent(config.health, config.baseAttack, 1, 1))
                .addComponent(new RangeAttackComponent(PhysicsLayer.NPC, 10f, 100f))
                .addComponent(new ResourceCostComponent(config.gold));
        return tower1;
    }
  }

/**
 * Creates a tower2 entity, adding various components to create a defensive tower
 * @param level of the tower
 * @return tower2 entity
 */
  public static Entity createTower2(int level, String name) {
    //@TODO Change string constant
    String TOWER2I = "images/TOWER2I.png";
    String TOWER2II = "images/TOWER2II.png";
    String TOWER2III = "images/TOWER2III.png";
    Entity tower2;
    BaseStructureConfig config;

    switch(level) {
      case 2: //Represents the first upgraded version of the tower
        tower2 = createBaseStructure(TOWER2II, name);
        config = configs.tower2I;
        tower2.addComponent(new CombatStatsComponent(config.health, config.baseAttack, 2, 2))
                .addComponent(new RangeAttackComponent(PhysicsLayer.NPC, 10f, 100f))
                .addComponent(new ResourceCostComponent(config.gold));
        return tower2;

        case 3: //Represents the second upgraded version of the tower
          tower2 = createBaseStructure(TOWER2III, name);
          config = configs.tower2II;
          tower2.addComponent(new CombatStatsComponent(config.health, config.baseAttack, 3, 3))
                  .addComponent(new RangeAttackComponent(PhysicsLayer.NPC, 10f, 100f))
                  .addComponent(new ResourceCostComponent(config.gold, config.stone));
          return tower2;
        default:
          tower2 = createBaseStructure(TOWER2I, name);
          config = configs.tower2;

          tower2.addComponent(new CombatStatsComponent(config.health, config.baseAttack, 1, 1))
                  .addComponent(new RangeAttackComponent(PhysicsLayer.NPC, 10f, 100f))
                  .addComponent(new ResourceCostComponent(config.gold));
          return tower2;
    }
  }
  /**
   * Creates a tower3 entity, and adds various components to create a defensive tower.
   * @param level of the tower
   * @return tower3 entity
   */
  public static Entity createTower3(int level, String name) {
    //@TODO Change string constant
    String TOWER3I = "images/TOWER3I.png";
    String TOWER3II = "images/TOWER3II.png";
    String TOWER3III = "images/TOWER3III.png";

    Entity tower3;
    BaseStructureConfig config;

    switch(level) {
       case 2: //Represents the first upgraded version of the tower
      tower3 = createBaseStructure(TOWER3II, name);
        config = configs.tower3I;
        tower3.addComponent(new CombatStatsComponent(config.health, config.baseAttack, 2,2))
                .addComponent(new RangeAttackComponent(PhysicsLayer.NPC, 10f, 100f))
                .addComponent(new ResourceCostComponent(config.gold));
        return tower3;

        case 3: //Represents the second upgraded version of the tower
        tower3 = createBaseStructure(TOWER3III, name);
          config = configs.tower3II;
          tower3.addComponent(new CombatStatsComponent(config.health, config.baseAttack, 3,3))
                  .addComponent(new RangeAttackComponent(PhysicsLayer.NPC, 10f, 100f))
                  .addComponent(new ResourceCostComponent(config.gold, config.stone));
          return tower3;
      default:
        tower3 = createBaseStructure(TOWER3I, name);
        config = configs.tower3;

        tower3.addComponent(new CombatStatsComponent(config.health, config.baseAttack, 1, 1))
                .addComponent(new RangeAttackComponent(PhysicsLayer.NPC, 10f, 100f))
                .addComponent(new ResourceCostComponent(config.gold));
        return tower3;
    }
  }

  /**
   * Creates a generic Structure to be used as a base entity by more specific Structure creation methods.
   * @param texture image representation for created structure
   * @return structure entity
   */
  public static Entity createBaseStructure(String texture, String name) {
    ServiceLocator.getAchievementHandler().getEvents().trigger(AchievementHandler.EVENT_BUILDING_PLACED,
            AchievementType.BUILDINGS, 1);

    /* //This is where the defence (aiming and shooting) tasks will be added
    AITaskComponent aiComponent =
        new AITaskComponent()
            .addTask(new WanderTask(new Vector2(2f, 2f), 2f))
            .addTask(new ChaseTask(target, 10, 3f, 4f));*/
    Entity structure =
        new Entity()
                .addComponent(new TextureRenderComponent(texture))
            .addComponent(new PhysicsComponent())
            .addComponent(new ColliderComponent().setLayer(PhysicsLayer.OBSTACLE))
            .addComponent(new HitboxComponent().setLayer(PhysicsLayer.NPC));
            //.addComponent(aiComponent);

    structure.getComponent(PhysicsComponent.class).setBodyType(BodyDef.BodyType.StaticBody);
    structure.getComponent(TextureRenderComponent.class).scaleEntity();
    PhysicsUtils.setScaledCollider(structure, 0.9f, 0.4f);
    structure.setName(name);
    structure.setCollectable(false);
    return structure;
  }


  private StructureFactory() {
    throw new IllegalStateException("Instantiating static util class");
  }

  /**
   * Function which handles the refund of player's resources should they sell a building.
   *
   * Refunds 80% of the buildings original cost
   *
   * @param structure : the building to refund
   */
  public static void handleRefund(Entity structure, float refundMultiplier) {
    Entity player = ServiceLocator.getEntityService().getNamedEntity("player");
//      System.out.println("Checking for inventory component");
//      System.out.println("Got inventory component");
      //Get the cost of the building
      int gold = structure.getComponent(ResourceCostComponent.class).getGoldCost();
      int stone = structure.getComponent(ResourceCostComponent.class).getStoneCost();
      int wood = structure.getComponent(ResourceCostComponent.class).getWoodCost();
//      System.out.println("refund: " + refundMultiplier);
      //Add (<resource> * refundMultiplier) to PLAYER's inventory
//      System.out.println("before: " + player.getComponent(InventoryComponent.class).getGold());

      player.getComponent(InventoryComponent.class).addGold((int)(gold * (refundMultiplier)));
//      System.out.println("After: " + player.getComponent(InventoryComponent.class).getGold());
      player.getComponent(InventoryComponent.class).addStone((int)(stone * refundMultiplier));
      player.getComponent(InventoryComponent.class).addWood((int)(wood * refundMultiplier));
  }

  /**
   * Function which handles the destruction / sale of building.
   * @param name : true if building has been sold, false if building has otherwise been destroyed
   *
   * In future could be expanded by using Enums vs boolean
   *
   */
  public static void handleBuildingDestruction(String name) {
    Entity structure = ServiceLocator.getEntityService().getNamedEntity(name);

    if (structure == null) {
      return;
    }
    int buildingHealth = structure.getComponent(CombatStatsComponent.class).getHealth();
    switch(buildingHealth) {
      case 0: //Building destroyed
        ServiceLocator.getStructureService().unregisterNamed(name);
        ServiceLocator.getEntityService().getNamedEntity(name).dispose();
        ServiceLocator.getUGSService().removeEntity(name);
        break;

      default:
        int health = structure.getComponent(CombatStatsComponent.class).getHealth();
        int maxHealth = structure.getComponent(CombatStatsComponent.class).getBaseHealth();
        Float refundMultiplier = (REFUNDMULTIPLIER * ((float) health / (float) maxHealth)) / (float) 100;
        handleRefund(structure, refundMultiplier);
        ServiceLocator.getStructureService().unregisterNamed(name);
        ServiceLocator.getEntityService().getNamedEntity(name).dispose();
        ServiceLocator.getUGSService().removeEntity(name);
        break;
    }
  }

  /**
   * Function which handles upgrading buildings. Does so by first obtaining and storing building state,
   * removing building and replacing with upgraded version.
   *
   * @param structName: Name of the structure to be upgraded
   *
   */
  public static void upgradeStructure(String structName) {
    //Store rectangle location, name, level
    Vector2 location = ServiceLocator.getEntityService().getNamedEntity(structName).getPosition();
    int level = ServiceLocator.getStructureService().getNamedEntity(structName)
        .getComponent(CombatStatsComponent.class).getLevel();

    //Remove building entity
    if (level > 2) {
      return;
    }
    ServiceLocator.getStructureService().unregisterNamed(structName);
    ServiceLocator.getEntityService().getNamedEntity(structName).dispose();

    //Upgrade depending on building
    if (structName.contains("wall")) {
      //Might not be worth implementing depending on how enemy team implements enemy AI

    } else if (structName.contains("tower1")) {
      Entity tower1;
        switch(level) {
          //Only two possible upgrades 1->2 and 2->3
          case 1:
//            System.out.println("Tower upgraded1");
            tower1 = StructureFactory.createTower1(2, structName);
            ServiceLocator.getEntityService().registerNamed(structName, tower1);
            ServiceLocator.getStructureService().registerNamed(structName, tower1);
            ServiceLocator.getStructureService().getNamedEntity(structName).setPosition(location);
            break;
          case 2:
//            System.out.println("Tower upgraded2");
            tower1 = StructureFactory.createTower1(3, structName);
            ServiceLocator.getEntityService().registerNamed(structName, tower1);
            ServiceLocator.getStructureService().registerNamed(structName, tower1);
            ServiceLocator.getStructureService().getNamedEntity(structName).setPosition(location);
            break;
        }
    } else if (structName.contains("tower2")) {
      switch(level) {
        //Only two possible upgrades 1->2 and 2->3
        case 1:
          ServiceLocator.getEntityService().registerNamed(structName, createTower2(2, structName));
          ServiceLocator.getEntityService().getNamedEntity(structName).setPosition(location);
          break;
        case 2:
          ServiceLocator.getEntityService().registerNamed(structName, createTower2(3, structName));
          ServiceLocator.getEntityService().getNamedEntity(structName).setPosition(location);
          break;
        }
    } else if (structName.contains("tower3")) {
      switch(level) {
        //Only two possible upgrades 1->2 and 2->3
        case 1:
          ServiceLocator.getEntityService().registerNamed(structName, createTower3(2, structName));
          ServiceLocator.getEntityService().getNamedEntity(structName).setPosition(location);
          break;
        case 2:
          ServiceLocator.getEntityService().registerNamed(structName, createTower3(3, structName));
          ServiceLocator.getEntityService().getNamedEntity(structName).setPosition(location);
          break;
      }
    }
  }
}
