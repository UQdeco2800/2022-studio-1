package com.deco2800.game.memento;

import com.deco2800.game.components.shop.artefacts.Artefact;

import java.util.List;

/**
 * the object that is maintaining the state of the originator. Memento can only be created, not changed, this is to
 * ensure the states of the player is properly saved and have no possibility of being altered, any changes to the player
 * state should be saved into another memento
 */
public class Memento {
    private int state, gold, attack, currentHealth;
    private List<Artefact> items;

    /**
     * constructor for a new memento
     * @param state - the id of the saved memento
     * @param gold - amount of gold that the player currently holds
     * @param currentHealth - amount of health that the player currently has
     * @param items - list of items the player have
     * @param attack - base attack value of the player (including weapon boost)
     */
    public Memento(int state, int gold, int currentHealth, List<Artefact> items, int attack) {
        this.state = state;
        this.gold = gold;
        this.currentHealth = currentHealth;
        this.items = items;
        this.attack = attack;
    }

    /**
     * retrieve id of the memento
     * @return - id of the memento
     */
    public int getState(){
        return state;
    }

    /**
     * retrieve the gold amount saved in the memento
     * @return - gold amount
     */
    public int getGold() {
        return gold;
    }

    /**
     * retrieve the health value saved in the memento
     * @return - health value
     */
    public int getCurrentHealth() {
        return currentHealth;
    }

    /**
     * retrieve the attack value saved in the memento
     * @return - base attack value
     */
    public int getAttack() {
        return attack;
    }

    /**
     * retrieve the list of artefact items saved in the memento
     * @return - list of artefact items
     */
    public List<Artefact> getItemList() {
        return items;
    }
}
