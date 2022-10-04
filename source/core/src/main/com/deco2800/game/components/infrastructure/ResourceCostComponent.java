package com.deco2800.game.components.infrastructure;

import com.deco2800.game.components.Component;
/**
 * Component used to store information related to the cost of different structures: gold, stone etc. 
 * Any structures which have an associated cost should have an instance of this class registered.
 */
public class ResourceCostComponent extends Component{
    private int gold;
    private int stone;
    private int wood;

    /**
     * Resource Cost Component constructor for objects which only cost gold.
     * Gold has a minimum value of 0
     * @param gold
     */
    public ResourceCostComponent(int gold) {
        if (gold >=0 ) {
            this.gold = gold;
        } else {
            this.gold = 0;
        }
    }

    /**
     * Resource Cost Component with extra parameter for stone.
     * Gold and stone have a minimum value of 0
     * @param gold
     * @param stone
     */
    public ResourceCostComponent(int gold, int stone) {
        if (gold >=0 ) {
            this.gold = gold;
        } else {
            this.gold = 0;
        }
        
        if (stone >=0 ) {
            this.stone = stone;
        } else {
            this.stone = 0;
        }

    }
    
    /**
     * Resource Cost Component with extra parameters for stone and wood.
     * 
     * All three parameters have a minimum value of 0
     * @param gold
     * @param stone
     * @param wood
     */
    public ResourceCostComponent(int gold, int stone, int wood) {
        if (gold >=0 ) {
            this.gold = gold;
        } else {
            this.gold = 0;
        }
        
        if (stone >=0 ) {
            this.stone = stone;
        } else {
            this.stone = 0;
        }

        if (wood >=0 ) {
            this.wood = wood;
        } else {
            this.wood = 0;
        }
    }

    /**
     * Returns the entity's gold cost
     * @return int
     */
    public int getGoldCost(){
        return gold;
    }

    /**
     * Returns the entity's stone cost
     * @return int
     */
    public int getStoneCost(){
        return stone;
    }

    /**
     * Returns the entity's wood cost
     * @return int
     */
    public int getWoodCost(){
        return wood;
    }
}