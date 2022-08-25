package com.deco2800.game.components.shop.artefacts;

/** Abstract Artefact class used to create diffrent child artefact types */
public abstract class Artefact {
    protected int price;
    protected String description;
    protected double attackMultiplier;
    protected double defenceMultiplier;

    /** Artefact class constructor */
    public Artefact(int price, String description, double attackMultiplier, double defenceMultiplier) {
        this.price = price;
        this.description = description;
        this.attackMultiplier = attackMultiplier;
        this.defenceMultiplier = defenceMultiplier;
    }

    /**
     * Getter for artefact price
     * 
     * @return: int
     */
    public int getPrice() {
        return this.price;
    }

    /**
     * Getter for artefact description
     * 
     * @return: String
     */
    public String getDescription() {
        return this.description;
    }

    /**
     * Getter for artefact attack multiplier
     * 
     * @return: double
     */
    public double getAttackMultiplier() {
        return this.attackMultiplier;
    }

    /**
     * Getter for artefact defence multiplier
     * 
     * @return: double
     */
    public double getDefenceMultiplier() {
        return this.defenceMultiplier;
    }
}
