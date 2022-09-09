package com.deco2800.game.components.tasks;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.deco2800.game.ai.tasks.DefaultTask;
import com.deco2800.game.ai.tasks.PriorityTask;
import com.deco2800.game.ai.tasks.TaskRunner;
import com.deco2800.game.components.CombatStatsComponent;
import com.deco2800.game.entities.Entity;
import com.deco2800.game.physics.BodyUserData;
import com.deco2800.game.physics.PhysicsEngine;
import com.deco2800.game.physics.PhysicsLayer;
import com.deco2800.game.physics.components.HitboxComponent;
import com.deco2800.game.rendering.DebugRenderer;
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
    private final PhysicsEngine physics;
    private final DebugRenderer debugRenderer;
    private MovementTask movementTask;
    private DayNightCycleService dayNightCycleService;
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
        private Vector2 v;
        private float x;
        private float y;
        RotationDirection(float x, float y) {
            this.x = x;
            this.y = y;
            this.v = new Vector2(x, y);
        }
    }

    /**
     * initialise the avoidance task. Task should get its entity registered using registerEntity() after creation
     * (must be done after creation as the entity doesn't exist yet
     * @param target the Entity this NPC is trying to reach
     */
    public MeleeAvoidObstacleTask(Entity target) {
        this.target = target;
        physics = ServiceLocator.getPhysicsService().getPhysics();
        debugRenderer = ServiceLocator.getRenderService().getDebug();
        dayNightCycleService = ServiceLocator.getDayNightCycleService();
        this.collisionEntities = new ArrayList<Entity>();
        //set rotation randomly to prevent identical behaviour among all entities
        this.rotation  = (int)(Math.random()*2) == 0 ? RotationDirection.LEFT  : RotationDirection.RIGHT;
        adjusting = false;
        resetAdjustCountdown();
    }

    /**
     * must be called after task, component, and entity are attached
     * registers collision listeners for the entity
     */
    public void registerEntityEvents() {
        entity = owner.getEntity();
        entity.getEvents().addListener("collisionStart", this::onCollisionStart);
        entity.getEvents().addListener("collisionEnd", this::onCollisionEnd);
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
        //check it's the right physics layer TODO check this works - invisible wall. Might be able to just remove this bc of the moving check
/*        if (!PhysicsLayer.contains(me.getFilterData().categoryBits, other.getFilterData().categoryBits)) {
            return;
        }*/
        Entity coll = ((BodyUserData) other.getBody().getUserData()).entity;
        if (coll.getComponent(CombatStatsComponent.class) != null) {
            return;
        }

        //save the entity and set the last position for later checking
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
        if (collisionEntities.contains(coll)) {
            collisionEntities.remove(coll);
        }
    }

    /**
     * set a new target perpendicular to the direction of the previous target
     * Note this is only called when priority is highest (i.e. the entity is already not moving)
     */
    @Override
    public void start() {
        super.start();
        currentDirection = rotateVector(target.getPosition(), rotation);
        movementTask = new MovementTask(currentDirection);
        movementTask.create(owner);
        movementTask.start();
    }

    /**
     * update checks if the new direction has run into any obstacles that stopped movement
     * Will not need to check if collision has ended (I think)
     */
    @Override
    public void update() {
        movementTask.setTarget(target.getPosition());
        movementTask.update();
        if (movementTask.getStatus() != Status.ACTIVE) {
            movementTask.start();
        }

        //if another collision, rotate again
        if (!moving()) {
            currentDirection = rotateVector(currentDirection, rotation);
            movementTask.setTarget(currentDirection);
            movementTask.update();
        }

        // if no collisions, rotate opposite and walk for a bit (prevents walking in circles, I hope)
        if (collisionEntities.size() == 0) {
            RotationDirection invert = (rotation == RotationDirection.LEFT ? RotationDirection.RIGHT : RotationDirection.LEFT);
            movementTask.setTarget(rotateVector(currentDirection, invert));
            movementTask.update();
            adjusting = true;
        }

        // don't break adjusting until it's moved
        if (adjusting) {
            adjustCountdown--;
            if (adjustCountdown == 0) {
                adjusting = false;
                resetAdjustCountdown();
            }
        }
    }

    /**
     * gets the priority of this task.
     * @return 10 if the owner entity isn't moving and has collided with something that needs to be avoided rather than attacked
     */
    @Override
    public int getPriority() {
        //todo reinstate below
        /*if (dayNightCycleService.getCurrentCycleStatus() != DayNightCycleStatus.NIGHT
                && dayNightCycleService.getCurrentCycleStatus() != DayNightCycleStatus.DUSK) {
            return -1;
        }*/
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

    /**
     * reset the adjust countdown. Helper method so if I change numbers I don't have to change them twice
     */
    private void resetAdjustCountdown() {
        adjustCountdown = 10;
    }

    /**
     * get the priority of the task when active. Should be superseded by the meleeAttackTargetTask
     * @return active priority
     */
    private int getActivePriority() {
        if (collisionEntities.size() != 0 || adjusting) {
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
        if (!moving() && collisionEntities.size() != 0) {
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
        return (owner.getEntity().getPosition().dst2(lastPos) > 0.001f);
    }

}
