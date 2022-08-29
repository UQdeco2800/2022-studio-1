package com.deco2800.game.entities.factories;

import com.badlogic.gdx.physics.box2d.BodyDef;
import com.deco2800.game.components.CombatStatsComponent;
import com.deco2800.game.components.HealthBarComponent;
import com.deco2800.game.components.TouchAttackComponent;
import com.deco2800.game.entities.Entity;
import com.deco2800.game.entities.configs.AttackConfig;
import com.deco2800.game.entities.configs.BaseEntityConfig;
import com.deco2800.game.entities.configs.StructureConfig;
import com.deco2800.game.files.FileLoader;
import com.deco2800.game.physics.PhysicsLayer;
import com.deco2800.game.physics.PhysicsUtils;
import com.deco2800.game.physics.components.ColliderComponent;
import com.deco2800.game.physics.components.HitboxComponent;
import com.deco2800.game.physics.components.PhysicsComponent;
import com.deco2800.game.rendering.TextureRenderComponent;

/**
 * Factory to create attack entities with predefined components.
 *
 * <p>Each structure entity type should have a creation method that returns a corresponding entity.
 * Predefined entity properties can be loaded from configs stored as json files which are defined in
 * "StructureConfigs".
 *
 * <p>If needed, this factory can be separated into more specific factories for entities with
 * similar characteristics.
 */
public class AttackFactory {
  private static final AttackConfig configs =
      FileLoader.readClass(AttackConfig.class, "configs/attack.json");



  /**
   * Creates an attack entity.
   *
   * //@param target entity to chase
   * @return entity
   */
  public static Entity createMeleeAttack(String texture) {
    Entity melee = createBaseAttack(texture);
    BaseEntityConfig config = configs.wall;

    melee.addComponent(new CombatStatsComponent(config.health, config.baseAttack));
    return melee;
  }


  /**
   * Creates a generic Attack to be used as a base entity by more specific Structure creation methods.
   *
   * @return entity
   */
  private static Entity createBaseAttack(String texture) {
   
    Entity attack =
        new Entity()
                .addComponent(new TextureRenderComponent(texture))
            .addComponent(new PhysicsComponent())
            .addComponent(new ColliderComponent().setLayer(PhysicsLayer.DEFAULT))
            .addComponent(new HitboxComponent().setLayer(PhysicsLayer.DEFAULT))
            .addComponent(new TouchAttackComponent(PhysicsLayer.DEFAULT, 1.5f));

    attack.getComponent(PhysicsComponent.class).setBodyType(BodyDef.BodyType.StaticBody);
    attack.getComponent(TextureRenderComponent.class).scaleEntity();
    attack.getComponent(ColliderComponent.class).setSensor(true);
    PhysicsUtils.setScaledCollider(attack, 0.9f, 0.4f);
    return attack;
  }

  private AttackFactory() {
    throw new IllegalStateException("Instantiating static util class");
  }
}
