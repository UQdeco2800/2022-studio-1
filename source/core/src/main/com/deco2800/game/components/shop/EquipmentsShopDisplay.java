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
import com.deco2800.game.components.CombatStatsComponent;
import com.deco2800.game.components.player.InventoryComponent;
import com.deco2800.game.components.player.PlayerStatsDisplay;
import com.deco2800.game.components.shop.equipments.Equipments;
import com.deco2800.game.entities.configs.EquipmentConfig;
import com.deco2800.game.files.FileLoader;
import com.deco2800.game.rendering.AnimationRenderComponent;
import com.deco2800.game.services.AchievementHandler;
import com.deco2800.game.services.ServiceLocator;
import com.deco2800.game.ui.UIComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class EquipmentsShopDisplay extends UIComponent {
    private static final Logger logger = LoggerFactory.getLogger(EquipmentsShopDisplay.class);
    private static final float Z_INDEX = 2f;

    Table itemDisplay;
    Group equipmentShop;

    private Sound filesound = Gdx.audio.newSound(Gdx.files.internal("sounds/purchase_fail.mp3"));
    private Sound coinSound = Gdx.audio.newSound(Gdx.files.internal("sounds/coin.mp3"));

    private CircularLinkedList<Equipments> stock;
    private Node<Equipments> current;
    private EquipmentConfig stats;
    private EquipmentConfig prevStats;
    private EquipmentConfig nextStats;

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
        entity.getEvents().addListener("equipmentShop", this::openShop);
        entity.getEvents().addListener("closeAll", this::closeShop);
        addActor();
    }

    private void addActor() {
        equipmentShop = new Group();
        itemDisplay = new Table();
        itemDisplay.setFillParent(true);
        itemDisplay.setSize((float) (Gdx.graphics.getWidth() * 0.6), (float) (Gdx.graphics.getHeight() * 0.4));

        // Create linked list of the available shop stock
        stock = new CircularLinkedList<Equipments>();
        List<Equipments> equipmentOptions = Equipments.getAllEquipmentTypes();
        for (Equipments e : equipmentOptions) {
            stock.add(e);
        }

        itemNumber = new Label("" + equipmentOptions.size(), skin, "button");
        itemNumber.setFontScale(1.5f);
        itemNumber.setColor(skin.getColor("black"));
        // Create the current equipments to display
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
        priceDisplay = ShopUtils.createImageTextButton(
                "", skin.getColor("black"),
                "button", 1f,
                goldenDrawable, goldenDrawable,
                skin,
                true);

        // create description sticker
        descriptionDisplay = ShopUtils.createImageTextButton("",
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

        subtitle = new Label("EQUIPMENT", skin, "title");
        subtitle.setFontScale(2f);
        subtitle.setColor(skin.getColor("black"));

        itemDisplay.add(prevItem).width(Gdx.graphics.getWidth() * 0.058f).height(Gdx.graphics.getWidth() * 0.058f);
        itemDisplay.add(currentItem).width(Gdx.graphics.getWidth() * 0.087f).height(Gdx.graphics.getWidth() * 0.087f);
        itemDisplay.add(nextItem).width(Gdx.graphics.getWidth() * 0.058f).height(Gdx.graphics.getWidth() * 0.058f);
        itemDisplay.row();
        itemDisplay.add(itemNumber).colspan(3).center();
        itemDisplay.row();
        itemDisplay.add(descriptionDisplay).colspan(3).center()
                .width(Gdx.graphics.getWidth() * 0.17f)
                .height(Gdx.graphics.getHeight() * 0.31f);
        itemDisplay.setPosition(Gdx.graphics.getWidth() / 2f, Gdx.graphics.getHeight() * 0.361f);

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

        subtitle.setPosition(Gdx.graphics.getWidth() * 0.45f,
                Gdx.graphics.getHeight() * 0.75f);

        backButton.setPosition(Gdx.graphics.getWidth() * 0.15f + 30f,
                Gdx.graphics.getHeight() * 0.85f -70f);
        backButton.setSize(40f, 40f);

        equipmentShop.addActor(itemDisplay);
        equipmentShop.addActor(leftButton);
        equipmentShop.addActor(rightButton);
        equipmentShop.addActor(buyButton);
        equipmentShop.addActor(priceDisplay);
        equipmentShop.addActor(subtitle);
        equipmentShop.addActor(backButton);
        equipmentShop.setVisible(false);
        stage.addActor(equipmentShop);

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
                        closeShop();
                        entity.getEvents().trigger("shop");
                    }
                });
    }

    private void buyItem() {
        if (MainArea.getInstance().getGameArea().getPlayer()
                .getComponent(InventoryComponent.class)
                .hasGold(stats.goldCost)) {
            logger.info("Sufficient Gold");

            // checks the type of equipment
            if (stats.type.equals("weapon")) {
                // if the current weapon is the same as the weapon that
                // the player is buying,
                // alert it as invalid purchase
                if (MainArea.getInstance().getGameArea().getPlayer()
                        .getComponent(InventoryComponent.class)
                        .getWeapon() == current.t
                        || MainArea.getInstance().getGameArea().getPlayer()
                        .getComponent(InventoryComponent.class)
                        .getEquipmentList()
                        .contains(current.t)) {
                    logger.info("Already has weapon, invalid purchase!");
                    filesound.play();
                } else {
                    MainArea.getInstance().getGameArea().getPlayer()
                            .getComponent(InventoryComponent.class)
                            .addGold(-1 * stats.goldCost);
                    // sets player weapon to new weapon and change
                    // attack accordingly
                    MainArea.getInstance().getGameArea().getPlayer()
                            .getComponent(InventoryComponent.class)
                            .setWeapon(current.t);
                    MainArea.getInstance().getGameArea().getPlayer()
                            .getComponent(InventoryComponent.class)
                            .addEquipmentToList(current.t);
                    MainArea.getInstance().getGameArea().getPlayer()
                            .getComponent(CombatStatsComponent.class)
                            .setAttackMultiplier(
                                    stats.attack);
                    MainArea.getInstance().getGameArea().getPlayer().getComponent(AnimationRenderComponent.class)
                            .startAnimation(Equipments.getAnimationName(current.t));
                    entity.getEvents().trigger("updateEquipment");

                    coinSound.play();

                    // Trigger events for achievements
                    ServiceLocator.getAchievementHandler().getEvents().trigger(AchievementHandler.EVENT_SHOP_ITEM_BOUGHT, 14);
                }
            } else {
                // invalid purchase if the armor is already in inventory
                if (MainArea.getInstance().getGameArea().getPlayer()
                        .getComponent(InventoryComponent.class)
                        .getArmor() == current.t ||
                        MainArea.getInstance().getGameArea().getPlayer()
                                .getComponent(InventoryComponent.class)
                                .getEquipmentList()
                                .contains(current.t)) {
                    logger.info("Already has this armor, invalid purchase!");
                    filesound.play();
                } else {
                    // adds new armor to player inventory and stat
                    MainArea.getInstance().getGameArea().getPlayer()
                            .getComponent(InventoryComponent.class)
                            .setArmor(current.t);
                    MainArea.getInstance().getGameArea().getPlayer()
                            .getComponent(CombatStatsComponent.class)
                            .setBaseDefense(stats.defense);
                    MainArea.getInstance().getGameArea().getPlayer()
                            .getComponent(InventoryComponent.class)
                            .addEquipmentToList(current.t);
                    MainArea.getInstance().getGameArea().getPlayer()
                            .getComponent(InventoryComponent.class)
                            .addGold(-1 * stats.goldCost);
                    coinSound.play();
                    entity.getEvents().trigger("updateEquipment");

                    // Trigger event for item Bought
                    ServiceLocator.getAchievementHandler().getEvents().trigger(AchievementHandler.EVENT_SHOP_ITEM_BOUGHT, 14);
                }
            }
        } else {
            logger.info("Insufficient gold!");
            filesound.play();
        }
        sufficientFunds = (MainArea.getInstance().getGameArea().getPlayer()
                .getComponent(InventoryComponent.class)
                .hasGold(stats.goldCost)
                && MainArea.getInstance().getGameArea().getPlayer()
                .getComponent(InventoryComponent.class)
                .countInEquipmentList(current.t) == 0);
        buyButton.getStyle().up = sufficientFunds ? goldenDrawable
                : redDrawable;
        buyButton.getStyle().down = sufficientFunds ? clickDrawable
                : redDrawable;
        buyButton.getStyle().checked = sufficientFunds ? goldenDrawable
                : redDrawable;

        MainArea.getInstance().getGameArea().getPlayer().getComponent(PlayerStatsDisplay.class).updateResourceAmount();
    }

    private void carouselLeft() {
        Node<Equipments> temp1 = current;
        current = stock.head.prev;
        stock.head = stock.head.prev;
        stock.tail = temp1.prev;
        prevStats = FileLoader.readClass(EquipmentConfig.class,
                Equipments.getFilepath(current.prev.t));
        stats = FileLoader.readClass(EquipmentConfig.class,
                Equipments.getFilepath(current.t));
        nextStats = FileLoader.readClass(EquipmentConfig.class,
                Equipments.getFilepath(current.next.t));

        priceDisplay.setText("Gold: " + Integer.toString(stats.goldCost));
        sufficientFunds = (MainArea.getInstance().getGameArea().getPlayer()
                .getComponent(InventoryComponent.class)
                .hasGold(stats.goldCost)
                && MainArea.getInstance().getGameArea().getPlayer()
                .getComponent(InventoryComponent.class)
                .countInEquipmentList(current.t) == 0);
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
                        .countInEquipmentList(current.t)
                        + "/1");
        i = i == 1 ? Equipments.getAllEquipmentTypes().size() : i - 1;
        itemNumber.setText("Item " + i + "/" + Equipments.getAllEquipmentTypes().size());
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

    private void carouselRight() {
        Node<Equipments> temp1 = current;
        current = stock.head.next;
        stock.head = stock.head.next;
        stock.tail = temp1;
        // read the stats of the new current
        prevStats = FileLoader.readClass(EquipmentConfig.class,
                Equipments.getFilepath(current.prev.t));
        stats = FileLoader.readClass(EquipmentConfig.class,
                Equipments.getFilepath(current.t));
        nextStats = FileLoader.readClass(EquipmentConfig.class,
                Equipments.getFilepath(current.next.t));

        // Integer.toString(stats.goldCost()), skin.getColor("black"),
        // displays the cost of the equipments from stats
        priceDisplay.setText("Gold: " + Integer.toString(stats.goldCost));
        sufficientFunds = (MainArea.getInstance().getGameArea().getPlayer()
                .getComponent(InventoryComponent.class)
                .hasGold(stats.goldCost)
                && MainArea.getInstance().getGameArea().getPlayer()
                .getComponent(InventoryComponent.class)
                .countInEquipmentList(current.t) == 0);
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
                        .countInEquipmentList(current.t)
                        + "/1");
        i = i == Equipments.getAllEquipmentTypes().size() ? 1 : i + 1;
        itemNumber.setText("Item " + i + "/" + Equipments.getAllEquipmentTypes().size());
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

    private void openShop() {
        i = 0;
        current = stock.tail;
        carouselRight();
        equipmentShop.setVisible(true);
    }

    private void closeShop() {
        equipmentShop.setVisible(false);
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
