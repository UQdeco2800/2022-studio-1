package com.deco2800.game.entities.factories;

import com.deco2800.game.entities.configs.StructureConfig;
import com.deco2800.game.extensions.GameExtension;
import com.deco2800.game.files.FileLoader;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(GameExtension.class)
class StructureFactoryTest {

    private static final StructureConfig configs =
            FileLoader.readClass(StructureConfig.class, "configs/structure.json");


    /**
     * Test that the structure wall health configuration is loaded properly
     */
    @Test
    void shouldTestGetWallHealth() {
        assertEquals(40,configs.wall.health);
    }

    /**
     * Test that the structure wall baseAttack configuration is loaded properly
     */
    @Test
    void shouldTestGetWallAttack() {
        assertEquals(0,configs.wall.baseAttack);
    }

    /**
     * Test that the structure tower1 health configuration is loaded properly
     */
    @Test
    void shouldTestGetTower1Health() {
        assertEquals(200,configs.tower1.health);
    }

    /**
     * Test that the structure tower1 baseAttack configuration is loaded properly
     */
    @Test
    void shouldTestGetTower1Attack() {
        assertEquals(10,configs.tower1.baseAttack);
    }
}