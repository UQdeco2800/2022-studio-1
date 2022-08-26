package com.deco2800.game.components.Environmental;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Fixture;
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
 * Entities can have one collision effect only, as described by the enum
 * for area of effect, specify a hitboxcomponent and size
 */
public class CollisionEffectComponent extends Component {

    /**
     * Collision effect
     */
    public enum CollisionEffect {
        DIVERT,
        SLOW,
        DAMAGE,
        NONE;
    }

    private CollisionEffect collisionEffect;
    private ColliderComponent colliderComponent;
    private PhysicsComponent physicsComponent;
    private EnvironmentalComponent environmentalComponent;
    private HitboxComponent hitboxComponent;
    private float speedModifier;

    public CollisionEffectComponent(CollisionEffect collisionEffect) {
        this.collisionEffect = collisionEffect;
    }

    /**
     * initialise the collisionEffect. Note this happens after entity has been attached
     */
    @Override
    public void create() {
        entity.getEvents().addListener("collisionStart", this::onCollisionStart);
        entity.getEvents().addListener("collisionEnd", this::onCollisionEnd);
        this.physicsComponent = entity.getComponent(PhysicsComponent.class);
        this.colliderComponent = entity.getComponent(ColliderComponent.class);
        this.environmentalComponent = entity.getComponent(EnvironmentalComponent.class);
        this.speedModifier = environmentalComponent.getSpeedModifier();
        setCollisionEffect(this.collisionEffect);
    }

    public CollisionEffect getCollisionEffect() { return this.collisionEffect; }

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
            case SLOW:
                this.colliderComponent.setSensor(true);
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
        if (this.colliderComponent.getFixture() != me) {
            //not triggered by colliderComponent - ignore
            //NB this could be changed to a different sized hitboxcomponent for AoE?
            return;
        }
        switch (this.getCollisionEffect()) {
            case SLOW:
                Entity target = ((BodyUserData) other.getBody().getUserData()).entity;
                PlayerActions playerActions = target.getComponent(PlayerActions.class);
                PhysicsMovementComponent npcMovementComponent = target.getComponent(PhysicsMovementComponent.class);
                if (playerActions != null) {
                    //player character
                    Vector2 speed = playerActions.getPlayerSpeed();
                    speed.x = (this.speedModifier * speed.x);
                    speed.y = (this.speedModifier * speed.y);
                }  else if (npcMovementComponent != null) {
                    //npc
                    Vector2 speed = npcMovementComponent.getSpeed();
                    speed.x = (this.speedModifier * speed.x);
                    speed.y = (this.speedModifier * speed.y);
                    //this is working without using the setter method but maybe we should anyway?
                } else {
                    break;
            }
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
        if (this.colliderComponent.getFixture() != me) {
            //not triggered by colliderComponent - ignore
            //NB this could be changed to a different sized hitboxcomponent instead of collidercomponent for AoE
            return;
        }
        switch (this.getCollisionEffect()) {
            case SLOW:
                Entity target = ((BodyUserData) other.getBody().getUserData()).entity;
                PlayerActions playerActions = target.getComponent(PlayerActions.class);
                PhysicsMovementComponent npcMovementComponent = target.getComponent(PhysicsMovementComponent.class);
                if (playerActions != null) {
                    //player character
                    Vector2 speed = playerActions.getPlayerSpeed();
                    speed.x = (1f/this.speedModifier * speed.x);
                    speed.y = (1f/this.speedModifier * speed.y);
                    //could also use resetSpeed() if this leads to floating point errors
                } else if (npcMovementComponent != null) {
                    //npc
                    Vector2 speed = npcMovementComponent.getSpeed();
                    speed.x = (1f/this.speedModifier * speed.x);
                    speed.y = (1f/this.speedModifier * speed.y);
                    //could also use resetSpeed() as above
                } else {
                    break;
                }
            default:
                break;
        }
    }

}