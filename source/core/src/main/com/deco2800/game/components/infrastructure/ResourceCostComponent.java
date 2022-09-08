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
     * @param gold
     */
    public ResourceCostComponent(int gold) {
        this.gold = gold;
    }

    /**
     * Resource Cost Component with extra parameter for stone.
     * @param gold
     * @param stone
     */
    public ResourceCostComponent(int gold, int stone) {
        this.gold = gold;
        this.stone = stone;
    }
    
    /**
     * Resource Cost Component with extra parameters for stone and wood.
     * @param gold
     * @param stone
     * @param wood
     */
    public ResourceCostComponent(int gold, int stone, int wood) {
        this.gold = gold;
        this.stone = stone;
        this.wood = wood;
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