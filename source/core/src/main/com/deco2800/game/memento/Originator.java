package com.deco2800.game.memento;

import java.util.List;

import com.deco2800.game.components.CombatStatsComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Originator {
    private static final Logger logger = LoggerFactory.getLogger(Originator.class);
    protected int state, gold, totalHealth, currentHealth;
    protected List<String> items;

    public Originator(int state) {
        this.state = state;
    }

    public void setState(int state){
        this.state = state;
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
        currentHealth = health;
    }
    public List<String> getItemList() {
        return items;
    }

    public void setItems(List<String> items) {
        this.items = items;
    }

    public Memento saveStateToMemento(){
        return new Memento(state, gold, totalHealth, currentHealth, items);
    }

    public void getStateFromMemento(Memento memento){
        state = memento.getState();
        gold = memento.getGold();
        totalHealth = memento.getTotalHealth();
        currentHealth = memento.getCurrentHealth();
        items = memento.getItemList();
    }

    @Override
    public String toString() {
        return "State " + state + " : Current Health = " + currentHealth + " out of Total Health = " + totalHealth +
                ", gold: " + gold + " , items in inventory: " + items;
    }
}
