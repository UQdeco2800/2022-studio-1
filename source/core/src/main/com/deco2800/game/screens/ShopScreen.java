package com.deco2800.game.screens;

import com.deco2800.game.components.player.InventoryComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.deco2800.game.AtlantisSinks;
import com.deco2800.game.components.gamearea.PerformanceDisplay;
import com.deco2800.game.components.shop.CommonShopComponents;
import com.deco2800.game.components.shop.ShopActions;
import com.deco2800.game.components.shop.ShopReturn;
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

public class ShopScreen extends ScreenAdapter {
    private static final Logger logger = LoggerFactory.getLogger(ShopScreen.class);
    private final AtlantisSinks game;
    private final Renderer renderer;
    private CareTaker playerStatus;

    public ShopScreen(AtlantisSinks game, CareTaker playerStatus) {
        this.game = game;
        this.playerStatus = playerStatus;

        logger.debug("Initialising shop screen services");
        ServiceLocator.registerTimeSource(new GameTime());

        ServiceLocator.registerInputService(new InputService());
        ServiceLocator.registerResourceService(new ResourceService());
        ServiceLocator.registerEntityService(new EntityService());
        ServiceLocator.registerRenderService(new RenderService());

        renderer = RenderFactory.createRenderer();

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
        renderer.resize(width, height);
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
        logger.debug("Disposing main game screen");

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
    }

    private void unloadAssets() {
        logger.debug("Unloading assets");
        ResourceService resourceService = ServiceLocator.getResourceService();
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

        Entity uiExit = new Entity();
        uiExit.addComponent(new InputDecorator(stage, 10))
                .addComponent(new PerformanceDisplay())
                .addComponent(new InventoryComponent(playerStatus.get(playerStatus.getAll().size() - 1).getGold(),
                        playerStatus.get(playerStatus.getAll().size() - 1).getStone()))
                .addComponent(new ShopActions(this.game, playerStatus))
                .addComponent(new ShopReturn())
                .addComponent(new CommonShopComponents())
                .addComponent(new Terminal())
                .addComponent(inputComponent)
                .addComponent(new TerminalDisplay());
        ServiceLocator.getEntityService().register(uiExit);

    }
}
