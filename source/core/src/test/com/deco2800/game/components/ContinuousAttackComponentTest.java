package com.deco2800.game.components;

import com.badlogic.gdx.physics.box2d.Fixture;
import com.deco2800.game.components.npc.ContinuousAttackComponent;
import com.deco2800.game.components.npc.EntityClassification;
import com.deco2800.game.entities.Entity;
import com.deco2800.game.extensions.GameExtension;
import com.deco2800.game.physics.PhysicsService;
import com.deco2800.game.physics.components.HitboxComponent;
import com.deco2800.game.physics.components.PhysicsComponent;
import com.deco2800.game.services.GameTime;
import com.deco2800.game.services.ServiceLocator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


@ExtendWith(GameExtension.class)
public class ContinuousAttackComponentTest {
    @BeforeEach
    void beforeEach() {
        ServiceLocator.registerPhysicsService(new PhysicsService());
        GameTime gameTime = mock(GameTime.class);
        when(gameTime.getDeltaTime()).thenReturn(20f / 1000);
        ServiceLocator.registerTimeSource(gameTime);
    }

    @Test
    synchronized void shouldContinuousAttackNonEnemy() throws InterruptedException {
        Entity enemy = createEnemy();
        Entity target = createTarget();
        Fixture entityFixture = enemy.getComponent(HitboxComponent.class).getFixture();
        Fixture targetFixture = target.getComponent(HitboxComponent.class).getFixture();

        enemy.getEvents().trigger("collisionStart", entityFixture, targetFixture);
        wait(10);
        assertTrue(target.getComponent(CombatStatsComponent.class).getHealth() < 100);
    }

    @Test
    synchronized void shouldNotAttackEnemy() throws InterruptedException {
        Entity enemy = createEnemy();
        Entity target = createEnemy();
        Fixture entityFixture = enemy.getComponent(HitboxComponent.class).getFixture();
        Fixture targetFixture = target.getComponent(HitboxComponent.class).getFixture();

        enemy.getEvents().trigger("collisionStart", entityFixture, targetFixture);
        wait(10);
        assertEquals(100, target.getComponent(CombatStatsComponent.class).getHealth());
    }

    @Test
    synchronized void shouldStopAttackingWhenCollisionEnds() throws InterruptedException {
        Entity enemy = createEnemy();
        Entity target = createTarget();
        Fixture entityFixture = enemy.getComponent(HitboxComponent.class).getFixture();
        Fixture targetFixture = target.getComponent(HitboxComponent.class).getFixture();

        enemy.getEvents().trigger("collisionStart", entityFixture, targetFixture);
        wait(10);
        enemy.getEvents().trigger("collisionEnd", entityFixture, targetFixture);
        int healthBefore  = target.getComponent(CombatStatsComponent.class).getHealth();

        wait(10);
        assertEquals(healthBefore, target.getComponent(CombatStatsComponent.class).getHealth());
    }

    private Entity createEnemy() {
        Entity enemy = new Entity().addComponent(new CombatStatsComponent(100, 1))
                .addComponent(new ContinuousAttackComponent(1, 1))
                .addComponent(new HitboxComponent())
                .addComponent(new EntityClassification(EntityClassification.NPCClassification.ENEMY))
                .addComponent(new PhysicsComponent());
        enemy.create();
        return enemy;
    }

    private Entity createTarget() {
        Entity target  = new Entity().addComponent(new CombatStatsComponent(100, 1))
                .addComponent(new HitboxComponent())
                .addComponent(new PhysicsComponent());
        target.create();
        return target;
    }

}
