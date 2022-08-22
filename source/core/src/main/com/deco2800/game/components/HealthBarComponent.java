package com.deco2800.game.components;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar;
import com.deco2800.game.rendering.RenderComponent;
import com.deco2800.game.rendering.RenderService;
import com.deco2800.game.services.ServiceLocator;
import com.deco2800.game.utils.DrawableUtil;


/**
 *  Renders the entities health bar. Requires CombatStatsComponent
 */
public class HealthBarComponent extends RenderComponent {

    private final ProgressBar progressBar;
    private float viewportHeight;
    private int screenHeight;
    private CombatStatsComponent combatStatsComponent;

    private int fullHealth;

    public HealthBarComponent(int width, int height) {
        this.progressBar = new ProgressBar (0f,
                1f,
                0.01f,
                false,
                new ProgressBar.ProgressBarStyle());
        progressBar.getStyle().background = DrawableUtil
                .getRectangularColouredDrawable(width, height, Color.RED);
        progressBar.getStyle().knob = DrawableUtil
                .getRectangularColouredDrawable(0, height, Color.GREEN);
        progressBar.getStyle().knobBefore = DrawableUtil
                .getRectangularColouredDrawable(width, height, Color.GREEN);

        progressBar.setWidth(width);
        progressBar.setHeight(height);

        progressBar.setAnimateDuration(0.0f);
        progressBar.setValue(1f);
        progressBar.setAnimateDuration(0.25f);
    }

    public void setCombatStatsComponent(CombatStatsComponent combatStatsComponent) {
        this.combatStatsComponent = combatStatsComponent;
    }

    @Override
    public void create() {
        super.create(); // registers renderer
        var pos = this.getEntity().getPosition();
        this.progressBar.setPosition(pos.x, pos.y);
        RenderService renderService = ServiceLocator.getRenderService();
        var cameraEntity = ServiceLocator.getEntityService().getNamedEntity("camera");
        var cameraComponent = cameraEntity.getComponent(CameraComponent.class);
        this.setCombatStatsComponent(this.getEntity().getComponent(CombatStatsComponent.class));
        this.fullHealth = this.combatStatsComponent.getHealth();
        this.screenHeight = renderService.getStage().getViewport().getScreenHeight();
        this.viewportHeight = cameraComponent.getCamera().viewportHeight; // in units

    }

    @Override
    protected void draw(SpriteBatch batch) {
        float pixelsPerUnit = this.screenHeight/this.viewportHeight;
        var entityCurrentPosition = this.getEntity().getPosition();
        this.progressBar.setValue( (float) this.combatStatsComponent.getHealth()/this.fullHealth);
        this.progressBar.updateVisualValue();
        this.progressBar.setPosition(entityCurrentPosition.x * pixelsPerUnit, entityCurrentPosition.y * pixelsPerUnit);
        Matrix4 originalMatrix = batch.getProjectionMatrix().cpy();
        batch.setProjectionMatrix(originalMatrix.cpy().scale(this.viewportHeight/this.screenHeight, this.viewportHeight/this.screenHeight, 1));
        this.progressBar.draw(batch, 1);
        batch.setProjectionMatrix(originalMatrix); // go back to original projection
    }

}
