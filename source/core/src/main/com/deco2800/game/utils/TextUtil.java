package com.deco2800.game.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.ui.Label;

public class TextUtil {
    public static String lineFormat(String labelContent, int numberPerLine) {
        StringBuilder formattedString = new StringBuilder();
        int count = 0;
        for (String word: labelContent.split(" ")) {
            if (count + word.length() >= numberPerLine) {
                if (!word.contains("\n")) formattedString.append('\n');
                count = 0;
            } else if (word.contains("\n")) {
                count = 0;
            }
            formattedString.append(word).append(" ");
            count += word.length() + 1;
        }
        return formattedString.toString();
    }

    public static Label createTextLabel(String text, int textColour, float fontSize) {
        Color color = new Color(textColour);

        Label.LabelStyle fontStyle = new Label.LabelStyle();
        fontStyle.font = new BitmapFont(Gdx.files.internal("flat-earth/skin/fonts/pixel_32.fnt"));
        fontStyle.fontColor = color;

        Label formattedText = new Label(text, fontStyle);
        formattedText.setFontScale(fontSize / 32);

        return formattedText;
    }
}
