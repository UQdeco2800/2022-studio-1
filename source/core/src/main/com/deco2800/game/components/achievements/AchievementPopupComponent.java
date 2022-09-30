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


public class AchievementPopupComponent extends UIComponent {

    public static final long POPUP_TIMEOUT = 5000;
    private Table content;

    private Table root;
    private Table details;

    private Table background;
    private Image achievementTypeImage;

    private Image achievementPopupBackground;

    private Label achievementTitleLabel;

    private Label achievementDescriptionLabel;

    private Queue<Achievement> popupQueue;

    private long lastPopupTime;

    private boolean isPopupActive;



    @Override
    public void create() {
        super.create();
        this.isPopupActive = false;
        popupQueue = new LinkedList<>();
        ServiceLocator.getAchievementHandler().getEvents()
                .addListener(AchievementHandler.EVENT_STAT_ACHIEVEMENT_MADE, this::onAchievementMade);
        ServiceLocator.getAchievementHandler().getEvents().addListener(AchievementHandler.EVENT_ACHIEVEMENT_MADE, this::onAchievementMade);
    }

    private void onAchievementMade(Achievement achievement) {
        this.popupQueue.add(achievement);
    }

    private void buildActors(String image, String title, String description) {
        root = new Table();
        root.setFillParent(true);
        root.debug();
        root.debugActor();
        root.debugCell();
        root.padBottom(Gdx.graphics.getHeight() - 120);

        content = new Table();
        content.top();
        content.debug();


        background = new Table();
        background.top();
        background.debug();
        background.setFillParent(true);

        details = new Table();
        details.setFillParent(true);

        Texture achievementTypeTexture = new Texture(Gdx.files.internal(image));
        achievementTypeImage = new Image(achievementTypeTexture);

        Texture backgroundTexture = new Texture(Gdx.files.internal("images/achievements/Notification_Popup_scaled.png"));
        achievementPopupBackground = new Image(backgroundTexture);
        background.add(achievementPopupBackground);
        background.pack();

        content.add(achievementTypeImage);
        achievementTitleLabel = new Label("Achievement Earned: " + title, skin, "default");
        achievementDescriptionLabel = new Label(description, skin, "small");
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

    private void clearActors() {
        /* if root is not null than other aren't */
        if (root != null) {
            background.clear();
            root.clear();
            content.clear();
            details.clear();
        }
    }

    @Override
    public void dispose() {
        super.dispose();
        clearActors();
    }



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

    private boolean shouldHidePopup() {
        long currentTime = System.currentTimeMillis();
        return currentTime - lastPopupTime >= POPUP_TIMEOUT;
    }
}
