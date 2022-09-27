package com.deco2800.game.components.storyline;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.deco2800.game.components.storyline.epilogue.*;
import com.deco2800.game.ui.UIComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class epilogueDisplay extends UIComponent {
    private static final Logger logger = LoggerFactory.getLogger(prologueDisplay.class);
    private static final float Z_INDEX = 2f;
    private Table rootTable;
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

        //load the epilogue screens
        frameset = new StoryLinkedList<>();
        frameset.add(new epilogue1());
        frameset.add(new epilogue2());
        frameset.add(new epilogue3());
        currentFrame = frameset.header;

        // load and set Background
        Texture storylineGradient = new Texture(Gdx.files.internal(currentFrame.f.getBackground()));
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
                        if (frameset.header.next != null) {
                            currentFrame = frameset.header.next;
                            frameset.header = frameset.header.next;

                            //subtitlesDisplay.setText(currentFrame.f.getSubtitles());

                            Drawable temp = new TextureRegionDrawable(new Texture(Gdx.files.internal(currentFrame.f.getBackground())));
                            rootTable.setBackground(temp);

                            //sub.setDrawable(new TextureRegionDrawable(new Texture(Gdx.files.internal(currentFrame.f.getCharacters()))));

                        } else {
                            entity.getEvents().trigger("skip");
                        }

                    }
                });

        //Add items to stage
        rootTable.add(backButton);
/*        subsTable.add(subtitlesDisplay).width(500).height(75);
        charTable.add(sub).width(500).height(75);*/
        stage.addActor(rootTable);
/*        stage.addActor(subsTable);
        stage.addActor(charTable);*/
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
        stage.clear();
        super.dispose();
    }
}