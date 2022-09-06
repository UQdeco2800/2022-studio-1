package com.deco2800.game.components.shop.artefacts;

/**
 * Potion child class of equipment
 */
public class Potion extends Equipment {
    static String NAME = "Potion";
    static int PRICE = 20;
    static String DESCRIPTION = "Restore the main character's health value";
    static double ATTACKMULTI = 0.00;
    static double DEFENCEMULTI = 0.00;
    static double RESTOREHEALTH = 20.00;
    static String texture = "images/shop-health-potion.png";
    static String categoryTexture = "images/category-button-better.png";

    /**
     *
     */
    public Potion() {
        super(NAME, PRICE, DESCRIPTION, ATTACKMULTI, DEFENCEMULTI, RESTOREHEALTH,texture, categoryTexture);
    }
}
