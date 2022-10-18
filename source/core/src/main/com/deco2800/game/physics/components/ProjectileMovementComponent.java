package com.deco2800.game.physics.components;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.deco2800.game.areas.terrain.TerrainComponent;
import com.deco2800.game.components.CombatStatsComponent;
import com.deco2800.game.entities.Entity;
import com.deco2800.game.entities.UGS;
import com.deco2800.game.services.GameTime;
import com.deco2800.game.services.ServiceLocator;

public class ProjectileMovementComponent extends PhysicsMovementComponent {

        private Vector2 origin;
        private final float MAX_TRAVEL_DISTANCE = 500f;
        private GameTime gameTime = ServiceLocator.getTimeSource();;
        private int updateRate = 100; // time inbetween each update
        private long lastUpdate = 0;

        public void create() {
                super.create();
                physicsComponent.getBody().setBullet(true);
                origin = entity.getPosition();

        }

        @Override
        public void update() {
                super.update();

                if (gameTime.getTime() > lastUpdate + updateRate) {
                        checkCollision();
                }

                if (Math.abs(getEntity().getPosition().dst(origin)) > MAX_TRAVEL_DISTANCE) {
                        System.out.println("Disposing of Projectile");
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

        public void checkCollision() {
                Entity projectile = getEntity();
                Vector2 worldPosOfProjectile = projectile.getPosition();
                GridPoint2 gridPosOfProjectile = ServiceLocator.getEntityService().getNamedEntity("terrain").
                        getComponent(TerrainComponent.class).worldToTilePosition(worldPosOfProjectile.x, worldPosOfProjectile.y);
                String ugsKey = UGS.generateCoordinate(gridPosOfProjectile.x, gridPosOfProjectile.y + 1);
                Entity underTheProjectile = ServiceLocator.getUGSService().getTile(ugsKey).getEntity();

                if (underTheProjectile != null && !underTheProjectile.getName().contains("Mr")) {
                        if (underTheProjectile.getName().equals("player")) {
                                underTheProjectile.getComponent(CombatStatsComponent.class).hit(projectile.getComponent(CombatStatsComponent.class));
                        }
                        projectile.getComponent(CombatStatsComponent.class).setHealth(1);
                }
        }

}
