package com.deco2800.game.entities.factories;

import com.badlogic.gdx.physics.box2d.BodyDef;
import com.deco2800.game.components.CombatStatsComponent;
import com.deco2800.game.components.HealthBarComponent;
import com.deco2800.game.entities.Entity;
import com.deco2800.game.entities.configs.BaseEntityConfig;
import com.deco2800.game.entities.configs.StructureConfig;
import com.deco2800.game.files.FileLoader;
import com.deco2800.game.physics.PhysicsLayer;
import com.deco2800.game.physics.components.ColliderComponent;
import com.deco2800.game.physics.components.PhysicsComponent;
import com.deco2800.game.rendering.TextureRenderComponent;

public class StructureFactory {
  private static final StructureConfig configs =
      FileLoader.readClass(StructureConfig.class, "configs/structure.json");
/**
   * Creates a crystal entity.
   *
   * //@param target entity to chase
   * @return entity
   */
  public static Entity createCrystal(String texture) {
    Entity crystal = createBaseStructure(texture);
    BaseEntityConfig config = configs.crystal;

    crystal.addComponent(new CombatStatsComponent(config.health, config.baseAttack))
            .addComponent(new HealthBarComponent(100, 10));
    return crystal;
  }

//  private int health;
//
//  public static Entity createCrystal() {
//    Entity crystal =
//            new Entity()
//                    .addComponent(new TextureRenderComponent("images/tree.png"))
//                    .addComponent(new PhysicsComponent())
//                    .addComponent(new ColliderComponent().setLayer(PhysicsLayer.OBSTACLE))
//                    .addComponent(new CombatStatsComponent(100));
//    return crystal;
//  }

}