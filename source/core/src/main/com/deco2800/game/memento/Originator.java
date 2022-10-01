package com.deco2800.game.memento;

import java.util.HashMap;
import java.util.List;
import com.deco2800.game.components.shop.artefacts.Artefact;
import com.deco2800.game.components.shop.artefacts.ShopBuilding;
import com.deco2800.game.components.shop.equipments.Equipments;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Part of the memento design pattern, originator is the object to store the
 * states of the player, this includes the
 * player's inventory states (e.g. amount of gold, type of sword, items in
 * inventory, health and base attack value
 *
 * Can be extended to save states for other entities in the future or use as
 * checkpoints for player to respawn
 */
public class Originator {
    private static final Logger logger = LoggerFactory.getLogger(Originator.class);
    protected int state, gold, stone, wood, currentHealth, attack, defense;
    protected Equipments weapon;
    protected Equipments armor;
    protected HashMap<Artefact, Integer> items;
    protected HashMap<ShopBuilding, Integer> buildings;
    protected List<Equipments> equipmentsList;

    /**
     * Originator constructor which can create a new originator to store the current
     * state of the player before entering
     * and exiting the shop
     * 
     * @param state - the id of the current player state
     */
    public Originator(int state) {
        this.state = state;
    }

    public int getState() {
        return state;
    }

    /**
     * returns the weapon enum of the previously saved record
     * @return weapon enum
     */
    public Equipments getWeapon() {
        return weapon;
    }

    /**
     * returns the armor enum of the previously saved record
     * @return armor enum
     */
    public Equipments getArmor() {return armor;}

    /**
     *  sets the weapon enum
     * @param weapon - weapon to be stored
     */
    public void setWeapon(Equipments weapon) {
        this.weapon = weapon;
    }

    /**
     *  sets the armor enum
     * @param armor - chestplate to be stored
     */
    public void setArmor(Equipments armor) {
        this.armor = armor;
    }

    /**
     * retrieve the gold status for player
     * 
     * @return amount of gold currently held by the player
     */
    public int getGold() {
        return gold;
    }

    /**
     * saves the amount of gold that the player currently have
     * 
     * @param gold - amount of gold that the player current holds
     */
    public void setGold(int gold) {
        this.gold = gold;
    }

    /**
     * saves the amount of gold that the player currently have
     * 
     * @param wood - amount of gold that the player current holds
     */
    public void setWood(int wood) {
        this.wood = wood;
    }

    /**
     * retrieve the stone status for player
     * 
     * @return amount of stone currently held by the player
     */
    public int getStone() {
        return stone;
    }

    /**
     * retrieve the wood status for player
     * 
     * @return amount of wood currently held by the player
     */
    public int getWood() {
        return wood;
    }

    /**
     * saves the amount of stone that the player currently have
     * 
     * @param stone - amount of stone that the player current holds
     */
    public void setStone(int stone) {
        this.stone = stone;
    }

    /**
     * saves the attack value for the player, this includes the boost for player
     * from different weapons bought in the
     * shop
     * 
     * @param attack - attack value for player
     */
    public void setAttack(int attack) {
        this.attack = attack;
    }

    /**
     * returns the attack value of the player
     * 
     * @return - basee attack value of player
     */
    public int getAttack() {
        return attack;
    }

    /**
     * sets the defense multiplier
     * @param defense - defense multiplier value
     */
    public void setDefense(int defense) {
        this.defense = defense;
    }

    /**
     * returns the defense multiplier
     */
    public int getDefense() {
        return defense;
    }
    /**
     * returns the current health of the player
     * 
     * @return - current health of the player
     */
    public int getCurrentHealth() {
        return currentHealth;
    }

    /**
     * saves the current health of the player
     * 
     * @param health - player health
     */
    public void setCurrentHealth(int health) {
        currentHealth = health;
    }

    /**
     * retrieve the list of items in player's inventory, may be extended to
     * different lists depending on types of item
     * 
     * @return - the list of artefacts items in player's inventory
     */
    public HashMap<Artefact, Integer> getItemList() {
        return items;
    }

    /**
     * stores the current state of player's inventory items
     * 
     * @param items - list of artefacts items in player's inventory
     */
    public void setItems(HashMap<Artefact, Integer> items) {
        this.items = items;
    }

    public List<Equipments> getEquipmentsList() {
        return equipmentsList;
    }

    public void setEquipmentsList(List<Equipments> equipmentsList) {
        this.equipmentsList = equipmentsList;
    }

    public void setBuildings(HashMap<ShopBuilding, Integer> buildings) {
        this.buildings = buildings;
    }

    public HashMap<ShopBuilding, Integer> getBuildings() {
        return buildings;
    }

    /**
     * converts the originator object to a memento to store in the caretaker
     * 
     * @return - the new memento generated from the current originator
     */
    public Memento saveStateToMemento() {
        return new Memento(state, gold, stone, wood, currentHealth, items, buildings,
                attack, defense, weapon, armor, equipmentsList);
    }

    /**
     * returns the states of the player from the specified memento
     * 
     * @param memento - the memento of the previous player state to read from
     */
    public void getStateFromMemento(Memento memento) {
        gold = memento.getGold();
        stone = memento.getStone();
        wood = memento.getWood();
        currentHealth = memento.getCurrentHealth();
        items = memento.getItemList();
        attack = memento.getAttack();
        weapon = memento.getWeapon();
        armor = memento.getArmor();
        equipmentsList = memento.getEquipmentsList();
    }

    @Override
    public String toString() {
        return "State " + state + " : Current Health = " + currentHealth + ", Attack Value: " + attack +
                ", Defense Value: " + defense + ", gold: " + gold + " , stone: " + stone + "  , wood: " + wood +
                " , items in inventory: " + items + ", weapon: " + weapon + ", armor: " + armor +
                ", equipments list: " + equipmentsList;
    }
}
