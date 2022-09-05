package com.deco2800.game.components.shop;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.deco2800.game.ui.UIComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Displays a button to exit the Main Game screen to the Main Menu screen.
 */
public class ShopReturn extends UIComponent {
    private static final Logger logger = LoggerFactory.getLogger(ShopReturn.class);
    private static final float Z_INDEX = 2f;

    Table table1;
    Table table2;
    Table table3;

    private TextButton buildingBtn;
    private TextureRegionDrawable buildingUp;
    private Texture buildingTexture;
    private Label buildingTitle;

    private TextureRegionDrawable artUp;
    private TextButton artefactBtn;
    private Texture artefactTexture;
    private Label artefactTitle;

    private TextureRegionDrawable equipmentUp;
    private TextButton equipmentBtn;
    private Texture equipmentTexture;
    private Label equipmentTitle;

    @Override
    public void create() {
        super.create();
        addActors();
    }

    private void addActors() {

        table1 = new Table();
        table1.setFillParent(true);
        table1.center().left();
        table1.padLeft(400).padTop(100);

        table2 = new Table();
        table2.setFillParent(true);
        table2.center().right();
        table2.padRight(400).padTop(100);

        table3 = new Table();
        table3.setFillParent(true);
        table3.center();
        table3.padTop(100);

        buildingTexture = new Texture(Gdx.files.internal("images/building-category-button.png"));
        buildingUp = new TextureRegionDrawable(buildingTexture);
        buildingBtn = ShopUtils.createImageTextButton("", skin.getColor("black"), "button", 1f,
                buildingUp, buildingUp,
                skin, false);
        buildingBtn.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent changeEvent, Actor actor) {
                logger.debug("Building Shopping Time!");
                entity.getEvents().trigger("buildShop");
            }
        });
        String buildingText = "Buildings";
        buildingTitle = new Label(buildingText, skin, "large");

        artefactTexture = new Texture(Gdx.files.internal("images/category-button-standard.png"));
        artUp = new TextureRegionDrawable(artefactTexture);
        artefactBtn = ShopUtils.createImageTextButton("", skin.getColor("black"), "button", 1f,
                artUp, artUp,
                skin, false);
        artefactBtn.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent changeEvent, Actor actor) {
                logger.debug("Artefact Shopping Time!");
                entity.getEvents().trigger("artefactShop");
            }
        });
        String artefactText = "Artefacts";
        artefactTitle = new Label(artefactText, skin, "large");

        equipmentTexture = new Texture(Gdx.files.internal("images/category-button-standard"
                + ".png"));
        equipmentUp = new TextureRegionDrawable(equipmentTexture);
        equipmentBtn = ShopUtils.createImageTextButton("", skin.getColor("black"),
                "button", 1f,
                equipmentUp, equipmentUp,
                skin, false);
        equipmentBtn.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent changeEvent, Actor actor) {
                logger.debug("Equipment Shopping Time!");
                entity.getEvents().trigger("artefactShop");
            }
        });
        String equipmentText = "Equipments";
        equipmentTitle = new Label(equipmentText, skin, "large");

        table1.add(buildingBtn).width(350).height(350);
        table1.row();
        table1.add(buildingTitle);
        table2.add(artefactBtn).width(350).height(350);
        table2.row();
        table2.add(artefactTitle);
        table3.add(equipmentBtn).width(350).height(350);
        table3.row();
        table3.add(equipmentTitle);

        stage.addActor(table1);
        stage.addActor(table2);
        stage.addActor(table3);
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
        table1.clear();
        table2.clear();
        table3.clear();
        stage.clear();
        super.dispose();
    }
}