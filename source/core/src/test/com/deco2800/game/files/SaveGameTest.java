package com.deco2800.game.files;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.Vector2;
import com.deco2800.game.areas.terrain.TerrainComponent;
import com.deco2800.game.components.CameraComponent;
import com.deco2800.game.components.Environmental.EnvironmentalComponent;
import com.deco2800.game.components.camera.CameraActions;
import com.deco2800.game.entities.*;
import com.deco2800.game.entities.factories.ObstacleFactory;
import com.deco2800.game.entities.factories.StructureFactory;
import com.deco2800.game.events.EventHandler;
import com.deco2800.game.extensions.GameExtension;
import com.deco2800.game.physics.PhysicsService;
import com.deco2800.game.rendering.RenderService;
import com.deco2800.game.rendering.TextureRenderComponent;
import com.deco2800.game.services.*;
import com.deco2800.game.services.configs.DayNightCycleConfig;
import com.deco2800.game.utils.RenderUtil;
import org.hamcrest.core.IsInstanceOf;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(GameExtension.class)
public class SaveGameTest {

    String filePath = "Saves/";

    private static final String[] forestTextures = {
            "images/Centaur_Back_left.png",
            "images/Centaur_Back_right.png",
            "images/Centaur_left.png",
            "images/Centaur_right.png",
            "images/landscape_objects/leftPalmTree.png",
            "images/landscape_objects/rightPalmTree.png",
            "images/landscape_objects/groupPalmTrees.png",
            "images/landscape_objects/almond-tree-60x62.png",
            "images/landscape_objects/fig-tree-60x62.png",
            "images/landscape_objects/vines.png",
            "images/landscape_objects/cypress-tree-60x100.png",
            "images/landscape_objects/geyser.png",
            "images/boss_enemy_angle1.png",
            "images/landscape_objects/billboard.png",
            "images/landscape_objects/chalice.png",
            "images/landscape_objects/pillar.png",
            "images/landscape_objects/wooden-fence-60x60.png",
            "images/65x33_tiles/shell.png",
            "images/pirate_crab_NE.png",
            "images/pirate_crab_NW.png",
            "images/pirate_crab_SE.png",
            "images/pirate_crab_SW.png",
            "images/crystal.png",
            "images/crystal_level2.png",
            "images/crystal_level3.png",
            "images/65x33_tiles/sand.png",
            "images/65x33_tiles/sand_night.png",
            "images/65x33_tiles/seaweed_1.png",
            "images/65x33_tiles/seaweed_1_night.png",
            "images/65x33_tiles/seaweed_2.png",
            "images/65x33_tiles/seaweed_2_night.png",
            "images/65x33_tiles/seaweed_3.png",
            "images/65x33_tiles/seaweed_3_night.png",
            "images/65x33_tiles/shorelineBottom.png",
            "images/65x33_tiles/shorelineTop.png",
            "images/65x33_tiles/shorelineBottomRight.png",
            "images/65x33_tiles/shorelineBottomLeft.png",
            "images/65x33_tiles/shorelineTopRight.png",
            "images/65x33_tiles/shorelineTopLeft.png",
            "images/65x33_tiles/shorelineLeft.png",
            "images/65x33_tiles/shorelineRight.png",
            "images/65x33_tiles/shorelineBottom_night.png",
            "images/65x33_tiles/shorelineTop_night.png",
            "images/65x33_tiles/shorelineBottomRight_night.png",
            "images/65x33_tiles/shorelineBottomLeft_night.png",
            "images/65x33_tiles/shorelineTopRight_night.png",
            "images/65x33_tiles/shorelineTopLeft_night.png",
            "images/65x33_tiles/shorelineLeft_night.png",
            "images/65x33_tiles/shorelineRight_night.png",
            "images/65x33_tiles/water0.png",
            "images/65x33_tiles/water1.png",
            "images/65x33_tiles/water2.png",
            "images/65x33_tiles/water3.png",
            "images/65x33_tiles/water_night0.png",
            "images/65x33_tiles/water_night1.png",
            "images/65x33_tiles/water_night2.png",
            "images/65x33_tiles/water_night3.png",
            "images/65x33_tiles/invalidTile.png",
            "images/65x33_tiles/validTile.png",
            "images/seastack1.png",
            "images/seastack2.png",
            "images/Eel_Bright_SW.png",
            "images/Eel_Bright_NE.png",
            "images/Eel_Bright_NW.png",
            "images/Eel_Bright_SW.png",
            "images/shipRack.png",
            "images/shipRackFront.png",
            "images/shipWreckBack.png",
            "images/shipWreckFront.png",
            "images/ElectricEel.png",
            "images/starfish.png",
            "images/NpcPlaceholder.png",
            "images/NPC convo.png",
            "images/npc1.png",
            "images/npcs/NPC-V2.2.png",
            "images/npcs/NPC-V2.1.png",
            "images/guardianLegacy1left.png",
            "images/guardianLegacy1right.png",
            "images/cornerWall1.png",
            "images/cornerWall2.png",
            "images/cornerWall3.png",
            "images/cornerWall4.png",
            "images/wallRight.png",
            "images/wallLeft.png",
            "images/attack_towers/lv1GuardianLeft.png",
            "images/TOWER3I.png"
    };

    void deleteFiles() {
        try {
            Files.deleteIfExists(Path.of(filePath + "Environmental.json"));
            Files.deleteIfExists(Path.of(filePath + "GameData.json"));
            Files.deleteIfExists(Path.of(filePath + "Structures.json"));
            Files.deleteIfExists(Path.of(filePath + "Player.json"));
            Files.deleteIfExists(Path.of(filePath + "Crystal.json"));
        } catch (IOException ignored) {

        }
    }

    void setUpServices() {
        EntityService entityService = new EntityService();
        StructureService structureService = new StructureService();
        DayNightCycleService dayNightCycleService = new DayNightCycleService();
        dayNightCycleService.config = new DayNightCycleConfig();
        dayNightCycleService.config.dawnLength = 1;
        dayNightCycleService.config.dayLength = 1;
        dayNightCycleService.config.duskLength = 1;
        dayNightCycleService.config.nightLength = 1;
        dayNightCycleService.config.maxDays = 1;
        dayNightCycleService.timer = mock(GameTime.class);
        when(dayNightCycleService.timer.getTime()).thenReturn(10000L);
        dayNightCycleService.events = new EventHandler();

        ResourceService resourceService = new ResourceService();
        RenderService renderService = new RenderService();

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

        resourceService.loadTextures(forestTextures);

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
    void testSaveGameEmpty() {
        deleteFiles();
        setUpServices();

        SaveGame.saveGameState();
        assertTrue(Files.exists(Path.of(filePath + "Environmental.json")));
        assertTrue(Files.exists(Path.of(filePath + "Structures.json")));
        assertTrue(Files.exists(Path.of(filePath + "GameData.json")));
    }

    @Test
    void testSaveGameSingleStaticEntityGeneric() {
        deleteFiles();
        setUpServices();

        Entity test = ObstacleFactory.createRock();
        test.setPosition(new Vector2(0, 0));
        Tile tile = new Tile();
        ServiceLocator.getUGSService().add(new GridPoint2(0, 0), tile);

        ServiceLocator.getUGSService().setEntity(new GridPoint2(0, 0), test, "test");

        SaveGame.saveGameState();
        assertTrue(Files.exists(Path.of(filePath + "Environmental.json")));
        assertTrue(Files.exists(Path.of(filePath + "Structures.json")));
        assertTrue(Files.exists(Path.of(filePath + "GameData.json")));

        ArrayList load = FileLoader.readClass(ArrayList.class, filePath + "Environmental.json");
        assertEquals(load.size(), 1);
    }


    @Test
    void testSaveGameMultipleStaticEntityGeneric() {
        deleteFiles();
        setUpServices();

        Entity test = ObstacleFactory.createRock();
        test.setPosition(new Vector2(0, 0));

        Entity test2 = ObstacleFactory.createTree();
        test2.setPosition(new Vector2(0, 0));

        Tile tile = new Tile();
        ServiceLocator.getUGSService().add(new GridPoint2(0, 0), tile);
        ServiceLocator.getUGSService().setEntity(new GridPoint2(0, 0), test, "test");

        Tile tile2 = new Tile();
        ServiceLocator.getUGSService().add(new GridPoint2(1, 1), tile2);
        ServiceLocator.getUGSService().setEntity(new GridPoint2(1, 1), test2, "test2");


        SaveGame.saveGameState();
        assertTrue(Files.exists(Path.of(filePath + "Environmental.json")));
        assertTrue(Files.exists(Path.of(filePath + "Structures.json")));
        assertTrue(Files.exists(Path.of(filePath + "GameData.json")));

        ArrayList load = FileLoader.readClass(ArrayList.class, filePath + "Environmental.json");
        assertEquals(load.size(), 2);
    }


    @Test
    void testSaveGameSingleStaticEntityStructure() {
        deleteFiles();
        setUpServices();

        Entity test = StructureFactory.createTower1(1, "test", false);
        test.setPosition(0,0);
        Tile tile = new Tile();

        ServiceLocator.getUGSService().add(new GridPoint2(0, 0), tile);
        ServiceLocator.getUGSService().setEntity(new GridPoint2(0, 0), test, "test");

        ServiceLocator.getUGSService().addStructure(test);

        SaveGame.saveGameState();
        assertTrue(Files.exists(Path.of(filePath + "Environmental.json")));
        assertTrue(Files.exists(Path.of(filePath + "Structures.json")));
        assertTrue(Files.exists(Path.of(filePath + "GameData.json")));

        ArrayList load = FileLoader.readClass(ArrayList.class,filePath + "Structures.json");
        assertEquals(1, load.size());
    }

    @Test
    void testSaveGameMultipleStaticEntityStructure() {
        deleteFiles();
        setUpServices();

        Entity test = StructureFactory.createTower1(1, "test", false);
        test.setPosition(0,0);
        Tile tile = new Tile();

        Entity test2 = StructureFactory.createWall("test", false, 0);
        test2.setPosition(1,1);
        Tile tile2 = new Tile();

        Entity test1 = StructureFactory.createTower3(1, "test", false);;
        test1.setPosition(2,2);
        Tile tile1 = new Tile();

        ServiceLocator.getUGSService().add(new GridPoint2(0, 0), tile);
        ServiceLocator.getUGSService().setEntity(new GridPoint2(0, 0), test, "test");
        ServiceLocator.getUGSService().addStructure(test);

        ServiceLocator.getUGSService().add(new GridPoint2(1, 1), tile2);
        ServiceLocator.getUGSService().setEntity(new GridPoint2(1, 1), test2, "test1");
        ServiceLocator.getUGSService().addStructure(test2);

        ServiceLocator.getUGSService().add(new GridPoint2(2, 2), tile1);
        ServiceLocator.getUGSService().setEntity(new GridPoint2(2,2), test1, "test2");
        ServiceLocator.getUGSService().addStructure(test2);

        SaveGame.saveGameState();
        assertTrue(Files.exists(Path.of(filePath + "Environmental.json")));
        assertTrue(Files.exists(Path.of(filePath + "Structures.json")));
        assertTrue(Files.exists(Path.of(filePath + "GameData.json")));

        ArrayList load = FileLoader.readClass(ArrayList.class,filePath + "Structures.json");
        assertEquals(3, load.size());
    }


    @Test
    void testSaveGameFollowedByLoadGameStructure() {
        deleteFiles();
        setUpServices();

        Entity test = StructureFactory.createTower1(1, "test", false);
        test.setPosition(0,0);
        Tile tile = new Tile();

        ServiceLocator.getUGSService().add(new GridPoint2(0, 0), tile);
        ServiceLocator.getUGSService().setEntity(new GridPoint2(0, 0), test, "test");
        ServiceLocator.getUGSService().addStructure(test);

        SaveGame.saveGameState();

        assertTrue(Files.exists(Path.of(filePath + "Environmental.json")));
        assertTrue(Files.exists(Path.of(filePath + "Structures.json")));
        assertTrue(Files.exists(Path.of(filePath + "GameData.json")));

        SaveGame.loadGameState();

       assertEquals(1, ServiceLocator.getUGSService().getStructures().size());
    }

    @Test
    void testSaveGameFollowedByMultipleStaticLoadGameStructure() {
        deleteFiles();
        setUpServices();

        deleteFiles();
        setUpServices();


        Entity test = StructureFactory.createTower1(1, "test", false);
        test.setPosition(0,0);
        Tile tile = new Tile();

        Entity test2 = StructureFactory.createWall("test1", false, 0);
        test2.setPosition(1,1);
        Tile tile2 = new Tile();

        Entity test1 = StructureFactory.createTower3(1, "test2", false);;
        test1.setPosition(2,2);
        Tile tile1 = new Tile();

        ServiceLocator.getUGSService().add(new GridPoint2(0, 0), tile);
        ServiceLocator.getUGSService().setEntity(new GridPoint2(0, 0), test, "test");
        ServiceLocator.getUGSService().addStructure(test);

        ServiceLocator.getUGSService().add(new GridPoint2(1, 1), tile2);
        ServiceLocator.getUGSService().setEntity(new GridPoint2(1, 1), test2, "test1");
        ServiceLocator.getUGSService().addStructure(test2);

        ServiceLocator.getUGSService().add(new GridPoint2(2, 2), tile1);
        ServiceLocator.getUGSService().setEntity(new GridPoint2(2,2), test1, "test2");
        ServiceLocator.getUGSService().addStructure(test2);

        SaveGame.saveGameState();

        assertTrue(Files.exists(Path.of(filePath + "Environmental.json")));
        assertTrue(Files.exists(Path.of(filePath + "Structures.json")));
        assertTrue(Files.exists(Path.of(filePath + "GameData.json")));

        SaveGame.loadGameState();

        assertEquals(3, ServiceLocator.getUGSService().getStructures().size());
        assertEquals(ServiceLocator.getUGSService().getEntity(new GridPoint2(2,2)).getName(), "test2");
        assertEquals(ServiceLocator.getUGSService().getEntity(new GridPoint2(1,1)).getName(), "test1");
        assertEquals(ServiceLocator.getUGSService().getEntity(new GridPoint2(0,0)).getName(), "test");
    }

    @Test
    void testSaveGameFollowedBySingleEnvironmentalObject() {
        deleteFiles();
        setUpServices();

        Entity test = ObstacleFactory.createRock();
        test.setPosition(new Vector2(0, 0));
        Tile tile = new Tile();
        test.setName("test");
        ServiceLocator.getUGSService().add(new GridPoint2(0, 0), tile);
        ServiceLocator.getUGSService().setEntity(new GridPoint2(0, 0), test, "test");

        SaveGame.saveGameState();
        assertTrue(Files.exists(Path.of(filePath + "Environmental.json")));
        assertTrue(Files.exists(Path.of(filePath + "Structures.json")));
        assertTrue(Files.exists(Path.of(filePath + "GameData.json")));

        SaveGame.loadGameState();
        assertEquals("test", ServiceLocator.getUGSService().getEntity(new GridPoint2(0,0)).getName());
    }

    @Test
    void testSaveGameFollowedByMultipleEnvironmentalObject() {

        deleteFiles();
        setUpServices();

        Entity test = ObstacleFactory.createRock();
        test.setPosition(new Vector2(0, 0));
        test.setName("rock1");

        Entity test2 = ObstacleFactory.createTree();
        test2.setPosition(new Vector2(1, 1));
        test2.setName("tree1");

        Tile tile = new Tile();
        ServiceLocator.getUGSService().add(new GridPoint2(0, 0), tile);
        ServiceLocator.getUGSService().setEntity(new GridPoint2(0, 0), test, "test");

        Tile tile2 = new Tile();
        ServiceLocator.getUGSService().add(new GridPoint2(1, 1), tile2);
        ServiceLocator.getUGSService().setEntity(new GridPoint2(1, 1), test2, "test2");

        SaveGame.saveGameState();
        assertTrue(Files.exists(Path.of(filePath + "Environmental.json")));
        assertTrue(Files.exists(Path.of(filePath + "Structures.json")));
        assertTrue(Files.exists(Path.of(filePath + "GameData.json")));

        SaveGame.loadGameState();
        assertEquals("rock1", ServiceLocator.getUGSService().getEntity(new GridPoint2(0,0)).getName());
        assertEquals("tree1", ServiceLocator.getUGSService().getEntity(new GridPoint2(1,1)).getName());

    }
}