package com.deco2800.game.entities.factories;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.deco2800.game.components.Enviromental.EnvironmentalComponent;
import com.deco2800.game.components.Enviromental.SpeedComponent;
import com.deco2800.game.entities.Entity;
import com.deco2800.game.physics.PhysicsLayer;
import com.deco2800.game.physics.PhysicsUtils;
import com.deco2800.game.physics.components.ColliderComponent;
import com.deco2800.game.physics.components.PhysicsComponent;
import com.deco2800.game.rendering.TextureRenderComponent;

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
    return createEnvironmentalObject("images/tree.png",EnvironmentalComponent.EnvironmentalType.WOOD, 2.5f, 0.5f, 0.2f);
  }

  /**
   * Creates a rock entity.
   * Placeholder image from:
   * <a href='https://www.freepik.com/vectors/sea-monster'>Sea monster vector created by macrovector - www.freepik.com</a>
   * @return entity
   */
  public static Entity createRock() {
    return createEnvironmentalObject("images/rock_placeholder_image.png", EnvironmentalComponent.EnvironmentalType.ROCK, 1, 0.5f, 0.2f);
  }

  /**
   * Create a environmental entity based off the given parameters.
   * @param type type of environmental object from EnvironmentalType
   * @param heightScale height scaling of the entity
   * @param scaleX x scaling of the entity
   * @param scaleY y scaling of the entity
   * @return Environmental Entity
   */
  private static Entity createEnvironmentalObject(String imgPath, EnvironmentalComponent.EnvironmentalType type, float heightScale, float scaleX, float scaleY) {
      Entity environmentalObject = new Entity()
                  .addComponent(new TextureRenderComponent(imgPath))
                  .addComponent(new PhysicsComponent())
                  .addComponent(new ColliderComponent().setLayer(PhysicsLayer.OBSTACLE))
                  .addComponent(new EnvironmentalComponent().setType(type))
                  .addComponent(new SpeedComponent());

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
        .addComponent(new ColliderComponent().setLayer(PhysicsLayer.OBSTACLE));
    wall.setScale(width, height);
    return wall;
  }

  private ObstacleFactory() {
    throw new IllegalStateException("Instantiating static util class");
  }
}
