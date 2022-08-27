package com.deco2800.game.areas.terrain;

import com.deco2800.game.components.Environmental.EnvironmentalComponent;
import com.deco2800.game.extensions.GameExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;


@ExtendWith(GameExtension.class)
class EnvironmentalTesting {

    @Test
    void createdEnvironmentalTree() {
        EnvironmentalComponent tree = new EnvironmentalComponent().setObstacle(EnvironmentalComponent.EnvironmentalObstacle.TREE);
        assertEquals(tree.getType(), EnvironmentalComponent.ResourceTypes.WOOD);
    }

    @Test
    void assertCorrectEnvironmentalType() {
        EnvironmentalComponent tree = new EnvironmentalComponent().setObstacle(EnvironmentalComponent.EnvironmentalObstacle.TREE);
        assertTrue(tree.getObstacle() instanceof EnvironmentalComponent.EnvironmentalObstacle);
    }

    @Test
    void assertValuesGreaterZeroEnvironmentalObjects() {
        EnvironmentalComponent component = new EnvironmentalComponent();
        for (EnvironmentalComponent.EnvironmentalObstacle obstacle: EnvironmentalComponent.EnvironmentalObstacle.values()) {
            component.setObstacle(obstacle);
            assertTrue(component.getResourceAmount() >= 0);
        }
    }

    @Test
    void assertValuesCorrectTypeEnvironmentalObject() {
        EnvironmentalComponent component = new EnvironmentalComponent();
        for (EnvironmentalComponent.EnvironmentalObstacle obstacle: EnvironmentalComponent.EnvironmentalObstacle.values()) {
            component.setObstacle(obstacle);
            assertTrue(component.getType() instanceof EnvironmentalComponent.ResourceTypes);
        }
    }
}

