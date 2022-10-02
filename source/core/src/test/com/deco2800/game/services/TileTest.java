package com.deco2800.game.services;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.spy;

import org.junit.Test;

import com.deco2800.game.entities.Entity;
import com.deco2800.game.entities.EntityService;
import com.deco2800.game.entities.Tile;

public class TileTest {
   
    /*
     * Test that the tile constructor with no parameters works correctly
     */
    @Test
    public void testConstructorNoParams(){
        Tile tile = new Tile();
        assertEquals(tile.getEntity(), null);
        assertEquals(tile.getTileType(), "");
    }
    /*
     * Test constructor with tileType works correctly
     */
    @Test
    public void testConstructorTileType() {
        Tile tile = new Tile("Water");
        assertEquals("Water", tile.getTileType());
        assertEquals(null, tile.getEntity());
    }
    
    /*
     * Test tile constructor with entity works correctly
     */
    @Test
    public void testConstructorEntity() {
        EntityService entityService = new EntityService();
        Entity entity = spy(Entity.class);
        entityService.register(entity);
        Tile tile = new Tile(entity);
        assertEquals(entity, tile.getEntity());
    }

    /*
     * Test the Tile setters work correctly
     */
    @Test
    public void testSetters() {
        //Create a mocked entity
        EntityService entityService = new EntityService();
        Entity entity = spy(Entity.class);
        entityService.register(entity);

        //Test tile default values are as expected
        Tile tile = new Tile();
        assertEquals("", tile.getTileType());
        assertEquals(null, tile.getEntity());

        //Set new tile attributes
        tile.setEntity(entity);
        tile.setTileType("grass");

        //Test setters
        assertEquals("grass", tile.getTileType());
        assertEquals(entity, tile.getEntity());
    }

    


}
