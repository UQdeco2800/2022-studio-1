package com.deco2800.game.components.infrastructure;

public class Wall extends Infrastructure {
    static int HEALTH = 500;
    String texture;
    /*
     * Constructor which initialises wall object and sets
     * texture and health to desired values
     */
    public Wall() {
        super(HEALTH);
        this.texture = "images/tree.png"; //Placeholder image, will need to replace
    }
    
    /*
     * Function which creates and returns a wall object with the desired health
     * value
     */
    public Wall createWall() {
        Wall wall = new Wall();
        return wall;
    }
    
}
