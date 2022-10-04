package com.deco2800.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Graphics;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.TimeUtils;
import com.deco2800.game.areas.GuidebookArea;
import com.deco2800.game.components.DayNightClockComponent;
import com.deco2800.game.components.Guidebook.GuidebookActions;
import com.deco2800.game.components.Guidebook.GuidebookDisplay;
import com.deco2800.game.components.Guidebook.GuidebookExitDisplay;
import com.deco2800.game.components.maingame.MainGameActions;
import com.deco2800.game.components.maingame.MainGameBuildingInterface;
import com.deco2800.game.components.maingame.MainGameExitDisplay;
import com.deco2800.game.components.maingame.MainGameInterface;
import com.deco2800.game.components.player.InventoryComponent;
import com.deco2800.game.components.settingsmenu.MusicSettings;
import com.deco2800.game.components.settingsmenu.SettingsMenuDisplay;
import com.deco2800.game.entities.configs.StructureConfig;
import com.deco2800.game.files.FileLoader;
import com.deco2800.game.memento.Memento;
import com.deco2800.game.services.configs.Page;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.deco2800.game.AtlantisSinks;
import com.deco2800.game.areas.MainArea;
import com.deco2800.game.components.gamearea.PerformanceDisplay;
import com.deco2800.game.components.shop.CommonShopComponents;
import com.deco2800.game.entities.Entity;
import com.deco2800.game.entities.EntityService;
import com.deco2800.game.entities.factories.RenderFactory;
import com.deco2800.game.input.InputComponent;
import com.deco2800.game.input.InputDecorator;
import com.deco2800.game.input.InputService;
import com.deco2800.game.memento.CareTaker;
import com.deco2800.game.rendering.RenderService;
import com.deco2800.game.rendering.Renderer;
import com.deco2800.game.services.GameTime;
import com.deco2800.game.services.ResourceService;
import com.deco2800.game.services.ServiceLocator;
import com.deco2800.game.ui.terminal.Terminal;
import com.deco2800.game.ui.terminal.TerminalDisplay;
import com.google.gson.Gson;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import static java.util.concurrent.TimeUnit.*;


public class GuidebookScreen extends ScreenAdapter {
    private static final Logger logger = LoggerFactory.getLogger(GuidebookScreen.class);
    private final AtlantisSinks game;
    private final Renderer renderer;
    private static Table[] guidebook;

    public static int renderTrigger = 0;

    private static final String[] mainGameTextures = {
            "images/guidebook-open.png",
            "images/uiElements/exports/guidebook-cover.png",
            "images/uiElements/exports/guidebook-opening.png",
            "images/uiElements/exports/guidebook-next.png",
            "images/uiElements/exports/guidebook-previous.png",
            "images/uiElements/exports/guidebook-heading-frame.png",
            "images/uiElements/exports/crystal.png",
            "images/Centaur_Back_right.png",
            "images/clock_sprites/clock_day1_1.png",
            "images/uiElements/exports/allEnemies.png",
            "images/uiElements/exports/allBuildings.png",
            "images/shop-quarry.png",
            "images/uiElements/exports/allCurrencies.png",
            "images/Shop.png",
            "images/inventory.png",
            "images/Achievements.png"
    };

    public GuidebookScreen(AtlantisSinks game) {

        this.game = game;

        logger.debug("Initialising guidebook screen services");
        ServiceLocator.registerTimeSource(new GameTime());

        ServiceLocator.registerInputService(new InputService());
        ServiceLocator.registerResourceService(new ResourceService());
        ServiceLocator.registerEntityService(new EntityService());
        ServiceLocator.registerRenderService(new RenderService());


        renderer = RenderFactory.createRenderer();
        MainArea.getInstance().setMainArea(new GuidebookArea());

        loadAssets();
        createUI();

        logger.debug("Initialising main game screen entities");
    }

    @Override
    public void render(float delta) {
        if (renderTrigger == 1) {
            guidebook = ServiceLocator.getEntityService().getNamedEntity("guidebook").getComponent(GuidebookDisplay.class).getGuidebook();
            for (Table table: guidebook) {
                if (table != null) {
                    table.remove();
                }
            }
            switch (GuidebookDisplay.bookStatus) {
                case CLOSED -> GuidebookDisplay.bookStatus = GuidebookStatus.OPENING;
                case OPENING, FLICK_NEXT, FLICK_PREVIOUS -> GuidebookDisplay.bookStatus = GuidebookStatus.OPEN;
            }
            guidebook = ServiceLocator.getEntityService().getNamedEntity("guidebook").getComponent(GuidebookDisplay.class).displayBook();
            renderTrigger = 0;
        }
        ServiceLocator.getEntityService().update();
        renderer.render();
    }

    @Override
    public void resize(int width, int height) {
        renderer.resize(width, height);
        guidebook = ServiceLocator.getEntityService().getNamedEntity("guidebook").getComponent(GuidebookDisplay.class).getGuidebook();
        for (Table table: guidebook) {
            if (table == null) {
                continue;
            } else {
                table.remove();
            }
        }
        guidebook = ServiceLocator.getEntityService().getNamedEntity("guidebook").getComponent(GuidebookDisplay.class).displayBook();
        logger.trace("Resized renderer: ({} x {})", width, height);
    }

    @Override
    public void pause() {
        logger.info("Game paused");
    }

    @Override
    public void resume() {
        logger.info("Game resumed");
    }

    @Override
    public void dispose() {
        logger.debug("Disposing guidebook screen");
        renderer.dispose();
        unloadAssets();

        ServiceLocator.getEntityService().dispose();
        ServiceLocator.getRenderService().dispose();
        ServiceLocator.getResourceService().dispose();

        ServiceLocator.clear();
    }

    private void loadAssets() {
        logger.debug("Loading assets");
        ResourceService resourceService = ServiceLocator.getResourceService();
        ServiceLocator.getResourceService().loadAll();
        resourceService.loadTextures(mainGameTextures);
        ServiceLocator.getResourceService().loadAll();
    }

    private void unloadAssets() {
        logger.debug("Unloading assets");
        ResourceService resourceService = ServiceLocator.getResourceService();
        resourceService.unloadAssets(mainGameTextures);
    }

    /**
     * Creates the main game's ui including components for rendering ui elements to
     * the screen and
     * capturing and handling ui input.
     */
    private void createUI() {
        logger.debug("Creating ui");
        Stage stage = ServiceLocator.getRenderService().getStage();
        InputComponent inputComponent = ServiceLocator.getInputService().getInputFactory().createForTerminal();
        Memento lastStatus = CareTaker.getInstance().getLast();

        Entity ui = new Entity();
        ui.addComponent(new GuidebookDisplay()).addComponent(new InputDecorator(stage, 10));
        ui.addComponent(new GuidebookActions(game)).addComponent(new InputDecorator(stage, 9));
        ui.addComponent(new GuidebookExitDisplay()).addComponent(new InputDecorator(stage, 11));
        ServiceLocator.getEntityService().registerNamed("guidebook", ui);

        guidebook = ServiceLocator.getEntityService().getNamedEntity("guidebook").getComponent(GuidebookDisplay.class).getGuidebook();

        ScheduledExecutorService opening = Executors.newSingleThreadScheduledExecutor();

        Runnable openingTask = () -> {
            GuidebookScreen.renderTrigger = 1;
            Gdx.graphics.requestRendering();

            ScheduledExecutorService open = Executors.newSingleThreadScheduledExecutor();
            Runnable openTask = () -> {
                GuidebookScreen.renderTrigger = 1;
                Gdx.graphics.requestRendering();
            };

            open.schedule(openTask, 250, MILLISECONDS);
        };

        opening.schedule(openingTask, 750, MILLISECONDS);
    }

}
