package com.deco2800.game.components.mainmenu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.deco2800.game.components.loadingPage.LoadingDisplay;
import com.deco2800.game.entities.Entity;
import com.deco2800.game.services.ServiceLocator;
import com.deco2800.game.ui.UIComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class loader extends UIComponent {

    private static final Logger logger = LoggerFactory.getLogger(LoadingDisplay.class);
    private Table loadTable;
    private Table loadingStat;
    private Table loadingTips;
    private Label currLoadProgress;

    @Override
    public void create() {
        super.create();
        addActors();
        Entity menu = ServiceLocator.getEntityService().getNamedEntity("menu");
        menu.getEvents().addListener("loadStatUpdate", this::updateLoadingStat);
    }

    public Table getLoadDisplay() {
        return loadTable;
    }

    public void loadUpdate() {
        if (loadTable == null) {
            loadTable = displayLoadScreen();
            return;
        }
        Texture colour = new Texture(Gdx.files.internal("images/StoryLine/SL_1.png"));
        Drawable backgroundColour = new TextureRegionDrawable(colour);
        loadTable.setBackground(backgroundColour);
    }

    public Table displayLoadScreen() {

        loadTable = new Table();
        loadTable.setFillParent(true);

        //create table and set positioning
        loadingStat = new Table();
        loadingStat.setFillParent(true);
        loadingStat.left().padBottom(100);

        loadingTips = new Table();
        loadingTips.setFillParent(true);
        loadingTips.left().padBottom(60);

        // load and set Background
        //TODO: Replace the background with loading screen design
        Texture loadingScreenTexture = new Texture(Gdx.files.internal("images/StoryLine/SL_1.png"));
        TextureRegionDrawable loadingScreenBackground = new TextureRegionDrawable(loadingScreenTexture);
        loadTable.setBackground(loadingScreenBackground);

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

        loadTable.row();
        loadTable.add(loadingTips).expandY().bottom();
        loadTable.row();
        loadTable.add(loadingStat);

        stage.addActor(loadTable);

        return loadTable;
    }

    public void updateLoadingStat(float loadProgress) {
        CharSequence newLoadStat = String.format("Loading:  %f", loadProgress);
        currLoadProgress.setText(newLoadStat);
        if (loadProgress >= 95) {
            logger.debug("Loading complete");
            stage.clear();
        }
    }

    private void addActors() {
    }

    @Override
    public void draw(SpriteBatch batch) {
        // draw is handled by the stage
    }

    @Override
    public void dispose() {
        loadTable.clear();
        loadingStat.clear();
        loadingTips.clear();
        stage.clear();
        super.dispose();
    }

}

