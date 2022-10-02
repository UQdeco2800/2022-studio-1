package com.deco2800.game.components.achievements;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.deco2800.game.achievements.Achievement;
import com.deco2800.game.achievements.AchievementType;
import com.deco2800.game.screens.AchievementScreen;
import com.deco2800.game.services.AchievementHandler;
import com.deco2800.game.services.ServiceLocator;
import com.deco2800.game.ui.UIComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

/**
 * Base achievement display class to be extended by achievement type screens
 */
public class AchievementDisplay extends UIComponent {
    /**
     * Event string for achievement summary button press
     */
    public static final String EVENT_SUMMARY_BUTTON_CLICKED = "summaryButtonClicked";

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
    private static final Logger logger = LoggerFactory.getLogger(AchievementScreen.class);

    private static final float Z_INDEX = 2f;

    /**
     * Table for displaying all screen content
     */
    private Table rootTable;

    /**
     * Table for containing all achievement badges
     */
    private Table displayTable;

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
        this.displayTable = new Table();
        displayTable.defaults().pad(10f);
        Table contentTable = new Table();
        contentTable.defaults().pad(10f);

        // Title
        // fonts -> https://libgdxinfo.wordpress.com/basic-label/
        /*
        Image title = new Image(new Texture(Gdx.files.internal("Achievements/AchievementsTitle.png")));
        title.setAlign(Align.center);
        title.setHeight(Gdx.graphics.getHeight() * 0.2f);

         */

        Label.LabelStyle titleStyle = new Label.LabelStyle();
        titleStyle.font = new BitmapFont(Gdx.files.internal("flat-earth/skin/fonts/pixel_32.fnt"));
        titleStyle.fontColor = Color.BLACK;

        Label title = new Label("Achievements",titleStyle);
        title.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight() * 0.5f);
        title.setAlignment(Align.center);

        // Background Colour
        Texture background = new Texture(Gdx.files.internal("images/achievements/Badge_Background_2540x1033_Box.png"));
        Drawable backgroundBox = new TextureRegionDrawable(background);

        Texture badgeBackground = new Texture(Gdx.files.internal("images/achievements/Background_1664x824_Box.png")); // _1348x888
        Drawable badgeBackgroundBox = new TextureRegionDrawable(badgeBackground);

        Texture tabBackground = new Texture(Gdx.files.internal("images/achievements/Tab_428x824_Box.png")); //
        Drawable tabBackgroundBox = new TextureRegionDrawable(tabBackground);

        Texture badgeDescription = new Texture(Gdx.files.internal("images/achievements/Badge_Description_1580x126_Box.png"));
        Drawable badgeDescriptionBox = new TextureRegionDrawable(badgeDescription);

        contentTable.setBackground(backgroundBox);
        navigationTable.setBackground(tabBackgroundBox);
        displayTable.setBackground(badgeBackgroundBox);

        // Add display content
        changeDisplay(this.displayTable, AchievementType.SUMMARY);

        // Home Button
        ImageButton summaryButton = createButton("images/achievements/summaryIcon.png");
        this.addButtonEvent(summaryButton, "Summary");
        navigationTable.add(summaryButton).colspan(2);
        navigationTable.row();

        // Building Button
        ImageButton buildingButton = createButton("images/achievements/Building_64x64_Icon.png");
        this.addButtonEvent(buildingButton, "Building");

        navigationTable.add(buildingButton);

        // Game Button
        ImageButton gameButton = createButton("images/achievements/Game_64x64_Icon.png");
        this.addButtonEvent(gameButton, "Game");

        navigationTable.add(gameButton);
        navigationTable.row();

        // Kill Button
        ImageButton killButton = createButton("images/achievements/Kill_64x64_Icon.png");
        this.addButtonEvent(killButton, "Kill");

        navigationTable.add(killButton);

        // Resource Button
        ImageButton resourceButton = createButton("images/achievements/Resource_64x64_Icon.png");
        this.addButtonEvent(resourceButton, "Resource");

        navigationTable.add(resourceButton);
        navigationTable.row();

        // Upgrade Button
        ImageButton upgradeButton = createButton("images/achievements/Upgrade_64x64_Icon.png");
        this.addButtonEvent(upgradeButton, "Upgrade");

        navigationTable.add(upgradeButton);

        // Misc Button
        ImageButton miscButton = createButton("images/achievements/Misc_64x64_Icon.png");
        this.addButtonEvent(miscButton, "Misc");

        navigationTable.add(miscButton);

        // Back Button
        ImageButton exitButton = createButton("images/uiElements/exports/back.png");
        this.addButtonEvent(exitButton, "Exit");

        exitTable.add(exitButton).expandX().expandY().right().top().pad(0f, 0f, 0f, 0f);
        
        // Display main content

        contentTable.add(navigationTable).colspan(2).expand();
        contentTable.add(displayTable).colspan(6).expand();

        rootTable.add(exitTable).colspan(8).fillX();
        rootTable.row();
        rootTable.add(title).colspan(8).expand();
        rootTable.row();
        rootTable.add(contentTable).colspan(8).expand();

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

    public static void changeDisplay(Table displayTable, AchievementType type) {
        displayTable.clear();
        displayTable.add(new Label(type.getTitle(), skin)).colspan(6).expand();
        displayTable.row();

        int achievementsAdded = 0;
        ArrayList<Achievement> achievements = new ArrayList<>(ServiceLocator.getAchievementHandler().getAchievements());

        if (type == AchievementType.SUMMARY) {
            displayTable.add(new Label("Building Achievements", skin)).colspan(3).expand();
            displayTable.add(new Label("Game Achievements", skin)).colspan(3).expand();
            displayTable.row();
            displayTable.add(new Label("Kill Achievements", skin)).colspan(3).expand();
            displayTable.add(new Label("Resource Achievements", skin)).colspan(3).expand();
            displayTable.row();
            displayTable.add(new Label("Upgrade Achievements", skin)).colspan(3).expand();
            displayTable.add(new Label("Misc Achievements", skin)).colspan(3).expand();
            return;
        }

        for (Achievement achievement : achievements) {
            if (achievement.getAchievementType() == type) {
                if (achievementsAdded != 0 && achievementsAdded % 2 == 0) {
                    displayTable.row();
                }

                displayTable.add(new Label(achievement.getName(), skin)).colspan(3).expand();

                achievementsAdded++;
            }
        }
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
                        entity.getEvents().trigger(events.get(name), displayTable);
                    }
                });
    }

    /**
     * Maps button names to event strings
     */
    private void mapEvents() {
        this.events.put("Summary", EVENT_SUMMARY_BUTTON_CLICKED);
        this.events.put("Building", EVENT_BUILDING_BUTTON_CLICKED);
        this.events.put("Game", EVENT_GAME_BUTTON_CLICKED);
        this.events.put("Kill", EVENT_KILL_BUTTON_CLICKED);
        this.events.put("Resource", EVENT_RESOURCE_BUTTON_CLICKED);
        this.events.put("Upgrade", EVENT_UPGRADE_BUTTON_CLICKED);
        this.events.put("Misc", EVENT_MISC_BUTTON_CLICKED);
        this.events.put("Exit", EVENT_EXIT_BUTTON_CLICKED);
    }
}
