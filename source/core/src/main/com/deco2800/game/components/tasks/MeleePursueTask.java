package com.deco2800.game.components.tasks;

import com.deco2800.game.ai.tasks.DefaultTask;
import com.deco2800.game.ai.tasks.PriorityTask;
import com.deco2800.game.entities.Entity;
import com.deco2800.game.services.DayNightCycleService;
import com.deco2800.game.services.DayNightCycleStatus;
import com.deco2800.game.services.ServiceLocator;

/**
 * Chases a target entity regardless of distance or line of sight.
 */
public class MeleePursueTask extends DefaultTask implements PriorityTask {
    private final Entity target;
    private MovementTask movementTask;
    private final DayNightCycleService dayNightCycleService;

    /**
     * chases an entity regardless of line of sight
     * @param target the entity to chase
     */
    public MeleePursueTask(Entity target) {
        this.target = target;
        dayNightCycleService = ServiceLocator.getDayNightCycleService();
    }

    /**
     * start chasing
     */
    @Override
    public void start() {
        super.start();
        movementTask = new MovementTask(target.getPosition());
        movementTask.create(owner);
        movementTask.start();
    }

    /**
     * update direction (for moving targets)
     */
    @Override
    public void update() {
        movementTask.setTarget(target.getPosition());
        movementTask.update();
        if (movementTask.getStatus() != Status.ACTIVE) {
            movementTask.start();
        }
    }

    /**
     * get priority
     * @return 0 if it's not dusk or night, 2 otherwise
     */
    @Override
    public int getPriority() {
        if (dayNightCycleService.getCurrentCycleStatus() != DayNightCycleStatus.NIGHT
            && dayNightCycleService.getCurrentCycleStatus() != DayNightCycleStatus.DUSK) {
            return 0;
        }
        return 2;
    }

    @Override
    public void stop() {
        super.stop();
        movementTask.stop();
    }

}
