package com.deco2800.game.components;

import com.badlogic.gdx.physics.box2d.Fixture;
import com.deco2800.game.entities.Entity;
import com.deco2800.game.extensions.GameExtension;
import com.deco2800.game.physics.PhysicsService;
import com.deco2800.game.physics.components.HitboxComponent;
import com.deco2800.game.physics.components.PhysicsComponent;
import com.deco2800.game.services.ServiceLocator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import util.EntityUtil;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(GameExtension.class)
class TouchAttackComponentTest {
  @BeforeEach
  void beforeEach() {
    ServiceLocator.registerPhysicsService(new PhysicsService());
  }

  @Test
  void shouldAttack() {
    short targetLayer = (1 << 3);
    Entity entity = EntityUtil.createAttacker(targetLayer);
    Entity target = EntityUtil.createTarget(targetLayer);

    Fixture entityFixture = entity.getComponent(HitboxComponent.class).getFixture();
    Fixture targetFixture = target.getComponent(HitboxComponent.class).getFixture();
    entity.getEvents().trigger("collisionStart", entityFixture, targetFixture);

    assertEquals(0, target.getComponent(CombatStatsComponent.class).getHealth());
  }

  @Test
  void shouldNotAttackOtherLayer() {
    short targetLayer = (1 << 3);
    short attackLayer = (1 << 4);
    Entity entity = EntityUtil.createAttacker(attackLayer);
    Entity target = EntityUtil.createTarget(targetLayer);

    Fixture entityFixture = entity.getComponent(HitboxComponent.class).getFixture();
    Fixture targetFixture = target.getComponent(HitboxComponent.class).getFixture();
    entity.getEvents().trigger("collisionStart", entityFixture, targetFixture);

    assertEquals(10, target.getComponent(CombatStatsComponent.class).getHealth());
  }

  @Test
  void shouldNotAttackWithoutCombatComponent() {
    short targetLayer = (1 << 3);
    Entity entity = EntityUtil.createAttacker(targetLayer);
    // Target does not have a combat component
    Entity target =
        new Entity()
            .addComponent(new PhysicsComponent())
            .addComponent(new HitboxComponent().setLayer(targetLayer));
    target.create();

    Fixture entityFixture = entity.getComponent(HitboxComponent.class).getFixture();
    Fixture targetFixture = target.getComponent(HitboxComponent.class).getFixture();

    // This should not cause an exception, but the attack should be ignored
    entity.getEvents().trigger("collisionStart", entityFixture, targetFixture);
  }


}
