package com.deco2800.game.components.shop;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.deco2800.game.areas.ForestGameArea;
import com.deco2800.game.areas.MainArea;
import com.deco2800.game.ui.UIComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ShopInterface extends UIComponent {
    private static final Logger logger = LoggerFactory.getLogger(ShopInterface.class);
    private static final float Z_INDEX = 2f;
    int screenWidth;
    int screenHeight;

    private Label subtitle;
    private Table backgroundTable;
    private Group group;
    private Group category;
    Table artefact;
    Table equipment;

    private TextureRegionDrawable artUp;
    private TextButton artefactBtn;
    private Texture artefactTexture;
    private Label artefactTitle;

    private TextureRegionDrawable equipmentUp;
    private TextButton equipmentBtn;
    private Texture equipmentTexture;
    private Label equipmentTitle;

    private ImageButton crossFrame;

    @Override
    public void create() {
        super.create();
        entity.getEvents().addListener("shop", this::openShop);
        entity.getEvents().addListener("closeAll", this::closeShop);
        addActors();

    }

    private void addActors() {
        screenWidth = Gdx.graphics.getWidth();
        screenHeight = Gdx.graphics.getHeight();

        group = new Group();
        category = new Group();
        backgroundTable = new Table();

        artefact = new Table();
        artefact.setFillParent(true);
        artefact.setSize(100f, 100f);
        artefact.center().right();
        artefact.padRight(150f).padTop(100f);

        equipment = new Table();
        equipment.setFillParent(true);
        equipment.setSize(100f, 100f);
        equipment.center().left();
        equipment.padLeft(150f).padTop(100f);

        // setup background for shop
        backgroundTable.setSize(Gdx.graphics.getWidth() * 0.7f, Gdx.graphics.getHeight() * 0.7f);
        Texture shopInterfaceTexture = new Texture(Gdx.files.internal("images/shop-background.png"));
        TextureRegionDrawable shop = new TextureRegionDrawable(shopInterfaceTexture);
        backgroundTable.setBackground(shop);
        backgroundTable.setPosition(Gdx.graphics.getWidth() / 2f - backgroundTable.getWidth() / 2f,
                Gdx.graphics.getHeight() / 2f - backgroundTable.getHeight() / 2f);

        // setup exit
        Texture crossTexture = new Texture(Gdx.files.internal("images/cross.png"));
        TextureRegionDrawable cross = new TextureRegionDrawable(crossTexture);
        crossFrame = new ImageButton(cross, cross);
        crossFrame.setSize(40f, 40f);
        crossFrame.setPosition(Gdx.graphics.getWidth() * 0.85f - 70f,
                Gdx.graphics.getHeight() * 0.85f - 70f);

        // setup title
        subtitle = new Label("Shop", skin, "title");
        subtitle.setFontScale(2f);
        subtitle.setColor(skin.getColor("black"));
        subtitle.setPosition(Gdx.graphics.getWidth() / 2f - 100f,
                Gdx.graphics.getHeight() * 0.75f);

        // setup artefact category
        artefactTexture = new Texture(Gdx.files.internal("images/shop-items-framed/health-potion-framed.png"));
        artUp = new TextureRegionDrawable(artefactTexture);
        artefactBtn = ShopUtils.createImageTextButton("", skin.getColor("black"), "button", 1f,
                artUp, artUp,
                skin, false);
        String artefactText = "Artefacts";
        artefactTitle = new Label(artefactText, skin, "large");

        // setup equipment category
        equipmentTexture = new Texture(Gdx.files.internal("images/shop-items-framed/sword-framed.png"));
        equipmentUp = new TextureRegionDrawable(equipmentTexture);
        equipmentBtn = ShopUtils.createImageTextButton("", skin.getColor("black"),
                "button", 1f,
                equipmentUp, equipmentUp,
                skin, false);
        String equipmentText = "Equipment";
        equipmentTitle = new Label(equipmentText, skin, "large");

        crossFrame.addListener(
                new ChangeListener() {
                    @Override
                    public void changed(ChangeEvent changeEvent, Actor actor) {
                        logger.debug("Close shop page");
                        closeShop();
                        entity.getEvents().trigger("closeAll");
                    }
                });

        artefactBtn.addListener(
                new ChangeListener() {
                    @Override
                    public void changed(ChangeEvent changeEvent, Actor actor) {
                        logger.debug("Opening artefact shop page");
                        changeShop();
                        entity.getEvents().trigger("artefactShop");
                    }
                });

        equipmentBtn.addListener(
                new ChangeListener() {
                    @Override
                    public void changed(ChangeEvent changeEvent, Actor actor) {
                        logger.debug("Opening equipment shop page");
                        changeShop();
                        entity.getEvents().trigger("equipmentShop");
                    }
                });

        group.addActor(backgroundTable);
        group.addActor(crossFrame);

        artefact.add(artefactBtn);
        artefact.row();
        artefact.add(artefactTitle);

        equipment.add(equipmentBtn);
        equipment.row();
        equipment.add(equipmentTitle);

        category.setSize(Gdx.graphics.getWidth() * 0.6f, Gdx.graphics.getHeight() * 0.4f);
        category.setPosition(Gdx.graphics.getWidth() / 2f - backgroundTable.getWidth() / 2.3f,
                Gdx.graphics.getHeight() / 2f - backgroundTable.getHeight() / 4f);
        category.addActor(artefact);
        category.addActor(equipment);

        group.setVisible(false);
        category.setVisible(false);
        subtitle.setVisible(false);

        stage.addActor(group);
        stage.addActor(category);
        stage.addActor(subtitle);
    }

    private void changeShop() {
        category.setVisible(false);
        subtitle.setVisible(false);
    }

    private void closeShop() {
        ((ForestGameArea) MainArea.getInstance().getGameArea()).exitShop();
        group.setVisible(false);
        category.setVisible(false);
        subtitle.setVisible(false);
    }

    private void openShop() {
        compareRes();
        group.setVisible(true);
        category.setVisible(true);
        subtitle.setVisible(true);
    }

    private void compareRes() {
        if (screenHeight != Gdx.graphics.getHeight() || screenWidth != Gdx.graphics.getWidth()) {
            backgroundTable.setSize(Gdx.graphics.getWidth() * 0.7f, Gdx.graphics.getHeight() * 0.7f);
            backgroundTable.setPosition(Gdx.graphics.getWidth() / 2f - backgroundTable.getWidth() / 2f,
                    Gdx.graphics.getHeight() / 2f - backgroundTable.getHeight() / 2f);
            subtitle.setPosition(Gdx.graphics.getWidth() / 2f - 100f,
                    Gdx.graphics.getHeight() * 0.75f);
            crossFrame.setPosition(Gdx.graphics.getWidth() * 0.85f - 70f,
                    Gdx.graphics.getHeight() * 0.85f - 70f);
            category.setSize(Gdx.graphics.getWidth() * 0.6f, Gdx.graphics.getHeight() * 0.4f);
            category.setPosition(Gdx.graphics.getWidth() / 2f - backgroundTable.getWidth() / 2.3f,
                    Gdx.graphics.getHeight() / 2f - backgroundTable.getHeight() / 4f);
            screenWidth = Gdx.graphics.getWidth();
            screenHeight = Gdx.graphics.getHeight();
            entity.getEvents().trigger("changeRes");
        }
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
        group.clear();
        category.clear();
        subtitle.clear();
        super.dispose();
    }
}
