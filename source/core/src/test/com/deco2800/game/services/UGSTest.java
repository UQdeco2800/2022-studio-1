package com.deco2800.game.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.spy;

import com.badlogic.gdx.math.GridPoint2;
import org.junit.Before;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import com.deco2800.game.entities.Entity;
import com.deco2800.game.entities.EntityService;
import com.deco2800.game.entities.Tile;
import com.deco2800.game.entities.UGS;
import com.deco2800.game.entities.factories.StructureFactory;
import com.deco2800.game.extensions.GameExtension;

class UGSTest {
    /**
     * Tests that a UGS is created correctly
     */
    @Test
    void shouldCreateUGS() {
        UGS tiles = new UGS();
        assertEquals(tiles.getClass(), UGS.class);
    }

    /**
     * Tests that a coordinate is generated correctly 
     */
    @Test
    void shouldGenerateCoordinate(){
        String string = UGS.generateCoordinate(1,1);
        assertEquals(string, "1,1");
    }

    /**
     * Tests that a Tile can be added to the UGS, and that the UGS returns 
     * the same result if the same key is provided
     */
    @Test
    void testReturnSmallHashmap() {
        UGS tiles = new UGS();
        GridPoint2 coordinate = new GridPoint2(1, 1);
        Tile tile = new Tile("Water");

        tiles.add(coordinate, tile);
        assertEquals(tiles.getTileType(coordinate), "Water");
        assertEquals(tiles.getEntity(coordinate), null);
        assertEquals(tiles.getTileType(new GridPoint2(1, 1)), "Water");

    }

    /**
     * Tests a Tile within the UGS can be updated 
     */
    @Test 
    void testSetTileType() {
        UGS tiles = new UGS();
        GridPoint2 coordinate = new GridPoint2(0, 0);
        Tile tile = new Tile("");
        tiles.add(coordinate, tile);

        assertEquals(tiles.getTileType(coordinate), "");

        tiles.setTileType(coordinate, "Grass");
        assertEquals(tiles.getTileType(coordinate), "Grass");
    }

    
    /**
     * Tests a Tile created with a tileType can be updated with a
     * new entity type
     */
    @Test
    @ExtendWith(GameExtension.class)
    void testSetEntity() {
        EntityService entityService = new EntityService();
        Entity entity = spy(Entity.class);
        entityService.register(entity);
        
        
        UGS tiles = new UGS();
        GridPoint2 coordinate = new GridPoint2(0, 0);
        Tile tile = new Tile("Water");
        tile.setEntity(entity);

        tiles.add(coordinate, tile);
        assertEquals(tiles.getTileType(coordinate), "Water");
        assertEquals(tiles.getEntity(coordinate), entity);
    }

    /**
     * Tests the UGS returns the correct tileType given it contains multiple tiles
     */
    @Test
    void testReturnMultipleTiles(){
        UGS tiles = new UGS();
        Tile tile1 = new Tile();
        Tile tile2 = new Tile("water");
        Tile tile3 = new Tile();
        GridPoint2 coordinate1 = new GridPoint2(0, 0);
        GridPoint2 coordinate2 = new GridPoint2(1, 1);
        GridPoint2 coordinate3 = new GridPoint2(2, 2);

        tiles.add(coordinate1, tile1);
        tiles.add(coordinate2, tile2);
        tiles.add(coordinate3, tile3);

        assertEquals(tiles.getTileType(coordinate2), "water");
    }

    /**
     * Tests the return value of the UGS is independent of the input 
     */
    @Test
    void testReturnGeneratedTiles() {
        UGS tiles = new UGS();
        GridPoint2 coordinate = new GridPoint2(1, 1);
        GridPoint2 coordinate1 = new GridPoint2(1,1);
        Tile tile = new Tile();
        tiles.add(coordinate, tile);
        assertEquals(tiles.getTileType(coordinate1), "");

    }

    @Test
    void testRemoveEntity() {
        EntityService entityService = new EntityService();

        UGS ugs = new UGS();
        Tile tile1 = new Tile();
        GridPoint2 coordinate1 = new GridPoint2(0, 0);
        ugs.add(coordinate1, tile1);
        Entity testEntity = new Entity();
        ugs.setEntity(coordinate1, testEntity, "testEntity");

        assertEquals(ugs.getEntity(coordinate1), testEntity);

        ugs.removeEntity("testEntity");
        assertNull(ugs.getEntity(coordinate1));
    }

    @Test
    void testGetEntityByName() {
        UGS ugs = new UGS();
        Tile tile1 = new Tile();
        GridPoint2 coordinate1 = new GridPoint2(0, 0);
        ugs.add(coordinate1, tile1);
        Entity testEntity = new Entity();
        ugs.setEntity(coordinate1, testEntity, "testEntity");

        assertEquals(testEntity, ugs.getEntityByName("testEntity"));
    }

//    /**
//     * Tests the UGS can be updated with a large entity
//     */
//    @Test
//    void TestSetLargeEntity() {
//        UGS tiles = new UGS();
//        EntityService entityService = new EntityService();
//        Entity entity = spy(Entity.class);
//        entityService.register(entity);
//
//        //Generated a 20x20 grid
//        for (int x = 0; x < 20; x++) {
//            for (int y = 0; y < 20; y++) {
//                GridPoint2 coord = new GridPoint2(x, y);
//                Tile tile = new Tile();
//                tiles.add(coord, tile);
//            }
//        }
//        GridPoint2 origin = new GridPoint2(0, 0);
//
//        //Set Large entity at 0,0 with width and height of 5
//        //Need to mockito entity service to make this work
//        tiles.setLargeEntity(origin, entity, 5, 5, "largeEntity");
//
//        //Check all tiles were set correctly
//        for (int x = 0; x < 5; x++) {
//            for (int y = 0; y < 5; y++) {
//                GridPoint2 coord = new GridPoint2(x, y);
//                assertEquals(tiles.getEntity(coord), entity);
//            }
//        }
//
//        //Check tiles along x-axis of the cube were not changed
//        for (int x = 0; x < 6; x++) {
//            GridPoint2 coord = new GridPoint2(x, 6);
//            assertEquals(tiles.getEntity(coord), null);
//        }
//
//        //Check tiles along y-axis of the cube were not changed
//        for (int y = 0; y < 6; y++) {
//            GridPoint2 coord = new GridPoint2(6, y);
//            assertEquals(tiles.getEntity(coord), null);
//        }
//    }
}
