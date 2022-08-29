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
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.deco2800.game.components.player.InventoryComponent;
import com.deco2800.game.components.shop.artefacts.Artefact;
import com.deco2800.game.components.shop.artefacts.BestSword;
import com.deco2800.game.components.shop.artefacts.BetterSword;
import com.deco2800.game.components.shop.artefacts.StandardSword;
import com.deco2800.game.ui.UIComponent;

/**
 * Displays a button to exit the Main Game screen to the Main Menu screen.
 */
public class ShopArtefactDisplay extends UIComponent {
    private static final Logger logger = LoggerFactory.getLogger(ShopBuildingDisplay.class);
    private static final float Z_INDEX = 2f;

    private float width;
    private float height;

    private StockList<Artefact> stock;
    private Node<Artefact> current;

    private Texture leftTexture;
    private TextureRegionDrawable left;
    private Button leftButton;

    private Texture rightTexture;
    private TextureRegionDrawable right;
    private Button rightButton;

    private Image currentItem;
    private Texture currentTexture;

    private Image returnShopBtn;
    private Texture returnShopTexture;
    private Texture goldenCategoryTexture;
    private TextureRegionDrawable goldenDrawable;
    private Texture brownCategoryTexture;
    private TextureRegionDrawable brownDrawable;

    private TextButton descriptionDisplay;
    private TextButton buyButton;
    private TextButton priceDisplay;

    @Override
    public void create() {
        super.create();
        addActors();
    }

    private void addActors() {

        width = stage.getWidth();
        height = stage.getHeight();

        stock = new StockList<Artefact>();
        stock.add(new StandardSword());
        stock.add(new BetterSword());
        stock.add(new BestSword());
        current = stock.head;

        returnShopTexture = new Texture(Gdx.files.internal("images/coin.png"));
        returnShopBtn = new Image(returnShopTexture);
        returnShopBtn.setSize(85, 85);
        returnShopBtn.setPosition(415, 860);

        currentTexture = new Texture(Gdx.files.internal(current.t.getCategoryTexture()));
        currentItem = new Image(currentTexture);
        currentItem.setScale(6f);

        brownCategoryTexture = new Texture(Gdx.files.internal("images/shop-description.png"));

        leftTexture = new Texture(Gdx.files.internal("images/left_arrow.png"));
        rightTexture = new Texture(Gdx.files.internal("images/right_arrow.png"));
        goldenCategoryTexture = new Texture(Gdx.files.internal("images/shop-buy-button.png"));
        goldenDrawable = new TextureRegionDrawable(goldenCategoryTexture);
        brownDrawable = new TextureRegionDrawable(brownCategoryTexture);
        left = new TextureRegionDrawable(leftTexture);
        right = new TextureRegionDrawable(rightTexture);
        leftButton = new Button(left);
        leftButton.setTransform(true);
        leftButton.setOrigin(0, 0);
        leftButton.setScale(0.15f);
        rightButton = new Button(right);
        rightButton.setTransform(true);
        rightButton.setOrigin(0, 0);
        rightButton.setScale(0.15f);
        priceDisplay = ShopUtils.createImageTextButton(
                Integer.toString(current.t.getPrice()), skin.getColor("black"),
                "button", 3f,
                goldenDrawable, goldenDrawable,
                skin,
                true);
        descriptionDisplay = ShopUtils.createImageTextButton(
                current.t.getName() + "\n" + current.t.getDescription(),
                skin.getColor("black"),
                "button", 3f,
                brownDrawable, brownDrawable, skin,
                true);
        descriptionDisplay.getLabel().setFontScale(0.5f);
        descriptionDisplay.setScaleY(6f);
        buyButton = ShopUtils.createImageTextButton("BUY", skin.getColor("black"), "button", 3f,
                brownDrawable, goldenDrawable,
                skin,
                false);
        rightButton.addListener(
                new ChangeListener() {
                    @Override
                    public void changed(ChangeEvent changeEvent, Actor actor) {
                        logger.info("Right button clicked");
                        Node<Artefact> temp = current;
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
                        Node<Artefact> temp = current;
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
                            // Sound kaching = new Sound('');
                        } else {
                            logger.info("Insufficient gold!");
                        }
                        entity.getComponent(CommonShopComponents.class).getGoldButton().setText(
                                Integer.toString(entity.getComponent(InventoryComponent.class).getGold()) + "    ");
                    }
                });

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
