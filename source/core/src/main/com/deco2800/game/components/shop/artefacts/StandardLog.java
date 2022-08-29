package com.deco2800.game.components.shop.artefacts;

public class StandardLog extends ShopBuilding {
    static String NAME = "Standard Log";
    static int PRICE = 5;
    static String DESCRIPTION = "Increases defence by 10%";
    static double ATTACKMULTI = 1.00;
    static double DEFENCEMULTI = 1.10;
    static String texture = "images/shop-log.png";
    static String categoryTexture = "images/building-category-button.png";

    /**
     * 
     */
    public StandardLog() {
        super(NAME, PRICE, DESCRIPTION, ATTACKMULTI, DEFENCEMULTI, texture, categoryTexture);
    }
}
