package com.deco2800.game.memento;

import java.util.List;

public class Memento {
    private int state, gold, totalHealth, currentHealth;
    private List<String> items;

    public Memento(int state, int gold, int totalHealth, int currentHealth, List<String> items) {
        this.state = state;
        this.gold = gold;
        this.totalHealth = totalHealth;
        this.currentHealth = currentHealth;
        this.items = items;
    }

    public int getState(){
        return state;
    }

    public int getGold() {
        return gold;
    }

    public int getTotalHealth() {
        return totalHealth;
    }

    public int getCurrentHealth() {
        return currentHealth;
    }
    public List<String> getItemList() {
        return items;
    }
}
