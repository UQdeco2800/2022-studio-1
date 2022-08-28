package com.deco2800.game.components.shop;

import com.deco2800.game.AtlantisSinks;
import com.deco2800.game.components.Component;
import com.deco2800.game.memento.CareTaker;
import com.deco2800.game.memento.Memento;
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
    private Memento currentStatus;

    public ShopActions(AtlantisSinks game, CareTaker playerStatus) {
        this.game = game;
        this.playerStatus = playerStatus;
        this.currentStatus = playerStatus.get(playerStatus.getAll().size() - 1);
    }

    @Override
    public void create() {
        entity.getEvents().addListener("exit", this::onExit);
        entity.getEvents().addListener("buy", this::onBuy);
    }

    /**
     * Swaps to the Main Menu screen.
     */
    private void onExit() {
        logger.info("Exiting shop screen");
        System.out.println(currentStatus.getGold());
        System.out.println(currentStatus.getItemList());
        playerStatus.add(currentStatus);
        game.setScreen(AtlantisSinks.ScreenType.MAIN_GAME, playerStatus);
    }

    private void onBuy() {
        logger.info("Buying item");
        currentStatus.setGold(currentStatus.getGold() - 10);
        List<String> items = currentStatus.getItemList();
        items.add("Dummy Artefact");
        currentStatus.setItems(items);
    }
}
