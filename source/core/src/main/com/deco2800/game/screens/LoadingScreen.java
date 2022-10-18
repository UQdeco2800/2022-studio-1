package com.deco2800.game.screens;

import com.badlogic.gdx.ScreenAdapter;
import com.deco2800.game.AtlantisSinks;
import com.deco2800.game.components.loadingPage.LoadingActions;
import com.deco2800.game.components.loadingPage.LoadingDisplay;
import com.deco2800.game.entities.Entity;
import com.deco2800.game.entities.EntityService;
import com.deco2800.game.entities.factories.RenderFactory;
import com.deco2800.game.input.InputService;
import com.deco2800.game.rendering.RenderService;
import com.deco2800.game.rendering.Renderer;
import com.deco2800.game.services.ResourceService;
import com.deco2800.game.services.ServiceLocator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LoadingScreen extends ScreenAdapter {

    private static final Logger logger = LoggerFactory.getLogger(LoadingScreen.class);
    private final AtlantisSinks game;
    private final Renderer renderer;

    //load all the texture images
    private static final String[] loadingTextures = {
            "images/StoryLine/SL_1.png",
    };

    public LoadingScreen(AtlantisSinks game) {
        this.game = game;

        logger.debug("Initialising loading screen services");
        ServiceLocator.registerInputService(new InputService());
        ServiceLocator.registerResourceService(new ResourceService());
        ServiceLocator.registerEntityService(new EntityService());
        ServiceLocator.registerRenderService(new RenderService());

        renderer = RenderFactory.createRenderer();

        loadAssets();
        createUI();
    }

    @Override
    public void render(float delta) {
        ServiceLocator.getEntityService().update();
        if (ServiceLocator.getResourceService().getAssetManager().update()) {
            game.setScreen(AtlantisSinks.ScreenType.MAIN_GAME);
        }
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
        logger.debug("Disposing loading screen");

        renderer.dispose();
        unloadAssets();
        ServiceLocator.getRenderService().dispose();
        ServiceLocator.getEntityService().dispose();
        ServiceLocator.clear();
    }

    private void loadAssets() {
        logger.debug("Loading assets");
        ResourceService resourceService = ServiceLocator.getResourceService();
        resourceService.loadTextures(loadingTextures);
        ServiceLocator.getResourceService().loadAll();
    }

    private void unloadAssets() {
        logger.debug("Unloading assets");
        ResourceService resourceService = ServiceLocator.getResourceService();
        resourceService.unloadAssets(loadingTextures);
    }

    /**
     * Creates the loading UI including components for rendering ui elements to the screen and
     * capturing and handling ui input.
     */
    private void createUI() {
        logger.debug("Creating loading screen ui");
        Entity ui = new Entity();
        ui.addComponent(new LoadingDisplay())
                .addComponent(new LoadingActions(game));
        ServiceLocator.getEntityService().register(ui);
    }
}
