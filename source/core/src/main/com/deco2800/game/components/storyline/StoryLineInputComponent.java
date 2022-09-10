package com.deco2800.game.components.storyline;

import com.badlogic.gdx.Input;
import com.deco2800.game.input.InputComponent;

public class StoryLineInputComponent extends InputComponent {

    @Override
    public boolean keyDown(int key) {
        switch (key) {
            case Input.Keys.SPACE:
                return true;
            case Input.Keys.S:
                entity.getEvents().trigger("skip");
                return true;
            default:
                return false;
        }
    }

    @Override
    public boolean keyUp(int key) {
        switch (key) {
            case Input.Keys.SPACE:
                return true;
            default:
                return false;
        }
    }




}
