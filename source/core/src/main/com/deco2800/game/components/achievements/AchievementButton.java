package com.deco2800.game.components.achievements;

import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.deco2800.game.achievements.AchievementType;

/**
 * Extends ImageButton to store what type of achievement the button is representing
 */
public class AchievementButton extends ImageButton {
    private AchievementType type;

    public AchievementButton(Drawable imageUp, Drawable imageDown, Drawable imageChecked, AchievementType type) {
        super(imageUp, imageDown, imageChecked);

        this.type = type;
    }

    public AchievementType getType() {
        return this.type;
    }

    public void setType(AchievementType type) {
        this.type = type;
    }
}
