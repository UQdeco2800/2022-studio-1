package com.deco2800.game.components.shop;

import com.deco2800.game.AtlantisSinks;
import com.deco2800.game.components.CombatStatsComponent;
import com.deco2800.game.components.Component;
import com.deco2800.game.components.player.InventoryComponent;
import com.deco2800.game.memento.CareTaker;
import com.deco2800.game.memento.Memento;
import com.deco2800.game.memento.Originator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * This class listens to events relevant to the Main Game Screen and does something when one of the
 * events is triggered.
 */
public class ShopActions extends Component {
    private static final Logger logger = LoggerFactory.getLogger(ShopActions.class);
    private AtlantisSinks game;
    private CareTaker playerStatus;

    public ShopActions(AtlantisSinks game, CareTaker playerStatus) {
        this.game = game;
        this.playerStatus = playerStatus;
    }

    @Override
    public void create() {
        entity.getEvents().addListener("exit", this::onExit);
    }

    /**
     * Swaps to the Main Menu screen.
     */
    private void onExit() {
        logger.info("Exiting shop screen");
        Originator currentStatus = new Originator(playerStatus.getAll().size());
        currentStatus.getStateFromMemento(playerStatus.get(playerStatus.getAll().size() - 1));
        currentStatus.setGold(entity.getComponent(InventoryComponent.class).getGold());
        currentStatus.setItems(entity.getComponent(InventoryComponent.class).getItems());
        playerStatus.add(currentStatus.saveStateToMemento());
        game.setScreen(AtlantisSinks.ScreenType.MAIN_GAME, playerStatus);
    }
}
