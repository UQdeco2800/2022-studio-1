package com.deco2800.game.components.maingame;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.deco2800.game.components.storyline.epilogueDisplay;
import com.deco2800.game.services.ServiceLocator;
import com.deco2800.game.ui.UIComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EpilogueLayover extends UIComponent {

    private static final Logger logger = LoggerFactory.getLogger(epilogueDisplay.class);
    private static boolean epilogueActive = false;
    private Table rootTable;
    private ImageButton backButton;
    private Table epilogueLose;
    private Table epilogueWin;


    @Override
    public void create() {
        super.create();
        addActors();
        ServiceLocator.getEntityService().getNamedEntity("crystal").getEvents().addListener("crystalDeath", this::onDeath);
    }

    private void onDeath() {
        epilogueActive = true;
        stage.addActor(epilogueLose);
        stage.addActor(backButton);
    }

    private void addActors() {

        rootTable = new Table();
        rootTable.setFillParent(true);

        //create table and set positioning
        epilogueLose = new Table();
        epilogueLose.setFillParent(true);

        epilogueWin = new Table();
        epilogueWin.setFillParent(true);

        // load and set Background
        Texture storylineLoseGradient = new Texture(Gdx.files.internal("images/StoryLine/VictoryScreen.png"));
        TextureRegionDrawable storyBackgroundLoseTexture = new TextureRegionDrawable(storylineLoseGradient);
        epilogueLose.setBackground(storyBackgroundLoseTexture);

        Texture storylineWinGradient = new Texture(Gdx.files.internal("images/StoryLine/VictoryScreen.png"));
        TextureRegionDrawable storyBackgroundWinTexture = new TextureRegionDrawable(storylineWinGradient);
        epilogueWin.setBackground(storyBackgroundWinTexture);

        Drawable clear = new TextureRegionDrawable(new Texture(Gdx.files.internal("images/StoryLine/clearBackground.png")));
        backButton = new ImageButton(clear, clear);

        backButton.addListener(
                new ChangeListener() {
                    @Override
                    public void changed(ChangeEvent changeEvent, Actor actor) {
                        logger.debug("next button clicked");
                        entity.getEvents().trigger("exit");
                    }
                });
    }

    @Override
    public void draw(SpriteBatch batch) {
        // draw is handled by the stage
    }

    @Override
    public void dispose() {
        epilogueLose.clear();
        epilogueWin.clear();
        rootTable.clear();
        stage.clear();
        super.dispose();
    }


}
