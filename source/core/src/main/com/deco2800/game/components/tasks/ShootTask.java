package com.deco2800.game.components.tasks;

import com.deco2800.game.ai.tasks.DefaultTask;
import com.deco2800.game.ai.tasks.PriorityTask;
import com.deco2800.game.services.GameTime;
import com.deco2800.game.services.ServiceLocator;
import com.deco2800.game.physics.PhysicsEngine;
import com.deco2800.game.physics.PhysicsLayer;
import com.deco2800.game.entities.Entity;
import com.deco2800.game.entities.factories.ProjectileFactory;
import com.deco2800.game.physics.raycast.RaycastHit;
import com.deco2800.game.rendering.DebugRenderer;
import com.badlogic.gdx.math.Vector2;

public class ShootTask extends DefaultTask implements PriorityTask {
    protected Entity target;
    private static final int SECOND = 500;
    protected final GameTime TotalTime;
    private long taskEnd;
    private final int priority;
    private final float viewDistance;
    private final float maxChaseDistance;
    private final PhysicsEngine physics;
    private final DebugRenderer debugRenderer;
    private final RaycastHit hit = new RaycastHit();

    /**
     * @param target       The entity to chase.
     * @param priority     Task priority when chasing (0 when not chasing).
     * @param viewDistance Maximum distance from the entity at which chasing can
     *                     start.
     */
    public ShootTask(Entity target, int priority, float viewDistance, float maxChaseDistance) {
        this.target = target;
        this.priority = priority;
        this.maxChaseDistance = maxChaseDistance;
        this.TotalTime = ServiceLocator.getTimeSource();
        this.viewDistance = viewDistance;
        physics = ServiceLocator.getPhysicsService().getPhysics();
        debugRenderer = ServiceLocator.getRenderService().getDebug();
    }

    @Override
    public void start() {
        super.start();
        taskEnd = TotalTime.getTime() + (SECOND);
    }

    @Override
    public void update() {
        Entity entity = this.owner.getEntity();
        if (TotalTime.getTime() >= taskEnd) {
            ProjectileFactory.createProjectile(entity, target);
            taskEnd = TotalTime.getTime() + (SECOND);
        }
    }

    @Override
    public int getPriority() {
        if (status == Status.ACTIVE) {
            return getActivePriority();
        }

        return getInactivePriority();
    }

    protected float getDistanceToTarget() {
        if (target == null)
            return -1f;
        return owner.getEntity().getPosition().dst(target.getPosition());
    }

    private int getActivePriority() {
        float dst = Math.abs(getDistanceToTarget());

        if (dst > maxChaseDistance || !isTargetVisible()) {
            return -1; // Too far, stop chasing
        }
        return priority;
    }

    private int getInactivePriority() {
        float dst = getDistanceToTarget();
        if (dst < viewDistance && isTargetVisible()) {
            return priority;
        }
        return -1;
    }

    private boolean isTargetVisible() {
        if (target == null)
            return false;

        Vector2 from = owner.getEntity().getCenterPosition();
        Vector2 to = target.getCenterPosition();
        // If there is an obstacle in the path to the player, not visible.
        if (physics.raycast(from, to, PhysicsLayer.OBSTACLE, hit)) {
            debugRenderer.drawLine(from, hit.point);
            return false;
        }
        debugRenderer.drawLine(from, to);
        return true;
    }
}
