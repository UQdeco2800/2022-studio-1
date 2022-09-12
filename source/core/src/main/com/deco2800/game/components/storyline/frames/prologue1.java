package com.deco2800.game.components.storyline.frames;

/**
 * prologue1 is child class of Frame
 */
public class prologue1 extends Frame {

    static String SUBTITLES = "This is frame 1";
    static String BACKGROUND = "test/files/storylineBackground.png";
    static String CHARACTER = "test/files/sub1.png";

    public prologue1() {
        super(SUBTITLES, BACKGROUND, CHARACTER);
    }

}