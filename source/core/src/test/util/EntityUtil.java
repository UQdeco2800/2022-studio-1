package util;

import com.deco2800.game.components.CombatStatsComponent;
import com.deco2800.game.components.HealthBarComponent;
import com.deco2800.game.components.TouchAttackComponent;
import com.deco2800.game.entities.Entity;
import com.deco2800.game.physics.components.HitboxComponent;
import com.deco2800.game.physics.components.PhysicsComponent;

public class EntityUtil {
    public static Entity createAttacker(short targetLayer) {
        Entity entity =
                new Entity()
                        .addComponent(new TouchAttackComponent(targetLayer))
                        .addComponent(new CombatStatsComponent(0, 10))
                        .addComponent(new PhysicsComponent())
                        .addComponent(new HitboxComponent());
        entity.create();
        return entity;
    }


    public static Entity createTarget(short layer) {
        Entity target =
                new Entity()
                        .addComponent(new CombatStatsComponent(10, 0))
                        .addComponent(new PhysicsComponent())
                        .addComponent(new HitboxComponent().setLayer(layer));
        target.create();
        return target;
    }

    public static Entity createTargetWithHealthBarComponent(short layer, int health, HealthBarComponent healthBarComponent) {
        Entity target =
                new Entity()
                        .addComponent(new CombatStatsComponent(health, 0))
                        .addComponent(new PhysicsComponent())
                        .addComponent(healthBarComponent)
                        .addComponent(new HitboxComponent().setLayer(layer));
        target.create();
        return target;
    }
}
