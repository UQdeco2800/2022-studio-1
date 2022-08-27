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
public class ShopBuidlingDisplay extends UIComponent {
    private static final Logger logger = LoggerFactory.getLogger(ShopBuidlingDisplay.class);
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

    private Image stoneFrame;
    private Texture stoneTexture;

    private Image goldFrame;
    private Texture goldTexture;

    private Image returnShopBtn;
    private Texture returnShopTexture;

    private Image buildingDescriptionFrame;
    private Texture buildingDescriptionTexture;

    private Image price;
    private Texture priceTexture;

    private Image buyBtn;
    private Texture buyTexture;



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
        buildingBtn.setPosition(740,430);
        buildingBtn.setSize(350,350);
        String buildingText = "Buildings";
        buildingTitle = new Label(buildingText, skin, "large");
        buildingTitle.setPosition(570,750);

        returnShopTexture = new Texture(Gdx.files.internal("images/uiElements/buttons/Setting_Off_Button.png"));
        returnShopBtn = new Image(returnShopTexture);
        returnShopBtn.setSize(85,85);
        returnShopBtn.setPosition(415,860);

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

        buildingDescriptionTexture = new Texture(Gdx.files.internal("images/shop-description.png"));
        buildingDescriptionFrame = new Image(buildingDescriptionTexture);
        buildingDescriptionFrame.setSize(350,320);
        buildingDescriptionFrame.setPosition(740,230);

        priceTexture = new Texture(Gdx.files.internal("images/shop-buy-button.png"));
        price = new Image(priceTexture);
        price.setSize(200,280);
        price.setPosition(540,250);

        buyTexture = new Texture(Gdx.files.internal("images/shop-buy-button.png"));
        buyBtn = new Image(buyTexture);
        buyBtn.setSize(200,280);
        buyBtn.setPosition(1090,250);

        stage.addActor(shop_background);
        stage.addActor(buildingBtn);
        stage.addActor(buildingTitle);
        stage.addActor(stoneFrame);
        stage.addActor(goldFrame);
        stage.addActor(returnShopBtn);
        stage.addActor(buildingDescriptionFrame);
        stage.addActor(price);
        stage.addActor(buyBtn);

        // Triggers an event when the button is pressed.
        returnTexture = new Texture(Gdx.files.internal("images/uiElements/buttons/Home_Button.png"));
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

