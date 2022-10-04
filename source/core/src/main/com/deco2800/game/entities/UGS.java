package com.deco2800.game.entities;

import java.util.HashMap;

import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.deco2800.game.areas.terrain.TerrainComponent;
import com.deco2800.game.areas.terrain.TerrainTile;
import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.Vector2;
import com.deco2800.game.services.ServiceLocator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Provides a global access point for entities to register themselves to the UGS. This allows for 
 * checking for collisions and preventing entities from moving on certain tiles. 
 * 
 */
public class UGS {
    private HashMap<String, Tile> tiles;
    private static final Logger logger = LoggerFactory.getLogger(UGS.class);
    static int MAPSIZE = 120; 

    public UGS() {
        this.tiles = new HashMap<String, Tile>();
//        generateUGS();
    }   

    /**
     * Takes a String (concatenated x,y value) and returns the associated tile's type
     * @param coordinate
     * @return String 
     */
    public String getTileType(String coordinate) {
        return tiles.get(coordinate).getTileType();
    }

    /**
     * Takes a String and returns the associated tile's entity 
     * or null if there is none
     * @param coordinate
     * @return Entity or NULL
     */
    public Entity getEntity(String coordinate) {
        return tiles.get(coordinate).getEntity();
    }

    public void removeEntity(String name) {
        for (int x = 0; x < MAPSIZE; x++) {
            for (int y = 0; y < MAPSIZE; y++) {
                String strCoord = generateCoordinate(x, y);
                if (tiles.get(strCoord).getEntity() != null) {
                    if (tiles.get(strCoord).getEntity().getName().equals(name)) {
                        tiles.get(strCoord).setEntity(null);
                    }
                }
            }
        }
    }

    /**
     * Takes a String and Entity, and sets the corresponding tile's 
     * Entity parameter
     * @param coordinate
     * @param entity Entity
     */
    public void setEntity(String coordinate, Entity entity) {
        tiles.get(coordinate).setEntity(entity);
        System.out.print("tile entity ==> ");
        System.out.println(tiles.get(coordinate).getEntity());
    }

    /**
     * Takes a String entityType and String coordinate and decides if that type of enity can spawn
     * at that coordinate.
     * @param coordinate x, y of the gridpoint in string form
     * @param entityType type of entity in string form
     * @return true if the entity can spawn in a gridpoint else returns false
     */

    public Boolean checkEntityPlacement(String coordinate, String entityType) {
        if (entityType.equals("structure")) {
            if (tiles.get(coordinate).getTileType().equals("sand")) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    /**
     * Takes a String and Entity, and sets the corresponding tile's 
     * tileType parameter
     * @param coordinate
     * @param tileType
     */
    public void setTileType(String coordinate, String tileType) {
        tiles.get(coordinate).setTileType(tileType);
    }

    /**
     * Adds a new entry to the UGS 
     * @param coordinate
     * @param tile
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
     * Generates a coordinate given an x and y value
     * @param x integer
     * @param y integer
     * @return String 
     */
    public static String generateCoordinate(int x, int y) {
        return String.format("%d,%d", x, y);
    }

    /**
     * This function is to be called anytime you
     * @param toRemove the generated coordinate key that maps to the entity you want to remove
     */
    public void dispose(Entity toRemove) {
        if (ServiceLocator.getRangeService().registeredInUGS().contains(toRemove)) {
            Vector2 pos = toRemove.getPosition();
            GridPoint2 gridPos = ServiceLocator.getEntityService().getNamedEntity("terrain").getComponent(TerrainComponent.class).worldToTilePosition(pos.x, pos.y);
            String tileKey = generateCoordinate(gridPos.x, gridPos.y);
            Tile tile = new Tile();
            tiles.replace(tileKey, tile);
        }
    }
  
    /**
     * 
     *  --------------------Does not account for tile type--------------------
     * Checks whether an entity can move in the given direction, and returns True if the move was carried 
     * out correctly. If the move can be carried out, removes the entity from the current tile and adds it
     * to the new tile.
     * 
     * Takes a coordinate string as the currentPosition, and a boolean for yDirection and xDirection. 
     * yDirection: True if moving up (i.e. pressing w key) else false for moving down (s key)
     * xDirection: True if moving to the right (i.e. pressing d key ) else false for moving to the left (a key)
     * 
     * E.g. currentPosition: "3,3"
     * yDirection: True
     * xDirection: False
     * 
     * Moves from 3,3 -> "2,2"
     *                   {x-1, y-1}
     * @param currentPosition String
     * @param yDirection Boolean
     * @param xDirection Boolean
     */
    public boolean moveEntity(Entity entity, String currentPosition, boolean xDirection, boolean yDirection) {
        String[] currentCoords = currentPosition.split(",");
        int currentX = Integer.parseInt(currentCoords[0]);
        int currentY = Integer.parseInt(currentCoords[1]);

        if (xDirection) { //If xDirection is true then x coordinate increases [+1]
            if (yDirection) { //If yDirection is true then y coordinate decreases [-1]
                int newX = currentX + 1;
                int newY = currentY - 1;
                String coordinate = generateCoordinate(newX, newY);

                if (getEntity(coordinate) == null) { //Check no entity in new tile
                    setEntity(currentPosition, null); //Clear entity from currentPosition
                    setEntity(coordinate, entity); //Update entity to new position
                } else {
                    return false; 
                }
            } else {
                //xDirection true [+1], yDirection false [+1]
                int newX = currentX + 1;
                int newY = currentY + 1;
                String coordinate = generateCoordinate(newX, newY);

                if (getEntity(coordinate) == null) { //Check no entity in new tile
                    setEntity(currentPosition, null); //Clear entity from currentPosition
                    setEntity(coordinate, entity); //Update entity to new position
                } else {
                    return false; 
                }
            } 
        } else { //If xDirection is false then x coordinate decreases [-1]
            if (yDirection) { //If yDirection is true then y coordinate decreases [-1]
                int newX = currentX - 1;
                int newY = currentY - 1;
                String coordinate = generateCoordinate(newX, newY);

                if (getEntity(coordinate) == null) { //Check no entity in new tile
                    setEntity(currentPosition, null); //Clear entity from currentPosition
                    setEntity(coordinate, entity); //Update entity to new position
                } else {
                    return false; 
                }
            } else {
                //xDirection false [-1], yDirection false [+1]
                int newX = currentX - 1;
                int newY = currentY + 1;
                String coordinate = generateCoordinate(newX, newY);

                if (getEntity(coordinate) == null) { //Check no entity in new tile
                    setEntity(currentPosition, null); //Clear entity from currentPosition
                    setEntity(coordinate, entity); //Update entity to new position
                } else {
                    return false; 
                }
            } 

        }
        return false;
    }

    /**
     * Generates a Mapsize x Mapsize sized grid and initialises a Tile object
     * in each coordinate. 
     */
    public void generateUGS() {
        for (int x = 0; x < MAPSIZE; x++) {
            for (int y = 0; y < MAPSIZE; y++) {
                Tile tile = new Tile();
                String coordinate = generateCoordinate(x, y);
                this.add(coordinate,tile);
                TiledMap tiledMap = ServiceLocator.getEntityService().getNamedEntity("terrain").getComponent(TerrainComponent.class).getMap();
                String t = ((TerrainTile) ((TiledMapTileLayer) tiledMap.getLayers().get(1)).getCell(x,y).getTile()).getName();
                this.setTileType(coordinate, t);
            }
        }
    }

    /**
     * Returns a copy of the UGS for testing purposes
     * @return UGS
     */
    public HashMap<String, Tile> printUGS() {
        return this.tiles;
    }
}
