package com.deco2800.game.components.shop;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.deco2800.game.components.gamearea.GameAreaDisplay;
import com.deco2800.game.ui.UIComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Displays a button to exit the Main Game screen to the Main Menu screen.
 */
public class ShopExitDisplay extends UIComponent {
    private static final Logger logger = LoggerFactory.getLogger(ShopExitDisplay.class);
    private static final float Z_INDEX = 2f;
    private Table table;

    private TextureRegionDrawable returnUp;

    private TextureRegionDrawable returnDown;
    private Texture returnTexture;

    private Texture shopTexture;

    private Image shop_background;

    private Image buildingBtn;
    private Texture buildingTexture;

    private Label buildingTitle;

    private Image artefactBtn;
    private Texture artefactTexture;
    private Label artefactTitle;

    private Image stoneFrame;
    private Texture stoneTexture;

    private Image goldFrame;
    private Texture goldTexture;



    @Override
    public void create() {
        super.create();
        addActors();
    }

    private void addActors() {
        table = new Table();
        table.top().right();
        table.setFillParent(true);

        buildingTexture = new Texture(Gdx.files.internal("images/shop-category-button.png"));
        buildingBtn = new Image(buildingTexture);
        buildingBtn.setPosition(500,450);
        buildingBtn.setSize(300,300);
        String buildingText = "Buildings";
        buildingTitle = new Label(buildingText, skin, "large");
        buildingTitle.setPosition(585,445);

        artefactTexture = new Texture(Gdx.files.internal("images/shop-category-button.png"));
        artefactBtn = new Image(artefactTexture);
        artefactBtn.setPosition(1000,450);
        artefactBtn.setSize(300,300);
        String artefactText = "Artefacts";
        artefactTitle = new Label(artefactText, skin, "large");
        artefactTitle.setPosition(1085,445);

        shopTexture = new Texture(Gdx.files.internal("images/shop-interface.png"));
        shop_background = new Image(shopTexture);
        shop_background.setPosition(400,300);

        stoneTexture = new Texture(Gdx.files.internal("images/shop-description.png"));
        stoneFrame = new Image(stoneTexture);
        stoneFrame.setSize(200,200);
        stoneFrame.setPosition(1100,700);
        stoneFrame.setColor(216,189,151,10);

        goldTexture = new Texture(Gdx.files.internal("images/shop-description.png"));
        goldFrame = new Image(goldTexture);
        goldFrame.setSize(200,200);
        goldFrame.setPosition(1100,780);

        stage.addActor(shop_background);
        stage.addActor(buildingBtn);
        stage.addActor(buildingTitle);
        stage.addActor(artefactBtn);
        stage.addActor(artefactTitle);
        stage.addActor(stoneFrame);
        stage.addActor(goldFrame);

        // Triggers an event when the button is pressed.
        returnTexture = new Texture(Gdx.files.internal("images/Home_Button.png"));
        returnUp = new TextureRegionDrawable(returnTexture);
        returnDown = new TextureRegionDrawable(returnTexture);
        ImageButton backBtn = new ImageButton(returnUp,returnDown);
        backBtn.addListener(
                new ChangeListener() {
                    @Override
                    public void changed(ChangeEvent changeEvent, Actor actor) {
                        logger.debug("Exit button clicked");
                        entity.getEvents().trigger("exit");
                    }
                });
        table.add(backBtn).padTop(10f).padRight(10f);
        stage.addActor(table);
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
        table.clear();
        super.dispose();
    }
}

