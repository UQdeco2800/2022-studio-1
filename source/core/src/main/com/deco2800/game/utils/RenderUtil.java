package com.deco2800.game.utils;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Matrix4;
import com.deco2800.game.components.CameraComponent;
import com.deco2800.game.services.ServiceLocator;

/**
 * Graphic utilities to help with rendering
 */
public class RenderUtil {

    private static RenderUtil instance = null;

    private RenderUtil () {
    }

    /**
     *
     * Temporarily swaps your projection to allow rendering in pixels.
     * This expects a callback function
     *
     * @param batch the batch to draw onto
     * @param runnable a callback method that includes draw calls
     */
    public  void renderInPixels(Batch batch, Runnable runnable) {
        float screenHeight = getScreenHeight();
        float viewportHeight = getCameraComponent().getCamera().viewportHeight;

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
    public  int getScreenHeight() {
        if (ServiceLocator.getRenderService().getStage() == null) {
            return 0;
        }
       return ServiceLocator.getRenderService().getStage().getViewport().getScreenHeight();
    }

    /**
     * Uses EntityService to fetch the "camera" entity from which the camera component
     * can then be located.
     *
     * @return the located camera component
     */
    public  CameraComponent getCameraComponent() {
        var cameraEntity = ServiceLocator.getEntityService().getNamedEntity("camera");
        return cameraEntity.getComponent(CameraComponent.class);
    }

    /**
     * Calculates and returns the pixels per unit required
     *
     * @return pixels per unit
     */
    public  float getPixelsPerUnit() {
        float viewportHeight = getCameraComponent().getCamera().viewportHeight; // in units
        return getScreenHeight() /viewportHeight;
    }

    public static RenderUtil getInstance() {
        if (instance == null) {
            instance = new RenderUtil();
        }

        return instance;
    }
}
