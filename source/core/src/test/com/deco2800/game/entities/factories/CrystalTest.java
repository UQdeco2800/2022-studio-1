package com.deco2800.game.entities.factories;

import com.badlogic.gdx.graphics.Texture;
import com.deco2800.game.components.CombatStatsComponent;
import com.deco2800.game.components.HealthBarComponent;
import com.deco2800.game.components.TouchAttackComponent;
import com.deco2800.game.entities.Entity;
import com.deco2800.game.extensions.GameExtension;
import com.deco2800.game.physics.PhysicsLayer;
import com.deco2800.game.physics.PhysicsService;
import com.deco2800.game.physics.components.ColliderComponent;
import com.deco2800.game.physics.components.HitboxComponent;
import com.deco2800.game.physics.components.PhysicsComponent;
import com.deco2800.game.rendering.TextureRenderComponent;
import com.deco2800.game.services.ServiceLocator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;


@ExtendWith(GameExtension.class)
@ExtendWith(MockitoExtension.class)
class CrystalTest {
//    private static final Logger logger = LoggerFactory.getLogger(CombatStatsComponent.class);
//    private EntityService entityService;
//    private RenderUtil renderUtil;
//
//
//    @BeforeEach
//    void beforeEach() {
//        ServiceLocator.registerEntityService(new EntityService());
////        ServiceLocator.registerRenderService(new RenderService());
////        renderUtil = Mockito.spy(RenderUtil.getInstance());
////        renderService = Mockito.spy(ServiceLocator.getRenderService());
//
//    }
//
////    @Test
////    void shouldUpgrade() {
////       Entity crystal= EntityUtil.mockCrystal(10,0,0,1,100);
////        ServiceLocator.getEntityService().registerNamed("crystal", crystal);
////
////        CrystalFactory.upgradeCrystal();
////    }
//    @Test
//    void shouldDetectClick() {
//        RenderFactory.createRenderer();
//        //crystal entity position is set to (60,0)
//        //mouse click position in bounds [59.8<x<60.2,-0.375<y<0.375] will be converted to int and return true
//        assertTrue(CrystalFactory.crystalClicked(60,0));
//        assertFalse(CrystalFactory.crystalClicked(59,0));
//        assertFalse(CrystalFactory.crystalClicked(60,1));
//
//
//
//    }

    @Mock Texture texture;
    @Mock Texture texture2;
    static Entity crystal;
    static Entity crystal2;

    @BeforeEach
    void crystalEntity() {
        ServiceLocator.registerPhysicsService(new PhysicsService());
        crystal = new Entity();
        crystal.addComponent(new TextureRenderComponent(texture));
        crystal.addComponent(new PhysicsComponent());
        crystal.addComponent(new ColliderComponent().setLayer(PhysicsLayer.PLAYER));
        crystal.addComponent(new HitboxComponent().setLayer(PhysicsLayer.PLAYER));
        crystal.addComponent(new TouchAttackComponent(PhysicsLayer.PLAYER, 1.5f));
        crystal.addComponent(new CombatStatsComponent(1000, 0, 0, 1, 1000));
        crystal.addComponent(new HealthBarComponent(50, 10));
    }

    // Test crystal entity exist
    @Test
    void crystalExist() {
        assertNotNull(crystal);
    }

    // Crystal stats update on upgrade
    @Test
    void upgradeCrystal() {
        CombatStatsComponent combatStatsComponent = crystal.getComponent(CombatStatsComponent.class);
        combatStatsComponent.setLevel(2);
        assertEquals(2, combatStatsComponent.getLevel());

        combatStatsComponent.setMaxHealth(1100);
        assertEquals(1100, combatStatsComponent.getMaxHealth());
        assertNotEquals(1100, combatStatsComponent.getHealth());
    }

    // Crystal stats update on upgrade
    @Test
    void recoverCrystalHealth() {
        CombatStatsComponent combatStatsComponent = crystal.getComponent(CombatStatsComponent.class);

        combatStatsComponent.setHealth(100);
        combatStatsComponent.addHealth(+20);
        assertEquals(120, combatStatsComponent.getHealth());
    }

    // Crystal stats update on upgrade
    @Test
    void triggerCrystal() {
        ServiceLocator.registerPhysicsService(new PhysicsService());
        crystal2 = new Entity();
        crystal2.setName("crystal2");
    }

}
