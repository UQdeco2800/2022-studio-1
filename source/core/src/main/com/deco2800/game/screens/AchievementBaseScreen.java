package com.deco2800.game.screens;

import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.deco2800.game.AtlantisSinks;
import com.deco2800.game.components.achievements.AchievementActions;
import com.deco2800.game.components.achievements.AchievementBaseDisplay;
import com.deco2800.game.entities.Entity;
import com.deco2800.game.entities.EntityService;
import com.deco2800.game.entities.factories.RenderFactory;
import com.deco2800.game.input.InputDecorator;
import com.deco2800.game.input.InputService;
import com.deco2800.game.rendering.RenderService;
import com.deco2800.game.rendering.Renderer;
import com.deco2800.game.services.AchievementHandler;
import com.deco2800.game.services.GameTime;
import com.deco2800.game.services.ResourceService;
import com.deco2800.game.services.ServiceLocator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Base game screen for displaying the player's achievements. To be extended for displaying actual
 * achievement pages. (change to abstract when other screens are implemented)
 */
public class AchievementBaseScreen extends ScreenAdapter {
    /**
     * Logger class for the AchievementBaseScreen
     */
    private static final Logger logger = LoggerFactory.getLogger(AchievementBaseScreen.class);

    /**
     * Array of achievement icon textures
     */
    private static final String[] achievementTextures = {
            "images/achievements/Building_Icon.png",
            "images/achievements/Game_Icon.png",
            "images/achievements/Kill_Icon.png",
            "images/achievements/Misc_Icon.png",
            "images/achievements/Resource_Icon.png",
            "images/achievements/Upgrade_Icon.png",
            "images/uiElements/exports/exit_button.png"
    };

    /**
     * Instance of AtlantisSinks game
     */
    private final AtlantisSinks game;

    /**
     * Stores the screen renderer
     */
    private final Renderer renderer;

    /**
     * Initialises the AchievementBaseScreen with all necessary services and assets
     * @param game AtlantisSinks
     */
    public AchievementBaseScreen(AtlantisSinks game) {
        this.game = game;

        logger.debug("Initialising {} screen services", this.getClass().getName());
        ServiceLocator.registerTimeSource(new GameTime());

        ServiceLocator.registerInputService(new InputService());
        ServiceLocator.registerResourceService(new ResourceService());
        ServiceLocator.registerEntityService(new EntityService());
        ServiceLocator.registerRenderService(new RenderService());
        ServiceLocator.registerAchievementHandler(new AchievementHandler());

        this.renderer = RenderFactory.createRenderer();

        loadAssets();
        createUI();

        logger.debug("Initialising {} screen entities", this.getClass().getName());
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
        logger.debug("Disposing of {} screen", this.getClass().toString());
        renderer.dispose();
        unloadAssets();

        ServiceLocator.getEntityService().dispose();
        ServiceLocator.getRenderService().dispose();
        ServiceLocator.getResourceService().dispose();

        ServiceLocator.clear();
    }

    /**
     * Helps load game assets to the resource service
     */
    private void loadAssets() {
        logger.debug("Loading assets");
        ResourceService resourceService = ServiceLocator.getResourceService();
        resourceService.loadTextures(achievementTextures);
        resourceService.loadAll();
    }

    /**
     * Helps unload assets from the resource service
     */
    private void unloadAssets() {
        logger.debug("Unloading assets");
        ServiceLocator.getResourceService().unloadAssets(achievementTextures);
    }

    /**
     * Creates UI elements for the achievement screen
     */
    private void createUI() {
        logger.debug("Creating achievement UI");
        Stage stage = ServiceLocator.getRenderService().getStage();

        Entity ui = new Entity();
        ui.addComponent(new AchievementBaseDisplay())
                .addComponent(new InputDecorator(stage, 10))
                .addComponent(new AchievementActions(this.game));
        ServiceLocator.getEntityService().registerNamed("AchievementUI", ui);
    }
}
