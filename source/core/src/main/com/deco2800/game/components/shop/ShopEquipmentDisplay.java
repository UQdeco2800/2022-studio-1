package com.deco2800.game.components.shop;

import com.deco2800.game.components.CombatStatsComponent;
import com.deco2800.game.components.shop.artefacts.*;
import com.deco2800.game.components.shop.equipments.Equipments;
import com.deco2800.game.entities.configs.EquipmentConfig;
import com.deco2800.game.files.FileLoader;
import net.dermetfan.gdx.physics.box2d.PositionController;
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

    private CircularLinkedList<Equipment> stock;
    private Node<Equipment> current;

    private CircularLinkedList<Equipments> stock1;
    private Node<Equipments> current1;
    private EquipmentConfig stats;

    Label subtitle;

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

        table1 = new Table();
        table1.setFillParent(true);
        table1.center().bottom().padBottom(-75);

        table2 = new Table();
        table2.setFillParent(true);
        table2.center().padTop(100);

        table3 = new Table();
        table3.setFillParent(true);
        table3.center().left().padLeft(350).padTop(100);

        table4 = new Table();
        table4.setFillParent(true);
        table4.center().right().padRight(350).padTop(100);

        table5 = new Table();
        table5.setFillParent(true);
        table5.left().bottom().padLeft(250);

        table6 = new Table();
        table6.setFillParent(true);
        table6.right().bottom().padRight(250);

        table7 = new Table();
        table7.setFillParent(true);
        table7.top().left().padLeft(10).padTop(40);

        table8 = new Table();
        table8.setFillParent(true);
        table8.top().left().padLeft(75).padTop(115);

        // Create linked list of the available shop stock
        stock = new CircularLinkedList<Equipment>();
        stock.add(new Shield());
        stock.add(new Potion());
        stock.add(new Bow());
        current = stock.head;

        //test the new equipments in the Equipments enums don't have images yet, so only the other attributes rotates
        stock1 = new CircularLinkedList<>();
        stock1.add(Equipments.AXE);
        stock1.add(Equipments.SWORD);
        stock1.add(Equipments.TRIDENT);
        stock1.add(Equipments.LV1_HELMET);
        stock1.add(Equipments.LV2_HELMET);
        stock1.add(Equipments.LV3_HELMET);
        stock1.add(Equipments.LV1_CHESTPLATE);
        stock1.add(Equipments.LV2_CHESTPLATE);
        stock1.add(Equipments.LV3_CHESTPLATE);
        current1 = stock1.head;
        //reads the current equipment's attributes
        stats = FileLoader.readClass(EquipmentConfig.class, Equipments.getFilepath(current1.t));


        // Create the current artefact to display
        currentTexture = new Texture(Gdx.files.internal(current.t.getTexture()));
        currentItem = new Image(currentTexture);

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

        // create right button
        rightButton = new Button(right);
        rightButton.setTransform(true);

        // create price sticker
        priceDisplay = ShopUtils.createImageTextButton(

                //Integer.toString(current.t.getPrice()), skin.getColor("black"),
                //displays the cost of the equipments from stats
                Integer.toString(stats.goldCost), skin.getColor("black"),

                "button", 1f,
                goldenDrawable, goldenDrawable,
                skin,
                true);

        // create description sticker
        descriptionDisplay = ShopUtils.createImageTextButton(
                current.t.getName() + "\n" + current.t.getDescription(),
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
                        Node<Equipment> temp = current;
                        current = stock.head.next;
                        stock.head = stock.head.next;
                        stock.tail = temp;

                        //the following follows the previous style of implementation of the carousel
                        Node<Equipments> temp1 = current1;
                        current1 = stock1.head.next;
                        stock1.head = stock1.head.next;
                        stock1.tail = temp1;
                        //read the stats of the new current
                        stats = FileLoader.readClass(EquipmentConfig.class, Equipments.getFilepath(current1.t));

                        //priceDisplay.setText(Integer.toString(current.t.getPrice()));
                        priceDisplay.setText(Integer.toString(stats.goldCost));

                        descriptionDisplay
                                .setText(current.t.getName() + "\n" + current.t.getDescription());
                        currentItem.setDrawable(new TextureRegionDrawable(
                                new Texture(Gdx.files.internal(current.t.getTexture()))));

                        System.out.println("Current equipment in display:" + current1.t.toString());
                    }
                });

        leftButton.addListener(
                new ChangeListener() {
                    @Override
                    public void changed(ChangeEvent changeEvent, Actor actor) {
                        logger.info("Left button clicked");
                        Node<Equipment> temp = current;
                        current = stock.head.prev;
                        stock.head = stock.head.prev;
                        stock.tail = temp.prev;

                        Node<Equipments> temp1 = current1;
                        current1 = stock1.head.prev;
                        stock1.head = stock1.head.prev;
                        stock1.tail = temp1.prev;
                        stats = FileLoader.readClass(EquipmentConfig.class, Equipments.getFilepath(current1.t));

                        //priceDisplay.setText(Integer.toString(current.t.getPrice()));
                        priceDisplay.setText(Integer.toString(stats.goldCost));
                        descriptionDisplay
                                .setText(current.t.getName() + "\n" + current.t.getDescription());
                        currentItem.setDrawable(new TextureRegionDrawable(
                                new Texture(Gdx.files.internal(current.t.getTexture()))));

                        System.out.println("Current equipment in display:" + current1.t.toString());
                    }
                });

        buyButton.addListener(
                new ChangeListener() {
                    @Override
                    public void changed(ChangeEvent changeEvent, Actor actor) {
                        logger.info("Buy button clicked");

                        //if (entity.getComponent(InventoryComponent.class).hasGold(current.t.getPrice())) {
                        if (entity.getComponent(InventoryComponent.class).hasGold(stats.goldCost)) {
                            logger.info("Sufficient Gold");



                            //entity.getComponent(InventoryComponent.class).addGold(-1 * current.t.getPrice());
                            entity.getComponent(InventoryComponent.class).addGold(-1 * stats.goldCost);

                            if (stats.type.equals("weapon")) {
                                if (entity.getComponent(InventoryComponent.class).getWeapon() != null) {
                                    System.out.println("Current Weapon is " + entity.getComponent(InventoryComponent.class).getWeapon().toString());
                                    EquipmentConfig prevWeapon = FileLoader.readClass(EquipmentConfig.class,
                                            Equipments.getFilepath(entity.getComponent(InventoryComponent.class)
                                                    .getWeapon()));
                                    entity.getComponent(CombatStatsComponent.class).setBaseAttack(
                                            entity.getComponent(CombatStatsComponent.class).getBaseAttack()
                                                    - prevWeapon.attack
                                    );
                                }
                                entity.getComponent(InventoryComponent.class).setWeapon(current1.t);
                                entity.getComponent(CombatStatsComponent.class).setBaseAttack(
                                        entity.getComponent(CombatStatsComponent.class).getBaseAttack() + stats.attack
                                );

                                System.out.println("Current Weapon changed to " + entity.getComponent(InventoryComponent.class).getWeapon().toString());
                            } else {

                                if (current1.t == Equipments.LV1_HELMET || current1.t == Equipments.LV2_HELMET
                                        || current1.t == Equipments.LV3_HELMET) {
                                    if (entity.getComponent(InventoryComponent.class).getHelmet() != null) {
                                        System.out.println("Current Helmet is " + entity.getComponent(InventoryComponent.class).getHelmet().toString());
                                        EquipmentConfig prevHelmet = FileLoader.readClass(EquipmentConfig.class,
                                                Equipments.getFilepath(entity.getComponent(InventoryComponent.class)
                                                        .getHelmet()));
                                        entity.getComponent(CombatStatsComponent.class).setBaseDefense(
                                                entity.getComponent(CombatStatsComponent.class).getBaseDefense()
                                                        - prevHelmet.defense
                                        );
                                    }
                                    entity.getComponent(InventoryComponent.class).setHelmet(current1.t);
                                    entity.getComponent(CombatStatsComponent.class).setBaseDefense(
                                            entity.getComponent(CombatStatsComponent.class).getBaseDefense()
                                                    + stats.defense
                                    );
                                    System.out.println("Current Helmet changed to " + entity.getComponent(InventoryComponent.class).getHelmet().toString());
                                } else {
                                    if (entity.getComponent(InventoryComponent.class).getChestplate() != null) {
                                        System.out.println("Current Chestplate is " + entity.getComponent(InventoryComponent.class).getChestplate().toString());
                                        EquipmentConfig prevChestplate = FileLoader.readClass(EquipmentConfig.class,
                                                Equipments.getFilepath(entity.getComponent(InventoryComponent.class)
                                                        .getChestplate()));
                                        entity.getComponent(CombatStatsComponent.class).setBaseDefense(
                                                entity.getComponent(CombatStatsComponent.class).getBaseDefense()
                                                        - prevChestplate.defense
                                        );
                                    }
                                    entity.getComponent(InventoryComponent.class).setChestplate(current1.t);
                                    entity.getComponent(CombatStatsComponent.class).setBaseDefense(
                                            entity.getComponent(CombatStatsComponent.class).getBaseDefense()
                                                    + stats.defense
                                    );
                                    System.out.println("Current Chestplate changed to " + entity.getComponent(InventoryComponent.class).getChestplate().toString());
                                }
                            }

                            Sound coinSound = Gdx.audio.newSound(Gdx.files.internal("sounds/coin.mp3"));
                            coinSound.play();
                        } else {
                            logger.info("Insufficient gold!");
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
        table3.add(leftButton).width(100).height(100);
        table2.add(currentItem).width(450).height(450);
        table4.add(rightButton).width(100).height(100);
        table5.add(priceDisplay).width(300).height(300);
        table1.add(descriptionDisplay).width(450).height(450);
        table6.add(buyButton).width(300).height(300);
        table7.add(backButton).width(50).height(50);
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
