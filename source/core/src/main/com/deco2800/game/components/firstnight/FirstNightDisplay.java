package com.deco2800.game.components.firstnight;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.deco2800.game.ui.UIComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * An ui component for displaying the storyline.
 */
public class FirstNightDisplay  extends UIComponent {
    private static final Logger logger = LoggerFactory.getLogger(com.deco2800.game.components.firstnight.FirstNightDisplay.class);
    private static final float Z_INDEX = 2f;
    private Table rootTable;



    @Override
    public void create() {
        super.create();
        addActors();
    }

    private void addActors() {

        //create table and set positioning
        rootTable = new Table();
        rootTable.setFillParent(true);

        // load and set Background
        Texture storylineGradient = new Texture(Gdx.files.internal("images/StoryLine/FirstNight.png"));
        TextureRegionDrawable storyBackgroundTexture = new TextureRegionDrawable(storylineGradient);
        rootTable.setBackground(storyBackgroundTexture);

        // create a transparent screen to act as button
        // this allows the screens transition
        Drawable clear = new TextureRegionDrawable(new Texture(Gdx.files.internal("images/StoryLine/clearBackground.png")));
        ImageButton backButton = new ImageButton(clear, clear);

        // Triggers the transition to next frame when the screen is clicked
        backButton.addListener(
                new ChangeListener() {
                    @Override
                    public void changed(ChangeEvent changeEvent, Actor actor) {
                        logger.debug("next button clicked");
                        entity.getEvents().trigger("next");
                    }
                });

        //Add items to stage
        rootTable.add(backButton);
        stage.addActor(rootTable);
    }

    @Override
    public void draw(SpriteBatch batch) {
        // draw is handled by the stage
    }

    @Override
    public float getZIndex() {
        return Z_INDEX;
    }

    @Override
    public void dispose() {
        rootTable.clear();
        stage.clear();
        super.dispose();
    }
}