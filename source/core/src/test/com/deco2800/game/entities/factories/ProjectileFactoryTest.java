package com.deco2800.game.entities.factories;

import com.badlogic.gdx.math.Vector2;
import com.deco2800.game.components.CombatStatsComponent;
import com.deco2800.game.components.TouchAttackComponent;
import com.deco2800.game.components.npc.EntityClassification;
import com.deco2800.game.entities.Entity;
import com.deco2800.game.extensions.GameExtension;
import com.deco2800.game.physics.PhysicsLayer;
import com.deco2800.game.physics.PhysicsService;
import com.deco2800.game.physics.components.ColliderComponent;
import com.deco2800.game.physics.components.HitboxComponent;
import com.deco2800.game.physics.components.PhysicsComponent;
import com.deco2800.game.physics.components.ProjectileMovementComponent;
import com.deco2800.game.services.ServiceLocator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.mockito.Mockito.mock;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(GameExtension.class)
class ProjectileFactoryTest {
    static Entity projectile;

    @BeforeEach
    void projectileEntity() {
        ServiceLocator.registerPhysicsService(new PhysicsService());
        short targetLayer = 0;

        projectile = new Entity()
                .addComponent(new PhysicsComponent())
                .addComponent(new ProjectileMovementComponent())
                .addComponent(new ColliderComponent())
                .addComponent(new HitboxComponent().setLayer(PhysicsLayer.PROJECTILE))
                .addComponent(new CombatStatsComponent(1, 5))
                .addComponent(new EntityClassification(EntityClassification.NPCClassification.ENEMY))
                .addComponent(new TouchAttackComponent(targetLayer, 0.2f));

        projectile.setPosition(mock(Vector2.class));
    }

    @Test
    void shouldCreateProjectile() {
        short layer = 0;
        projectile.getComponent(ProjectileMovementComponent.class).setTarget(mock(Vector2.class));
        projectile.getComponent(ProjectileMovementComponent.class).setMoving(true);
        projectile.getComponent(ProjectileMovementComponent.class).setNewSpeed(new Vector2(4, 4));
        projectile.getComponent(ColliderComponent.class).setSensor(true);
        projectile.getComponent(ColliderComponent.class).setTangible(layer);

        assertNotNull(projectile);
    }


}
