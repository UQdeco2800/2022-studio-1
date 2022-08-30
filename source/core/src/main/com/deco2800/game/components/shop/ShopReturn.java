package com.deco2800.game.components.shop;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
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

    private float width;
    private float height;

    private TextButton buildingBtn;
    private TextureRegionDrawable buildingUp;
    private Texture buildingTexture;
    private Label buildingTitle;

    private TextureRegionDrawable artUp;
    private TextButton artefactBtn;
    private Texture artefactTexture;
    private Label artefactTitle;

    @Override
    public void create() {
        super.create();
        addActors();
    }

    private void addActors() {
        width = stage.getWidth();
        height = stage.getHeight();
        buildingTexture = new Texture(Gdx.files.internal("images/building-category-button.png"));
        buildingUp = new TextureRegionDrawable(buildingTexture);
        buildingBtn = ShopUtils.createImageTextButton("", skin.getColor("black"), "button", 6f,
                buildingUp, buildingUp,
                skin, false);
        buildingBtn.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent changeEvent, Actor actor) {
                logger.debug("Building Shopping Time!");
                entity.getEvents().trigger("buildShop");
            }
        });
        buildingBtn.setPosition(width * 0.15f, height * 0.20f);
        String buildingText = "Buildings";
        buildingTitle = new Label(buildingText, skin, "large");
        buildingTitle.setPosition(505, 225);

        artefactTexture = new Texture(Gdx.files.internal("images/category-button-standard.png"));
        artUp = new TextureRegionDrawable(artefactTexture);
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

        stage.addActor(buildingBtn);
        stage.addActor(buildingTitle);
        stage.addActor(artefactBtn);
        stage.addActor(artefactTitle);
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