package com.deco2800.game.components.mainmenu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.WidgetGroup;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.deco2800.game.components.loadingPage.LoadingDisplay;
import com.deco2800.game.entities.Entity;
import com.deco2800.game.services.ServiceLocator;
import com.deco2800.game.ui.UIComponent;
import com.deco2800.game.utils.TextUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class loader extends UIComponent {

    private static final Logger logger = LoggerFactory.getLogger(LoadingDisplay.class);
    private Table loadTable;
    private Label currLoadProgress;
    private Image loadBarFront;

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
        Texture colour = new Texture(Gdx.files.internal("loadingAssets/loading_screen.png"));
        Drawable backgroundColour = new TextureRegionDrawable(colour);
        loadTable.setBackground(backgroundColour);
    }

    public Table displayLoadScreen() {

        loadTable = new Table();
        loadTable.setFillParent(true);

        // load and set Background
        //TODO: Replace the background with loading screen design
        Texture loadingScreenTexture = new Texture(Gdx.files.internal("loadingAssets/loading_screen.png"));
        TextureRegionDrawable loadingScreenBackground = new TextureRegionDrawable(loadingScreenTexture);
        loadTable.setBackground(loadingScreenBackground);

        Image title =
                new Image(
                        ServiceLocator.getResourceService()
                                .getAsset("images/uiElements/exports/title.png", Texture.class));

        // display the loading stat number
        int loadProgress = 0;
        CharSequence LoadStat = String.format("Loading:  %d", loadProgress);
        currLoadProgress = new Label(String.valueOf(LoadStat), skin, "large");

        Image loadBarBack = new Image(new Texture(Gdx.files.internal("loadingAssets/load_frame.png")));
        loadBarFront = new Image(new Texture(Gdx.files.internal("loadingAssets/load_bar.png")));
        loadBarFront.setSize(0, 18);

        // display the tips
        //TODO: make a list / LinkedList of tips to pick randomly from
        CharSequence getTips = "insert tips here";
        Label displayTips = TextUtil.createTextLabel((String) getTips, 0x000000ff, 24);
        //Label displayTips = new Label(getTips, skin, "large");

        loadTable.add(title).padTop(50);
        loadTable.row();
        //add loading stat number
        //loadTable.add(currLoadProgress).expandY().bottom().padBottom(10);
        loadTable.add(displayTips).fillY().bottom().padBottom(50);
        loadTable.row();
        loadBarFront.setSize(1f, 0.43f * loadBarFront.getHeight());
        //loadTable.add(loadBarBack);
        loadTable.add(loadBarFront);

        stage.addActor(loadTable);

        return loadTable;
    }

    public void updateLoadingStat(float loadProgress) {
        CharSequence newLoadStat = String.format("Loading:  %d", (int)loadProgress);
        currLoadProgress.setText(newLoadStat);
        loadBarFront.setWidth(loadProgress * 8 + 30);
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
        stage.clear();
        super.dispose();
    }

}

