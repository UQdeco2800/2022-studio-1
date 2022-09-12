package com.deco2800.game.entities.factories;

import com.badlogic.gdx.graphics.Texture;
import com.deco2800.game.components.*;
import com.deco2800.game.components.camera.CameraActions;
import com.deco2800.game.entities.Entity;
import com.deco2800.game.entities.EntityService;
import com.deco2800.game.entities.factories.CrystalFactory;
import com.deco2800.game.extensions.GameExtension;
import com.deco2800.game.physics.PhysicsService;
import com.deco2800.game.physics.components.*;
import com.deco2800.game.rendering.*;
import com.deco2800.game.services.ResourceService;
import com.deco2800.game.services.ServiceLocator;
import com.deco2800.game.utils.RenderUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.EntityUtil;
import com.deco2800.game.physics.PhysicsLayer;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.lang.reflect.Method;


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

    /**
     * Check whether the function is exist in CrystalFactory
     * @throws Exception
     */
    @Test
    static void checkIfCrystalClassIsUpgradable() throws Exception {
        CrystalFactory crystalMethod = mock(CrystalFactory.class);
        /*
        Assume method upgradeCrystal is not in CrystalFactory
        Then catch the method and assert method is null
         */ 
        // Class[] test = null;
        // try {
        //     Class[] param = null;
        //     Method upgradeCrystalExist = crystalMethod.getClass().getMethod("upgradeCrystal", param);
        //     // when(crystal_.getClass().getMethod("upgradeCrystal", param)).thenReturn(true);
        // } catch (NoSuchMethodException e) {
        //     assertTrue(null == null);
        // }
        if (crystalMethod != null) {
            assertTrue(crystalMethod.hasMethod("upgradeCrystal"));
            assertTrue(crystalMethod.hasMethod("triggerCrystal"));
            assertTrue(crystalMethod.hasMethod("upgradeCrystal"));
            assertTrue(crystalMethod.hasMethod("crystalClicked"));
            assertTrue(crystalMethod.hasMethod("recoverCrystalHealth"));
        } else {
            throw new Exception("Method is not exist");
        }
    }
}
