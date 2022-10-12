package com.deco2800.game.physics.components;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.deco2800.game.services.ServiceLocator;

public class ProjectileMovementComponent extends PhysicsMovementComponent {

        private Vector2 origin;
        private final float MAX_TRAVEL_DISTANCE = 500f;

        public void create() {
                super.create();
                physicsComponent.getBody().setBullet(true);
                origin = entity.getPosition();
        }

        @Override
        public void update() {
                super.update();

                if (Math.abs(getEntity().getPosition().dst(origin)) > MAX_TRAVEL_DISTANCE) {
                        ServiceLocator.getUGSService().removeEntity(getEntity().getName());
                }
        }

        @Override
        protected Vector2 getDirection() {
                if (getTarget() == null) {
                        return new Vector2(0, 0);
                }

                return getTarget().cpy().sub(origin).nor();
        }

        @Override
        protected void setToVelocity(Body body, Vector2 desiredVelocity) {
                body.setLinearVelocity(desiredVelocity);
        }

}
