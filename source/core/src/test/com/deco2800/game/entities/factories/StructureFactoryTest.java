// package com.deco2800.game.entities.factories;

// import com.deco2800.game.components.player.InventoryComponent;
// import com.deco2800.game.entities.Entity;
// import com.deco2800.game.entities.EntityService;
// import com.deco2800.game.entities.StructureService;
// import com.deco2800.game.entities.configs.StructureConfig;
// import com.deco2800.game.extensions.GameExtension;
// import com.deco2800.game.files.FileLoader;
// import com.deco2800.game.input.InputService;
// import com.deco2800.game.physics.PhysicsService;
// import com.deco2800.game.rendering.RenderService;
// import com.deco2800.game.services.ResourceService;
// import com.deco2800.game.services.ServiceLocator;
// import com.deco2800.game.utils.RenderUtil;

// import org.junit.jupiter.api.Test;
// import org.junit.jupiter.api.extension.ExtendWith;
// import org.junit.runner.RunWith;
// import org.mockito.Mock;
// import org.mockito.Mockito;
// import org.mockito.junit.MockitoJUnitRunner;

// import static org.junit.jupiter.api.Assertions.*;

// import org.junit.Before;

// @ExtendWith(GameExtension.class)
// @RunWith(MockitoJUnitRunner.class)
// class StructureFactoryTest {
    
//    private static final StructureConfig configs =
//            FileLoader.readClass(StructureConfig.class, "configs/structure.json");

//     private RenderUtil renderUtil;
//     private InputService inputService;
//     private RenderService renderService;
//     private ResourceService resourceService;
//     private EntityService entityService;
//     @Mock Entity entity; 
//    /**
//     * Test that the structure wall health configuration is loaded properly
//     */
//    @Test
//    void shouldTestGetWallHealth() {
//        assertEquals(40,configs.wall.health);
//    }

//    /**
//     * Test that the structure wall baseAttack configuration is loaded properly
//     */
//    @Test
//    void shouldTestGetWallAttack() {
//        assertEquals(0,configs.wall.baseAttack);
//    }

//    /**
//     * Test that the structure tower1 health configuration is loaded properly
//     */
//    @Test
//    void shouldTestGetTower1Health() {
//        assertEquals(200,configs.tower1.health);
//    }

//    /**
//     * Test that the structure tower1 baseAttack configuration is loaded properly
//     */
//    @Test
//    void shouldTestGetTower1Attack() {
//        assertEquals(10,configs.tower1.baseAttack);
//    }
   
//    @Before
//    void before() {
//        ServiceLocator.registerPhysicsService(new PhysicsService());
//        ServiceLocator.registerRenderService(new RenderService());
//        renderUtil = Mockito.spy(RenderUtil.getInstance());
//        renderService = Mockito.spy(ServiceLocator.getRenderService());

//        ServiceLocator.registerInputService(new InputService());
//        inputService = Mockito.spy(ServiceLocator.getInputService());
//    }

//    @Test void testHandleRefund() {
//     // ServiceLocator.registerInputService(new InputService());
//     // inputService = Mockito.spy(ServiceLocator.getInputService());
//     // ServiceLocator.registerResourceService(new ResourceService());
//     // resourceService = Mockito.spy(ServiceLocator.getResourceService());
//     // ServiceLocator.registerEntityService(new EntityService());
//     // entityService = Mockito.spy(ServiceLocator.getEntityService());
//     // ServiceLocator.registerPhysicsService(new PhysicsService());
//     // ServiceLocator.registerRenderService(new RenderService());
//     // renderUtil = Mockito.spy(RenderUtil.getInstance());
//     // renderService = Mockito.spy(ServiceLocator.getRenderService());
    


//     Entity player = PlayerFactory.createPlayer();
//     Entity structure = StructureFactory.createTower1(1);
//     StructureFactory.handleRefund(structure, 80f);
//     assertEquals(player.getComponent(InventoryComponent.class).getGold(), 80) ;

//    }
// }