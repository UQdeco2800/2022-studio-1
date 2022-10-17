package com.deco2800.game.components.achievements;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.deco2800.game.areas.ForestGameArea;
import com.deco2800.game.components.CombatStatsComponent;
import com.deco2800.game.ui.UIComponent;
import com.deco2800.game.utils.DrawableUtil;
import com.deco2800.game.utils.RenderUtil;

public class AchievementProgressBar extends UIComponent {
    private final ProgressBar progressBar;

    private final int target;

    private int done;

    private boolean showPercentage;

    Table progressBarContent;

    Label label;

    public AchievementProgressBar(int width, int height, int max, boolean showPercentage) {
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

        this.showPercentage = showPercentage;

        this.target = max;
        this.buildActor();;
    }

    private void updateLabel() {
        String labelText = "";

        if (showPercentage) {
            float percentageComplete = (this.done/(float)this.target)*100f;
           labelText = "%.2f%%".formatted(percentageComplete);
        } else {
            labelText = "%d/%d".formatted(this.done, this.target);
        }

        if (label == null) {
            label = new Label(labelText, skin, ForestGameArea.SMALL_FONT);
        } else {
            label.setText(labelText);
        }
    }

    private void buildActor() {
        this.progressBarContent = new Table();
        this.progressBarContent.add(progressBar);
        progressBarContent.row();
        updateLabel();
        progressBarContent.add(label);
        progressBarContent.setDebug(true);
    }

    public Actor getActor() {
        return this.progressBarContent;
    }

    public void setDone(int amountDone) {
        this.done = amountDone;
        this.progressBar.setValue( (float) this.done/this.target);
        this.progressBar.updateVisualValue();
        updateLabel();
    }

    @Override
    protected void draw(SpriteBatch batch) {
        // do nothing
    }
}
