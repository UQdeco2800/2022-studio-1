package com.deco2800.game.entities.factories;

import com.badlogic.gdx.math.Vector;
import com.badlogic.gdx.math.Vector2;
import com.deco2800.game.areas.GameArea;
import com.deco2800.game.areas.terrain.TerrainComponent;
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

        String texturePath = "images/eel_projectile.png";
        short targetLayer = PhysicsLayer.PLAYER;

        if (source.getName().contains("turret")) {
            targetLayer = PhysicsLayer.ENEMY;
        }

        Entity projectile = new Entity()
                .addComponent(new TextureRenderComponent("images/eel_projectile.png"))
                .addComponent(new PhysicsComponent())
                .addComponent(new ProjectileMovementComponent())
                .addComponent(new ColliderComponent())
                .addComponent(new HitboxComponent().setLayer(PhysicsLayer.PROJECTILE))
                .addComponent(new CombatStatsComponent(1, 5))
                .addComponent(new EntityClassification(EntityClassification.NPCClassification.ENEMY))
                .addComponent(new TouchAttackComponent(targetLayer, 0.2f));

        destination.y -= ServiceLocator.getEntityService().getNamedEntity("terrain")
                .getComponent(TerrainComponent.class).getTileSize() / 2;

        projectile.getComponent(ProjectileMovementComponent.class).setTarget(destination);
        projectile.getComponent(ProjectileMovementComponent.class).setMoving(true);
        projectile.getComponent(ProjectileMovementComponent.class).setNewSpeed(new Vector2(4, 4));
        projectile.getComponent(ColliderComponent.class).setSensor(true);
        projectile.getComponent(ColliderComponent.class).setTangible(targetLayer);

        Vector2 sourcePosition = source.getPosition();

        sourcePosition.y -= ServiceLocator.getEntityService().getNamedEntity("terrain")
                .getComponent(TerrainComponent.class).getTileSize() / 3;

        projectile.setPosition(sourcePosition);
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
