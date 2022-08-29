package com.deco2800.game.components.shop.artefacts;

public class BestLog extends ShopBuilding {
    static String NAME = "Best Log";
    static int PRICE = 20;
    static String DESCRIPTION = "Increases defence by 20%";
    static double ATTACKMULTI = 1.00;
    static double DEFENCEMULTI = 1.20;
    static String texture = "images/shop-log.png";
    static String categoryTexture = "images/building-category-button.png";

    /**
     * 
     */
    public BestLog() {
        super(NAME, PRICE, DESCRIPTION, ATTACKMULTI, DEFENCEMULTI, texture, categoryTexture);
    }
}