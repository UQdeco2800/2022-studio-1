package com.deco2800.game.components.storyline;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.deco2800.game.ui.UIComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.LinkedList;

/**
 * An ui component for displaying the storyline.
 */
public class prologueDisplay extends UIComponent {
    private static final Logger logger = LoggerFactory.getLogger(prologueDisplay.class);
    private Table rootTable;
    private LinkedList<String> prologueFrames;
    private int currentFrame;
    private int maxFrame;


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

        prologueFrames = new LinkedList<>();
        prologueFrames.add("images/StoryLine/prologue1_revised.png");
        prologueFrames.add("images/StoryLine/prologue2_revised.png");
        prologueFrames.add("images/StoryLine/prologue3_revised.png");
        prologueFrames.add("images/StoryLine/prologue4_revised.png");
        currentFrame = 0;
        maxFrame = 4;


        // load and set Background
        Texture storylineGradient = new Texture(Gdx.files.internal(prologueFrames.get(currentFrame)));
        TextureRegionDrawable storyBackgroundTexture = new TextureRegionDrawable(storylineGradient);
        rootTable.setBackground(storyBackgroundTexture);
        currentFrame += 1;

        stage.addActor(rootTable);
    }

    private void nextScreen() {
        if (currentFrame < maxFrame) {
            Drawable nextFrame = new TextureRegionDrawable(new Texture(Gdx.files.internal(prologueFrames.get(currentFrame))));
            rootTable.setBackground(nextFrame);
            currentFrame += 1;
        } else {
            prologueFrames.clear();
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