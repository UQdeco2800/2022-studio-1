package com.deco2800.game.components.infrastructure;

/**
 * Core abstract infrastructure class which creates a default infrastructure object. 
 */
public abstract class infrastructure {
    int health;
    /*
     * Creates an abstract infrastructure class. 
     * Returns entity
     */
    public infrastructure(int health) {
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

