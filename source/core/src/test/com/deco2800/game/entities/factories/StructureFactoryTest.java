//package com.deco2800.game.entities.factories;
//
//import com.badlogic.gdx.Files;
//import com.badlogic.gdx.Gdx;
//import com.badlogic.gdx.graphics.Texture;
//import com.deco2800.game.areas.terrain.TerrainComponent;
//import com.deco2800.game.components.CombatStatsComponent;
//import com.deco2800.game.components.HealthBarComponent;
//import com.deco2800.game.components.infrastructure.ResourceCostComponent;
//import com.deco2800.game.entities.Entity;
//import com.deco2800.game.entities.StructureService;
//import com.deco2800.game.entities.configs.BaseEntityConfig;
//import com.deco2800.game.entities.configs.BaseStructureConfig;
//import com.deco2800.game.entities.configs.StructureConfig;
//import com.deco2800.game.files.FileLoader;
//import com.deco2800.game.rendering.TextureRenderComponent;
//import com.deco2800.game.services.DayNightCycleService;
//import com.deco2800.game.services.ServiceLocator;
//import com.deco2800.game.services.configs.DayNightCycleConfig;
//import org.junit.jupiter.api.BeforeAll;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.Mock;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.Mockito.*;
//
//
//class StructureFactoryTest {
//
//    private static String[] textures = {"images/65x33_tiles/wall_left.png",
//            "images/65x33_tiles/wall_right.png",
//            "images/65x33_tiles/temp_wall_left.png",
//            "images/65x33_tiles/temp_wall_right.png",
//            "images/attack_towers/tempStructures/temp_tower3lv1Left.png",
//            "images/attack_towers/tempStructures/temp_tower3lv1Right.png",
//            "images/attack_towers/tower3lv1Left.png",
//            "images/attack_towers/tower3lv1Right.png",
//            "images/attack_towers/tower3lv2Left.png",
//            "images/attack_towers/tower3lv2Right.png",
//            "images/attack_towers/tower3lv3Left.png",
//            "images/attack_towers/tower3lv3Right.png",
//            "images/attack_towers/Attack_Structure2_lev1.png",
//            "images/attack_towers/Attack_Structure2_lev2.png",
//            "images/attack_towers/Attack_Structure2_lev3.png",
//            "images/attack_towers/tempStructures/temp_Attack_Structure2_lev1.png",
//            "images/attack_towers/tempStructures/temp_Attack_Structure2_lev1.png"};
//
//    @BeforeEach
//    void beforeEach() {
//    }
//
//    @Test
//    void shouldCreateWallEntity() {
//        @Mock StructureFactory structureFactory = new StructureFactory();
//        Entity wall = structureFactory.createWall("wall", false, 0);
//        assertTrue(wall != null);
//    }
//
//    @Test
//    void shouldCreateTempWallEntity() {
//    }
//}