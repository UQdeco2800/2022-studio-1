package com.deco2800.game.services;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.Test;
import com.deco2800.game.entities.Tile;

public class TileTest {
   
    @Test
    public void testConstructorNoParams(){
        Tile tile = new Tile();
        assertEquals(tile.getEntity(), null);
        assertEquals(tile.getTileType(), "");
    }


}
