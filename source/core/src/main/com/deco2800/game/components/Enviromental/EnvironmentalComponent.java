package com.deco2800.game.components.Enviromental;

import com.deco2800.game.components.Component;

/**
 * Universal component used for environmental objects. Currently in initial testing phase. With additional
 * environmental objects to be created.
 *
 * An environmental component has a set of attributes based off the environmental type
 * to be used for attaching to entities.
 *
 * Example of correct usage when attaching to entity is (Using a rock):
 *      new EnvironmentalComponent().setType(EnvironmentalComponent.EnvironmentalType.ROCK)
 *
 */
public class EnvironmentalComponent extends Component {

    /**
     * Enum containing the associate values for the environmental component
     *
     * An environmental component has:
     *      resourceValue: The number of resources to be returned when this object is destroyed
     *      speedModifier: A speed modifier for the player/enemy
     *
     * E.g A cobweb has resourceValue = 0, speedModifier = 0.6 indicating zero resources will be given when broken
     * and when a player walks through it, their speed is reduced by 40%
     */
    public enum EnvironmentalType {
        UNDEFINED(0, 1),
        WOOD(10, 1),
        ROCK(20, 1),
        COBWEB(0, 0.6);

        private int resourceValue;
        private double speedModifier;

        /**
         * Enum constructor for EnvironmentalType
         *
         * @param resourceValue The number of resources to give when the Environmental type is destroyed/removed
         * @param speedModifier The active modifier this object will apply to players/enemies/entities
         */
        EnvironmentalType(int resourceValue, double speedModifier) {
            this.resourceValue = resourceValue;
            this.speedModifier = speedModifier;
        }
    }

    private EnvironmentalType type;

    /**
     * Sets the component to be UNDEFINED on startup
     */
    @Override
    public void create() {
        this.type = EnvironmentalType.UNDEFINED;
    }

    /**
     * @return The Enum type of environmental object with associated attributes
     */
    public EnvironmentalType getType() {
        return this.type;
    }

    /**
     * Basic setter of component type
     * @param type the type of component from EnvironmentalType
     * @return itself (EnvironmentalComponent) to be used in Obstacle Factory
     */
    public EnvironmentalComponent setType(EnvironmentalType type) {
        this.type = type;
        return this;
    }

    /**
     * @return The number of resources when the Environmental Type is broken/destroyed
     */
    public Integer getResourceAmount() {
        return this.type.resourceValue;
    }

    /**
     * @return The speed modifier of the Environmental Type
     */
    public Double getSpeedModifier() {
        return this.type.speedModifier;
    }

    @Override
    public String toString() {
        return "";
    }

}
