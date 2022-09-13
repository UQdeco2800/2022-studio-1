package com.deco2800.game.components;
import com.deco2800.game.components.infrastructure.ResourceCostComponent;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

class ResourceCostComponentTest {
    @Test
    void shouldGetResources() {
        ResourceCostComponent resourceCostComponent = new ResourceCostComponent(10);
        assertEquals(10, resourceCostComponent.getGoldCost()); 

        ResourceCostComponent resourceCostComponent1 = new ResourceCostComponent(-10);
        assertEquals(0, resourceCostComponent1.getGoldCost()); 

        ResourceCostComponent resourceCostComponent2 = new ResourceCostComponent(1000, -10, 0);
        assertEquals(1000, resourceCostComponent2.getGoldCost()); 
        assertEquals(0, resourceCostComponent2.getStoneCost()); 
        assertEquals(0, resourceCostComponent2.getWoodCost()); 
    }

}