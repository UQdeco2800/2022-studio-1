package com.deco2800.game.utils;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Matrix4;
import com.deco2800.game.components.CameraComponent;
import com.deco2800.game.services.ServiceLocator;

/**
 * Graphic utilities to help with rendering
 */
public class RenderUtil {


    /**
     *
     * Temporarily swaps your projection to allow rendering in pixels.
     * This expects a callback function
     *
     * @param batch the batch to draw onto
     * @param camera the camera
     * @param runnable a callback method that includes draw calls
     */
    public static void renderInPixels(Batch batch, Camera camera, Runnable runnable) {
        float screenHeight = ServiceLocator.getRenderService().getStage().getViewport().getScreenHeight();
        float viewportHeight = camera.viewportHeight; // in units

        Matrix4 originalMatrix = batch.getProjectionMatrix().cpy();
        batch.setProjectionMatrix(originalMatrix.cpy()
                .scale(viewportHeight/screenHeight,
                        viewportHeight/screenHeight, 1));
        runnable.run(); // call various draw methods
        batch.setProjectionMatrix(originalMatrix); // go back to original projection
    }

    /**
     * Utility to get screen height
     *
     * @return the screen height in pixels
     */
    public static int getScreenHeight() {
       return ServiceLocator.getRenderService().getStage().getViewport().getScreenHeight();
    }

    /**
     * Uses EntityService to fetch the "camera" entity from which the camera component
     * can the be located.
     *
     * @return the located camera component
     */
    public static CameraComponent getCameraComponent() {
        var cameraEntity = ServiceLocator.getEntityService().getNamedEntity("camera");
        return cameraEntity.getComponent(CameraComponent.class);
    }

    /**
     * Calculates and returns the pixels per unit required
     *
     * @return pixels per unit
     */
    public static float getPixelsPerUnit() {
        float viewportHeight = getCameraComponent().getCamera().viewportHeight; // in units
        return getScreenHeight() /viewportHeight;
    }
}
