package com.deco2800.game.components.infrastructure;

/**
 * Core abstract infrastructure class which creates a default infrastructure object. 
 */
public abstract class Infrastructure {
    int health;
    /*
     * Creates an abstract infrastructure class. 
     * Returns entity
     */
    public Infrastructure(int health) {
        this.health = health; 
    }

    /*
     * Function which returns the health of the infrastructure class.
     * Returns: Integer representing health. 
     */
    public int getHealth(){
        return health;
    }
}

