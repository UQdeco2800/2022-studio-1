package com.deco2800.game.entities.factories;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.deco2800.game.areas.ForestGameArea;
import com.deco2800.game.components.CombatStatsComponent;
import com.deco2800.game.components.HealthBarComponent;
import com.deco2800.game.components.RangeAttackComponent;
import com.deco2800.game.components.infrastructure.ResourceCostComponent;
import com.deco2800.game.components.infrastructure.TrapComponent;
import com.deco2800.game.components.player.InventoryComponent;
import com.deco2800.game.entities.Entity;
import com.deco2800.game.entities.EntityService;
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
  public static Entity createWall() {
    Entity wall = createBaseStructure("images/wall-right.png");
    BaseStructureConfig config = configs.wall;

    wall.addComponent(new CombatStatsComponent(config.health, config.baseAttack))
            .addComponent(new HealthBarComponent(75, 10))
            .addComponent(new ResourceCostComponent(config.gold));
    return wall;
  }

/**
 * Creates a trap entity 
 * 
 * @return entity 
 */
public static Entity createTrap() {
  //TODO change trap texture
  Entity trap = createBaseStructure("images/wall-right.png");
  BaseStructureConfig config = configs.trap;

  trap.addComponent(new CombatStatsComponent(config.health, config.baseAttack))
          .addComponent(new HealthBarComponent(75, 10))
          .addComponent(new TrapComponent(PhysicsLayer.NPC, 1.5f))
          .addComponent(new ResourceCostComponent(config.gold));
  return trap;
}

  /**
   * Creates a tower1 entity.
   *
   * @return entity
   */
  public static Entity createTower1(int level) {
    //@TODO Change string constant 
    String TOWER1I = "images/mini_tower.png";
    String TOWER1II = "images/mini_tower.png";
    String TOWER1III = "images/mini_tower.png";
    Entity tower1;
    BaseStructureConfig config;

    tower1 = createBaseStructure("images/mini_tower.png");
    switch(level) {
      case 1: //Represents the base level structure
        tower1 = createBaseStructure("images/mini_tower.png");
        config = configs.tower1;
    
        tower1.addComponent(new CombatStatsComponent(config.health, config.baseAttack, 1))
                .addComponent(new HealthBarComponent(75, 10))
                .addComponent(new RangeAttackComponent(PhysicsLayer.NPC, 10f, 100f))
                .addComponent(new ResourceCostComponent(config.gold));
        return tower1;
      
      case 2: //Represents the first upgraded version of the tower
        tower1 = createBaseStructure(TOWER1I);
        config = configs.tower1I;
        tower1.addComponent(new CombatStatsComponent(config.health, config.baseAttack, 2))
                .addComponent(new HealthBarComponent(75, 10))
                .addComponent(new RangeAttackComponent(PhysicsLayer.NPC, 10f, 100f))
                .addComponent(new ResourceCostComponent(config.gold));
        return tower1;

        case 3: //Represents the second upgraded version of the tower
          tower1 = createBaseStructure(TOWER1II);
          config = configs.tower1II;
          tower1.addComponent(new CombatStatsComponent(config.health, config.baseAttack, 3))
                  .addComponent(new HealthBarComponent(75, 10))
                  .addComponent(new RangeAttackComponent(PhysicsLayer.NPC, 10f, 100f))
                  .addComponent(new ResourceCostComponent(config.gold, config.stone));
          return tower1;
    }
    //should never run    
    return tower1;
  }

  /**
   * Creates a generic Structure to be used as a base entity by more specific Structure creation methods.
   * @param texture image representation for created structure
   * @return structure entity
   */
  public static Entity createBaseStructure(String texture) {
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
    return structure;
  }


  private StructureFactory() {
    throw new IllegalStateException("Instantiating static util class");
  }

  /**
   * Function which handles the refund of player's resources should they sell a building. 
   * 
   * Refunds 80% of the buildings original cost * <Percentage of buildings health compared to max health>
   * 
   * @param Entity : the building to refund
   */
  public static void handleRefund(Entity structure, Float refundMultiplier) {
    Entity player = ServiceLocator.getEntityService().getNamedEntity("player");
      System.out.println("Checking for inventory component");
      System.out.println("Got inventory component");
      //Get the cost of the building
      int gold = structure.getComponent(ResourceCostComponent.class).getGoldCost();
      int stone = structure.getComponent(ResourceCostComponent.class).getStoneCost();
      int wood = structure.getComponent(ResourceCostComponent.class).getWoodCost();
      System.out.println("refund: " + refundMultiplier);
      //Add (<resource> * refundMultiplier) to PLAYER's inventory
      System.out.println("before: " + player.getComponent(InventoryComponent.class).getGold());

      player.getComponent(InventoryComponent.class).addGold((int)(gold * (refundMultiplier)));
      System.out.println("After: " + player.getComponent(InventoryComponent.class).getGold());
      player.getComponent(InventoryComponent.class).addStone((int)(stone * refundMultiplier));
      player.getComponent(InventoryComponent.class).addWood((int)(wood * refundMultiplier));
  }

  /**
   * Function which handles the destruction / sale of building. 
   * @param structure : true if building has been sold, false if building has otherwise been destroyed
   * 
   * In future could be expanded by using Enums vs boolean
   *  
   */
  public static void handleBuildingDestruction(Entity structure, SortedMap<String, Rectangle> structureRects) {
    int buildingHealth = structure.getComponent(CombatStatsComponent.class).getHealth();
    //Get structureRects from structureService
      //SortedMap<String, Rectangle> structureRects = new TreeMap<>();
    //Iterate through structure list and obtain matching rectangle 
    for (Map.Entry<String, Rectangle> rectangle : structureRects.entrySet()){
        if (rectangle.getKey().contains(ServiceLocator.getStructureService().getName(structure))){
          switch(buildingHealth) {
            case 0: //Building destroyed
            ServiceLocator.getStructureService().getNamedEntity(rectangle.getKey()).dispose();
            structureRects.remove(rectangle.getKey());

            default: 
              int health = structure.getComponent(CombatStatsComponent.class).getHealth();
              int maxHealth = structure.getComponent(CombatStatsComponent.class).getBaseHealth();
              Float refundMultiplier = (REFUNDMULTIPLIER * ((float) health / (float) maxHealth)) / (float) 100;
              handleRefund(structure, refundMultiplier);
          }
        }
    }           


  }

  /**
   * Function which handles upgrading buildings. Does so by first obtaining and storing building state, 
   * removing building and replacing with upgraded version.
   * 
   * @param rectangle: Entry from structureRects indicating building to upgrade
   * 
   */
  public void upgradeStructure(Map.Entry<String, Rectangle> rectangle) {
    //Store rectangle location, name, level
    Vector2 location = ServiceLocator.getEntityService().getNamedEntity(rectangle.getKey()).getPosition();
    String rectangleName = rectangle.getKey();
    int level = ServiceLocator.getEntityService().getNamedEntity(rectangle.getKey())
        .getComponent(CombatStatsComponent.class).getLevel();

    //Remove building entity
    ServiceLocator.getEntityService().getNamedEntity(rectangle.getKey()).dispose();

    //Upgrade depending on building
    if (rectangleName.contains("wall")) {
      //Might not be worth implementing depending on how enemy team implements enemy AI
      
    } else if (rectangleName.contains("tower1")) {
        switch(level) {
          //Only two possible upgrades 1->2 and 2->3
          case 1: 
            ServiceLocator.getEntityService().registerNamed(rectangleName, createTower1(2));
            ServiceLocator.getEntityService().getNamedEntity(rectangleName).setPosition(location);
          case 2:
            ServiceLocator.getEntityService().registerNamed(rectangleName, createTower1(3));
            ServiceLocator.getEntityService().getNamedEntity(rectangleName).setPosition(location);
        }

    } 

  }
}
