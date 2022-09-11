package com.deco2800.game.components.shop.artefacts;

/**
 * Abstract building class used to create diffrent child building types*/
public abstract class ShopBuilding {
     protected String name;
     protected int Stoneprice;
     protected int Woodprice;
     protected String description;
     protected double attackMultiplier;
     protected double defenceMultiplier;
     protected String texture;
     protected String categoryTexture;

    /** building class constructor
    * @param name              the name of the building
    * @param Stoneprice             the Stoneprice in stone of the building
    * @param description       what the building does
    * @param attackMultiplier  a decimal to increase attack (>= 1.00)
    * @param defenceMultiplier a decimal to increase defence (>= 1.00)
    * @param texture           image associated with the stone
    * @param categoryTexture   image with category board
    */
    public ShopBuilding(String name, int Stoneprice, int Woodprice, String description, double attackMultiplier, double defenceMultiplier,
            String texture,
            String categoryTexture) {
        this.name = name;
        this.Stoneprice = Stoneprice;
        this.Woodprice = Woodprice;
        this.description = description;
        this.attackMultiplier = attackMultiplier;
        this.defenceMultiplier = defenceMultiplier;
        this.texture = texture;
        this.categoryTexture = categoryTexture;
    }

    /**
     * Getter for building name
     * 
     * @return String
     */
    public String getName() {
        return this.name;
    }

    /**
     * Getter for building price
     * 
     * @return int
     */
    public int getStonePrice() {
        return this.Stoneprice;
    }

    /**
     * Getter for building price
     *
     * @return int
     */
    public int getWoodPrice() {return this.Woodprice; }

    /**
     * Getter for building description
     * 
     * @return String
     */
    public String getDescription() {
        return this.description;
    }

    /**
     * Getter for building attack multiplier
     * 
     * @return double
     */
    public double getAttackMultiplier() {
        return this.attackMultiplier;
    }

    /**
     * Getter for building defence multiplier
     * 
     * @return double
     */
    public double getDefenceMultiplier() {
        return this.defenceMultiplier;
    }

    /**
     * Getter for building texture file
     * 
     * @return String
     */
    public String getTexture() {
        return this.texture;
    }

    /**
     * Getter for building category texture file
     * 
     * @return String
     */
    public String getCategoryTexture() {
        return this.categoryTexture;
    }
}
