package com.deco2800.game.components.storyline.frames;

public abstract class Frame {

    protected String subtitles;
    protected String background;
    protected String characters;

    public Frame(String subtitles, String background, String characters) {
        this.subtitles = subtitles;
        this.background = background;
        this.characters = characters;
    }

    public String getSubtitles() {
        return this.subtitles;
    }

    public String getBackground() {
        return this.background;
    }

    public String getCharacters() {
        return this.characters;
    }
}