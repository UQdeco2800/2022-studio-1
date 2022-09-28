package com.deco2800.game.components;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar;
import com.deco2800.game.areas.MainArea;
import com.deco2800.game.entities.Entity;
import com.deco2800.game.rendering.RenderComponent;
import com.deco2800.game.services.ServiceLocator;
import com.deco2800.game.utils.DrawableUtil;
import com.deco2800.game.utils.RenderUtil;

import java.util.Objects;


/**
 *  Renders the entities health bar. Requires CombatStatsComponent
 */
public class HealthBarComponent extends RenderComponent {

    private final ProgressBar progressBar;
    private CombatStatsComponent combatStatsComponent;

    private int fullHealth;

    private final int healthBarWidth;

    private final int healthBarHeight;

    private float pixelsPerUnit;
    private float entityWidthScale;
    private float entityHeightScale;

    private RenderUtil renderUtil;

    /**
     *
     * Constructs a new health bar
     *
     * @param width the width of the health bar in pixels
     * @param height the height of the healbar in pixels
     */
    public HealthBarComponent(int width, int height) {
        this.healthBarWidth = width;
        this.healthBarHeight = height;
        this.progressBar = new ProgressBar (0f,
                1f,
                0.01f,
                false,
                new ProgressBar.ProgressBarStyle());

        // if statement to customise crystal health bar

//        if(this.entity == ServiceLocator.getEntityService().getNamedEntity("crystal")) {
//            progressBar.getStyle().background = DrawableUtil
//                    .getRectangularColouredDrawable(width, height, Color.BROWN);
//            progressBar.getStyle().knob = DrawableUtil
//                    .getRectangularColouredDrawable(0, height, Color.VIOLET);
//            progressBar.getStyle().knobBefore = DrawableUtil
//                    .getRectangularColouredDrawable(width, height, Color.VIOLET);
//        } else {
            progressBar.getStyle().background = DrawableUtil
                    .getRectangularColouredDrawable(width, height, Color.RED);
            progressBar.getStyle().knob = DrawableUtil
                    .getRectangularColouredDrawable(0, height, Color.GREEN);
            progressBar.getStyle().knobBefore = DrawableUtil
                    .getRectangularColouredDrawable(width, height, Color.GREEN);
//        }
        progressBar.setWidth(width);
        progressBar.setHeight(height);

        progressBar.setAnimateDuration(0.0f);
        progressBar.setValue(1f);
        progressBar.setAnimateDuration(0.25f);
        renderUtil = RenderUtil.getInstance();
    }

    public void setCombatStatsComponent(CombatStatsComponent combatStatsComponent) {
        this.combatStatsComponent = combatStatsComponent;
    }

    /**
     * Returns the progress bar that represents the heath bar percentage to
     * being displayed
     *
     * @return the progress bar representing health
     */
    public ProgressBar getProgressBar() {
        return progressBar;
    }

    /**
     * For setting a renderUtil if instantiated as a mock or otherwise
     *
     * @param renderUtil the instance to use
     */
    public void setRenderUtil(RenderUtil renderUtil) {
        this.renderUtil = renderUtil;
    }

    /**
     * As soon as the entity is created we want to get its CombatStatsComponent.
     * This means the order in which you add the HealthBarComponent and CombatStatsComponent is
     * important. You are expected to have CombatStatsComponent first added.
     * fullHealth refers to the initial health of the entity when constructed
     */
    @Override
    public void create() {
        super.create(); // registers renderer
        var pos = this.getEntity().getPosition();
        this.progressBar.setPosition(pos.x, pos.y);
        this.setCombatStatsComponent(this.getEntity().getComponent(CombatStatsComponent.class));
        this.fullHealth = this.combatStatsComponent.getHealth();
        this.pixelsPerUnit = renderUtil.getPixelsPerUnit();
        this.entityWidthScale = this.getEntity().getScale().x;
        this.entityHeightScale = this.getEntity().getScale().y;
    }

    /**
     * When the render calls draw we render the health bar with
     * any adjustments made so it reflects the remaining or added heath
     * @param batch Batch to render to.
     */
    @Override
    protected void draw(SpriteBatch batch) {
        var entityCurrentPosition = this.getEntity().getPosition();
        /* Update progress bar*/
        this.progressBar.setValue( (float) this.combatStatsComponent.getHealth()/this.fullHealth);
        this.progressBar.updateVisualValue();

        /* We need these calculations to correctly position the health bar at the top of entity */

        // Added if statement to not display health bar on top of crystal
        //System.out.println(this.entity.getPosition());
        //Entity player = MainArea.getInstance().getGameArea().getPlayer();

        if (this.entity!= null) {
            if (!Objects.equals(this.entity.getName(),"player")&&!Objects.equals(this.entity.getName(),"crystal")) {

                    float healthBarXPos = ((entityCurrentPosition.x * pixelsPerUnit) + (entityWidthScale / 2 * pixelsPerUnit))
                            - (this.healthBarWidth / 2f);
                    float healthBarYPos = (entityCurrentPosition.y * pixelsPerUnit) + (entityHeightScale * pixelsPerUnit);
                    this.progressBar.setPosition(healthBarXPos, healthBarYPos);
            }

        }
        /* We need to temporarily render in pixels */
        if (batch != null) {
            renderUtil.renderInPixels(batch, () -> {
                this.progressBar.draw(batch, 1);
            });
        }
    }


}
