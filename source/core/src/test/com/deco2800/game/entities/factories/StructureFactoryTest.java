package com.deco2800.game.entities.factories;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.headless.HeadlessFiles;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.Vector2;
import com.deco2800.game.achievements.Achievement;
import com.deco2800.game.areas.terrain.TerrainComponent;
import com.deco2800.game.components.CameraComponent;
import com.deco2800.game.components.camera.CameraActions;
import com.deco2800.game.components.infrastructure.OrientationComponent;
import com.deco2800.game.entities.Entity;
import com.deco2800.game.entities.EntityService;
import com.deco2800.game.entities.StructureService;
import com.deco2800.game.entities.UGS;
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

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(GameExtension.class)
class StructureFactoryTest {
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
        UGS ugs = new UGS();

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
        when(terrain.getTileSize()).thenReturn(1f);

        Entity terrainEntity = new Entity();
        terrainEntity.setName("terrain");
        terrainEntity.addComponent(terrain);
        ServiceLocator.getEntityService().registerNamed("terrain", terrainEntity);

        Entity camera = new Entity().addComponent(new CameraComponent());

        camera.addComponent(new CameraActions());

        ServiceLocator.getEntityService().registerNamed("camera", camera);

        while (!resourceService.loadForMillis(10)) {
        }
    }
    //############## WALL TESTS #############
    @Test
    void shouldCreateWallEntityFacingLeft() {
        Entity wall = StructureFactory.createWall("wall", false, 0);
        assertTrue(wall != null);
        assertTrue(wall.getComponent(OrientationComponent.class).getOrientation() == 0);
    }

    @Test
    void shouldCreateWallEntityFacingRight() {
        Entity wall = StructureFactory.createWall("wall", false, 1);
        assertTrue(wall != null);
        assertTrue(wall.getComponent(OrientationComponent.class).getOrientation() == 1);
    }

    @Test
    void shouldCreateTempWallEntityFacingLeft() {
        Entity tempWall = StructureFactory.createWall("wall", true, 0);
        assertTrue(tempWall != null);
        assertTrue(tempWall.getComponent(OrientationComponent.class).getOrientation() == 0);
    }

    @Test
    void shouldCreateTempWallEntityFacingRight() {
        Entity tempWall = StructureFactory.createWall("wall", true, 1);
        assertTrue(tempWall != null);
        assertTrue(tempWall.getComponent(OrientationComponent.class).getOrientation() == 1);
    }

    //############## TOWER1 TESTS #############
    @Test
    void shouldCreateTower1Level1EntityFacingLeft() {
        Entity tower1 = StructureFactory.createTower1(1, "tower1", false, 0);
        assertTrue(tower1 != null);
        assertTrue(tower1.getComponent(OrientationComponent.class).getOrientation() == 0);
    }

    @Test
    void shouldCreateTower1Level1EntityFacingRight() {
        Entity tower1 = StructureFactory.createTower1(1, "tower1", false, 1);
        assertTrue(tower1 != null);
        assertTrue(tower1.getComponent(OrientationComponent.class).getOrientation() == 1);
    }

    @Test
    void shouldCreateTower1Level2EntityFacingLeft() {
        Entity tower1 = StructureFactory.createTower1(2, "tower1", false, 0);
        assertTrue(tower1 != null);
        assertTrue(tower1.getComponent(OrientationComponent.class).getOrientation() == 0);
    }

    @Test
    void shouldCreateTower1Level2EntityFacingRight() {
        Entity tower1 = StructureFactory.createTower1(2, "tower1", false, 1);
        assertTrue(tower1 != null);
        assertTrue(tower1.getComponent(OrientationComponent.class).getOrientation() == 1);
    }

    @Test
    void shouldCreateTower1Level3EntityFacingLeft() {
        Entity tower1 = StructureFactory.createTower1(3, "tower1", false, 0);
        assertTrue(tower1 != null);
        assertTrue(tower1.getComponent(OrientationComponent.class).getOrientation() == 0);
    }

    @Test
    void shouldCreateTower1Level3EntityFacingRight() {
        Entity tower1 = StructureFactory.createTower1(3, "tower1", false, 1);
        assertTrue(tower1 != null);
        assertTrue(tower1.getComponent(OrientationComponent.class).getOrientation() == 1);
    }

    @Test
    void shouldCreateTempTower1Level1EntityFacingLeft() {
        Entity tower1 = StructureFactory.createTower1(1, "tower1", true, 0);
        assertTrue(tower1 != null);
        assertTrue(tower1.getComponent(OrientationComponent.class).getOrientation() == 0);
    }

    @Test
    void shouldCreateTempTower1Level1EntityFacingRight() {
        Entity tower1 = StructureFactory.createTower1(1, "tower1", true, 1);
        assertTrue(tower1 != null);
        assertTrue(tower1.getComponent(OrientationComponent.class).getOrientation() == 1);
    }

    //############## TOWER2 TESTS #############
    @Test
    void shouldCreateTower2Level1EntityFacingLeft() {
        Entity tower2 = StructureFactory.createTower2(1, "tower2", false, 0);
        assertTrue(tower2 != null);
        assertTrue(tower2.getComponent(OrientationComponent.class).getOrientation() == 0);
    }

    @Test
    void shouldCreateTower2Level1EntityFacingRight() {
        Entity tower2 = StructureFactory.createTower2(1, "tower2", false, 1);
        assertTrue(tower2 != null);
        assertTrue(tower2.getComponent(OrientationComponent.class).getOrientation() == 1);
    }

    @Test
    void shouldCreateTower2Level2EntityFacingLeft() {
        Entity tower2 = StructureFactory.createTower2(2, "tower2", false, 0);
        assertTrue(tower2 != null);
        assertTrue(tower2.getComponent(OrientationComponent.class).getOrientation() == 0);
    }

    @Test
    void shouldCreateTower2Level2EntityFacingRight() {
        Entity tower2 = StructureFactory.createTower2(2, "tower2", false, 1);
        assertTrue(tower2 != null);
        assertTrue(tower2.getComponent(OrientationComponent.class).getOrientation() == 1);
    }

    @Test
    void shouldCreateTower2Level3EntityFacingLeft() {
        Entity tower2 = StructureFactory.createTower2(3, "tower2", false, 0);
        assertTrue(tower2 != null);
        assertTrue(tower2.getComponent(OrientationComponent.class).getOrientation() == 0);
    }

    @Test
    void shouldCreateTower2Level3EntityFacingRight() {
        Entity tower2 = StructureFactory.createTower2(3, "tower2", false, 1);
        assertTrue(tower2 != null);
        assertTrue(tower2.getComponent(OrientationComponent.class).getOrientation() == 1);
    }

    @Test
    void shouldCreateTempTower2Level1EntityFacingLeft() {
        Entity tower2 = StructureFactory.createTower2(1, "tower2", true, 0);
        assertTrue(tower2 != null);
        assertTrue(tower2.getComponent(OrientationComponent.class).getOrientation() == 0);
    }

    @Test
    void shouldCreateTempTower2Level1EntityFacingRight() {
        Entity tower2 = StructureFactory.createTower2(1, "tower2", true, 1);
        assertTrue(tower2 != null);
        assertTrue(tower2.getComponent(OrientationComponent.class).getOrientation() == 1);
    }

    //############## TOWER3 TESTS #############
    @Test
    void shouldCreateTower3Level1EntityFacingLeft() {
        Entity tower3 = StructureFactory.createTower3(1, "tower3", false, 0);
        assertTrue(tower3 != null);
        assertTrue(tower3.getComponent(OrientationComponent.class).getOrientation() == 0);
    }

    @Test
    void shouldCreateTower3Level1EntityFacingRight() {
        Entity tower3 = StructureFactory.createTower3(1, "tower3", false, 1);
        assertTrue(tower3 != null);
        assertTrue(tower3.getComponent(OrientationComponent.class).getOrientation() == 1);
    }

    @Test
    void shouldCreateTower3Level2EntityFacingLeft() {
        Entity tower3 = StructureFactory.createTower3(2, "tower3", false, 0);
        assertTrue(tower3 != null);
        assertTrue(tower3.getComponent(OrientationComponent.class).getOrientation() == 0);
    }

    @Test
    void shouldCreateTower3Level2EntityFacingRight() {
        Entity tower3 = StructureFactory.createTower3(2, "tower3", false, 1);
        assertTrue(tower3 != null);
        assertTrue(tower3.getComponent(OrientationComponent.class).getOrientation() == 1);
    }

    @Test
    void shouldCreateTower3Level3EntityFacingLeft() {
        Entity tower3 = StructureFactory.createTower3(3, "tower3", false, 0);
        assertTrue(tower3 != null);
        assertTrue(tower3.getComponent(OrientationComponent.class).getOrientation() == 0);
    }

    @Test
    void shouldCreateTower3Level3EntityFacingRight() {
        Entity tower3 = StructureFactory.createTower3(3, "tower3", false, 1);
        assertTrue(tower3 != null);
        assertTrue(tower3.getComponent(OrientationComponent.class).getOrientation() == 1);
    }

    @Test
    void shouldCreateTempTower3Level1EntityFacingLeft() {
        Entity tower3 = StructureFactory.createTower3(1, "tower3", true, 0);
        assertTrue(tower3 != null);
        assertTrue(tower3.getComponent(OrientationComponent.class).getOrientation() == 0);
    }

    @Test
    void shouldCreateTempTower3Level1EntityFacingRight() {
        Entity tower3 = StructureFactory.createTower3(1, "tower3", true, 1);
        assertTrue(tower3 != null);
        assertTrue(tower3.getComponent(OrientationComponent.class).getOrientation() == 1);
    }

    //############## TRAP TESTS #############
    @Test
    void shouldCreateTrap1Entity() {
        Entity trap1 = StructureFactory.createTrap("trap1", false, 0);
        assertTrue(trap1 != null);
    }

    @Test
    void shouldCreateTrap2Entity() {
        Entity trap2 = StructureFactory.createTrap("trap2", false, 1);
        assertTrue(trap2 != null);
    }

    @Test
    void shouldCreateTempTrap1Entity() {
        Entity tempTrap1 = StructureFactory.createTrap("temp_trap1", true, 0);
        assertTrue(tempTrap1 != null);
    }

    @Test
    void shouldCreateTempTrap2Entity() {
        Entity tempTrap2 = StructureFactory.createTrap("temp_trap2", true, 1);
        assertTrue(tempTrap2 != null);
    }

    @Test
    void shouldCreateBaseStructure() {
        Entity baseStructure = StructureFactory.createBaseStructure("images/65x33_tiles/wall_left.png", "baseStruct", false);
        assertTrue(baseStructure != null);
    }
}
