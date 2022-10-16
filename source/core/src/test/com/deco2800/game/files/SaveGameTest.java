package com.deco2800.game.files;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.deco2800.game.areas.terrain.TerrainComponent;
import com.deco2800.game.components.*;
import com.deco2800.game.components.Environmental.EnvironmentalComponent;
import com.deco2800.game.components.camera.CameraActions;
import com.deco2800.game.entities.*;
import com.deco2800.game.entities.factories.ObstacleFactory;
import com.deco2800.game.entities.factories.StructureFactory;
import com.deco2800.game.events.EventHandler;
import com.deco2800.game.entities.configs.CrystalConfig;
import com.deco2800.game.entities.factories.*;
import com.deco2800.game.extensions.GameExtension;
import com.deco2800.game.input.InputService;
import com.deco2800.game.physics.PhysicsLayer;
import com.deco2800.game.physics.PhysicsService;
import com.deco2800.game.physics.components.ColliderComponent;
import com.deco2800.game.physics.components.HitboxComponent;
import com.deco2800.game.physics.components.PhysicsComponent;
import com.deco2800.game.rendering.RenderService;
import com.deco2800.game.rendering.TextureRenderComponent;
import com.deco2800.game.services.*;
import com.deco2800.game.services.configs.DayNightCycleConfig;
import com.deco2800.game.utils.RenderUtil;
import org.hamcrest.core.IsInstanceOf;
import org.junit.jupiter.api.BeforeEach;
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
    Entity testPlayer;
    Entity testCrystal;
    private static final CrystalConfig crystalStats = FileLoader.readClass(CrystalConfig.class, "configs/crystal.json");
    Tile testTile;

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
            "images/attack_towers/tow1_1_l.png",
            "images/attack_towers/tow1_1_r.png",
            "images/attack_towers/tow1_2_l.png",
            "images/attack_towers/tow1_2_r.png",
            "images/attack_towers/tow1_3_l.png",
            "images/attack_towers/tow1_3_r.png",
            "images/attack_towers/tow2_2_l.gif",
            "images/TOWER3I.png"
    };

    private final String[] clockSprites = {
            "images/clock_sprites/clock_day1_1.png",
            "images/clock_sprites/clock_day1_2.png",
            "images/clock_sprites/clock_day1_3.png",
            "images/clock_sprites/clock_day1_4.png",
            "images/clock_sprites/clock_day1_5.png",
            "images/clock_sprites/clock_day1_6.png",
            "images/clock_sprites/clock_day1_7.png",
            "images/clock_sprites/clock_day1_8.png",
            "images/clock_sprites/clock_day2_1.png",
            "images/clock_sprites/clock_day2_2.png",
            "images/clock_sprites/clock_day2_3.png",
            "images/clock_sprites/clock_day2_4.png",
            "images/clock_sprites/clock_day2_5.png",
            "images/clock_sprites/clock_day2_6.png",
            "images/clock_sprites/clock_day2_7.png",
            "images/clock_sprites/clock_day2_8.png",
            "images/clock_sprites/clock_day3_1.png",
            "images/clock_sprites/clock_day3_2.png",
            "images/clock_sprites/clock_day3_3.png",
            "images/clock_sprites/clock_day3_4.png",
            "images/clock_sprites/clock_day3_5.png",
            "images/clock_sprites/clock_day3_6.png",
            "images/clock_sprites/clock_day3_7.png",
            "images/clock_sprites/clock_day3_8.png",
            "images/clock_sprites/clock_day4_1.png",
            "images/clock_sprites/clock_day4_2.png",
            "images/clock_sprites/clock_day4_3.png",
            "images/clock_sprites/clock_day4_4.png",
            "images/clock_sprites/clock_day4_5.png",
            "images/clock_sprites/clock_day4_6.png",
            "images/clock_sprites/clock_boss.png"
    };

    private static final String[] textureAtlases = {
            "images/anim_demo/main.atlas",
            "images/eel_animations/eel.atlas",
            "images/starfish_animation/starfish.atlas"
    };

    @BeforeEach
    void deleteFiles() {
        try {
            Files.deleteIfExists(Path.of(filePath + "Environmental.json"));
            Files.deleteIfExists(Path.of(filePath + "GameData.json"));
            Files.deleteIfExists(Path.of(filePath + "Structures.json"));
            Files.deleteIfExists(Path.of(filePath + "Player.json"));
            Files.deleteIfExists(Path.of(filePath + "Crystal.json"));
            Files.deleteIfExists(Path.of(filePath + "Enemies.json"));
        } catch (IOException ignored) {

        }
    }

    @BeforeEach
    void setUpServices() {
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

        resourceService.loadTextures(forestTextures);
        resourceService.loadTextures(clockSprites);
        resourceService.loadTextureAtlases(textureAtlases);

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

    void setUpEnemyTesting() {
        testTile = new Tile();
        testCrystal = new Entity()
                .addComponent(new TextureRenderComponent("images/crystal.png"))
                .addComponent(new TouchAttackComponent(PhysicsLayer.PLAYER, 1.5f))
                .addComponent(new CombatStatsComponent(crystalStats.health, crystalStats.baseAttack,
                        crystalStats.defense, crystalStats.level, 1000));
        ServiceLocator.getUGSService().add(new GridPoint2(1, 1), testTile);
        ServiceLocator.getUGSService().setEntity(new GridPoint2(1, 1), testCrystal, "crystal");
        testPlayer = mock(Entity.class);
        ServiceLocator.getUGSService().add(new GridPoint2(0, 0), testTile);
        ServiceLocator.getUGSService().setEntity(new GridPoint2(0, 0), testPlayer, "player");
    }

    @Test
    void testSaveGameEmpty() {
        SaveGame.saveGameState();
        assertTrue(Files.exists(Path.of(filePath + "Environmental.json")));
        assertTrue(Files.exists(Path.of(filePath + "Structures.json")));
        assertTrue(Files.exists(Path.of(filePath + "GameData.json")));
        assertTrue(Files.exists(Path.of(filePath + "Enemies.json")));
    }

    @Test
    void testSaveGameSingleStaticEntityGeneric() {
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
        Entity test = StructureFactory.createTower1(1, "test", false, 0);
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
        Entity test = StructureFactory.createTower1(1, "test", false, 0);
        test.setPosition(0,0);
        Tile tile = new Tile();

        Entity test2 = StructureFactory.createWall("test", false, 0);
        test2.setPosition(1,1);
        Tile tile2 = new Tile();

        Entity test1 = StructureFactory.createTower3(1, "test", false);
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
        Entity test = StructureFactory.createTower1(1, "test", false, 0);
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

        Entity test = StructureFactory.createTower1(1, "test", false, 0);
        test.setPosition(0,0);
        Tile tile = new Tile();

        Entity test2 = StructureFactory.createWall("test1", false, 0);
        test2.setPosition(1,1);
        Tile tile2 = new Tile();

        Entity test1 = StructureFactory.createTower3(1, "test2", false);
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


    @Test
    void testSaveGameSingleEnemy() {
        setUpEnemyTesting();

        Entity testEnemy = NPCFactory.createPirateCrabEnemy(testPlayer);
        testEnemy.setName("Mr. Crabs@" + testEnemy.getId());
        testEnemy.setPosition(2,2);

        ServiceLocator.getEntityService().registerNamed("testCrab",  testEnemy);
        ServiceLocator.getUGSService().add(new GridPoint2(2, 2), testTile);
        ServiceLocator.getUGSService().setEntity(new GridPoint2(2, 2), testEnemy, "testEnemy");

        SaveGame.saveGameState();
        assertTrue(Files.exists(Path.of(filePath + "Environmental.json")));
        assertTrue(Files.exists(Path.of(filePath + "Structures.json")));
        assertTrue(Files.exists(Path.of(filePath + "GameData.json")));
        assertTrue(Files.exists(Path.of(filePath + "Enemies.json")));

        ArrayList load = FileLoader.readClass(ArrayList.class,filePath + "Enemies.json");
        assertEquals(1, load.size());
    }


    @Test
    void testSaveGameMultipleEnemies() {
        setUpEnemyTesting();

        Entity testEnemy = NPCFactory.createPirateCrabEnemy(testPlayer);
        testEnemy.setName("Mr. Crabs@" + testEnemy.getId());
        testEnemy.setPosition(2,2);
        Entity testEnemy1 = NPCFactory.createElectricEelEnemy(testPlayer, testCrystal);
        testEnemy1.setName("Mr. Electricity@" + testEnemy1.getId());
        testEnemy1.setPosition(3,3);
        Entity testEnemy2 = NPCFactory.createStarFishEnemy(testPlayer, testCrystal);
        testEnemy2.setName("Mr. Starfish@" + testEnemy2.getId());
        testEnemy2.setPosition(4,4);

        ServiceLocator.getEntityService().registerNamed("testCrab",  testEnemy);
        ServiceLocator.getUGSService().add(new GridPoint2(2, 2), testTile);
        ServiceLocator.getUGSService().setEntity(new GridPoint2(2, 2), testEnemy, "testEnemy");

        ServiceLocator.getEntityService().registerNamed("testEel",  testEnemy1);
        ServiceLocator.getUGSService().add(new GridPoint2(3, 3), testTile);
        ServiceLocator.getUGSService().setEntity(new GridPoint2(3, 3), testEnemy, "testEnemy1");

        //Starfish already registered in NPCFactory!
        //ServiceLocator.getEntityService().registerNamed("testStarfish",  testEnemy2);
        ServiceLocator.getUGSService().add(new GridPoint2(4, 4), testTile);
        ServiceLocator.getUGSService().setEntity(new GridPoint2(4, 4), testEnemy, "testEnemy1");

        SaveGame.saveGameState();
        assertTrue(Files.exists(Path.of(filePath + "Environmental.json")));
        assertTrue(Files.exists(Path.of(filePath + "Structures.json")));
        assertTrue(Files.exists(Path.of(filePath + "GameData.json")));
        assertTrue(Files.exists(Path.of(filePath + "Enemies.json")));

        ArrayList load = FileLoader.readClass(ArrayList.class,filePath + "Enemies.json");
        assertEquals(3, load.size());
    }


    @Test
    void testSaveGameFollowedBySingleEnemyLoad() {
        setUpEnemyTesting();

        Entity testEnemy = NPCFactory.createPirateCrabEnemy(testPlayer);
        testEnemy.setName("Mr. Crabs");
        testEnemy.setPosition(2,2);

        ServiceLocator.getEntityService().registerNamed("testCrab",  testEnemy);
        ServiceLocator.getUGSService().add(new GridPoint2(2, 2), testTile);
        ServiceLocator.getUGSService().setEntity(new GridPoint2(2, 2), testEnemy, "testEnemy");

        SaveGame.saveGameState();
        assertTrue(Files.exists(Path.of(filePath + "Environmental.json")));
        assertTrue(Files.exists(Path.of(filePath + "Structures.json")));
        assertTrue(Files.exists(Path.of(filePath + "GameData.json")));
        assertTrue(Files.exists(Path.of(filePath + "Enemies.json")));

        SaveGame.loadGameState();
        int numberOfEnemies = 0;
        //Cannot use UGS because it's only used for structures and environmental objects, also maybe because enemies move(?)
        for (Entity enemy : ServiceLocator.getEntityService().getAllNamedEntities().values()) {
            if (enemy.getClass() == Enemy.class){
                numberOfEnemies++;
                assertEquals("Mr. Crabs", enemy.getName());
            }
        }
        assertEquals(1,numberOfEnemies);
    }


    @Test
    void testSaveGameFollowedByMultipleEnemiesLoad() {
        setUpEnemyTesting();

        Entity testEnemy = NPCFactory.createPirateCrabEnemy(testPlayer);
        testEnemy.setName("Mr. Crabs1");
        testEnemy.setPosition(2,2);
        Entity testEnemy1 = NPCFactory.createElectricEelEnemy(testPlayer, testCrystal);
        testEnemy1.setName("Mr. Electricity2");
        testEnemy1.setPosition(3,3);
        Entity testEnemy2 = NPCFactory.createStarFishEnemy(testPlayer, testCrystal);
        testEnemy2.setName("Mr. Starfish3");
        testEnemy2.setPosition(4,4);

        ServiceLocator.getEntityService().registerNamed("testCrab",  testEnemy);
        ServiceLocator.getUGSService().add(new GridPoint2(2, 2), testTile);
        ServiceLocator.getUGSService().setEntity(new GridPoint2(2, 2), testEnemy, "testEnemy");

        ServiceLocator.getEntityService().registerNamed("testEel",  testEnemy1);
        ServiceLocator.getUGSService().add(new GridPoint2(3, 3), testTile);
        ServiceLocator.getUGSService().setEntity(new GridPoint2(3, 3), testEnemy, "testEnemy1");

        //Starfish already registered in NPCFactory!
        //ServiceLocator.getEntityService().registerNamed("testStarfish",  testEnemy2);
        ServiceLocator.getUGSService().add(new GridPoint2(4, 4), testTile);
        ServiceLocator.getUGSService().setEntity(new GridPoint2(4, 4), testEnemy, "testEnemy2");

        SaveGame.saveGameState();
        assertTrue(Files.exists(Path.of(filePath + "Environmental.json")));
        assertTrue(Files.exists(Path.of(filePath + "Structures.json")));
        assertTrue(Files.exists(Path.of(filePath + "GameData.json")));
        assertTrue(Files.exists(Path.of(filePath + "Enemies.json")));

        SaveGame.loadGameState();
        int numberOfEnemies = 0;
        int numberOfCrabs = 0;
        int numberOfEels = 0;
        int numberOfStarfishes = 0;
        //Cannot use UGS because it's only used for structures and environmental objects, also maybe because enemies move(?)
        for (Entity enemy : ServiceLocator.getEntityService().getAllNamedEntities().values()) {
            if (enemy.getClass() == Enemy.class){
                numberOfEnemies++;
                if(enemy.getName() == "Mr. Crabs1"){
                    numberOfCrabs++;
                } else if (enemy.getName() == "Mr. Electricity2") {
                    numberOfEels++;
                } else if (enemy.getName() == "Mr. Starfish3") {
                    numberOfStarfishes++;
                }
            }
        }
        assertEquals(1,numberOfCrabs);
        assertEquals(1,numberOfEels);
        assertEquals(1,numberOfStarfishes);
        assertEquals(3,numberOfEnemies);
    }

    @Test
    void testSaveDayNightCycle() {
        DayNightCycleService cycleService = ServiceLocator.getDayNightCycleService();
        cycleService.currentDayNumber = 3;
        cycleService.currentCycleStatus = DayNightCycleStatus.DUSK;
        SaveGame.saveGameState();
        cycleService.currentDayNumber = 1;
        cycleService.currentCycleStatus = DayNightCycleStatus.NIGHT;
        SaveGame.loadGameState();
        assertEquals(3, cycleService.currentDayNumber);
        assertEquals(DayNightCycleStatus.DUSK, cycleService.currentCycleStatus);
    }

    @Test
    void testClockLoad() {
        Stage stage = mock(Stage.class);
        ServiceLocator.getRenderService().setStage(stage);
        DayNightCycleService cycleService = ServiceLocator.getDayNightCycleService();
        cycleService.currentDayNumber = 1;
        //says day 1, but is passed to the rest of the game and shown on clock as day 2
        cycleService.currentCycleStatus = DayNightCycleStatus.DUSK;
        DayNightClockComponent clockComponent = new DayNightClockComponent();
        new Entity().addComponent(clockComponent);
        clockComponent.create();
        clockComponent.loadFromSave();
        int currentSprite = clockComponent.getCurrentSprite();
        assertEquals((1 * 8 + 5), currentSprite);
        assertEquals("images/clock_sprites/clock_day2_6.png", clockSprites[currentSprite]);
    }
}
