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

public class epilogueDisplay extends UIComponent {

    private static final Logger logger = LoggerFactory.getLogger(epilogueDisplay.class);
    private Table rootTable;
    private LinkedList<String> epilogueFrames;
    private int currentFrame;
    private int maxFrame;


    @Override
    public void create() {
        super.create();
        addActors();
        //entity.getEvents().addListener("next", this::nextScreen);
    }

    private void addActors() {

        //create table and set positioning
        rootTable = new Table();
        rootTable.setFillParent(true);

        epilogueFrames = new LinkedList<>();
        epilogueFrames.add("images/StoryLine/SL_1.png");
        epilogueFrames.add("images/StoryLine/SL_2.png");
        epilogueFrames.add("images/StoryLine/SL_3.png");
        currentFrame = 0;
        maxFrame = 3;


        // load and set Background
        Texture storylineGradient = new Texture(Gdx.files.internal(epilogueFrames.get(currentFrame)));
        TextureRegionDrawable storyBackgroundTexture = new TextureRegionDrawable(storylineGradient);
        rootTable.setBackground(storyBackgroundTexture);
        currentFrame += 1;

        stage.addActor(rootTable);
    }

    private void nextScreen() {
        if (currentFrame < maxFrame) {
            Drawable nextFrame = new TextureRegionDrawable(new Texture(Gdx.files.internal(epilogueFrames.get(currentFrame))));
            rootTable.setBackground(nextFrame);
            currentFrame += 1;
        } else {
            epilogueFrames.clear();
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