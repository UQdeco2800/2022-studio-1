package com.deco2800.game.components.Environmental;

import com.deco2800.game.components.Component;

/**
 * Universal component used for environmental objects. Currently in initial
 * testing phase. With additional
 * environmental objects to be created.
 *
 * An environmental component has a set of attributes based off the
 * environmental type
 * to be used for attaching to entities.
 *
 * Example of correct usage when attaching to entity is (Using a rock):
 * new
 * EnvironmentalComponent().setType(EnvironmentalComponent.EnvironmentalType.ROCK)
 *
 */
public class EnvironmentalComponent extends Component {

    private EnvironmentalObstacle environmentalObstacle;

    /**
     * Types of resources
     */
    public enum ResourceTypes {
        NONE,
        STONE,
        GOLD,
        WOOD;
    }

    /**
     * Enum containing the associate values for the environmental component
     *
     * An environmental component has:
     * resourceType: The type of resource
     * resourceValue: The number of resources to be returned associated with
     * resource type
     *
     * E.g A cobweb has resourceValue = 0, speedModifier = 0.6 indicating zero
     * resources will be given when broken
     * and when a player walks through it, their speed is reduced by 40%
     */
    public enum EnvironmentalObstacle {
        UNDEFINED(ResourceTypes.NONE, 0),
        TREE(ResourceTypes.WOOD, 10),
        ROCK(ResourceTypes.STONE, 20),
        SPIKY_BUSH(ResourceTypes.WOOD, 20),
        KNOCKBACK_TOWER(ResourceTypes.GOLD, 20),
        SPEED_ARTEFACT(ResourceTypes.GOLD, 1),
        VINE(ResourceTypes.NONE, 0),
        STONE_PILLAR(ResourceTypes.STONE, 30),
        GEYSER(ResourceTypes.STONE, 10),
        WOODEN_FENCE(ResourceTypes.WOOD, 5),
        SHIPWRECK_FRONT(ResourceTypes.WOOD, 30),
        SHIPWRECK_BACK(ResourceTypes.WOOD, 50);

        private int resourceValue;
        private ResourceTypes type;

        /**
         * Enum constructor for EnvironmentalType
         *
         * @param resourceValue The number of resources to give when the Environmental
         *                      type is destroyed/removed
         */
        EnvironmentalObstacle(ResourceTypes type, int resourceValue) {
            this.resourceValue = resourceValue;
            this.type = type;
        }
    }

    /**
     * Sets the component to be UNDEFINED on startup
     */
    @Override
    public void create() {
        this.environmentalObstacle = EnvironmentalObstacle.UNDEFINED;
    }

    /**
     * @return The Enum type of environmental object with associated attributes
     */
    public EnvironmentalObstacle getObstacle() {
        return this.environmentalObstacle;
    }

    /**
     * Basic setter of component type
     * 
     * @param obstacle the type of component from EnvironmentalType
     * @return itself (EnvironmentalComponent) to be used in Obstacle Factory
     */
    public EnvironmentalComponent setObstacle(EnvironmentalObstacle obstacle) {
        this.environmentalObstacle = obstacle;
        return this;
    }

    /**
     * @return The number of resources when the Environmental Type is
     *         broken/destroyed
     */
    public Integer getResourceAmount() {
        return this.environmentalObstacle.resourceValue;
    }

    /**
     * @return the resource type
     */
    public ResourceTypes getType() {
        return this.environmentalObstacle.type;
    }

}
