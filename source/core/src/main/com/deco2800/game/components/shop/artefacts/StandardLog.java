package com.deco2800.game.components.shop.artefacts;

/**
 * StandardLog child class of ShopBuilding
 */
public class StandardLog extends ShopBuilding {
    static String NAME = "Standard Log";
    static int STONEPRICE = 1;
    static int WOODPRICE = 100;
    static String DESCRIPTION = "Increases defence by 10%";
    static double ATTACKMULTI = 1.00;
    static double DEFENCEMULTI = 1.10;
    static String texture = "images/shop-log.png";
    static String categoryTexture = "images/building-category-button.png";

    /**
     * 
     */
    public StandardLog() {
        super(NAME, STONEPRICE, WOODPRICE,DESCRIPTION, ATTACKMULTI, DEFENCEMULTI, texture, categoryTexture);
    }
}
