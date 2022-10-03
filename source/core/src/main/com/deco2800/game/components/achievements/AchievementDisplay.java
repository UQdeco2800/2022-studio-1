package com.deco2800.game.components.achievements;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Null;
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

        Texture tabBackground = new Texture(Gdx.files.internal("images/achievements/Tab_Background_Box.png")); //
        Drawable tabBackgroundBox = new TextureRegionDrawable(tabBackground);

        Texture badgeDescription = new Texture(Gdx.files.internal("images/achievements/Badge_Description_1580x126_Box.png"));
        Drawable badgeDescriptionBox = new TextureRegionDrawable(badgeDescription);

        contentTable.setBackground(backgroundBox);
        navigationTable.setBackground(tabBackgroundBox);
        displayTable.setBackground(badgeBackgroundBox);

        // Add display content
        changeDisplay(this.displayTable, AchievementType.SUMMARY);

        // Home Button
        ImageButton summaryButton = createButton("images/achievements/Summary_Button.png");
        this.addButtonEvent(summaryButton, "Summary");
        navigationTable.add(summaryButton).colspan(2).expand();
        navigationTable.row();

        // Building Button
        ImageButton buildingButton = createButton("images/achievements/Building_Icon.png");
        this.addButtonEvent(buildingButton, "Building");

        navigationTable.add(buildingButton).expand();

        // Game Button
        ImageButton gameButton = createButton("images/achievements/Game_Icon.png");
        this.addButtonEvent(gameButton, "Game");

        navigationTable.add(gameButton).expand();
        navigationTable.row();

        // Kill Button
        ImageButton killButton = createButton("images/achievements/Kill_Icon.png");
        this.addButtonEvent(killButton, "Kill");

        navigationTable.add(killButton).expand();

        // Resource Button
        ImageButton resourceButton = createButton("images/achievements/Resource_Icon.png");
        this.addButtonEvent(resourceButton, "Resource");

        navigationTable.add(resourceButton).expand();
        navigationTable.row();

        // Upgrade Button
        ImageButton upgradeButton = createButton("images/achievements/Upgrade_Icon.png");
        this.addButtonEvent(upgradeButton, "Upgrade");

        navigationTable.add(upgradeButton).expand();

        // Misc Button
        ImageButton miscButton = createButton("images/achievements/Misc_Icon.png");
        this.addButtonEvent(miscButton, "Misc");

        navigationTable.add(miscButton).expand();

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

    /**
     *  Creates the milestone button actor.
     *  listens on hover events to display milestone achievement
     *  in description
     *
     * @param milestoneNumber milestoneNumber
     * @param isComplete whether the milestone is complete
     * @param achievement the achievement to create the image button for
     * @param descriptionLabel the description label for the achievement
     * @return an image or
     */
    public static Image getMilestoneImageButtonByNumber(int milestoneNumber, boolean isComplete, Achievement achievement,
                                                        Label descriptionLabel) {
        switch (milestoneNumber) {
            case 1: {
                return createMilestoneImageButtonWithHoverEvent(isComplete, descriptionLabel, achievement, 1);
            }
          case 2: {
              return createMilestoneImageButtonWithHoverEvent(isComplete, descriptionLabel, achievement, 2);
          }
          case 3: {
              return createMilestoneImageButtonWithHoverEvent(isComplete, descriptionLabel, achievement, 3);
          }
          case 4: {
              return createMilestoneImageButtonWithHoverEvent(isComplete, descriptionLabel, achievement, 4);
          }
          default:
              return null;
        }
    }

    /**
     *
     * Creates a milestone button for the achievement card with hover events on it
     *
     * @param isComplete whether the milestone is achieved
     * @param descriptionLabel the UI label which will be dynamically changed on hover
     * @param achievement the achievement to create the button for
     * @param milestoneNumber the milestone number
     * @return the image button
     */
    private static Image createMilestoneImageButtonWithHoverEvent(boolean isComplete, Label descriptionLabel, Achievement achievement, int milestoneNumber) {
        AchievementHandler achievementService = ServiceLocator.getAchievementHandler();

        Texture backgroundTexture = new Texture(Gdx.files.internal(
                isComplete ? "images/achievements/milestone_%d_completed.png".formatted(milestoneNumber) :
                        "images/achievements/milestone_%d_incomplete.png".formatted(milestoneNumber) ));
        var image = new Image(backgroundTexture);
        if (isComplete) {
            image.addListener(new ClickListener() {
                @Override
                public void enter(InputEvent event, float x, float y, int pointer, @Null Actor fromActor) {
                    descriptionLabel.setText(achievement.getDescription().formatted(achievementService.getMilestoneTotal(achievement, milestoneNumber)));
                }

                @Override
                public void exit(InputEvent event, float x, float y, int pointer, @Null Actor toActor) {
                    descriptionLabel.setText(achievement.getDescription().formatted(achievement.getTotalAchieved()));
                }
            });
        }
        return image;
    }

    /**
     * Constructs the achievement milestone buttons for a given achievement
     *
     * @param achievement the achievement to create the buttons for
     * @param descriptionLabel the label to be changed on hover
     * @return a row of buttons table
     */
    public static Table buildAchievementMilestoneButtons(Achievement achievement, Label descriptionLabel) {
        var achievementService = ServiceLocator.getAchievementHandler();
        Table milestoneButtons = new Table();
        milestoneButtons.add();
        milestoneButtons.add(getMilestoneImageButtonByNumber(1,
                achievementService.isMilestoneAchieved(achievement, 1), achievement, descriptionLabel));
        milestoneButtons.add(getMilestoneImageButtonByNumber(2,
                achievementService.isMilestoneAchieved(achievement, 2),achievement,descriptionLabel));
        milestoneButtons.add(getMilestoneImageButtonByNumber(3,
                achievementService.isMilestoneAchieved(achievement, 3),achievement,descriptionLabel));
        milestoneButtons.add(getMilestoneImageButtonByNumber(4,
                achievementService.isMilestoneAchieved(achievement, 4),achievement,descriptionLabel));
       milestoneButtons.add();
       return milestoneButtons;
    }

    /**
     * Builds an achievement card actor read to be displayed
     *
     * @param achievement the achievement to create th card for
     * @return the actor (Table)
     */
    public static Table buildAchievementCard(Achievement achievement) {
        Table achievementCard = new Table();
        achievementCard.pad(30);
        Texture backgroundTexture = new Texture(Gdx.files.internal(achievement.isCompleted() ? "images/achievements/achievement_card_completed.png" : "images/achievements/achievement_card_locked_n.png"));
        Image backgroundImg = new Image(backgroundTexture);
        achievementCard.setBackground(backgroundImg.getDrawable());
        Table achievementCardHeader = new Table();
        Texture achievementTypeTexture = new Texture(Gdx.files.internal(achievement.getAchievementType().getPopupImage()));
        Image achievementTypeImage = new Image(achievementTypeTexture);
        achievementCardHeader.add(achievementTypeImage);
        achievementCardHeader.add(new Label(achievement.getName(), skin, "small"));
        achievementCard.add(achievementCardHeader).expand();
        achievementCard.row();
        var descriptionLabel = new Label(achievement.getDescription(), skin, "small");
        achievementCard.add(descriptionLabel).colspan(3).expand();
        if (achievement.isStat()) {
            descriptionLabel.setText(achievement.getDescription().formatted(achievement.getTotalAchieved()));
            achievementCard.row();
            achievementCard.add(buildAchievementMilestoneButtons(achievement, descriptionLabel)).padBottom(20);
        }
        achievementCard.pack();

        return achievementCard;
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

                displayTable.add(buildAchievementCard(achievement)).colspan(3).expand();

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
