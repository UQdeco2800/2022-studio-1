package com.deco2800.game.components.storyline;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.deco2800.game.input.InputComponent;

/**
 * Input handler for the storyline UI for keyboard input.
 * This input handler only uses keyboard input.
 */
public class storyLineInputComponent extends InputComponent {

    /**
     * Triggers storyline UI events on specific keycodes.
     *
     * @return whether the input was processed
     * @see InputProcessor#keyUp(int)
     */
    @Override
    public boolean keyUp(int key) {
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

}
