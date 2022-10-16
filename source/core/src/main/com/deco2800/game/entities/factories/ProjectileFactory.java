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
        System.out.println("SHOOTER IS" + shooter);
        Vector2 targetNew = target.getPosition();
        return makeProjectile(targetNew, shooter);
    }

    private static Entity makeProjectile(Vector2 destination, Entity source) {
        System.out.println("WE MADE A FUCKING PROJECTILE");
        String texturePath = "images/eel_projectile.png";

        if (source.getName().contains("turret")) {
            // change to turret proj
        }

        Entity projectile = new Entity()
                .addComponent(new TextureRenderComponent("images/eel_projectile.png"))
                .addComponent(new PhysicsComponent())
                .addComponent(new PhysicsMovementComponent())
                .addComponent(new ColliderComponent())
                .addComponent(new HitboxComponent().setLayer(PhysicsLayer.NPC))
                .addComponent(new CombatStatsComponent(1, 5))
                .addComponent(new EntityClassification(EntityClassification.NPCClassification.ENEMY))
                .addComponent(new TouchAttackComponent(PhysicsLayer.PLAYER, 0.2f));

        projectile.getComponent(PhysicsMovementComponent.class).setTarget(destination);
        projectile.getComponent(PhysicsMovementComponent.class).setMoving(true);
        projectile.getComponent(PhysicsMovementComponent.class).setNewSpeed(new Vector2(4, 4));
        projectile.getComponent(ColliderComponent.class).setSensor(false);

        projectile.setPosition(source.getPosition().x, source.getPosition().y);
        PhysicsUtils.setScaledCollider(projectile, 2f, 2f);
        projectile.scaleHeight(20f);

        projectile.setName(source.getName() + "Projectile@" + projectile.getId());
        ServiceLocator.getEntityService().registerNamed("Enemy@" + projectile.getId(), projectile);
        return projectile;
    }

    private ProjectileFactory() {
        throw new IllegalStateException("Instantiating static util class");
    }
}
