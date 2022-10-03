package com.deco2800.game.components.shop;

import com.deco2800.game.AtlantisSinks;
import com.deco2800.game.components.CombatStatsComponent;
import com.deco2800.game.components.Component;
import com.deco2800.game.files.SaveGame;
import com.deco2800.game.rendering.Renderer;
import com.deco2800.game.components.player.InventoryComponent;
import com.deco2800.game.memento.CareTaker;
import com.deco2800.game.memento.Originator;
import com.deco2800.game.services.ServiceLocator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class listens to events relevant to the Main Game Screen and does
 * something when one of the
 * events is triggered.
 */
public class ShopActions extends Component {
    private static final Logger logger = LoggerFactory.getLogger(ShopActions.class);
    private AtlantisSinks game;
    private Renderer renderer;
    private CareTaker playerStatus;

    public ShopActions(AtlantisSinks game) {
        this.game = game;
        this.playerStatus = CareTaker.getInstance();
    }

    @Override
    public void create() {
        entity.getEvents().addListener("exit", this::onExit);
        entity.getEvents().addListener("mainShop", this::onMainShop);
        entity.getEvents().addListener("buildShop", this::onBuildShop);
        entity.getEvents().addListener("artefactShop", this::onArtefactShop);
        entity.getEvents().addListener("equipmentShop", this::onEquipmentShop);
    }

    /**
     * Swaps to the Main game screen. updates player status before exiting the shop
     */
    private void onExit() {
        logger.info("Exiting shop screen");
        if (game.getScreenType() != AtlantisSinks.ScreenType.SHOP) {
            saveStatus();
        }
        game.setScreen(AtlantisSinks.ScreenType.MAIN_GAME);
    }

    /**
     * Swaps to the Main Shop screen.
     */
    private void onMainShop() {
        logger.info("Entering main shop screen");
        saveStatus();
        game.setScreen(AtlantisSinks.ScreenType.SHOP);
    }

    /**
     * Saves the currently relevant status of the player based on the type of shop screen they are in
     */
    private void saveStatus() {
        Originator currentStatus = new Originator(playerStatus.size());
        currentStatus.getStateFromMemento(playerStatus.getLast());
        if (game.getScreenType() == AtlantisSinks.ScreenType.EQUIPMENT_SHOP) {
            currentStatus.setDefense(entity.getComponent(CombatStatsComponent.class).getBaseDefense());
            currentStatus.setAttack(entity.getComponent(CombatStatsComponent.class).getAttackMultiplier());
            currentStatus.setWeapon(entity.getComponent(InventoryComponent.class).getWeapon());
            currentStatus.setArmor(entity.getComponent(InventoryComponent.class).getArmor());
            currentStatus.setGold(entity.getComponent(InventoryComponent.class).getGold());
            currentStatus.setEquipmentsList(entity.getComponent(InventoryComponent.class).getEquipmentList());
        } else if (game.getScreenType() == AtlantisSinks.ScreenType.ARTEFACT_SHOP) {
            currentStatus.setItems(entity.getComponent(InventoryComponent.class).getItems());
            currentStatus.setGold(entity.getComponent(InventoryComponent.class).getGold());
        } else if (game.getScreenType() == AtlantisSinks.ScreenType.BUILD_SHOP) {
            currentStatus.setBuildings(entity.getComponent(InventoryComponent.class).getBuildings());
            currentStatus.setStone(entity.getComponent(InventoryComponent.class).getStone());
            currentStatus.setWood(entity.getComponent(InventoryComponent.class).getWood());
        }
        playerStatus.add(currentStatus.saveStateToMemento());
    }
    /**
     * Swaps to the Building Shop screen.
     */
    private void onBuildShop() {
        logger.info("Entering Build shop screen");
        game.setScreen(AtlantisSinks.ScreenType.BUILD_SHOP);
    }

    /**
     * Swaps to the Artefact Shop screen.
     */
    private void onArtefactShop() {
        logger.info("Entering Artefact shop screen");
        game.setScreen(AtlantisSinks.ScreenType.ARTEFACT_SHOP);
    }

    /**
     * Swaps to the Equipment Shop screen.
     */
    private void onEquipmentShop() {
        logger.info("Entering Equipment shop screen");
        game.setScreen(AtlantisSinks.ScreenType.EQUIPMENT_SHOP);
    }
}
