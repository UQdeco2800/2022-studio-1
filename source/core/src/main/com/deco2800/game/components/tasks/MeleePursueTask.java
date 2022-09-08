package com.deco2800.game.components.tasks;

import com.badlogic.gdx.math.Vector2;
import com.deco2800.game.ai.tasks.AITaskComponent;
import com.deco2800.game.ai.tasks.DefaultTask;
import com.deco2800.game.ai.tasks.PriorityTask;
import com.deco2800.game.entities.Entity;
import com.deco2800.game.physics.PhysicsEngine;
import com.deco2800.game.physics.PhysicsLayer;
import com.deco2800.game.physics.raycast.RaycastHit;
import com.deco2800.game.rendering.DebugRenderer;
import com.deco2800.game.services.ServiceLocator;

/** Chases a target entity regardless of distance or line of sight.
 * Will either walk around or attack objects in its path, depending on whether
 * the object is attackable
 */
public class MeleePursueTask extends DefaultTask implements PriorityTask {
    private final Entity target;
    private final int priority;
    private final PhysicsEngine physics;
    private final DebugRenderer debugRenderer;
    private MovementTask movementTask;

    private AITaskComponent aitaskComponent;
    private Entity entity;

    public MeleePursueTask(Entity target, int priority) {
        this.target = target;
        this.priority = priority;
        physics = ServiceLocator.getPhysicsService().getPhysics();
        debugRenderer = ServiceLocator.getRenderService().getDebug();
    }

    @Override
    public int getPriority() {
        return this.priority;
    }

    public void registerEntity(Entity entity) {
        this.entity = entity;
    }

    @Override
    public void start() {
        super.start();
        movementTask = new MovementTask(target.getPosition());
        movementTask.create(owner);
        movementTask.start();
    }

    @Override
    public void update() {
        //I think this is where the algorithm lives!
        movementTask.setTarget(target.getPosition());
        movementTask.update();
        if (movementTask.getStatus() != Status.ACTIVE) {
            movementTask.start();
        }
    }

    /**
     * todo pathfinding! options:
     * wait for structureservice to be initialised along with the environmentalcollision thing & use them to pathfind ahead of time
     * Note that this would require either recalculation of the whole movement path every time the game updates or some way to deal with
     * being knocked aside by the player
     * NB all the buildings have healthbars so you'd really only need to check environmentalCollision EXCEPT that movementTask has
     * a fuckin uhhh abort movement if you haven't moved in a while feature?
     * OR use collisionComponent somehow to check collisions & combatStats (need to figure out how to pass the entity into the task)
     * OR use raycasting somehow to check LOS
     * Maybe make the target the nearest building unless you have LOS to the crystal, so two different tasks? HEY THAT'S A GOOD IDEA
     * three tasks - avoid obstacle (if you've hit something), move to nearest building (use StructureService) and move to crystal
     * 4 - move back and forward to repeatedly trigger attack? Or just manually repeatedly trigger attack - call a collision
     * */

    public void stop() {
        super.stop();
        movementTask.stop();
    }


}
