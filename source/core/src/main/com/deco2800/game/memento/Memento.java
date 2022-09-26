package com.deco2800.game.memento;

import com.deco2800.game.components.shop.artefacts.Artefact;
import com.deco2800.game.components.shop.equipments.Equipments;

import java.util.HashMap;
import java.util.List;

/**
 * the object that is maintaining the state of the originator. Memento can only
 * be created, not changed, this is to
 * ensure the states of the player is properly saved and have no possibility of
 * being altered, any changes to the player
 * state should be saved into another memento
 */
public class Memento {
    private int state, gold, stone, wood, attack, currentHealth, defense;
    private Equipments weapon;
    private Equipments armor;
    private HashMap<Artefact, Integer> items;
    private List<Equipments> equipmentsList;

    /**
     * constructor for a new memento
     * 
     * @param state         - the id of the saved memento
     * @param gold          - amount of gold that the player currently holds
     * @param stone         - amount of stone that the player currently holds
     * @param wood         - amount of wood that the player currently holds
     * @param currentHealth - amount of health that the player currently has
     * @param items         - list of items the player have
     * @param attack        - base attack value of the player (including weapon
     *                      boost)
     * @param defense       - defense multiplier from armor
     * @param weapon        - current weapon
     * @param armor         - current armor
     *
     */
    public Memento(int state, int gold, int stone, int wood, int currentHealth, HashMap<Artefact, Integer> items, int attack
                   , int defense, Equipments weapon, Equipments armor, List<Equipments> equipmentsList) {
        this.state = state;
        this.gold = gold;
        this.stone = stone;
        this.wood = wood;
        this.currentHealth = currentHealth;
        this.items = items;
        this.attack = attack;
        this.defense = defense;
        this.weapon = weapon;
        this.armor = armor;
        this.equipmentsList = equipmentsList;
    }

    /**
     * retrieve id of the memento
     * 
     * @return - id of the memento
     */
    public int getState() {
        return state;
    }

    /**
     * retrieve the gold amount saved in the memento
     * 
     * @return - gold amount
     */
    public int getGold() {
        return gold;
    }

    /**
     * retrieve the stone amount saved in the memento
     * 
     * @return - stone amount
     */
    public int getStone() {
        return stone;
    }

    /**
     * retrieve the wood amount saved in the memento
     * 
     * @return - wood amount
     */
    public int getWood() {
        return wood;
    }

    /**
     * retrieve the health value saved in the memento
     * 
     * @return - health value
     */
    public int getCurrentHealth() {
        return currentHealth;
    }

    /**
     * retrieve the attack value saved in the memento
     * 
     * @return - base attack value
     */
    public int getAttack() {
        return attack;
    }

    /**
     * retrieve the defense multiplier in the memento
     * @return - defense multiplier
     */
    public int getDefense() {
        return defense;
    }

    /**
     * retrieve the weapon that the player currently has
     * @return - currently held weapon
     */
    public Equipments getWeapon() {
        return weapon;
    }

    /**
     * retrieve the armor that the player currently has
     * @return - current armor
     */
    public Equipments getArmor() {return armor;}
    /**
     * retrieve the list of artefact items saved in the memento
     * 
     * @return - list of artefact items
     */
    public HashMap<Artefact, Integer> getItemList() {
        return items;
    }

    public List<Equipments> getEquipmentsList() {
        return equipmentsList;
    }
}
