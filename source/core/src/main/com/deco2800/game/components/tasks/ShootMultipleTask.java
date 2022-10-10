package com.deco2800.game.components.tasks;

import java.util.ArrayList;
import java.util.Map;
import java.util.Map.Entry;

import org.slf4j.LoggerFactory;
import org.slf4j.Logger;

import com.badlogic.gdx.math.Vector2;
import com.deco2800.game.entities.Entity;
import com.deco2800.game.entities.EntityService;
import com.deco2800.game.services.DayNightCycleService;
import com.deco2800.game.services.DayNightCycleStatus;
import com.deco2800.game.services.ServiceLocator;

public class ShootMultipleTask extends ShootTask {
        Logger logger = LoggerFactory.getLogger(ShootMultipleTask.class);

        private ArrayList<Entity> targets;

        public ShootMultipleTask(ArrayList<Entity> targets, float viewDistance) {
                super(targets.size() > 0 ? targets.get(0) : null, 0, viewDistance, 0f);
                this.targets = targets;
                ServiceLocator.getDayNightCycleService().getEvents().addListener(
                                DayNightCycleService.EVENT_PART_OF_DAY_PASSED,
                                this::updateTargets);
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
                if (partOfDay == DayNightCycleStatus.NIGHT) {

                        targets.clear();
                        Map<String, Entity> namedEntities = ServiceLocator.getEntityService().getAllNamedEntities();

                        for (Entry<String, Entity> entry : namedEntities.entrySet()) {
                                if (entry.getKey().contains("Enemy")) {
                                        targets.add(entry.getValue());
                                }
                        }

                        target = targets.size() > 0 ? targets.get(0) : null;
                }
        }

        @Override
        public void update() {
                EntityService entityService = ServiceLocator.getEntityService();
                Vector2 ownerPosition = owner.getEntity().getPosition();
                for (Entity entity : targets) {
                        float currentDistance = super.getDistanceToTarget();
                        if (entityService.getNamedEntity(entity.getName()) != null) {
                                if (ownerPosition.dst(entity.getPosition()) < currentDistance) {
                                        this.target = entity;
                                }
                        }
                }
                super.update();
        }

}
