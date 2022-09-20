package com.deco2800.game.components.camera;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.deco2800.game.components.CameraComponent;
import com.deco2800.game.components.Component;

public class CameraActions extends Component {
        private boolean panning = false;
        private boolean zoomIn = false;
        private boolean zoomOut = false;
        private Vector2 panDirection = Vector2.Zero.cpy();

        @Override
        public void create() {
                entity.getEvents().addListener("pan", this::pan);
                entity.getEvents().addListener("stopPan", this::stopPan);
                entity.getEvents().addListener("zoom", this::zoom);
                entity.getEvents().addListener("zoomStop", this::stopZoom);
                entity.getEvents().addListener("stopZoomOut", this::stopZoomOut);
                entity.getEvents().addListener("zoomOut", this::zoomOut);
        }

        /**
         * Zooms the camera.
         */
        void zoom() {
                this.zoomIn = true;
        }

        /**
         * Stops the camera from zooming.
         */
        void stopZoom() {
                this.zoomIn = false;
        }

        void zoomOut() {
                this.zoomOut = true;
        }

        void stopZoomOut() {
                this.zoomOut = false;
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
                OrthographicCamera camera = (OrthographicCamera) cameraComp.getCamera();
                if (panning) {
                        camera.translate(panDirection.x / 3,
                                        panDirection.y / 3,
                                        0);
                        camera.update();
                }

                if (zoomIn) {
                        float newZoomValue = camera.zoom - 0.05f;
                        if (newZoomValue > 0) {
                                camera.zoom = newZoomValue;
                                camera.update();
                        }

                }

                if (zoomOut) {
                        float newZoomValue = camera.zoom + 0.05f;
                        if (newZoomValue <= 2) {
                                camera.zoom += 0.05f;
                                camera.update();
                        }
                }
        }

}
