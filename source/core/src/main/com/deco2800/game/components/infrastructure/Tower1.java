package com.deco2800.game.components.infrastructure;

import com.deco2800.game.entities.Entity;

/*
 * First specialised tower which extends upon the defensiveTower abstract class. 
 * Adds additional relevant components to create desired behaviour. 
 */

public class Tower1 extends DefensiveTower {
    
    /*
     * Constructor which initialises texture, health and damage to desired values
     */
    public Tower1(int health, int damage, String texture) {
        super(health, damage, texture);
        this.texture = "images/mud.png";
        this.health = 100;
        this.damage = 100;
    }
    
    /*
     * Function which creates the tower entity. 
     */
    public Entity createTower1() {
        Entity tower1 = createTower();

        return tower1;
    }    
}
