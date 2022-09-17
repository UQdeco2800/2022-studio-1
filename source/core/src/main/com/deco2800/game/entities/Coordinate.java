package com.deco2800.game.entities;

/**
 * Class used by the Unified Grid system to store the relative x,y coordinate
 * of the tile
 */
public class Coordinate {
    private int x;
    private int y;

    public Coordinate(int x, int y){
        this.x = x;
        this.y = y;
    }

    /**
     * Returns the x coordinate
     * @return int
     */
    public int getX() {
        return this.x;
    }

    /**
     * Returns the y coordinate
     * @return int
     */
    public int getY() {
        return this.y;
    }
}
