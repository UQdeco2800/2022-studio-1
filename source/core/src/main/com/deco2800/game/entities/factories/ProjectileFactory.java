package com.deco2800.game.entities.factories;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.deco2800.game.components.CombatStatsComponent;
import com.deco2800.game.components.TouchAttackComponent;
import com.deco2800.game.entities.Entity;
import com.deco2800.game.physics.PhysicsLayer;
import com.deco2800.game.physics.components.*;
import com.deco2800.game.rendering.TextureRenderComponent;

public class ProjectileFactory {
    public static void createProjectile(Entity shooter, Entity target) {
        float x1 = shooter.getPosition().x;
        float y1 = shooter.getPosition().y;
        float x2 = target.getPosition().x;
        float y2 = target.getPosition().y;

        Vector2 Target = new Vector2(x2 - x1, y2 - y1);

        //Entity Projectile = makeProjectile();


    }

    private static Entity makeProjectile() {
        Entity projectile = new Entity()
                .addComponent(new TextureRenderComponent("images/eel_projectile.png"))
                .addComponent(new PhysicsComponent())
                .addComponent(new PhysicsMovementComponent())
                .addComponent(new ColliderComponent())
                .addComponent(new CombatStatsComponent(1,20))
                .addComponent(new TouchAttackComponent(PhysicsLayer.PLAYER, 0.2f));

        return projectile;
    }

    private ProjectileFactory() {
        throw new IllegalStateException("Instantiating static util class");
    }
}
