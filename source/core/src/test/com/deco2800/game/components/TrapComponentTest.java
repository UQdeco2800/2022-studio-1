// package com.deco2800.game.components;

// import com.badlogic.gdx.physics.box2d.Fixture;
// import com.deco2800.game.entities.Entity;
// import com.deco2800.game.extensions.GameExtension;
// import com.deco2800.game.physics.PhysicsService;
// import com.deco2800.game.physics.components.HitboxComponent;
// import com.deco2800.game.physics.components.PhysicsComponent;
// import com.deco2800.game.services.ServiceLocator;
// import org.junit.jupiter.api.BeforeEach;
// import com.deco2800.game.components.infrastructure.TrapComponent;
// import org.junit.jupiter.api.Test;
// import org.junit.jupiter.api.extension.ExtendWith;
// import util.EntityUtil;

// import static org.junit.jupiter.api.Assertions.assertEquals;

// @ExtendWith(GameExtension.class)
// class TrapComponentTest {
//     @BeforeEach
//   void beforeEach() {
//     ServiceLocator.registerPhysicsService(new PhysicsService());
//   }

//   @Test
//   void shouldTrap() {
//     short targetLayer = (1 << 3);
//     Entity entity = EntityUtil.createTrap(targetLayer);
//     Entity target = EntityUtil.createTarget(targetLayer);

//     Fixture entityFixture = entity.getComponent(HitboxComponent.class).getFixture();
//     Fixture targetFixture = target.getComponent(HitboxComponent.class).getFixture();
//     entity.getEvents().trigger("collisionStart", entityFixture, targetFixture);

//     assertEquals(0, target.getComponent(CombatStatsComponent.class).getHealth());
//   }

//   @Test
//   void shouldNotTrapOtherLayer() {
//     short targetLayer = (1 << 3);
//     short attackLayer = (1 << 4);
//     Entity entity = EntityUtil.createTrap(attackLayer);
//     Entity target = EntityUtil.createTarget(targetLayer);

//     Fixture entityFixture = entity.getComponent(HitboxComponent.class).getFixture();
//     Fixture targetFixture = target.getComponent(HitboxComponent.class).getFixture();
//     entity.getEvents().trigger("collisionStart", entityFixture, targetFixture);

//     assertEquals(10, target.getComponent(CombatStatsComponent.class).getHealth());
//   }

//   @Test
//   void shouldNotTrapWithoutCombatComponent() {
//     short targetLayer = (1 << 3);
//     Entity entity = EntityUtil.createTrap(targetLayer);
//     // Target does not have a combat component
//     Entity target =
//         new Entity()
//             .addComponent(new PhysicsComponent())
//             .addComponent(new HitboxComponent().setLayer(targetLayer));
//     target.create();

//     Fixture entityFixture = entity.getComponent(HitboxComponent.class).getFixture();
//     Fixture targetFixture = target.getComponent(HitboxComponent.class).getFixture();

//     // This should not cause an exception, but the attack should be ignored
//     entity.getEvents().trigger("collisionStart", entityFixture, targetFixture);
//   }

//   @Test
//   void trapRemoved(){
//     short targetLayer = (1 << 3);
//     Entity entity = EntityUtil.createTrap(targetLayer);
//     Entity target = EntityUtil.createTarget(targetLayer);

//     Fixture entityFixture = entity.getComponent(HitboxComponent.class).getFixture();
//     Fixture targetFixture = target.getComponent(HitboxComponent.class).getFixture();
//     entity.getEvents().trigger("collisionStart", entityFixture, targetFixture);

//     //Tests that the trap's health was set to 0 
//     assertEquals(0, entity.getComponent(CombatStatsComponent.class).getHealth());

//     //tests trap was removed from map

//     //TODO ------------------------FINISH------------------------

//   }
// }
