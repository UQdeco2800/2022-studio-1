package com.deco2800.game.entities;

/**
 * Core infrastructure interface which creates a default infrastructure object. 
 */
public interface Infrastructure {

    /*
     * Creates a infrastructure entity. 
     * Returns entity
     */
     public static Entity createInfrastructure() {
        Entity infrastructure = 
            new Entity();
        return infrastructure;
     }
}
