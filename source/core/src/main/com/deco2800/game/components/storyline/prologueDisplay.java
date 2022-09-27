package com.deco2800.game.components.storyline;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.deco2800.game.components.storyline.prologue.*;
import com.deco2800.game.ui.UIComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * An ui component for displaying the storyline.
 */
public class prologueDisplay extends UIComponent {
    private static final Logger logger = LoggerFactory.getLogger(prologueDisplay.class);
    private Table rootTable;
    private StoryLinkedList<Frame> frameset;
    private Node<Frame> currentFrame;


    @Override
    public void create() {
        super.create();
        addActors();
        entity.getEvents().addListener("next", this::nextScreen);
    }

    private void addActors() {

        //create table and set positioning
        rootTable = new Table();
        rootTable.setFillParent(true);

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

        stage.addActor(rootTable);
    }

    private void nextScreen() {
        if (frameset.header.next != null) {
            currentFrame = frameset.header.next;
            frameset.header = frameset.header.next;

            Drawable temp = new TextureRegionDrawable(new Texture(Gdx.files.internal(currentFrame.f.getBackground())));
            rootTable.setBackground(temp);

        } else {
            entity.getEvents().trigger("skip");
        }
    }

    @Override
    public void draw(SpriteBatch batch) {
        // draw is handled by the stage
    }

    @Override
    public void dispose() {
        rootTable.clear();
        stage.clear();
        super.dispose();
    }
}