package com.deco2800.game.components.shop;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.deco2800.game.components.player.InventoryComponent;
import com.deco2800.game.ui.UIComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Displays a button to exit the Main Game screen to the Main Menu screen.
 */
public class ShopReturn extends UIComponent {
    private static final Logger logger = LoggerFactory.getLogger(ShopReturn.class);
    private static final float Z_INDEX = 2f;

    private TextureRegionDrawable returnUp;

    private TextureRegionDrawable returnDown;
    private Texture returnTexture;

    private TextButton buildingBtn;
    private Texture buildingTexture;

    private Label buildingTitle;

    private TextButton artefactBtn;
    private Texture artefactTexture;
    private Label artefactTitle;

    private TextButton stoneFrame;
    private Texture stoneTexture;

    private TextButton goldFrame;
    private Texture goldTexture;

    @Override
    public void create() {
        super.create();
        addActors();
    }

    private void addActors() {

        float width = stage.getWidth();
        float height = stage.getHeight();
        buildingTexture = new Texture(Gdx.files.internal("images/building-category-button.png"));
        TextureRegionDrawable buildingUp = new TextureRegionDrawable(buildingTexture);
        buildingBtn = ShopUtils.createImageTextButton("", skin.getColor("black"), "button", 6f,
                buildingUp, buildingUp,
                skin, false);
        buildingBtn.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent changeEvent, Actor actor) {
                logger.debug("Building SHopping Time!");
                entity.getEvents().trigger("buildShop");
            }
        });
        buildingBtn.setPosition(width * 0.15f, height * 0.20f);
        String buildingText = "Buildings";
        buildingTitle = new Label(buildingText, skin, "large");
        buildingTitle.setPosition(505, 225);

        artefactTexture = new Texture(Gdx.files.internal("images/category-button-standard.png"));
        TextureRegionDrawable artUp = new TextureRegionDrawable(artefactTexture);
        artefactBtn = ShopUtils.createImageTextButton("", skin.getColor("black"), "button", 6f,
                artUp, artUp,
                skin, false);
        artefactBtn.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent changeEvent, Actor actor) {
                logger.debug("Artefact Shopping Time!");
                entity.getEvents().trigger("artefactShop");
            }
        });
        artefactBtn.setPosition(width * 0.50f, height * 0.20f);
        String artefactText = "Artefacts";
        artefactTitle = new Label(artefactText, skin, "large");
        artefactTitle.setPosition(1085, 225);

        stoneTexture = new Texture(Gdx.files.internal("images/border_stone.png"));
        TextureRegionDrawable stone = new TextureRegionDrawable(stoneTexture);
        // TODO change gold coins to stone count in inventory when available
        stoneFrame = ShopUtils.createImageTextButton(
                Integer.toString(entity.getComponent(InventoryComponent.class).getGold()) + "    ",
                skin.getColor("black"),
                "title", 1f, stone, stone, skin, true);
        stoneFrame.setTransform(true);
        stoneFrame.getLabel().setScale(3f);
        stoneFrame.setSize(200, 200);
        stoneFrame.setPosition(1100, 700);
        stoneFrame.setColor(216, 189, 151, 10);

        goldTexture = new Texture(Gdx.files.internal("images/border_coin.png"));
        TextureRegionDrawable coin = new TextureRegionDrawable(goldTexture);
        goldFrame = ShopUtils.createImageTextButton(
                Integer.toString(entity.getComponent(InventoryComponent.class).getGold()) + "    ",
                skin.getColor("black"),
                "title", 1f, coin, coin, skin, true);
        goldFrame.setTransform(true);
        goldFrame.getLabel().setScale(3f);
        goldFrame.setSize(200, 200);
        goldFrame.setPosition(1100, 780);
        returnTexture = new Texture(Gdx.files.internal("images/Home_Button.png"));
        returnUp = new TextureRegionDrawable(returnTexture);
        returnDown = new TextureRegionDrawable(returnTexture);
        TextButton backBtn = ShopUtils.createImageTextButton("EXIT", skin.getColor("black"), "title", 1f, returnDown,
                returnUp,
                skin, false);
        backBtn.setPosition(width * 0.85f, height * 0.85f);
        backBtn.addListener(
                new ChangeListener() {
                    @Override
                    public void changed(ChangeEvent changeEvent, Actor actor) {
                        logger.info("Exit button clicked");
                        entity.getEvents().trigger("exit");
                    }
                });

        Label title = new Label("SHOP", skin, "title");
        title.setPosition(width * 0.05f, height * 0.90f);
        title.setFontScale(4f);
        title.setColor(skin.getColor("black"));

        stage.addActor(backBtn);
        stage.addActor(title);

        stage.addActor(buildingBtn);
        stage.addActor(buildingTitle);
        stage.addActor(artefactBtn);
        stage.addActor(artefactTitle);
        stage.addActor(stoneFrame);
        stage.addActor(goldFrame);
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
        super.dispose();
    }
}