package com.deco2800.game.entities.factories;

import com.badlogic.gdx.physics.box2d.BodyDef;
import com.deco2800.game.components.CombatStatsComponent;
import com.deco2800.game.components.HealthBarComponent;
import com.deco2800.game.components.TouchAttackComponent;
import com.deco2800.game.entities.Entity;
import com.deco2800.game.entities.configs.BaseEntityConfig;
import com.deco2800.game.entities.configs.CrystalConfig;
import com.deco2800.game.entities.configs.StructureConfig;
import com.deco2800.game.files.FileLoader;
import com.deco2800.game.physics.PhysicsLayer;
import com.deco2800.game.physics.PhysicsUtils;
import com.deco2800.game.physics.components.ColliderComponent;
import com.deco2800.game.physics.components.HitboxComponent;
import com.deco2800.game.physics.components.PhysicsComponent;
import com.deco2800.game.rendering.TextureRenderComponent;
import com.deco2800.game.services.ServiceLocator;

/**
 * Factory to create crystal entity.
 *
 * <p>Predefined crystal properties are loaded from a config stored as a json file and should have
 * the properties stores in 'CrystalConfig'.
 */

public class CrystalFactory {
    private static final CrystalConfig crystalStats =
            FileLoader.readClass(CrystalConfig.class, "configs/crystal.json");

    /**
     * Creates a crystal entity.
     * @return entity
     */
    public static Entity createCrystal() {
        Entity crystal =
                new Entity()
                        .addComponent(new TextureRenderComponent("images/crystal.png"))
                        .addComponent(new PhysicsComponent())
                        .addComponent(new ColliderComponent().setLayer(PhysicsLayer.OBSTACLE))
                        // I've just moved the hitbox component onto the obstacle layer for now because when it was on
                        // the NPC layer the player character was attacking it feel free to change this later
                        .addComponent(new HitboxComponent().setLayer(PhysicsLayer.PLAYER))
                        .addComponent(new TouchAttackComponent(PhysicsLayer.PLAYER, 1.5f));

        crystal.addComponent(new CombatStatsComponent(crystalStats.health, crystalStats.baseAttack, crystalStats.level))
                .addComponent(new HealthBarComponent(100, 10));
        ServiceLocator.getEntityService().registerNamed("crystal", crystal);


        crystal.getComponent(PhysicsComponent.class).setBodyType(BodyDef.BodyType.StaticBody);
        crystal.getComponent(TextureRenderComponent.class).scaleEntity();
        //crystal.scaleHeight(2);
        PhysicsUtils.setScaledCollider(crystal, 1f, 0.5f);
        return crystal;
    }





    private CrystalFactory() {
        throw new IllegalStateException("Instantiating static util class");
    }
}
