package com.deco2800.game.components.Environmental;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.deco2800.game.components.CombatStatsComponent;
import com.deco2800.game.components.player.PlayerActions;
import com.deco2800.game.entities.Entity;
import com.deco2800.game.physics.BodyUserData;
import com.deco2800.game.physics.components.ColliderComponent;
import com.deco2800.game.physics.components.HitboxComponent;
import com.deco2800.game.physics.components.PhysicsComponent;
import com.deco2800.game.components.Component;
import com.deco2800.game.physics.components.PhysicsMovementComponent;

/**
 * class containing collision effects defined by the CollisionEffect enum
 * Entities implementing this must also implement ColliderComponent, PhysicsComponent & EnvironmentalComponent
 * To make the effect AoE, a hitboxcomponent must also be added (change the size of hitboxcomponent to change AoE)
 * At this stage, only speed is supported as an AoE effect
 * By default, collision effects impact both players and NPCs. Use SetEffectTarget() to change this.
 */
public class CollisionEffectComponent extends Component {

    /**
     * Collision effect
     */
    public enum CollisionEffect {
        DIVERT,
        SLOW,
        DAMAGE,
        KNOCKBACK,
        NONE;
    }

    /**
     * Effect target
     */
    public enum EffectTarget {
        PLAYER,
        NPC,
        ALL;
    }

    private CollisionEffect collisionEffect;
    private ColliderComponent colliderComponent;
    private PhysicsComponent physicsComponent;
    private EnvironmentalComponent environmentalComponent;
    private float speedModifier; //note it might be worth restructuring to have this be standalone rather than relying on environmentalcomponent
    private HitboxComponent hitboxComponent;
    private boolean AoE;
    private float knockbackForce = 1f;
    private int damage = 1;
    private EffectTarget effectTarget = EffectTarget.ALL;

    public CollisionEffectComponent(CollisionEffect collisionEffect) {
        this.collisionEffect = collisionEffect;
    }

    /**
     * initialise the collisionEffect. Note this happens after entity has been attached
     * damage and knockbackforce should be set separately, using setDamage() and setKnockbackForce()
     * damage knocks back by default; set knockbackforce to zero to avoid this
     */
    @Override
    public void create() {
        entity.getEvents().addListener("collisionStart", this::onCollisionStart);
        entity.getEvents().addListener("collisionEnd", this::onCollisionEnd);
        this.physicsComponent = entity.getComponent(PhysicsComponent.class);
        this.colliderComponent = entity.getComponent(ColliderComponent.class);
        this.environmentalComponent = entity.getComponent(EnvironmentalComponent.class);
        this.hitboxComponent = entity.getComponent(HitboxComponent.class);
        this.AoE = (this.hitboxComponent != null);
        this.speedModifier = environmentalComponent.getSpeedModifier();
        setCollisionEffect(this.collisionEffect);
    }

    public CollisionEffect getCollisionEffect() { return this.collisionEffect; }

    public void setDamage(int damage) {
        this.damage = damage;
    }

    /**
     * Sets AoE value to true - used in checks to confirm how to implement collision effect
     * Will do nothing if the entity has no HitboxComponent attached (to avoid null pointer issues later on)
     * @param AoE whether the effect should be AoE or not
     */
    public void setAoe(boolean AoE) {
        if (entity.getComponent(HitboxComponent.class) != null) {
            //do nothing if there's not hitbox - otherwise there will be null pointer issues when collisions happen
            this.AoE = AoE;
        }
    }

    public void setKnockbackForce(float knockbackForce) {
        this.knockbackForce = knockbackForce;
    }

    /**
     * sets the entity/ies to be effected
     * @param target the target effect, from EffectTarget enum (PLAYER/NPC/ALL)
     */
    public void setEffectTarget(EffectTarget target) {
        this.effectTarget = target;
    }

    /**
     * sets collision effect. Requires entity to be attached to ColliderComponent
     * Note that this changes ColliderComponent behaviour - may need to change this depending on desired behaviour
     * @param effect the effect to set, defined in CollisionEffectComponent.SetCollisionEffect enum
     * @return CollisionEffectComponent
     */
    public CollisionEffectComponent setCollisionEffect(CollisionEffect effect) {
        this.collisionEffect = effect;
        switch (this.collisionEffect) {
            case NONE:
                this.colliderComponent.setSensor(true);
                break;
            case SLOW:
                if (!AoE) {
                    this.colliderComponent.setSensor(true);
                } //hitbox should already be a sensor
                break;
            case DIVERT:
            case DAMAGE:
            default:
                this.colliderComponent.setSensor(false);
                break;
        }
        return this;
    }

    /**
     * Collision handler - adds effect on collision start
     * @param me the fixture associated with the ColliderComponent attached to this Entity
     * @param other same but for the other object involved
     */
    private void onCollisionStart(Fixture me, Fixture other) {
        //check collision has been triggered by the right thing
        if (AoE) {
            if (this.hitboxComponent.getFixture() != me) {
                return;
            }
        } else {
            if (this.colliderComponent.getFixture() != me) {
                return;
            }
        }

        Entity target = ((BodyUserData) other.getBody().getUserData()).entity;
        if (target.getComponent(PlayerActions.class) != null && effectTarget == EffectTarget.NPC) {
            //incorrect target
            return;
        }
        if (target.getComponent(PhysicsMovementComponent.class) != null && effectTarget == EffectTarget.PLAYER) {
            //incorrect target
            return;
        }
        switch (this.getCollisionEffect()) {
            case SLOW:
                PlayerActions playerActions = target.getComponent(PlayerActions.class);
                PhysicsMovementComponent npcMovementComponent = target.getComponent(PhysicsMovementComponent.class);
                if (playerActions != null) {
                    //player character
                    Vector2 speed = playerActions.getPlayerSpeed();
                    speed.x = (this.speedModifier * speed.x);
                    speed.y = (this.speedModifier * speed.y);
                    break;
                }  else if (npcMovementComponent != null) {
                    //npc
                    Vector2 speed = npcMovementComponent.getSpeed();
                    speed.x = (this.speedModifier * speed.x);
                    speed.y = (this.speedModifier * speed.y);
                    //this is working without using the setter method but maybe we should anyway?
                    break;
                } else {
                    break;
                }
            case DAMAGE:
                CombatStatsComponent targetStats = target.getComponent(CombatStatsComponent.class);
                if (targetStats != null) {
                    CombatStatsComponent combatStats = new CombatStatsComponent(1, damage);
                    targetStats.hit(combatStats);
                    combatStats.dispose();
                    //falls through to knockback
                }
            case KNOCKBACK:
                PhysicsComponent targetPhysicsComponent = target.getComponent(PhysicsComponent.class);
                if (targetPhysicsComponent != null && knockbackForce > 0f) {
                    Body targetBody = targetPhysicsComponent.getBody();
                    Vector2 direction = target.getCenterPosition().sub(entity.getCenterPosition());
                    Vector2 impulse = direction.setLength(knockbackForce);
                    targetBody.applyLinearImpulse(impulse, targetBody.getWorldCenter(), true);
                }
                break;
            default:
                break;
        }
    }

    /**
     * Collision handler - removes effect when collision ends
     * @param me the fixture associated with the ColliderComponent attached to this Entity
     * @param other same but for the other object involved
     */
    private void onCollisionEnd(Fixture me, Fixture other) {
        //check collision has been triggered by the right thing
        if (AoE) {
            if (this.hitboxComponent.getFixture() != me) {
                return;
            }
        } else {
            if (this.colliderComponent.getFixture() != me) {
                return;
            }
        }
        Entity target = ((BodyUserData) other.getBody().getUserData()).entity;
        if (target.getComponent(PlayerActions.class) != null && effectTarget == EffectTarget.NPC) {
            //incorrect target
            return;
        }
        if (target.getComponent(PhysicsMovementComponent.class) != null && effectTarget == EffectTarget.PLAYER) {
            //incorrect target
            return;
        }
        switch (this.getCollisionEffect()) {
            case SLOW:
                PlayerActions playerActions = target.getComponent(PlayerActions.class);
                PhysicsMovementComponent npcMovementComponent = target.getComponent(PhysicsMovementComponent.class);
                if (playerActions != null) {
                    //player character
                    Vector2 speed = playerActions.getPlayerSpeed();
                    speed.x = (1f/this.speedModifier * speed.x);
                    speed.y = (1f/this.speedModifier * speed.y);
                    //could also use resetSpeed() if this leads to floating point errors
                    break;
                } else if (npcMovementComponent != null) {
                    //npc
                    Vector2 speed = npcMovementComponent.getSpeed();
                    speed.x = (1f/this.speedModifier * speed.x);
                    speed.y = (1f/this.speedModifier * speed.y);
                    //could also use resetSpeed() as above
                    break;
                } else {
                    break;
                }
            default:
                break;
        }
    }

}