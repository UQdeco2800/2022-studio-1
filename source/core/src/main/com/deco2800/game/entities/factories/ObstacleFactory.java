package com.deco2800.game.entities.factories;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.deco2800.game.components.Environmental.CollisionEffectComponent;
import com.deco2800.game.components.Environmental.EnvironmentalComponent;
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
 * <p>Each obstacle entity type should have a creation method that returns a corresponding entity.
 */
public class ObstacleFactory {

  /**
   * Creates a tree entity.
   * @return entity
   */
  public static Entity createTree() {
    String[] sprites = {"images/landscape_objects/almond-tree-60x62.png", "images/landscape_objects/fig-tree-60x62.png"};
    int index = (int) ((Math.random() * (sprites.length)));
    Entity tree = createEnvironmentalObject(sprites[index], EnvironmentalComponent.EnvironmentalObstacle.TREE,
            2.5f, 0.5f, 0.2f, CollisionEffectComponent.CollisionEffect.DIVERT, 1f);
    ServiceLocator.getEntityService().registerNamed("Tree@" + tree.getId(), tree);
    return tree;
  }

  /**
   * Creates a rock entity.
   * @return entity
   */
  public static Entity createRock() {
    String[] sprites = {"images/landscape_objects/limestone-boulder-60x60.png", "images/landscape_objects/marble-stone-60x40.png"};
    int index = (int) ((Math.random() * (sprites.length)));
    Entity rock = createEnvironmentalObject(sprites[index], EnvironmentalComponent.EnvironmentalObstacle.ROCK,
            0.8f, 0.5f, 0.2f, CollisionEffectComponent.CollisionEffect.DIVERT, 1f);
    ServiceLocator.getEntityService().registerNamed("Rock@" + rock.getId(), rock);
    return rock;
  }

  /**
   * Creates a vine entity, which slows players down
   * @return entity
   */
  public static Entity createVine() {
    Entity vine = createEnvironmentalObject("images/landscape_objects/vines.png", EnvironmentalComponent.EnvironmentalObstacle.VINE,
            2f, 0.5f, 0.2f, CollisionEffectComponent.CollisionEffect.SLOW, 0.5f);
    ServiceLocator.getEntityService().registerNamed("Vine@" + vine.getId(), vine);
    return vine;
  }

  /**
   * Creates a spiky tree entity, which damages players and knocks them back on contact
   * @return entity
   */
  public static Entity createSpikyTree() {
    Entity spikyBush = createEnvironmentalObject("images/landscape_objects/cypress-tree-60x100.png", EnvironmentalComponent.EnvironmentalObstacle.SPIKY_BUSH,
            2.5f, 0.5f, 0.2f, CollisionEffectComponent.CollisionEffect.DAMAGE, 1f);
    ServiceLocator.getEntityService().registerNamed("SpikeyBush@" + spikyBush.getId(), spikyBush);
    return spikyBush;
  }

  /**
   * Creates a geyser, which damages and knocks back the player
   * @return entity
   */
  public static Entity createGeyser() {
    Entity geyser = createEnvironmentalObject("images/landscape_objects/geyser.png", EnvironmentalComponent.EnvironmentalObstacle.GEYSER,
            1f, 0.5f, 0.2f, CollisionEffectComponent.CollisionEffect.DAMAGE, 0.5f);
    ServiceLocator.getEntityService().registerNamed("Geyser@" + geyser.getId(), geyser);
    return geyser;
  }

  /**
   * Creates a Billboard entity, which knocks players back on contact
   * @return entity
   */
  public static Entity createBillboard() {
    Entity billboard = createEnvironmentalObject("images/landscape_objects/billboard.png", EnvironmentalComponent.EnvironmentalObstacle.KNOCKBACK_TOWER,
            3f, 0.5f, 0.2f, CollisionEffectComponent.CollisionEffect.KNOCKBACK, 1f);
    ServiceLocator.getEntityService().registerNamed("Billboard@" + billboard.getId(), billboard);
    return billboard;
  }

  /**
   * creates an AOE artefact that changes player speed in an area of effect around the artefact.
   * @return entity
   */
  public static Entity createAoeSpeedArtefact() {
    Entity artefact = createEnvironmentalObject("images/landscape_objects/chalice.png", EnvironmentalComponent.EnvironmentalObstacle.SPEED_ARTEFACT,
            0.5f, 0.2f, 0.2f, CollisionEffectComponent.CollisionEffect.SLOW, 1.4f);
    Vector2 aoeSize = new Vector2();
    Vector2 size = artefact.getScale();
    //sets aoe to twice the scale of the object
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
   * @return entity
   */
  public static Entity createPillar() {
    Entity pillar = createEnvironmentalObject("images/landscape_objects/pillar.png", EnvironmentalComponent.EnvironmentalObstacle.STONE_PILLAR,
            3f, 0.2f, 0.2f, CollisionEffectComponent.CollisionEffect.DIVERT, 1f);
    ServiceLocator.getEntityService().registerNamed("Pillar@" + pillar.getId(), pillar);
    return pillar;
  }

  /**
   * creates a wooden fence entity
   * @return entity
   */
  public static Entity createWoodenFence() {
    Entity fence = createEnvironmentalObject("images/landscape_objects/wooden-fence-60x60.png", EnvironmentalComponent.EnvironmentalObstacle.WOODEN_FENCE,
            0.8f, 0.2f, 0.2f, CollisionEffectComponent.CollisionEffect.DIVERT, 1f);
    ServiceLocator.getEntityService().registerNamed("Fence@" + fence.getId(), fence);
    return fence;
  }


  /**
   * Create an environmental entity based off the given parameters.
   * @param type type of environmental object from EnvironmentalType
   * @param heightScale height scaling of the entity
   * @param scaleX x scaling of the entity
   * @param scaleY y scaling of the entity
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
    environmentalObject.getComponent(PhysicsComponent.class).setBodyType(BodyType.StaticBody);
    environmentalObject.getComponent(TextureRenderComponent.class).scaleEntity();
    environmentalObject.scaleHeight(heightScale);
    PhysicsUtils.setScaledCollider(environmentalObject, scaleX, scaleY);
    return environmentalObject;
  }

  /**
   * Creates an invisible physics wall.
   * @param width Wall width in world units
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
