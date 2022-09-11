package com.deco2800.game.components.shop.artefacts;

/**
 * Bow child class of equipment
 */
public class Bow extends Equipment {
    static String NAME = "Bow";
    static int PRICE = 25;
    static String DESCRIPTION = "Long-range attack weapons";
    static double ATTACKMULTI = 1.00;
    static double DEFENCEMULTI = 0.50;
    static double RESTOREHEALTH = 0.00;
    static String texture = "images/shop-bow-and-arrow.png";
    static String categoryTexture = "images/category-button-better.png";

    /**
     *
     */
    public Bow() {
        super(NAME, PRICE, DESCRIPTION, ATTACKMULTI, DEFENCEMULTI, RESTOREHEALTH, texture, categoryTexture);
    }
}