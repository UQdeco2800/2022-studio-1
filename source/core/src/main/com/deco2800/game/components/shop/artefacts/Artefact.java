package com.deco2800.game.components.shop.artefacts;

/** Abstract Artefact class used to create diffrent child artefact types */
public abstract class Artefact {
    protected String name;
    protected int price;
    protected String description;
    protected double attackMultiplier;
    protected double defenceMultiplier;
    protected String texture;
    protected String categoryTexture;

    /** Artefact class constructor */
    public Artefact(String name, int price, String description, double attackMultiplier, double defenceMultiplier,
            String texture,
            String categoryTexture) {
        this.name = name;
        this.price = price;
        this.description = description;
        this.attackMultiplier = attackMultiplier;
        this.defenceMultiplier = defenceMultiplier;
        this.texture = texture;
        this.categoryTexture = categoryTexture;
    }

    /**
     * Getter for artefact name
     * 
     * @return: String
     */
    public String getName() {
        return this.name;
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

    /**
     * Getter for artefact texture file
     * 
     * @return: String
     */
    public String getTexture() {
        return this.texture;
    }

    /**
     * Getter for artefact category texture file
     * 
     * @return: String
     */
    public String getCategoryTexture() {
        return this.categoryTexture;
    }
}
