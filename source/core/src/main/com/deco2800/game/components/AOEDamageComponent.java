package com.deco2800.game.components;

import com.badlogic.gdx.math.GridPoint2;
import com.deco2800.game.entities.Entity;
import com.deco2800.game.physics.components.HitboxComponent;
import com.deco2800.game.services.GameTime;
import com.deco2800.game.services.RangeService;
import com.deco2800.game.services.ServiceLocator;

import java.util.ArrayList;

public class AOEDamageComponent extends Component {

        private short targetLayer;
        private int rangeRadius;
        private int numTargets;
        private Entity[] targets;

        private CombatStatsComponent combatStats;
        private HitboxComponent hitbox;

        private GameTime gameTime;
        private int updateRate = 2500; // time inbetween each update
        private long lastUpdate = 0;

        public AOEDamageComponent(int numTargets, int radiusRange, int attackRate) {
                // this.targetLayer = targetLayer;
                this.numTargets = numTargets;
                this.rangeRadius = radiusRange;
                this.updateRate = attackRate;

                targets = new Entity[numTargets];

                this.gameTime = ServiceLocator.getTimeSource();
        }

        @Override
        public void create() {
                combatStats = entity.getComponent(CombatStatsComponent.class);
                hitbox = entity.getComponent(HitboxComponent.class);
        }

        @Override
        public void update() {

                if (gameTime.getTime() > lastUpdate + updateRate) {
                        updateTargets();
                        damageTargets();
                        lastUpdate = gameTime.getTime();
                }
        }

        /**
         * checks all tiles within the specified radius, and records the closest enemy
         * entities in the targets array.
         */
        private void updateTargets() {
                int locationX = 0;
                int locationY = 0;
                targets = new Entity[numTargets];
                RangeService rangeService = ServiceLocator.getRangeService();
                Entity current = getEntity();
                if (!current.getName().contains("emp")) {
                        String location = current.getName().substring(7, 13);
                        locationX = Integer.parseInt(location.substring(0, 2));
                        locationY = Integer.parseInt(location.substring(4, 6));
                }
                if (locationX != 0 && locationY != 0) {

                        GridPoint2 newGridy = new GridPoint2(locationX, locationY);

                        ArrayList<Entity> tilesInRadius = rangeService.perimeter(current, newGridy);

                        for (Entity target : tilesInRadius) {
                                if (target != null && target.getName().contains("Mr")) {
                                        boolean slotFilled = false;
                                        for (int j = 0; j < numTargets; j++) {
                                                if (targets[j] == null) {
                                                        targets[j] = target;
                                                        slotFilled = true;
                                                        break;
                                                }
                                        }

                                        if (slotFilled)
                                                break;

                                        for (int j = 0; j < numTargets; j++) {
                                                if (getDistanceToTarget(target) < getDistanceToTarget(targets[j])) {
                                                        targets[j] = target;
                                                }
                                        }
                                }

                        }
                }
        }

        /**
         * returns the magnitude of the vector defined between the target and the owner
         * of this component
         * 
         * @param target target
         * @return distance between the target and the owner
         */
        private float getDistanceToTarget(Entity target) {
                return this.getEntity().getPosition().dst(target.getPosition());
        }

        /**
         * Hits all targets in the targets array
         */
        private void damageTargets() {
                for (Entity target : targets) {
                        if (target != null) {
                                target.getComponent(CombatStatsComponent.class).hit(combatStats);
                        }
                }
        }

}
