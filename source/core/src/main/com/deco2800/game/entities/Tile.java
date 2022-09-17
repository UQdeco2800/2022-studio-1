package com.deco2800.game.entities;

import java.util.ArrayList;

public class Tile {
    private String tileType;
    private Entity entity;
    private ArrayList entityInfo;
    
    /**
     * Creates a new Tile object and initialises it with default values.
     */
    public Tile() {
        this.tileType = "";
        this.entity = null;
        this.entityInfo = new ArrayList<>();
    }
    /**
     * Creates a new Tile object and initialises it with a given tileType
     * 
     * @param tileType string
     */
    public Tile(String tileType) {
        this.tileType = tileType;
        this.entity = null;
        this.entityInfo = new ArrayList<>();
    }

    /**
     * Creates a new Tile object and initialises it with the provided
     * values for tileType and entity
     * @param tileType string
     * @param entity Entity
     */
    public Tile(String tileType, Entity entity) {
        this.tileType = tileType;
        this.entity = entity;
        this.entityInfo = new ArrayList<>();
    }

    /**
     * Sets the tileType parameter of the tile
     * @param tileType string
     */
    public void setTileType(String tileType) {
        this.tileType = tileType;
    }

    /**
     * Sets the entity parameter of the tile
     * @param entity
     */
    public void setEntity(Entity entity) {
        this.entity = entity;
    }

    /**
     * Returns the tile's type
     * @return string
     */
    public String getTileType() {
        return this.tileType;
    }

    /**
     * Returns the entity contained by the tile or null if none present
     * @return Entity 
     */
    public Entity getEntity() {
        return this.entity;
    }
}
