package com.deco2800.game.components.infrastructure;
import com.deco2800.game.entities.Entity;
/*
 * Core defensiveTower class which extends the infrastructure class. Adds 
 * additional attributes such as damage, which all defensive towers possess. 
 */

public abstract class defensiveTower extends infrastructure {
    int damage; //Damage of the given defensive tower

    public defensiveTower(int health, int damage) {
        super(health); //Sets the health of the tower
        this.damage = damage; //Sets the damage of the tower

    }
}
