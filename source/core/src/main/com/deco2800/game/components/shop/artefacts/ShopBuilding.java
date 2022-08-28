package com.deco2800.game.components.shop.artefacts;

/** Abstract building class used to create diffrent child building types */
public abstract class ShopBuilding {
    protected String name;
    protected int price;
    protected String description;
    protected double attackMultiplier;
    protected double defenceMultiplier;
    protected String texture;
    protected String categoryTexture;

    /** building class constructor */
    public ShopBuilding(String name, int price, String description, double attackMultiplier, double defenceMultiplier,
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
     * Getter for building name
     * 
     * @return: String
     */
    public String getName() {
        return this.name;
    }

    /**
     * Getter for building price
     * 
     * @return: int
     */
    public int getPrice() {
        return this.price;
    }

    /**
     * Getter for building description
     * 
     * @return: String
     */
    public String getDescription() {
        return this.description;
    }

    /**
     * Getter for building attack multiplier
     * 
     * @return: double
     */
    public double getAttackMultiplier() {
        return this.attackMultiplier;
    }

    /**
     * Getter for building defence multiplier
     * 
     * @return: double
     */
    public double getDefenceMultiplier() {
        return this.defenceMultiplier;
    }

    /**
     * Getter for building texture file
     * 
     * @return: String
     */
    public String getTexture() {
        return this.texture;
    }

    /**
     * Getter for building category texture file
     * 
     * @return: String
     */
    public String getCategoryTexture() {
        return this.categoryTexture;
    }
}
