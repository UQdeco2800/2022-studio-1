package com.deco2800.game.components;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar;
import com.deco2800.game.rendering.RenderComponent;
import com.deco2800.game.utils.DrawableUtil;
import com.deco2800.game.utils.RenderUtil;


/**
 *  Renders the entities health bar. Requires CombatStatsComponent
 */
public class HealthBarComponent extends RenderComponent {

    private final ProgressBar progressBar;
    private CombatStatsComponent combatStatsComponent;

    private CameraComponent cameraComponent;

    private int fullHealth;

    private final int healthBarWidth;

    private final int healthBarHeight;

    private float pixelsPerUnit;
    private float entityWidthScale;
    private float entityHeightScale;

    public HealthBarComponent(int width, int height) {
        this.healthBarWidth = width;
        this.healthBarHeight = height;
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
        this.cameraComponent = RenderUtil.getCameraComponent();
        this.setCombatStatsComponent(this.getEntity().getComponent(CombatStatsComponent.class));
        this.fullHealth = this.combatStatsComponent.getHealth();
        this.pixelsPerUnit = RenderUtil.getPixelsPerUnit();
        this.entityWidthScale = this.getEntity().getScale().x;
        this.entityHeightScale = this.getEntity().getScale().y;
    }

    @Override
    protected void draw(SpriteBatch batch) {
        var entityCurrentPosition = this.getEntity().getPosition();
        /* Update progress bar*/
        this.progressBar.setValue( (float) this.combatStatsComponent.getHealth()/this.fullHealth);
        this.progressBar.updateVisualValue();

        /* We need these calculations to correctly position the health bar at the top of entity */
        float healthBarXPos = ((entityCurrentPosition.x * pixelsPerUnit) + (entityWidthScale/2 * pixelsPerUnit))
                - (this.healthBarWidth / 2f);
        float healthBarYPos = (entityCurrentPosition.y *pixelsPerUnit) + (entityHeightScale * pixelsPerUnit);
        this.progressBar.setPosition(healthBarXPos , healthBarYPos);

        /* We need to temporarily render in pixels */
        RenderUtil.renderInPixels(batch, () -> {
            this.progressBar.draw(batch, 1);
        });
    }


}
