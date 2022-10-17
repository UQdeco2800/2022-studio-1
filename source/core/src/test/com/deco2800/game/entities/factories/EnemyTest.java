package com.deco2800.game.entities.factories;

import com.deco2800.game.components.CombatStatsComponent;
import com.deco2800.game.components.infrastructure.ResourceType;
import com.deco2800.game.components.npc.EntityClassification;
import com.deco2800.game.entities.Enemy;
import com.deco2800.game.entities.Entity;
import com.deco2800.game.extensions.GameExtension;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(GameExtension.class)
public class EnemyTest {
    static Enemy enemy;
    @BeforeEach
    void enemyEntity() {
        enemy = new Enemy();
        enemy.addComponent(new CombatStatsComponent(100, 10,10,1,100))
             .addComponent(new EntityClassification(EntityClassification.NPCClassification.ENEMY));
    }

    @Test
    void enemyExisted() {
        Assertions.assertNotNull(enemy);
    }

    @Test
    void shouldSetEnemyLevel() {
        enemy.getComponent(CombatStatsComponent.class).setLevel(-1);
        assertEquals(1, enemy.getComponent(CombatStatsComponent.class).getLevel());

        enemy.getComponent(CombatStatsComponent.class).setLevel(0);
        assertEquals(1, enemy.getComponent(CombatStatsComponent.class).getLevel());

        enemy.getComponent(CombatStatsComponent.class).setLevel(1);
        assertEquals(1, enemy.getComponent(CombatStatsComponent.class).getLevel());

        enemy.getComponent(CombatStatsComponent.class).setLevel(2);
        assertEquals(2, enemy.getComponent(CombatStatsComponent.class).getLevel());

        enemy.getComponent(CombatStatsComponent.class).setLevel(3);
        assertEquals(3, enemy.getComponent(CombatStatsComponent.class).getLevel());
    }

    @Test
    void shouldSetEnemyMAXHealth() {
        enemy.getComponent(CombatStatsComponent.class).setMaxHealth(-100);
        assertEquals(100, enemy.getComponent(CombatStatsComponent.class).getMaxHealth());

        enemy.getComponent(CombatStatsComponent.class).setMaxHealth(0);
        assertEquals(100, enemy.getComponent(CombatStatsComponent.class).getMaxHealth());

        enemy.getComponent(CombatStatsComponent.class).setMaxHealth(100);
        assertEquals(100, enemy.getComponent(CombatStatsComponent.class).getMaxHealth());
    }

    @Test
    void shouldSetEnemyHealth() {
        enemy.getComponent(CombatStatsComponent.class).setMaxHealth(-100);
        assertEquals(100, enemy.getComponent(CombatStatsComponent.class).getMaxHealth());

        enemy.getComponent(CombatStatsComponent.class).setMaxHealth(0);
        assertEquals(100, enemy.getComponent(CombatStatsComponent.class).getMaxHealth());

        enemy.getComponent(CombatStatsComponent.class).setMaxHealth(100);
        assertEquals(100, enemy.getComponent(CombatStatsComponent.class).getMaxHealth());
    }

    @Test
    void shouldSetEnemyAttack() {
        enemy.getComponent(CombatStatsComponent.class).setBaseAttack(-10);
        assertEquals(10, enemy.getComponent(CombatStatsComponent.class).getBaseAttack());

        enemy.getComponent(CombatStatsComponent.class).setBaseAttack(0);
        assertEquals(0, enemy.getComponent(CombatStatsComponent.class).getBaseAttack());

        enemy.getComponent(CombatStatsComponent.class).setBaseAttack(10);
        assertEquals(10, enemy.getComponent(CombatStatsComponent.class).getBaseAttack());

        enemy.getComponent(CombatStatsComponent.class).setBaseAttack(100);
        assertEquals(100, enemy.getComponent(CombatStatsComponent.class).getBaseAttack());
    }

    @Test
    void shouldSetEnemyDefense() {
        enemy.getComponent(CombatStatsComponent.class).setBaseDefense(-10);
        assertEquals(10, enemy.getComponent(CombatStatsComponent.class).getBaseDefense());

        enemy.getComponent(CombatStatsComponent.class).setBaseDefense(0);
        assertEquals(0, enemy.getComponent(CombatStatsComponent.class).getBaseDefense());

        enemy.getComponent(CombatStatsComponent.class).setBaseDefense(10);
        assertEquals(10, enemy.getComponent(CombatStatsComponent.class).getBaseDefense());

        enemy.getComponent(CombatStatsComponent.class).setBaseDefense(100);
        assertEquals(100, enemy.getComponent(CombatStatsComponent.class).getBaseDefense());
    }

    @Test
    void shouldSetEnemyBounty() {
        enemy.setResourceType(ResourceType.GOLD);

        enemy.setResourceAmount(-10);
        assertEquals(0, enemy.getResourceAmount());

        enemy.setResourceAmount(0);
        assertEquals(0, enemy.getResourceAmount());

        enemy.setResourceAmount(10);
        assertEquals(10, enemy.getResourceAmount());
    }

    @Test
    void shouldLevelUp() {
        enemy.setName("Crab");
        enemy.getComponent(CombatStatsComponent.class).setLevel(1);
        enemy.getComponent(CombatStatsComponent.class).levelUp();
        assertEquals(50, enemy.getComponent(CombatStatsComponent.class).getMaxHealth());
        assertEquals(50, enemy.getComponent(CombatStatsComponent.class).getHealth());
        assertEquals(10, enemy.getComponent(CombatStatsComponent.class).getBaseAttack());
        assertEquals(5, enemy.getComponent(CombatStatsComponent.class).getBaseDefense());

        enemy.setName("Electricity");
        enemy.getComponent(CombatStatsComponent.class).setLevel(1);
        enemy.getComponent(CombatStatsComponent.class).levelUp();
        assertEquals(30, enemy.getComponent(CombatStatsComponent.class).getMaxHealth());
        assertEquals(30, enemy.getComponent(CombatStatsComponent.class).getHealth());
        assertEquals(20, enemy.getComponent(CombatStatsComponent.class).getBaseAttack());
        assertEquals(0, enemy.getComponent(CombatStatsComponent.class).getBaseDefense());

        enemy.setName("Starfish");
        enemy.getComponent(CombatStatsComponent.class).setLevel(1);
        enemy.getComponent(CombatStatsComponent.class).levelUp();
        assertEquals(30, enemy.getComponent(CombatStatsComponent.class).getMaxHealth());
        assertEquals(30, enemy.getComponent(CombatStatsComponent.class).getHealth());
        assertEquals(20, enemy.getComponent(CombatStatsComponent.class).getBaseAttack());
        assertEquals(0, enemy.getComponent(CombatStatsComponent.class).getBaseDefense());
    }
}
