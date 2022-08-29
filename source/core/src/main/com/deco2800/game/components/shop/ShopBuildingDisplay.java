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
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.deco2800.game.components.player.InventoryComponent;
import com.deco2800.game.components.shop.artefacts.BestLog;
import com.deco2800.game.components.shop.artefacts.BetterLog;
import com.deco2800.game.components.shop.artefacts.ShopBuilding;
import com.deco2800.game.components.shop.artefacts.StandardLog;
import com.deco2800.game.ui.UIComponent;

/**
 * Displays UI specific to the ShopArtefactScreen
 */
public class ShopBuildingDisplay extends UIComponent {
    private static final Logger logger = LoggerFactory.getLogger(ShopBuildingDisplay.class);
    private static final float Z_INDEX = 2f;

    private float width;
    private float height;

    private StockList<ShopBuilding> stock;
    private Node<ShopBuilding> current;

    private Texture leftTexture;
    private TextureRegionDrawable left;
    private Button leftButton;

    private Texture rightTexture;
    private TextureRegionDrawable right;
    private Button rightButton;

    private Image currentItem;
    private Texture currentTexture;

    private Texture goldenCategoryTexture;
    private TextureRegionDrawable goldenDrawable;
    private Texture brownCategoryTexture;
    private TextureRegionDrawable brownDrawable;

    private TextButton descriptionDisplay;
    private TextButton buyButton;
    private TextButton priceDisplay;

    private Texture backTexture;
    private TextureRegionDrawable upBack;
    private ImageButton backButton;

    @Override
    public void create() {
        super.create();
        addActors();
    }

    private void addActors() {

        width = stage.getWidth();
        height = stage.getHeight();

        // Create linked list of the available shop stock
        stock = new StockList<ShopBuilding>();
        stock.add(new StandardLog());
        stock.add(new BetterLog());
        stock.add(new BestLog());
        current = stock.head;

        // Create the current artefact to display
        currentTexture = new Texture(Gdx.files.internal(current.t.getCategoryTexture()));
        currentItem = new Image(currentTexture);
        currentItem.setScale(6f);

        // Create textures for arrows, price, descrition and buy button
        brownCategoryTexture = new Texture(Gdx.files.internal("images/shop-description.png"));
        leftTexture = new Texture(Gdx.files.internal("images/left_arrow.png"));
        rightTexture = new Texture(Gdx.files.internal("images/right_arrow.png"));
        goldenCategoryTexture = new Texture(Gdx.files.internal("images/shop-buy-button.png"));
        goldenDrawable = new TextureRegionDrawable(goldenCategoryTexture);
        brownDrawable = new TextureRegionDrawable(brownCategoryTexture);
        left = new TextureRegionDrawable(leftTexture);
        right = new TextureRegionDrawable(rightTexture);

        // create left button
        leftButton = new Button(left);
        leftButton.setTransform(true);
        leftButton.setOrigin(0, 0);
        leftButton.setScale(0.15f);

        // create right button
        rightButton = new Button(right);
        rightButton.setTransform(true);
        rightButton.setOrigin(0, 0);
        rightButton.setScale(0.15f);

        // create price sticker
        priceDisplay = ShopUtils.createImageTextButton(
                Integer.toString(current.t.getPrice()), skin.getColor("black"),
                "button", 3f,
                goldenDrawable, goldenDrawable,
                skin,
                true);

        // create description sticker
        descriptionDisplay = ShopUtils.createImageTextButton(
                current.t.getName() + "\n" + current.t.getDescription(),
                skin.getColor("black"),
                "button", 3f,
                brownDrawable, brownDrawable, skin,
                true);
        descriptionDisplay.getLabel().setFontScale(0.5f);
        descriptionDisplay.setScaleY(6f);

        // create buy button
        buyButton = ShopUtils.createImageTextButton("BUY", skin.getColor("black"), "button", 3f,
                brownDrawable, goldenDrawable,
                skin,
                false);

        // create the back button
        backTexture = new Texture(Gdx.files.internal("images/backButton.png"));
        upBack = new TextureRegionDrawable(backTexture);
        backButton = new ImageButton(upBack, upBack);
        backButton.setSize(50, 50);
        backButton.setPosition(width * 0.01f, height * 0.9f);

        // Add listeners to relevant buttons
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
                            logger.info("Sufficient stone");
                            entity.getComponent(InventoryComponent.class).addStone(-1 * current.t.getPrice());
                            Sound rockSound = Gdx.audio.newSound(Gdx.files.internal("sounds/rock.mp3"));
                            rockSound.play();
                        } else {
                            logger.info("Insufficient stone!");
                        }
                        entity.getComponent(CommonShopComponents.class).getStoneButton().setText(
                                Integer.toString(entity.getComponent(InventoryComponent.class).getStone()) + "    ");
                    }
                });

        backButton.addListener(
                new ChangeListener() {
                    @Override
                    public void changed(ChangeEvent changeEvent, Actor actor) {
                        logger.debug("Back button clicked");
                        entity.getEvents().trigger("mainShop");
                    }
                });

        // Add items to the stage

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
        stage.addActor(backButton);

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
