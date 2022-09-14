package com.deco2800.game.screens;

import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.deco2800.game.AtlantisSinks;
import com.deco2800.game.components.storyline.StoryLineActions;
import com.deco2800.game.components.storyline.StoryLineDisplay;
import com.deco2800.game.entities.Entity;
import com.deco2800.game.entities.EntityService;
import com.deco2800.game.entities.factories.RenderFactory;
import com.deco2800.game.input.InputComponent;
import com.deco2800.game.input.InputDecorator;
import com.deco2800.game.input.InputService;
import com.deco2800.game.rendering.RenderService;
import com.deco2800.game.rendering.Renderer;
import com.deco2800.game.services.ResourceService;
import com.deco2800.game.services.ServiceLocator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class StoryLineScreen extends ScreenAdapter{

    private static final Logger logger = LoggerFactory.getLogger(StoryLineScreen.class);
    private final AtlantisSinks game;
    private final Renderer renderer;

    //load all the texture images
    private static final String[] storylineTextures = {
            "test/files/storylineBackground1.png",
            "test/files/clearBackground.png",
            "test/files/storylineBackground2.png",
            "test/files/storylineBackground3.png",
    };

    public StoryLineScreen(AtlantisSinks game) {
        this.game = game;

        logger.debug("Initialising storyline screen services");
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
        logger.debug("Disposing storyline screen");

        renderer.dispose();
        unloadAssets();
        ServiceLocator.getRenderService().dispose();
        ServiceLocator.getEntityService().dispose();
        ServiceLocator.clear();
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
     * Creates the storyline UI including components for rendering ui elements to the screen and
     * capturing and handling ui input.
     */
    private void createUI() {
        logger.debug("Creating storyline ui");
        Stage stage = ServiceLocator.getRenderService().getStage();
        InputComponent inputComponent = ServiceLocator.getInputService().getInputFactory().createForStoryLine();
        Entity ui = new Entity();
        ui.addComponent(new StoryLineDisplay())
                .addComponent(new InputDecorator(stage, 10))
                .addComponent(inputComponent)
                .addComponent(new StoryLineActions(game));
        ServiceLocator.getEntityService().register(ui);
    }
}
