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
import com.deco2800.game.achievements.Achievement;
import com.deco2800.game.achievements.AchievementType;
import com.deco2800.game.areas.ForestGameArea;
import com.deco2800.game.services.AchievementHandler;
import com.deco2800.game.services.ServiceLocator;
import com.deco2800.game.ui.UIComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.stream.Collectors;

/**
 * Base achievement display class to be extended by achievement type screens
 */
public class AchievementInterface extends UIComponent {
    /**
     * Event string for opening the achievement screen
     */
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
     * ArrayList of achievement icon buttons
     */
    private ArrayList<AchievementButton> achievementButtons;

    /**
     * Group containing the achievement badges
     */
    private Group achievementBadges;
    private float badgeWidth;

    /**
     * List of achievement progress bars for purpose of updating
     */
    private java.util.List<AchievementProgressBar> achievementProgressBars = new ArrayList<>();

    /**
     * Create the achievement base display
     */
    public void create() {
        super.create();
        entity.getEvents().addListener(EVENT_OPEN_ACHIEVEMENTS, this::openAchievements);
        entity.getEvents().addListener("closeAll", this::closeAchievements);
        ServiceLocator.getAchievementHandler().getEvents().addListener(AchievementHandler.EVENT_ACHIEVEMENT_MADE,
                this::updateAchievementProgressBarOnAchievementMade);
        ServiceLocator.getAchievementHandler().getEvents().addListener(AchievementHandler.EVENT_STAT_ACHIEVEMENT_MADE,
                this::updateAchievementProgressBarOnAchievementMade);
        achievementButtons = new ArrayList<>();
        achievementBadges = new Group();
        addActors();
    }


    /**
     * Upon receiving that an achiement update is made update summary
     * and individual sections
     * @param achievement the achievement just achieved
     */
    private void updateAchievementProgressBarOnAchievementMade(Achievement achievement) {
        java.util.List<AchievementProgressBar> summaryProgress = achievementProgressBars
                .stream()
                .filter(a -> a.getAchievementType() == AchievementType.SUMMARY).toList();


        java.util.List<AchievementProgressBar> typeProgressBars = achievementProgressBars
                .stream()
                .filter(a -> a.getAchievementType() == achievement.getAchievementType()).toList();

        updateAchievementTypeProgressBars(typeProgressBars, achievement.getAchievementType());
        updateSummaryProgressBars(summaryProgress);
    }

    /**
     * Updates overall summary achievements
     * NOTE: Only one will be in list
     *
     * @param progressBars progress bars to update
     */
    private void updateSummaryProgressBars(java.util.List<AchievementProgressBar> progressBars) {
      for(AchievementProgressBar progressBar : progressBars) {
          progressBar.setDone(getTotalAchievementTypesAchieved());
      }

    }

    /**
     * Updates every other progress bar that is not the summary
     *
     * @param progressBars the list of all types of progressbars excluding SUMMARY
     * @param type type other than SUMMARY
     */
    private void updateAchievementTypeProgressBars(java.util.List<AchievementProgressBar> progressBars, AchievementType type) {
        for(AchievementProgressBar progressBar : progressBars) {
            progressBar.setDone(getTotalAchievementsAchievedByType(type));;
        }
    }

    /**
     * Adds content tables to the resource stage
     */
    private void addActors() {
        group = new Group();
        Table backgroundTable = new Table();
        float iconSize = Gdx.graphics.getHeight() * 0.11f;
        float backgroundTableWidth = Gdx.graphics.getWidth() * 0.7f;
        float backgroundTableHeight = Gdx.graphics.getHeight() * 0.7f;

        float navigationTableWidth = Gdx.graphics.getWidth() * 0.7f * 0.25f;
        float navigationTableHeight = Gdx.graphics.getHeight() * 0.7f * 0.75f;

        float leftColumnX = Gdx.graphics.getWidth() * 0.7f * 0.24f + navigationTableWidth / 4f - iconSize / 2f + 5f;
        float rightColumnX = Gdx.graphics.getWidth() * 0.7f * 0.24f + navigationTableWidth * 3f / 4f - iconSize / 2f - 5f;

        float firstRowY = Gdx.graphics.getHeight() * 0.57f;

        // Setup background for achievements
        backgroundTable.setSize(backgroundTableWidth, backgroundTableHeight);
        backgroundTable.setPosition(Gdx.graphics.getWidth() / 2f - backgroundTableWidth / 2f,
                Gdx.graphics.getHeight() / 2f - backgroundTableHeight / 2f);

        Table navigationTable = new Table();
        navigationTable.setSize(navigationTableWidth, navigationTableHeight);
        navigationTable.setPosition(Gdx.graphics.getWidth() * 0.7f * 0.24f, Gdx.graphics.getHeight() * 0.7f * 0.28f);

        this.displayTable = new Table();
        displayTable.defaults().pad(5f);
        displayTable.setSize(Gdx.graphics.getWidth() * 0.7f * 0.69f, Gdx.graphics.getHeight() * 0.7f * 0.75f);
        displayTable.setPosition(Gdx.graphics.getWidth() * 0.7f * 0.5f, Gdx.graphics.getHeight() * 0.7f * 0.28f);

        // Title
        Label title = new Label("Achievements", skin, ForestGameArea.TITLE_FONT);
        title.setFontScale(2f);
        title.setPosition(Gdx.graphics.getWidth() * 0.26f,
                Gdx.graphics.getHeight() * 0.75f);

        // Background Colour
        Texture background = new Texture(Gdx.files.internal("images/achievements/Background.png"));
        Drawable backgroundBox = new TextureRegionDrawable(background);

        Texture badgeBackground = new Texture(Gdx.files.internal("images/achievements/BadgeContent_Box.png"));
        Drawable badgeBackgroundBox = new TextureRegionDrawable(badgeBackground);

        Texture tabBackground = new Texture(Gdx.files.internal("images/achievements/Tab_Background_Box.png"));
        Drawable tabBackgroundBox = new TextureRegionDrawable(tabBackground);

        backgroundTable.setBackground(backgroundBox);
        navigationTable.setBackground(tabBackgroundBox);
        displayTable.setBackground(badgeBackgroundBox);

        // Add display content
        changeDisplay(AchievementType.SUMMARY);

        // Home Button
        AchievementButton summaryButton = createButton(AchievementType.SUMMARY);
        this.addButtonEvent(summaryButton, AchievementType.SUMMARY.getTitle());
        summaryButton.setSize(Gdx.graphics.getWidth() * 0.7f * 0.25f * 0.75f, iconSize);
        summaryButton.setPosition(Gdx.graphics.getWidth() * 0.19f, firstRowY);

        // Building Button
        AchievementButton buildingButton = createButton(AchievementType.BUILDINGS);
        this.addButtonEvent(buildingButton, AchievementType.BUILDINGS.getTitle());
        buildingButton.setPosition(leftColumnX, firstRowY - iconSize);

        // Game Button
        AchievementButton gameButton = createButton(AchievementType.GAME);
        this.addButtonEvent(gameButton, AchievementType.GAME.getTitle());
        gameButton.setPosition(rightColumnX, firstRowY - iconSize);

        // Kill Button
        AchievementButton killButton = createButton(AchievementType.KILLS);
        this.addButtonEvent(killButton, AchievementType.KILLS.getTitle());
        killButton.setPosition(leftColumnX, firstRowY - 2 * iconSize);

        // Resource Button
        AchievementButton resourceButton = createButton(AchievementType.RESOURCES);
        this.addButtonEvent(resourceButton, AchievementType.RESOURCES.getTitle());
        resourceButton.setPosition(rightColumnX, firstRowY - 2 * iconSize);

        // Upgrade Button
        AchievementButton upgradeButton = createButton(AchievementType.UPGRADES);
        this.addButtonEvent(upgradeButton, AchievementType.UPGRADES.getTitle());
        upgradeButton.setPosition(leftColumnX, firstRowY - 3 * iconSize);

        // Misc Button
        AchievementButton miscButton = createButton(AchievementType.MISC);
        this.addButtonEvent(miscButton, AchievementType.MISC.getTitle());
        miscButton.setPosition(rightColumnX, firstRowY - 3 * iconSize);

        // Back Button
        Texture backTexture = new Texture(Gdx.files.internal("images/backButton.png"));
        Texture backTextureHover = new Texture(Gdx.files.internal("images/backButton_hover.png"));
        TextureRegionDrawable upBack = new TextureRegionDrawable(backTexture);
        TextureRegionDrawable downBack = new TextureRegionDrawable(backTexture);
        TextureRegionDrawable checkedBack = new TextureRegionDrawable(backTextureHover);
        ImageButton backButton = new ImageButton(upBack, downBack, checkedBack);

        backButton.setSize(40f, 40f);
        backButton.setPosition(Gdx.graphics.getWidth() * 0.85f - 70f,
                Gdx.graphics.getHeight() * 0.85f - 70f);

        this.addExitButtonEvent(backButton);

        group.addActor(backgroundTable);
        group.addActor(title);

        // Display main content
        displayTable.align(Align.top);
        displayTable.pad(Gdx.graphics.getHeight() * 0.1f);

        group.addActor(navigationTable);
        group.addActor(displayTable);

        // Add achievement badges
        group.addActor(achievementBadges);

        // Add buttons
        for (AchievementButton button : achievementButtons) {
            if (!button.getType().equals(AchievementType.SUMMARY)) {
                button.setSize(iconSize, iconSize);
            }

            group.addActor(button);
        }

        group.addActor(backButton);
        group.setVisible(false);
        stage.addActor(group);
        //stage.setDebugAll(true);
    }

    /**
     * Opens the achievement interface by setting the group's visibility to true
     */
    private void openAchievements() {
        group.setVisible(true);
    }

    /**
     * Closes the achievement interface by setting the group's visibility to false
     */
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
     * @return Group of ImageButtons
     */
    public static Group buildAchievementMilestoneButtons(Group milestoneButtons, Achievement achievement, Label descriptionLabel) {
        var achievementService = ServiceLocator.getAchievementHandler();
        float buttonSize = Gdx.graphics.getHeight() * 0.11f / 5f;

        for (int i = 1; i <= 4; i++) {
            Image button = getMilestoneImageButtonByNumber(i,
                    achievementService.isMilestoneAchieved(achievement, i), achievement, descriptionLabel);

            if (button != null) {
                button.setSize(buttonSize, buttonSize);
                button.setPosition(milestoneButtons.getParent().getX() + (i - 1) * buttonSize, milestoneButtons.getParent().getY());
                milestoneButtons.addActor(button);
            }
        }

       return milestoneButtons;
    }

    /**
     * Builds an achievement card actor read to be displayed
     *
     * @param achievement the achievement to create the card for
     * @return the actor (Table)
     */
    public Group buildAchievementCard(Achievement achievement) {
        Group achievementCard = new Group();
        float contentX = achievementCard.getX() + Gdx.graphics.getWidth() * 0.05f;
        float contentWidth = badgeWidth * 0.7f;

        Texture backgroundTexture = new Texture(
                Gdx.files.internal(achievement.isCompleted() ?
                        "images/achievements/%s_Tick.png".formatted(achievement.getAchievementType().getTitle()) :
                        "images/achievements/%s_Lock.png".formatted(achievement.getAchievementType().getTitle())
                )
        );

        Image backgroundImg = new Image(backgroundTexture);
        backgroundImg.setFillParent(true);
        achievementCard.addActor(backgroundImg);

        Label achievementTitle = new Label(achievement.getName(), skin, ForestGameArea.TITLE_FONT);
        achievementTitle.setFontScale(0.5f);
        achievementTitle.setPosition(contentX, achievementCard.getY() + Gdx.graphics.getHeight() * 0.062f);
        achievementTitle.setAlignment(Align.center);
        achievementTitle.setWidth(contentWidth);
        achievementCard.addActor(achievementTitle);

        var descriptionLabel = new Label(achievement.isStat() ?
                achievement.getDescription().formatted(achievement.getTotalAchieved()) :
                achievement.getDescription(), skin, ForestGameArea.SMALL_FONT);
        descriptionLabel.setFontScale(0.9f);
        descriptionLabel.setWidth(contentWidth);
        descriptionLabel.setWrap(true);
        descriptionLabel.setAlignment(Align.center);
        descriptionLabel.setPosition(contentX,
                achievementCard.getY() + Gdx.graphics.getHeight() * 0.05f - descriptionLabel.getHeight() / 2f);
        achievementCard.addActor(achievementTitle);
        achievementCard.addActor(descriptionLabel);

        if (achievement.isStat()) {
            Group milestoneButtons = new Group();
            milestoneButtons.setPosition(contentX + contentWidth / 4f - milestoneButtons.getWidth() / 2f,
                    achievementCard.getY() + Gdx.graphics.getHeight() * 0.015f);
            achievementCard.addActor(milestoneButtons);
            achievementCard.addActor(buildAchievementMilestoneButtons(milestoneButtons, achievement, descriptionLabel));
        }

        return achievementCard;
    }
    
    /**
     * Creates an achievement summary card of the provided achievement type
     * @param type AchievementType
     * @return Group
     */
    public Group buildAchievementSummaryCard(AchievementType type) {
        Group summaryCard = new Group();
        float contentX = summaryCard.getX() + Gdx.graphics.getWidth() * 0.05f;
        float contentWidth = badgeWidth * 0.7f;

        Texture backgroundTexture = new Texture(
                    Gdx.files.internal("images/achievements/%s_Summary.png".formatted(type.getTitle())));

        Image backgroundImg = new Image(backgroundTexture);
        backgroundImg.setFillParent(true);
        summaryCard.addActor(backgroundImg);

        Label achievementTitle = new Label(type.getTitle(), skin, ForestGameArea.TITLE_FONT);
        achievementTitle.setFontScale(0.5f);
        achievementTitle.setPosition(contentX, summaryCard.getY() + Gdx.graphics.getHeight() * 0.056f);
        achievementTitle.setAlignment(Align.center);
        achievementTitle.setWidth(contentWidth);
        summaryCard.addActor(achievementTitle);
        long totalAchievements = ServiceLocator.getAchievementHandler()
                .getAchievements()
                .stream()
                .filter(a -> a.getAchievementType() == type).count();
        long totalAchieved = ServiceLocator.getAchievementHandler()
                .getAchievements()
                .stream()
                .filter(a -> a.getAchievementType() == type && a.isCompleted())
                .count();
        AchievementProgressBar progressBar = new AchievementProgressBar(100, 10, (int) totalAchievements, false, type);
        Actor progressBarActor = progressBar.getActor();
        progressBarActor.setPosition(contentX, (summaryCard.getY() - 15)+ Gdx.graphics.getHeight() * 0.056f);
        progressBarActor.setWidth(contentWidth);
        progressBar.setDone((int)totalAchieved);
        summaryCard.addActor(progressBarActor);

        return summaryCard;
    }

    /**
     * Sets all achievement icons to be checked, used for highlighting the selected achievement icon
     * from the navigation panel
     */
    public void changeSelectedIcon() {
        for (AchievementButton button : this.achievementButtons) {
            button.setChecked(true);
        }
    }

    /**
     * Calculates the total list of achievements achieved
     * @return total achieved
     */
    private int getTotalAchievementTypesAchieved() {
        return ServiceLocator.getAchievementHandler().getAchievements()
                .stream()
                .filter(a -> a.getAchievementType() != AchievementType.SUMMARY && a.isCompleted())
                .collect(Collectors.groupingBy(Achievement::getAchievementType))
                .values()
                .size();
    }

    /**
     * Calculates the total amount of achievements in existent
     * @return total amount of achievements that exist
     */
    private int getTotalAchievementsInExistent() {
        return ServiceLocator.getAchievementHandler().getAchievements()
                .stream()
                .filter(a -> a.getAchievementType() != AchievementType.SUMMARY)
                .collect(Collectors.groupingBy(Achievement::getAchievementType))
                .values()
                .size();
    }

    /**
     * Gets the total amount of achievements achieved by the type
     *
     * @param type the type of achievements
     * @return total number achieved
     */
    private int getTotalAchievementsAchievedByType(AchievementType type) {
        return (int) ServiceLocator.getAchievementHandler().getAchievements()
                .stream()
                .filter(a -> a.getAchievementType() == type && a.isCompleted())
                .count();
    }

    /**
     * Computes the total number of achievements by the achievement type
     * @param type the achievement type to check
     * @return total number that exist
     */
    private int getTotalNumberOfAchievementsByType(AchievementType type) {
        return (int) ServiceLocator.getAchievementHandler().getAchievements()
                .stream()
                .filter(a -> a.getAchievementType() == type)
                .count();
    }

    /**
     * Change displayed achievement badges to those of the provided type. If type is SUMMARY
     * a breakdown of completion of each achievement type will be displayed
     * @param type AchievementType
     */
    public void changeDisplay(AchievementType type) {
        achievementBadges.clear();

        Label title = new Label(type.getTitle(), skin, ForestGameArea.TITLE_FONT);
        title.setFontScale(1f);
        title.setPosition(displayTable.getX() + displayTable.getWidth() / 2f - title.getWidth() / 2f, Gdx.graphics.getHeight() * 0.64f);
        achievementBadges.addActor(title);


        int achievementsAdded = 0;
        Group achievementBadge;
        ArrayList<Achievement> achievements = new ArrayList<>(ServiceLocator.getAchievementHandler().getAchievements());

        this.badgeWidth = Gdx.graphics.getWidth() * 0.21f;
        float badgeHeight = Gdx.graphics.getHeight() * 0.12f;

        float leftColumnX = displayTable.getX() + displayTable.getWidth() / 4f - badgeWidth / 2f + badgeWidth / 20f;
        float rightColumnX = displayTable.getX() + displayTable.getWidth() * 3f / 4f - badgeWidth / 2f - badgeWidth / 20f;
        float firstRowY = Gdx.graphics.getHeight() * 0.5f;

        if (type == AchievementType.SUMMARY) {
            // Add progress bar
            AchievementProgressBar progressBar = new AchievementProgressBar(100, 8, getTotalAchievementsInExistent(), false, type);
            progressBar.setHideLabel(true);
            Actor progressBarActor = progressBar.getActor();
            progressBarActor.setPosition(displayTable.getX() + displayTable.getWidth() / 2f - badgeWidth/ 2f, (Gdx.graphics.getHeight() - 15) * 0.64f);
            progressBarActor.setWidth(badgeWidth);
            progressBar.setDone(getTotalAchievementTypesAchieved());
            achievementBadges.addActor(progressBarActor);

            for (AchievementType achievementType : AchievementType.values()) {
                if (achievementType == AchievementType.SUMMARY) {
                    continue;
                }

                achievementBadge = buildAchievementSummaryCard(achievementType);
                achievementBadge.setSize(badgeWidth, badgeHeight);
                achievementBadge.setPosition(achievementsAdded % 2 == 0 ? leftColumnX : rightColumnX, firstRowY - (badgeHeight + (achievementsAdded < 2 ? 0f : badgeHeight / 8f)) * Math.floorDiv(achievementsAdded, 2));

                achievementBadges.addActor(achievementBadge);

                achievementsAdded++;
            }

            return;
        }

        // Add progress bar per achievement type
        AchievementProgressBar progressBar = new AchievementProgressBar(100, 8, getTotalNumberOfAchievementsByType(type), false, type);
        progressBar.setHideLabel(true);
        Actor progressBarActor = progressBar.getActor();
        progressBarActor.setPosition(displayTable.getX() + displayTable.getWidth() / 2f - badgeWidth/ 2f, (Gdx.graphics.getHeight() - 15) * 0.64f);
        progressBarActor.setWidth(badgeWidth);
        progressBar.setDone(getTotalAchievementsAchievedByType(type));
        achievementBadges.addActor(progressBarActor);

        for (Achievement achievement : achievements) {
            if (achievement.getAchievementType() == type) {
                achievementBadge = buildAchievementCard(achievement);

                achievementBadge.setSize(badgeWidth, badgeHeight);
                achievementBadge.setPosition(achievementsAdded % 2 == 0 ? leftColumnX : rightColumnX, firstRowY - (badgeHeight + (achievementsAdded < 2 ? 0f : badgeHeight / 8f)) * Math.floorDiv(achievementsAdded, 2));

                achievementBadges.addActor(achievementBadge);

                achievementsAdded++;
            }
        }
    }

    /**
     * Creates an AchievementButton from a provided image
     * 
     * @param type AchievementType
     * @return AchievementButton
     */
    private AchievementButton createButton(AchievementType type) {
        String image = "images/achievements/" + type.getTitle() + "_Icon.png";
        String imageNotSelected = "images/achievements/" + type.getTitle() + "_NotCurrent.png";

        Texture buttonTexture = new Texture(Gdx.files.internal(image));
        TextureRegionDrawable up = new TextureRegionDrawable(buttonTexture);
        TextureRegionDrawable down = new TextureRegionDrawable(buttonTexture);

        Texture buttonNotSelected = new Texture(Gdx.files.internal(imageNotSelected));
        TextureRegionDrawable isUnselected = new TextureRegionDrawable(buttonNotSelected);

        AchievementButton button = new AchievementButton(up, down, isUnselected, type);
        button.getLabel().setColor(skin.getColor(ForestGameArea.BLACK));

        button.setChecked(!type.equals(AchievementType.SUMMARY));

        this.achievementButtons.add(button);

        return button;
    }

    /**
     * Add listener to the provided AchievementButton, will change display of achievement interface
     * on click
     * @param button AchievementButton
     * @param name   String
     */
    private void addButtonEvent(AchievementButton button, String name) {
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

    /**
     * Add listener to provided ImageButton, will close achievement interface
     * @param button ImageButton
     */
    public void addExitButtonEvent(ImageButton button) {
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
    }
}
