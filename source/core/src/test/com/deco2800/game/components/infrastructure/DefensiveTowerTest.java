package com.deco2800.game.components.infrastructure;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DefensiveTowerTest {

    @Test
    void shouldGetDamage() {
        DefensiveTower tower = new DefensiveTower(100, 100, "assets/images/tree.png");
        assertEquals(100, tower.getDamage());
    }

    @Test
    void shouldGetHealth() {
        DefensiveTower tower = new DefensiveTower(10, 100, "assets/images/tree.png");
        assertEquals(10, tower.getHealth());
    }
}