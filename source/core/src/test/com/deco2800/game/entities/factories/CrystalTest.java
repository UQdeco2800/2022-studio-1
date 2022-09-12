//package com.deco2800.game.entities.factories;
//
//import com.deco2800.game.components.CombatStatsComponent;
//import com.deco2800.game.components.camera.CameraActions;
//import com.deco2800.game.entities.EntityService;
//import com.deco2800.game.entities.factories.CrystalFactory;
//import com.deco2800.game.extensions.GameExtension;
//import com.deco2800.game.physics.PhysicsService;
//import com.deco2800.game.rendering.RenderService;
//import com.deco2800.game.services.ServiceLocator;
//import com.deco2800.game.utils.RenderUtil;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.Mockito;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import util.EntityUtil;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//
//@ExtendWith(GameExtension.class)
//class CrystalTest {
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
//
//
//
//
//
//}
