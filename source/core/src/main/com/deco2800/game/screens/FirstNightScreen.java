package com.deco2800.game.screens;

import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.deco2800.game.AtlantisSinks;
import com.deco2800.game.components.firstnight.FirstNightActions;
import com.deco2800.game.components.firstnight.FirstNightDisplay;
import com.deco2800.game.entities.Entity;
import com.deco2800.game.entities.factories.RenderFactory;
import com.deco2800.game.input.InputDecorator;
import com.deco2800.game.rendering.Renderer;
import com.deco2800.game.services.ResourceService;
import com.deco2800.game.services.ServiceLocator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FirstNightScreen extends ScreenAdapter {

    private static final Logger logger = LoggerFactory.getLogger(FirstNightScreen.class);
    private final AtlantisSinks game;
    private final Renderer renderer;

    // load all the texture images
    private static final String[] storylineTextures = {
            "images/StoryLine/clearBackground.png",
            "images/StoryLine/FirstNight.png"
    };

    public FirstNightScreen(AtlantisSinks game) {
        this.game = game;

        logger.debug("Initialising firstNight screen services");
        renderer = RenderFactory.createRenderer();

        loadAssets();
        createUI();
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
        logger.debug("Disposing first night screen");

        renderer.dispose();
    }

    private void loadAssets() {
        logger.debug("Loading assets");
        ResourceService resourceService = ServiceLocator.getResourceService();
        resourceService.loadTextures(storylineTextures);
        ServiceLocator.getResourceService().loadAll();
    }

    private void unloadAssets() {
        logger.debug("Unloading assets");
        ResourceService resourceService = ServiceLocator.getResourceService();
        resourceService.unloadAssets(storylineTextures);
    }

    /**
     * Creates the storyline UI including components for rendering ui elements to
     * the screen and
     * capturing and handling ui input.
     */
    private void createUI() {
        logger.debug("Creating storyline ui");
        Stage stage = ServiceLocator.getRenderService().getStage();
        Entity ui = new Entity();
        ui.addComponent(new FirstNightDisplay())
                .addComponent(new InputDecorator(stage, 10))
                .addComponent(new FirstNightActions(game));
        ServiceLocator.getEntityService().register(ui);
    }
}