package com.deco2800.game.entities.factories;

import com.badlogic.gdx.math.Vector2;
import com.deco2800.game.areas.GameArea;
import com.deco2800.game.components.CombatStatsComponent;
import com.deco2800.game.components.HealthBarComponent;
import com.deco2800.game.components.TouchAttackComponent;
import com.deco2800.game.components.npc.EntityClassification;
import com.deco2800.game.entities.Entity;
import com.deco2800.game.physics.PhysicsLayer;
import com.deco2800.game.physics.PhysicsUtils;
import com.deco2800.game.physics.components.*;
import com.deco2800.game.rendering.TextureRenderComponent;
import com.deco2800.game.services.ServiceLocator;


public class ProjectileFactory {
    public static Entity createProjectile(Entity shooter, Entity target) {
        Vector2 targetNew = target.getPosition();
        return makeProjectile(targetNew, shooter);
    }

    private static Entity makeProjectile(Vector2 destination, Entity source) {
        Entity projectile = new Entity()
                .addComponent(new TextureRenderComponent("images/Eel_Bright_SW.png"))
                .addComponent(new PhysicsComponent())
                .addComponent(new PhysicsMovementComponent())
                .addComponent(new ColliderComponent())
                .addComponent(new HitboxComponent().setLayer(PhysicsLayer.NPC))
                .addComponent(new CombatStatsComponent(1,5))
                .addComponent(new EntityClassification(EntityClassification.NPCClassification.ENEMY))
                .addComponent(new TouchAttackComponent(PhysicsLayer.PLAYER, 0.2f));

        projectile.getComponent(PhysicsMovementComponent.class).setTarget(destination);
        projectile.getComponent(PhysicsMovementComponent.class).setMoving(true);
        projectile.getComponent(ColliderComponent.class).setSensor(true);

        projectile.setPosition(source.getPosition());
        PhysicsUtils.setScaledCollider(projectile, 2f, 2f);

        ServiceLocator.getEntityService().registerNamed("Enemy@" + projectile.getId(), projectile);
        return projectile;
    }

    private ProjectileFactory() {
        throw new IllegalStateException("Instantiating static util class");
    }
}
