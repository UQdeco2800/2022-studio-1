package com.deco2800.game.components;

import com.badlogic.gdx.math.Vector2;
import com.deco2800.game.ai.tasks.AITaskComponent;
import com.deco2800.game.components.npc.EffectNearBy;
import com.deco2800.game.components.npc.EntityClassification;
import com.deco2800.game.entities.Entity;
import com.deco2800.game.entities.EntityService;
import com.deco2800.game.physics.PhysicsService;
import com.deco2800.game.physics.components.HitboxComponent;
import com.deco2800.game.physics.components.PhysicsComponent;
import com.deco2800.game.physics.components.PhysicsMovementComponent;
import com.deco2800.game.services.ServiceLocator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class EffectNearbyTest {
    @BeforeEach
    void beforeEach() {
        ServiceLocator.registerPhysicsService(new PhysicsService());
        ServiceLocator.registerEntityService(new EntityService());
    }

    @Test
    void shouldSpeedBoostEnemyInRange() {
        Entity enemy1 = createEnemy();
        Entity enemy2 = createEnemy();
        Entity boss = createBoss();
        boss.setPosition(0, 0);
        enemy1.setPosition(10, 10);
        enemy2.setPosition(250, 250);

        //run the game for 60 frames
        for (int i = 0; i < 62; i++) {
            boss.update();
        }
        Vector2 speed1 = enemy1.getComponent(PhysicsMovementComponent.class).getSpeed();
        Vector2 speed2 = enemy2.getComponent(PhysicsMovementComponent.class).getSpeed();
        assertTrue(speed1.x > speed2.x);
    }

    @Test
    void shouldRegenEnemyInRange() {
        Entity enemy1 = createEnemy();
        Entity enemy2 = createEnemy();
        Entity boss = createBoss();
        boss.setPosition(0, 0);
        enemy1.setPosition(10, 10);
        enemy2.setPosition(250, 250);
        CombatStatsComponent combat1 = enemy1.getComponent(CombatStatsComponent.class);
        CombatStatsComponent combat2 = enemy2.getComponent(CombatStatsComponent.class);
        combat1.setHealth(5);
        combat2.setHealth(5);
        //run the game for 60 frames
        for (int i = 0; i < 62; i++) {
            boss.update();
        }
        assertEquals(combat2.getHealth(), 5);
        assertTrue(combat1.getHealth() > 5);
    }

    @Test
    void shouldDamageBuffInRange() {
        Entity enemy1 = createEnemy();
        Entity enemy2 = createEnemy();
        Entity boss = createBoss();
        boss.setPosition(0, 0);
        enemy1.setPosition(10, 10);
        enemy2.setPosition(250, 250);
        CombatStatsComponent combat1 = enemy1.getComponent(CombatStatsComponent.class);
        CombatStatsComponent combat2 = enemy2.getComponent(CombatStatsComponent.class);
        //run the game for 60 frames
        for (int i = 0; i < 62; i++) {
            boss.update();
        }
        assertEquals(10, combat2.getCurrentAttack());
        assertTrue(combat1.getCurrentAttack() > 10);
    }

    @Test
    void shouldNotEffectNonEnemy() {
        ServiceLocator.registerPhysicsService(new PhysicsService());
        Entity enemy1 = createEnemy();
        Entity nonEnemy = createNonEnemy();
        Entity boss = createBoss();
        boss.setPosition(0, 0);
        enemy1.setPosition(10, 10);
        nonEnemy.setPosition(5, 5);
        //run the game for 60 frames
        for (int i = 0; i < 62; i++) {
            boss.update();
        }
        assertTrue(enemy1.getComponent(PhysicsMovementComponent.class).getSpeed().x >
                nonEnemy.getComponent(PhysicsMovementComponent.class).getSpeed().x);
    }

    Entity createEnemy() {
        Entity enemy = new Entity().addComponent(new CombatStatsComponent(10, 10))
                .addComponent(new HitboxComponent())
                .addComponent(new PhysicsComponent())
                .addComponent(new EntityClassification(EntityClassification.NPCClassification.ENEMY))
                .addComponent(new AITaskComponent())
                .addComponent(new PhysicsMovementComponent());
        ServiceLocator.getEntityService().register(enemy);
        return enemy;
    }

    Entity createBoss() {
        EffectNearBy effect = new EffectNearBy(true, false, false);
        effect.enableRegen();
        effect.enableAttackDamageBuff();
        effect.enableSpeed();
        Entity boss = new Entity().addComponent(new CombatStatsComponent(10, 10))
                .addComponent(new HitboxComponent())
                .addComponent(new PhysicsComponent())
                .addComponent(new EntityClassification(EntityClassification.NPCClassification.BOSS))
                .addComponent(effect)
                .addComponent(new PhysicsMovementComponent());
        boss.create();
        return boss;
    }

    Entity createNonEnemy() {
        Entity player = new Entity().addComponent(new CombatStatsComponent(10, 10))
                .addComponent(new HitboxComponent())
                .addComponent(new PhysicsComponent())
                .addComponent(new PhysicsMovementComponent());
        player.create();
        return player;
    }
}
