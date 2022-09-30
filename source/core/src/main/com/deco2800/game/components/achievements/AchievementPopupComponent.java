package com.deco2800.game.components.achievements;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.deco2800.game.achievements.Achievement;
import com.deco2800.game.services.AchievementHandler;
import com.deco2800.game.services.ServiceLocator;
import com.deco2800.game.ui.UIComponent;

import java.util.LinkedList;
import java.util.Queue;


/**
 * Builds and displays an achievement widget that is displayed momentarily when a player
 * makes a new achievement in the game.
 * The widget is displayed for 5 seconds
 */
public class AchievementPopupComponent extends UIComponent {

    /* The amount of time the popup should show  in milliseconds (5 secs) */
    public static final long POPUP_TIMEOUT = 5000;

    /* For UI Layout */
    private Table content;
    private Table root;
    private Table details;
    private Table background;



    /* A FIFO Queue for queuing achievements */
    private Queue<Achievement> popupQueue;

    /* Records the last time a popup was displayed */
    private long lastPopupTime;

    /* Whether a popup is currently showing */
    private boolean isPopupActive;


    /**
     * Initialises component.
     * Connects to the player achievements event
     */
    @Override
    public void create() {
        super.create();
        this.isPopupActive = false;
        popupQueue = new LinkedList<>();
        ServiceLocator.getAchievementHandler().getEvents()
                .addListener(AchievementHandler.EVENT_STAT_ACHIEVEMENT_MADE, this::onAchievementMade);
        ServiceLocator.getAchievementHandler().getEvents().addListener(AchievementHandler.EVENT_ACHIEVEMENT_MADE, this::onAchievementMade);
    }

    /**
     * On receiving an event that a player has made an achievement.
     * The achievement is queued and drawn in next draw callback for the
     * component. First achievement to arrive takes priority.
     *
     * @param achievement the achievement achieved
     */
    private void onAchievementMade(Achievement achievement) {
        this.popupQueue.add(achievement);
    }

    /**
     * Builds the popup achievement widget to be displayed
     *
     * @param image the achievement type image
     * @param title the title of the achievement
     * @param description the description of the achievement
     */
    private void buildActors(String image, String title, String description) {
        root = new Table();
        root.setFillParent(true);
        root.padBottom(Gdx.graphics.getHeight() - 120);

        content = new Table();
        content.top();

        background = new Table();
        background.top();
        background.setFillParent(true);

        details = new Table();
        details.setFillParent(true);

        Texture achievementTypeTexture = new Texture(Gdx.files.internal(image));
        Image achievementTypeImage = new Image(achievementTypeTexture);

        Texture backgroundTexture = new Texture(Gdx.files.internal("images/achievements/Notification_Popup_scaled.png"));
        Image achievementPopupBackground = new Image(backgroundTexture);
        background.add(achievementPopupBackground);
        background.pack();

        content.add(achievementTypeImage);
        Label achievementTitleLabel = new Label("Achieved:" + title, skin, "default");
        Label achievementDescriptionLabel = new Label(description, skin, "small");
        details.add(achievementTitleLabel);
        details.row();
        details.add(achievementDescriptionLabel);
        content.add(details);
        root.add(content);
    }

    private void addActors() {
        stage.addActor(background);
        stage.addActor(root);
    }

    /**
     * Used to remove the popup from stage
     */
    private void clearActors() {
        /* if root is not null than other aren't */
        if (root != null) {
            background.clear();
            root.clear();
            content.clear();
            details.clear();
        }
    }

    /**
     * Clean up
     */
    @Override
    public void dispose() {
        super.dispose();
        clearActors();
    }


    /**
     * When the draw method is called we check if the popup queue is not
     * empty and then display the one first in the list.
     * Other popups will get their turn when the ones to be shown timeout
     *
     * @param batch Batch to render to. (not used) actors added to stage instead
     */
    @Override
    protected void draw(SpriteBatch batch) {
        if (!popupQueue.isEmpty() && !isPopupActive) {
            var achievement = popupQueue.poll();
            if (achievement.isStat()) {
                clearActors();
                buildActors(achievement.getAchievementType().getPopupImage(),
                        achievement.getName(), achievement.getDescription().formatted(achievement.getTotalAchieved()));
                addActors();
                lastPopupTime = System.currentTimeMillis();
                isPopupActive = true;
            }
        }

        if(isPopupActive && shouldHidePopup()) {
           clearActors();
           isPopupActive = false;
        }
    }

    /**
     * Determines whether to hide the popup based on the default timeout
     * value
     * @return true if popup should be hidden false otherwise
     */
    private boolean shouldHidePopup() {
        long currentTime = System.currentTimeMillis();
        return currentTime - lastPopupTime >= POPUP_TIMEOUT;
    }
}
