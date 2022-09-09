package com.deco2800.game.components.storyline;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.deco2800.game.services.ServiceLocator;
import com.deco2800.game.ui.UIComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A ui component for displaying the Main menu.
 */
public class StoryLineDisplay extends UIComponent {
    private static final Logger logger = LoggerFactory.getLogger(com.deco2800.game.components.storyline.StoryLineDisplay.class);
    private static final float Z_INDEX = 2f;
    private Table rootTable;


    @Override
    public void create() {
        super.create();
        addActors();
    }

    private void addActors() {

        rootTable = new Table();
        rootTable.setFillParent(true);

        Table mainTable = new Table();
        Table skipTable = new Table();
        //skipTable.setFillParent(true);
        skipTable.padLeft(1400).padTop(900);
        /*
        Image title =
                new Image(
                        ServiceLocator.getResourceService()
                                .getAsset("images/uiElements/exports/title.png", Texture.class));
        */

        // Background Colour
        Texture back = new Texture(Gdx.files.internal("test/files/storylineBackground.png"));
        Drawable storybackgroundTexture = new TextureRegionDrawable(back);
        rootTable.setBackground(storybackgroundTexture);

        // inserting home Button
        Texture homeButton1 = new Texture(Gdx.files.internal("test/files/skipButton.png"));
        TextureRegionDrawable homeUp = new TextureRegionDrawable(homeButton1);
        TextureRegionDrawable homeDown = new TextureRegionDrawable(homeButton1);
        ImageButton skipButton = new ImageButton(homeUp, homeDown);

//    TextButton startBtn = new TextButton("Start", skin);
//    TextButton loadBtn = new TextButton("Load", skin);
//    TextButton settingsBtn = new TextButton("Settings", skin);
//    TextButton exitBtn = new TextButton("Exit", skin);

        // Triggers an event when the button is pressed
        skipButton.addListener(
                new ChangeListener() {
                    @Override
                    public void changed(ChangeEvent changeEvent, Actor actor) {
                        logger.debug("Skip button clicked");
                        entity.getEvents().trigger("skip");
                    }
                });

        skipTable.add(skipButton).width(275).height(150);
        rootTable.add(skipTable);

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
        super.dispose();
    }
}