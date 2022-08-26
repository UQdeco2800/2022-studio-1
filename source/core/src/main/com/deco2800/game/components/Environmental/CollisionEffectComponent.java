package com.deco2800.game.components.Environmental;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.deco2800.game.components.player.PlayerActions;
import com.deco2800.game.entities.Entity;
import com.deco2800.game.physics.BodyUserData;
import com.deco2800.game.physics.components.ColliderComponent;
import com.deco2800.game.physics.components.PhysicsComponent;
import com.deco2800.game.components.Component;

/**
 * class containing collision effects defined by the CollisionEffect enum
 * Entities implementing this must also implement ColliderComponent, PhysicsComponent & EnvironmentalComponent
 * Entities can have one collision effect only, as described by the enum
 */
public class CollisionEffectComponent extends Component {

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
    private float speedModifier;

    public CollisionEffectComponent(CollisionEffect collisionEffect) {
        this.collisionEffect = collisionEffect;
    }

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

    public CollisionEffectComponent setCollisionEffect(CollisionEffect effect) {
        this.collisionEffect = effect;
        if (this.collisionEffect == CollisionEffect.DIVERT) {
            this.colliderComponent.setSensor(false);
        } else {
            this.colliderComponent.setSensor(true);
        }
        return this;
    }

    private void onCollisionStart(Fixture me, Fixture other) {
        if (this.colliderComponent.getFixture() != me) {
            //not triggered by hitbox - ignore
            //NB this could be changed to a different sized hitboxcomponent for AoE?
            return;
        }
        switch (this.getCollisionEffect()) {
            case DIVERT:
                break;
            case SLOW:
                Entity target = ((BodyUserData) other.getBody().getUserData()).entity;
                PlayerActions playerActions = target.getComponent(PlayerActions.class);
                if (playerActions != null) {
                    //player character
                    Vector2 speed = playerActions.getPlayerSpeed();
                    speed.x = (this.speedModifier * speed.x);
                    speed.y = (this.speedModifier * speed.y);
                }  //TODO what about other entities (enemies)?
            default:
                break;
        }
    }

    private void onCollisionEnd(Fixture me, Fixture other) {
        if (this.colliderComponent.getFixture() != me) {
            //not triggered by hitbox - ignore
            //NB this could be changed to a different sized hitboxcomponent instead of collidercomponent for AoE?
            return;
        }
        switch (this.getCollisionEffect()) {
            case DIVERT:
                break;
            case SLOW:
                Entity target = ((BodyUserData) other.getBody().getUserData()).entity;
                PlayerActions playerActions = target.getComponent(PlayerActions.class);
                if (playerActions != null) {
                    //player character
                    Vector2 speed = playerActions.getPlayerSpeed();
                    speed.x = (1f/this.speedModifier * speed.x);
                    speed.y = (1f/this.speedModifier * speed.y);
                }
                //TODO what about other entities (enemies)?
            default:
                break;
        }
    }

}