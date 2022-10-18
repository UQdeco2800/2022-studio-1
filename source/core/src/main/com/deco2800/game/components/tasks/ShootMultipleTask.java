package com.deco2800.game.components.tasks;

import com.badlogic.gdx.math.Vector2;
import com.deco2800.game.entities.Entity;
import com.deco2800.game.entities.UGS;
import com.deco2800.game.entities.factories.ProjectileFactory;
import com.deco2800.game.services.DayNightCycleService;
import com.deco2800.game.services.DayNightCycleStatus;
import com.deco2800.game.services.ServiceLocator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Map;
import java.util.Map.Entry;

public class ShootMultipleTask extends ShootTask {
        Logger logger = LoggerFactory.getLogger(ShootMultipleTask.class);

        private ArrayList<Entity> targets;
        private final int updateTimeDelta = 5000;
        private long taskEnd;

        public ShootMultipleTask(ArrayList<Entity> targets, float viewDistance) {
                super(targets.size() > 0 ? targets.get(0) : null, 1, viewDistance, 500f);
                this.targets = targets;
                ServiceLocator.getDayNightCycleService().getEvents().addListener(
                                DayNightCycleService.EVENT_PART_OF_DAY_PASSED,
                                this::updateTargets);
        }

        @Override
        public void start() {
                super.start();
                taskEnd = TotalTime.getTime() + (updateTimeDelta);
                System.out.println("Started");
                updateTargets(DayNightCycleStatus.NIGHT);
        }

        /**
         * Adds a target to the target list
         * 
         * @param target target to add
         */
        public void addTarget(Entity target) {
                if (target == null) {
                        logger.error("[ShootMultipleTask] addTarget: target is null. Ignoring.");
                        return;
                }

                targets.add(target);
        }

        /**
         * Clears the current targets list, adds all new enemies to the targets list,
         * and sets the current target to the first added enemy.
         * 
         * @param partOfDay day cycle status on function call
         */
        private void updateTargets(DayNightCycleStatus partOfDay) {
                System.out.println("day night cycle change: " + partOfDay.name());
                if (partOfDay == DayNightCycleStatus.NIGHT) {

                        targets.clear();
                        Map<String, Entity> namedEntities = ServiceLocator.getEntityService().getAllNamedEntities();

                        for (Entry<String, Entity> entry : namedEntities.entrySet()) {
                                if (entry.getKey().contains("Enemy") && !entry.getValue().getName().contains("eelP")) {
                                        targets.add(entry.getValue());
                                        System.out.println("target added: " + entry.getValue().getName());
                                }
                        }

                        target = targets.size() > 0 ? targets.get(0) : null;
                }
        }

        @Override
        public void update() {
                Entity owner = this.owner.getEntity();
                // System.out.println("[ShootTask Update] {Owner: " + owner.getName() + "}
                // {Time-taskEnd:"
                // + TotalTime.getTime()
                // + " - "
                // + taskEnd + "}");
                if (TotalTime.getTime() >= taskEnd) {

                        if (this.target == null) {
                                return;
                        }
                        Vector2 ownerPosition = owner.getPosition();
                        UGS ugs = ServiceLocator.getUGSService();
                        float currentDistance = Math.abs(super.getDistanceToTarget());

                        for (Entity target : targets) {
                                if (currentDistance == -1f) {
                                        this.target = target;
                                        currentDistance = super.getDistanceToTarget();
                                        continue;
                                }

                                if (ugs.getEntityByName(target.getName()) != null) {
                                        if (ownerPosition.dst(target.getPosition()) < currentDistance) {
                                                this.target = target;
                                                currentDistance = super.getDistanceToTarget();
                                        }
                                }
                        }

                        System.out.println("Shooting a projectile");
                        ProjectileFactory.createProjectile(owner, target);
                        taskEnd = TotalTime.getTime() + (updateTimeDelta);
                }
        }

        @Override
        protected boolean isTargetVisible() {
                return true;
        }

}
