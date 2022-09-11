package com.deco2800.game.components.shop.artefacts;

/** Abstract Artefact class used to create diffrent child artefact types */
public abstract class Equipment {
    protected String name;
    protected int price;
    protected String description;
    protected double attackMultiplier;
    protected double defenceMultiplier;
    protected double restoreHealth;
    protected String texture;
    protected String categoryTexture;

    /**
     * @param name              the name of the equipment
     * @param price             the price in gold coin of the equipment
     * @param description       what the equipment does
     * @param attackMultiplier  a decimal to increase attack (>= 1.00)
     * @param defenceMultiplier a decimal to increase defence (>= 1.00)
     * @param texture           image associated with the Equipment
     * @param categoryTexture   image with category board
     */
    public Equipment(String name, int price, String description, double attackMultiplier, double defenceMultiplier, double restoreHealth,
                    String texture,
                    String categoryTexture) {
        this.name = name;
        this.price = price;
        this.description = description;
        this.attackMultiplier = attackMultiplier;
        this.defenceMultiplier = defenceMultiplier;
        this.restoreHealth = restoreHealth;
        this.texture = texture;
        this.categoryTexture = categoryTexture;
    }

    /**
     * Getter for equipment name
     *
     * @return String name of equipment
     */
    public String getName() {
        return this.name;
    }

    /**
     * Getter for equipment price
     *
     * @return int price of equipment
     */
    public int getPrice() {
        return this.price;
    }

    /**
     * Getter for equipment description
     *
     * @return String description
     */
    public String getDescription() {
        return this.description;
    }

    /**
     * Getter for equipment attack multiplier
     *
     * @return double attack multiplier
     */
    public double getAttackMultiplier() {
        return this.attackMultiplier;
    }

    /**
     * Getter for equipment defence multiplier
     *
     * @return double defence multiplier
     */
    public double getDefenceMultiplier() {
        return this.defenceMultiplier;
    }

    /**
     * Getter for equipment restore health
     *
     * @return double restore health
     */
    public double getRestoreHealth() {
        return this.restoreHealth;
    }

    /**
     * Getter for equipment texture file
     *
     * @return Texture
     */
    public String getTexture() {
        return this.texture;
    }

    /**
     * Getter for equipment category texture file
     *
     * @return CategoryTexture
     */
    public String getCategoryTexture() {
        return this.categoryTexture;
    }
}
