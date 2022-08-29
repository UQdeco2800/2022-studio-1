package com.deco2800.game.components.shop;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.deco2800.game.components.player.InventoryComponent;
import com.deco2800.game.components.shop.artefacts.BestLog;
import com.deco2800.game.components.shop.artefacts.BetterLog;
import com.deco2800.game.components.shop.artefacts.ShopBuilding;
import com.deco2800.game.components.shop.artefacts.StandardLog;
import com.deco2800.game.services.ServiceLocator;
import com.deco2800.game.ui.UIComponent;

/**
 * Displays a button to exit the Main Game screen to the Main Menu screen.
 */
public class ShopBuildingDisplay extends UIComponent {
    private static final Logger logger = LoggerFactory.getLogger(ShopBuildingDisplay.class);
    private static final float Z_INDEX = 2f;

    private StockList<ShopBuilding> stock;
    private Node<ShopBuilding> current;

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
        stock.add(new StandardLog());
        stock.add(new BetterLog());
        stock.add(new BestLog());
        current = stock.head;

        float width = stage.getWidth();
        float height = stage.getHeight();

        returnShopTexture = new Texture(Gdx.files.internal("images/coin.png"));
        returnShopBtn = new Image(returnShopTexture);
        returnShopBtn.setSize(85, 85);
        returnShopBtn.setPosition(415, 860);

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

        currentTexture = new Texture(Gdx.files.internal(current.t.getCategoryTexture()));
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
                Integer.toString(current.t.getPrice()), skin.getColor("black"),
                "button", 3f,
                s, s,
                skin,
                true);
        TextButton descriptionDisplay = ShopUtils.createImageTextButton(
                current.t.getName() + "\n" + current.t.getDescription(),
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
                        Node<ShopBuilding> temp = current;
                        current = stock.head.next;
                        stock.head = stock.head.next;
                        stock.tail = temp;

                        priceDisplay.setText(Integer.toString(current.t.getPrice()));
                        descriptionDisplay
                                .setText(current.t.getName() + "\n" + current.t.getDescription());
                        currentItem.setDrawable(new TextureRegionDrawable(
                                new Texture(Gdx.files.internal(current.t.getCategoryTexture()))));
                    }
                });

        leftButton.addListener(
                new ChangeListener() {
                    @Override
                    public void changed(ChangeEvent changeEvent, Actor actor) {
                        logger.info("Left button clicked");
                        // entity.getEvents().trigger("right");
                        Node<ShopBuilding> temp = current;
                        current = stock.head.prev;
                        stock.head = stock.head.prev;
                        stock.tail = temp.prev;

                        priceDisplay.setText(Integer.toString(current.t.getPrice()));
                        descriptionDisplay
                                .setText(current.t.getName() + "\n" + current.t.getDescription());
                        currentItem.setDrawable(new TextureRegionDrawable(
                                new Texture(Gdx.files.internal(current.t.getCategoryTexture()))));
                    }
                });

        buyButton.addListener(
                new ChangeListener() {
                    @Override
                    public void changed(ChangeEvent changeEvent, Actor actor) {
                        logger.info("Buy button clicked");

                        if (entity.getComponent(InventoryComponent.class).hasGold(current.t.getPrice())) {
                            logger.info("Sufficient Gold");
                            entity.getComponent(InventoryComponent.class).addGold(-1 * current.t.getPrice());
                            Sound coinSound = ServiceLocator.getResourceService().getAsset("sounds/coin.mp3",
                                    Sound.class);
                            coinSound.play();
                        } else {
                            logger.info("Insufficient gold!");
                        }
                        goldFrame.setText(
                                Integer.toString(entity.getComponent(InventoryComponent.class).getGold()) + "    ");
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
        returnTexture = new Texture(Gdx.files.internal("images/Home_Button.png"));
        returnUp = new TextureRegionDrawable(returnTexture);
        returnDown = new TextureRegionDrawable(returnTexture);

        Texture backTexture = new Texture(Gdx.files.internal("images/backButton.png"));
        TextureRegionDrawable upBack = new TextureRegionDrawable(backTexture);
        TextureRegionDrawable downBack = new TextureRegionDrawable(backTexture);
        ImageButton backButton = new ImageButton(upBack, downBack);
        backButton.setSize(50, 50);
        // backButton.setScale(0.5f);
        backButton.setPosition(width * 0f, height * 0.9f);
        backButton.addListener(
                new ChangeListener() {
                    @Override
                    public void changed(ChangeEvent changeEvent, Actor actor) {
                        logger.debug("Exit button clicked");
                        entity.getEvents().trigger("exit");
                    }
                });

        TextButton exitButton = ShopUtils.createImageTextButton("EXIT", skin.getColor("black"), "title", 1f, returnDown,
                returnUp,
                skin, false);
        exitButton.setPosition(width * 0.85f, height * 0.85f);
        exitButton.addListener(
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

        stage.addActor(title);
        stage.addActor(backButton);

        stage.addActor(goldFrame);
        stage.addActor(stoneFrame);

        leftButton.setPosition(width * 0.30f, height * 0.45f);
        stage.addActor(leftButton);

        currentItem.setPosition(width * 0.33f, height * 0.27f);
        stage.addActor(currentItem);

        rightButton.setPosition(width * 0.70f, height * 0.45f);
        stage.addActor(rightButton);
        priceDisplay.setPosition(width * 0.05f, height * 0.0f);

        stage.addActor(priceDisplay);
        descriptionDisplay.setPosition(width * 0.24f, height * -0.15f);
        stage.addActor(descriptionDisplay);
        buyButton.setPosition(width * 0.78f, height * 0.0f);
        stage.addActor(buyButton);
        stage.addActor(exitButton);

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
        stage.clear();
        super.dispose();
    }
}