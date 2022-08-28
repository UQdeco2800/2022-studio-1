package com.deco2800.game.memento;

import com.deco2800.game.components.shop.artefacts.Artefact;

import java.util.List;

public class Memento {
    private int state, gold, attack, currentHealth;
    private List<Artefact> items;

    public Memento(int state, int gold, int currentHealth, List<Artefact> items, int attack) {
        this.state = state;
        this.gold = gold;
        this.currentHealth = currentHealth;
        this.items = items;
        this.attack = attack;
    }

    public int getState(){
        return state;
    }

    public int getGold() {
        return gold;
    }

    public int getCurrentHealth() {
        return currentHealth;
    }

    public int getAttack() {
        return attack;
    }
    public List<Artefact> getItemList() {
        return items;
    }
}
