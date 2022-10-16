package com.deco2800.game.components.camera;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.Vector2;
import com.deco2800.game.areas.terrain.TerrainComponent;
import com.deco2800.game.components.CameraComponent;
import com.deco2800.game.components.CombatStatsComponent;
import com.deco2800.game.components.Component;
import com.deco2800.game.services.DayNightCycleStatus;
import com.deco2800.game.services.ServiceLocator;

import java.util.Timer;
import java.util.TimerTask;


public class CameraActions extends Component {
        private boolean panning = false;
        private boolean playerMoving = false;
        private Vector2 playerPosition = Vector2.Zero.cpy();
        private boolean zoomIn = false;
        private boolean zoomOut = false;
        private boolean screenShake = false;
        boolean interval = false;
        long cameraShakeStartTime;
        private Vector2 panDirection = Vector2.Zero.cpy();

        @Override
        public void create() {
                entity.getEvents().addListener("pan", this::pan);
                entity.getEvents().addListener("stopPan", this::stopPan);
                entity.getEvents().addListener( "playerMovementPan", this::playerMovementPan);
                entity.getEvents().addListener("stopPlayerMovementPan", this::stopPlayerMovementPan);
                entity.getEvents().addListener("zoom", this::zoom);
                entity.getEvents().addListener("zoomStop", this::stopZoom);
                entity.getEvents().addListener("stopZoomOut", this::stopZoomOut);
                entity.getEvents().addListener("zoomOut", this::zoomOut);
                entity.getEvents().addListener("screenShake", this::startScreenShake);

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
         * Sets the position of the camera to the players position in response to
         * players movement.
         * Re-attaches camera to player, and disables manual panning
         * 
         * @param position location of camera
         */
        void playerMovementPan(Vector2 position) {
                this.playerMoving = true;
                this.playerPosition = position;
        }

        /**
         * Stops the camera from panning (upon player not moving)
         */
        void stopPlayerMovementPan() {
                this.playerMoving = false;
        }

        public void startScreenShake(){
                this.screenShake = true;
                this.cameraShakeStartTime = ServiceLocator.getTimeSource().getTime();

        }

        void shake(){

                CameraComponent cameraComp = entity.getComponent(CameraComponent.class);
                OrthographicCamera camera = (OrthographicCamera) cameraComp.getCamera();
                long currentGameTime = ServiceLocator.getTimeSource().getTime();
                final int[] i = {0};

                Timer time = new Timer();
                TimerTask shake = new TimerTask() {
                        @Override
                        public void run() {
                                if (i[0] == 10){
                                        System.out.println("stop");
                                        time.cancel();
                                }
                                if ( !interval) {
                                        camera.translate(+10,10);
                                        interval = true;
                                } else {
                                        camera.translate(-10,-10);
                                        interval = false;
                                }
                                i[0]++;

                        }
                };
                time.scheduleAtFixedRate(shake, 200, 200);




        }
        /**
         * Updates the current position of the camera.
         */
        @Override
        public void update() {
                CameraComponent cameraComp = entity.getComponent(CameraComponent.class);
                OrthographicCamera camera = (OrthographicCamera) cameraComp.getCamera();
                if (playerMoving) {

                        Vector2 intermediatePosition = new Vector2(camera.position.x, camera.position.y);
                        intermediatePosition.interpolate(playerPosition, 0.1f, Interpolation.swing);

                        Vector2 difference = new Vector2(camera.position.x - intermediatePosition.x,
                                        camera.position.y - intermediatePosition.y);

                        camera.translate(difference);
                        camera.update();
                } else if (panning) {
                        camera.translate(panDirection.x / 3,
                                        panDirection.y / 3);
                        camera.update();
                }

                if (zoomIn) {
                        float newZoomValue = camera.zoom - 0.02f;
                        if (newZoomValue > 0) {
                                camera.zoom = newZoomValue;
                                camera.update();
                        }

                }

                if (zoomOut) {
                        float newZoomValue = camera.zoom + 0.02f;
                        if (newZoomValue <= 6) {
                                camera.zoom = newZoomValue;
                                camera.update();
                        }
                }

                if (screenShake) {
                        shake();
                        camera.update();
                        screenShake = false;
                 }

        }

}
