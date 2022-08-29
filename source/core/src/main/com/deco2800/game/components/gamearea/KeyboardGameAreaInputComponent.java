package com.deco2800.game.components.gamearea;

import com.badlogic.gdx.Input.Keys;
import com.deco2800.game.input.InputComponent;

public class KeyboardGameAreaInputComponent extends InputComponent {

        public KeyboardGameAreaInputComponent() {
                super(5);
        }

        /**
         * Triggers Camera events on key press.
         * 
         * @return whether or not the input was processed
         * @see InputProcessor#keyDown(int)
         */
        @Override
        public boolean keyDown(int key) {
                switch (key) {
                        case Keys.PLUS:
                                triggerExpandIslandEvent();
                                return true;
                        default:
                                return false;
                }
        }

        /**
         * Triggers Camera events on key press.
         * 
         * @return whether or not the input was processed
         * @see InputProcessor#keyUp(int)
         */
        @Override
        public boolean keyUp(int key) {
                switch (key) {
                        case Keys.PLUS:
                                return true;
                        default:
                                return false;
                }
        }

        /**
         * Moves the camera in the given direction. Only if debug mode is on.
         */
        private void triggerExpandIslandEvent() {
                entity.getEvents().trigger("expandIsland");
        }

}