package com.deco2800.game.components.shop;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.deco2800.game.areas.MainArea;
import com.deco2800.game.components.player.InventoryComponent;
import com.deco2800.game.components.player.PlayerStatsDisplay;
import com.deco2800.game.components.shop.artefacts.Artefact;
import com.deco2800.game.entities.configs.ArtefactConfig;
import com.deco2800.game.files.FileLoader;
import com.deco2800.game.services.AchievementHandler;
import com.deco2800.game.services.ServiceLocator;
import com.deco2800.game.ui.UIComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class ArtefactShopDisplay extends UIComponent {
    private static final Logger logger = LoggerFactory.getLogger(ArtefactShopDisplay.class);
    private static final float Z_INDEX = 2f;
    
    Table itemDisplay;
    Group artefactShop;


    private CircularLinkedList<Artefact> stock;
    private Node<Artefact> current;
    private ArtefactConfig stats;
    private ArtefactConfig prevStats;
    private ArtefactConfig nextStats;

    Label subtitle;
    Label itemNumber;
    int i;

    private Texture leftTexture;
    private TextureRegionDrawable left;
    private Button leftButton;

    private Texture rightTexture;
    private TextureRegionDrawable right;
    private Button rightButton;

    private Image currentItem;
    private Texture currentTexture;
    private Image prevItem;
    private Texture prevTexture;
    private Image nextItem;
    private Texture nextTexture;

    private Texture goldenCategoryTexture;
    private TextureRegionDrawable goldenDrawable;
    private Texture clickCategoryTexture;
    private TextureRegionDrawable clickDrawable;
    private Texture brownCategoryTexture;
    private TextureRegionDrawable brownDrawable;
    private Texture redCategoryTexture;
    private TextureRegionDrawable redDrawable;

    private TextButton descriptionDisplay;
    private TextButton buyButton;
    private TextButton priceDisplay;
    boolean sufficientFunds;

    private Texture backTexture;
    private TextureRegionDrawable upBack;
    private ImageButton backButton;
    
    @Override
    public void create() {
        super.create();
        entity.getEvents().addListener("artefactShop", this::openShop);
        entity.getEvents().addListener("closeAll", this::closeShop);
        entity.getEvents().addListener("changeRes", this::changeRes);
        addActors();
    }

    private void addActors() {
        artefactShop = new Group();
        
        // Create linked list of the available shop stock
        stock = new CircularLinkedList<Artefact>();
        List<Artefact> artefactOptions = Artefact.getAllartefactTypes();
        for (Artefact e : artefactOptions) {
            stock.add(e);
        }
        itemNumber = new Label("", skin, "button");
        itemNumber.setFontScale(1.5f);
        itemNumber.setColor(skin.getColor("black"));
        // Create the current artefact to display
        currentTexture = new Texture(Gdx.files.internal("images/shop-items-framed/category-button-clicked.png"));
        currentItem = new Image(currentTexture);

        prevTexture = new Texture(Gdx.files.internal("images/shop-items-framed/category-button-clicked.png"));
        prevItem = new Image(prevTexture);

        nextTexture = new Texture(Gdx.files.internal("images/shop-items-framed/category-button-clicked.png"));
        nextItem = new Image(nextTexture);

        // Create textures for arrows, price, descrition and buy button
        brownCategoryTexture = new Texture(Gdx.files.internal("images/shop-description.png"));
        leftTexture = new Texture(Gdx.files.internal("images/left_arrow.png"));
        rightTexture = new Texture(Gdx.files.internal("images/right_arrow.png"));
        goldenCategoryTexture = new Texture(Gdx.files.internal("images/buy-button.png"));
        clickCategoryTexture = new Texture(Gdx.files.internal("images/buy-button-clicked.png"));
        redCategoryTexture = new Texture(Gdx.files.internal("images/buy-button-cannot-buy.png"));
        goldenDrawable = new TextureRegionDrawable(goldenCategoryTexture);
        clickDrawable = new TextureRegionDrawable(clickCategoryTexture);
        brownDrawable = new TextureRegionDrawable(brownCategoryTexture);
        redDrawable = new TextureRegionDrawable(redCategoryTexture);
        left = new TextureRegionDrawable(leftTexture);
        right = new TextureRegionDrawable(rightTexture);

        // create left button
        leftButton = new Button(left);
        leftButton.setTransform(true);

        // create right button
        rightButton = new Button(right);
        rightButton.setTransform(true);

        // create price sticker
        priceDisplay = ShopUtils.createImageTextButton( "", skin.getColor("black"),
                "button", 1f,
                goldenDrawable, goldenDrawable,
                skin,
                true);

        // create description sticker
        descriptionDisplay = ShopUtils.createImageTextButton( "",
                skin.getColor("black"),
                "button", 1f,
                brownDrawable, brownDrawable, skin,
                true);

        // create buy button
        buyButton = ShopUtils.createImageTextButton("BUY", skin.getColor("black"), "button", 1f,
                goldenDrawable, clickDrawable, skin, false);

        // create the back button
        backTexture = new Texture(Gdx.files.internal("images/backButton.png"));
        upBack = new TextureRegionDrawable(backTexture);
        backButton = new ImageButton(upBack, upBack);

        subtitle = new Label("ARTEFACTS", skin, "title");
        subtitle.setFontScale(2f);
        subtitle.setColor(skin.getColor("black"));

        changeRes();

        artefactShop.addActor(leftButton);
        artefactShop.addActor(rightButton);
        artefactShop.addActor(buyButton);
        artefactShop.addActor(priceDisplay);
        artefactShop.addActor(subtitle);
        artefactShop.addActor(backButton);
        artefactShop.setVisible(false);

        stage.addActor(artefactShop);
        // Add listeners to relevant buttons
        rightButton.addListener(
                new ChangeListener() {
                    @Override
                    public void changed(ChangeEvent changeEvent, Actor actor) {
                        logger.info("Right button clicked");
                        carouselRight();
                    }
                });

        leftButton.addListener(
                new ChangeListener() {
                    @Override
                    public void changed(ChangeEvent changeEvent, Actor actor) {
                        logger.info("Left button clicked");
                        carouselLeft();
                    }
                });

        buyButton.addListener(
                new ChangeListener() {
                    @Override
                    public void changed(ChangeEvent changeEvent, Actor actor) {
                        logger.info("Buy button clicked");
                        buyItem();
                    }
                });

        backButton.addListener(
                new ChangeListener() {
                    @Override
                    public void changed(ChangeEvent changeEvent, Actor actor) {
                        logger.debug("Back button clicked");
                        artefactShop.setVisible(false);
                        entity.getEvents().trigger("shop");
                    }
                });

        
    }

    private void changeRes() {

        itemDisplay = new Table();
        itemDisplay.setFillParent(true);
        itemDisplay.setSize(Gdx.graphics.getWidth() * 0.6f, Gdx.graphics.getHeight() * 0.4f);
        itemDisplay.setPosition(Gdx.graphics.getWidth() / 2f, Gdx.graphics.getHeight() * 0.361f);

        itemDisplay.removeActor(prevItem);
        itemDisplay.removeActor(currentItem);
        itemDisplay.removeActor(nextItem);

        itemDisplay.add(prevItem).width(Gdx.graphics.getWidth() * 0.058f).height(Gdx.graphics.getWidth() * 0.058f);
        itemDisplay.add(currentItem).width(Gdx.graphics.getWidth() * 0.087f).height(Gdx.graphics.getWidth() * 0.087f);
        itemDisplay.add(nextItem).width(Gdx.graphics.getWidth() * 0.058f).height(Gdx.graphics.getWidth() * 0.058f);

        itemDisplay.row();
        itemDisplay.add(itemNumber).colspan(3).center();
        itemDisplay.row();
        itemDisplay.add(descriptionDisplay).colspan(3).center()
                .width(Gdx.graphics.getWidth() * 0.31f)
                .height(Gdx.graphics.getHeight() * 0.31f);
        itemDisplay.setPosition(Gdx.graphics.getWidth() / 2f, Gdx.graphics.getHeight() * 0.361f);

        artefactShop.addActor(itemDisplay);

        leftButton.setPosition(Gdx.graphics.getWidth() / 2f - Gdx.graphics.getWidth() * 0.3f,
                Gdx.graphics.getHeight() / 2f);
        leftButton.setSize(Gdx.graphics.getWidth() * 0.029f, Gdx.graphics.getWidth() * 0.029f);

        rightButton.setPosition(Gdx.graphics.getWidth() / 2f + Gdx.graphics.getWidth() * 0.27f,
                Gdx.graphics.getHeight() / 2f);
        rightButton.setSize(Gdx.graphics.getWidth() * 0.029f, Gdx.graphics.getWidth() * 0.029f);

        buyButton.setPosition(Gdx.graphics.getWidth() / 2f + Gdx.graphics.getWidth() * 0.2f,
                Gdx.graphics.getHeight() / 2f - Gdx.graphics.getHeight() * 0.33f);
        buyButton.setSize(Gdx.graphics.getWidth() * 0.094f, Gdx.graphics.getHeight() * 0.156f);

        priceDisplay.setPosition(Gdx.graphics.getWidth() * 0.2f,
                Gdx.graphics.getHeight() / 2f - Gdx.graphics.getHeight() * 0.33f);
        priceDisplay.setSize(Gdx.graphics.getWidth() * 0.094f, Gdx.graphics.getHeight() * 0.156f);

        subtitle.setPosition(Gdx.graphics.getWidth() / 2f - 210f,
                Gdx.graphics.getHeight() * 0.75f);

        backButton.setPosition(Gdx.graphics.getWidth() * 0.15f + 30f,
                Gdx.graphics.getHeight() * 0.85f -70f);
        backButton.setSize(40f, 40f);
    }

    private void carouselRight() {
        Node<Artefact> temp = current;
        current = stock.head.next;
        stock.head = stock.head.next;
        stock.tail = temp;
        prevStats = FileLoader.readClass(ArtefactConfig.class,
                Artefact.getFilepath(current.prev.t));
        stats = FileLoader.readClass(ArtefactConfig.class,
                Artefact.getFilepath(current.t));
        nextStats = FileLoader.readClass(ArtefactConfig.class,
                Artefact.getFilepath(current.next.t));

        priceDisplay.setText("Gold: " + Integer.toString(stats.goldCost));
        sufficientFunds = MainArea.getInstance().getGameArea().getPlayer()
                .getComponent(InventoryComponent.class)
                .hasGold(stats.goldCost);
        buyButton.getStyle().up = sufficientFunds ? goldenDrawable
                : redDrawable;
        buyButton.getStyle().down = sufficientFunds ? clickDrawable
                : redDrawable;
        buyButton.getStyle().checked = sufficientFunds ? goldenDrawable
                : redDrawable;
        descriptionDisplay
                .setText(stats.name + "\n" + stats.description + "\n"
                        + "Inventory Count: "
                        + MainArea.getInstance().getGameArea().getPlayer()
                        .getComponent(InventoryComponent.class)
                        .getItemCount(current.t));
        i = i == Artefact.getAllartefactTypes().size() ? 1 : i + 1;
        itemNumber.setText("Item " + i + "/" + Artefact.getAllartefactTypes().size());
        currentItem.setDrawable(new TextureRegionDrawable(
                new Texture(Gdx.files.internal(
                        stats.itemBackgroundImagePath))));
        prevItem.setDrawable(new TextureRegionDrawable(
                new Texture(Gdx.files.internal(
                        prevStats.itemBackgroundImagePath))));
        nextItem.setDrawable(new TextureRegionDrawable(
                new Texture(Gdx.files.internal(
                        nextStats.itemBackgroundImagePath))));
    }

    private void carouselLeft() {
        Node<Artefact> temp = current;
        current = stock.head.prev;
        stock.head = stock.head.prev;
        stock.tail = temp.prev;
        prevStats = FileLoader.readClass(ArtefactConfig.class,
                Artefact.getFilepath(current.prev.t));
        stats = FileLoader.readClass(ArtefactConfig.class,
                Artefact.getFilepath(current.t));
        nextStats = FileLoader.readClass(ArtefactConfig.class,
                Artefact.getFilepath(current.next.t));
        priceDisplay.setText("Gold: " + Integer.toString(stats.goldCost));
        sufficientFunds = MainArea.getInstance().getGameArea().getPlayer()
                .getComponent(InventoryComponent.class)
                .hasGold(stats.goldCost);
        buyButton.getStyle().up = sufficientFunds ? goldenDrawable
                : redDrawable;
        buyButton.getStyle().down = sufficientFunds ? clickDrawable
                : redDrawable;
        buyButton.getStyle().checked = sufficientFunds ? goldenDrawable
                : redDrawable;
        descriptionDisplay
                .setText(stats.name + "\n" + stats.description + "\n"
                        + "Inventory Count: "
                        + MainArea.getInstance().getGameArea().getPlayer()
                        .getComponent(InventoryComponent.class)
                        .getItemCount(current.t));
        i = i == 1 ? Artefact.getAllartefactTypes().size() : i - 1;
        itemNumber.setText("Item " + i + "/" + Artefact.getAllartefactTypes().size());
        currentItem.setDrawable(new TextureRegionDrawable(
                new Texture(Gdx.files.internal(
                        stats.itemBackgroundImagePath))));
        prevItem.setDrawable(new TextureRegionDrawable(
                new Texture(Gdx.files.internal(
                        prevStats.itemBackgroundImagePath))));
        nextItem.setDrawable(new TextureRegionDrawable(
                new Texture(Gdx.files.internal(
                        nextStats.itemBackgroundImagePath))));
    }

    private void buyItem() {
        if (MainArea.getInstance().getGameArea().getPlayer()
                .getComponent(InventoryComponent.class)
                .hasGold(stats.goldCost)) {
            logger.info("Sufficient Gold");
            MainArea.getInstance().getGameArea().getPlayer()
                    .getComponent(InventoryComponent.class)
                    .addGold(-1 * stats.goldCost);
            MainArea.getInstance().getGameArea().getPlayer()
                    .getComponent(PlayerStatsDisplay.class)
                    .updateResourceAmount();
            Sound coinSound = Gdx.audio.newSound(
                    Gdx.files.internal("sounds/coin.mp3"));
            coinSound.play();
            MainArea.getInstance().getGameArea().getPlayer()
                    .getComponent(InventoryComponent.class)
                    .addItems(current.t);

            // Trigger events for achievements
            ServiceLocator.getAchievementHandler().getEvents().trigger(AchievementHandler.EVENT_SHOP_ITEM_BOUGHT, 14);
            entity.getEvents().trigger("updateArtefact");
        } else {
            logger.info("Insufficient gold!");
            Sound filesound = Gdx.audio.newSound(
                    Gdx.files.internal("sounds/purchase_fail.mp3"));
            filesound.play();
        }
        sufficientFunds = MainArea.getInstance().getGameArea().getPlayer()
                .getComponent(InventoryComponent.class)
                .hasGold(stats.goldCost);
        buyButton.getStyle().up = sufficientFunds ? goldenDrawable
                : redDrawable;
        buyButton.getStyle().down = sufficientFunds ? clickDrawable
                : redDrawable;
        buyButton.getStyle().checked = sufficientFunds ? goldenDrawable
                : redDrawable;
        descriptionDisplay
                .setText(stats.name + "\n" + stats.description + "\n"
                        + "Inventory Count: "
                        + MainArea.getInstance().getGameArea().getPlayer().getComponent(
                                InventoryComponent.class)
                        .getItemCount(current.t));
    }

    private void openShop() {
        i = 0;
        current = stock.tail;
        carouselRight();
        artefactShop.setVisible(true);

    }

    private void closeShop() {
        artefactShop.setVisible(false);
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
        artefactShop.clear();
        super.dispose();
    }
}
