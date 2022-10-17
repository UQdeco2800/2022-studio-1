package com.deco2800.game.components.achievements;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.deco2800.game.achievements.AchievementType;
import com.deco2800.game.ui.UIComponent;

/**
 * Extends ImageButton to store what type of achievement the button is representing
 */
public class AchievementButton extends TextButton {
    private AchievementType type;

    public AchievementButton(Drawable imageUp, Drawable imageDown, Drawable imageChecked, AchievementType type) {
        super(type.equals(AchievementType.SUMMARY) ? AchievementType.SUMMARY.getTitle() : "", new TextButtonStyle(
                imageUp,
                imageDown,
                imageChecked,
                UIComponent.getSkin().getFont("button")
        ));

        this.type = type;
    }

    public AchievementType getType() {
        return this.type;
    }

    public void setType(AchievementType type) {
        this.type = type;
    }
}
