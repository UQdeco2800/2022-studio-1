package com.deco2800.game.components.shop.artefacts;

/**
 * StandardSword child class of Artefact
 */
public class StandardSword extends Artefact {
    static String NAME = "Standard Sword";
    static int PRICE = 5;
    static String DESCRIPTION = "Increases attack by 10%";
    static double ATTACKMULTI = 1.10;
    static double DEFENCEMULTI = 1.00;
    static String texture = "images/shop-sword.png";
    static String categoryTexture = "images/category-button-standard.png";

    /**
     * 
     */
    public StandardSword() {
        super(NAME, PRICE, DESCRIPTION, ATTACKMULTI, DEFENCEMULTI, texture, categoryTexture);
    }
}
