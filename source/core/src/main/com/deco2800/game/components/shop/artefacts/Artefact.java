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

    /**
     * @param name              the name of the artefact
     * @param price             the price in gold coin of the artefact
     * @param description       what the artefact does
     * @param attackMultiplier  a decimal to increase attack (>= 1.00)
     * @param defenceMultiplier a decimal to increase defence (>= 1.00)
     * @param texture           image associated with the Artefact
     * @param categoryTexture   image with category board
     */
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
     * @return String name of artefact
     */
    public String getName() {
        return this.name;
    }

    /**
     * Getter for artefact price
     * 
     * @return int price of artefact
     */
    public int getPrice() {
        return this.price;
    }

    /**
     * Getter for artefact description
     * 
     * @return String description
     */
    public String getDescription() {
        return this.description;
    }

    /**
     * Getter for artefact attack multiplier
     * 
     * @return double attack multiplier
     */
    public double getAttackMultiplier() {
        return this.attackMultiplier;
    }

    /**
     * Getter for artefact defence multiplier
     * 
     * @return double defence multiplier
     */
    public double getDefenceMultiplier() {
        return this.defenceMultiplier;
    }

    /**
     * Getter for artefact texture file
     * 
     * @return Texture
     */
    public String getTexture() {
        return this.texture;
    }

    /**
     * Getter for artefact category texture file
     * 
     * @return CategoryTexture
     */
    public String getCategoryTexture() {
        return this.categoryTexture;
    }
}
