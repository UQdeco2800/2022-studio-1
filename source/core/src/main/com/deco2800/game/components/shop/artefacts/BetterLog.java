package com.deco2800.game.components.shop.artefacts;

/**
 * BetterLog child class of ShopBuilding
 */
public class BetterLog extends ShopBuilding {
    static String NAME = "Better Log";
    static int PRICE = 10;
    static String DESCRIPTION = "Increases defence by 15%";
    static double ATTACKMULTI = 1.00;
    static double DEFENCEMULTI = 1.15;
    static String texture = "images/shop-log.png";
    static String categoryTexture = "images/building-category-button.png";

    /**
     * 
     */
    public BetterLog() {
        super(NAME, PRICE, DESCRIPTION, ATTACKMULTI, DEFENCEMULTI, texture, categoryTexture);
    }
}