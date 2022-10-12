package com.deco2800.game.screens;

import com.deco2800.game.memento.Memento;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.deco2800.game.AtlantisSinks;
import com.deco2800.game.areas.MainArea;
import com.deco2800.game.areas.ShopArea;
import com.deco2800.game.components.gamearea.PerformanceDisplay;
import com.deco2800.game.components.player.InventoryComponent;
import com.deco2800.game.components.shop.CommonShopComponents;
import com.deco2800.game.components.shop.ShopActions;
import com.deco2800.game.components.shop.ShopArtefactDisplay;
import com.deco2800.game.components.shop.ShopBackground;
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

public class ShopArtefactScreen extends ScreenAdapter {
    private static final Logger logger = LoggerFactory.getLogger(ShopArtefactScreen.class);

    private final AtlantisSinks game;
    private final Renderer renderer;

    public ShopArtefactScreen(AtlantisSinks game) {
        this.game = game;

        logger.debug("Initialising artefact shop screen services");

        renderer = RenderFactory.createRenderer();

        loadAssets();
        createUI();
        MainArea.getInstance().setMainArea(new ShopArea());

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
        logger.debug("Disposing shop artefact game screen");
        renderer.dispose();
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
        Memento lastStatus = CareTaker.getInstance().getLast();
        Entity uiCommon = new Entity();
        uiCommon.addComponent(new ShopBackground());
        ServiceLocator.getEntityService().register(uiCommon);
        Entity uiArtefact = new Entity();
        uiArtefact.addComponent(new InputDecorator(stage, 10))
                .addComponent(new PerformanceDisplay())
                .addComponent(new ShopActions(this.game))
                .addComponent(new InventoryComponent(lastStatus.getGold(),
                        lastStatus.getStone(), lastStatus.getWood()))
                .addComponent(new CommonShopComponents())
                .addComponent(new ShopArtefactDisplay())
                .addComponent(new Terminal())
                .addComponent(inputComponent)
                .addComponent(new TerminalDisplay());
        uiArtefact.getComponent(InventoryComponent.class).setItems(lastStatus.getItemList());
        ServiceLocator.getEntityService().register(uiArtefact);

    }
}
