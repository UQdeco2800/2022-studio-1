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
import com.badlogic.gdx.utils.TimeUtils;
import com.deco2800.game.components.loadingPage.LoadingDisplay;
import com.deco2800.game.entities.Entity;
import com.deco2800.game.services.ServiceLocator;
import com.deco2800.game.ui.UIComponent;
import com.deco2800.game.utils.TextUtil;
import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;

public class loader extends UIComponent {

    private static final Logger logger = LoggerFactory.getLogger(LoadingDisplay.class);
    private Table loadTable;
    private Label currLoadProgress;
    private Image loadBarFront;
    private String[] tips = parseTipsJson("configs/tips.json");

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
        String tip = getRandomTip();
        Label displayTips = TextUtil.createTextLabel( tip, 0x252525FF, 18);

        loadTable.add(title).expandX().expandY().padTop(50);
        loadTable.row();
        //add loading stat number
        //loadTable.add(currLoadProgress).expandY().bottom().padBottom(10);
        loadTable.add(displayTips).fillY().bottom().padBottom(50);
        loadTable.row();
        loadTable.add(loadBarFront).padBottom(50);

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

    private static String[] parseTipsJson(String path) {
        Gson gson = new Gson();
        BufferedReader buffer;
        try {
            buffer = Files.newBufferedReader(Paths.get(path));
        } catch (IOException e) {
            System.out.println("File not valid");
            return null;
        }
        return gson.fromJson((Reader) buffer, String[].class);
    }

    private String getRandomTip() {
        return tips[(int)(TimeUtils.nanoTime() % tips.length)];
    }
}

