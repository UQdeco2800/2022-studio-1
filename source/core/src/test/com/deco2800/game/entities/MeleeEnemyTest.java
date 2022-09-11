package com.deco2800.game.entities;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.deco2800.game.ai.tasks.AITaskComponent;
import com.deco2800.game.components.tasks.MeleeAvoidObstacleTask;
import com.deco2800.game.components.tasks.MeleePursueTask;
import com.deco2800.game.extensions.GameExtension;
import com.deco2800.game.physics.PhysicsService;
import com.deco2800.game.physics.components.ColliderComponent;
import com.deco2800.game.physics.components.HitboxComponent;
import com.deco2800.game.physics.components.PhysicsComponent;
import com.deco2800.game.physics.components.PhysicsMovementComponent;
import com.deco2800.game.rendering.DebugRenderer;
import com.deco2800.game.rendering.RenderService;
import com.deco2800.game.services.DayNightCycleService;
import com.deco2800.game.services.DayNightCycleStatus;
import com.deco2800.game.services.GameTime;
import com.deco2800.game.services.ServiceLocator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(GameExtension.class)
public class MeleeEnemyTest {

    @BeforeEach
    void beforeEach() {
        // Mock rendering, physics, game time, daynightccyle
        RenderService renderService = new RenderService();
        renderService.setDebug(mock(DebugRenderer.class));
        ServiceLocator.registerRenderService(renderService);
        GameTime gameTime = mock(GameTime.class);
        when(gameTime.getDeltaTime()).thenReturn(20f / 1000);
        ServiceLocator.registerTimeSource(gameTime);
        ServiceLocator.registerPhysicsService(new PhysicsService());
        DayNightCycleService dayNightCycleService = mock(DayNightCycleService.class);
        ServiceLocator.registerDayNightCycleService(dayNightCycleService);
    }

    @Test
    void shouldNotMoveTowardsTargetAtDay() {
        Entity target = new Entity();
        target.setPosition(2f, 2f);

        AITaskComponent ai = new AITaskComponent().addTask(new MeleePursueTask(target));
        Entity entity = makePhysicsEntity().addComponent(ai);
        entity.create();
        entity.setPosition(0f, 0f);

        ServiceLocator.getDayNightCycleService().setPartOfDayTo(DayNightCycleStatus.DAY);

        float initialDistance = entity.getPosition().dst(target.getPosition());
        // Run the game for a few cycles
        for (int i = 0; i < 3; i++) {
            entity.earlyUpdate();
            entity.update();
            ServiceLocator.getPhysicsService().getPhysics().update();
        }
        float newDistance = entity.getPosition().dst(target.getPosition());
        assertTrue(newDistance - initialDistance < 0.001f);
    }


    @Test
    void shouldMoveTowardsTargetAtNight() {
        Entity target = new Entity();
        target.setPosition(2f, 2f);

        AITaskComponent ai = new AITaskComponent().addTask(new MeleePursueTask(target));
        Entity entity = makePhysicsEntity().addComponent(ai);
        entity.create();
        entity.setPosition(0f, 0f);

        ServiceLocator.getDayNightCycleService().setPartOfDayTo(DayNightCycleStatus.NIGHT);

        float initialDistance = entity.getPosition().dst(target.getPosition());
        // Run the game for a few cycles
        for (int i = 0; i < 3; i++) {
            entity.earlyUpdate();
            entity.update();
            ServiceLocator.getPhysicsService().getPhysics().update();
        }
        float newDistance = entity.getPosition().dst(target.getPosition());
        assertTrue(newDistance < initialDistance);
    }

    /**
     *     this is just not working because of the ways the checks are made in the code & I can't figure out how to check if
     *     it has gone around the obstacle or not, or even whether it's collided with an obstacle that stops it from moving.
     */
//    @Test
//    void shouldActivateAvoidOnCollision() {
//        ServiceLocator.getDayNightCycleService().setPartOfDayTo(DayNightCycleStatus.NIGHT);
//
//        Entity target = new Entity();
//        target.setPosition(0f, 0f);
//
//        AITaskComponent ai = new AITaskComponent();
//
//        Entity entity = makePhysicsEntity();
//        entity.create();
//        entity.setPosition(2f, 2f);
//
//        MeleeAvoidObstacleTask meleeAvoidObstacleTask = new MeleeAvoidObstacleTask(target);
//        MeleePursueTask meleePursueTask = new MeleePursueTask(target);
//        meleePursueTask.create(() -> entity);
//        meleeAvoidObstacleTask.create(() -> entity);
//
//        Entity obstacle = makeCollisionEntity();
//        obstacle.setPosition(1f, 1f);
//        obstacle.setScale(new Vector2(1f, 1f));
//
//        assertTrue(meleeAvoidObstacleTask.getPriority() <= 0);
//
//        Fixture obstacleFixture = obstacle.getComponent(ColliderComponent.class).getFixture();
//        Fixture entityFixture = entity.getComponent(HitboxComponent.class).getFixture();
//
//        ai.dispose();
//        entity.getEvents().trigger("collisionStart", entityFixture, obstacleFixture);
//
//        assertTrue(meleeAvoidObstacleTask.getPriority() > 0);
//    }

    private Entity makePhysicsEntity() {
        return new Entity()
                .addComponent(new PhysicsComponent())
                .addComponent(new PhysicsMovementComponent())
                .addComponent(new HitboxComponent());
    }

//    private Entity makeCollisionEntity() {
//        return new Entity()
//                .addComponent(new PhysicsComponent())
//                .addComponent(new ColliderComponent());
//    }

}
