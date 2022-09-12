package com.deco2800.game.entities.factories;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
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
import com.deco2800.game.rendering.AnimationRenderComponent;
import com.deco2800.game.rendering.TextureRenderComponent;
import com.deco2800.game.services.ServiceLocator;

/**
 * Factory to create shop entity.
 *
 * <p>Predefined crystal properties are loaded from a config stored as a json file and should have
 * the properties stores in 'CrystalConfig'.
 */

public class ShopFactory {

    /**
     * Creates a shop entity.
     * @return entity
     */
    public static Entity createBuyFail() {
        AnimationRenderComponent buy_animator = new AnimationRenderComponent(ServiceLocator.getResourceService().getAsset("images/anim_demo/buy_button_fail.atlas", TextureAtlas.class));
        buy_animator.addAnimation("fail_1", 0.5f, Animation.PlayMode.LOOP);
        buy_animator.startAnimation("fail_1");
        Entity buyFail =
                new Entity()
                        .addComponent(new TextureRenderComponent("images/anim_demo/buy_button_fail.atlas"));
        buyFail.getComponent(TextureRenderComponent.class).scaleEntity();
        buyFail.addComponent(buy_animator);
        buy_animator.startAnimation("fail_1");
//        buy_animator.stopAnimation();
        return buyFail;
    }

    public static Entity createItemJitter() {
        AnimationRenderComponent jitter_animator = new AnimationRenderComponent(ServiceLocator.getResourceService().getAsset("images/anim_demo/buy_button_fail.atlas", TextureAtlas.class));
        jitter_animator.addAnimation("jitter_1", 0.5f, Animation.PlayMode.LOOP);
        jitter_animator.startAnimation("jitter_1");
        Entity itemJitter =
                new Entity()
                        .addComponent(new TextureRenderComponent("images/anim_demo/Items_jitter.atlas"));
        itemJitter.getComponent(TextureRenderComponent.class).scaleEntity();
        itemJitter.addComponent(jitter_animator);
        jitter_animator.startAnimation("jitter_1");
//        buy_animator.stopAnimation();
        return itemJitter;
    }





    private ShopFactory() {
        throw new IllegalStateException("Instantiating static util class");
    }
}
