package com.deco2800.game.components.camera;

import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.math.Vector2;
import com.deco2800.game.input.InputComponent;
import com.deco2800.game.utils.math.Vector2Utils;

public class KeyboardCameraInputComponent extends InputComponent {
        private final Vector2 panDirection = Vector2.Zero.cpy();
        private boolean zoom = false;
        private boolean zoomOut = false;

        public KeyboardCameraInputComponent() {
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
                        case Keys.UP:
                                panDirection.add(Vector2Utils.UP);
                                triggerPanEvent();
                                return true;
                        case Keys.LEFT:
                                panDirection.add(Vector2Utils.LEFT);
                                triggerPanEvent();
                                return true;
                        case Keys.DOWN:
                                panDirection.add(Vector2Utils.DOWN);
                                triggerPanEvent();
                                return true;
                        case Keys.RIGHT:
                                panDirection.add(Vector2Utils.RIGHT);
                                triggerPanEvent();
                                return true;
                        case Keys.Z:
                                zoom = true;
                                triggerZoomEvent();
                                return true;
//                        case Keys.T:
//                                triggerScreenShake();
//                                return true;
                        case Keys.X:
                                zoomOut = true;
                                triggerZoomOutEvent();
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
                        case Keys.UP:
                                panDirection.sub(Vector2Utils.UP);
                                triggerPanEvent();
                                return true;
                        case Keys.LEFT:
                                panDirection.sub(Vector2Utils.LEFT);
                                triggerPanEvent();
                                return true;
                        case Keys.DOWN:
                                panDirection.sub(Vector2Utils.DOWN);
                                triggerPanEvent();
                                return true;
                        case Keys.RIGHT:
                                panDirection.sub(Vector2Utils.RIGHT);
                                triggerPanEvent();
                                return true;
                        case Keys.Z:
                                zoom = false;
                                triggerZoomEvent();
                                return true;
                        case Keys.X:
                                zoomOut = false;
                                triggerZoomOutEvent();
                                return true;
                        case Keys.P:
                                return true;
                        default:
                                return false;
                }
        }

        /**
         * Moves the camera in the given direction. Only if debug mode is on.
         */
        private void triggerPanEvent() {
                if (panDirection.epsilonEquals(Vector2.Zero)) {
                        entity.getEvents().trigger("panStop");
                } else {
                        entity.getEvents().trigger("pan", panDirection);
                }
        }

        /**
         * Zooms the camera in. Only if debug mode is on.
         */
        private void triggerZoomEvent() {
                if (zoom) {
                        entity.getEvents().trigger("zoom");
                } else {
                        entity.getEvents().trigger("zoomStop");
                }
        }

        private void triggerZoomOutEvent() {
                if (zoomOut) {
                        entity.getEvents().trigger("zoomOut");
                } else {
                        entity.getEvents().trigger("stopZoomOut");
                }
        }

        private void triggerScreenShake() {
                entity.getEvents().trigger("screenShake");
        }
}
