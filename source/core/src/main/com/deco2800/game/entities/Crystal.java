package com.deco2800.game.entities;

import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.deco2800.game.entities.Entity;
import com.deco2800.game.physics.PhysicsLayer;
import com.deco2800.game.physics.PhysicsUtils;
import com.deco2800.game.physics.components.ColliderComponent;
import com.deco2800.game.physics.components.PhysicsComponent;
import com.deco2800.game.rendering.TextureRenderComponent;
import com.deco2800.game.components.CombatStatsComponent;

/**
 * Core infrastructure interface which creates a default infrastructure object.
 */
public class Crystal extends Entity {

    /*
     * Creates a infrastructure entity.
     * Returns entity
     */
    private int health;

    public static Entity createCrystal() {
        Entity crystal =
            new Entity()
                .addComponent(new TextureRenderComponent("images/tree.png"))
                .addComponent(new PhysicsComponent())
                .addComponent(new ColliderComponent().setLayer(PhysicsLayer.OBSTACLE))
                .addComponent(new CombatStatsComponent(100));
        return crystal;
    }
}
