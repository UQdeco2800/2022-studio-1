package com.deco2800.game.components.storyline;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.deco2800.game.components.shop.ShopUtils;
import com.deco2800.game.components.storyline.frames.Frame;
import com.deco2800.game.components.storyline.frames.prologue1;
import com.deco2800.game.components.storyline.frames.prologue2;
import com.deco2800.game.components.storyline.frames.prologue3;
import com.deco2800.game.components.storyline.frames.prologue4;
import com.deco2800.game.ui.UIComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * An ui component for displaying the storyline.
 */
public class StoryLineDisplay extends UIComponent {
    private static final Logger logger = LoggerFactory.getLogger(com.deco2800.game.components.storyline.StoryLineDisplay.class);
    private static final float Z_INDEX = 2f;
    private Table rootTable;
    private Table skipTable;
/*    private Table charTable;
    private Table subsTable;
    private TextButton subtitlesDisplay;
    private Image sub;*/
    private StoryLinkedList<Frame> frameset;
    private Node<Frame> currentFrame;


    @Override
    public void create() {
        super.create();
        addActors();
    }

    private void addActors() {

        //create table and set positioning
        rootTable = new Table();
        rootTable.setFillParent(true);

        skipTable = new Table();
        skipTable.setFillParent(true);
        skipTable.right().bottom().padRight(50).padBottom(50);

        /*

        //Could be used to subimppose a layer later

        charTable = new Table();
        charTable.setFillParent(true);
        charTable.center().padTop(100);

        subsTable = new Table();
        subsTable.setFillParent(true);
        subsTable.center().bottom().padBottom(50);
        */

        //load the epilogue screens
        frameset = new StoryLinkedList<>();
        frameset.add(new prologue1());
        frameset.add(new prologue2());
        frameset.add(new prologue3());
        frameset.add(new prologue4());
        currentFrame = frameset.header;

        // load and set Background
        Texture storylineGradient = new Texture(Gdx.files.internal(currentFrame.f.getBackground()));
        TextureRegionDrawable storyBackgroundTexture = new TextureRegionDrawable(storylineGradient);
        rootTable.setBackground(storyBackgroundTexture);

        // create a transparent screen to act as button
        // this allows the screens transition
        Drawable clear = new TextureRegionDrawable(new Texture(Gdx.files.internal("images/StoryLine/clearBackground.png")));
        ImageButton backButton = new ImageButton(clear, clear);

        // inserting skip Button
        Texture skipButton1 = new Texture(Gdx.files.internal("images/StoryLine/skipButton.png"));
        TextureRegionDrawable skipUp = new TextureRegionDrawable(skipButton1);
        TextureRegionDrawable skipDown = new TextureRegionDrawable(skipButton1);
        ImageButton skipButton = new ImageButton(skipUp, skipDown);

/*
        // load the character image
        Texture currentSubTexture = new Texture(Gdx.files.internal(currentFrame.f.getCharacters()));
        sub = new Image(currentSubTexture);

        // load the empty dialogue box and populate with text
        Texture empty = new Texture(Gdx.files.internal("test/files/emptyDialogue.png"));
        TextureRegionDrawable testDisplay = new TextureRegionDrawable(empty);
        subtitlesDisplay = ShopUtils.createImageTextButton(
                currentFrame.f.getSubtitles(),
                skin.getColor("white"),
                "button", 1f,
                testDisplay, testDisplay, skin,
                true);
*/


        // Triggers an event when the button is pressed
        skipButton.addListener(
                new ChangeListener() {
                    @Override
                    public void changed(ChangeEvent changeEvent, Actor actor) {
                        logger.debug("Skip button clicked");
                        entity.getEvents().trigger("skip");
                    }
                });

        // Triggers the transition to next frame when the screen is clicked
        backButton.addListener(
                new ChangeListener() {
                    @Override
                    public void changed(ChangeEvent changeEvent, Actor actor) {
                        logger.debug("next button clicked");
                        if (frameset.header.next != null) {
                            currentFrame = frameset.header.next;
                            frameset.header = frameset.header.next;

                            //subtitlesDisplay.setText(currentFrame.f.getSubtitles());

                            Drawable temp = new TextureRegionDrawable(new Texture(Gdx.files.internal(currentFrame.f.getBackground())));
                            rootTable.setBackground(temp);

                            //sub.setDrawable(new TextureRegionDrawable(new Texture(Gdx.files.internal(currentFrame.f.getCharacters()))));
                            skipTable.clear();          //clears the skip button after 1st frame

                        } else {
                            entity.getEvents().trigger("skip");
                        }

                    }
                });

        //Add items to stage
        rootTable.add(backButton);
/*        subsTable.add(subtitlesDisplay).width(500).height(75);
        charTable.add(sub).width(500).height(75);*/
        skipTable.add(skipButton).width(275).height(150);

        stage.addActor(rootTable);
/*        stage.addActor(subsTable);
        stage.addActor(charTable);*/
        stage.addActor(skipTable);
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
/*
        subsTable.clear();
        charTable.clear();
*/
        skipTable.clear();
        stage.clear();
        super.dispose();
    }
}