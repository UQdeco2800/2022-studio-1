package com.deco2800.game.entities;

import com.deco2800.game.components.CombatStatsComponent;
import com.deco2800.game.entities.factories.CrystalFactory;
import com.deco2800.game.extensions.GameExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.junit.jupiter.api.Assertions.assertEquals;


@ExtendWith(GameExtension.class)
class CrystalTest {
    @Test
    void shouldSetLevel() {
        CombatStatsComponent stats = new CombatStatsComponent(100, 0, 0,1);
        assertEquals(1, stats.getLevel());

        stats.setLevel(5);
        assertEquals(5, stats.getLevel());

        stats.setLevel(2);
        assertEquals(2, stats.getLevel());
    }

    @Test
    void shouldUpgrade() {
        CombatStatsComponent stats2 = new CombatStatsComponent(100, 0, 0, 3);
        assertEquals(3, stats2.getLevel());
        assertEquals(100, stats2.getHealth());

        stats2.upgrade();
        assertEquals(4, stats2.getLevel());
        assertEquals(200, stats2.getHealth());

    }



}
