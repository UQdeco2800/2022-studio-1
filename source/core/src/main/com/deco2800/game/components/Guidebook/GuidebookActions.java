package com.deco2800.game.components.Guidebook;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.deco2800.game.AtlantisSinks;
import com.deco2800.game.components.Component;
import com.deco2800.game.components.player.InventoryComponent;
import com.deco2800.game.memento.CareTaker;
import com.deco2800.game.memento.Originator;
import com.deco2800.game.rendering.Renderer;
import com.deco2800.game.screens.GuidebookScreen;
import com.deco2800.game.screens.GuidebookStatus;
import com.deco2800.game.services.AchievementHandler;
import com.deco2800.game.services.ServiceLocator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.lang.Thread;

import static java.util.concurrent.TimeUnit.MILLISECONDS;

/**
 * This class listens to events relevant to the Main Game Screen and does
 * something when one of the
 * events is triggered.
 */
public class GuidebookActions extends Component {
    private static final String GUIDEBOOK = "guidebook";

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
        int proposedNextPage = GuidebookDisplay.currentPage + 2;
        if (proposedNextPage >= GuidebookDisplay.MAX_PAGES) {
            return;
        }
        GuidebookDisplay.currentPage = proposedNextPage;
        Table[] guidebook = ServiceLocator.getEntityService().getNamedEntity(GUIDEBOOK)
                .getComponent(GuidebookDisplay.class).getGuidebook();
        for (Table table : guidebook) {
            table.remove();
        }
        GuidebookDisplay.bookStatus = GuidebookStatus.FLICK_NEXT;
        ServiceLocator.getEntityService().getNamedEntity(GUIDEBOOK).getComponent(GuidebookDisplay.class)
                .displayBook();

        ScheduledExecutorService flicking = Executors.newSingleThreadScheduledExecutor();

        Runnable flickTask = () -> {
            GuidebookScreen.renderTrigger = 1;
            Gdx.graphics.requestRendering();
        };

        Sound clickSound = Gdx.audio.newSound(
                Gdx.files.internal("sounds/mouse_click.mp3"));
        clickSound.play();

        try {
            Thread.sleep(200);
        } catch (InterruptedException e) {}

        Sound flipSound = Gdx.audio.newSound(
                Gdx.files.internal("sounds/book_flip.mp3"));
        flipSound.play();

        flicking.schedule(flickTask, 250, MILLISECONDS);
    }

    private void backPage() {
        currentPage = GuidebookDisplay.currentPage;
        int proposedBackPage = GuidebookDisplay.currentPage - 2;
        if (proposedBackPage < 0) {
            return;
        }
        GuidebookDisplay.currentPage = proposedBackPage;

        Table[] guidebook = ServiceLocator.getEntityService().getNamedEntity(GUIDEBOOK)
                .getComponent(GuidebookDisplay.class).getGuidebook();
        for (Table table : guidebook) {
            table.remove();
        }
        GuidebookDisplay.bookStatus = GuidebookStatus.FLICK_PREVIOUS;
        ServiceLocator.getEntityService().getNamedEntity(GUIDEBOOK).getComponent(GuidebookDisplay.class)
                .displayBook();

        ScheduledExecutorService flicking = Executors.newSingleThreadScheduledExecutor();

        Runnable flickTask = () -> {
            GuidebookScreen.renderTrigger = 1;
            Gdx.graphics.requestRendering();
        };

        Sound clickSound = Gdx.audio.newSound(
                Gdx.files.internal("sounds/mouse_click.mp3"));
        clickSound.play();

        try {
            Thread.sleep(200);
        } catch (InterruptedException e) {}

        Sound flipSound = Gdx.audio.newSound(
                Gdx.files.internal("sounds/book_flip.mp3"));
        flipSound.play();

        flicking.schedule(flickTask, 250, MILLISECONDS);
    }

    /**
     * Swaps to the Main game screen. updates player status before exiting the shop
     */
    private void onExit() {
        logger.info("Exiting guidebook screen");
        ServiceLocator.getAchievementHandler().getEvents().trigger(AchievementHandler.EVENT_GUIDEBOOK_CLOSED);
        game.setScreen(AtlantisSinks.ScreenType.MAIN_MENU);
        GuidebookDisplay.bookStatus = GuidebookStatus.CLOSED;
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
     * Saves the currently relevant status of the player based on the type of shop
     * screen they are in
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
