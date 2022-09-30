package com.deco2800.game.components.achievements;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.deco2800.game.achievements.AchievementType;
import com.deco2800.game.ui.UIComponent;

public class AchievementPopupComponent extends UIComponent {

    private Table content;

    private Table root;
    private Table details;
    private Image achievementTypeImage;

    private Image achievementPopupBackground;

    private Label achievementTitleLabel;

    private Label achievementDescriptionLabel;

    @Override
    public void create() {
        super.create();
        addActors();
    }


    private void addActors() {

        root = new Table();
        root.setFillParent(true);
        root.debug();
        root.padBottom(Gdx.graphics.getHeight() - 230);

        content = new Table();
        content.top();
        content.setHeight(0.2f);
        content.debug();


        details = new Table();
        details.padBottom(10);
        details.setFillParent(true);

        Texture achievementTypeTexture = new Texture(Gdx.files.internal(AchievementType.BUILDINGS.getImage()));
        achievementTypeImage = new Image(achievementTypeTexture);
        achievementTypeImage.setScale(0.5f);

        Texture backgroundTexture = new Texture(Gdx.files.internal("images/achievements/Notification_Popup.png"));
        achievementPopupBackground = new Image(backgroundTexture);
        //achievementPopupBackground.setScale(0.7f);
        achievementPopupBackground.setHeight(0.2f);
        achievementPopupBackground.pack();
        var background = achievementPopupBackground.getDrawable();
        content.setBackground(background);
        content.pack();
        content.add(achievementTypeImage);
        achievementTitleLabel = new Label("Achievement Earnt: Upgrader", skin, "small");
        achievementDescriptionLabel = new Label("Upgraded 5 times", skin, "small");
        details.add(achievementTitleLabel);
        details.row();
        details.add(achievementDescriptionLabel);
        content.add(details);
        root.add(content);
        stage.addActor(root);
    }

    @Override
    public void dispose() {

        super.dispose();
    }



    @Override
    protected void draw(SpriteBatch batch) {
        // handled by stage
    }
}
