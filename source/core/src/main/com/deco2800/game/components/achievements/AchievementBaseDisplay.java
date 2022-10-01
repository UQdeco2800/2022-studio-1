package com.deco2800.game.components.achievements;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.deco2800.game.screens.achievements.AchievementBaseScreen;
import com.deco2800.game.ui.UIComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.HashMap;

/**
 * Base achievement display class to be extended by achievement type screens
 */
public class AchievementBaseDisplay extends UIComponent {
    /**
     * Event string for building achievement button press
     */
    public static final String EVENT_BUILDING_BUTTON_CLICKED = "buildingButtonClicked";

    /**
     * Event string for game achievement button press
     */
    public static final String EVENT_GAME_BUTTON_CLICKED = "gameButtonClicked";

    /**
     * Event string for kill achievement button press
     */
    public static final String EVENT_KILL_BUTTON_CLICKED = "killButtonClicked";

    /**
     * Event string for resource achievement button press
     */
    public static final String EVENT_RESOURCE_BUTTON_CLICKED = "resourceButtonClicked";

    /**
     * Event string for resource achievement button press
     */
    public static final String EVENT_UPGRADE_BUTTON_CLICKED = "upgradeButtonClicked";

    /**
     * Event string for misc achievement button press
     */
    public static final String EVENT_MISC_BUTTON_CLICKED = "miscButtonClicked";

    /**
     * Event string for exit button press
     */
    public static final String EVENT_EXIT_BUTTON_CLICKED = "exitButtonClicked";

    /**
     * Hashmap of button types and their events
     */
    private final HashMap<String, String> events = new HashMap<>();

    /**
     * Logger for the AchievementBaseDisplay class
     */
    private static final Logger logger = LoggerFactory.getLogger(AchievementBaseScreen.class);

    private static final float Z_INDEX = 2f;

    /**
     * Table for displaying all screen content
     */
    private Table rootTable;

    /**
     * Create the achievement base display
     */
    public void create() {
        super.create();
        mapEvents();
        addActors();
    }

    /**
     * Adds content tables to the resource stage
     */
    private void addActors() {
        rootTable = new Table();
        rootTable.defaults().pad(10f);
        rootTable.setFillParent(true);

        rootTable.center();

        Table exitTable = new Table();
        Table navigationTable = new Table();
        navigationTable.defaults().pad(10f);
        Table displayTable = new Table();
        displayTable.defaults().pad(10f);
        Table contentTable = new Table();
        contentTable.defaults().pad(10f);

        // Title
        Label title = new Label("Achievements", skin, "large");
        title.setAlignment(Align.center);

        // Background Colour
        Texture background = new Texture(Gdx.files.internal("Achievements/Badge_Background_Box.png"));
        Drawable backgroundBox = new TextureRegionDrawable(background);

        Texture badgeBackground = new Texture(Gdx.files.internal("Achievements/Background_Box.png"));
        Drawable badgeBackgroundBox = new TextureRegionDrawable(badgeBackground);

        Texture tabBackground = new Texture(Gdx.files.internal("Achievements/Tab_Box.png"));
        Drawable tabBackgroundBox = new TextureRegionDrawable(tabBackground);

        contentTable.setBackground(backgroundBox);
        navigationTable.setBackground(tabBackgroundBox);
        displayTable.setBackground(badgeBackgroundBox);

        displayTable.add(new Label("Achievement 1", skin, "large"));
        displayTable.row();
        displayTable.add(new Label("Achievement 2", skin, "large"));
        displayTable.row();
        displayTable.add(new Label("Achievement 3", skin, "large"));
        displayTable.row();
        displayTable.add(new Label("Achievement 4", skin, "large"));
        displayTable.row();
        displayTable.add(new Label("Achievement 5", skin, "large"));
        displayTable.row();
        displayTable.add(new Label("Achievement 6", skin, "large"));
        // Home Button

        // Building Button
        ImageButton buildingButton = createButton("images/achievements/Building_Icon.png");
        this.addButtonEvent(buildingButton, "Building");

        navigationTable.add(buildingButton);

        // Game Button
        ImageButton gameButton = createButton("images/achievements/Game_Icon.png");
        this.addButtonEvent(gameButton, "Game");

        navigationTable.add(gameButton);
        navigationTable.row();

        // Kill Button
        ImageButton killButton = createButton("images/achievements/Kill_Icon.png");
        this.addButtonEvent(killButton, "Kill");

        navigationTable.add(killButton);

        // Resource Button
        ImageButton resourceButton = createButton("images/achievements/Resource_Icon.png");
        this.addButtonEvent(resourceButton, "Resource");

        navigationTable.add(resourceButton);
        navigationTable.row();

        // Upgrade Button
        ImageButton upgradeButton = createButton("images/achievements/Upgrade_Icon.png");
        this.addButtonEvent(upgradeButton, "Upgrade");

        navigationTable.add(upgradeButton);

        // Misc Button
        ImageButton miscButton = createButton("images/achievements/Misc_Icon.png");
        this.addButtonEvent(miscButton, "Misc");

        navigationTable.add(miscButton);

        // Back Button
        ImageButton exitButton = createButton("images/uiElements/exports/back.png");
        this.addButtonEvent(exitButton, "Exit");

        exitTable.add(exitButton).expandX().expandY().right().top().pad(0f, 0f, 0f, 0f);

        // Add display content
        // fonts -> https://libgdxinfo.wordpress.com/basic-label/
        
        // Display main content

        contentTable.add(navigationTable).colspan(2).expand();
        contentTable.add(displayTable).colspan(3).expand();

        rootTable.add(exitTable).colspan(5).fillX();
        rootTable.row();
        rootTable.add(title).colspan(5).fillX();
        rootTable.row();
        rootTable.add(contentTable).colspan(5).expand();

        stage.addActor(rootTable);
        stage.setDebugAll(true);
    }

    @Override
    public void draw(SpriteBatch batch) {
        // draw is handled by the stage
    }

    @Override
    public void dispose() {
        super.dispose();
        rootTable.clear();
    }

    /**
     * Creates an ImageButton from a provided image
     * @param image String: File location
     * @return ImageButton
     */
    private ImageButton createButton(String image) {
        Texture buttonTexture = new Texture(Gdx.files.internal(image));
        TextureRegionDrawable up = new TextureRegionDrawable(buttonTexture);
        TextureRegionDrawable down = new TextureRegionDrawable(buttonTexture);

        return new ImageButton(up, down);
    }

    /**
     * Add listener to the provided ImageButton
     * @param button ImageButton
     * @param name String
     */
    private void addButtonEvent(ImageButton button, String name) {
        button.addListener(
                new ChangeListener() {
                    @Override
                    public void changed(ChangeEvent event, Actor actor) {
                        logger.debug("{} button clicked", name);
                        entity.getEvents().trigger(events.get(name));
                    }
                });
    }

    /**
     * Maps button names to event strings
     */
    private void mapEvents() {
        this.events.put("Building", EVENT_BUILDING_BUTTON_CLICKED);
        this.events.put("Game", EVENT_GAME_BUTTON_CLICKED);
        this.events.put("Kill", EVENT_KILL_BUTTON_CLICKED);
        this.events.put("Resource", EVENT_RESOURCE_BUTTON_CLICKED);
        this.events.put("Upgrade", EVENT_UPGRADE_BUTTON_CLICKED);
        this.events.put("Misc", EVENT_MISC_BUTTON_CLICKED);
        this.events.put("Exit", EVENT_EXIT_BUTTON_CLICKED);
    }
}
