package com.deco2800.game.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.spy;

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
    @Test
    void shouldCreateUGS() {
        UGS tiles = new UGS();
        assertEquals(tiles.getClass(), UGS.class);
    }

    @Test
    void shouldGenerateCoordinate(){
        String string = UGS.generateCoordinate(1,1);
        assertEquals(string, "1,1");
    }

    @Test
    void testReturnSmallHashmap() {
        UGS tiles = new UGS();
        String coordinate = UGS.generateCoordinate(1,1);
        Tile tile = new Tile("Water");

        tiles.add(coordinate, tile);
        assertEquals(tiles.getTileType(coordinate), "Water");
        assertEquals(tiles.getEntity(coordinate), null);
        assertEquals(tiles.getTileType("1,1"), "Water");

    }

    @Test 
    void testSetTileType() {
        UGS tiles = new UGS();
        String coordinate = UGS.generateCoordinate(0,0);
        Tile tile = new Tile("");
        tiles.add(coordinate, tile);

        assertEquals(tiles.getTileType(coordinate), "");

        tiles.setTileType(coordinate, "Grass");
        assertEquals(tiles.getTileType(coordinate), "Grass");
    }

    @Test 
    void testSetEntity() {
        UGS tiles = new UGS();
        EntityService entityService = new EntityService();
        Entity entity = spy(Entity.class);
        entityService.register(entity);
        String coordinate = UGS.generateCoordinate(0,0);
        Tile tile = new Tile("");
        tiles.add(coordinate, tile);

        assertEquals(tiles.getEntity(coordinate), null);

        tiles.setEntity(coordinate, entity);
        assertEquals(tiles.getEntity(coordinate), entity);
    }

    @Test
    @ExtendWith(GameExtension.class)
    void testReturnEntitySmallHashmap() {
        EntityService entityService = new EntityService();
        Entity entity = spy(Entity.class);
        entityService.register(entity);
        
        
        UGS tiles = new UGS();
        String coordinate = UGS.generateCoordinate(0,0);
        Tile tile = new Tile("Water");
        tile.setEntity(entity);

        tiles.add(coordinate, tile);
        assertEquals(tiles.getTileType(coordinate), "Water");
        assertEquals(tiles.getEntity(coordinate), entity);
    }

    @Test
    void testReturnMultipleTiles(){
        UGS tiles = new UGS();
        Tile tile1 = new Tile();
        Tile tile2 = new Tile("water");
        Tile tile3 = new Tile();
        String coordinate1 = UGS.generateCoordinate(0, 0);
        String coordinate2 = UGS.generateCoordinate(1, 1);
        String coordinate3 = UGS.generateCoordinate(2, 2);

        tiles.add(coordinate1, tile1);
        tiles.add(coordinate2, tile2);
        tiles.add(coordinate2, tile2);

        assertEquals(tiles.getTileType(coordinate2), "water");
    }

    @Test
    void testReturnGeneratedTiles() {
        UGS tiles = new UGS();
        String coordinate = UGS.generateCoordinate(1, 1);
        String coordinate1 = UGS.generateCoordinate(1, 1);
        Tile tile = new Tile();
        tiles.add(coordinate, tile);
        assertEquals(tiles.getTileType(coordinate1), "");

    }
}
