package com.deco2800.game.entities;

import java.util.HashMap;

public class UGS {
    private HashMap<String, Tile> tiles;
    
    public UGS() {
        this.tiles = new HashMap<String, Tile>();
    }

    /**
     * Takes a String (concatenated x,y value) and returns the associated tile's type
     * @param String "xy"
     * @return String 
     */
    public String getTileType(String coordinate) {
        return tiles.get(coordinate).getTileType();
    }

    /**
     * Takes a String and returns the associated tile's entity 
     * or null if there is none
     * @param String "xy"
     * @return Entity or NULL
     */
    public Entity getEntity(String coordinate) {
        return tiles.get(coordinate).getEntity();
    }

    /**
     * Takes a String and Entity, and sets the corresponding tile's 
     * Entity parameter
     * @param String "xy"
     * @param entity Entity
     */
    public void setEntity(String coordinate, Entity entity) {
        tiles.get(coordinate).setEntity(entity);
    }

    /**
     * Takes a String and Entity, and sets the corresponding tile's 
     * tileType parameter
     * @param String "xy"
     * @param tileType
     */
    public void setTileType(String coordinate, String tileType) {
        tiles.get(coordinate).setTileType(tileType);
    }
    /**
     * Adds a new entry to the UGS 
     * @param String "xy"
     * @param tile Tile
     */
    public void put(String coordinate, Tile tile) {
        tiles.put(coordinate, tile);
    }

    /**
     * Returns a coordinate given an x and y value
     * @param x integer
     * @param y integer
     * @return String 
     */
    public static String generateCoordinate(int x, int y) {
        return String.format("%d,%d", x, y);
    }
  
}
