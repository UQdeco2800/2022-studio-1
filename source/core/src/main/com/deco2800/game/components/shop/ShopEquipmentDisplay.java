package com.deco2800.game.components.shop;

import com.deco2800.game.components.CombatStatsComponent;
import com.deco2800.game.components.shop.equipments.Equipments;
import com.deco2800.game.entities.configs.EquipmentConfig;
import com.deco2800.game.files.FileLoader;

import java.util.List;

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
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.deco2800.game.components.player.InventoryComponent;
import com.deco2800.game.ui.UIComponent;

/**
 * Displays UI specific to the ShopEquipmentScreen
 */
public class ShopEquipmentDisplay extends UIComponent {
    private static final Logger logger = LoggerFactory.getLogger(ShopEquipmentDisplay.class);
    private static final float Z_INDEX = 2f;

    Table table1;
    Table table2;
    Table table3;
    Table table4;
    Table table5;
    Table table6;
    Table table7;
    Table table8;

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

        table1 = new Table();
        table1.setFillParent(true);
        table1.center().bottom();

        table2 = new Table();
        table2.setFillParent(true);
        table2.center().padTop(100);

        table3 = new Table();
        table3.setFillParent(true);
        table3.center().left().padLeft(150).padTop(100);

        table4 = new Table();
        table4.setFillParent(true);
        table4.center().right().padRight(150).padTop(100);

        table5 = new Table();
        table5.setFillParent(true);
        table5.left().bottom().left();

        table6 = new Table();
        table6.setFillParent(true);
        table6.right().bottom().right();

        table7 = new Table();
        table7.setFillParent(true);
        table7.top().left().padLeft(10).padTop(40);

        table8 = new Table();
        table8.setFillParent(true);
        table8.top().left().padLeft(75).padTop(115);

        // Create the carousel for the items
        stock = new CircularLinkedList<>();
        List<Equipments> equipmentOptions = Equipments.getAllEquipmentTypes();
        for (Equipments e : equipmentOptions) {
            stock.add(e);
        }

        current = stock.head;
        i = 1;
        itemNumber = new Label("Item " + i + "/" + equipmentOptions.size(), skin, "button");
        itemNumber.setFontScale(1f);
        itemNumber.setColor(skin.getColor("black"));

        // reads the current equipment's attributes
        prevStats = FileLoader.readClass(EquipmentConfig.class, Equipments.getFilepath(current.prev.t));
        stats = FileLoader.readClass(EquipmentConfig.class, Equipments.getFilepath(current.t));
        nextStats = FileLoader.readClass(EquipmentConfig.class, Equipments.getFilepath(current.next.t));

        // Create the current equipment to display
        currentTexture = new Texture(Gdx.files.internal(stats.itemBackgroundImagePath));
        currentItem = new Image(currentTexture);

        prevTexture = new Texture(Gdx.files.internal(prevStats.itemBackgroundImagePath));
        prevItem = new Image(prevTexture);

        nextTexture = new Texture(Gdx.files.internal(nextStats.itemBackgroundImagePath));
        nextItem = new Image(nextTexture);

        // Create textures for arrows, price, description and buy button
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

        // create right button
        rightButton = new Button(right);
        rightButton.setTransform(true);

        // create price sticker
        priceDisplay = ShopUtils.createImageTextButton(

                // Integer.toString(current.t.getPrice()), skin.getColor("black"),
                // displays the cost of the equipments from stats
                "Gold: " + Integer.toString(stats.goldCost), skin.getColor("black"),

                "button", 1f,
                goldenDrawable, goldenDrawable,
                skin,
                true);

        // create description sticker
        descriptionDisplay = ShopUtils.createImageTextButton(
                stats.name + "\n" + stats.description,
                skin.getColor("black"),
                "button", 1f,
                brownDrawable, brownDrawable, skin,
                true);

        // create buy button
        buyButton = ShopUtils.createImageTextButton("BUY", skin.getColor("black"), "button", 1f,
                brownDrawable, goldenDrawable,
                skin,
                false);

        // create the back button
        backTexture = new Texture(Gdx.files.internal("images/backButton.png"));
        upBack = new TextureRegionDrawable(backTexture);
        backButton = new ImageButton(upBack, upBack);

        // Add listeners to relevant buttons
        rightButton.addListener(
                new ChangeListener() {
                    @Override
                    public void changed(ChangeEvent changeEvent, Actor actor) {
                        logger.info("Right button clicked");

                        // the following follows the previous style of implementation of the carousel
                        Node<Equipments> temp1 = current;
                        current = stock.head.next;
                        stock.head = stock.head.next;
                        stock.tail = temp1;
                        // read the stats of the new current
                        prevStats = FileLoader.readClass(EquipmentConfig.class, Equipments.getFilepath(current.prev.t));
                        stats = FileLoader.readClass(EquipmentConfig.class, Equipments.getFilepath(current.t));
                        nextStats = FileLoader.readClass(EquipmentConfig.class, Equipments.getFilepath(current.next.t));

                        // priceDisplay.setText(Integer.toString(current.t.getPrice()));
                        priceDisplay.setText("Gold: " + Integer.toString(stats.goldCost));

                        descriptionDisplay
                                .setText(stats.name + "\n" + stats.description);
                        i = i == equipmentOptions.size() ? 1 : i + 1;
                        itemNumber.setText("Item " + i + "/" + equipmentOptions.size());
                        currentItem.setDrawable(new TextureRegionDrawable(
                                new Texture(Gdx.files.internal(stats.itemBackgroundImagePath))));
                        prevItem.setDrawable(new TextureRegionDrawable(
                                new Texture(Gdx.files.internal(prevStats.itemBackgroundImagePath))));
                        nextItem.setDrawable(new TextureRegionDrawable(
                                new Texture(Gdx.files.internal(nextStats.itemBackgroundImagePath))));

                        System.out.println("Current equipment in display:" + current.t.toString());
                    }
                });

        leftButton.addListener(
                new ChangeListener() {
                    @Override
                    public void changed(ChangeEvent changeEvent, Actor actor) {
                        logger.info("Left button clicked");

                        Node<Equipments> temp1 = current;
                        current = stock.head.prev;
                        stock.head = stock.head.prev;
                        stock.tail = temp1.prev;
                        prevStats = FileLoader.readClass(EquipmentConfig.class, Equipments.getFilepath(current.prev.t));
                        stats = FileLoader.readClass(EquipmentConfig.class, Equipments.getFilepath(current.t));
                        nextStats = FileLoader.readClass(EquipmentConfig.class, Equipments.getFilepath(current.next.t));

                        // priceDisplay.setText(Integer.toString(current.t.getPrice()));
                        priceDisplay.setText("Gold: " + Integer.toString(stats.goldCost));
                        descriptionDisplay
                                .setText(stats.name + "\n" + stats.description);
                        i = i == 1 ? equipmentOptions.size() : i - 1;
                        itemNumber.setText("Item " + i + "/" + equipmentOptions.size());
                        currentItem.setDrawable(new TextureRegionDrawable(
                                new Texture(Gdx.files.internal(stats.itemBackgroundImagePath))));
                        prevItem.setDrawable(new TextureRegionDrawable(
                                new Texture(Gdx.files.internal(prevStats.itemBackgroundImagePath))));
                        nextItem.setDrawable(new TextureRegionDrawable(
                                new Texture(Gdx.files.internal(nextStats.itemBackgroundImagePath))));
                        System.out.println("Current equipment in display:" + current.t.toString());
                    }
                });

        buyButton.addListener(
                new ChangeListener() {
                    @Override
                    public void changed(ChangeEvent changeEvent, Actor actor) {

                        logger.info("Buy button clicked");

                        // checks if the player has sufficient gold
                        if (entity.getComponent(InventoryComponent.class).hasGold(stats.goldCost)) {
                            logger.info("Sufficient Gold");

                            // checks the type of equipment
                            if (stats.type.equals("weapon")) {

                                // checks if there's a previously saved weapon
                                if (entity.getComponent(InventoryComponent.class).getWeapon() != null) {

                                    // if the current weapon is the same as the weapon that the player is buying,
                                    // alert it as invalid purchase
                                    if (entity.getComponent(InventoryComponent.class).getWeapon() == current.t) {
                                        logger.info("Already has weapon, invalid purchase!");
                                        filesound.play();
                                        buyButton.setColor(255, 0, 0, 1);

                                    } else {
                                        entity.getComponent(InventoryComponent.class).addGold(-1 * stats.goldCost);
                                        coinSound.play();
                                        buyButton.setColor(121, 15, 85, 1);

                                        // read the stats of the old weapon
                                        EquipmentConfig prevWeapon = FileLoader.readClass(EquipmentConfig.class,
                                                Equipments.getFilepath(entity.getComponent(InventoryComponent.class)
                                                        .getWeapon()));

                                        // remove the old weapon effect from player
                                        entity.getComponent(CombatStatsComponent.class).setBaseAttack(
                                                entity.getComponent(CombatStatsComponent.class)
                                                        .getBaseAttack() / prevWeapon.attack);

                                        // sets player weapon to new weapon and change attack accordingly
                                        entity.getComponent(InventoryComponent.class).setWeapon(current.t);
                                        entity.getComponent(CombatStatsComponent.class).setBaseAttack(
                                                entity.getComponent(CombatStatsComponent.class).getBaseAttack()
                                                        * stats.attack);
                                    }

                                } else {
                                    entity.getComponent(InventoryComponent.class).addGold(-1 * stats.goldCost);
                                    coinSound.play();
                                    buyButton.setColor(121, 15, 85, 1);

                                    entity.getComponent(InventoryComponent.class).setWeapon(current.t);
                                    entity.getComponent(CombatStatsComponent.class).setBaseAttack(
                                            entity.getComponent(CombatStatsComponent.class).getBaseAttack()
                                                    * stats.attack);
                                }

                            } else {

                                // checks if the equipment is of type helmet
                                if (current.t == Equipments.HELMET) {
                                    if (entity.getComponent(InventoryComponent.class).getHelmet() != null) {

                                        // invalid purchase if the helmet that are bought is already in the inventory
                                        if (entity.getComponent(InventoryComponent.class).getHelmet() == current.t) {
                                            logger.info("Already has helmet, invalid purchase!");
                                            filesound.play();
                                            buyButton.setColor(255, 0, 0, 1);
                                        } else {

                                            // read the old helmet stat
                                            EquipmentConfig prevHelmet = FileLoader.readClass(EquipmentConfig.class,
                                                    Equipments.getFilepath(entity.getComponent(InventoryComponent.class)
                                                            .getHelmet()));
                                            // remove old helmet effect from player combat stat
                                            entity.getComponent(CombatStatsComponent.class).setBaseDefense(
                                                    entity.getComponent(CombatStatsComponent.class).getBaseDefense()
                                                            - prevHelmet.defense);

                                            // sets new helmet to player inventory and changes stat accordingly
                                            entity.getComponent(InventoryComponent.class).setHelmet(current.t);
                                            entity.getComponent(CombatStatsComponent.class).setBaseDefense(
                                                    entity.getComponent(CombatStatsComponent.class).getBaseDefense()
                                                            + stats.defense);

                                            entity.getComponent(InventoryComponent.class).addGold(-1 * stats.goldCost);
                                            coinSound.play();
                                            buyButton.setColor(121, 15, 85, 1);
                                        }
                                    } else {
                                        entity.getComponent(InventoryComponent.class).setHelmet(current.t);
                                        entity.getComponent(CombatStatsComponent.class).setBaseDefense(
                                                entity.getComponent(CombatStatsComponent.class).getBaseDefense()
                                                        + stats.defense);
                                        entity.getComponent(InventoryComponent.class).addGold(-1 * stats.goldCost);
                                        coinSound.play();
                                        buyButton.setColor(121, 15, 85, 1);
                                    }

                                } else {
                                    if (entity.getComponent(InventoryComponent.class).getChestplate() != null) {

                                        // invalid purchase if the chestplate is already in inventory
                                        if (entity.getComponent(InventoryComponent.class)
                                                .getChestplate() == current.t) {
                                            logger.info("Already has chestplate, invalid purchase!");
                                            filesound.play();
                                            buyButton.setColor(255, 0, 0, 1);
                                        } else {

                                            // reads old chestplate stat
                                            EquipmentConfig prevChestplate = FileLoader.readClass(EquipmentConfig.class,
                                                    Equipments.getFilepath(entity.getComponent(InventoryComponent.class)
                                                            .getChestplate()));

                                            // remove old chestplate effect from player
                                            entity.getComponent(CombatStatsComponent.class).setBaseDefense(
                                                    entity.getComponent(CombatStatsComponent.class).getBaseDefense()
                                                            - prevChestplate.defense);

                                            // adds new chestplate to player inventory and stat
                                            entity.getComponent(InventoryComponent.class).setChestplate(current.t);
                                            entity.getComponent(CombatStatsComponent.class).setBaseDefense(
                                                    entity.getComponent(CombatStatsComponent.class).getBaseDefense()
                                                            + stats.defense);

                                            entity.getComponent(InventoryComponent.class).addGold(-1 * stats.goldCost);
                                            coinSound.play();
                                            buyButton.setColor(121, 15, 85, 1);
                                        }
                                    } else {
                                        entity.getComponent(InventoryComponent.class).setChestplate(current.t);
                                        entity.getComponent(CombatStatsComponent.class).setBaseDefense(
                                                entity.getComponent(CombatStatsComponent.class).getBaseDefense()
                                                        + stats.defense);
                                        entity.getComponent(InventoryComponent.class).addGold(-1 * stats.goldCost);
                                        coinSound.play();
                                        buyButton.setColor(121, 15, 85, 1);
                                    }
                                }
                            }
                        } else {
                            logger.info("Insufficient gold!");
                            filesound.play();
                            buyButton.setColor(255, 0, 0, 1);
                        }
                        entity.getComponent(CommonShopComponents.class).getGoldButton().setText(
                                Integer.toString(entity.getComponent(InventoryComponent.class).getGold()) + "    ");
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

        subtitle = new Label("Equipment", skin, "title");
        subtitle.setFontScale(1f);
        subtitle.setColor(skin.getColor("black"));

        // Add items to the stage
        table3.add(leftButton).width(50f).height(50f);
        table2.add(prevItem).width(100f).height(100f);
        table2.add(currentItem).width(150f).height(150f);
        table2.add(nextItem).width(100f).height(100f);
        table2.row();
        table2.add(itemNumber).colspan(3).center();
        table4.add(rightButton).width(50f).height(50f);
        table5.add(priceDisplay).width(250f).height(150f);
        table1.add(descriptionDisplay).width(400f).height(200f);
        table6.add(buyButton).width(250f).height(150f);
        table7.add(backButton).width(50f).height(50f);
        table8.add(subtitle);
        stage.addActor(table1);
        stage.addActor(table2);
        stage.addActor(table3);
        stage.addActor(table4);
        stage.addActor(table5);
        stage.addActor(table6);
        stage.addActor(table7);
        stage.addActor(table8);

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
        table1.clear();
        table2.clear();
        table3.clear();
        table4.clear();
        table5.clear();
        table6.clear();
        table7.clear();
        table8.clear();
        stage.clear();
        super.dispose();
    }
}
