package com.deco2800.game.components.infrastructure;

import com.deco2800.game.entities.Entity;

/*
 * First specialised tower which extends upon the defensiveTower abstract class. 
 * Adds additional relevant components to create desired behaviour. 
 */
public class Tower1 extends DefensiveTower {
    static int HEALTH = 100;
    static int DAMAGE = 20; 
    /*
     * Constructor which initialises texture, health and damage to desired values
     */
    public Tower1(int health, int damage, String texture) {
        super(health, damage, texture);
        this.texture = "images/mud.png"; //Placeholder image, will need to replace
        this.health = HEALTH;
        this.damage = DAMAGE;
    }
    
    /*
     * Function which creates the tower entity. 
     */
    public Tower1 createTower1() {
        Tower1 tower1 = createTower();

        return tower1;
    }    
}
