package com.deco2800.game.components.loadingPage;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.deco2800.game.services.ServiceLocator;
import com.deco2800.game.ui.UIComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class LoadingDisplay extends UIComponent {

    private static final Logger logger = LoggerFactory.getLogger(LoadingDisplay.class);
    private Table rootTable;
    private Table loadingStat;
    private Table loadingTips;
    private Label currLoadProgress;

    @Override
    public void create() {
        super.create();
        addActors();
        entity.getEvents().addListener("loadStatUpdate", this::updateLoadingStat);
    }

    private void addActors() {

        rootTable = new Table();
        rootTable.setFillParent(true);

        //create table and set positioning
        loadingStat = new Table();
        loadingStat.setFillParent(true);
        loadingStat.bottom().padBottom(100);

        loadingTips = new Table();
        loadingTips.setFillParent(true);
        loadingTips.bottom().padBottom(60);


        // load and set Background
        //TODO: Replace the background with loading screen design
        Texture loadingScreenTexture = new Texture(Gdx.files.internal("images/StoryLine/SL_1.png"));
        TextureRegionDrawable loadingScreenBackground = new TextureRegionDrawable(loadingScreenTexture);
        rootTable.setBackground(loadingScreenBackground);

        // display the loading stat number
        int loadProgress = 0;
        CharSequence LoadStat = String.format("Loading:  %d", loadProgress);
        currLoadProgress = new Label(String.valueOf(LoadStat), skin, "large");

        // display the tips
        //TODO: make a list / LinkedList of tips to pick randomly from
        CharSequence getTips = "insert tips here";
        Label displayTips = new Label(getTips, skin, "large");

        loadingTips.add(displayTips);
        loadingStat.add(currLoadProgress);

        stage.addActor(rootTable);
        stage.addActor(loadingStat);
        stage.addActor(loadingTips);

    }

    public void updateLoadingStat(int loadProgress) {
        CharSequence newLoadStat = String.format("Loading:  %d", loadProgress);
        currLoadProgress.setText(newLoadStat);
        if (loadProgress >= 100) {
            logger.debug("Loading complete");
            entity.getEvents().trigger("loadingFinish");
        }
    }

    @Override
    public void draw(SpriteBatch batch) {
        // draw is handled by the stage
    }

    @Override
    public void dispose() {
        rootTable.clear();
        loadingStat.clear();
        loadingTips.clear();
        stage.clear();
        super.dispose();
    }

}
