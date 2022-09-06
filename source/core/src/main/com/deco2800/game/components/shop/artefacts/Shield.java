package com.deco2800.game.components.shop.artefacts;

/**
 * Shield child class of equipment
 */
public class Shield extends Equipment {
    static String NAME = "Shield";
    static int PRICE = 10;
    static String DESCRIPTION = "Increase the main character's defense power";
    static double ATTACKMULTI = 0.00;
    static double DEFENCEMULTI = 3.00;
    static double RESTOREHEALTH = 0.00;
    static String texture = "images/shop-shield.png";
    static String categoryTexture = "images/category-button-better.png";

    /**
     *
     */
    public Shield() {
        super(NAME, PRICE, DESCRIPTION, ATTACKMULTI, DEFENCEMULTI, RESTOREHEALTH, texture, categoryTexture);
    }
}