package com.deco2800.game.components;

import com.deco2800.game.extensions.GameExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.junit.Assert.assertNotEquals;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(GameExtension.class)
class CombatStatsComponentTest {
  @Test
  void shouldSetGetHealth() {
    CombatStatsComponent combat = new CombatStatsComponent(1000, 0, 0,1, 1000);
    assertEquals(1000, combat.getHealth());

    combat.setHealth(150);
    assertEquals(150, combat.getHealth());

    combat.setHealth(900);
    assertEquals(900, combat.getHealth());

    combat.setHealth(-50);
    assertEquals(0, combat.getHealth());
        
    combat.setHealth(1100);
    assertNotEquals(1100, combat.getHealth());
    assertFalse(combat.getHealth() > 1000);
  }

  @Test
  void shouldCheckIsDead() {
    CombatStatsComponent combat = new CombatStatsComponent(100, 20);
    assertFalse(combat.isDead());

    combat.setHealth(0);
    assertTrue(combat.isDead());
  }

  @Test
  void shouldAddHealth() {
    CombatStatsComponent combat = new CombatStatsComponent(100, 20);
    combat.addHealth(-500);
    assertEquals(0, combat.getHealth());

    combat.addHealth(100);
    combat.addHealth(-20);
    assertEquals(80, combat.getHealth());
  }

  @Test
  void shouldSetGetBaseAttack() {
    CombatStatsComponent combat = new CombatStatsComponent(100, 20);
    assertEquals(20, combat.getBaseAttack());

    combat.setBaseAttack(150);
    assertEquals(150, combat.getBaseAttack());

    combat.setBaseAttack(-50);
    assertEquals(150, combat.getBaseAttack());
  }

  @Test
  void shouldSetLevel() {
      CombatStatsComponent stats = new CombatStatsComponent(100, 0, 0, 1);
      assertEquals(1, stats.getLevel());

      stats.setLevel(2);
      assertEquals(2, stats.getLevel());

      stats.setLevel(3);
      assertEquals(3, stats.getLevel());

      stats.setLevel(3);
      assertEquals(3, stats.getLevel());

      stats.setLevel(4);
      assertEquals(4, stats.getLevel());

      stats.setLevel(5);
      assertEquals(5, stats.getLevel());

      // Shouldn't be able set level 0 or less
      stats.setLevel(0);
      assertNotEquals(0, stats.getLevel());
      assertFalse(stats.getLevel() < 1);

      stats.setLevel(-1);
      assertNotEquals(-1, stats.getLevel());
      assertFalse(stats.getLevel() < 1);
  }

  @Test
  void shouldSetMaxHealth() {
    CombatStatsComponent stats = new CombatStatsComponent(100, 0, 0,1,1000);
    assertEquals(1000, stats.getMaxHealth());

    stats.setMaxHealth(5000);
    assertEquals(5000, stats.getMaxHealth());

    stats.setMaxHealth(2000);
    assertEquals(2000, stats.getMaxHealth());

    //max health will retain original value as negative max health is invalid
    stats.setMaxHealth(-10);
    assertEquals(2000, stats.getMaxHealth());

    // Shouldn't be able set max health 0 or less
    stats.setMaxHealth(0);
    assertNotEquals(0, stats.getMaxHealth());
    assertFalse(stats.getMaxHealth() <= 0);

    stats.setMaxHealth(-100);
    assertNotEquals(-100, stats.getMaxHealth());
    assertFalse(stats.getMaxHealth() <= 0);
  }

  @Test
  void healthShouldNotExceedMax() {
    CombatStatsComponent stats = new CombatStatsComponent(100, 0, 0,1,500);
    assertEquals(100, stats.getHealth());

    //health is capped at the max health value
    stats.setHealth(5000);
    assertEquals(500, stats.getHealth());

    stats.addHealth(600);
    assertEquals(500, stats.getHealth());

    stats.setHealth(400);
    assertEquals(400, stats.getHealth());
  }

  @Test
  void isHit() {
    CombatStatsComponent attacker = new CombatStatsComponent(100, 50);
    CombatStatsComponent Crystal = new CombatStatsComponent(1000,0,0,1,1500);

    Crystal.hit(attacker);

    assertEquals(950, Crystal.getHealth());

    Crystal.hit(attacker);
    Crystal.hit(attacker);
    assertEquals(850, Crystal.getHealth());
  }

  @Test
  void shouldBeInvincible() {
    CombatStatsComponent attack  = new CombatStatsComponent(100, 50);
    CombatStatsComponent target = new CombatStatsComponent(100, 0,0,1, 100);
    target.setInvincibility(true);
    target.hit(attack);

    assertEquals(100, target.getHealth());
    target.setInvincibility(false);
    target.hit(attack);
    assertEquals(50, target.getHealth());
  }
}
