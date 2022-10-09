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
    public String getTileType(GridPoint2 coordinate) {
        String stringCoord = generateCoordinate(coordinate.x, coordinate.y);
        return tiles.get(stringCoord).getTileType();
    }

    /**
     * Takes a String and returns the associated tile's entity 
     * or null if there is none
     * @param coordinate
     * @return Entity or NULL
     */
    public Entity getEntity(GridPoint2 coordinate) {
        String stringCoord = generateCoordinate(coordinate.x, coordinate.y);
        return tiles.get(stringCoord).getEntity();
    }

    /**
     * Removes an entity from the hashmap by name
     * @param name of the entity to remove
     */
    public void removeEntity(String name) {
        for (int x = 0; x < MAPSIZE; x++) {
            for (int y = 0; y < MAPSIZE; y++) {
                String strCoord = generateCoordinate(x, y);
                if (tiles.get(strCoord).getEntity() != null) {
                    if (tiles.get(strCoord).getEntity().getName().equals(name)) {
                        ServiceLocator.getEntityService().getNamedEntity(name).dispose();
                        tiles.get(strCoord).setEntity(null);
                    }
                }
            }
        }
    }

    public String getStringByEntity(Entity entity) {
        for (String tilePos: tiles.keySet()) {
            if (tiles.get(tilePos).getEntity() != null && tiles.get(tilePos).getEntity().equals(entity)) {
                return tilePos;
            }
        }
        return null;
    }

    /**
     * Returns an entity or null found in the ugs by name of that entity
     * @param name of the entity to find
     * @return the entity found or null
     */
    public Entity getEntityByName(String name) {
        for (int x = 0; x < MAPSIZE; x++) {
            for (int y = 0; y < MAPSIZE; y++) {
                String strCoord = generateCoordinate(x, y);
                if (tiles.get(strCoord).getEntity() != null) {
                    if (tiles.get(strCoord).getEntity().getName().equals(name)) {
                        return tiles.get(strCoord).getEntity();
                    }
                }
            }
        }
        return null;
    }

    /**
     * Takes a String and Entity, and sets the corresponding tile's 
     * Entity parameter
     * @param coordinate
     * @param entity Entity
     */
    public Boolean setEntity(GridPoint2 coordinate, Entity entity, String entityName) {
        if (checkEntityPlacement(coordinate, entityName)) {
            if (entity != null) {
                //Add entity to the entity list through the entity service
                ServiceLocator.getEntityService().registerNamed(entityName, entity);
                Vector2 entityWorldPos = ServiceLocator.getEntityService().getNamedEntity("terrain").getComponent(TerrainComponent.class).tileToWorldPosition(coordinate);
                entity.setPosition(entityWorldPos);
            }
            String stringCoord = generateCoordinate(coordinate.x, coordinate.y);
            tiles.get(stringCoord).setEntity(entity);
            return true;
        }
        return false;
    }

    /**
     * Takes a String entityType and String coordinate and decides if that type of enity can spawn
     * at that coordinate.
     * @param coordinate x, y of the gridpoint in string form
     * @param entityType type of entity in string form
     * @return true if the entity can spawn in a gridpoint else returns false
     */

    public Boolean checkEntityPlacement(GridPoint2 coordinate, String entityType) {
        String stringCoord = generateCoordinate(coordinate.x, coordinate.y);
        if (getEntity(coordinate) == null) {
            if (entityType.contains("structure")) {
                if (tiles.get(stringCoord).getTileType().equals("sand")) {
                    logger.info("Building has been built at {}", coordinate);
                    return true;
                } else {
                    logger.info("Building cannot be built on water");
                    return false;
                }
            } else {
                logger.info("Tile {} is clear", coordinate);
                return true;
            }
        }
        logger.info("Tile {} is not clear", coordinate);
        return false;
    }

    /**
     * Takes a String and Entity, and sets the corresponding tile's 
     * tileType parameter
     * @param coordinate
     * @param tileType
     */
    public void setTileType(GridPoint2 coordinate, String tileType) {
        String stringCoord = generateCoordinate(coordinate.x, coordinate.y);
        tiles.get(stringCoord).setTileType(tileType);
    }

    /**
     * Adds a new entry to the UGS 
     * @param coordinate
     * @param tile
     */
    public void add(GridPoint2 coordinate, Tile tile) {
        String stringCoord = generateCoordinate(coordinate.x, coordinate.y);
        tiles.put(stringCoord, tile);
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
    public void setLargeEntity(GridPoint2 origin, Entity entity, int dimensionX, int dimensionY, String entityName) {
       for (int x = 0; x < dimensionX; x++) {
            for (int y = 0; y < dimensionY; y++) {
                int coordX = origin.x + x;
                int coordY = origin.y + y;
                GridPoint2 coordinate = new GridPoint2(coordX, coordY);
                this.setEntity(coordinate, entity, entityName);
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
    public boolean moveEntity(Entity entity, GridPoint2 currentPosition, boolean xDirection, boolean yDirection, String entityName) {

        if (xDirection) { //If xDirection is true then x coordinate increases [+1]
            if (yDirection) { //If yDirection is true then y coordinate decreases [-1]
                int newX = currentPosition.x + 1;
                int newY = currentPosition.y - 1;
                GridPoint2 coordinate = new GridPoint2(newX, newY);

                if (getEntity(coordinate) == null) { //Check no entity in new tile
                    setEntity(currentPosition, null, ""); //Clear entity from currentPosition
                    setEntity(coordinate, entity, entityName); //Update entity to new position
                } else {
                    return false; 
                }
            } else {
                //xDirection true [+1], yDirection false [+1]
                int newX = currentPosition.x + 1;
                int newY = currentPosition.y + 1;
                GridPoint2 coordinate = new GridPoint2(newX, newY);

                if (getEntity(coordinate) == null) { //Check no entity in new tile
                    setEntity(currentPosition, null, ""); //Clear entity from currentPosition
                    setEntity(coordinate, entity, entityName); //Update entity to new position
                } else {
                    return false; 
                }
            } 
        } else { //If xDirection is false then x coordinate decreases [-1]
            if (yDirection) { //If yDirection is true then y coordinate decreases [-1]
                int newX = currentPosition.x - 1;
                int newY = currentPosition.y - 1;
                GridPoint2 coordinate = new GridPoint2(newX, newY);

                if (getEntity(coordinate) == null) { //Check no entity in new tile
                    setEntity(currentPosition, null, ""); //Clear entity from currentPosition
                    setEntity(coordinate, entity, entityName); //Update entity to new position
                } else {
                    return false; 
                }
            } else {
                //xDirection false [-1], yDirection false [+1]
                int newX = currentPosition.x - 1;
                int newY = currentPosition.y + 1;
                GridPoint2 coordinate = new GridPoint2(newX, newY);

                if (getEntity(coordinate) == null) { //Check no entity in new tile
                    setEntity(currentPosition, null, ""); //Clear entity from currentPosition
                    setEntity(coordinate, entity, entityName); //Update entity to new position
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
                GridPoint2 coordinate = new GridPoint2(x, y);
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
