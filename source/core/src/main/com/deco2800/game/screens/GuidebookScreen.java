package com.deco2800.game.screens;

import com.badlogic.gdx.Gdx;
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

import java.io.*;
import java.nio.Buffer;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

public class GuidebookScreen extends ScreenAdapter {
    private static final Logger logger = LoggerFactory.getLogger(GuidebookScreen.class);
    private final AtlantisSinks game;
    private final Renderer renderer;

    public static Page[] guideBookJSON;
    private Table guidebook;

    private final long delay = 5000000000L;
    private long currentTime;
    private long deltaTime;
    private long timeElapsed = 0;

    private static final String[] mainGameTextures = {
            "images/guidebook-open.png",
            "images/uiElements/exports/guidebook-cover.png"
    };

    public GuidebookScreen(AtlantisSinks game) {

        this.game = game;

        currentTime = TimeUtils.nanoTime();

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
        ServiceLocator.getEntityService().update();
        renderer.render();
    }

    @Override
    public void resize(int width, int height) {
        if (guidebook == null) {
            guidebook = ServiceLocator.getEntityService().getNamedEntity("guidebook").getComponent(GuidebookDisplay.class).displayBook();
            return;
        }
        renderer.resize(width, height);
        guidebook.remove();
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

        parse();
    }

    private void parse() {
        Gson gson = new Gson();
        BufferedReader buffer = null;
        try {
            buffer = Files.newBufferedReader(Paths.get("configs/guidebookcontent.json"));
        } catch (IOException e) {
            System.out.println("File not valid");
            return;
        }
        Page[] pages = gson.fromJson((Reader) buffer, Page[].class);
        System.out.println(pages[0].content);
    }
}
