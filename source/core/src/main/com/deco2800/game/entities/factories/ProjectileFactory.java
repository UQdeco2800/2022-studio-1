package com.deco2800.game.entities.factories;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.deco2800.game.components.CombatStatsComponent;
import com.deco2800.game.components.TouchAttackComponent;
import com.deco2800.game.entities.Entity;
import com.deco2800.game.physics.PhysicsLayer;
import com.deco2800.game.physics.PhysicsUtils;
import com.deco2800.game.physics.components.*;
import com.deco2800.game.rendering.TextureRenderComponent;

public class ProjectileFactory {
    public static Entity createProjectile(Entity shooter, Entity target) {
        float x1 = shooter.getPosition().x;
        float y1 = shooter.getPosition().y;
        float x2 = target.getPosition().x;
        float y2 = target.getPosition().y;

        Vector2 targetNew = new Vector2(x2 - x1, y2 - y1);

        return makeProjectile(targetNew, target, shooter, x1, y1);
    }

    private static Entity makeProjectile(Vector2 destination, Entity target, Entity source, float x1, float y1) {
        Entity projectile = new Entity()
                .addComponent(new TextureRenderComponent("images/ElectricEel.png"))
                .addComponent(new PhysicsComponent())
                .addComponent(new PhysicsMovementComponent())
                .addComponent(new ColliderComponent())
                .addComponent(new CombatStatsComponent(1,20))
                .addComponent(new TouchAttackComponent(PhysicsLayer.PLAYER, 0.2f));

        projectile.getComponent(PhysicsMovementComponent.class).setTarget(destination);
        projectile.getComponent(PhysicsMovementComponent.class).setMoving(true);
        projectile.getComponent(ColliderComponent.class).setSensor(true);

        projectile.setPosition(x1 - projectile.getScale().x / 2 + source.getScale().x / 2,y1  - projectile.getScale().y / 2  + source.getScale().y / 2);

        return projectile;
    }

    private ProjectileFactory() {
        throw new IllegalStateException("Instantiating static util class");
    }
}
