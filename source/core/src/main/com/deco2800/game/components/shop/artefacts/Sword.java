package com.deco2800.game.components.shop.artefacts;

public class Sword extends Artefact {
    static int PRICE = 5;
    static String DESCRIPTION = "The sword increases attack by 10%";
    static double ATTACKMULTI = 1.10;
    static double DEFENCEMULTI = 1.00;
    public String texture;
    

    /**
     * 
     */
    public Sword() {
        super(PRICE, DESCRIPTION, ATTACKMULTI, DEFENCEMULTI);
        this.texture = "images/shop-sword.png";
    }
}
