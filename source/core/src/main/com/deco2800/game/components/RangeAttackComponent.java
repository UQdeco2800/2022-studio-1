package com.deco2800.game.components;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.deco2800.game.entities.Entity;
import com.deco2800.game.physics.PhysicsLayer;
import com.deco2800.game.physics.components.ColliderComponent;
import com.deco2800.game.physics.components.HitboxComponent;
import com.deco2800.game.physics.components.PhysicsComponent;
import com.deco2800.game.services.ServiceLocator;


public class RangeAttackComponent extends Component{
    private short targetLayer;
    private float knockbackForce = 0f;
    private CombatStatsComponent combatStats;
    private HitboxComponent hitboxComponent;
    private float range = 0f;
    private boolean targetAcquired = false; 
    private Entity target; 

    public RangeAttackComponent(short targetLayer, float knockbackForce, float range) {
        this.targetLayer = targetLayer;
        this.knockbackForce = knockbackForce;
        this.range = range; 
    }
    @Override
    public void create() {
        combatStats = entity.getComponent(CombatStatsComponent.class);
        hitboxComponent = entity.getComponent(HitboxComponent.class);
    }

    private float getDistanceToTarget(Entity target) {
        return this.getEntity().getPosition().dst(target.getPosition());
    }

    private void toggleTargetAcquired() {
        targetAcquired = !targetAcquired;
    }

    /*
     * Function which attacks the given target and applies knockback
     */
    private void attackTarget() {
        //attack target
        CombatStatsComponent targetStats = target.getComponent(CombatStatsComponent.class);
        if (targetStats != null) {
            targetStats.hit(combatStats);
        } 
        //apply knockback
        PhysicsComponent physicsComponent = target.getComponent(PhysicsComponent.class);
        if (physicsComponent != null && knockbackForce > 0f) {
            Body targetBody = physicsComponent.getBody();
            Vector2 direction = target.getCenterPosition().sub(entity.getCenterPosition());
            Vector2 impulse = direction.setLength(knockbackForce);
            targetBody.applyLinearImpulse(impulse, targetBody.getWorldCenter(), true);
        }
    }

    /*
    * Check target acquired
    * if target acquired check distance, if distances < range attack
    * if target outside range, negate TargetAcquired, check for new targets
    */
    @Override
    public void update() {
        if (targetAcquired) {
            if (getDistanceToTarget(this.target) <= this.range) {
                while (getDistanceToTarget(entity) <= this.range) {
                    attackTarget();
                }
            } else {
                //Toggle targetAcquired, and set acquired target to null
                toggleTargetAcquired();
                this.target = null;
            }
        } else {
            for (Entity entity : ServiceLocator.getEntityService().getAllNamedEntities().values()) {   
                ColliderComponent colliderComponent = entity.getComponent(ColliderComponent.class);
                if (colliderComponent != null) {    
                    if (colliderComponent.getLayer() == PhysicsLayer.NPC) {  //Check entity is an NPC
                        if (getDistanceToTarget(entity) <= this.range) { //Check range to target
                            toggleTargetAcquired();
                            this.target = entity; //Set target
                            while (getDistanceToTarget(entity) <= this.range) {
                                attackTarget();
                            }
                        }
                     }

                }
            }
        }
    }

    
}
