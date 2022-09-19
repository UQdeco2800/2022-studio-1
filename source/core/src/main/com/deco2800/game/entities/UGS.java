package com.deco2800.game.entities;

import java.util.HashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UGS {
    private HashMap<String, Tile> tiles;
    private static final Logger logger = LoggerFactory.getLogger(UGS.class);

    
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
    public void add(String coordinate, Tile tile) {
        tiles.put(coordinate, tile);
    }

    /**
     * Function for setting / updating tiles for an entity whose size is greater than 1x1.
     * 
     * Function takes an x,y dimension and will set/update the coordinates within those dimensions from the x,y
     * origin to contain the entity.
     * 
     * e.g.
     * origin is 1,0 
     * dimensionX = 2
     * dimensionY = 2
     * 
     *  _ _ _      _ _ _       
     * |_|_|_|    |_|x|x|
     * |_|_|_| -> |_|x|x|
     * |_|_|_|    |_|_|_|
     * 
     * @param origin String
     * @param entity Entity
     * @param dimensionX Int
     * @param dimensionY Int
     */
    public void setLargeEntity(String origin, Entity entity, int dimensionX, int dimensionY) {
        String[] originCoords = origin.split(",");
        int originX = Integer.parseInt(originCoords[0]);
        int originY = Integer.parseInt(originCoords[1]);

        for (int x = 0; x < dimensionX; x++) {
            for (int y = 0; y < dimensionY; y++) {
                int coordX = originX + x;
                int coordY = originY + y;
                String coordinate = generateCoordinate(coordX, coordY);
                this.setEntity(coordinate, entity);
            }
        }
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
