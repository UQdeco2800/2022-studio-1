package com.deco2800.game.areas;

public class MainArea {

    private GameArea gameArea;

    private static MainArea mainArea;

    private MainArea() {

    }

    public static MainArea getInstance() {
        if (null == mainArea) {
            mainArea =  new MainArea();
        }
        return mainArea;
    }

    public void setMainArea(GameArea gameArea) {
        this.gameArea = gameArea;
        gameArea.create();
    }

    public GameArea getGameArea() {
        return gameArea;
    }
}
