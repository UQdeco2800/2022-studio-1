package com.deco2800.game.components.tasks;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;
import com.deco2800.game.ai.tasks.DefaultTask;
import com.deco2800.game.ai.tasks.PriorityTask;
import com.deco2800.game.entities.Entity;
import com.deco2800.game.physics.PhysicsEngine;
import com.deco2800.game.physics.PhysicsLayer;
import com.deco2800.game.physics.raycast.RaycastHit;
import com.deco2800.game.rendering.AnimationRenderComponent;
import com.deco2800.game.rendering.DebugRenderer;
import com.deco2800.game.services.ServiceLocator;

/** Chases a target entity until they get too far away or line of sight is lost */
public class RangedMovementTask extends DefaultTask implements PriorityTask {
    private final Entity target;

    private final float range;
    private final int priority;
    private final float viewDistance;
    private final float maxChaseDistance;
    private final PhysicsEngine physics;
    private final DebugRenderer debugRenderer;
    private final RaycastHit hit = new RaycastHit();
    private MovementTask movementTask;

    /**
     * @param target The entity to chase.
     * @param priority Task priority when chasing (0 when not chasing).
     * @param viewDistance Maximum distance from the entity at which chasing can start.
     * @param maxChaseDistance Maximum distance from the entity while chasing before giving up.
     */
    public RangedMovementTask(Entity target, int priority, float range, float viewDistance, float maxChaseDistance) {
        this.target = target;
        this.range = range;
        this.priority = priority;
        this.viewDistance = viewDistance;
        this.maxChaseDistance = maxChaseDistance;
        physics = ServiceLocator.getPhysicsService().getPhysics();
        debugRenderer = ServiceLocator.getRenderService().getDebug();
    }

    @Override
    public void start() {
        super.start();
        movementTask = new MovementTask(target.getPosition());
        movementTask.create(owner);
        animationDirection((int)target.getPosition().x,(int)target.getPosition().y);
        movementTask.start();
    }

    @Override
    public void update() {
        movementTask.setTarget(target.getPosition());
        movementTask.update();
        animationDirection((int)target.getPosition().x,(int)target.getPosition().y);

        if (movementTask.getStatus() != Status.ACTIVE) {
            movementTask.start();
        }
    }

    @Override
    public void stop() {
        super.stop();
        movementTask.stop();
        animationDirection((int)target.getPosition().x,(int)target.getPosition().y);
    }

    @Override
    public int getPriority() {
        if (status == Status.ACTIVE) {
            return getActivePriority();
        }

        return getInactivePriority();
    }

    private float getDistanceToTarget() {
        return owner.getEntity().getPosition().dst(target.getPosition());
    }

    private int getActivePriority() {
        float dst = getDistanceToTarget();
        if (dst <= range || !isTargetVisible()) {
            stop();
            return -1; // Stop and get ready to shoot
        } else if (dst > maxChaseDistance || !isTargetVisible()) {
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

    //Class takes input of target values and compares current position
    private void animationDirection(int xValue, int yValue ) {
        //Eel current position
        int eelCurrentPosX = (int)this.owner.getEntity().getPosition().x;
        int eelCurrentPosY = (int)this.owner.getEntity().getPosition().y;

        checkAnimations();
        if (eelCurrentPosX < xValue && eelCurrentPosY > yValue) {
            this.owner.getEntity().getComponent(AnimationRenderComponent.class).startAnimation("fr");
        } else if (eelCurrentPosX < xValue && eelCurrentPosY < yValue) {
            this.owner.getEntity().getComponent(AnimationRenderComponent.class).startAnimation("br");
        } else if (eelCurrentPosX > xValue && eelCurrentPosY > yValue) {
            this.owner.getEntity().getComponent(AnimationRenderComponent.class).startAnimation("fl");
        } else if (eelCurrentPosX > xValue && eelCurrentPosY < yValue) {
            this.owner.getEntity().getComponent(AnimationRenderComponent.class).startAnimation("bl");
        }

        this.owner.getEntity().getComponent(AnimationRenderComponent.class).scaleEntity();
        this.owner.getEntity().setScale(1.2f, 1.2f);
    }

    //Checks all animation directions
    private void checkAnimations() {
        if (!(this.owner.getEntity().getComponent(AnimationRenderComponent.class).hasAnimation("fr"))) {
            this.owner.getEntity().getComponent(AnimationRenderComponent.class).
                    addAnimation("fr", 0.1f, Animation.PlayMode.LOOP);
        }

        if (!(this.owner.getEntity().getComponent(AnimationRenderComponent.class).hasAnimation("br"))) {
            this.owner.getEntity().getComponent(AnimationRenderComponent.class).
                    addAnimation("br", 0.1f, Animation.PlayMode.LOOP);
        }

        if (!(this.owner.getEntity().getComponent(AnimationRenderComponent.class).hasAnimation("fl"))) {
            this.owner.getEntity().getComponent(AnimationRenderComponent.class).
                    addAnimation("fl", 0.1f, Animation.PlayMode.LOOP);
        }

        if (!(this.owner.getEntity().getComponent(AnimationRenderComponent.class).hasAnimation("bl"))) {
            this.owner.getEntity().getComponent(AnimationRenderComponent.class).
                    addAnimation("bl", 0.1f, Animation.PlayMode.LOOP);
        }
    }
}
