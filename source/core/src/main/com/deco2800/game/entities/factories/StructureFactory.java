package com.deco2800.game.entities.factories;

import java.util.ArrayList;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.deco2800.game.achievements.AchievementType;
import com.deco2800.game.ai.tasks.AITaskComponent;
import com.deco2800.game.areas.terrain.TerrainComponent;
import com.deco2800.game.components.AOEDamageComponent;
import com.deco2800.game.components.CombatStatsComponent;
import com.deco2800.game.components.HealthBarComponent;
import com.deco2800.game.components.infrastructure.OrientationComponent;
import com.deco2800.game.components.infrastructure.ResourceCostComponent;
import com.deco2800.game.components.infrastructure.TrapComponent;
import com.deco2800.game.components.player.InventoryComponent;

import com.deco2800.game.components.tasks.ShootMultipleTask;
import com.deco2800.game.entities.Entity;
import com.deco2800.game.entities.configs.BaseStructureConfig;
import com.deco2800.game.entities.configs.StructureConfig;
import com.deco2800.game.files.FileLoader;
import com.deco2800.game.physics.PhysicsLayer;
import com.deco2800.game.physics.PhysicsUtils;
import com.deco2800.game.physics.components.ColliderComponent;
import com.deco2800.game.physics.components.HitboxComponent;
import com.deco2800.game.physics.components.PhysicsComponent;
import com.deco2800.game.rendering.AnimationRenderComponent;
import com.deco2800.game.rendering.TextureRenderComponent;
import com.deco2800.game.services.AchievementHandler;
import com.deco2800.game.services.ServiceLocator;
import com.sun.jdi.ObjectReference;
import org.w3c.dom.Text;

/**
 * Factory to create structure entities with predefined components.
 *
 * <p>
 * Each structure entity type should have a creation method that returns a
 * corresponding entity.
 * Predefined entity properties can be loaded from configs stored as json files
 * which are defined in
 * "StructureConfigs".
 *
 * <p>
 * If needed, this factory can be separated into more specific factories for
 * entities with
 * similar characteristics.
 */
public class StructureFactory {
  private static final StructureConfig configs = FileLoader.readClass(StructureConfig.class, "configs/structure.json");
  private static int REFUNDMULTIPLIER = 80;
  private static String[] tempWallSprites = { "images/65x33_tiles/temp_wall_left.png", "images/65x33_tiles/temp_wall_right.png" };
  private static String[] wallSprites = { "images/65x33_tiles/wall_left.png", "images/65x33_tiles/wall_right.png" };
  private static String[] tempTower1Sprites = {"images/attack_towers/tempStructures/temp_tow1_1_l.png", "images/attack_towers/tempStructures/temp_tow1_1_r.png"};
  private static String[][] tower1Sprites = { {"images/attack_towers/tow1_1_l.png",
          "images/attack_towers/tow1_1_r.png"}, {"images/attack_towers/tow1_2_l.png",
          "images/attack_towers/tow1_2_r.png"}, {"images/attack_towers/tow1_3_l.png",
          "images/attack_towers/tow1_3_r.png"} };
  private static String [] tempTower2Sprites = {"tower2Level1", "tower2Level1"};
  //Change to tower 2
  private static String[][] tower2Sprites = {{"tower2Level1", "tower2Level1"},
          {"tower2Level2", "tower2Level2"},{"tower2Level3", "tower2Level3"}};
  private static String[] tempTower3Sprites = {"images/attack_towers/tempStructures/temp_tower3lv1Left.png",
          "images/attack_towers/tempStructures/temp_tower3lv1Right.png"};
  private static String[][] tower3Sprites = {{"images/attack_towers/tower3lv1Left.png", "images/attack_towers/tower3lv1Right.png"},
          {"images/attack_towers/tower3lv2Left.png", "images/attack_towers/tower3lv2Right.png"},
          {"images/attack_towers/tower3lv3Left.png", "images/attack_towers/tower3lv3Right.png"}};

  /**
   * creates an entity of a coloured tile to show where a building can be placed
   *
   * @param name    of the entity
   * @param texture the entity uses
   * @return a coloured tile entity
   */
  public static Entity createVisualFeedbackTile(String name, String texture) {
    Entity structure = new Entity().addComponent(new TextureRenderComponent(texture));
    float tileSize = ServiceLocator.getEntityService().getNamedEntity("terrain").getComponent(TerrainComponent.class)
        .getTileSize();
    Texture t = structure.getComponent(TextureRenderComponent.class).getTexture();
    structure.setScale((tileSize), (tileSize) * (float) t.getHeight() / t.getWidth());
    structure.setName(name);
    structure.setCollectable(false);
    return structure;
  }

  /**
   * Creates a wall entity.
   *
   * @return specialised Wall entity
   */
  public static Entity createWall(String name, Boolean isTemp, int orientation) {
    Entity wall;
    if (isTemp) {
      wall = createBaseStructure(tempWallSprites[orientation], name, false); // change texture to be temp texture
    } else {
      wall = createBaseStructure(wallSprites[orientation], name, false);
    }
    wall.setRotation(orientation);
    BaseStructureConfig config = configs.wall;
    config.orientation = orientation;
    wall.addComponent(new CombatStatsComponent(config.health, config.baseAttack, 1, 1, 100))
        .addComponent(new ResourceCostComponent(config.gold))
        .addComponent((new HealthBarComponent(50, 10)));
    float tileSize = ServiceLocator.getEntityService().getNamedEntity("terrain").getComponent(TerrainComponent.class)
        .getTileSize();
    Texture t = wall.getComponent(TextureRenderComponent.class).getTexture();
    wall.setScale((tileSize), (tileSize) * (float) t.getHeight() / t.getWidth());
    // set name and collectable so game doesn't crash when main character attacks
    // wall, feel free to remove
    wall.setCollectable(Boolean.FALSE);

    return wall;
  }

  /**
   * Creates a trap entity
   *
   * @return entity
   */
  public static Entity createTrap(String name, Boolean isTemp) {
    // TODO change trap texture
    Entity trap;
    if (isTemp) {
      trap = createBaseStructure("images/trap.png", name, false); // change texture to be temp texture
    } else {
      trap = createBaseStructure("images/trap.png", name, false);
    }
    BaseStructureConfig config = configs.trap;

    trap.addComponent(new CombatStatsComponent(config.health, config.baseAttack, 1, 1, 100))
        .addComponent(new TrapComponent(PhysicsLayer.NPC, 1.5f))
        .addComponent(new ResourceCostComponent(config.gold))
        .addComponent((new HealthBarComponent(50, 10)));
    float tileSize = ServiceLocator.getEntityService().getNamedEntity("terrain").getComponent(TerrainComponent.class)
        .getTileSize();
    Texture t = trap.getComponent(TextureRenderComponent.class).getTexture();
    trap.setScale((tileSize), (tileSize) * (float) t.getHeight() / t.getWidth());
    return trap;
  }

  public static Entity createTurret(String name) {
    Entity turret = createBaseStructure("images/turret.png", name, false);
    BaseStructureConfig config = configs.turret;

    AITaskComponent aiTaskComponent = new AITaskComponent()
        .addTask(new ShootMultipleTask(new ArrayList<>(), 500f));

    turret.addComponent(new CombatStatsComponent(config.health, config.baseAttack, 1, 1, 100))
        .addComponent(new ResourceCostComponent(config.gold))
        .addComponent((new HealthBarComponent(50, 10)))
        .addComponent(aiTaskComponent);
    float tileSize = ServiceLocator.getEntityService().getNamedEntity("terrain").getComponent(TerrainComponent.class)
        .getTileSize();
    Texture t = turret.getComponent(TextureRenderComponent.class).getTexture();
    turret.setScale((tileSize), (tileSize) * (float) t.getHeight() / t.getWidth());
    return turret;
  }

  /**
   * Creates a tower1 entity.
   *
   * @param level of the tower to create
   * @return entity
   */
  public static Entity createTower1(int level, String name, Boolean isTemp, int orientation) {
    // TODO Change string constant
    String TOWER1I;
    if (isTemp) {
      TOWER1I = tempTower1Sprites[orientation]; // change texture to be temp
    } else {
      TOWER1I = tower1Sprites[0][orientation];
    }
    String TOWER1II = tower1Sprites[1][orientation];
    String TOWER1III = tower1Sprites[2][orientation];

    Entity tower1;
    BaseStructureConfig config;

    float tileSize;
    Texture t;

    switch (level) {
      case 2: // Represents the first upgraded version of the tower
        tower1 = createBaseStructure(TOWER1II, name, true);
        config = configs.tower1I;
        config.orientation = orientation;
        tower1.addComponent(new CombatStatsComponent(config.health, config.baseAttack, 2, 2, 100))
            .addComponent(new AOEDamageComponent(3, 2, 5000))
            .addComponent(new ResourceCostComponent(config.gold))
            .addComponent((new HealthBarComponent(50, 10)))
            .addComponent((new OrientationComponent(config.orientation)));
        tileSize = ServiceLocator.getEntityService().getNamedEntity("terrain").getComponent(TerrainComponent.class)
            .getTileSize();
        t = tower1.getComponent(TextureRenderComponent.class).getTexture();
        tower1.setScale((tileSize), (tileSize) * (float) t.getHeight() / t.getWidth());
        return tower1;

      case 3: // Represents the second upgraded version of the tower
        tower1 = createBaseStructure(TOWER1III, name, false);
        config = configs.tower1II;
        config.orientation = orientation;
        tower1.addComponent(new CombatStatsComponent(config.health, config.baseAttack, 3, 3, 100))
            .addComponent(new AOEDamageComponent(5, 3, 5000))
            .addComponent(new ResourceCostComponent(config.gold, config.stone))
            .addComponent((new HealthBarComponent(50, 10)))
            .addComponent(new OrientationComponent(config.orientation));
        tileSize = ServiceLocator.getEntityService().getNamedEntity("terrain").getComponent(TerrainComponent.class)
            .getTileSize();
        t = tower1.getComponent(TextureRenderComponent.class).getTexture();
        tower1.setScale((tileSize), (tileSize) * (float) t.getHeight() / t.getWidth());
        return tower1;
      default:
        tower1 = createBaseStructure(TOWER1I, name, false);
        config = configs.tower1;
        config.orientation = orientation;
        tower1.addComponent(new CombatStatsComponent(config.health, config.baseAttack, 1, 1, 100))
            .addComponent(new AOEDamageComponent(1, 1, 5000))
            .addComponent(new ResourceCostComponent(config.gold))
            .addComponent((new HealthBarComponent(50, 10)))
            .addComponent(new OrientationComponent(config.orientation));
        tileSize = ServiceLocator.getEntityService().getNamedEntity("terrain").getComponent(TerrainComponent.class)
            .getTileSize();
        t = tower1.getComponent(TextureRenderComponent.class).getTexture();
        tower1.setScale((tileSize), (tileSize) * (float) t.getHeight() / t.getWidth());
        return tower1;
    }
  }

  /**
   * Creates a tower2 entity, adding various components to create a defensive
   * tower
   *
   * @param level of the tower
   * @return tower2 entity
   */
  public static Entity createTower2(int level, String name, Boolean isTemp, int orientation) {
    // @TODO Change string constant
    String TOWER2I;
    if (isTemp) {
      TOWER2I = tempTower2Sprites[orientation]; // change texture to be temp texture
    } else {
      TOWER2I = tower2Sprites[0][orientation];
    }
    String TOWER2II = tower2Sprites[1][orientation];
    String TOWER2III = tower2Sprites[2][orientation];
    Entity tower2;
    BaseStructureConfig config;
    float tileSize;
    Texture t;

    switch (level) {
      case 2: // Represents the first upgraded version of the tower
        tower2 = createBaseStructure(TOWER2II, name, true);
        config = configs.tower2I;
        config.orientation = orientation;
        tower2.addComponent(new CombatStatsComponent(config.health, config.baseAttack, 2, 2, 100))
            .addComponent(new AOEDamageComponent(4, 3, 4500))
            .addComponent(new ResourceCostComponent(config.gold))
            .addComponent((new HealthBarComponent(50, 10)))
            .addComponent(new OrientationComponent(config.orientation));
        // tileSize = ServiceLocator.getEntityService().getNamedEntity("terrain").getComponent(TerrainComponent.class)
        //     .getTileSize();
        // t = tower2.getComponent(TextureRenderComponent.class).getTexture();
        // tower2.setScale((tileSize), (tileSize) * (float) t.getHeight() / t.getWidth());
        tower2.setScale(10,10);
        return tower2;

      case 3: // Represents the second upgraded version of the tower
        tower2 = createBaseStructure(TOWER2III, name, false);
        config = configs.tower2II;
        config.orientation = orientation;
        tower2.addComponent(new CombatStatsComponent(config.health, config.baseAttack, 3, 3, 100))
            .addComponent(new AOEDamageComponent(5, 3, 4250))
            .addComponent(new ResourceCostComponent(config.gold, config.stone))
            .addComponent((new HealthBarComponent(50, 10)))
            .addComponent(new OrientationComponent(config.orientation));
        // tileSize = ServiceLocator.getEntityService().getNamedEntity("terrain").getComponent(TerrainComponent.class)
        //     .getTileSize();
        // t = tower2.getComponent(TextureRenderComponent.class).getTexture();
        // tower2.setScale((tileSize), (tileSize) * (float) t.getHeight() / t.getWidth());
        tower2.setScale(10, 10);
        return tower2;
      default:
        tower2 = createBaseStructure(TOWER2I, name, false);
        config = configs.tower2;
        config.orientation = orientation;
        tower2.addComponent(new CombatStatsComponent(config.health, config.baseAttack, 1, 1, 100))
            .addComponent(new AOEDamageComponent(3, 2, 4750))
            .addComponent(new ResourceCostComponent(config.gold))
            .addComponent((new HealthBarComponent(50, 10)))
            .addComponent(new OrientationComponent(config.orientation));
        // tileSize = ServiceLocator.getEntityService().getNamedEntity("terrain").getComponent(TerrainComponent.class)
        //     .getTileSize();
        // t = tower2.getComponent(TextureRenderComponent.class).getTexture();
        // tower2.setScale((tileSize), (tileSize) * (float) t.getHeight() / t.getWidth());
        tower2.setScale(10, 10);
        return tower2;
    }
  }

  /**
   * Creates a tower3 entity, and adds various components to create a defensive
   * tower.
   *
   * @param level of the tower
   * @return tower3 entity
   */
  public static Entity createTower3(int level, String name, Boolean isTemp, int orientation) {
    // @TODO Change string constant
    String TOWER3I;
    if (isTemp) {
      TOWER3I = tempTower3Sprites[orientation]; // change texture to be temp texture
    } else {
      TOWER3I = tower3Sprites[0][orientation];
    }
    String TOWER3II = tower3Sprites[1][orientation];
    String TOWER3III = tower3Sprites[2][orientation];

    Entity tower3;
    BaseStructureConfig config;
    float tileSize;
    Texture t;

    switch (level) {
      case 2: // Represents the first upgraded version of the tower
        tower3 = createBaseStructure(TOWER3II, name, false);
        config = configs.tower3I;
        config.orientation = orientation;
        tower3.addComponent(new CombatStatsComponent(config.health, config.baseAttack, 2, 2, 100))
            .addComponent(new AOEDamageComponent(6, 3, 3750))
            .addComponent(new ResourceCostComponent(config.gold))
            .addComponent((new HealthBarComponent(50, 10)))
            .addComponent(new OrientationComponent(config.orientation));
        tileSize = ServiceLocator.getEntityService().getNamedEntity("terrain").getComponent(TerrainComponent.class)
            .getTileSize();
        t = tower3.getComponent(TextureRenderComponent.class).getTexture();
        tower3.setScale((tileSize), (tileSize) * (float) t.getHeight() / t.getWidth());
        return tower3;

      case 3: // Represents the second upgraded version of the tower
        tower3 = createBaseStructure(TOWER3III, name, false);
        config = configs.tower3II;
        config.orientation = orientation;
        tower3.addComponent(new CombatStatsComponent(config.health, config.baseAttack, 3, 3, 100))
            .addComponent(new AOEDamageComponent(7, 4, 3000))
            .addComponent(new ResourceCostComponent(config.gold, config.stone))
            .addComponent((new HealthBarComponent(50, 10)))
            .addComponent(new OrientationComponent(config.orientation));
        tileSize = ServiceLocator.getEntityService().getNamedEntity("terrain").getComponent(TerrainComponent.class)
            .getTileSize();
        t = tower3.getComponent(TextureRenderComponent.class).getTexture();
        tower3.setScale((tileSize), (tileSize) * (float) t.getHeight() / t.getWidth());
        return tower3;
      default:
        tower3 = createBaseStructure(TOWER3I, name, false);
        config = configs.tower3;
        config.orientation = orientation;
        tower3.addComponent(new CombatStatsComponent(config.health, config.baseAttack, 1, 1, 100))
            .addComponent(new AOEDamageComponent(4, 3, 4250))
            .addComponent(new ResourceCostComponent(config.gold))
            .addComponent((new HealthBarComponent(50, 10)))
            .addComponent(new OrientationComponent(config.orientation));
        tileSize = ServiceLocator.getEntityService().getNamedEntity("terrain").getComponent(TerrainComponent.class)
            .getTileSize();
        t = tower3.getComponent(TextureRenderComponent.class).getTexture();
        tower3.setScale((tileSize), (tileSize) * (float) t.getHeight() / t.getWidth());
        return tower3;
    }
  }

  /**
   * Creates a generic Structure to be used as a base entity by more specific
   * Structure creation methods.
   *
   * @param texture image representation for created structure
   * @return structure entity
   */
  public static Entity createBaseStructure(String texture, String name, boolean animated) {
    ServiceLocator.getAchievementHandler().getEvents().trigger(AchievementHandler.EVENT_BUILDING_PLACED,
        AchievementType.BUILDINGS, 1);

    /*
     * //This is where the defence (aiming and shooting) tasks will be added
     * AITaskComponent aiComponent =
     * new AITaskComponent()
     * .addTask(new WanderTask(new Vector2(2f, 2f), 2f))
     * .addTask(new ChaseTask(target, 10, 3f, 4f));
     */

    Entity structure = new Entity();
    if (animated) {

      // texture (String) must just be the name of the file without extension

      AnimationRenderComponent animator = new AnimationRenderComponent(ServiceLocator.getResourceService()
          .getAsset("images/attack_towers/animations/" + texture + ".atlas", TextureAtlas.class));
      animator.addAnimation(texture, 0.2f, Animation.PlayMode.LOOP);
      animator.startAnimation(texture);
      structure.addComponent(animator);

      structure.getComponent(AnimationRenderComponent.class).scaleEntity();
    } else {
      structure.addComponent(new TextureRenderComponent(texture));
      structure.getComponent(TextureRenderComponent.class).scaleEntity();
    }

    structure.addComponent(new PhysicsComponent())
        .addComponent(new ColliderComponent().setLayer(PhysicsLayer.OBSTACLE))
        .addComponent(new HitboxComponent().setLayer(PhysicsLayer.NPC));
    // .addComponent(aiComponent);

    structure.getComponent(PhysicsComponent.class).setBodyType(BodyDef.BodyType.StaticBody);

    structure.setCreationMethod(Thread.currentThread().getStackTrace()[2].getMethodName());
    PhysicsUtils.setScaledCollider(structure, 0.9f, 0.4f);
    structure.setName(name);
    structure.setCollectable(false);
    return structure;
  }

  private StructureFactory() {
    throw new IllegalStateException("Instantiating static util class");
  }

  /**
   * Function which handles the refund of player's resources should they sell a
   * building.
   *
   * Refunds 80% of the buildings original cost
   *
   * @param structure : the building to refund
   */
  public static void handleRefund(Entity structure, float refundMultiplier) {
    Entity player = ServiceLocator.getEntityService().getNamedEntity("player");
    // Get the cost of the building
    int gold = structure.getComponent(ResourceCostComponent.class).getGoldCost();
    int stone = structure.getComponent(ResourceCostComponent.class).getStoneCost();
    int wood = structure.getComponent(ResourceCostComponent.class).getWoodCost();
    // Add (<resource> * refundMultiplier) to PLAYER's inventory

    player.getComponent(InventoryComponent.class).addGold((int) (gold * (refundMultiplier)));
    player.getComponent(InventoryComponent.class).addStone((int) (stone * refundMultiplier));
    player.getComponent(InventoryComponent.class).addWood((int) (wood * refundMultiplier));
  }

  /**
   * Function which handles the destruction / sale of building.
   *
   * @param name : true if building has been sold, false if building has otherwise
   *             been destroyed
   *
   *             In future could be expanded by using Enums vs boolean
   *
   */
  public static void handleBuildingDestruction(String name) {
    Entity structure = ServiceLocator.getUGSService().getEntityByName(name);

    if (structure == null) {
      return;
    }
    int buildingHealth = structure.getComponent(CombatStatsComponent.class).getHealth();
    switch (buildingHealth) {
      case 0: // Building destroyed
        ServiceLocator.getUGSService().removeEntity(name);
        break;

      default:
        int health = structure.getComponent(CombatStatsComponent.class).getHealth();
        int maxHealth = structure.getComponent(CombatStatsComponent.class).getBaseHealth();
        Float refundMultiplier = (REFUNDMULTIPLIER * ((float) health / (float) maxHealth)) / (float) 100;
        handleRefund(structure, refundMultiplier);
        ServiceLocator.getUGSService().removeEntity(name);
        break;
    }
  }

  /**
   * Function which handles upgrading buildings. Does so by first obtaining and
   * storing building state,
   * removing building and replacing with upgraded version.
   *
   * @param structName: Name of the structure to be upgraded
   *
   */
  public static void upgradeStructure(GridPoint2 gridPos, String structName) {
    // Store rectangle location, name, level
    int level = ServiceLocator.getUGSService().getEntityByName(structName)
        .getComponent(CombatStatsComponent.class).getLevel();
    if (level > 2) {
      return;
    }
    // Remove building entity
    int orientation = ServiceLocator.getUGSService().getEntityByName(structName)
        .getComponent(OrientationComponent.class).getOrientation();
    ServiceLocator.getUGSService().removeEntity(structName);

    // Upgrade depending on building
    if (structName.contains("wall")) {
      // Might not be worth implementing depending on how enemy team implements enemy
      // AI
    } else if (structName.contains("tower1")) {
      Entity tower1;
      switch (level) {
        // Only two possible upgrades 1->2 and 2->3
        case 1:
          tower1 = StructureFactory.createTower1(2, structName, false, orientation);
          ServiceLocator.getUGSService().setEntity(gridPos, tower1, structName);
          break;
        case 2:
          tower1 = StructureFactory.createTower1(3, structName, false, orientation);
          ServiceLocator.getUGSService().setEntity(gridPos, tower1, structName);
          break;
      }
    } else if (structName.contains("tower2")) {
      Entity tower2;
      switch (level) {
        // Only two possible upgrades 1->2 and 2->3
        case 1:
          tower2 = StructureFactory.createTower2(2, structName, true, orientation);
          ServiceLocator.getUGSService().setEntity(gridPos, tower2, structName);
          break;
        case 2:
          tower2 = StructureFactory.createTower2(3, structName, true, orientation);
          ServiceLocator.getUGSService().setEntity(gridPos, tower2, structName);
          break;
      }
    } else if (structName.contains("tower3")) {
      Entity tower3;
      switch (level) {
        // Only two possible upgrades 1->2 and 2->3
        case 1:
          tower3 = StructureFactory.createTower3(2, structName, false, orientation);
          ServiceLocator.getUGSService().setEntity(gridPos, tower3, structName);
          break;
        case 2:
          tower3 = StructureFactory.createTower3(3, structName, false, orientation);
          ServiceLocator.getUGSService().setEntity(gridPos, tower3, structName);
          break;
      }
    }
  }
}
