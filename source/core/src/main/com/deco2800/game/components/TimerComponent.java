package com.deco2800.game.components;

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
        if (timer > 420) {
            ServiceLocator.getEntityService().addToDestroyEntities(this.entity);
        }
    }
}
