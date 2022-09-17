package com.deco2800.game.components.storyline.prologue;

public abstract class Frame {

    protected String subtitles;
    protected String background;
    protected String characters;

    /**
     * @param subtitles         subtitles in text format, this will be written inside the empty dialogue box
     * @param background        path for the background image for the frame
     * @param characters        path for the image file for the characters overly on background
     */
    public Frame(String subtitles, String background, String characters) {
        this.subtitles = subtitles;
        this.background = background;
        this.characters = characters;
    }

    /**
     * Getter for subtitles text
     * @return String subtitles
     */
    public String getSubtitles() {
        return this.subtitles;
    }
    /**
     * Getter for background image path
     * @return String background path
     */
    public String getBackground() {
        return this.background;
    }
    /**
     * Getter for character image path
     * @return String character path
     */
    public String getCharacters() {
        return this.characters;
    }
}