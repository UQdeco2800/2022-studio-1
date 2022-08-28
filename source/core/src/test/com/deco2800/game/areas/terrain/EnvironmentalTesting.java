package com.deco2800.game.areas.terrain;

import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.deco2800.game.components.CombatStatsComponent;
import com.deco2800.game.components.Environmental.CollisionEffectComponent;
import com.deco2800.game.components.Environmental.EnvironmentalComponent;
import com.deco2800.game.components.player.PlayerActions;
import com.deco2800.game.entities.Entity;
import com.deco2800.game.extensions.GameExtension;
import com.deco2800.game.physics.PhysicsService;
import com.deco2800.game.physics.components.ColliderComponent;
import com.deco2800.game.physics.components.HitboxComponent;
import com.deco2800.game.physics.components.PhysicsComponent;
import com.deco2800.game.physics.components.PhysicsMovementComponent;
import com.deco2800.game.services.ResourceService;
import com.deco2800.game.services.ServiceLocator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.junit.jupiter.api.Assertions.*;


@ExtendWith(GameExtension.class)
class EnvironmentalTesting {

    @Test
    void createdEnvironmentalTree() {
        EnvironmentalComponent tree = new EnvironmentalComponent().setObstacle(EnvironmentalComponent.EnvironmentalObstacle.TREE);
        assertEquals(tree.getType(), EnvironmentalComponent.ResourceTypes.WOOD);
    }

    @Test
    void assertCorrectEnvironmentalType() {
        EnvironmentalComponent tree = new EnvironmentalComponent().setObstacle(EnvironmentalComponent.EnvironmentalObstacle.TREE);
        assertTrue(tree.getObstacle() instanceof EnvironmentalComponent.EnvironmentalObstacle);
    }

    @Test
    void assertValuesGreaterZeroEnvironmentalObjects() {
        EnvironmentalComponent component = new EnvironmentalComponent();
        for (EnvironmentalComponent.EnvironmentalObstacle obstacle: EnvironmentalComponent.EnvironmentalObstacle.values()) {
            component.setObstacle(obstacle);
            assertTrue(component.getResourceAmount() >= 0);
        }
    }

    @Test
    void assertValuesCorrectTypeEnvironmentalObject() {
        EnvironmentalComponent component = new EnvironmentalComponent();
        for (EnvironmentalComponent.EnvironmentalObstacle obstacle: EnvironmentalComponent.EnvironmentalObstacle.values()) {
            component.setObstacle(obstacle);
            assertTrue(component.getType() instanceof EnvironmentalComponent.ResourceTypes);
        }
    }

    @Test
    void assertSetAoeNullProtection() {
        ServiceLocator.registerPhysicsService(new PhysicsService());
       Entity entity = new Entity()
                .addComponent(new PhysicsComponent())
                .addComponent(new ColliderComponent())
                .addComponent(new EnvironmentalComponent())
                .addComponent(new CollisionEffectComponent(CollisionEffectComponent.CollisionEffect.DIVERT, 0.5f));
       entity.create();
       CollisionEffectComponent cec = entity.getComponent(CollisionEffectComponent.class);
       cec.setAoe(true);
       assertEquals(cec.isAoe(), false);
    }

    @Test
    void assertSlowWorks() {
        ServiceLocator.registerPhysicsService(new PhysicsService());
        Entity entity = createCollisionObject(CollisionEffectComponent.CollisionEffect.SLOW);
        Entity target = createPlayer();

        Vector2 initialSpeed = target.getComponent(PlayerActions.class).getPlayerSpeed();

        Fixture entityFixture = entity.getComponent(ColliderComponent.class).getFixture();
        Fixture targetFixture = target.getComponent(HitboxComponent.class).getFixture();

        entity.getEvents().trigger("collisionStart", entityFixture, targetFixture);

        //account for floating point errors
        assertTrue(initialSpeed.x * 0.5f - target.getComponent(PlayerActions.class).getPlayerSpeed().x < 0.001f);
        assertTrue(initialSpeed.y * 0.5f - target.getComponent(PlayerActions.class).getPlayerSpeed().y < 0.001f);

        entity.getEvents().trigger("collisionEnd", entityFixture, targetFixture);

        assertTrue(initialSpeed.x - target.getComponent(PlayerActions.class).getPlayerSpeed().x  < 0.001f);
        assertTrue(initialSpeed.y - target.getComponent(PlayerActions.class).getPlayerSpeed().y < 0.001f);
    }

    void assertDamageWorks() {
        ServiceLocator.registerPhysicsService(new PhysicsService());
        Entity entity = createCollisionObject(CollisionEffectComponent.CollisionEffect.DAMAGE);
        Entity target = createPlayer();

        Fixture entityFixture = entity.getComponent(ColliderComponent.class).getFixture();
        Fixture targetFixture = target.getComponent(HitboxComponent.class).getFixture();

        entity.getEvents().trigger("collisionStart", entityFixture, targetFixture);

        assertEquals(0, target.getComponent(CombatStatsComponent.class).getHealth());

    }
    @Test
    void assertEffectTargetWorks() {
        ServiceLocator.registerPhysicsService(new PhysicsService());

        Entity entity = createCollisionObject(CollisionEffectComponent.CollisionEffect.DAMAGE);
        Entity player = createPlayer();
        Entity npc = createNpc();
        entity.getComponent(CollisionEffectComponent.class).setEffectTarget(CollisionEffectComponent.EffectTarget.PLAYER);

        Fixture entityFixture = entity.getComponent(ColliderComponent.class).getFixture();
        Fixture playerFixture = player.getComponent(HitboxComponent.class).getFixture();
        Fixture npcFixture = npc.getComponent(HitboxComponent.class).getFixture();

        entity.getEvents().trigger("collisionStart", entityFixture, npcFixture);
        entity.getEvents().trigger("collisionStart", entityFixture, playerFixture);

        assertEquals(0, player.getComponent(CombatStatsComponent.class).getHealth());
        assertEquals(1, npc.getComponent(CombatStatsComponent.class).getHealth());
    }

    Entity createPlayer() {
        Entity player = new Entity()
                .addComponent(new PlayerActions())
                .addComponent(new HitboxComponent())
                .addComponent(new PhysicsComponent())
                .addComponent(new CombatStatsComponent(1, 0));
        player.create();
        return player;
    }

    Entity createNpc() {
        Entity npc = new Entity()
                .addComponent(new PhysicsMovementComponent())
                .addComponent(new HitboxComponent())
                .addComponent(new PhysicsComponent())
                .addComponent(new CombatStatsComponent(1, 0));
        npc.create();
        return npc;
    }

    Entity createCollisionObject(CollisionEffectComponent.CollisionEffect effect) {
        Entity object = new Entity()
                .addComponent(new PhysicsComponent())
                .addComponent(new ColliderComponent())
                .addComponent(new EnvironmentalComponent())
                .addComponent(new CollisionEffectComponent(effect, 0.5f));
        object.create();
        return object;
    }


    @Test
    void assertCollisionAddWorksEnvironmentalObject() {

        ServiceLocator.registerResourceService(new ResourceService());

        Entity tree = new Entity().addComponent( new EnvironmentalComponent().setObstacle(EnvironmentalComponent.EnvironmentalObstacle.TREE));
        Entity rock = new Entity().addComponent( new EnvironmentalComponent().setObstacle(EnvironmentalComponent.EnvironmentalObstacle.ROCK));
        Entity vine = new Entity().addComponent( new EnvironmentalComponent().setObstacle(EnvironmentalComponent.EnvironmentalObstacle.VINE));

        EnvironmentalCollision collision = new EnvironmentalCollision(new TerrainComponent(null, null, null, null, 1f));
        collision.addEntity(tree);
        collision.addEntity(rock);
        collision.addEntity(vine);

        //only 1 is expected as hashmap is used.
        assertEquals(1, collision.getEntities().size());
    }

    @Test
    void assertCollisionAddMultipleEnvironmentalObject() {

        ServiceLocator.registerResourceService(new ResourceService());

        Entity tree = new Entity().addComponent( new EnvironmentalComponent().setObstacle(EnvironmentalComponent.EnvironmentalObstacle.TREE));
        Entity rock = new Entity().addComponent( new EnvironmentalComponent().setObstacle(EnvironmentalComponent.EnvironmentalObstacle.ROCK));
        Entity vine = new Entity().addComponent( new EnvironmentalComponent().setObstacle(EnvironmentalComponent.EnvironmentalObstacle.VINE));

        EnvironmentalCollision collision = new EnvironmentalCollision(new TerrainComponent(null, null, null, null, 1f));
        tree.setPosition(10, 10);
        collision.addEntity(tree);
        rock.setPosition(20, 20);
        collision.addEntity(rock);
        vine.setPosition(-10, -10);
        collision.addEntity(vine);

        assertEquals(3, collision.getEntities().size());
    }

    @Test
    void assertCollisionTrueSingleEnvironmentalObject() {

    ServiceLocator.registerResourceService(new ResourceService());

        Entity tree = new Entity().addComponent( new EnvironmentalComponent().setObstacle(EnvironmentalComponent.EnvironmentalObstacle.TREE));
        Entity rock = new Entity().addComponent( new EnvironmentalComponent().setObstacle(EnvironmentalComponent.EnvironmentalObstacle.ROCK));
        Entity vine = new Entity().addComponent( new EnvironmentalComponent().setObstacle(EnvironmentalComponent.EnvironmentalObstacle.VINE));

        EnvironmentalCollision collision = new EnvironmentalCollision(new TerrainComponent(null, new TiledMap(), null, TerrainComponent.TerrainOrientation.ISOMETRIC, 0.5f));

        collision.addEntity(tree);

        assertTrue(collision.wouldCollide(rock, 0, 0));
    }


    @Test
    void assertCollisionFalseSingleEnvironmentalObject() {

        ServiceLocator.registerResourceService(new ResourceService());

        Entity tree = new Entity().addComponent( new EnvironmentalComponent().setObstacle(EnvironmentalComponent.EnvironmentalObstacle.TREE));
        Entity rock = new Entity().addComponent( new EnvironmentalComponent().setObstacle(EnvironmentalComponent.EnvironmentalObstacle.ROCK));
        Entity vine = new Entity().addComponent( new EnvironmentalComponent().setObstacle(EnvironmentalComponent.EnvironmentalObstacle.VINE));

        EnvironmentalCollision collision = new EnvironmentalCollision(new TerrainComponent(null, new TiledMap(), null, TerrainComponent.TerrainOrientation.ISOMETRIC, 0.5f));

        collision.addEntity(tree);

        assertFalse(collision.wouldCollide(rock, 10, 10));
    }


    @Test
    void assertCollisionXSlightlyOffSingleEnvironmentalObject() {

        ServiceLocator.registerResourceService(new ResourceService());

        Entity tree = new Entity().addComponent( new EnvironmentalComponent().setObstacle(EnvironmentalComponent.EnvironmentalObstacle.TREE));
        Entity rock = new Entity().addComponent( new EnvironmentalComponent().setObstacle(EnvironmentalComponent.EnvironmentalObstacle.ROCK));

        EnvironmentalCollision collision = new EnvironmentalCollision(new TerrainComponent(null, new TiledMap(), null, TerrainComponent.TerrainOrientation.ISOMETRIC, 0.5f));

        collision.addEntity(tree);

        assertTrue(collision.wouldCollide(rock, 1, 0));
    }

    @Test
    void assertCollisionYSlightlyOffSingleEnvironmentalObject() {

        ServiceLocator.registerResourceService(new ResourceService());

        Entity tree = new Entity().addComponent( new EnvironmentalComponent().setObstacle(EnvironmentalComponent.EnvironmentalObstacle.TREE));
        Entity rock = new Entity().addComponent( new EnvironmentalComponent().setObstacle(EnvironmentalComponent.EnvironmentalObstacle.ROCK));

        EnvironmentalCollision collision = new EnvironmentalCollision(new TerrainComponent(null, new TiledMap(), null, TerrainComponent.TerrainOrientation.ISOMETRIC, 0.5f));

        collision.addEntity(tree);

        assertTrue(collision.wouldCollide(rock, 0, 1));
    }
}

