package com.deco2800.game.components.infrastructure;

/**
 * Resource building is a type of building used to generate resources for the player
 */
public class ResourceBuilding {
    /**
     * The rate at which this building produces a resource
     */
    private int productionRate;
    /**
     * The type of resource that this building produces
     */
    private final ResourceType type;

    public ResourceBuilding(int health, int productionRate, ResourceType type) {
        super(health);
        this.productionRate = productionRate;
        this.type = type;
    }

    /**
     * Returns this building's production rate
     *
     * @return the production rate
     */
    public int getProductionRate() {
        return productionRate;
    }

    /**
     * Updates this building's production rate to a new value
     *
     * @param productionRate the new production rate
     */
    public void updateProductionRate(int productionRate) {
        this.productionRate = productionRate;
    }

    /**
     * Returns this building's resource type
     *
     * @return the resource type
     */
    public ResourceType getType() {
        return type;
    }
}
