package com.deco2800.game.components.tasks;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.deco2800.game.ai.tasks.DefaultTask;
import com.deco2800.game.ai.tasks.PriorityTask;
import com.deco2800.game.components.CombatStatsComponent;
import com.deco2800.game.components.player.PlayerActions;
import com.deco2800.game.entities.Entity;
import com.deco2800.game.physics.BodyUserData;
import com.deco2800.game.physics.components.HitboxComponent;
import com.deco2800.game.services.DayNightCycleService;
import com.deco2800.game.services.DayNightCycleStatus;
import com.deco2800.game.services.ServiceLocator;

import java.util.ArrayList;
import java.util.List;
/**
 * task that gets enemies to avoid obstacles. The algorithm is pretty simple:
 * task is given a random rotation direction (right or left) at startup and on collision with a non-destroyable object that
 * impedes movement, it will turn that direction and walk until (a) collision ends or (b) another collision occurs, at which
 * point they will turn in that same direction again.
 * After collision ends, task rotates in the inverse direction to its given rotation direction and walks for 10 update() cycles
 * before resuming normal pathfinding. This will (hopefully) prevent getting stuck repeatedly in the same 'bucket'
 */
public class MeleeAvoidObstacleTask extends DefaultTask implements PriorityTask {
    private final Entity target;
    private MovementTask movementTask;
    private final DayNightCycleService dayNightCycleService;
    private Entity entity;
    private List<Entity> collisionEntities;
    private Vector2 lastPos;
    private Vector2 currentDirection;
    private RotationDirection rotation;
    private boolean adjusting;
    private int adjustCountdown;
    private enum RotationDirection {
        LEFT(-1f, 1f),
        RIGHT(1f, -1f);
        private final float x;
        private final float y;
        RotationDirection(float x, float y) {
            this.x = x;
            this.y = y;
        }
    }

    /**
     * initialise the avoidance task. Task should get its entity registered using registerEntity() after creation
     * (must be done after creation as the entity doesn't exist yet
     * @param target the Entity this NPC is trying to reach
     */
    public MeleeAvoidObstacleTask(Entity target) {
        this.target = target;
        dayNightCycleService = ServiceLocator.getDayNightCycleService();
        this.collisionEntities = new ArrayList<>();
        resetAdjustCountdown();
        currentDirection = target.getPosition();
    }

    /**
     * must be called after task, component, and entity are attached
     * registers collision listeners for the entity
     */
    public void registerEntityEvents() {
        entity = owner.getEntity();
        entity.getEvents().addListener("collisionStart", this::onCollisionStart);
        entity.getEvents().addListener("collisionEnd", this::onCollisionEnd);
        Vector2 buffer = owner.getEntity().getScale();
        buffer.x /= 2;
        buffer.y /= 2;
    }

    /**
     * When a collision starts, set sentinel values and store the entity for further action if needed
     * @param me (Fixture) me
     * @param other (Fixture) other
     */
    private void onCollisionStart(Fixture me, Fixture other) {
        //check collision has been triggered by the right thing
        if (entity.getComponent(HitboxComponent.class).getFixture() != me) {
            return;
        }
        Entity coll = ((BodyUserData) other.getBody().getUserData()).entity;
        if (coll.getComponent(CombatStatsComponent.class) != null) {
            return;
        }
        // note this will also cause things with aoe effects to be added to the array - hopefully this isn't a big deal
        // but might be worth addressing in future sprints
        collisionEntities.add(coll);
        lastPos = owner.getEntity().getPosition();
    }

    /**
     * when collision ends, remove it from the array
     * @param me me
     * @param other the other fixture
     */
    private void onCollisionEnd(Fixture me, Fixture other) {
        //check collision has been triggered by the right thing
        if (entity.getComponent(HitboxComponent.class).getFixture() != me) {
            return;
        }
        //remove from array
        Entity coll = ((BodyUserData) other.getBody().getUserData()).entity;
        collisionEntities.remove(coll);
    }

    /**
     * set a new target perpendicular to the direction of the previous target
     * Note this is only called when priority is highest (i.e. the entity is already not moving)
     */
    @Override
    public void start() {
        super.start();
        lastPos = owner.getEntity().getPosition();
        Vector2 pos = owner.getEntity().getCenterPosition();
        Vector2 obsPos = collisionEntities.get(collisionEntities.size()-1).getCenterPosition();
        currentDirection = convertTargetToDirection(target.getPosition());

        if (isLeft(convertTargetToDirection(target.getCenterPosition()), pos, obsPos)) {
            rotation = RotationDirection.RIGHT;
        } else {
            rotation = RotationDirection.LEFT;
        }
        currentDirection = rotateVector(currentDirection, rotation);
        movementTask = new MovementTask(convertDirectionToTarget(currentDirection));
        movementTask.create(owner);
        movementTask.start();
    }

    /**
     * update checks if the new direction has run into any obstacles that stopped movement,
     * and adjusts direction if necessary
     */
    @Override
    public void update() {
        if (movementTask.getStatus() == Status.FAILED) {
            currentDirection = rotateVector(rotateVector(currentDirection, rotation), rotation);
            movementTask.setTarget(convertDirectionToTarget(currentDirection));
        }

        if (movementTask.getStatus() != Status.ACTIVE) {
            movementTask.start();
        }

        // if no collisions, rotate opposite and walk for a bit (prevents walking in circles, I hope)
        if (collisionEntities.isEmpty()) {
            RotationDirection invert = (rotation == RotationDirection.LEFT ? RotationDirection.RIGHT : RotationDirection.LEFT);
            movementTask.setTarget(convertDirectionToTarget(rotateVector(currentDirection, invert)));
            movementTask.update();
            adjusting = true;
        }

        // don't break adjusting until it's moved
        if (adjusting) {
            adjustCountdown--;
            if (adjustCountdown == 0) {
                resetAdjustCountdown();
            }
        }
    }

    /**
     * gets the priority of this task.
     * @return -1 if it's not nighttime, otherwise active priority if active, inactive priority if inactive
     */
    @Override
    public int getPriority() {
        if (dayNightCycleService.getCurrentCycleStatus() != DayNightCycleStatus.NIGHT
                && dayNightCycleService.getCurrentCycleStatus() != DayNightCycleStatus.DUSK) {
            return -1;
        }
        if (status == Status.ACTIVE) {
            return getActivePriority();
        } else {
            return getInactivePriority();
        }
    }

    @Override
    public void stop() {
        super.stop();
        movementTask.stop();
    }

    private void resetAdjustCountdown() {
        adjustCountdown = 10;
        adjusting = false;
    }

    /**
     * get the priority of the task when active.
     * @return active priority
     */
    private int getActivePriority() {
        if (!collisionEntities.isEmpty() || adjusting) {
            return 3;
        } else {
            return -1;
        }
    }

    /**
     * get the priority of the task when inactive
     * @return inactive priority
     */
    private int getInactivePriority() {
        if (!moving() && !collisionEntities.isEmpty()) {
            return 3;
        } else {
            return -1;
        }
    }

    /**
     * rotate a vector 90 degrees to the left or right
     * @param vector the vector to be rotated
     * @param rotationDirection the direction in which it should be rotated
     * @return new, rotated vector
     */
    private Vector2 rotateVector(Vector2 vector, RotationDirection rotationDirection) {
        return new Vector2(vector.y * rotationDirection.x, vector.x * rotationDirection.y);
    }

    /**
     * checks if the owner entity is moving, used to check if a collision impedes the path
     * @return boolean of whether the owner entity is moving
     */
    private boolean moving() {
        if (lastPos == null) {
            return true;
        }
        return (owner.getEntity().getPosition().dst2(lastPos) > 0.0001f);
    }

    /**
     * convert point in 2d space to directional vector from position of owner entity
     * @param target target point
     * @return directional vector
     */
    private Vector2 convertTargetToDirection(Vector2 target) {
        return new Vector2(target.x - owner.getEntity().getPosition().x, target.y - owner.getEntity().getPosition().y);
    }

    /**
     * convert directional vector to target point in 2d space
     * @param direction to convert
     * @return target position
     */
    private Vector2 convertDirectionToTarget(Vector2 direction) {
        return new Vector2((owner.getEntity().getPosition().x + direction.x) * 10, (owner.getEntity().getPosition().y + direction.y)*10);
    }

    /**
     * calculates whether a point is to the left or right of a vector
     * @param dir directional vector of owner entity's movement
     * @param pos position of the owner entity
     * @param point the point to be calculated
     * @return true if point is on left of direction
     */
    private boolean isLeft(Vector2 dir, Vector2 pos, Vector2 point) {
        Vector2 p = new Vector2(pos.x - point.x, pos.y - point.y);
        return (p.x * dir.y - p.y * dir.x < 0);
    }

}
