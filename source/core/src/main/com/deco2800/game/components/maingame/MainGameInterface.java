package com.deco2800.game.components.maingame;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.deco2800.game.ai.tasks.AITaskComponent;
import com.deco2800.game.areas.MainArea;
import com.deco2800.game.components.CombatStatsComponent;
import com.deco2800.game.components.player.InventoryComponent;
import com.deco2800.game.components.player.KeyboardPlayerInputComponent;
import com.deco2800.game.components.shop.ShopUtils;
import com.deco2800.game.components.shop.artefacts.Artefact;
import com.deco2800.game.components.shop.artefacts.ShopBuilding;
import com.deco2800.game.components.shop.equipments.Equipments;
import com.deco2800.game.concurrency.JobSystem;
import com.deco2800.game.entities.Enemy;
import com.deco2800.game.entities.Entity;
import com.deco2800.game.entities.configs.EquipmentConfig;
import com.deco2800.game.entities.configs.ShopBuildingConfig;
import com.deco2800.game.files.FileLoader;
import com.deco2800.game.services.ServiceLocator;
import com.deco2800.game.ui.UIComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Time;
import java.util.*;

/**
 * Displays a button to exit the Main Game screen to the Main Menu screen.
 */
public class MainGameInterface extends UIComponent {
    private static final Logger logger = LoggerFactory.getLogger(MainGameExitDisplay.class);
    private static final float Z_INDEX = 2f;
    private Table leftSideTable;
    private Table rightSideTable;
    private Table table1;
    private Group group;

    private int equipmentPos = 0;
    private Image currEquipment;
    private Image prevEquipment;
    private Image nextEquipment;
    private Texture currTexture;
    private Texture prevTexture;
    private Texture nextTexture;

    private int buildingPos = 0;
    private Image currBuilding;
    private Image nextBuilding;
    private Image prevBuilding;
    private Texture currBuildingTexture;
    private Texture prevBuildingTexture;
    private Texture nextBuildingTexture;

    private Image attackItem;
    private Image defenceItem;

    private Label potionQuantity;
    private Label clockQuantity;
    private Label bedQuantity;

    private Time bedTimer;

    private EquipmentConfig equipmentStats;
    private ShopBuildingConfig buildingStats;

    private int currEquipListSize = MainArea.getInstance().getGameArea().getPlayer()
            .getComponent(InventoryComponent.class).getEquipmentList().size();

    private ArrayList<ShopBuilding> currBuildingList;

    @Override
    public void create() {
        super.create();
        addActors();
    }

    private void addActors() {
        rightSideTable = new Table();
        rightSideTable.bottom().right();
        rightSideTable.setFillParent(true);

        leftSideTable = new Table();
        leftSideTable.bottom().left();
        leftSideTable.setFillParent(true);

        group = new Group();

        Texture inventoryInterfaceTexture = new Texture(Gdx.files.internal("images/popup-border.png"));
        TextureRegionDrawable inventory = new TextureRegionDrawable(inventoryInterfaceTexture);
        ImageButton inventoryFrame = new ImageButton(inventory, inventory);
        inventoryFrame.setSize(800f, 800f);
        inventoryFrame.setPosition(Gdx.graphics.getWidth() / 2 - inventoryFrame.getWidth() / 2,
                Gdx.graphics.getHeight() / 2 - inventoryFrame.getHeight() / 2);

        Texture crossTexture = new Texture(Gdx.files.internal("images/cross.png"));
        TextureRegionDrawable cross = new TextureRegionDrawable(crossTexture);
        ImageButton crossFrame = new ImageButton(cross, cross);
        crossFrame.setSize(40f, 40f);
        crossFrame.setPosition(inventoryFrame.getX() + 60f, inventoryFrame.getY() + 610f);

        Label subtitle = new Label("Inventory", skin, "title");
        subtitle.setFontScale(1f);
        subtitle.setColor(skin.getColor("black"));
        subtitle.setPosition(inventoryFrame.getX() + 110f, inventoryFrame.getY() + 610f);

        Image heartImage = new Image(
                ServiceLocator.getResourceService().getAsset("images/uiElements/exports/heart.png", Texture.class));
        heartImage.setSize(40f, 40f);
        heartImage.setPosition(inventoryFrame.getX() + 400f, inventoryFrame.getY() + 610f);

        Image healthBarImage = new Image(
                ServiceLocator.getResourceService().getAsset("images/healthBar.png", Texture.class));
        healthBarImage.setSize(200f, 30f);
        healthBarImage.setPosition(inventoryFrame.getX() + 450f, inventoryFrame.getY() + 610f);

        Texture leftTexture = new Texture(Gdx.files.internal("images/box-border.png"));
        TextureRegionDrawable leftDrawable = new TextureRegionDrawable(leftTexture);
        ImageButton leftBox = new ImageButton(leftDrawable, leftDrawable);
        leftBox.setSize(350f, 650f);
        leftBox.setPosition(inventoryFrame.getX() + 60f, inventoryFrame.getY() + 50f);

        Texture leftTitleTexture = new Texture(Gdx.files.internal("images/description-box.png"));
        TextureRegionDrawable leftTitleDrawable = new TextureRegionDrawable(leftTitleTexture);
        ImageButton leftTitleBox = new ImageButton(leftTitleDrawable, leftTitleDrawable);
        leftTitleBox.setSize(350f, 60f);
        leftTitleBox.setPosition(leftBox.getX(), leftBox.getY() + 460f);

        Label leftTitle = new Label("Equipment", skin, "small");
        leftTitle.setColor(skin.getColor("white"));
        leftTitle.setPosition(leftBox.getX() + 125f, leftBox.getY() + 480f);

        Texture leftArrowTexture = new Texture(Gdx.files.internal("images/left_arrow.png"));
        TextureRegionDrawable leftArrowDrawable = new TextureRegionDrawable(leftArrowTexture);
        ImageButton leftArrow = new ImageButton(leftArrowDrawable, leftArrowDrawable);
        leftArrow.setSize(50f, 50f);
        leftArrow.setPosition(leftBox.getX() + 20f, leftBox.getY() + 380f);

        if (currEquipListSize == 0) {
            prevTexture = new Texture(Gdx.files.internal("images/shop-category-button.png"));
            currTexture = new Texture(Gdx.files.internal("images/shop-category-button.png"));
            nextTexture = new Texture(Gdx.files.internal("images/shop-category-button.png"));
        } else if (currEquipListSize == 1) {
            prevTexture = new Texture(Gdx.files.internal("images/shop-category-button.png"));
            nextTexture = new Texture(Gdx.files.internal("images/shop-category-button.png"));
            EquipmentConfig currData = FileLoader.readClass(EquipmentConfig.class,
                    Equipments.getFilepath(MainArea.getInstance().getGameArea().getPlayer()
                            .getComponent(InventoryComponent.class).getEquipmentList()
                            .get(0)));
            currTexture = new Texture(Gdx.files.internal(currData.itemBackgroundImagePath));
        } else if (currEquipListSize == 2) {
            prevTexture = new Texture(Gdx.files.internal("images/shop-category-button.png"));
            EquipmentConfig data = FileLoader.readClass(EquipmentConfig.class,
                    Equipments.getFilepath(MainArea.getInstance().getGameArea().getPlayer()
                            .getComponent(InventoryComponent.class).getEquipmentList()
                            .get(0)));
            currTexture = new Texture(Gdx.files.internal(data.itemBackgroundImagePath));
            data = FileLoader.readClass(EquipmentConfig.class,
                    Equipments.getFilepath(MainArea.getInstance().getGameArea().getPlayer()
                            .getComponent(InventoryComponent.class).getEquipmentList()
                            .get(1)));
            nextTexture = new Texture(Gdx.files.internal(data.itemBackgroundImagePath));
        } else {
            EquipmentConfig data = FileLoader.readClass(EquipmentConfig.class,
                    Equipments.getFilepath(MainArea.getInstance().getGameArea().getPlayer()
                            .getComponent(InventoryComponent.class).getEquipmentList()
                            .get(0)));
            currTexture = new Texture(Gdx.files.internal(data.itemBackgroundImagePath));
            data = FileLoader.readClass(EquipmentConfig.class,
                    Equipments.getFilepath(MainArea.getInstance().getGameArea().getPlayer()
                            .getComponent(InventoryComponent.class).getEquipmentList()
                            .get(1)));
            nextTexture = new Texture(Gdx.files.internal(data.itemBackgroundImagePath));
            data = FileLoader.readClass(EquipmentConfig.class,
                    Equipments.getFilepath(MainArea.getInstance().getGameArea().getPlayer()
                            .getComponent(InventoryComponent.class).getEquipmentList()
                            .get(currEquipListSize - 1)));
            prevTexture = new Texture(Gdx.files.internal(data.itemBackgroundImagePath));
        }

        prevEquipment = new Image(prevTexture);
        prevEquipment.setSize(50f, 50f);
        prevEquipment.setPosition(leftBox.getX() + 75f, leftBox.getY() + 380f);

        currEquipment = new Image(currTexture);
        currEquipment.setSize(70f, 70f);
        currEquipment.setPosition(leftBox.getX() + 135f, leftBox.getY() + 380f);

        nextEquipment = new Image(nextTexture);
        nextEquipment.setSize(50f, 50f);
        nextEquipment.setPosition(leftBox.getX() + 215f, leftBox.getY() + 380f);

        Texture rightArrowTexture = new Texture(Gdx.files.internal("images/right_arrow.png"));
        TextureRegionDrawable rightArrowDrawable = new TextureRegionDrawable(rightArrowTexture);
        ImageButton rightArrow = new ImageButton(rightArrowDrawable, rightArrowDrawable);
        rightArrow.setSize(50f, 50f);
        rightArrow.setPosition(leftBox.getX() + 275f, leftBox.getY() + 380f);

        Texture leftSmallTitle1Texture = new Texture(Gdx.files.internal("images/description-box.png"));
        TextureRegionDrawable leftSmallTitle1Drawable = new TextureRegionDrawable(leftSmallTitle1Texture);
        ImageButton leftSmallTitle1 = new ImageButton(leftSmallTitle1Drawable, leftSmallTitle1Drawable);
        leftSmallTitle1.setSize(150f, 60f);
        leftSmallTitle1.setPosition(leftBox.getX() + 20f, leftBox.getY() + 320f);

        Label leftTitle1 = new Label("DISARM", skin, "small");
        leftTitle1.setColor(skin.getColor("white"));
        leftTitle1.setPosition(leftBox.getX() + 50f, leftBox.getY() + 340f);

        Texture leftSmallTitle2Texture = new Texture(Gdx.files.internal("images/description-box.png"));
        TextureRegionDrawable leftSmallTitle2Drawable = new TextureRegionDrawable(leftSmallTitle2Texture);
        ImageButton leftSmallTitle2 = new ImageButton(leftSmallTitle2Drawable, leftSmallTitle2Drawable);
        leftSmallTitle2.setSize(150f, 60f);
        leftSmallTitle2.setPosition(leftBox.getX() + 170f, leftBox.getY() + 320f);

        Label leftTitle2 = new Label("Equip", skin, "small");
        leftTitle2.setColor(skin.getColor("white"));
        leftTitle2.setPosition(leftBox.getX() + 210f, leftBox.getY() + 340f);

        Texture descriptionTexture = new Texture(Gdx.files.internal("images/description-box.png"));
        TextureRegionDrawable descriptionDrawable = new TextureRegionDrawable(descriptionTexture);
        ImageButton description = new ImageButton(descriptionDrawable, descriptionDrawable);
        description.setSize(description.getWidth() / 2, description.getHeight() / 5);
        description.setPosition(leftBox.getX(), leftBox.getY() + 250f);

        Texture selectedTexture = new Texture(Gdx.files.internal("images/selected-item-box.png"));
        TextureRegionDrawable selectedDrawable = new TextureRegionDrawable(selectedTexture);
        ImageButton selected = new ImageButton(selectedDrawable, selectedDrawable);
        selected.setSize(selected.getWidth() / 3, selected.getHeight() / 4);
        selected.setPosition(leftBox.getX() + 60f, leftBox.getY() + 160f);

        Texture attackItemTexture;
        if (MainArea.getInstance().getGameArea().getPlayer().getComponent(InventoryComponent.class)
                .getWeapon() == null) {
            attackItemTexture = new Texture(Gdx.files.internal("images/shop-category-button.png"));
        } else {
            EquipmentConfig weapon = FileLoader.readClass(EquipmentConfig.class, Equipments
                    .getFilepath(MainArea.getInstance().getGameArea().getPlayer()
                            .getComponent(InventoryComponent.class).getWeapon()));
            attackItemTexture = new Texture(Gdx.files.internal(weapon.itemBackgroundImagePath));
        }
        attackItem = new Image(attackItemTexture);
        attackItem.setSize(40f,40f);
        attackItem.setPosition(leftBox.getX() + 115f, leftBox.getY() + 200f);

        Label attack = new Label("Attack", skin, "small");
        attack.setColor(skin.getColor("black"));
        attack.setSize(0.3f, 0.3f);
        attack.setPosition(leftBox.getX() + 105f, leftBox.getY() + 190f);

        Texture defenceItemTexture;
        if (MainArea.getInstance().getGameArea().getPlayer().getComponent(InventoryComponent.class)
                .getArmor() == null) {
            defenceItemTexture = new Texture(Gdx.files.internal("images/shop-category-button.png"));
        } else {
            EquipmentConfig armor = FileLoader.readClass(EquipmentConfig.class, Equipments
                    .getFilepath(MainArea.getInstance().getGameArea().getPlayer()
                            .getComponent(InventoryComponent.class).getArmor()));
            defenceItemTexture = new Texture(Gdx.files.internal(armor.itemBackgroundImagePath));
        }

        defenceItem = new Image(defenceItemTexture);
        defenceItem.setSize(40f,40f);
        defenceItem.setPosition(leftBox.getX() + 190f, leftBox.getY() + 200f);

        Label defence = new Label("Defence", skin, "small");
        defence.setColor(skin.getColor("black"));
        defence.setSize(0.3f, 0.3f);
        defence.setPosition(leftBox.getX() + 175f, leftBox.getY() + 190f);

        Texture rightTexture = new Texture(Gdx.files.internal("images/box-border.png"));
        TextureRegionDrawable rightDrawable = new TextureRegionDrawable(rightTexture);
        ImageButton rightBox = new ImageButton(rightDrawable, rightDrawable);
        rightBox.setSize(350f, 650f);
        rightBox.setPosition(inventoryFrame.getX() + 410f, inventoryFrame.getY() + 50f);

        Texture rightHealthTexture = new Texture(Gdx.files.internal("images/description-box.png"));
        TextureRegionDrawable rightHealthDrawable = new TextureRegionDrawable(rightHealthTexture);
        ImageButton rightHealthFrame = new ImageButton(rightHealthDrawable, rightHealthDrawable);
        rightHealthFrame.setSize(350f, 60f);
        rightHealthFrame.setPosition(rightBox.getX() - 50f, rightBox.getY() + 460f);

        Label healthTitle = new Label("Health", skin, "small");
        healthTitle.setColor(skin.getColor("white"));
        healthTitle.setPosition(rightBox.getX() + 80f, rightBox.getY() + 480f);

        Texture potionTexture = new Texture(Gdx.files.internal("images/shop-health-potion.png"));
        TextureRegionDrawable potionDrawable = new TextureRegionDrawable(potionTexture);
        ImageButton potion = new ImageButton(potionDrawable, potionDrawable);
        potion.setSize(40f, 40f);
        potion.setPosition(rightBox.getX() + 50f, rightBox.getY() + 405f);

        potionQuantity = new Label("X" + MainArea.getInstance().getGameArea().getPlayer()
                .getComponent(InventoryComponent.class).getItems().getOrDefault(Artefact.HEALTH_POTION, 0), skin,
                "large");
        potionQuantity.setColor(skin.getColor("black"));
        potionQuantity.setPosition(rightBox.getX() + 100f, rightBox.getY() + 405f);

        Texture potionUseTexture = new Texture(Gdx.files.internal("images/shop-buy-button.png"));
        TextureRegionDrawable potionUseDrawable = new TextureRegionDrawable(potionUseTexture);
        TextButton potionUseButton = ShopUtils.createInventoryButton("USE", skin.getColor("black")
                , "button", 1f, potionUseDrawable, potionUseDrawable, skin, false);
        potionUseButton.setPosition(rightBox.getX() + 230f, rightBox.getY() + 365f);

        Texture clockTexture = new Texture(Gdx.files.internal("images/shop-clock.png"));
        TextureRegionDrawable clockDrawable = new TextureRegionDrawable(clockTexture);
        ImageButton clock = new ImageButton(clockDrawable, clockDrawable);
        clock.setSize(40f, 40f);
        clock.setPosition(rightBox.getX() + 50f, rightBox.getY() + 355f);

        clockQuantity = new Label("X" + MainArea.getInstance().getGameArea().getPlayer()
                .getComponent(InventoryComponent.class).getItems().getOrDefault(Artefact.CLOCK, 0), skin, "large");
        clockQuantity.setColor(skin.getColor("black"));
        clockQuantity.setPosition(rightBox.getX() + 100f, rightBox.getY() + 355f);

        Texture clockUseTexture = new Texture(Gdx.files.internal("images/shop-buy-button.png"));
        TextureRegionDrawable clockUseDrawable = new TextureRegionDrawable(clockUseTexture);
        TextButton clockUseButton = ShopUtils.createInventoryButton("USE", skin.getColor("black")
                , "button", 1f, clockUseDrawable, clockUseDrawable, skin, false);
        clockUseButton.setPosition(rightBox.getX() + 230f, rightBox.getY() + 315f);

        Texture bedTexture = new Texture(Gdx.files.internal("images/shop-bed.png"));
        TextureRegionDrawable bedDrawable = new TextureRegionDrawable(bedTexture);
        ImageButton bed = new ImageButton(bedDrawable, bedDrawable);
        bed.setSize(40f, 40f);
        bed.setPosition(rightBox.getX() + 50f, rightBox.getY() + 305f);

        bedQuantity = new Label("X" + MainArea.getInstance().getGameArea().getPlayer()
                .getComponent(InventoryComponent.class).getItems().getOrDefault(Artefact.BED, 0), skin, "large");
        bedQuantity.setColor(skin.getColor("black"));
        bedQuantity.setPosition(rightBox.getX() + 100f, rightBox.getY() + 305f);


        Texture bedUseTexture = new Texture(Gdx.files.internal("images/shop-buy-button.png"));
        TextureRegionDrawable bedUseDrawable = new TextureRegionDrawable(bedUseTexture);
        TextButton bedUseButton = ShopUtils.createInventoryButton("USE", skin.getColor("black")
                , "button", 1f, bedUseDrawable, bedUseDrawable, skin, false);
        bedUseButton.setPosition(rightBox.getX() + 230f, rightBox.getY() + 265f);

        Texture rightBuildingTexture = new Texture(Gdx.files.internal("images/description-box.png"));
        TextureRegionDrawable rightBuildingDrawable = new TextureRegionDrawable(rightBuildingTexture);
        ImageButton rightBuildingFrame = new ImageButton(rightBuildingDrawable, rightBuildingDrawable);
        rightBuildingFrame.setSize(350f, 60f);
        rightBuildingFrame.setPosition(rightBox.getX() - 50f, rightBox.getY() + 255f);

        Label buildingTitle = new Label("Buildings", skin, "small");
        buildingTitle.setColor(skin.getColor("white"));
        buildingTitle.setPosition(rightBox.getX() + 80f, rightBox.getY() + 275f);

        Texture buildingLeftArrowTexture = new Texture(Gdx.files.internal("images/left_arrow.png"));
        TextureRegionDrawable buildingLeftArrowDrawable = new TextureRegionDrawable(buildingLeftArrowTexture);
        ImageButton buildingLeftArrow = new ImageButton(buildingLeftArrowDrawable, buildingLeftArrowDrawable);
        buildingLeftArrow.setSize(40f, 40f);
        buildingLeftArrow.setPosition(rightBox.getX() + 60f, rightBox.getY() + 205f);

        currBuildingList = getBuildingList();

        if (currBuildingList.size() == 0) {
            prevBuildingTexture = new Texture(Gdx.files.internal("images/shop-category-button.png"));
            currBuildingTexture = new Texture(Gdx.files.internal("images/shop-category-button.png"));
            nextBuildingTexture = new Texture(Gdx.files.internal("images/shop-category-button.png"));
        } else if (currBuildingList.size() == 1) {
            prevBuildingTexture = new Texture(Gdx.files.internal("images/shop-category-button.png"));

            nextBuildingTexture = new Texture(Gdx.files.internal("images/shop-category-button.png"));

            ShopBuildingConfig data = FileLoader.readClass(ShopBuildingConfig.class,
                    ShopBuilding.getFilepath(currBuildingList.get(0)));
            currBuildingTexture = new Texture(Gdx.files.internal(data.itemBackgroundImagePath));
        } else if (currBuildingList.size() == 2) {
            prevBuildingTexture = new Texture(Gdx.files.internal("images/shop-category-button.png"));

            ShopBuildingConfig data = FileLoader.readClass(ShopBuildingConfig.class,
                    ShopBuilding.getFilepath(currBuildingList.get(0)));
            currBuildingTexture = new Texture(Gdx.files.internal(data.itemBackgroundImagePath));

            data = FileLoader.readClass(ShopBuildingConfig.class,
                    ShopBuilding.getFilepath(currBuildingList.get(1)));
            nextBuildingTexture = new Texture(Gdx.files.internal(data.itemBackgroundImagePath));
        } else {
            ShopBuildingConfig data = FileLoader.readClass(ShopBuildingConfig.class,
                    ShopBuilding.getFilepath(currBuildingList.get(0)));
            currBuildingTexture = new Texture(Gdx.files.internal(data.itemBackgroundImagePath));

            data = FileLoader.readClass(ShopBuildingConfig.class,
                    ShopBuilding.getFilepath(currBuildingList.get(1)));
            nextBuildingTexture = new Texture(Gdx.files.internal(data.itemBackgroundImagePath));

            data = FileLoader.readClass(ShopBuildingConfig.class,
                    ShopBuilding.getFilepath(currBuildingList.get(currBuildingList.size() - 1)));
            prevBuildingTexture = new Texture(Gdx.files.internal(data.itemBackgroundImagePath));
        }
        prevBuilding = new Image(prevBuildingTexture);
        prevBuilding.setSize(40f,40f);
        prevBuilding.setPosition(rightBox.getX() + 105f, rightBox.getY() + 205f);

        currBuilding = new Image(currBuildingTexture);
        currBuilding.setSize(50f,50f);
        currBuilding.setPosition(rightBox.getX() + 150f, rightBox.getY() + 205f);

        nextBuilding = new Image(nextBuildingTexture);
        nextBuilding.setSize(40f,40f);
        nextBuilding.setPosition(rightBox.getX() + 205f, rightBox.getY() + 205f);

        Texture buildingRightArrowTexture = new Texture(Gdx.files.internal("images/right_arrow.png"));
        TextureRegionDrawable buildingRightArrowDrawable = new TextureRegionDrawable(buildingRightArrowTexture);
        ImageButton buildingRightArrow = new ImageButton(buildingRightArrowDrawable, buildingRightArrowDrawable);
        buildingRightArrow.setSize(40f, 40f);
        buildingRightArrow.setPosition(rightBox.getX() + 245f, rightBox.getY() + 205f);

        Texture placeButtonTexture = new Texture(Gdx.files.internal("images/shop-buy-button.png"));
        TextureRegionDrawable placeButtonDrawable = new TextureRegionDrawable(placeButtonTexture);
        ImageButton placeButton = new ImageButton(placeButtonDrawable, placeButtonDrawable);
        placeButton.setPosition(rightBox.getX() + 120f, rightBox.getY() + 135f);

        Label place = new Label("Place", skin, "small");
        place.setColor(skin.getColor("black"));
        place.setPosition(rightBox.getX() + 150f, rightBox.getY() + 180f);

        // Entering the Shop Button
        Texture shopTexture = new Texture(Gdx.files.internal("images/Shop.png"));
        TextureRegionDrawable upShop = new TextureRegionDrawable(shopTexture);
        TextureRegionDrawable downShop = new TextureRegionDrawable(shopTexture);
        ImageButton shopButton = new ImageButton(upShop, downShop);

        // Entering the Inventory Button -- need to add the inventory button
        Texture inventoryTexture = new Texture(Gdx.files.internal("images/inventory.png"));
        TextureRegionDrawable downInventory = new TextureRegionDrawable(inventoryTexture);
        TextureRegionDrawable upInventory = new TextureRegionDrawable(inventoryTexture);
        ImageButton inventoryButton = new ImageButton(upInventory, downInventory);

        // the achievements button
        Texture achievementsTexture = new Texture(Gdx.files.internal("images/Achievements.png"));
        TextureRegionDrawable upAchievements = new TextureRegionDrawable(achievementsTexture);
        TextureRegionDrawable downAchievements = new TextureRegionDrawable(achievementsTexture);
        ImageButton achievementsButton = new ImageButton(upAchievements, downAchievements);

        // Guidebook button
        Texture guideBookTexture = new Texture(Gdx.files.internal("images/uiElements/exports/help-button.png"));
        TextureRegionDrawable upGuidebook = new TextureRegionDrawable(guideBookTexture);
        TextureRegionDrawable downGuidebook = new TextureRegionDrawable(guideBookTexture);
        ImageButton guideBookButton = new ImageButton(upGuidebook, downGuidebook);

        // trigger for guidebook
        guideBookButton.addListener(
                new ChangeListener() {
                    @Override
                    public void changed(ChangeEvent changeEvent, Actor actor) {
                        logger.debug("Guidebook button clicked");
                        entity.getEvents().trigger("guideBook");
                    }
                });

        // Triggers an event when the button is pressed.
        inventoryButton.addListener(
                new ChangeListener() {
                    @Override
                    public void changed(ChangeEvent changeEvent, Actor actor) {
                        logger.debug("Inventory button clicked");
                        group.setVisible(true);
                    }
                });

        // Trigger for an achievement
        achievementsButton.addListener(
                new ChangeListener() {
                    @Override
                    public void changed(ChangeEvent changeEvent, Actor actor) {
                        logger.debug("Achievement button clicked");
                        entity.getEvents().trigger("achievement");
                    }
                });

        // trigger for shop button
        shopButton.addListener(
                new ChangeListener() {
                    @Override
                    public void changed(ChangeEvent changeEvent, Actor actor) {
                        logger.debug("Shop button clicked");
                        entity.getEvents().trigger("shop");
                    }
                });
        rightSideTable.add(guideBookButton).right().bottom().size(100f, 100f);
        crossFrame.addListener(
                new ChangeListener() {
                    @Override
                    public void changed(ChangeEvent changeEvent, Actor actor) {
                        logger.debug("Close inventory page");
                        group.setVisible(false);
                    }
                });

        leftArrow.addListener(
                new ChangeListener() {
                    @Override
                    public void changed(ChangeEvent changeEvent, Actor actor) {
                        logger.debug("Left equipment button clicked");

                        if (currEquipListSize >= 1) {
                            if (equipmentPos == 0) {
                                equipmentPos = currEquipListSize - 1;
                            } else {
                                equipmentPos -= 1;
                            }
                            if (currEquipListSize == 2 && equipmentPos == 0) {
                                equipmentStats = FileLoader.readClass(EquipmentConfig.class,
                                        Equipments.getFilepath(MainArea.getInstance()
                                                .getGameArea().getPlayer()
                                                .getComponent(InventoryComponent.class)
                                                .getEquipmentList()
                                                .get(0)));

                                currEquipment.setDrawable(new TextureRegionDrawable(
                                        new Texture(Gdx.files.internal(equipmentStats.itemBackgroundImagePath))));

                                equipmentStats = FileLoader.readClass(EquipmentConfig.class,
                                        Equipments.getFilepath(MainArea.getInstance()
                                                .getGameArea().getPlayer()
                                                .getComponent(InventoryComponent.class)
                                                .getEquipmentList()
                                                .get(1)));

                                nextEquipment.setDrawable(new TextureRegionDrawable(
                                        new Texture(Gdx.files.internal(equipmentStats.itemBackgroundImagePath))));

                                prevEquipment.setDrawable(new TextureRegionDrawable(
                                        new Texture(Gdx.files.internal("images/shop-category-button.png"))));
                            }

                            if (currEquipListSize >= 3) {
                                equipmentStats = FileLoader.readClass(EquipmentConfig.class,
                                        Equipments.getFilepath(MainArea.getInstance()
                                                .getGameArea().getPlayer()
                                                .getComponent(InventoryComponent.class)
                                                .getEquipmentList()
                                                .get(equipmentPos)));
                                currEquipment.setDrawable(new TextureRegionDrawable(
                                        new Texture(Gdx.files.internal(equipmentStats.itemBackgroundImagePath))));

                                if (equipmentPos + 1 > currEquipListSize - 1) {
                                    equipmentStats = FileLoader.readClass(EquipmentConfig.class,
                                            Equipments.getFilepath( MainArea.getInstance()
                                                    .getGameArea().getPlayer()
                                                    .getComponent (InventoryComponent.class)
                                                    .getEquipmentList()
                                                    .get(0)));
                                } else {
                                    equipmentStats = FileLoader.readClass(EquipmentConfig.class,
                                            Equipments.getFilepath(MainArea.getInstance()
                                                    .getGameArea().getPlayer()
                                                    .getComponent(InventoryComponent.class)
                                                    .getEquipmentList()
                                                    .get(equipmentPos + 1)));
                                }
                                nextEquipment.setDrawable(new TextureRegionDrawable(
                                        new Texture(Gdx.files.internal(equipmentStats.itemBackgroundImagePath))));

                                if (equipmentPos - 1 < 0) {
                                    equipmentStats = FileLoader.readClass(EquipmentConfig.class,
                                            Equipments.getFilepath( MainArea.getInstance()
                                                    .getGameArea().getPlayer()
                                                    .getComponent(InventoryComponent.class)
                                                    .getEquipmentList()
                                                    .get(currEquipListSize - 1)));
                                } else {
                                    equipmentStats = FileLoader.readClass(EquipmentConfig.class,
                                            Equipments.getFilepath(MainArea.getInstance()
                                                    .getGameArea().getPlayer()
                                                    .getComponent(InventoryComponent.class)
                                                    .getEquipmentList()
                                                    .get(equipmentPos - 1)));
                                }
                                prevEquipment.setDrawable(new TextureRegionDrawable(
                                        new Texture(Gdx.files.internal(equipmentStats.itemBackgroundImagePath))));
                            }
                        }
                    }
                });

        rightArrow.addListener(
                new ChangeListener() {
                    @Override
                    public void changed(ChangeEvent changeEvent, Actor actor) {
                        logger.debug("Right equipment button clicked");
                        if (currEquipListSize >= 1) {
                            if (equipmentPos == currEquipListSize - 1) {
                                equipmentPos = 0;
                            } else {
                                equipmentPos += 1;
                            }
                            if (currEquipListSize == 2 && equipmentPos == 1) {
                                equipmentStats = FileLoader.readClass(EquipmentConfig.class,
                                        Equipments.getFilepath(MainArea.getInstance()
                                                .getGameArea().getPlayer()
                                                .getComponent(InventoryComponent.class)
                                                .getEquipmentList()
                                                .get(1)));
                                currEquipment.setDrawable(new TextureRegionDrawable(
                                        new Texture(Gdx.files.internal(equipmentStats.itemBackgroundImagePath))));

                                equipmentStats = FileLoader.readClass(EquipmentConfig.class,
                                        Equipments.getFilepath(MainArea.getInstance()
                                                .getGameArea().getPlayer()
                                                .getComponent(InventoryComponent.class)
                                                .getEquipmentList()
                                                .get(0)));
                                prevEquipment.setDrawable(new TextureRegionDrawable(
                                        new Texture(Gdx.files.internal(equipmentStats.itemBackgroundImagePath))));

                                nextEquipment.setDrawable(new TextureRegionDrawable(
                                        new Texture(Gdx.files.internal("images/shop-category-button.png"))));
                            }

                            if (currEquipListSize >= 3) {
                                equipmentStats = FileLoader.readClass(EquipmentConfig.class,
                                        Equipments.getFilepath(MainArea.getInstance()
                                                .getGameArea().getPlayer()
                                                .getComponent(InventoryComponent.class)
                                                .getEquipmentList()
                                                .get(equipmentPos)));
                                currEquipment.setDrawable(new TextureRegionDrawable(
                                        new Texture(Gdx.files.internal(equipmentStats.itemBackgroundImagePath))));

                                if (equipmentPos + 1 > currEquipListSize - 1) {
                                    equipmentStats = FileLoader.readClass(EquipmentConfig.class,
                                            Equipments.getFilepath(MainArea.getInstance()
                                                    .getGameArea().getPlayer()
                                                    .getComponent(InventoryComponent.class)
                                                    .getEquipmentList()
                                                    .get(0)));
                                } else {
                                    equipmentStats = FileLoader.readClass(EquipmentConfig.class,
                                            Equipments.getFilepath(MainArea.getInstance()
                                                    .getGameArea().getPlayer()
                                                    .getComponent(InventoryComponent.class)
                                                    .getEquipmentList()
                                                    .get(equipmentPos + 1)));
                                }

                                nextEquipment.setDrawable(new TextureRegionDrawable(
                                        new Texture(Gdx.files.internal(equipmentStats.itemBackgroundImagePath))));

                                if (equipmentPos - 1 < 0) {
                                    equipmentStats = FileLoader.readClass(EquipmentConfig.class,
                                            Equipments.getFilepath(MainArea.getInstance()
                                                    .getGameArea().getPlayer()
                                                    .getComponent(InventoryComponent.class)
                                                    .getEquipmentList()
                                                    .get(currEquipListSize - 1)));
                                } else {
                                    equipmentStats = FileLoader.readClass(EquipmentConfig.class,
                                            Equipments.getFilepath(MainArea.getInstance()
                                                    .getGameArea().getPlayer()
                                                    .getComponent(InventoryComponent.class)
                                                    .getEquipmentList()
                                                    .get(equipmentPos - 1)));
                                }

                                prevEquipment.setDrawable(new TextureRegionDrawable(
                                        new Texture(Gdx.files.internal(equipmentStats.itemBackgroundImagePath))));
                            }
                        }
                    }
                });

        potionUseButton.addListener(
                new ChangeListener() {
                    @Override
                    public void changed(ChangeEvent changeEvent, Actor actor) {
                        logger.debug("potion use");
                        if (MainArea.getInstance().getGameArea().getPlayer()
                                .getComponent(InventoryComponent.class)
                                .useItem(Artefact.HEALTH_POTION)) {
                            MainArea.getInstance().getGameArea().getPlayer()
                                    .getComponent(CombatStatsComponent.class)
                                    .setHealth(MainArea.getInstance().getGameArea().getPlayer()
                                            .getComponent(CombatStatsComponent.class)
                                            .getMaxHealth());
                            potionQuantity.setText("X" + MainArea.getInstance().getGameArea().getPlayer()
                                    .getComponent(InventoryComponent.class)
                                    .getItems()
                                    .get(Artefact.HEALTH_POTION));
                        }
                    }
                });

        clockUseButton.addListener(
                new ChangeListener() {
                    @Override
                    public void changed(ChangeEvent changeEvent, Actor actor) {
                        logger.debug("clock use");
                        if (MainArea.getInstance().getGameArea().getPlayer()
                                .getComponent(InventoryComponent.class)
                                .useItem(Artefact.CLOCK)) {
                            clockQuantity.setText("X" + MainArea.getInstance().getGameArea().getPlayer()
                                    .getComponent(InventoryComponent.class)
                                    .getItems()
                                    .get(Artefact.CLOCK));

                            JobSystem.launch(() -> {
                                try {
                                    clockAbility(ServiceLocator.getTimeSource().getTime());
                                } catch (InterruptedException e) {
                                    logger.error(e.getMessage());
                                }
                                return null;
                            });
                        }
                    }
                });

        bedUseButton.addListener(
                new ChangeListener() {
                    @Override
                    public void changed(ChangeEvent changeEvent, Actor actor) {
                        logger.debug("bed use");
                        if (MainArea.getInstance().getGameArea().getPlayer()
                                .getComponent(InventoryComponent.class)
                                .useItem(Artefact.BED)) {
                            bedQuantity.setText("X" + MainArea.getInstance().getGameArea().getPlayer()
                                    .getComponent(InventoryComponent.class)
                                    .getItems()
                                    .get(Artefact.BED));

                            JobSystem.launch(() -> {
                                try {
                                    bedAbility(ServiceLocator.getTimeSource().getTime()
                                            , MainArea.getInstance().getGameArea().getPlayer()
                                                    .getComponent(CombatStatsComponent.class).getHealth());
                                } catch (InterruptedException e) {
                                    logger.error(e.getMessage());
                                }
                                return null;
                            });

                        }
                    }
                });

        buildingLeftArrow.addListener(
                new ChangeListener() {
                    @Override
                    public void changed(ChangeEvent changeEvent, Actor actor) {
                        logger.debug("left building arrow use");
                        if (currBuildingList.size() >= 1) {
                            if (buildingPos == 0) {
                                buildingPos = currBuildingList.size() - 1;
                            } else {
                                buildingPos -= 1;
                            }
                            if (currBuildingList.size() == 2 && buildingPos == 0) {
                                buildingStats = FileLoader.readClass(ShopBuildingConfig.class,
                                        ShopBuilding.getFilepath(currBuildingList
                                                .get(0)));
                                currBuilding.setDrawable(new TextureRegionDrawable(
                                        new Texture(Gdx.files.internal(buildingStats.itemBackgroundImagePath))));

                                buildingStats = FileLoader.readClass(ShopBuildingConfig.class,
                                        ShopBuilding.getFilepath(currBuildingList
                                                .get(1)));
                                nextBuilding.setDrawable(new TextureRegionDrawable(
                                        new Texture(Gdx.files.internal(buildingStats.itemBackgroundImagePath))));

                                prevBuilding.setDrawable(new TextureRegionDrawable(
                                        new Texture(Gdx.files.internal("images/shop-category-button.png"))
                                ));
                            }

                            if (currBuildingList.size() >= 3) {
                                buildingStats = FileLoader.readClass(ShopBuildingConfig.class,
                                        ShopBuilding.getFilepath(currBuildingList
                                                .get(buildingPos)));
                                currBuilding.setDrawable(new TextureRegionDrawable(
                                        new Texture(Gdx.files.internal(buildingStats.itemBackgroundImagePath))));

                                if (buildingPos + 1 > currBuildingList.size() - 1) {
                                    buildingStats = FileLoader.readClass(ShopBuildingConfig.class,
                                            ShopBuilding.getFilepath(currBuildingList
                                                    .get(0)));
                                } else {
                                    buildingStats = FileLoader.readClass(ShopBuildingConfig.class,
                                            ShopBuilding.getFilepath(currBuildingList
                                                    .get(buildingPos + 1)));
                                }
                                nextBuilding.setDrawable(new TextureRegionDrawable(
                                        new Texture(Gdx.files.internal(buildingStats.itemBackgroundImagePath))));

                                if (buildingPos - 1 < 0) {
                                    buildingStats = FileLoader.readClass(ShopBuildingConfig.class,
                                            ShopBuilding.getFilepath(currBuildingList
                                                    .get(currBuildingList.size() - 1)));
                                } else {
                                    buildingStats = FileLoader.readClass(ShopBuildingConfig.class,
                                            ShopBuilding.getFilepath(currBuildingList
                                                    .get(buildingPos - 1)));
                                }
                                prevBuilding.setDrawable(new TextureRegionDrawable(
                                        new Texture(Gdx.files.internal(buildingStats.itemBackgroundImagePath))));

                            }
                        }
                    }
                });

        buildingRightArrow.addListener(
                new ChangeListener() {
                    @Override
                    public void changed(ChangeEvent changeEvent, Actor actor) {
                        logger.debug("right building arrow use");
                        if (currBuildingList.size() >= 1) {
                            if (buildingPos == currBuildingList.size() - 1) {
                                buildingPos = 0;
                            } else {
                                buildingPos += 1;
                            }
                            if (currBuildingList.size() == 2 && buildingPos == 1) {
                                buildingStats = FileLoader.readClass(ShopBuildingConfig.class,
                                        ShopBuilding.getFilepath(currBuildingList
                                                .get(1)));
                                currBuilding.setDrawable(new TextureRegionDrawable(
                                        new Texture(Gdx.files.internal(buildingStats.itemBackgroundImagePath))));

                                buildingStats = FileLoader.readClass(ShopBuildingConfig.class,
                                        ShopBuilding.getFilepath(currBuildingList
                                                .get(0)));
                                prevBuilding.setDrawable(new TextureRegionDrawable(
                                        new Texture(Gdx.files.internal(buildingStats.itemBackgroundImagePath))));

                                nextBuilding.setDrawable(new TextureRegionDrawable(
                                        new Texture(Gdx.files.internal("images/shop-category-button.png"))
                                ));
                            }

                            if (currBuildingList.size() >= 3) {
                                buildingStats = FileLoader.readClass(ShopBuildingConfig.class,
                                        ShopBuilding.getFilepath(currBuildingList
                                                .get(buildingPos)));
                                currBuilding.setDrawable(new TextureRegionDrawable(
                                        new Texture(Gdx.files.internal(buildingStats.itemBackgroundImagePath))));

                                if (buildingPos + 1 > currBuildingList.size() - 1) {
                                    buildingStats = FileLoader.readClass(ShopBuildingConfig.class,
                                            ShopBuilding.getFilepath(currBuildingList
                                                    .get(0)));
                                } else {
                                    buildingStats = FileLoader.readClass(ShopBuildingConfig.class,
                                            ShopBuilding.getFilepath(currBuildingList
                                                    .get(buildingPos + 1)));
                                }
                                nextBuilding.setDrawable(new TextureRegionDrawable(
                                        new Texture(Gdx.files.internal(buildingStats.itemBackgroundImagePath))));

                                if (buildingPos - 1 < 0) {
                                    buildingStats = FileLoader.readClass(ShopBuildingConfig.class,
                                            ShopBuilding.getFilepath(currBuildingList
                                                    .get(currBuildingList.size() - 1)));
                                } else {
                                    buildingStats = FileLoader.readClass(ShopBuildingConfig.class,
                                            ShopBuilding.getFilepath(currBuildingList
                                                    .get(buildingPos - 1)));
                                }
                                prevBuilding.setDrawable(new TextureRegionDrawable(
                                        new Texture(Gdx.files.internal(buildingStats.itemBackgroundImagePath))));
                            }
                        }
                    }
                });

        placeButton.addListener(
                new ChangeListener() {
                    @Override
                    public void changed(ChangeEvent changeEvent, Actor actor) {
                        logger.debug("bed use");
                        // Add place building function
                    }
                });

        rightSideTable.add(inventoryButton).right().bottom().size(150f, 150f);
        // adding building button to the right
        leftSideTable.add(shopButton).left().bottom().size(150f, 150f);
        // adding settings to the left
        leftSideTable.add(achievementsButton).left().bottom().size(100f, 100f);
        group.addActor(inventoryFrame);
        group.addActor(subtitle);
        group.addActor(heartImage);
        group.addActor(healthBarImage);
        group.addActor(leftBox);
        group.addActor(leftTitleBox);
        group.addActor(leftTitle);
        group.addActor(leftArrow);
        group.addActor(prevEquipment);
        group.addActor(currEquipment);
        group.addActor(nextEquipment);
        group.addActor(rightArrow);
        group.addActor(leftSmallTitle1);
        group.addActor(leftSmallTitle2);
        group.addActor(leftTitle1);
        group.addActor(leftTitle2);
        group.addActor(description);
        group.addActor(selected);
        group.addActor(attackItem);
        group.addActor(attack);
        group.addActor(defenceItem);
        group.addActor(defence);
        group.addActor(rightBox);
        group.addActor(rightHealthFrame);
        group.addActor(healthTitle);
        group.addActor(potion);
        group.addActor(potionQuantity);
        group.addActor(potionUseButton);
        group.addActor(clock);
        group.addActor(clockQuantity);
        group.addActor(clockUseButton);
        group.addActor(bed);
        group.addActor(bedQuantity);
        group.addActor(bedUseButton);
        group.addActor(rightBuildingFrame);
        group.addActor(buildingTitle);
        group.addActor(buildingLeftArrow);
        group.addActor(prevBuilding);
        group.addActor(currBuilding);
        group.addActor(nextBuilding);
        group.addActor(buildingRightArrow);
        group.addActor(placeButton);
        group.addActor(place);
        group.addActor(crossFrame);
        group.setVisible(false);

        stage.addActor(leftSideTable);
        stage.addActor(rightSideTable);
        stage.addActor(group);
    }

    private ArrayList<ShopBuilding> getBuildingList() {
        ArrayList<ShopBuilding> buildings = new ArrayList<>();
        for (Map.Entry<ShopBuilding, Integer> building : MainArea.getInstance()
                .getGameArea().getPlayer().
                getComponent(InventoryComponent.class)
                .getBuildings().entrySet()) {
            buildings.add(building.getKey());
        }
        return buildings;
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
        leftSideTable.clear();
        rightSideTable.clear();
        group.clear();
        super.dispose();
    }

    public void bedAbility(float startingTime, int currentHealth) throws InterruptedException {

        while(ServiceLocator.getTimeSource().getTimeSince((long) startingTime) != 15000) {
            MainArea.getInstance().getGameArea().getPlayer().getComponent(CombatStatsComponent.class)
                    .setHealth(currentHealth +
                            Math.round(ServiceLocator.getTimeSource()
                                    .getTimeSince((long) startingTime/15000  * 45)));
        }
    }

    public void clockAbility(float startingTime) throws InterruptedException {
        while(ServiceLocator.getTimeSource().getTimeSince((long) startingTime) != 5000) {
            MainArea.getInstance().getGameArea().getPlayer()
                    .getComponent(CombatStatsComponent.class)
                    .setInvincibility(true);
        }
        MainArea.getInstance().getGameArea().getPlayer()
                .getComponent(CombatStatsComponent.class)
                .setInvincibility(false);
    }
}
