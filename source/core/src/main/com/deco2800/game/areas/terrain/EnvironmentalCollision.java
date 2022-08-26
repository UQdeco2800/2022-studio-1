package com.deco2800.game.areas.terrain;

import com.badlogic.gdx.math.Vector2;
import com.deco2800.game.entities.Entity;

import java.util.Collection;
import java.util.Hashtable;


/**
 * Helper Class that stores entities on the map and checks collisions when given a new entity
 * Used for placing environmental objects such as trees, rocks and buildings plus additional
 * utility like finding closet entity to a point or retrieving a list of all environmental entities
 */
public class EnvironmentalCollision {

    private Hashtable<Vector2, Entity> entityMap;
    private TerrainComponent terrain;

    /**
     * Constructor method for the Environmental Collision.
     * @param terrain Requires the terrain for convering x,y into world positions
     */
    public EnvironmentalCollision(TerrainComponent terrain) {
        this.entityMap = new Hashtable<>();
        this.terrain = terrain;
    }

    /**
     * @return Collection of entities stored on the map
     */
    public Collection<Entity> getEntities() {
        return entityMap.values();
    }

    /**
     * Adds a new entity to hashtable
     * @param newEntity new entity to be added to the environment
     */
    public void addEntity(Entity newEntity) {
        entityMap.put(newEntity.getCenterPosition(), newEntity);
    }

    /**
     * Finds the closet entity based off euclidean distance from a given x,y point
     * @param x cell cord
     * @param y cell cord
     * @return Entity closet
     */
    public Entity findClosetEntity(int x, int y) {
        if (entityMap.values().size() == 0) {
            return null;
        }

        Entity closetEntity = null;
        float smallestDistance = 99999;

        for (Entity entity: entityMap.values()) {
            float entityX = entity.getCenterPosition().x;
            float entityY = entity.getCenterPosition().y;

            double currentDistance = Math.sqrt(Math.pow(Math.abs(x - entityX), 2) + Math.pow(Math.abs(y - entityY), 2));

            if (currentDistance < smallestDistance) {
                closetEntity = entity;
            }
        }

        return closetEntity;
    }


    /**
     * Calculates if the given entity will collide with already existing entities
     * Still in testing phase. Uses the scale x and scale y of the entity to determine collision/hitbox
     * size
     *
     * Due to isometric view world positions must be used thus the conversion from cell coordinates
     *
     * @param potentialEntity The new entity to be added to the map
     * @param xPotential the proposed x cell position of the entity
     * @param yPotential the proposed y cell position of the entity
     * @return true if a collision would occur else false
     */
    public Boolean wouldCollide(Entity potentialEntity, int xPotential, int yPotential) {
        //if empty no collisions to check:
        if (entityMap.values().size() == 0) {
            return false;
        }

        //convert to world positions
        float x = terrain.tileToWorldPosition(xPotential, yPotential).x;
        float y = terrain.tileToWorldPosition(xPotential, yPotential).y;

        //x,y positions of potential entity
        float potentialEntityTop = y + potentialEntity.getScale().y / 2 ;
        float potentialEntityBottom = y - potentialEntity.getScale().y / 2 ;
        float potentialEntityRight = x + potentialEntity.getScale().x / 2 ;
        float potentialEntityLeft = x - potentialEntity.getScale().x / 2;

        for (Entity entity: entityMap.values()) {
            //x,y positions of current entity
            float placedRight = entity.getCenterPosition().x + entity.getScale().x / 2;
            float placedLeft = entity.getCenterPosition().x - entity.getScale().x / 2;
            float placedTop = entity.getCenterPosition().y + entity.getScale().y / 2;
            float placedBottom = entity.getCenterPosition().y - entity.getScale().y / 2;

            //check if collision occurs with current entity
            if (!(potentialEntityRight <  placedLeft || potentialEntityLeft > placedRight)) {
                return true;
            } else if (!(potentialEntityBottom > placedTop || potentialEntityTop < placedBottom)) {
                return true;
            }
        }

        return false;
    }

}
