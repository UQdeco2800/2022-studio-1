package com.deco2800.game.files;

import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.Vector2;
import com.deco2800.game.areas.terrain.TerrainComponent;
import com.deco2800.game.components.Environmental.EnvironmentalComponent;
import com.deco2800.game.entities.*;
import com.deco2800.game.entities.factories.ObstacleFactory;
import com.deco2800.game.entities.factories.StructureFactory;
import com.deco2800.game.extensions.GameExtension;
import com.deco2800.game.physics.PhysicsService;
import com.deco2800.game.rendering.RenderService;
import com.deco2800.game.rendering.TextureRenderComponent;
import com.deco2800.game.services.*;
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
            "images/attack_towers/lv1GuardianLeft.png"
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
        Entity terrainEntity = new Entity().addComponent(terrain);
        ServiceLocator.getEntityService().registerNamed("terrain", terrainEntity);

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
}
//
//    @Test
//    void testSaveGameMultipleStaticEntityGeneric() {
//        deleteFiles();
//        setUpServices();
//
//        Entity test =  ObstacleFactory.createRock();
//
//        ServiceLocator.getUGSService().setEntity(new GridPoint2(10,10),test, "test");
//
//        SaveGame.saveGameState();
//        assertTrue(Files.exists(Path.of(filePath + "Environmental.json")));
//        assertTrue(Files.exists(Path.of(filePath + "Structures.json")));
//        assertTrue(Files.exists(Path.of(filePath + "GameData.json")));
//
//        ArrayList load = FileLoader.readClass(ArrayList.class,filePath + "Environmental.json");
//        assertEquals(load.size(), 1);
//    }
//
//
////    @Test
////    void testSaveGameSingleStaticEntityStructure() {
////        deleteFiles();
////        setUpServices();
////
////        Entity test = StructureFactory.createTower1(1);
////
////        ServiceLocator.getStructureService().registerNamed(test.getName(), test);
////
////        SaveGame.saveGameState();
////        assertTrue(Files.exists(Path.of(filePath + "Environmental.json")));
////        assertTrue(Files.exists(Path.of(filePath + "Structures.json")));
////        assertTrue(Files.exists(Path.of(filePath + "GameData.json")));
////
////        ArrayList load = FileLoader.readClass(ArrayList.class,filePath + "Structures.json");
////        assertEquals(1, load.size());
////    }
//
////    @Test
////    void testSaveGameMultipleStaticEntityStructure() {
////        deleteFiles();
////        setUpServices();
////
////        Entity test = StructureFactory.createTower1(1);
////        test.setName("a");
////        Entity test2 = StructureFactory.createTower2(1);
////        test.setName("b");
////        Entity test3 = StructureFactory.createTower3(2);
////        test3.setName("c");
////        Entity test4 = StructureFactory.createWall();
////        test4.setName("d");
////
////        ServiceLocator.getStructureService().registerNamed(test.getName(), test);
////        ServiceLocator.getStructureService().registerNamed(test2.getName(), test2);
////        ServiceLocator.getStructureService().registerNamed(test3.getName(), test3);
////        ServiceLocator.getStructureService().registerNamed(test4.getName(), test4);
////
////        SaveGame.saveGameState();
////        assertTrue(Files.exists(Path.of(filePath + "Environmental.json")));
////        assertTrue(Files.exists(Path.of(filePath + "Structures.json")));
////        assertTrue(Files.exists(Path.of(filePath + "GameData.json")));
////
////        ArrayList load = FileLoader.readClass(ArrayList.class,filePath + "Structures.json");
////        assertEquals(4, load.size());
////    }
//
//
//    @Test
//    void testSaveGameSingleStaticEntityEnvironment() {
//        deleteFiles();
//        setUpServices();
//
//        Entity test = ObstacleFactory.createRock();
//        ServiceLocator.getEntityService().addEntity(test);
//
//        SaveGame.saveGameState();
//        assertTrue(Files.exists(Path.of(filePath + "Environmental.json")));
//        assertTrue(Files.exists(Path.of(filePath + "Structures.json")));
//        assertTrue(Files.exists(Path.of(filePath + "GameData.json")));
//
//        ArrayList load = FileLoader.readClass(ArrayList.class,filePath + "Environmental.json");
//        assertEquals(1, load.size());
//    }
//
//    @Test
//    void testSaveGameMultipleStaticEntityEnvironment() {
//        deleteFiles();
//        setUpServices();
//
//        Entity rock = ObstacleFactory.createRock();
//        rock.setPosition(new Vector2(1,1));
//        Entity tree = ObstacleFactory.createTree();
//        tree.setPosition(new Vector2(2,2));
//        Entity shell = ObstacleFactory.createShell();
//        shell.setPosition(new Vector2(3,3));
//        Entity shipFront = ObstacleFactory.createShipwreckFront();
//        shipFront.setPosition(new Vector2(4,4));
//        Entity shipEnd = ObstacleFactory.createShipwreckBack();
//        shipEnd.setPosition(new Vector2(5,5));
//        Entity pillar = ObstacleFactory.createPillar();
//        pillar.setPosition(new Vector2(6,6));
//        Entity chalace = ObstacleFactory.createAoeSpeedArtefact();
//        chalace.setPosition(new Vector2(7,7));
//        Entity vines = ObstacleFactory.createVine();
//        vines.setPosition(new Vector2(8,8));
//
//        ServiceLocator.getEntityService().addEntity(rock);
//        ServiceLocator.getEntityService().addEntity(tree);
//        ServiceLocator.getEntityService().addEntity(shell);
//        ServiceLocator.getEntityService().addEntity(shipFront);
//        ServiceLocator.getEntityService().addEntity(shipEnd);
//        ServiceLocator.getEntityService().addEntity(pillar);
//        ServiceLocator.getEntityService().addEntity(chalace);
//        ServiceLocator.getEntityService().addEntity(vines);
//
//        SaveGame.saveGameState();
//        assertTrue(Files.exists(Path.of(filePath + "Environmental.json")));
//        assertTrue(Files.exists(Path.of(filePath + "Structures.json")));
//        assertTrue(Files.exists(Path.of(filePath + "GameData.json")));
//
//        ArrayList load = FileLoader.readClass(ArrayList.class,filePath + "Environmental.json");
//        assertEquals(8, load.size());
//    }
//
////    @Test
////    void testSaveGameFollowedByLoadGameStructure() {
////        deleteFiles();
////        setUpServices();
////
////        Entity test = StructureFactory.createTower1(1);
////        test.setPosition(new Vector2(5,5));
////
////        ServiceLocator.getStructureService().addEntity(test);
////
////        SaveGame.saveGameState();
////
////        ServiceLocator.getStructureService().unregister(test);
////
////        assertTrue(Files.exists(Path.of(filePath + "Environmental.json")));
////        assertTrue(Files.exists(Path.of(filePath + "Structures.json")));
////        assertTrue(Files.exists(Path.of(filePath + "GameData.json")));
////
////        SaveGame.loadGameState();
////
////       assertEquals(1, ServiceLocator.getStructureService().getEntities().size());
////    }
//
////    @Test
////    void testSaveGameFollowedByMultipleStaticLoadGameStructure() {
////        deleteFiles();
////        setUpServices();
////
////        Entity test = StructureFactory.createTower1(1);
////        test.setPosition(new Vector2(5,5));
////        test.setName("a");
////        Entity test2 = StructureFactory.createTower2(1);
////        test2.setPosition(new Vector2(6,6));
////        test2.setName("b");
////        Entity test3 = StructureFactory.createTower3(1);
////        test3.setPosition(new Vector2(7,7));
////        test3.setName("c");
////        Entity test4 = StructureFactory.createWall();
////        test4.setPosition(new Vector2(8,8));
////        test4.setName("d");
////
////        ServiceLocator.getStructureService().addEntity(test);
////        ServiceLocator.getStructureService().addEntity(test2);
////        ServiceLocator.getStructureService().addEntity(test3);
////        ServiceLocator.getStructureService().addEntity(test4);
////
////        SaveGame.saveGameState();
////
////        ServiceLocator.getStructureService().unregister(test);
////        ServiceLocator.getStructureService().unregister(test2);
////        ServiceLocator.getStructureService().unregister(test3);
////        ServiceLocator.getStructureService().unregister(test4);
////
////        assertTrue(Files.exists(Path.of(filePath + "Environmental.json")));
////        assertTrue(Files.exists(Path.of(filePath + "Structures.json")));
////        assertTrue(Files.exists(Path.of(filePath + "GameData.json")));
////
////        SaveGame.loadGameState();
////        System.out.println(ServiceLocator.getStructureService().getEntities());
////        assertEquals(4, ServiceLocator.getStructureService().getEntities().size());
////    }
//
//
////    @Test
////    void testSaveGameFollowedByMultipleStaticLoadGameStructureALL() {
////        deleteFiles();
////        setUpServices();
////
////        Entity test = StructureFactory.createTower1(1);
////        test.setPosition(new Vector2(5,5));
////        test.setName("a");
////
////        Entity test1a = StructureFactory.createTower1(2);
////        test1a.setPosition(new Vector2(5,5));
////        test1a.setName("aa");
////
////        Entity test1b = StructureFactory.createTower1(3);
////        test1b.setPosition(new Vector2(5,5));
////        test1b.setName("aaa");
////
////        Entity test2 = StructureFactory.createTower2(1);
////        test2.setPosition(new Vector2(6,6));
////        test2.setName("b");
////
////        Entity test2a = StructureFactory.createTower2(2);
////        test2a.setPosition(new Vector2(6,6));
////        test2a.setName("bb");
////
////        Entity test2b = StructureFactory.createTower2(3);
////        test2b.setPosition(new Vector2(6,6));
////        test2b.setName("bbb");
////
////
////        Entity test3 = StructureFactory.createTower3(1);
////        test3.setPosition(new Vector2(7,7));
////        test3.setName("c");
////
////        Entity test3a = StructureFactory.createTower3(2);
////        test3a.setPosition(new Vector2(7,7));
////        test3a.setName("cc");
////
////        Entity test3b = StructureFactory.createTower3(3);
////        test3b.setPosition(new Vector2(7,7));
////        test3b.setName("cc");
////
////
////        Entity test4 = StructureFactory.createWall();
////        test4.setPosition(new Vector2(8,8));
////        test4.setName("d");
////
////        Entity test5 = StructureFactory.createTrap();
////        test5.setPosition(new Vector2(8,8));
////        test5.setName("d");
////
////        ServiceLocator.getStructureService().addEntity(test);
////        ServiceLocator.getStructureService().addEntity(test1a);
////        ServiceLocator.getStructureService().addEntity(test1b);
////
////        ServiceLocator.getStructureService().addEntity(test2);
////        ServiceLocator.getStructureService().addEntity(test2a);
////        ServiceLocator.getStructureService().addEntity(test2b);
////
////        ServiceLocator.getStructureService().addEntity(test3);
////        ServiceLocator.getStructureService().addEntity(test3a);
////        ServiceLocator.getStructureService().addEntity(test3b);
////
////        ServiceLocator.getStructureService().addEntity(test4);
////        ServiceLocator.getStructureService().addEntity(test5);
////
////        SaveGame.saveGameState();
////
////        ServiceLocator.getStructureService().unregister(test);
////        ServiceLocator.getStructureService().unregister(test1a);
////        ServiceLocator.getStructureService().unregister(test1b);
////
////        ServiceLocator.getStructureService().unregister(test2);
////        ServiceLocator.getStructureService().unregister(test2a);
////        ServiceLocator.getStructureService().unregister(test2b);
////
////        ServiceLocator.getStructureService().unregister(test3);
////        ServiceLocator.getStructureService().unregister(test3a);
////        ServiceLocator.getStructureService().unregister(test3b);
////
////        ServiceLocator.getStructureService().unregister(test4);
////        ServiceLocator.getStructureService().unregister(test5);
////
////        assertTrue(Files.exists(Path.of(filePath + "Environmental.json")));
////        assertTrue(Files.exists(Path.of(filePath + "Structures.json")));
////        assertTrue(Files.exists(Path.of(filePath + "GameData.json")));
////
////        SaveGame.loadGameState();
////        System.out.println(ServiceLocator.getStructureService().getEntities());
////        assertEquals(4, ServiceLocator.getStructureService().getEntities().size());
////    }
//
////    @Test
////    void testSaveGameFollowedByMultipleStaticLoadGameStructurePositionsRemain() {
////        deleteFiles();
////        setUpServices();
////
////        Entity test = StructureFactory.createTower1(1);
////        test.setPosition(new Vector2(5,5));
////        test.setName("a");
////        Vector2 test_position = test.getPosition();
////        Entity test2 = StructureFactory.createTower2(1);
////        test2.setPosition(new Vector2(6,6));
////        test2.setName("b");
////        Vector2 test2_position = test2.getPosition();
////        Entity test3 = StructureFactory.createTower3(1);
////        test3.setPosition(new Vector2(7,7));
////        test3.setName("c");
////        Vector2 test3_position = test3.getPosition();
////        Entity test4 = StructureFactory.createWall();
////        test4.setPosition(new Vector2(8,8));
////        test4.setName("d");
////        Vector2 test4_position = test4.getPosition();
////
////        ServiceLocator.getStructureService().registerNamed(test.getName(),test);
////        ServiceLocator.getStructureService().registerNamed(test2.getName(),test2);
////        ServiceLocator.getStructureService().registerNamed(test3.getName(),test3);
////        ServiceLocator.getStructureService().registerNamed(test4.getName(),test4);
////
////        SaveGame.saveGameState();
////
////        assertTrue(Files.exists(Path.of(filePath + "Environmental.json")));
////        assertTrue(Files.exists(Path.of(filePath + "Structures.json")));
////        assertTrue(Files.exists(Path.of(filePath + "GameData.json")));
////
////        SaveGame.loadGameState();
////
////        assertEquals( ServiceLocator.getStructureService().getNamedEntity("a").getPosition(), test_position);
////        assertEquals( ServiceLocator.getStructureService().getNamedEntity("b").getPosition(), test2_position);
////        assertEquals( ServiceLocator.getStructureService().getNamedEntity("c").getPosition(), test3_position);
////        assertEquals( ServiceLocator.getStructureService().getNamedEntity("d").getPosition(), test4_position);
////
////    }
//
//
//    @Test
//    void testSaveGameFollowedBySingleEnvironmentalObject() {
//        deleteFiles();
//        setUpServices();
//
//        Entity test = ObstacleFactory.createRock();
//        test.setPosition(new Vector2(5,5));
//        test.setName("a");
//
//        ServiceLocator.getEntityService().register(test);
//        SaveGame.saveGameState();
//        ServiceLocator.getEntityService().unregister(test);
//
//        assertTrue(Files.exists(Path.of(filePath + "Environmental.json")));
//        assertTrue(Files.exists(Path.of(filePath + "Structures.json")));
//        assertTrue(Files.exists(Path.of(filePath + "GameData.json")));
//
//        SaveGame.loadGameState();
//
//
//        assertEquals( 2, ServiceLocator.getEntityService().getAllNamedEntities().size());
//    }
//
//    @Test
//    void testSaveGameFollowedByMultipleEnvironmentalObject() {
//        deleteFiles();
//        setUpServices();
//
//        Entity test = ObstacleFactory.createRock();
//        test.setPosition(new Vector2(5,5));
//        test.setName("a");
//        Entity test2 = ObstacleFactory.createTree();
//        test2.setPosition(new Vector2(6,6));
//        test2.setName("b");
//        Entity test3 = ObstacleFactory.createPillar();
//        test3.setPosition(new Vector2(7,7));
//        test3.setName("c");
//
//        SaveGame.saveGameState();
//
//
//        assertTrue(Files.exists(Path.of(filePath + "Environmental.json")));
//        assertTrue(Files.exists(Path.of(filePath + "Structures.json")));
//        assertTrue(Files.exists(Path.of(filePath + "GameData.json")));
//
//        SaveGame.loadGameState();
//        System.out.println(ServiceLocator.getEntityService().getAllNamedEntities());
//
//        assertEquals( 4, ServiceLocator.getEntityService().getAllNamedEntities().size());
//    }
//}
