package com.deco2800.game.entities;

import com.badlogic.gdx.math.GridPoint2;
import com.deco2800.game.areas.terrain.TerrainComponent;
import com.deco2800.game.components.CameraComponent;
import com.deco2800.game.components.camera.CameraActions;
import com.deco2800.game.extensions.GameExtension;
import com.deco2800.game.files.FileLoader;
import com.deco2800.game.input.InputService;
import com.deco2800.game.physics.PhysicsService;
import com.deco2800.game.rendering.RenderService;
import com.deco2800.game.services.*;
import com.deco2800.game.services.configs.DayNightCycleConfig;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(GameExtension.class)
class StructureServiceTest {

    private static String[] textures = {
            "images/attack_towers/tow1_1_l.png",
            "images/upgradeFail500.png",
            "images/upgradeFail1500.png",
            "images/tutorials/crystalLevelPopUp.png",
            "images/crystal2.0.png",
            "images/crystal1.png",
            "images/crystal2.png",
            "images/crystal3.png",
            "images/attack_towers/tow1_1_r.png",
            "images/attack_towers/tow1_2_l.png",
            "images/attack_towers/tow1_2_r.png",
            "images/attack_towers/tow1_3_l.png",
            "images/attack_towers/tow1_3_r.png",
            "images/attack_towers/tempStructures/temp_tow1_1_l.png",
            "images/attack_towers/tempStructures/temp_tow1_1_r.png",
            "images/attack_towers/elecball.png",
            "images/65x33_tiles/wall_left.png",
            "images/65x33_tiles/wall_right.png",
            "images/65x33_tiles/temp_wall_left.png",
            "images/65x33_tiles/temp_wall_right.png",
            "images/attack_towers/tempStructures/temp_tower3lv1Left.png",
            "images/attack_towers/tempStructures/temp_tower3lv1Right.png",
            "images/attack_towers/tower3lv1Left.png",
            "images/attack_towers/tower3lv1Right.png",
            "images/attack_towers/tower3lv2Left.png",
            "images/attack_towers/tower3lv2Right.png",
            "images/attack_towers/tower3lv3Left.png",
            "images/attack_towers/tower3lv3Right.png",
            "images/attack_towers/Attack_Structure2_lev1.png",
            "images/attack_towers/Attack_Structure2_lev2.png",
            "images/attack_towers/Attack_Structure2_lev3.png",
            "images/attack_towers/tempStructures/temp_Attack_Structure2_lev1.png",
            "images/attack_towers/tempStructures/temp_Attack_Structure2_lev1.png",
            "images/shop_structures_sprites/Trap_shop_sprite.png",
            "images/shop_structures_sprites/Trap2_shop_sprite.png"};

    @BeforeEach
    void beforeEach() {
        ServiceLocator.registerTimeSource(new GameTime());

        EntityService entityService = new EntityService();
        StructureService structureService = new StructureService();
        DayNightCycleService dayNightCycleService = new DayNightCycleService(ServiceLocator.getTimeSource(),
                FileLoader.readClass(DayNightCycleConfig.class, "configs/DayNight.json"));
        ResourceService resourceService = new ResourceService();
        RenderService renderService = new RenderService();
        InputService inputService = new InputService();

        AchievementHandler achievementHandler = new AchievementHandler();
        PhysicsService physicsService = new PhysicsService();
        UGS ugs = mock(UGS.class);
        when(ugs.checkEntityPlacement(new GridPoint2(50,50), "structure")).thenReturn(true);
        when(ugs.checkEntityPlacement(new GridPoint2(60,60), "structure")).thenReturn(false);



        ServiceLocator.registerEntityService(entityService);
        ServiceLocator.registerStructureService(structureService);
        ServiceLocator.registerDayNightCycleService(dayNightCycleService);
        ServiceLocator.registerResourceService(resourceService);
        ServiceLocator.registerRenderService(renderService);
        ServiceLocator.registerAchievementHandler(achievementHandler);
        ServiceLocator.registerPhysicsService(physicsService);
        ServiceLocator.registerUGSService(ugs);
        ServiceLocator.registerInputService(inputService);

        resourceService.loadTextures(textures);

        //mock terrain to avoid nullpointerexception when scaling entities
        TerrainComponent terrain = mock(TerrainComponent.class);
        when(terrain.getTileSize()).thenReturn(16f);


        Entity terrainEntity = new Entity();
        terrainEntity.setName("terrain");
        terrainEntity.addComponent(terrain);
        ServiceLocator.getEntityService().registerNamed("terrain", terrainEntity);

        Entity camera = new Entity().addComponent(new CameraComponent());

        camera.addComponent(new CameraActions());

        ServiceLocator.getEntityService().registerNamed("camera", camera);
        ugs.generateUGS();

        while (!resourceService.loadForMillis(10)) {
        }
    }

    @Test
    void shouldRegisterStructure() {
        ServiceLocator.getStructureService().registerNamed("wall", new Entity());
        assertTrue(ServiceLocator.getStructureService().getAllNamedEntities().containsKey("wall"));
    }

    @Test
    void shouldUnregisterStructure() {
        ServiceLocator.getStructureService().unregisterNamed("wall");
        assertFalse(ServiceLocator.getStructureService().getAllNamedEntities().containsKey("wall"));
    }

    @Test
    void shouldGetLastAddedEntityToStructureService() {
        ServiceLocator.getStructureService().registerNamed("wall", new Entity());
        Entity lastEntity = ServiceLocator.getStructureService().getLastEntity();
        assertTrue(lastEntity != null);
    }

    @Test
    void shouldBuildStructure() {
        Boolean isBuilt;
        isBuilt = ServiceLocator.getStructureService().buildStructure("wall", new GridPoint2(50, 50));
        assertTrue(isBuilt);
        isBuilt = ServiceLocator.getStructureService().buildStructure("tower1", new GridPoint2(50, 50));
        assertTrue(isBuilt);
        isBuilt = ServiceLocator.getStructureService().buildStructure("tower2", new GridPoint2(50, 50));
        assertTrue(isBuilt);
        isBuilt = ServiceLocator.getStructureService().buildStructure("tower3", new GridPoint2(50, 50));
        assertTrue(isBuilt);
        isBuilt = ServiceLocator.getStructureService().buildStructure("trap1", new GridPoint2(50, 50));
        assertTrue(isBuilt);
        isBuilt = ServiceLocator.getStructureService().buildStructure("trap2", new GridPoint2(50, 50));
        assertTrue(isBuilt);
    }

    @Test
    void shouldNotBuildStructureOnFullTile() {
        Boolean isBuilt;
        isBuilt = ServiceLocator.getStructureService().buildStructure("wall", new GridPoint2(60, 60));
        assertFalse(isBuilt);
        isBuilt = ServiceLocator.getStructureService().buildStructure("tower1", new GridPoint2(60, 60));
        assertFalse(isBuilt);
        isBuilt = ServiceLocator.getStructureService().buildStructure("tower2", new GridPoint2(60, 60));
        assertFalse(isBuilt);
        isBuilt = ServiceLocator.getStructureService().buildStructure("tower3", new GridPoint2(60, 60));
        assertFalse(isBuilt);
        isBuilt = ServiceLocator.getStructureService().buildStructure("trap1", new GridPoint2(60, 60));
        assertFalse(isBuilt);
        isBuilt = ServiceLocator.getStructureService().buildStructure("trap2", new GridPoint2(60, 60));
        assertFalse(isBuilt);
    }

    @Test
    void shouldGetTempBuildStateFalse() {
        ServiceLocator.getStructureService().setTempBuildState(false);
        Boolean tempBuildState = ServiceLocator.getStructureService().getTempBuildState();
        assertFalse(tempBuildState);
    }

    @Test
    void shouldGetTempBuildStateTrue() {
        ServiceLocator.getStructureService().setTempBuildState(true);
        Boolean tempBuildState = ServiceLocator.getStructureService().getTempBuildState();
        assertTrue(tempBuildState);
    }

    @Test
    void shouldSetTempBuildStateFalse() {
        ServiceLocator.getStructureService().setTempBuildState(false);
        Boolean tempBuildState = ServiceLocator.getStructureService().getTempBuildState();
        assertFalse(tempBuildState);
    }

    @Test
    void shouldSetTempBuildStateTrue() {
        ServiceLocator.getStructureService().setTempBuildState(true);
        Boolean tempBuildState = ServiceLocator.getStructureService().getTempBuildState();
        assertTrue(tempBuildState);
    }

    @Test
    void shouldGetStructureOrientationLeft() {
        ServiceLocator.registerStructureService(new StructureService());
        int orientation = ServiceLocator.getStructureService().getStructureOrientation();
        assertEquals(0, orientation);
    }

    @Test
    void shouldGetStructureOrientationRight() {
        ServiceLocator.registerStructureService(new StructureService());
        ServiceLocator.getStructureService().toggleStructureOrientation();
        int orientation = ServiceLocator.getStructureService().getStructureOrientation();
        assertEquals(1, orientation);
    }

    @Test
    void shouldToggleStructureOrientation() {
        int orientation;
        ServiceLocator.registerStructureService(new StructureService());
        orientation = ServiceLocator.getStructureService().getStructureOrientation();
        assertEquals(1, orientation);
        ServiceLocator.getStructureService().toggleStructureOrientation();
        orientation = ServiceLocator.getStructureService().getStructureOrientation();
        assertEquals(0, orientation);
    }

}