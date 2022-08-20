package com.deco2800.game.components.camera;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.math.Vector2;
import com.deco2800.game.components.CameraComponent;
import com.deco2800.game.components.Component;

public class CameraActions extends Component {
        private boolean panning = false;
        private boolean zoom = false;
        private Vector2 panDirection = Vector2.Zero.cpy();
        private boolean debug = false;

        @Override
        public void create() {
                entity.getEvents().addListener("pan", this::pan);
                entity.getEvents().addListener("stopPan", this::stopPan);
                entity.getEvents().addListener("zoom", this::zoom);
                entity.getEvents().addListener("zoomStop", this::stopZoom);
                entity.getEvents().addListener("toggleDebug", this::toggleDebug);
        }

        /**
         * Toggles debug mode.
         */
        void toggleDebug() {
                this.debug = !this.debug;
        }

        /**
         * Zooms the camera.
         */
        void zoom() {
                this.zoom = true;
        }

        /**
         * Stops the camera from zooming.
         */
        void stopZoom() {
                this.zoom = false;
        }

        /**
         * Pans the camera in the chosen direction.
         * 
         * @param direction direction to pan towards
         */
        void pan(Vector2 direction) {
                this.panDirection = direction;
                this.panning = true;

        }

        /**
         * Stops the camera from panning
         */
        void stopPan() {
                this.panDirection = Vector2.Zero.cpy();
                this.panning = false;
        }

        /**
         * Updates the current position of the camera.
         */
        @Override
        public void update() {
                CameraComponent cameraComp = entity.getComponent(CameraComponent.class);
                Camera camera = cameraComp.getCamera();
                if (panning && this.debug) {
                        camera.translate(panDirection.x,
                                        panDirection.y,
                                        0);
                        camera.update();
                }

                if (zoom) {
                        // zoom
                }
        }

}
