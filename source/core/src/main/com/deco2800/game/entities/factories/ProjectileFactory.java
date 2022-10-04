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

import static java.sql.DriverManager.println;

public class ProjectileFactory {
    public static Entity createProjectile(Entity shooter, Entity target) {
        float x1 = shooter.getPosition().x;
        float y1 = shooter.getPosition().y;
        float x2 = target.getPosition().x;
        float y2 = target.getPosition().y;
        System.out.println("cat");
        Vector2 targetNew = target.getPosition();

        return makeProjectile(targetNew, target, shooter, x1, y1);
    }

    private static Entity makeProjectile(Vector2 destination, Entity target, Entity source, float x1, float y1) {
        //System.out.println("cat2");
        Entity projectile = new Entity()
                .addComponent(new TextureRenderComponent("images/Eel_Bright_SW.png"))
                .addComponent(new PhysicsComponent())
                .addComponent(new PhysicsMovementComponent())
                .addComponent(new ColliderComponent())
                .addComponent(new HitboxComponent().setLayer(PhysicsLayer.NPC))
                .addComponent(new CombatStatsComponent(1,20))
                .addComponent(new EntityClassification(EntityClassification.NPCClassification.ENEMY))
                .addComponent(new TouchAttackComponent(PhysicsLayer.PLAYER, 0.2f));

        projectile.setPosition(source.getPosition());
        PhysicsUtils.setScaledCollider(projectile, 0.4f, 0.2f);

        projectile.getComponent(PhysicsMovementComponent.class).setTarget(destination);
        projectile.getComponent(PhysicsMovementComponent.class).setMoving(true);
        projectile.getComponent(ColliderComponent.class).setSensor(true);
        System.out.println("cat2");

        ServiceLocator.getEntityService().registerNamed("Enemy@" + projectile.getId(), projectile);
        return projectile;
    }

    private ProjectileFactory() {
        throw new IllegalStateException("Instantiating static util class");
    }
}
