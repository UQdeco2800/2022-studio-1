package com.deco2800.game.entities.factories;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.deco2800.game.components.CombatStatsComponent;
import com.deco2800.game.components.HealthBarComponent;
import com.deco2800.game.components.TouchAttackComponent;
import com.deco2800.game.components.crystal.CrystalAnimationController;
import com.deco2800.game.entities.Entity;
import com.deco2800.game.entities.configs.CrystalConfig;
import com.deco2800.game.files.FileLoader;
import com.deco2800.game.physics.PhysicsLayer;
import com.deco2800.game.physics.PhysicsUtils;
import com.deco2800.game.physics.components.ColliderComponent;
import com.deco2800.game.physics.components.HitboxComponent;
import com.deco2800.game.physics.components.PhysicsComponent;
import com.deco2800.game.rendering.AnimationRenderComponent;
import com.deco2800.game.rendering.TextureRenderComponent;
import com.deco2800.game.services.ServiceLocator;

/**
 * Factory to create crystal entity.
 *
 * <p>
 * Predefined crystal properties are loaded from a config stored as a json file
 * and should have
 * the properties stores in 'CrystalConfig'.
 */

public class CrystalFactory {
    private static final CrystalConfig crystalStats = FileLoader.readClass(CrystalConfig.class, "configs/crystal.json");

    /**
     * Creates a crystal entity.
     * 
     * @return entity
     */
    public static Entity createCrystal(String texture, String name) {
        AnimationRenderComponent animator =
            new AnimationRenderComponent(
                    ServiceLocator.getResourceService().getAsset("images/crystal_animation/p_crystal_damaged.atlas", TextureAtlas.class));
        animator.addAnimation("pcrystal", 0.1f, Animation.PlayMode.NORMAL);
        animator.addAnimation("plast", 0.1f, Animation.PlayMode.NORMAL);

        Entity crystal = new Entity()
                .addComponent(new TextureRenderComponent(texture))
                .addComponent(new PhysicsComponent())
                .addComponent(new ColliderComponent().setLayer(PhysicsLayer.PLAYER))
                .addComponent(new HitboxComponent().setLayer(PhysicsLayer.PLAYER))
                .addComponent(new TouchAttackComponent(PhysicsLayer.PLAYER, 1.5f))
                .addComponent(animator)
                .addComponent(new CrystalAnimationController());

        crystal.addComponent(new CombatStatsComponent(crystalStats.health, crystalStats.baseAttack,
                crystalStats.defense, crystalStats.level, 1000))
                .addComponent(new HealthBarComponent(50, 10));
        crystal.setName("crystal");
        crystal.setCollectable(false);

        crystal.getComponent(PhysicsComponent.class).setBodyType(BodyDef.BodyType.StaticBody);
        // crystal.getComponent(TextureRenderComponent.class).scaleEntity();
        crystal.getComponent(AnimationRenderComponent.class).scaleEntity();
        crystal.scaleHeight(15.0f);
        PhysicsUtils.setScaledCollider(crystal, 1f, 0.5f);
        return crystal;
    }

    private CrystalFactory() {
        throw new IllegalStateException("Instantiating static util class");
    }
}
