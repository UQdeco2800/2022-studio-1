package com.deco2800.game.components.tasks;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.deco2800.game.ai.tasks.AITaskComponent;
import com.deco2800.game.ai.tasks.DefaultTask;
import com.deco2800.game.ai.tasks.PriorityTask;
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

public class MeleeAttackObstacleTask extends DefaultTask implements PriorityTask {
    private final Entity target;
    private final PhysicsEngine physics;
    private final DebugRenderer debugRenderer;
    private DayNightCycleService dayNightCycleService;
    private Entity entity;
    private List<Entity> collisionEntities;
    private Vector2 lastPos;

    /**
     * initialise the avoidance task. Task should get its entity registered using registerEntity() after creation
     * (must be done after creation as the entity doesn't exist yet
     * @param target the Entity this NPC is trying to reach
     */
    public MeleeAttackObstacleTask(Entity target) {
        this.target = target;
        physics = ServiceLocator.getPhysicsService().getPhysics();
        debugRenderer = ServiceLocator.getRenderService().getDebug();
        dayNightCycleService = ServiceLocator.getDayNightCycleService();
        this.collisionEntities = new ArrayList<>();
    }

    public void registerEntityEvents() {
        entity = owner.getEntity();
        entity.getEvents().addListener("collisionStart", this::onCollisionStart);
        entity.getEvents().addListener("collisionEnd", this::onCollisionEnd);
    }

    /**
     * When a collision starts, set sentinel values and store the entity for ongoing action
     * @param me (Fixture) me
     * @param other (Fixture) other
     */
    private void onCollisionStart(Fixture me, Fixture other) {
        //check collision has been triggered by the right thing
        if (entity.getComponent(HitboxComponent.class).getFixture() != me) {
            return;
        }
        //check it's the right physics layer TODO check this works - invisible wall
/*        if (!PhysicsLayer.contains(me.getFilterData().categoryBits, other.getFilterData().categoryBits)) {
            return;
        }*/

        //set the last position for later checking (whether entity is still moving)
        lastPos = owner.getEntity().getPosition();
        Entity coll = ((BodyUserData) other.getBody().getUserData()).entity;
        if (coll.getComponent(CombatStatsComponent.class) != null
                && coll.getComponent(AITaskComponent.class) != null) {
            collisionEntities.add(coll);
        }
    }

    /**
     * when collision ends, remove the colliding entity from the entities array
     * @param me (fixture) me
     * @param other (fixture) the other fixture
     */
    private void onCollisionEnd(Fixture me, Fixture other) {
        if (entity.getComponent(HitboxComponent.class).getFixture() != me) {
            return;
        }
        Entity coll = ((BodyUserData) other.getBody().getUserData()).entity;

        if (collisionEntities.contains(coll)) {
            collisionEntities.remove(coll);
        }
    }

    /**
     * Hits the thing it's colliding with
     * todo what if there are two?
     */
    @Override
    public void start() {
        super.start();
        Entity target = collisionEntities.get(collisionEntities.size()-1);
        owner.getEntity().getComponent(CombatStatsComponent.class).hit(
                target.getComponent(CombatStatsComponent.class));
    }

    /**
     * Continually hit the target.
     * todo what if there are two?
     */
    @Override
    public void update() {
        Entity target = collisionEntities.get(collisionEntities.size()-1);
        owner.getEntity().getComponent(CombatStatsComponent.class).hit(
                target.getComponent(CombatStatsComponent.class));
        if (target.getComponent(CombatStatsComponent.class).isDead()) {
            collisionEntities.clear();
        }
    }

    /**
     * gets the priority of this task.
     * @return 10 if the thing in front is stopping it from moving and is destroyable
     * -1 otherwise
     */
    @Override
    public int getPriority() {
        //todo reinstate below
/*        if (dayNightCycleService.getCurrentCycleStatus() != DayNightCycleStatus.NIGHT
                && dayNightCycleService.getCurrentCycleStatus() != DayNightCycleStatus.DUSK) {
            return 0;
        }*/
        if (!moving()
                && collisionEntities.size() != 0
                && collisionEntities.get(collisionEntities.size()-1).getComponent(CombatStatsComponent.class) != null) {
            return 4;
        }
        return -1;
    }

    private boolean moving() {
        if (lastPos  == null) {
            return true;
        }
        return (owner.getEntity().getPosition().dst2(lastPos) > 0.001f);
    }

}
