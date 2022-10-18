package com.deco2800.game.physics;

import com.deco2800.game.entities.Entity;
import com.deco2800.game.physics.components.ProjectileMovementComponent;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;


public class ProjectileMovementComponentTest {

    @Test
    void testProjectileMovementComponentIsAdded() {
        ProjectileMovementComponent pmc = new ProjectileMovementComponent();
        Entity testProjectile = new Entity().addComponent(pmc);
        assertEquals(testProjectile.getComponent(ProjectileMovementComponent.class), pmc);
    }
}
