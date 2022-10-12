package com.deco2800.game.physics.components;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;

public class ProjectileMovementComponent extends PhysicsMovementComponent {

        private Vector2 direction;
        private Vector2 origin;

        public void create() {
                super.create();
                physicsComponent.getBody().setBullet(true);
                origin = entity.getPosition();
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
                System.out.println("set velocity");
        }

}
