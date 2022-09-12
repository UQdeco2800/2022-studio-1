package com.deco2800.game.components.shop.artefacts;

/**
 * BestLog child class of ShopBuilding
 */
public class BestLog extends ShopBuilding {
    static String NAME = "Best Log";
    static int STONEPRICE = 20;
    static int WOODPRICE = 5;
    static String DESCRIPTION = "Increases defence by 20%";
    static double ATTACKMULTI = 1.00;
    static double DEFENCEMULTI = 1.20;
    static String texture = "images/shop-log.png";
    static String categoryTexture = "images/building-category-button.png";

    /**
     * 
     */
    public BestLog() {
        super(NAME, STONEPRICE, WOODPRICE,DESCRIPTION, ATTACKMULTI, DEFENCEMULTI, texture, categoryTexture);
    }
}