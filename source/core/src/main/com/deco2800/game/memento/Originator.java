package com.deco2800.game.memento;

import java.util.List;

import com.deco2800.game.components.CombatStatsComponent;
import com.deco2800.game.components.shop.artefacts.Artefact;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Originator {
    private static final Logger logger = LoggerFactory.getLogger(Originator.class);
    protected int state, gold, totalHealth, currentHealth, attack;
    protected List<Artefact> items;

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

    public void setAttack(int attack) {
        this.attack = attack;
    }

    public int getAttack() {
        return attack;
    }

    public int getCurrentHealth() {
        return currentHealth;
    }

    public void setCurrentHealth(int health) {
        currentHealth = health;
    }
    public List<Artefact> getItemList() {
        return items;
    }

    public void setItems(List<Artefact> items) {
        this.items = items;
    }

    public Memento saveStateToMemento(){
        return new Memento(state, gold, currentHealth, items, attack);
    }

    public void getStateFromMemento(Memento memento){
        state = memento.getState();
        gold = memento.getGold();
        currentHealth = memento.getCurrentHealth();
        items = memento.getItemList();
    }

    @Override
    public String toString() {
        return "State " + state + " : Current Health = " + currentHealth + ", Base Attack Value: " + attack +
                ", gold: " + gold + " , items in inventory: " + items;
    }
}
