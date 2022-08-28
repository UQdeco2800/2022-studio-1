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

    public void setGold(int gold) {
        this.gold = gold;
    }
    public int getTotalHealth() {
        return totalHealth;
    }

    public void setTotalHealth(int health) {
        this.totalHealth = health;
    }

    public int getCurrentHealth() {
        return currentHealth;
    }

    public void setCurrentHealth(int health) {
        this.currentHealth = health;
    }
    public List<String> getItemList() {
        return items;
    }

    public void setItems(List<String> items) {
        this.items = items;
    }
}
