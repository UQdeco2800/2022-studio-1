package com.deco2800.game.components.Guidebook;

import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.deco2800.game.AtlantisSinks;
import com.deco2800.game.components.CombatStatsComponent;
import com.deco2800.game.components.Component;
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
public class GuidebookActions extends Component {
    private static final Logger logger = LoggerFactory.getLogger(GuidebookActions.class);
    private AtlantisSinks game;
    private Renderer renderer;
    private CareTaker playerStatus;
    public int currentPage;

    public GuidebookActions(AtlantisSinks game) {
        this.game = game;
        this.playerStatus = CareTaker.getInstance();
    }

    @Override
    public void create() {
        entity.getEvents().addListener("exit", this::onExit);
        entity.getEvents().addListener("mainGuidebook", this::onMainGuidebook);
        entity.getEvents().addListener("backPage", this::backPage);
        entity.getEvents().addListener("nextPage", this::nextPage);

        
    }
    private void nextPage() {
        int currentPage = GuidebookDisplay.currentPage;
        int proposedNextPage = GuidebookDisplay.currentPage + 2;
        GuidebookDisplay.currentPage = (proposedNextPage < GuidebookDisplay.maxPages) ? proposedNextPage : currentPage;
        Table[] guidebook = ServiceLocator.getEntityService().getNamedEntity("guidebook").getComponent(GuidebookDisplay.class).getGuidebook();
        for (Table table: guidebook) {
            table.remove();
        }
        ServiceLocator.getEntityService().getNamedEntity("guidebook").getComponent(GuidebookDisplay.class).displayBook();
    }

    private void backPage() {
        currentPage = GuidebookDisplay.currentPage;
        int proposedBackPage = GuidebookDisplay.currentPage - 2;
        GuidebookDisplay.currentPage = (proposedBackPage >= 0) ? proposedBackPage : currentPage;

        Table[] guidebook = ServiceLocator.getEntityService().getNamedEntity("guidebook").getComponent(GuidebookDisplay.class).getGuidebook();
        for (Table table: guidebook) {
            table.remove();
        }
        ServiceLocator.getEntityService().getNamedEntity("guidebook").getComponent(GuidebookDisplay.class).displayBook();
    }


    /**
     * Swaps to the Main game screen. updates player status before exiting the shop
     */
    private void onExit() {
        logger.info("Exiting guidebook screen");
        game.setScreen(AtlantisSinks.ScreenType.MAIN_GAME);
    }

    /**
     * Swaps to the Main Guidebook screen.
     */
    private void onMainGuidebook() {
        logger.info("Entering main Guidebook screen");
        saveStatus();
        game.setScreen(AtlantisSinks.ScreenType.GUIDEBOOK);

    }

    /**
     * Saves the currently relevant status of the player based on the type of shop screen they are in
     */
    private void saveStatus() {
        Originator currentStatus = new Originator(playerStatus.size());
        currentStatus.getStateFromMemento(playerStatus.getLast());
        currentStatus.setGold(entity.getComponent(InventoryComponent.class).getGold());
        currentStatus.setItems(entity.getComponent(InventoryComponent.class).getItems());
        currentStatus.setStone(entity.getComponent(InventoryComponent.class).getStone());
        currentStatus.setStone(entity.getComponent(InventoryComponent.class).getWood());

        playerStatus.add(currentStatus.saveStateToMemento());
    }

}
