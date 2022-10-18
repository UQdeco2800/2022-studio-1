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
    private AchievementHandler achievementHandler;

    private static String[] textures = {"images/65x33_tiles/wall_left.png",
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
            "images/attack_towers/tempStructures/temp_Attack_Structure2_lev1.png"};

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
        when(terrain.tileToWorldPosition(new GridPoint2(0, 0))).thenReturn(new Vector2(0, 0));
        when(terrain.tileToWorldPosition(new GridPoint2(1, 1))).thenReturn(new Vector2(1, 1));
        when(terrain.tileToWorldPosition(new GridPoint2(2, 2))).thenReturn(new Vector2(2, 2));
        when(terrain.tileToWorldPosition(new GridPoint2(3, 3))).thenReturn(new Vector2(3, 3));
        when(terrain.tileToWorldPosition(new GridPoint2(4, 4))).thenReturn(new Vector2(4, 4));
        when(terrain.tileToWorldPosition(new GridPoint2(5, 5))).thenReturn(new Vector2(5, 5));
        when(terrain.worldToTilePosition(0, 0)).thenReturn(new GridPoint2(0,0));
        when(terrain.worldToTilePosition(1, 1)).thenReturn(new GridPoint2(1,1));
        when(terrain.worldToTilePosition(2, 2)).thenReturn(new GridPoint2(2,2));
        when(terrain.worldToTilePosition(3, 3)).thenReturn(new GridPoint2(3,3));
        when(terrain.worldToTilePosition(4, 4)).thenReturn(new GridPoint2(4,4));

//        float x = any(float.class);
//        float y = any(float.class);
//        when(terrain.worldToTilePosition(x, y)).thenReturn(new GridPoint2((int) x, (int) y));

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

    @Test
    void shouldCreateWallEntity() {
        Entity wall = StructureFactory.createWall("wall", false, 0);
        assertTrue(wall != null);
    }

    @Test
    void shouldCreateTempWallEntity() {
    }
}