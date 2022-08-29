package com.deco2800.game.components.shop.artefacts;

/**
 * BetterSword child class of Artefact
 */
public class BetterSword extends Artefact {
    static String NAME = "Better Sword";
    static int PRICE = 10;
    static String DESCRIPTION = "Increases attack by 15%";
    static double ATTACKMULTI = 1.15;
    static double DEFENCEMULTI = 1.00;
    static String texture = "images/shop-sword.png";
    static String categoryTexture = "images/category-button-better.png";

    /**
     * 
     */
    public BetterSword() {
        super(NAME, PRICE, DESCRIPTION, ATTACKMULTI, DEFENCEMULTI, texture, categoryTexture);
    }
}
