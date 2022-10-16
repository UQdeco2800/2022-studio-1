package com.deco2800.game.components.maingame;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.deco2800.game.areas.ForestGameArea;
import com.deco2800.game.areas.MainArea;
import com.deco2800.game.components.CombatStatsComponent;
import com.deco2800.game.components.player.InventoryComponent;
import com.deco2800.game.components.shop.ShopUtils;
import com.deco2800.game.components.shop.artefacts.Artefact;
import com.deco2800.game.components.shop.artefacts.ShopBuilding;
import com.deco2800.game.components.shop.equipments.Equipments;
import com.deco2800.game.concurrency.JobSystem;
import com.deco2800.game.entities.configs.EquipmentConfig;
import com.deco2800.game.entities.configs.ShopBuildingConfig;
import com.deco2800.game.files.FileLoader;
import com.deco2800.game.rendering.AnimationRenderComponent;
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
  private Group group;

  private int equipmentPos = 0;
  private Equipments currentEquipment;
  private Image currEquipment;
  private Image prevEquipment;
  private Image nextEquipment;
  private Texture currTexture;
  private Texture prevTexture;
  private Texture nextTexture;
  private TextButton description;

  private int buildingPos = 0;
  private ShopBuilding currentBuilding;
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

  private EquipmentConfig equipmentStats;
  private ShopBuildingConfig buildingStats;

  private int currEquipListSize = MainArea.getInstance().getGameArea().getPlayer()
      .getComponent(InventoryComponent.class).getEquipmentList().size();

  private ArrayList<ShopBuilding> currBuildingList;

  @Override
  public void create() {
    super.create();
    entity.getEvents().addListener("updateEquipment", this::updateEquipment);
    entity.getEvents().addListener("updateArtefact", this::updateArtefact);
    entity.getEvents().addListener("updateBuilding", this::updateBuilding);
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
    inventoryFrame.setPosition(Gdx.graphics.getWidth() / 2f - inventoryFrame.getWidth() / 2f,
        Gdx.graphics.getHeight() / 2f - inventoryFrame.getHeight() / 2f);

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
        ServiceLocator.getResourceService().getAsset("images/uiElements/exports/heart.png",
            Texture.class));
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
      currentEquipment = null;
    } else if (currEquipListSize == 1) {
      prevTexture = new Texture(Gdx.files.internal("images/shop-category-button.png"));
      nextTexture = new Texture(Gdx.files.internal("images/shop-category-button.png"));
      currentEquipment = MainArea.getInstance().getGameArea().getPlayer()
          .getComponent(InventoryComponent.class).getEquipmentList()
          .get(0);
      EquipmentConfig currData = FileLoader.readClass(EquipmentConfig.class,
          Equipments.getFilepath(currentEquipment));
      currTexture = new Texture(Gdx.files.internal(currData.itemBackgroundImagePath));
    } else if (currEquipListSize == 2) {
      prevTexture = new Texture(Gdx.files.internal("images/shop-category-button.png"));
      currentEquipment = MainArea.getInstance().getGameArea().getPlayer()
          .getComponent(InventoryComponent.class).getEquipmentList()
          .get(0);
      EquipmentConfig data = FileLoader.readClass(EquipmentConfig.class,
          Equipments.getFilepath(currentEquipment));
      currTexture = new Texture(Gdx.files.internal(data.itemBackgroundImagePath));
      data = FileLoader.readClass(EquipmentConfig.class,
          Equipments.getFilepath(MainArea.getInstance().getGameArea().getPlayer()
              .getComponent(InventoryComponent.class).getEquipmentList()
              .get(1)));
      nextTexture = new Texture(Gdx.files.internal(data.itemBackgroundImagePath));
    } else {
      currentEquipment = MainArea.getInstance().getGameArea().getPlayer()
          .getComponent(InventoryComponent.class).getEquipmentList()
          .get(0);
      EquipmentConfig data = FileLoader.readClass(EquipmentConfig.class,
          Equipments.getFilepath(currentEquipment));
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

    Texture leftSmallTitle1Texture = new Texture(Gdx.files.internal("images/shop-buy-button.png"));
    TextureRegionDrawable leftSmallTitle1Drawable = new TextureRegionDrawable(leftSmallTitle1Texture);
    TextButton leftSmallTitle1 = ShopUtils.createInventoryButton("Equip", skin.getColor("black"), "button",
        1f, leftSmallTitle1Drawable, leftSmallTitle1Drawable, skin, false);
    leftSmallTitle1.setSize(125f, 60f);
    leftSmallTitle1.setPosition(leftBox.getX() + 20f, leftBox.getY() + 320f);

    Texture leftSmallTitle2Texture = new Texture(Gdx.files.internal("images/shop-buy-button.png"));
    TextureRegionDrawable leftSmallTitle2Drawable = new TextureRegionDrawable(leftSmallTitle2Texture);
    TextButton leftSmallTitle2 = ShopUtils.createInventoryButton("Disarm", skin.getColor("black"), "button",
        1f, leftSmallTitle2Drawable, leftSmallTitle2Drawable, skin, false);
    leftSmallTitle2.setSize(125f, 60f);
    leftSmallTitle2.setPosition(leftBox.getX() + 175f, leftBox.getY() + 320f);

    equipmentStats = FileLoader.readClass(
        EquipmentConfig.class,
        Equipments.getFilepath(currentEquipment));
    Texture descriptionTexture = new Texture(Gdx.files.internal("images/description-box.png"));
    TextureRegionDrawable descriptionDrawable = new TextureRegionDrawable(descriptionTexture);
    description = ShopUtils.createImageTextButton(equipmentStats.name + "\n" + equipmentStats.description,
        skin.getColor("black"), "button",
        1f, descriptionDrawable, descriptionDrawable, skin, true);
    description.setSize((float) (description.getWidth() / 2.1), description.getHeight() / 5);
    description.setPosition(leftBox.getX() + 10f, leftBox.getY() + 250f);

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
    attackItem.setSize(40f, 40f);
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
    defenceItem.setSize(40f, 40f);
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
        .getComponent(InventoryComponent.class).getItems()
        .getOrDefault(Artefact.HEALTH_POTION, 0), skin,
        "large");
    potionQuantity.setColor(skin.getColor("black"));
    potionQuantity.setPosition(rightBox.getX() + 100f, rightBox.getY() + 405f);

    Texture potionUseTexture = new Texture(Gdx.files.internal("images/shop-buy-button.png"));
    TextureRegionDrawable potionUseDrawable = new TextureRegionDrawable(potionUseTexture);
    TextButton potionUseButton = ShopUtils.createInventoryButton("USE", skin.getColor("black"), "button",
        1f, potionUseDrawable, potionUseDrawable, skin, false);
    potionUseButton.setPosition(rightBox.getX() + 230f, rightBox.getY() + 365f);

    Texture clockTexture = new Texture(Gdx.files.internal("images/shop-clock.png"));
    TextureRegionDrawable clockDrawable = new TextureRegionDrawable(clockTexture);
    ImageButton clock = new ImageButton(clockDrawable, clockDrawable);
    clock.setSize(40f, 40f);
    clock.setPosition(rightBox.getX() + 50f, rightBox.getY() + 355f);

    clockQuantity = new Label("X" + MainArea.getInstance().getGameArea().getPlayer()
        .getComponent(InventoryComponent.class).getItems().getOrDefault(Artefact.CLOCK, 0),
        skin, "large");
    clockQuantity.setColor(skin.getColor("black"));
    clockQuantity.setPosition(rightBox.getX() + 100f, rightBox.getY() + 355f);

    Texture clockUseTexture = new Texture(Gdx.files.internal("images/shop-buy-button.png"));
    TextureRegionDrawable clockUseDrawable = new TextureRegionDrawable(clockUseTexture);
    TextButton clockUseButton = ShopUtils.createInventoryButton("USE", skin.getColor("black"), "button", 1f,
        clockUseDrawable, clockUseDrawable, skin, false);
    clockUseButton.setPosition(rightBox.getX() + 230f, rightBox.getY() + 315f);

    Texture bedTexture = new Texture(Gdx.files.internal("images/shop-bed.png"));
    TextureRegionDrawable bedDrawable = new TextureRegionDrawable(bedTexture);
    ImageButton bed = new ImageButton(bedDrawable, bedDrawable);
    bed.setSize(40f, 40f);
    bed.setPosition(rightBox.getX() + 50f, rightBox.getY() + 305f);

    bedQuantity = new Label("X" + MainArea.getInstance().getGameArea().getPlayer()
        .getComponent(InventoryComponent.class).getItems().getOrDefault(Artefact.BED, 0), skin,
        "large");
    bedQuantity.setColor(skin.getColor("black"));
    bedQuantity.setPosition(rightBox.getX() + 100f, rightBox.getY() + 305f);

    Texture bedUseTexture = new Texture(Gdx.files.internal("images/shop-buy-button.png"));
    TextureRegionDrawable bedUseDrawable = new TextureRegionDrawable(bedUseTexture);
    TextButton bedUseButton = ShopUtils.createInventoryButton("USE", skin.getColor("black"), "button", 1f,
        bedUseDrawable, bedUseDrawable, skin, false);
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
      currentBuilding = currBuildingList.get(0);
      ShopBuildingConfig data = FileLoader.readClass(ShopBuildingConfig.class,
          ShopBuilding.getFilepath(currentBuilding));
      currBuildingTexture = new Texture(Gdx.files.internal(data.itemBackgroundImagePath));
    } else if (currBuildingList.size() == 2) {
      prevBuildingTexture = new Texture(Gdx.files.internal("images/shop-category-button.png"));
      currentBuilding = currBuildingList.get(0);
      ShopBuildingConfig data = FileLoader.readClass(ShopBuildingConfig.class,
          ShopBuilding.getFilepath(currentBuilding));
      currBuildingTexture = new Texture(Gdx.files.internal(data.itemBackgroundImagePath));

      data = FileLoader.readClass(ShopBuildingConfig.class,
          ShopBuilding.getFilepath(currBuildingList.get(1)));
      nextBuildingTexture = new Texture(Gdx.files.internal(data.itemBackgroundImagePath));
    } else {
      currentBuilding = currBuildingList.get(0);
      ShopBuildingConfig data = FileLoader.readClass(ShopBuildingConfig.class,
          ShopBuilding.getFilepath(currentBuilding));
      currBuildingTexture = new Texture(Gdx.files.internal(data.itemBackgroundImagePath));

      data = FileLoader.readClass(ShopBuildingConfig.class,
          ShopBuilding.getFilepath(currBuildingList.get(1)));
      nextBuildingTexture = new Texture(Gdx.files.internal(data.itemBackgroundImagePath));

      data = FileLoader.readClass(ShopBuildingConfig.class,
          ShopBuilding.getFilepath(currBuildingList.get(currBuildingList.size() - 1)));
      prevBuildingTexture = new Texture(Gdx.files.internal(data.itemBackgroundImagePath));
    }
    prevBuilding = new Image(prevBuildingTexture);
    prevBuilding.setSize(40f, 40f);
    prevBuilding.setPosition(rightBox.getX() + 105f, rightBox.getY() + 205f);

    currBuilding = new Image(currBuildingTexture);
    currBuilding.setSize(50f, 50f);
    currBuilding.setPosition(rightBox.getX() + 150f, rightBox.getY() + 205f);

    nextBuilding = new Image(nextBuildingTexture);
    nextBuilding.setSize(40f, 40f);
    nextBuilding.setPosition(rightBox.getX() + 205f, rightBox.getY() + 205f);

    Texture buildingRightArrowTexture = new Texture(Gdx.files.internal("images/right_arrow.png"));
    TextureRegionDrawable buildingRightArrowDrawable = new TextureRegionDrawable(buildingRightArrowTexture);
    ImageButton buildingRightArrow = new ImageButton(buildingRightArrowDrawable,
        buildingRightArrowDrawable);
    buildingRightArrow.setSize(40f, 40f);
    buildingRightArrow.setPosition(rightBox.getX() + 245f, rightBox.getY() + 205f);

    Texture placeButtonTexture = new Texture(Gdx.files.internal("images/shop-buy-button.png"));
    TextureRegionDrawable placeButtonDrawable = new TextureRegionDrawable(placeButtonTexture);
    TextButton placeButton = ShopUtils.createInventoryButton("Place", skin.getColor("black"), "button", 1f,
        placeButtonDrawable, placeButtonDrawable, skin, false);
    placeButton.setPosition(rightBox.getX() + 120f, rightBox.getY() + 135f);

    // Entering the Shop Button
    Texture shopTexture = new Texture(Gdx.files.internal("images/Shop.png"));
    Texture shopTextureOnClick = new Texture(Gdx.files.internal("images/shop_onClick.png"));
    TextureRegionDrawable upShop = new TextureRegionDrawable(shopTexture);
    TextureRegionDrawable downShop = new TextureRegionDrawable(shopTexture);
    TextureRegionDrawable shopChecked = new TextureRegionDrawable(shopTextureOnClick);
    ImageButton shopButton = new ImageButton(upShop, downShop, shopChecked);

    shopButton.addListener(
        new InputListener() {
          @Override
          public void enter(InputEvent event, float x, float y, int pointer, Actor actor) {
            shopButton.setChecked(true);
          }

          @Override
          public void exit(InputEvent event, float x, float y, int pointer, Actor actor) {
            shopButton.setChecked(false);
          }
        });

    // Entering the Inventory Button -- need to add the inventory button
    Texture inventoryTexture = new Texture(Gdx.files.internal("images/inventory.png"));
    Texture inventoryOnClickTexture = new Texture(Gdx.files.internal("images/inventory_OnClick.png"));
    TextureRegionDrawable downInventory = new TextureRegionDrawable(inventoryTexture);
    TextureRegionDrawable upInventory = new TextureRegionDrawable(inventoryTexture);
    TextureRegionDrawable inventoryChecked = new TextureRegionDrawable(inventoryOnClickTexture);
    ImageButton inventoryButton = new ImageButton(upInventory, downInventory, inventoryChecked);

    inventoryButton.addListener(
        new InputListener() {
          @Override
          public void enter(InputEvent event, float x, float y, int pointer, Actor actor) {
            inventoryButton.setChecked(true);
          }

          @Override
          public void exit(InputEvent event, float x, float y, int pointer, Actor actor) {
            inventoryButton.setChecked(false);
          }
        });

    // the achievements button
    Texture achievementsTexture = new Texture(Gdx.files.internal("images/achievemnets_onclick.png"));
    Texture achievementsTextureOnClick = new Texture(Gdx.files.internal("images/Achievements.png"));
    TextureRegionDrawable upAchievements = new TextureRegionDrawable(achievementsTexture);
    TextureRegionDrawable downAchievements = new TextureRegionDrawable(achievementsTexture);
    TextureRegionDrawable achievementsChecked = new TextureRegionDrawable(achievementsTextureOnClick);
    ImageButton achievementsButton = new ImageButton(upAchievements, downAchievements, achievementsChecked);

    // Adds hover state to achievements
    achievementsButton.addListener(
        new InputListener() {
          @Override
          public void enter(InputEvent event, float x, float y, int pointer, Actor actor) {
            achievementsButton.setChecked(true);
          }

          @Override
          public void exit(InputEvent event, float x, float y, int pointer, Actor actor) {
            achievementsButton.setChecked(false);
          }
        });

    // Guidebook button
    Texture guideBookTexture = new Texture(Gdx.files.internal("images/guidebook.png"));
    Texture guideBookTextureCheck = new Texture(Gdx.files.internal("images/guideBookCheck.png"));
    TextureRegionDrawable upGuidebook = new TextureRegionDrawable(guideBookTexture);
    TextureRegionDrawable downGuidebook = new TextureRegionDrawable(guideBookTexture);
    TextureRegionDrawable guidebookCheck = new TextureRegionDrawable(guideBookTextureCheck);
    ImageButton guideBookButton = new ImageButton(upGuidebook, downGuidebook, guidebookCheck);

    // Adds hover state to achievements
    guideBookButton.addListener(
        new InputListener() {
          @Override
          public void enter(InputEvent event, float x, float y, int pointer, Actor actor) {
            guideBookButton.setChecked(true);
          }

          @Override
          public void exit(InputEvent event, float x, float y, int pointer, Actor actor) {
            guideBookButton.setChecked(false);
          }
        });

    // trigger for guidebook
    guideBookButton.addListener(new ClickListener() {
      @Override
      public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
        logger.debug("Guidebook button clicked");
        entity.getEvents().trigger("guideBook");
        return true;
      }
    });

    // Triggers an event when the button is pressed.
    inventoryButton.addListener(
        new ClickListener() {
          @Override
          public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
            logger.debug("Inventory button clicked");
            entity.getEvents().trigger("closeAll");
            group.setVisible(true);
            return true;
          }
        });

    // Trigger for an achievement
    achievementsButton.addListener(
        new ClickListener() {
          @Override
          public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
            logger.debug("Achievement button clicked");
            entity.getEvents().trigger("closeAll");
            entity.getEvents().trigger("achievement");
            return true;
          }
        });

    // trigger for shop button
    shopButton.addListener(
        new ClickListener() {
          @Override
          public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
            logger.debug("Shop button clicked");
            group.setVisible(false);
            entity.getEvents().trigger("closeAll");
            entity.getEvents().trigger("shop");
            ((ForestGameArea) MainArea.getInstance().getGameArea()).playShopMusic();
            return true;
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

    leftSmallTitle1.addListener(
        new ChangeListener() {
          @Override
          public void changed(ChangeEvent changeEvent, Actor actor) {
            logger.debug("Equip button clicked");

            if (currentEquipment != null) {
              EquipmentConfig equipmentStats = FileLoader.readClass(
                  EquipmentConfig.class,
                  Equipments.getFilepath(currentEquipment));
              if (equipmentStats.type.equals("weapon")) {
                MainArea.getInstance().getGameArea().getPlayer().getComponent(InventoryComponent.class)
                    .setWeapon(currentEquipment);
                MainArea.getInstance().getGameArea().getPlayer().getComponent(CombatStatsComponent.class)
                    .setAttackMultiplier(equipmentStats.attack);
                System.out.println("WEAPON equipped");
                MainArea.getInstance().getGameArea().getPlayer().getComponent(AnimationRenderComponent.class)
                    .startAnimation(Equipments.getAnimationName(currentEquipment));
                attackItem.setDrawable(
                    new TextureRegionDrawable(new Texture(Gdx.files.internal(equipmentStats.itemBackgroundImagePath))));
              } else {
                MainArea.getInstance().getGameArea().getPlayer().getComponent(InventoryComponent.class)
                    .setArmor(currentEquipment);
                MainArea.getInstance().getGameArea().getPlayer().getComponent(CombatStatsComponent.class)
                    .setBaseDefense(equipmentStats.attack);
                System.out.println("ARMOR equipped");
                defenceItem.setDrawable(
                    new TextureRegionDrawable(new Texture(Gdx.files.internal(equipmentStats.itemBackgroundImagePath))));
              }
            }
          }
        });

    leftSmallTitle2.addListener(
        new ChangeListener() {
          @Override
          public void changed(ChangeEvent changeEvent, Actor actor) {
            logger.debug("Disarm button clicked");
            EquipmentConfig currentStats = FileLoader.readClass(
                EquipmentConfig.class,
                Equipments.getFilepath(currentEquipment));
            EquipmentConfig equipmentStats = FileLoader.readClass(
                EquipmentConfig.class,
                Equipments.getFilepath(Equipments.AXE));
            if (currentStats.type.equals("weapon")) {
              MainArea.getInstance().getGameArea().getPlayer().getComponent(InventoryComponent.class)
                  .setWeapon(Equipments.AXE);
              MainArea.getInstance().getGameArea().getPlayer().getComponent(CombatStatsComponent.class)
                  .setAttackMultiplier(equipmentStats.attack);
              System.out.println("WEAPON disarmed");
              MainArea.getInstance().getGameArea().getPlayer().getComponent(AnimationRenderComponent.class)
                  .startAnimation(Equipments.getAnimationName(Equipments.AXE));
              attackItem.setDrawable(
                  new TextureRegionDrawable(new Texture(Gdx.files.internal(equipmentStats.itemBackgroundImagePath))));
            } else {
              MainArea.getInstance().getGameArea().getPlayer().getComponent(InventoryComponent.class)
                  .setArmor(null);
              MainArea.getInstance().getGameArea().getPlayer().getComponent(CombatStatsComponent.class)
                  .setBaseDefense(0);
              System.out.println("ARMOR disarmed");
              defenceItem.setDrawable(
                  new TextureRegionDrawable(new Texture(Gdx.files.internal("images/shop-category-button.png"))));
            }
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
                currentEquipment = MainArea.getInstance().getGameArea().getPlayer()
                    .getComponent(InventoryComponent.class).getEquipmentList()
                    .get(0);
                equipmentStats = FileLoader.readClass(
                    EquipmentConfig.class,
                    Equipments.getFilepath(currentEquipment));
                description.setText(equipmentStats.name + "\n" + equipmentStats.description);
                currEquipment.setDrawable(new TextureRegionDrawable(
                    new Texture(Gdx.files.internal(
                        equipmentStats.itemBackgroundImagePath))));

                equipmentStats = FileLoader.readClass(
                    EquipmentConfig.class,
                    Equipments.getFilepath(MainArea
                        .getInstance()
                        .getGameArea()
                        .getPlayer()
                        .getComponent(InventoryComponent.class)
                        .getEquipmentList()
                        .get(1)));

                nextEquipment.setDrawable(new TextureRegionDrawable(
                    new Texture(Gdx.files.internal(
                        equipmentStats.itemBackgroundImagePath))));

                prevEquipment.setDrawable(new TextureRegionDrawable(
                    new Texture(Gdx.files.internal(
                        "images/shop-category-button.png"))));
              }
              sideEquipment();
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
                currentEquipment = MainArea
                    .getInstance()
                    .getGameArea()
                    .getPlayer()
                    .getComponent(InventoryComponent.class)
                    .getEquipmentList()
                    .get(1);
                equipmentStats = FileLoader.readClass(
                    EquipmentConfig.class,
                    Equipments.getFilepath(currentEquipment));
                description.setText(equipmentStats.name + "\n" + equipmentStats.description);
                currEquipment.setDrawable(new TextureRegionDrawable(
                    new Texture(Gdx.files.internal(
                        equipmentStats.itemBackgroundImagePath))));

                equipmentStats = FileLoader.readClass(
                    EquipmentConfig.class,
                    Equipments.getFilepath(MainArea
                        .getInstance()
                        .getGameArea()
                        .getPlayer()
                        .getComponent(InventoryComponent.class)
                        .getEquipmentList()
                        .get(0)));
                prevEquipment.setDrawable(new TextureRegionDrawable(
                    new Texture(Gdx.files.internal(
                        equipmentStats.itemBackgroundImagePath))));

                nextEquipment.setDrawable(new TextureRegionDrawable(
                    new Texture(Gdx.files.internal(
                        "images/shop-category-button.png"))));
              }
              sideEquipment();
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
                  .setHealth(MainArea.getInstance().getGameArea()
                      .getPlayer()
                      .getComponent(CombatStatsComponent.class)
                      .getMaxHealth());
              potionQuantity.setText("X" + MainArea.getInstance()
                  .getGameArea().getPlayer()
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
              clockQuantity.setText("X" + MainArea.getInstance().getGameArea()
                  .getPlayer()
                  .getComponent(InventoryComponent.class)
                  .getItems()
                  .get(Artefact.CLOCK));

              JobSystem.launch(() -> {
                try {
                  clockAbility(ServiceLocator.getTimeSource()
                      .getTime());
                } catch (InterruptedException e) {
                  logger.error(e.getMessage());
                  Thread.currentThread().interrupt();
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
              bedQuantity.setText("X" + MainArea.getInstance().getGameArea()
                  .getPlayer()
                  .getComponent(InventoryComponent.class)
                  .getItems()
                  .get(Artefact.BED));

              JobSystem.launch(() -> {
                try {
                  bedAbility(ServiceLocator.getTimeSource()
                      .getTime(),
                      MainArea.getInstance()
                          .getGameArea()
                          .getPlayer()
                          .getComponent(CombatStatsComponent.class)
                          .getHealth());
                } catch (InterruptedException e) {
                  logger.error(e.getMessage());
                  Thread.currentThread().interrupt();
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
                currentBuilding = currBuildingList.get(0);
                buildingStats = FileLoader.readClass(
                    ShopBuildingConfig.class,
                    ShopBuilding.getFilepath(
                        currentBuilding));
                currBuilding.setDrawable(new TextureRegionDrawable(
                    new Texture(Gdx.files.internal(
                        buildingStats.itemBackgroundImagePath))));

                buildingStats = FileLoader.readClass(
                    ShopBuildingConfig.class,
                    ShopBuilding.getFilepath(
                        currBuildingList
                            .get(1)));
                nextBuilding.setDrawable(new TextureRegionDrawable(
                    new Texture(Gdx.files.internal(
                        buildingStats.itemBackgroundImagePath))));

                prevBuilding.setDrawable(new TextureRegionDrawable(
                    new Texture(Gdx.files.internal(
                        "images/shop-category-button.png"))));
              }
              sideBuildings();
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
                currentBuilding = currBuildingList.get(1);
                buildingStats = FileLoader.readClass(
                    ShopBuildingConfig.class,
                    ShopBuilding.getFilepath(currentBuilding));
                currBuilding.setDrawable(new TextureRegionDrawable(
                    new Texture(Gdx.files.internal(
                        buildingStats.itemBackgroundImagePath))));

                buildingStats = FileLoader.readClass(
                    ShopBuildingConfig.class,
                    ShopBuilding.getFilepath(
                        currBuildingList
                            .get(0)));
                prevBuilding.setDrawable(new TextureRegionDrawable(
                    new Texture(Gdx.files.internal(
                        buildingStats.itemBackgroundImagePath))));

                nextBuilding.setDrawable(new TextureRegionDrawable(
                    new Texture(Gdx.files.internal(
                        "images/shop-category-button.png"))));
              }
              sideBuildings();
            }
          }
        });

    placeButton.addListener(
        new ChangeListener() {
          @Override
          public void changed(ChangeEvent changeEvent, Actor actor) {
            if (currentBuilding != null) {
              logger.debug("Placing Building");
              buildingStats = FileLoader.readClass(
                  ShopBuildingConfig.class,
                  ShopBuilding.getFilepath(currentBuilding));
              String[] names = buildingStats.name.split("[\\W_]+");
              String buildingCamelName = "";
              for (int i = 0; i < names.length; i++) {
                String name = names[i];
                if (i == 0) {
                  name = name.isEmpty() ? name : name.toLowerCase();
                } else {
                  name = name.isEmpty() ? name
                      : Character.toUpperCase(name.charAt(0)) + name.substring(1).toLowerCase();
                }
                buildingCamelName += name;
              }

              logger.debug(buildingCamelName);
              MainArea.getInstance()
                  .getGameArea().getPlayer()
                  .getComponent(InventoryComponent.class)
                  .removeBuilding(currentBuilding);

              group.setVisible(false);
              ServiceLocator.getStructureService().buildTempStructure(buildingCamelName);
              currBuildingList = getBuildingList();

              if (currBuildingList.size() == 0) {
                prevBuildingTexture = new Texture(Gdx.files.internal("images/shop-category-button.png"));
                currBuildingTexture = new Texture(Gdx.files.internal("images/shop-category-button.png"));
                nextBuildingTexture = new Texture(Gdx.files.internal("images/shop-category-button.png"));
                currentBuilding = null;
              } else if (currBuildingList.size() == 1) {
                prevBuildingTexture = new Texture(Gdx.files.internal("images/shop-category-button.png"));

                nextBuildingTexture = new Texture(Gdx.files.internal("images/shop-category-button.png"));
                currentBuilding = currBuildingList.get(0);
                ShopBuildingConfig data = FileLoader.readClass(ShopBuildingConfig.class,
                    ShopBuilding.getFilepath(currentBuilding));
                currBuildingTexture = new Texture(Gdx.files.internal(data.itemBackgroundImagePath));
              } else if (currBuildingList.size() == 2) {
                prevBuildingTexture = new Texture(Gdx.files.internal("images/shop-category-button.png"));
                currentBuilding = currBuildingList.get(0);
                ShopBuildingConfig data = FileLoader.readClass(ShopBuildingConfig.class,
                    ShopBuilding.getFilepath(currentBuilding));
                currBuildingTexture = new Texture(Gdx.files.internal(data.itemBackgroundImagePath));

                data = FileLoader.readClass(ShopBuildingConfig.class,
                    ShopBuilding.getFilepath(currBuildingList.get(1)));
                nextBuildingTexture = new Texture(Gdx.files.internal(data.itemBackgroundImagePath));
              } else {
                currentBuilding = currBuildingList.get(0);
                ShopBuildingConfig data = FileLoader.readClass(ShopBuildingConfig.class,
                    ShopBuilding.getFilepath(currentBuilding));
                currBuildingTexture = new Texture(Gdx.files.internal(data.itemBackgroundImagePath));

                data = FileLoader.readClass(ShopBuildingConfig.class,
                    ShopBuilding.getFilepath(currBuildingList.get(1)));
                nextBuildingTexture = new Texture(Gdx.files.internal(data.itemBackgroundImagePath));

                data = FileLoader.readClass(ShopBuildingConfig.class,
                    ShopBuilding.getFilepath(currBuildingList.get(currBuildingList.size() - 1)));
                prevBuildingTexture = new Texture(Gdx.files.internal(data.itemBackgroundImagePath));
              }
              prevBuilding.setDrawable(new TextureRegionDrawable(prevBuildingTexture));

              currBuilding.setDrawable(new TextureRegionDrawable(currBuildingTexture));

              nextBuilding.setDrawable(new TextureRegionDrawable(nextBuildingTexture));
            } else {
              logger.debug("No building to place!");
            }
          }
        });

    rightSideTable.add(guideBookButton).right().bottom().size(100f, 100f);

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
    group.addActor(crossFrame);
    group.setVisible(false);

    stage.addActor(leftSideTable);
    stage.addActor(rightSideTable);
    stage.addActor(group);
  }

  private ArrayList<ShopBuilding> getBuildingList() {
    ArrayList<ShopBuilding> buildings = new ArrayList<>();
    for (Map.Entry<ShopBuilding, Integer> building : MainArea.getInstance()
        .getGameArea().getPlayer()
        .getComponent(InventoryComponent.class)
        .getBuildings().entrySet()) {
      if (building.getValue() > 0) {
        buildings.add(building.getKey());
      }
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

    while (ServiceLocator.getTimeSource().getTimeSince((long) startingTime) != 15000) {
      MainArea.getInstance().getGameArea().getPlayer().getComponent(CombatStatsComponent.class)
          .setHealth(currentHealth +
              Math.round(ServiceLocator.getTimeSource()
                  .getTimeSince((long) startingTime / 15000
                      * 45)));
    }
  }

  public void clockAbility(float startingTime) throws InterruptedException {
    while (ServiceLocator.getTimeSource().getTimeSince((long) startingTime) != 5000) {
      MainArea.getInstance().getGameArea().getPlayer()
          .getComponent(CombatStatsComponent.class)
          .setInvincibility(true);
    }
    MainArea.getInstance().getGameArea().getPlayer()
        .getComponent(CombatStatsComponent.class)
        .setInvincibility(false);
  }

  private void sideEquipment() {
    if (currEquipListSize >= 3) {
      currentEquipment = MainArea
          .getInstance()
          .getGameArea()
          .getPlayer()
          .getComponent(InventoryComponent.class)
          .getEquipmentList()
          .get(equipmentPos);
      equipmentStats = FileLoader.readClass(
          EquipmentConfig.class,
          Equipments.getFilepath(currentEquipment));
      description.setText(equipmentStats.name + "\n" + equipmentStats.description);
      currEquipment.setDrawable(new TextureRegionDrawable(
          new Texture(Gdx.files.internal(
              equipmentStats.itemBackgroundImagePath))));

      if (equipmentPos + 1 > currEquipListSize - 1) {
        equipmentStats = FileLoader.readClass(
            EquipmentConfig.class,
            Equipments.getFilepath(MainArea
                .getInstance()
                .getGameArea()
                .getPlayer()
                .getComponent(InventoryComponent.class)
                .getEquipmentList()
                .get(0)));
      } else {
        equipmentStats = FileLoader.readClass(
            EquipmentConfig.class,
            Equipments.getFilepath(MainArea
                .getInstance()
                .getGameArea()
                .getPlayer()
                .getComponent(InventoryComponent.class)
                .getEquipmentList()
                .get(equipmentPos
                    + 1)));
      }

      nextEquipment.setDrawable(new TextureRegionDrawable(
          new Texture(Gdx.files.internal(
              equipmentStats.itemBackgroundImagePath))));

      if (equipmentPos - 1 < 0) {
        equipmentStats = FileLoader.readClass(
            EquipmentConfig.class,
            Equipments.getFilepath(MainArea
                .getInstance()
                .getGameArea()
                .getPlayer()
                .getComponent(InventoryComponent.class)
                .getEquipmentList()
                .get(currEquipListSize
                    - 1)));
      } else {
        equipmentStats = FileLoader.readClass(
            EquipmentConfig.class,
            Equipments.getFilepath(MainArea
                .getInstance()
                .getGameArea()
                .getPlayer()
                .getComponent(InventoryComponent.class)
                .getEquipmentList()
                .get(equipmentPos
                    - 1)));
      }

      prevEquipment.setDrawable(new TextureRegionDrawable(
          new Texture(Gdx.files.internal(
              equipmentStats.itemBackgroundImagePath))));
    }
  }

  private void sideBuildings() {
    if (currBuildingList.size() >= 3) {
      currentBuilding = currBuildingList.get(buildingPos);
      buildingStats = FileLoader.readClass(
          ShopBuildingConfig.class,
          ShopBuilding.getFilepath(currentBuilding));
      currBuilding.setDrawable(new TextureRegionDrawable(
          new Texture(Gdx.files.internal(
              buildingStats.itemBackgroundImagePath))));

      if (buildingPos + 1 > currBuildingList.size() - 1) {
        buildingStats = FileLoader.readClass(
            ShopBuildingConfig.class,
            ShopBuilding.getFilepath(
                currBuildingList
                    .get(0)));
      } else {
        buildingStats = FileLoader.readClass(
            ShopBuildingConfig.class,
            ShopBuilding.getFilepath(
                currBuildingList
                    .get(buildingPos + 1)));
      }
      nextBuilding.setDrawable(new TextureRegionDrawable(
          new Texture(Gdx.files.internal(
              buildingStats.itemBackgroundImagePath))));

      if (buildingPos - 1 < 0) {
        buildingStats = FileLoader.readClass(
            ShopBuildingConfig.class,
            ShopBuilding.getFilepath(
                currBuildingList
                    .get(currBuildingList
                        .size()
                        - 1)));
      } else {
        buildingStats = FileLoader.readClass(
            ShopBuildingConfig.class,
            ShopBuilding.getFilepath(
                currBuildingList
                    .get(buildingPos - 1)));
      }
      prevBuilding.setDrawable(new TextureRegionDrawable(
          new Texture(Gdx.files.internal(
              buildingStats.itemBackgroundImagePath))));

    }
  }

  private void updateBuilding() {
    currBuildingList = getBuildingList();
    buildingPos = 0;

    if (currBuildingList.size() == 1) {
      prevBuildingTexture = new Texture(Gdx.files.internal("images/shop-category-button.png"));

      nextBuildingTexture = new Texture(Gdx.files.internal("images/shop-category-button.png"));
      currentBuilding = currBuildingList.get(0);
      ShopBuildingConfig data = FileLoader.readClass(ShopBuildingConfig.class,
          ShopBuilding.getFilepath(currentBuilding));
      currBuildingTexture = new Texture(Gdx.files.internal(data.itemBackgroundImagePath));
    } else if (currBuildingList.size() == 2) {
      prevBuildingTexture = new Texture(Gdx.files.internal("images/shop-category-button.png"));
      currentBuilding = currBuildingList.get(0);
      ShopBuildingConfig data = FileLoader.readClass(ShopBuildingConfig.class,
          ShopBuilding.getFilepath(currentBuilding));
      currBuildingTexture = new Texture(Gdx.files.internal(data.itemBackgroundImagePath));

      data = FileLoader.readClass(ShopBuildingConfig.class,
          ShopBuilding.getFilepath(currBuildingList.get(1)));
      nextBuildingTexture = new Texture(Gdx.files.internal(data.itemBackgroundImagePath));
    } else {
      currentBuilding = currBuildingList.get(0);
      ShopBuildingConfig data = FileLoader.readClass(ShopBuildingConfig.class,
          ShopBuilding.getFilepath(currentBuilding));
      currBuildingTexture = new Texture(Gdx.files.internal(data.itemBackgroundImagePath));

      data = FileLoader.readClass(ShopBuildingConfig.class,
          ShopBuilding.getFilepath(currBuildingList.get(1)));
      nextBuildingTexture = new Texture(Gdx.files.internal(data.itemBackgroundImagePath));

      data = FileLoader.readClass(ShopBuildingConfig.class,
          ShopBuilding.getFilepath(currBuildingList.get(currBuildingList.size() - 1)));
      prevBuildingTexture = new Texture(Gdx.files.internal(data.itemBackgroundImagePath));
    }

    prevBuilding.setDrawable(new TextureRegionDrawable(prevBuildingTexture));

    currBuilding.setDrawable(new TextureRegionDrawable(currBuildingTexture));

    nextBuilding.setDrawable(new TextureRegionDrawable(nextBuildingTexture));
  }

  private void updateArtefact() {
    clockQuantity.setText("X" + MainArea.getInstance().getGameArea()
        .getPlayer()
        .getComponent(InventoryComponent.class)
        .getItems()
        .getOrDefault(Artefact.CLOCK, 0));
    potionQuantity.setText("X" + MainArea.getInstance().getGameArea()
        .getPlayer()
        .getComponent(InventoryComponent.class)
        .getItems()
        .getOrDefault(Artefact.HEALTH_POTION, 0));
    bedQuantity.setText("X" + MainArea.getInstance().getGameArea()
        .getPlayer()
        .getComponent(InventoryComponent.class)
        .getItems()
        .getOrDefault(Artefact.BED, 0));
  }

  private void updateEquipment() {
    equipmentPos = 0;
    currEquipListSize = MainArea.getInstance().getGameArea().getPlayer()
        .getComponent(InventoryComponent.class).getEquipmentList().size();

    if (currEquipListSize == 1) {
      prevTexture = new Texture(Gdx.files.internal("images/shop-category-button.png"));
      nextTexture = new Texture(Gdx.files.internal("images/shop-category-button.png"));
      currentEquipment = MainArea.getInstance().getGameArea().getPlayer()
          .getComponent(InventoryComponent.class).getEquipmentList()
          .get(0);
      EquipmentConfig currData = FileLoader.readClass(EquipmentConfig.class,
          Equipments.getFilepath(currentEquipment));
      currTexture = new Texture(Gdx.files.internal(currData.itemBackgroundImagePath));
    } else if (currEquipListSize == 2) {
      prevTexture = new Texture(Gdx.files.internal("images/shop-category-button.png"));

      currentEquipment = MainArea.getInstance().getGameArea().getPlayer()
          .getComponent(InventoryComponent.class).getEquipmentList()
          .get(0);
      EquipmentConfig data = FileLoader.readClass(EquipmentConfig.class,
          Equipments.getFilepath(currentEquipment));
      currTexture = new Texture(Gdx.files.internal(data.itemBackgroundImagePath));
      data = FileLoader.readClass(EquipmentConfig.class,
          Equipments.getFilepath(MainArea.getInstance().getGameArea().getPlayer()
              .getComponent(InventoryComponent.class).getEquipmentList()
              .get(1)));
      nextTexture = new Texture(Gdx.files.internal(data.itemBackgroundImagePath));
    } else {
      currentEquipment = MainArea.getInstance().getGameArea().getPlayer()
          .getComponent(InventoryComponent.class).getEquipmentList()
          .get(0);
      EquipmentConfig data = FileLoader.readClass(EquipmentConfig.class,
          Equipments.getFilepath(currentEquipment));
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
    prevEquipment.setDrawable(new TextureRegionDrawable(prevTexture));

    currEquipment.setDrawable(new TextureRegionDrawable(currTexture));

    nextEquipment.setDrawable(new TextureRegionDrawable(nextTexture));

    equipmentStats = FileLoader.readClass(
        EquipmentConfig.class,
        Equipments.getFilepath(currentEquipment));
    description.setText(equipmentStats.name + "\n" + equipmentStats.description);

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
    defenceItem.setDrawable(new TextureRegionDrawable(defenceItemTexture));

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
    attackItem.setDrawable(new TextureRegionDrawable(attackItemTexture));
  }

}
