package com.deco2800.game.components.shop;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

public class ShopUtils {
    /**
     * 
     * @param text:       The text to display in the button
     * @param color:      The color for the font
     * @param down:       TextureRegionDrawable for when button is clicked
     * @param up:         TextureRegionDrawable for when button is not clicked
     * @param skin:       the default skin to set the font
     * @param isDisabled: true if you want the button to act as an image with text
     * @return TextButton with image
     */
    public static TextButton createImageTextButton(String text, Color color, String textString, float scale,
            TextureRegionDrawable down,
            TextureRegionDrawable up,
            Skin skin, Boolean isDisabled) {
        TextButtonStyle style = new TextButtonStyle(up, down, up, skin.getFont(textString));
        TextButton button = new TextButton(text, style);
        button.getLabel().setColor(color);
        button.setTransform(true);
        button.setOrigin(0, 0);
        button.setScale(scale);
        button.setDisabled(isDisabled);
        button.padBottom(3 * scale);
        return button;
    }
}
