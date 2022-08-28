package com.deco2800.game.components.shop;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.deco2800.game.components.player.InventoryComponent;
import com.deco2800.game.components.shop.StockList.Node;
import com.deco2800.game.components.shop.artefacts.Artefact;
import com.deco2800.game.components.shop.artefacts.BestSword;
import com.deco2800.game.components.shop.artefacts.BetterSword;
import com.deco2800.game.components.shop.artefacts.StandardSword;
import com.deco2800.game.ui.UIComponent;

/**
 * Displays a button to exit the Main Game screen to the Main Menu screen.
 */
public class ShopBuildingDisplay extends UIComponent {
    private static final Logger logger = LoggerFactory.getLogger(ShopBuildingDisplay.class);
    private static final float Z_INDEX = 2f;
    private Table table;

    private StockList stock;
    private Node current;

    private TextureRegionDrawable returnUp;

    private TextureRegionDrawable returnDown;
    private Texture returnTexture;

    private Texture leftTexture;

    private TextButton stoneFrame;
    private Texture stoneTexture;

    private TextButton goldFrame;
    private Texture goldTexture;

    private Image currentItem;
    private Texture currentTexture;

    private Image returnShopBtn;
    private Texture returnShopTexture;

    private Image buildingDescriptionFrame;
    private Texture buildingDescriptionTexture;
    private Label descriptionTitle;

    private Image price;
    private Texture priceTexture;
    private String priceTitle = "100";

    private Image buyBtn;
    private Texture buyTexture;
    private Label buyTitle;

    private Image sword;
    private Texture swordTexture;
    private Label swordTitle;

    private TextButton priceDisplay;

    @Override
    public void create() {
        super.create();
        addActors();
        entity.getEvents().addListener("updateHealth", this::setLabel);
    }

    public void setLabel(String string) {
        logger.info("50 50 50");
        this.priceTitle = string;
        this.priceDisplay.setText(priceTitle);
        // create();
        // entity.getEvents().trigger("exit");
    }

    private void addActors() {

        stock = new StockList();
        stock.add(new StandardSword());
        stock.add(new BetterSword());
        stock.add(new BestSword());
        current = stock.head;

        float width = stage.getWidth();
        float height = stage.getHeight();

        returnShopTexture = new Texture(Gdx.files.internal("images/uiElements/buttons/Setting_Off_Button.png"));
        returnShopBtn = new Image(returnShopTexture);
        returnShopBtn.setSize(85, 85);
        returnShopBtn.setPosition(415, 860);

        stoneTexture = new Texture(Gdx.files.internal("images/shop-description.png"));
        TextureRegionDrawable stoneDraw = new TextureRegionDrawable(stoneTexture);
        stoneFrame = ShopUtils.createImageTextButton(
                Integer.toString(entity.getComponent(InventoryComponent.class).getGold()), skin.getColor("black"),
                "button", 1f, stoneDraw, stoneDraw, skin, true);
        stoneFrame.setSize(200, 200);
        stoneFrame.setPosition(1100, 700);
        stoneFrame.setColor(216, 189, 151, 10);

        goldTexture = new Texture(Gdx.files.internal("images/shop-description.png"));
        TextureRegionDrawable goldDraw = new TextureRegionDrawable(goldTexture);
        goldFrame = ShopUtils.createImageTextButton(
                "Gold: " + Integer.toString(entity.getComponent(InventoryComponent.class).getGold()),
                skin.getColor("black"),
                "button", 1f, goldDraw, goldDraw, skin, true);
        goldFrame.setSize(200, 200);
        goldFrame.setPosition(1100, 780);

        currentTexture = new Texture(Gdx.files.internal(current.artefact.getCategoryTexture()));
        currentItem = new Image(currentTexture);
        currentItem.setScale(6f);

        buildingDescriptionTexture = new Texture(Gdx.files.internal("images/shop-description.png"));
        buildingDescriptionFrame = new Image(buildingDescriptionTexture);
        buildingDescriptionFrame.setSize(350, 320);
        buildingDescriptionFrame.setPosition(740, 230);
        String descriptionText = "Defense against enemies";
        descriptionTitle = new Label(descriptionText, skin, "small");
        descriptionTitle.setPosition(800, 400);

        leftTexture = new Texture(Gdx.files.internal("images/left_arrow.png"));
        Texture rightTexture = new Texture(Gdx.files.internal("images/right_arrow.png"));
        priceTexture = new Texture(Gdx.files.internal("images/shop-buy-button.png"));
        price = new Image(priceTexture);
        price.setScale(4f);
        TextureRegionDrawable s = new TextureRegionDrawable(priceTexture);
        TextureRegionDrawable pressed = new TextureRegionDrawable(buildingDescriptionTexture);
        TextureRegionDrawable left = new TextureRegionDrawable(leftTexture);
        TextureRegionDrawable right = new TextureRegionDrawable(rightTexture);
        Button leftButton = new Button(left);
        leftButton.setTransform(true);
        leftButton.setOrigin(0, 0);
        leftButton.setScale(0.15f);
        Button rightButton = new Button(right);
        rightButton.setTransform(true);
        rightButton.setOrigin(0, 0);
        rightButton.setScale(0.15f);
        priceDisplay = ShopUtils.createImageTextButton(
                Integer.toString(current.artefact.getPrice()), skin.getColor("black"),
                "button", 3f,
                s, s,
                skin,
                true);
        TextButton descriptionDisplay = ShopUtils.createImageTextButton(
                current.artefact.getName() + "\n" + current.artefact.getDescription(),
                skin.getColor("black"),
                "button", 3f,
                pressed, pressed, skin,
                true);
        descriptionDisplay.getLabel().setFontScale(0.5f);
        descriptionDisplay.setScaleY(6f);
        TextButton buyButton = ShopUtils.createImageTextButton("BUY", skin.getColor("black"), "button", 3f, pressed, s,
                skin,
                false);
        rightButton.addListener(
                new ChangeListener() {
                    @Override
                    public void changed(ChangeEvent changeEvent, Actor actor) {
                        logger.info("Right button clicked");
                        // entity.getEvents().trigger("right");
                        Node temp = current;
                        current = stock.head.next;
                        stock.head = stock.head.next;
                        stock.tail = temp;

                        priceDisplay.setText(Integer.toString(current.artefact.getPrice()));
                        descriptionDisplay
                                .setText(current.artefact.getName() + "\n" + current.artefact.getDescription());
                        currentItem.setDrawable(new TextureRegionDrawable(
                                new Texture(Gdx.files.internal(current.artefact.getCategoryTexture()))));
                    }
                });

        leftButton.addListener(
                new ChangeListener() {
                    @Override
                    public void changed(ChangeEvent changeEvent, Actor actor) {
                        logger.info("Left button clicked");
                        // entity.getEvents().trigger("right");
                        Node temp = current;
                        current = stock.head.prev;
                        stock.head = stock.head.prev;
                        stock.tail = temp.prev;

                        priceDisplay.setText(Integer.toString(current.artefact.getPrice()));
                        descriptionDisplay
                                .setText(current.artefact.getName() + "\n" + current.artefact.getDescription());
                        currentItem.setDrawable(new TextureRegionDrawable(
                                new Texture(Gdx.files.internal(current.artefact.getCategoryTexture()))));
                    }
                });

        buyButton.addListener(
                new ChangeListener() {
                    @Override
                    public void changed(ChangeEvent changeEvent, Actor actor) {
                        logger.info("Buy button clicked");

                        if (entity.getComponent(InventoryComponent.class).hasGold(current.artefact.getPrice())) {
                            logger.info("Sufficient Gold");
                            entity.getComponent(InventoryComponent.class).addGold(-1 * current.artefact.getPrice());
                        } else {
                            logger.info("Insufficient gold!");
                        }
                        goldFrame.setText(
                                "Gold: " + Integer.toString(entity.getComponent(InventoryComponent.class).getGold()));
                    }
                });

        buyTexture = new Texture(Gdx.files.internal("images/shop-buy-button.png"));
        buyBtn = new Image(buyTexture);
        buyBtn.setSize(200, 280);
        buyBtn.setPosition(1090, 250);
        String buyText = "BUY";
        buyTitle = new Label(buyText, skin, "large");
        buyTitle.setPosition(1170, 390);

        swordTexture = new Texture(Gdx.files.internal("images/shop-sword.png"));
        sword = new Image(swordTexture);
        sword.setSize(200, 200);
        sword.setPosition(820, 500);
        String swordText = "Sword";
        swordTitle = new Label(swordText, skin, "large");
        swordTitle.setPosition(870, 700);

        // Triggers an event when the button is pressed.
        returnTexture = new Texture(Gdx.files.internal("images/uiElements/buttons/Home_Button.png"));
        returnUp = new TextureRegionDrawable(returnTexture);
        returnDown = new TextureRegionDrawable(returnTexture);
        TextButton backBtn = ShopUtils.createImageTextButton("EXIT", skin.getColor("black"), "button", 1f, returnDown,
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

        stage.addActor(goldFrame);
        stage.addActor(stoneFrame);

        leftButton.setPosition(width * 0.30f, height * 0.45f);
        stage.addActor(leftButton);

        currentItem.setPosition(width * 0.33f, height * 0.27f);
        stage.addActor(currentItem);

        rightButton.setPosition(width * 0.70f, height * 0.45f);
        stage.addActor(rightButton);
        // priceDisplay.setOrigin(0, 0);
        priceDisplay.setPosition(width * 0.05f, height * 0.0f);

        stage.addActor(priceDisplay);
        // descriptionDisplay.setOrigin(0, 0);
        descriptionDisplay.setPosition(width * 0.24f, height * -0.15f);
        stage.addActor(descriptionDisplay);
        // buyButton.setOrigin(0, 0);
        buyButton.setPosition(width * 0.78f, height * 0.0f);
        stage.addActor(buyButton);

        /*
         * table.add(buildingItem);
         * table.add(buildingTitle);
         * table.add(stoneFrame);
         * table.add(goldFrame);
         * table.add(returnShopBtn);
         * table.add(buildingDescriptionFrame);
         * table.add(descriptionTitle);
         * table.add(price);
         * table.add(priceTitle);
         * table.add(buyBtn);
         * table.add(buyTitle);
         * table.add(sword);
         * table.add(swordTitle);
         */

        // stage.addActor(table);
        // priceTitle.setAlignment(Align.center);
        // stage.addActor(priceDisplay);

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
        stage.clear();
        super.dispose();
    }
}
