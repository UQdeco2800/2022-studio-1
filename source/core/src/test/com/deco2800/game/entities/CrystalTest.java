package com.deco2800.game.entities;

import com.badlogic.gdx.utils.Logger;
import com.deco2800.game.components.CombatStatsComponent;
import com.deco2800.game.entities.factories.CrystalFactory;
import com.deco2800.game.extensions.GameExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;


@ExtendWith(GameExtension.class)
class CrystalTest {
   @Test
   void shouldSetLevel() {
        CombatStatsComponent stats = new CombatStatsComponent(100, 0, 0,1);
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
}
