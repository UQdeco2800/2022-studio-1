package com.deco2800.game.entities.factories;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.deco2800.game.components.Environmental.CollisionEffectComponent;
import com.deco2800.game.components.Environmental.EnvironmentalComponent;
import com.deco2800.game.components.Environmental.CollisionEffectComponent.CollisionEffect;
import com.deco2800.game.components.Environmental.EnvironmentalComponent.EnvironmentalObstacle;
import com.deco2800.game.components.infrastructure.ResourceType;
import com.deco2800.game.entities.Entity;
import com.deco2800.game.physics.PhysicsLayer;
import com.deco2800.game.physics.PhysicsUtils;
import com.deco2800.game.physics.components.ColliderComponent;
import com.deco2800.game.physics.components.PhysicsComponent;
import com.deco2800.game.rendering.TextureRenderComponent;
import com.deco2800.game.physics.components.HitboxComponent;
import com.deco2800.game.services.ServiceLocator;

/**
 * Factory to create obstacle entities.
 *
 * <p>
 * Each obstacle entity type should have a creation method that returns a
 * corresponding entity.
 */
public class ObstacleFactory {

  /**
   * Creates a tree entity.
   * 
   * @return entity
   */
  public static Entity createTree() {
    String[] sprites = { "images/landscape_objects/leftPalmTree.png",
        "images/landscape_objects/rightPalmTree.png",
        "images/landscape_objects/groupPalmTrees.png" };
    int index = (int) ((Math.random() * (sprites.length)));
    Entity tree = createEnvironmentalObject(sprites[index], EnvironmentalComponent.EnvironmentalObstacle.TREE,
        2.5f, 0.5f, 0.2f, CollisionEffectComponent.CollisionEffect.DIVERT, 1f);
    tree.setName("Tree");
    tree.setResourceType(ResourceType.WOOD);
    tree.setCollectable(true);
    tree.setResourceAmount(10);
    ServiceLocator.getEntityService().registerNamed("Tree@" + tree.getId(), tree);
    tree.getComponent(TextureRenderComponent.class).scaleEntity();

    return tree;
  }

  /**
   * Creates a tree entity. Overloading used for save/load game
   *
   * @return entity
   */
  public static Entity createTree(String image) {

    Entity tree = createEnvironmentalObject(image, EnvironmentalComponent.EnvironmentalObstacle.TREE,
        2.5f, 0.5f, 0.2f, CollisionEffectComponent.CollisionEffect.DIVERT, 1f);
    tree.setName("Tree");
    tree.setResourceType(ResourceType.WOOD);
    tree.setCollectable(true);
    tree.setResourceAmount(10);
    ServiceLocator.getEntityService().registerNamed("Tree@" + tree.getId(), tree);
    return tree;
  }

  /**
   * Creates a rock entity.
   * 
   * @return entity
   */
  public static Entity createRock() {
    String[] sprites = { "images/seastack1.png", "images/seastack2.png" };
    int index = (int) ((Math.random() * (sprites.length)));
    Entity rock = createEnvironmentalObject(sprites[index], EnvironmentalComponent.EnvironmentalObstacle.ROCK,
        0.8f, 0.5f, 0.2f, CollisionEffectComponent.CollisionEffect.DIVERT, 1f);

    rock.setName("Rock");
    rock.setResourceType(ResourceType.STONE);
    rock.setCollectable(true);
    rock.setResourceAmount(10);

    ServiceLocator.getEntityService().registerNamed("Rock@" + rock.getId() + MathUtils.random(0, 200), rock);
    rock.getComponent(TextureRenderComponent.class).scaleEntity();

    return rock;
  }

  /**
   * Creates a rock entity. Overloading used for the purpose of save/load game
   *
   * @return entity
   */
  public static Entity createRock(String image) {
    Entity rock = createEnvironmentalObject(image, EnvironmentalComponent.EnvironmentalObstacle.ROCK,
        0.8f, 0.5f, 0.2f, CollisionEffectComponent.CollisionEffect.DIVERT, 1f);

    rock.setName("Rock");
    rock.setResourceType(ResourceType.STONE);
    rock.setCollectable(true);
    rock.setResourceAmount(10);
    return rock;
  }

  /**
   * Creates a vine entity, which slows players down
   * 
   * @return entity
   */
  public static Entity createVine() {
    Entity vine = createEnvironmentalObject("images/landscape_objects/vines.png",
        EnvironmentalComponent.EnvironmentalObstacle.VINE,
        2f, 0.5f, 0.2f, CollisionEffectComponent.CollisionEffect.SLOW, 0.5f);

    vine.setName("Vine");
    vine.setResourceType(ResourceType.WOOD);
    vine.setCollectable(true);
    vine.setResourceAmount(5);

    ServiceLocator.getEntityService().registerNamed("Vine@" + vine.getId(), vine);
    vine.getComponent(TextureRenderComponent.class).scaleEntity();

    return vine;
  }

  /**
   * Creates a spiky tree entity, which damages players and knocks them back on
   * contact
   * 
   * @return entity
   */
  public static Entity createSpikyTree() {
    Entity spikyBush = createEnvironmentalObject("images/landscape_objects/cypress-tree-60x100.png",
        EnvironmentalComponent.EnvironmentalObstacle.SPIKY_BUSH,
        2.5f, 0.5f, 0.2f, CollisionEffectComponent.CollisionEffect.DAMAGE, 1f);

    spikyBush.setName("SpikeyTree");
    spikyBush.setResourceType(ResourceType.WOOD);
    spikyBush.setCollectable(true);
    spikyBush.setResourceAmount(20);

    ServiceLocator.getEntityService().registerNamed("SpikeyBush@" + spikyBush.getId(), spikyBush);
    spikyBush.getComponent(TextureRenderComponent.class).scaleEntity();

    return spikyBush;
  }

  /**
   * Creates a geyser, which damages and knocks back the player
   * 
   * @return entity
   */
  public static Entity createGeyser() {
    Entity geyser = createEnvironmentalObject("images/landscape_objects/geyser.png",
        EnvironmentalComponent.EnvironmentalObstacle.GEYSER,
        1f, 0.5f, 0.2f, CollisionEffectComponent.CollisionEffect.DAMAGE, 0.5f);

    geyser.setName("Geyser");
    geyser.setResourceType(ResourceType.STONE);
    geyser.setCollectable(true);
    geyser.setResourceAmount(10);

    ServiceLocator.getEntityService().registerNamed("Geyser@" + geyser.getId(), geyser);
    geyser.getComponent(TextureRenderComponent.class).scaleEntity();

    return geyser;
  }

  /**
   * Creates a Billboard entity, which knocks players back on contact
   * 
   * @return entity
   */
  public static Entity createBillboard() {
    Entity billboard = createEnvironmentalObject("images/landscape_objects/billboard.png",
        EnvironmentalComponent.EnvironmentalObstacle.KNOCKBACK_TOWER,
        3f, 0.5f, 0.2f, CollisionEffectComponent.CollisionEffect.KNOCKBACK, 1f);
    ServiceLocator.getEntityService().registerNamed("Billboard@" + billboard.getId(), billboard);

    billboard.setName("Billboard");
    billboard.setCollectable(false);

    return billboard;
  }

  /**
   * creates an AOE artefact that changes player speed in an area of effect around
   * the artefact.
   * 
   * @return entity
   */
  public static Entity createAoeSpeedArtefact() {
    Entity artefact = createEnvironmentalObject("images/landscape_objects/chalice.png",
        EnvironmentalComponent.EnvironmentalObstacle.SPEED_ARTEFACT,
        0.5f, 0.2f, 0.2f, CollisionEffectComponent.CollisionEffect.SLOW, 1.4f);
    artefact.setName("Chalice");
    artefact.setCollectable(false);
    Vector2 aoeSize = new Vector2();
    Vector2 size = artefact.getScale();
    // sets aoe to twice the scale of the object
    aoeSize.x = size.x * 4;
    aoeSize.y = size.y * 4;
    artefact.addComponent(new HitboxComponent());
    artefact.getComponent(HitboxComponent.class).setAsBox(aoeSize);
    artefact.getComponent(CollisionEffectComponent.class).setAoe(true);
    artefact.getComponent(CollisionEffectComponent.class).setEffectTarget(CollisionEffectComponent.EffectTarget.PLAYER);
    return artefact;
  }

  /**
   * creates a pillar entity
   * 
   * @return entity
   */
  public static Entity createPillar() {
    Entity pillar = createEnvironmentalObject("images/landscape_objects/pillar.png",
        EnvironmentalComponent.EnvironmentalObstacle.STONE_PILLAR,
        3f, 0.2f, 0.2f, CollisionEffectComponent.CollisionEffect.DIVERT, 1f);

    // TODO: Have an external file that keeps track of all object stats -- later
    // refactoring task
    pillar.setName("Pillar");
    pillar.setResourceType(ResourceType.STONE);
    pillar.setCollectable(true);
    pillar.setResourceAmount(10);

    ServiceLocator.getEntityService().registerNamed("Pillar@" + pillar.getId(), pillar);
    pillar.getComponent(TextureRenderComponent.class).scaleEntity();

    return pillar;
  }

  /**
   * creates a wooden fence entity
   * 
   * @return entity
   */
  public static Entity createWoodenFence() {
    Entity fence = createEnvironmentalObject("images/landscape_objects/wooden-fence-60x60.png",
        EnvironmentalComponent.EnvironmentalObstacle.WOODEN_FENCE,
        0.8f, 0.2f, 0.2f, CollisionEffectComponent.CollisionEffect.DIVERT, 1f);

    fence.setName("Fence");
    fence.setCollectable(true);
    fence.setResourceType(ResourceType.WOOD);
    fence.setResourceAmount(50);

    ServiceLocator.getEntityService().registerNamed("Fence@" + fence.getId(), fence);
    return fence;
  }

  /**
   * creates a shipwreck entity (front)
   * 
   * @return entity
   */
  public static Entity createShipwreckFront() {
    Entity shipwreck = createEnvironmentalObject("images/shipWreckFront.png",
        EnvironmentalComponent.EnvironmentalObstacle.SHIPWRECK_BACK,
        2f, 0.2f, 0.2f, CollisionEffectComponent.CollisionEffect.DIVERT, 1f);

    shipwreck.setName("ShipwreckFront");
    shipwreck.setCollectable(true);
    shipwreck.setResourceType(ResourceType.WOOD);
    shipwreck.setResourceAmount(50);

    ServiceLocator.getEntityService().registerNamed("shipWreckFront@" + shipwreck.getId(), shipwreck);
    shipwreck.getComponent(TextureRenderComponent.class).scaleEntity();

    return shipwreck;
  }

  /**
   * creates a shipwreck entity (back)
   * 
   * @return entity
   */
  public static Entity createShipwreckBack() {
    Entity shipwreck = createEnvironmentalObject("images/shipWreckBack.png",
        EnvironmentalComponent.EnvironmentalObstacle.SHIPWRECK_BACK,
        2f, 0.2f, 0.2f, CollisionEffectComponent.CollisionEffect.DIVERT, 1f);

    shipwreck.setName("ShipwreckBack");
    shipwreck.setCollectable(true);
    shipwreck.setResourceType(ResourceType.WOOD);
    shipwreck.setResourceAmount(50);

    ServiceLocator.getEntityService().registerNamed("shipWreckBack@" + shipwreck.getId(), shipwreck);
    shipwreck.getComponent(TextureRenderComponent.class).scaleEntity();

    return shipwreck;
  }

  public static Entity createShell() {
    Entity shell = createEnvironmentalObject("images/65x33_tiles/shell.png",
        EnvironmentalComponent.EnvironmentalObstacle.SHELL, 0.5f, 0.5f, 0.5f,
        CollisionEffectComponent.CollisionEffect.NONE, 1f);

    shell.setName("Shell");
    shell.setCollectable(false);

    ServiceLocator.getEntityService().register(shell);
    shell.getComponent(TextureRenderComponent.class).scaleEntity();

    return shell;
  }

  /**
   * Create an environmental entity based off the given parameters.
   * 
   * @param type        type of environmental object from EnvironmentalType
   * @param heightScale height scaling of the entity
   * @param scaleX      x scaling of the entity
   * @param scaleY      y scaling of the entity
   * @return Environmental Entity
   */
  private static Entity createEnvironmentalObject(String imgPath, EnvironmentalComponent.EnvironmentalObstacle type,
      float heightScale, float scaleX, float scaleY,
      CollisionEffectComponent.CollisionEffect collisionEffect, float speedModifier) {
    Entity environmentalObject = new Entity()
        .addComponent(new TextureRenderComponent(imgPath))
        .addComponent(new PhysicsComponent())
        .addComponent(new ColliderComponent().setLayer(PhysicsLayer.OBSTACLE))
        .addComponent(new EnvironmentalComponent().setObstacle(type))
        .addComponent(new CollisionEffectComponent(collisionEffect, speedModifier));
    environmentalObject.setName("EnvironmentalObject");
    environmentalObject.setCollectable(false);
    environmentalObject.getComponent(PhysicsComponent.class).setBodyType(BodyType.StaticBody);
    environmentalObject.getComponent(TextureRenderComponent.class).scaleEntity();
    environmentalObject.scaleHeight(heightScale);
    environmentalObject.getComponent(TextureRenderComponent.class).scaleEntity();

    PhysicsUtils.setScaledCollider(environmentalObject, scaleX, scaleY);
    return environmentalObject;
  }

  /**
   * Creates an invisible physics wall.
   * 
   * @param width  Wall width in world units
   * @param height Wall height in world units
   * @return Wall entity of given width and height
   */
  public static Entity createWall(float width, float height) {
    Entity wall = new Entity()
        .addComponent(new PhysicsComponent().setBodyType(BodyType.StaticBody))
        .addComponent(new ColliderComponent().setLayer(PhysicsLayer.OBSTACLE).setTangible(PhysicsLayer.PLAYER));
    wall.setName("wall");
    wall.setScale(width, height);
    return wall;
  }

  private ObstacleFactory() {
    throw new IllegalStateException("Instantiating static util class");
  }
}
