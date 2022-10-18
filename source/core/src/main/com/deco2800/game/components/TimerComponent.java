package com.deco2800.game.components;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.deco2800.game.entities.Entity;
import com.deco2800.game.physics.BodyUserData;
import com.deco2800.game.physics.PhysicsLayer;
import com.deco2800.game.physics.components.HitboxComponent;
import com.deco2800.game.physics.components.PhysicsComponent;
import com.deco2800.game.services.ServiceLocator;

/**
 * When this entity touches a valid enemy's hitbox, deal damage to them and
 * apply a knockback.
 *
 * <p>
 * Requires CombatStatsComponent, HitboxComponent on this entity.
 *
 * <p>
 * Damage is only applied if target entity has a CombatStatsComponent. Knockback
 * is only applied
 * if target entity has a PhysicsComponent.
 */
public class TimerComponent extends Component {
    private int timer;


    @Override
    public void update(){
        timer++;
        if (timer > 600) {
            ServiceLocator.getEntityService().addToDestroyEntities(this.entity);
        }
    }
}
