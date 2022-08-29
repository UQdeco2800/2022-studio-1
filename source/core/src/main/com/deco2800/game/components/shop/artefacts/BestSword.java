package com.deco2800.game.components.shop.artefacts;

/**
 * BestSword child class of Artefact
 */
public class BestSword extends Artefact {
    static String NAME = "Best Sword";
    static int PRICE = 20;
    static String DESCRIPTION = "Increases attack by 20%";
    static double ATTACKMULTI = 1.20;
    static double DEFENCEMULTI = 1.00;
    static String TEXTURE = "images/shop-sword.png";
    static String CATEGORYTEXTURE = "images/category-button-best.png";

    /**
     * 
     */
    public BestSword() {
        super(NAME, PRICE, DESCRIPTION, ATTACKMULTI, DEFENCEMULTI, TEXTURE, CATEGORYTEXTURE);
    }
}
