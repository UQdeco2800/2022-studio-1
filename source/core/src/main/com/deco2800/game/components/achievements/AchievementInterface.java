package com.deco2800.game.components.achievements;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Null;
import com.badlogic.gdx.utils.SnapshotArray;
import com.deco2800.game.achievements.Achievement;
import com.deco2800.game.achievements.AchievementType;
import com.deco2800.game.services.AchievementHandler;
import com.deco2800.game.services.ServiceLocator;
import com.deco2800.game.ui.UIComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Objects;

/**
 * Base achievement display class to be extended by achievement type screens
 */
public class AchievementInterface extends UIComponent {
    public static final String EVENT_OPEN_ACHIEVEMENTS = "achievement";

    /**
     * Logger for the AchievementBaseDisplay class
     */
    private static final Logger logger = LoggerFactory.getLogger(AchievementInterface.class);

    /**
     * Table for displaying all screen content
     */
    private Group group;

    /**
     * Table for containing all achievement badges
     */
    private Table displayTable;

    /**
     * Table for containing achievement navigation buttons
     */
    private Table navigationTable;

    /**
     * Create the achievement base display
     */
    public void create() {
        super.create();
        entity.getEvents().addListener(EVENT_OPEN_ACHIEVEMENTS, this::openAchievements);
        entity.getEvents().addListener("closeAll", this::closeAchievements);
        addActors();
    }

    /**
     * Adds content tables to the resource stage
     */
    private void addActors() {
        group = new Group();
        Table backgroundTable = new Table();

        // Setup background for achievements
        backgroundTable.setSize(Gdx.graphics.getWidth() * 0.7f, Gdx.graphics.getHeight() * 0.7f);
        backgroundTable.setPosition(Gdx.graphics.getWidth() / 2f - backgroundTable.getWidth() / 2f,
                Gdx.graphics.getHeight() / 2f - backgroundTable.getHeight() / 2f);

        this.navigationTable = new Table();
        navigationTable.setSize(Gdx.graphics.getWidth() * 0.7f * 0.25f, Gdx.graphics.getHeight() * 0.7f * 0.75f);
        navigationTable.setPosition(Gdx.graphics.getWidth() * 0.7f * 0.24f, Gdx.graphics.getHeight() * 0.7f * 0.28f);

        this.displayTable = new Table();
        displayTable.defaults().pad(5f);
        displayTable.setSize(Gdx.graphics.getWidth() * 0.7f * 0.69f, Gdx.graphics.getHeight() * 0.7f * 0.75f);
        displayTable.setPosition(Gdx.graphics.getWidth() * 0.7f * 0.5f, Gdx.graphics.getHeight() * 0.7f * 0.28f);

        // Title
        Label title = new Label("Achievements", skin, "title");
        title.setFontScale(2f);
        title.setPosition(Gdx.graphics.getWidth() * 0.15f + 110f,
                Gdx.graphics.getHeight() * 0.85f - 110f);

        // Background Colour
        Texture background = new Texture(Gdx.files.internal("images/achievements/Background_2540x1033.png"));
        Drawable backgroundBox = new TextureRegionDrawable(background);

        Texture badgeBackground = new Texture(Gdx.files.internal("images/achievements/BadgeBackground_1664x824.png"));
        Drawable badgeBackgroundBox = new TextureRegionDrawable(badgeBackground);

        Texture tabBackground = new Texture(Gdx.files.internal("images/achievements/Tab_Background_Box.png"));
        Drawable tabBackgroundBox = new TextureRegionDrawable(tabBackground);

        backgroundTable.setBackground(backgroundBox);
        navigationTable.setBackground(tabBackgroundBox);
        displayTable.setBackground(badgeBackgroundBox);

        // Add display content
        changeDisplay(AchievementType.SUMMARY);

        // Home Button
        ImageButton summaryButton = createButton(AchievementType.SUMMARY);
        this.addButtonEvent(summaryButton, AchievementType.SUMMARY.getTitle());
        navigationTable.add(summaryButton).colspan(2).expand();
        navigationTable.row();

        // Building Button
        ImageButton buildingButton = createButton(AchievementType.BUILDINGS);
        this.addButtonEvent(buildingButton, AchievementType.BUILDINGS.getTitle());

        navigationTable.add(buildingButton).expand();

        // Game Button
        ImageButton gameButton = createButton(AchievementType.GAME);
        this.addButtonEvent(gameButton, AchievementType.GAME.getTitle());

        navigationTable.add(gameButton).expand();
        navigationTable.row();

        // Kill Button
        ImageButton killButton = createButton(AchievementType.KILLS);
        this.addButtonEvent(killButton, AchievementType.KILLS.getTitle());

        navigationTable.add(killButton).expand();

        // Resource Button
        ImageButton resourceButton = createButton(AchievementType.RESOURCES);
        this.addButtonEvent(resourceButton, AchievementType.RESOURCES.getTitle());

        navigationTable.add(resourceButton).expand();
        navigationTable.row();

        // Upgrade Button
        ImageButton upgradeButton = createButton(AchievementType.UPGRADES);
        this.addButtonEvent(upgradeButton, AchievementType.UPGRADES.getTitle());

        navigationTable.add(upgradeButton).expand();

        // Misc Button
        ImageButton miscButton = createButton(AchievementType.MISC);
        this.addButtonEvent(miscButton, AchievementType.MISC.getTitle());

        navigationTable.add(miscButton).expand();

        // Back Button
        Texture backTexture = new Texture(Gdx.files.internal("images/cross.png"));
        Texture backTextureHover = new Texture(Gdx.files.internal("images/backButton_hover.png"));
        TextureRegionDrawable upBack = new TextureRegionDrawable(backTexture);
        TextureRegionDrawable downBack = new TextureRegionDrawable(backTexture);
        TextureRegionDrawable checkedBack = new TextureRegionDrawable(backTextureHover);
        ImageButton backButton = new ImageButton(upBack, downBack, checkedBack);

        backButton.setSize(40f, 40f);
        backButton.setPosition(Gdx.graphics.getWidth() * 0.85f - 70f,
                Gdx.graphics.getHeight() * 0.85f -70f);

        this.addButtonEvent(backButton, "Exit");

        group.addActor(backgroundTable);
        group.addActor(title);

        // Display main content
        displayTable.align(Align.top);
        displayTable.pad(50f);

        navigationTable.pad(50f);

        group.addActor(navigationTable);
        group.addActor(displayTable);

        group.addActor(backButton);
        group.setVisible(false);
        stage.addActor(group);
        //stage.setDebugAll(true);
    }

    private void openAchievements() {
        group.setVisible(true);
    }

    private void closeAchievements() {
        group.setVisible(false);
    }

    @Override
    public void draw(SpriteBatch batch) {
        // draw is handled by the stage
    }

    @Override
    public void dispose() {
        super.dispose();
        group.clear();
    }

    /**
     * Creates the milestone button actor.
     * listens on hover events to display milestone achievement
     * in description
     *
     * @param milestoneNumber  milestoneNumber
     * @param isComplete       whether the milestone is complete
     * @param achievement      the achievement to create the image button for
     * @param descriptionLabel the description label for the achievement
     * @return an image or
     */
    public static Image getMilestoneImageButtonByNumber(int milestoneNumber, boolean isComplete,
            Achievement achievement,
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
     * @param isComplete       whether the milestone is achieved
     * @param descriptionLabel the UI label which will be dynamically changed on
     *                         hover
     * @param achievement      the achievement to create the button for
     * @param milestoneNumber  the milestone number
     * @return the image button
     */
    private static Image createMilestoneImageButtonWithHoverEvent(boolean isComplete, Label descriptionLabel,
            Achievement achievement, int milestoneNumber) {
        AchievementHandler achievementService = ServiceLocator.getAchievementHandler();

        Texture backgroundTexture = new Texture(Gdx.files.internal(
                isComplete ? "images/achievements/milestone_%d_completed.png".formatted(milestoneNumber)
                        : "images/achievements/milestone_%d_incomplete.png".formatted(milestoneNumber)));
        var image = new Image(backgroundTexture);
        if (isComplete) {
            image.addListener(new ClickListener() {
                @Override
                public void enter(InputEvent event, float x, float y, int pointer, @Null Actor fromActor) {
                    descriptionLabel.setText(achievement.getDescription()
                            .formatted(achievementService.getMilestoneTotal(achievement, milestoneNumber)));
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
     * @param achievement      the achievement to create the buttons for
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
                achievementService.isMilestoneAchieved(achievement, 2), achievement, descriptionLabel));
        milestoneButtons.add(getMilestoneImageButtonByNumber(3,
                achievementService.isMilestoneAchieved(achievement, 3), achievement, descriptionLabel));
        milestoneButtons.add(getMilestoneImageButtonByNumber(4,

                achievementService.isMilestoneAchieved(achievement, 4),achievement,descriptionLabel));
       milestoneButtons.add();
       milestoneButtons.align(Align.center);
       return milestoneButtons;
    }

    /**
     * Builds an achievement card actor read to be displayed
     *
     * @param achievement the achievement to create the card for
     * @return the actor (Table)
     */
    public static Table buildAchievementCard(Achievement achievement) {
        Table achievementCard = new Table();

        achievementCard.pad(30f, 40f, 30f, 40f);
        Texture backgroundTexture;

        if (achievement.getAchievementType() == AchievementType.RESOURCES || achievement.getAchievementType() == AchievementType.UPGRADES) {
            backgroundTexture = new Texture(Gdx.files.internal(achievement.isCompleted() ? "images/achievements/%s_Tick.png".formatted(achievement.getAchievementType().getTitle())
                    : "images/achievements/%s_Lock.png".formatted(achievement.getAchievementType().getTitle())));
        } else {
            backgroundTexture = new Texture(Gdx.files.internal(achievement.isCompleted() ? "images/achievements/achievement_card_completed.png"
                    : "images/achievements/achievement_card_locked_n.png"));
        }

        Image backgroundImg = new Image(backgroundTexture);
        achievementCard.setBackground(backgroundImg.getDrawable());

        Label achievementTitle = new Label(achievement.getName(), skin, "title");
        achievementTitle.setFontScale(0.7f);
        achievementTitle.setAlignment(Align.center);
        achievementCard.add(achievementTitle).colspan(3).expandX();
        achievementCard.row();

        ArrayList<String> achievementDescription = splitDescription(achievement.isStat() ? achievement.getDescription().formatted(achievement.getTotalAchieved()) : achievement.getDescription());

        var descriptionLabel = new Label(achievementDescription.get(0), skin, "large");
        descriptionLabel.setFontScale(0.7f);
        achievementCard.add(descriptionLabel).colspan(3).expandX();
        achievementCard.row();

        Label tempLabel;

        for (String s : achievementDescription) {
            if (achievementDescription.indexOf(s) == 0) {
                continue;
            }

            tempLabel = new Label(s, skin, "large");
            tempLabel.setFontScale(0.7f);
            achievementCard.add(tempLabel).colspan(3).expandX();
            achievementCard.row();
        }

        if (achievementDescription.size() == 1) {
            tempLabel = new Label("", skin, "large");
            tempLabel.setFontScale(0.7f);
            achievementCard.add(tempLabel).colspan(3).expandX();
            achievementCard.row();
        }

        if (achievement.isStat()) {
            achievementCard.add(buildAchievementMilestoneButtons(achievement, descriptionLabel)).expandX().colspan(3).padBottom(20).align(Align.center);
        } else {
            tempLabel = new Label("", skin, "large");
            tempLabel.setFontScale(0.7f);
            achievementCard.add(tempLabel).colspan(3).expandX();
            achievementCard.row();
        }

        achievementCard.row();
        achievementCard.pack();

        return achievementCard;
    }

    /**
     * Split a description string into multiple lines
     * 
     * @param description String
     * @return ArrayList
     */
    public static ArrayList<String> splitDescription(String description) {
        ArrayList<String> splitDescription = new ArrayList<>();
        String[] temp = description.split(" ");
        int rowLength = 0;
        int maxRowLength = 5;

        StringBuilder row = new StringBuilder();

        for (String s : temp) {
            if (rowLength >= maxRowLength) {
                if (row.isEmpty()) {
                    return splitDescription;
                }

                splitDescription.add(row.toString());
                rowLength = 0;
                row = new StringBuilder();
            }

            row.append(s);
            row.append(" ");
            rowLength++;
        }

        splitDescription.add(row.toString());

        return splitDescription;
    }

    public static Table buildAchievementSummaryCard(AchievementType type) {
        Table summaryCard = new Table();
        summaryCard.pad(30f, 40f, 30f, 40f);
        Texture backgroundTexture;

        if (type == AchievementType.RESOURCES || type == AchievementType.UPGRADES) {
            backgroundTexture = new Texture(
                    Gdx.files.internal(ServiceLocator.getAchievementHandler().allCompletedOfType(type)
                            ? "images/achievements/%s_Tick.png".formatted(type.getTitle())
                            : "images/achievements/%s_Lock.png".formatted(type.getTitle())));
        } else {
            backgroundTexture = new Texture(
                    Gdx.files.internal(ServiceLocator.getAchievementHandler().allCompletedOfType(type)
                            ? "images/achievements/achievement_card_completed.png"
                            : "images/achievements/achievement_card_locked_n.png"));
        }

        Image backgroundImg = new Image(backgroundTexture);
        summaryCard.setBackground(backgroundImg.getDrawable());

        Label title = new Label(type.getTitle(), skin, "title");
        title.setFontScale(0.7f);
        summaryCard.add(title).colspan(3).expand();
        summaryCard.row();

        return summaryCard;
    }

    public void changeSelectedIcon() {
        SnapshotArray<Actor> navActors = navigationTable.getChildren();

        for (int i = 0; i < navActors.size; i++) {
            if (navActors.get(i) != null && navActors.get(i).getClass() == AchievementButton.class) {
                AchievementButton button = (AchievementButton) navActors.get(i);

                button.setChecked(true);
            }
        }
    }

    public void changeDisplay(AchievementType type) {
        displayTable.clear();
        Label title = new Label(type.getTitle(), skin, "title");
        title.setFontScale(1f);
        displayTable.add(title).colspan(6).expandX();
        displayTable.row();

        int achievementsAdded = 0;
        ArrayList<Achievement> achievements = new ArrayList<>(ServiceLocator.getAchievementHandler().getAchievements());

        if (type == AchievementType.SUMMARY) {
            for (AchievementType achievementType : AchievementType.values()) {
                if (achievementsAdded != 0 && achievementsAdded % 2 == 0) {
                    displayTable.row();
                } else if (achievementType == AchievementType.SUMMARY) {
                    continue;
                }

                displayTable.add(buildAchievementSummaryCard(achievementType)).colspan(3).fillX();

                achievementsAdded++;
            }

            return;
        }

        for (Achievement achievement : achievements) {
            if (achievement.getAchievementType() == type) {
                if (achievementsAdded != 0 && achievementsAdded % 2 == 0) {
                    displayTable.row();
                }

                displayTable.add(buildAchievementCard(achievement)).colspan(3).fillX();

                achievementsAdded++;
            }
        }
    }

    /**
     * Creates an ImageButton from a provided image
     * 
     * @param type AchievementType
     * @return ImageButton
     */
    private static ImageButton createButton(AchievementType type) {
        String image = "images/achievements/" + type.getTitle() + "_Icon.png";
        String imageNotSelected = "images/achievements/" + type.getTitle() + "_NotCurrent.png";

        Texture buttonTexture = new Texture(Gdx.files.internal(image));
        TextureRegionDrawable up = new TextureRegionDrawable(buttonTexture);
        TextureRegionDrawable down = new TextureRegionDrawable(buttonTexture);

        Texture buttonNotSelected = new Texture(Gdx.files.internal(imageNotSelected));
        TextureRegionDrawable isUnselected = new TextureRegionDrawable(buttonNotSelected);

        AchievementButton button = new AchievementButton(up, down, isUnselected, type);
        button.setChecked(!type.equals(AchievementType.SUMMARY));

        return button;
    }

    /**
     * Add listener to the provided ImageButton
     * 
     * @param button ImageButton
     * @param name   String
     */
    private void addButtonEvent(ImageButton button, String name) {
        if (Objects.equals(name, "Exit")) {
            button.addListener(
                    new ClickListener() {
                        @Override
                        public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                            logger.debug("Exit button clicked");
                            closeAchievements();
                            entity.getEvents().trigger("closeAll");
                            return true;
                        }
                    });
            // Adds hover state to button
            button.addListener(
                    new InputListener() {
                        @Override
                        public void enter(InputEvent event, float x, float y, int pointer, Actor actor) {
                            button.setChecked(true);
                        }

                        @Override
                        public void exit(InputEvent event, float x, float y, int pointer, Actor actor) {
                            button.setChecked(false);
                        }
                    });
            button.addListener(
                    new TextTooltip("Close achievement page", skin));

            return;
        }

        button.addListener(
                new ClickListener() {
                    @Override
                    public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                        logger.debug("{} button clicked", name);
                        changeDisplay(AchievementType.valueOf(name.toUpperCase()));
                        changeSelectedIcon();
                        return true;
                    }
                });

        button.addListener(
                new TextTooltip(name, skin));
    }
}
