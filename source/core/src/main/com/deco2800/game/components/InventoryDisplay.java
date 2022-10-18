package com.deco2800.game.components;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.deco2800.game.areas.ForestGameArea;
import com.deco2800.game.areas.MainArea;
import com.deco2800.game.components.player.InventoryComponent;
import com.deco2800.game.components.shop.ShopUtils;
import com.deco2800.game.components.shop.artefacts.Artefact;
import com.deco2800.game.components.shop.equipments.Equipments;
import com.deco2800.game.concurrency.JobSystem;
import com.deco2800.game.entities.configs.EquipmentConfig;
import com.deco2800.game.files.FileLoader;
import com.deco2800.game.rendering.AnimationRenderComponent;
import com.deco2800.game.services.ServiceLocator;
import com.deco2800.game.ui.UIComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class InventoryDisplay extends UIComponent {
        private static final Logger logger = LoggerFactory.getLogger(InventoryDisplay.class);
        private static final float Z_INDEX = 2f;
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

        private Image attackItem;
        private Image defenceItem;

        private Label potionQuantity;
        private Label clockQuantity;
        private Label bedQuantity;

        private EquipmentConfig equipmentStats;

        private int currEquipListSize = MainArea.getInstance().getGameArea().getPlayer()
                        .getComponent(InventoryComponent.class).getEquipmentList().size();

        @Override
        public void create() {
                super.create();
                entity.getEvents().addListener("updateEquipment", this::updateEquipment);
                entity.getEvents().addListener("updateArtefact", this::updateArtefact);
                entity.getEvents().addListener("inventory", this::openInventory);
                entity.getEvents().addListener("closeAll", this::closeInventory);
                addActors();

        }

        private void addActors() {

                group = new Group();

                Texture inventoryInterfaceTexture = new Texture(Gdx.files.internal("images/popup-border.png"));
                TextureRegionDrawable inventory = new TextureRegionDrawable(inventoryInterfaceTexture);
                ImageButton inventoryFrame = new ImageButton(inventory, inventory);
                inventoryFrame.setSize(800f, 800f);
                inventoryFrame.setPosition(Gdx.graphics.getWidth() / 2f - inventoryFrame.getWidth() / 2f,
                                Gdx.graphics.getHeight() / 2f - inventoryFrame.getHeight() / 2f);

                Texture crossTexture = new Texture(Gdx.files.internal("images/backButton.png"));
                TextureRegionDrawable cross = new TextureRegionDrawable(crossTexture);
                ImageButton crossFrame = new ImageButton(cross, cross);
                crossFrame.setSize(40f, 40f);
                crossFrame.setPosition(inventoryFrame.getX() + 700f, inventoryFrame.getY() + 610f);

                Label subtitle = new Label("Inventory", skin, "title");
                subtitle.setFontScale(1.5f);
                subtitle.setColor(skin.getColor("black"));
                subtitle.setPosition(inventoryFrame.getX() + 250f, inventoryFrame.getY() + 610f);

                Texture leftTexture = new Texture(Gdx.files.internal("images/box-border.png"));
                TextureRegionDrawable leftDrawable = new TextureRegionDrawable(leftTexture);
                ImageButton leftBox = new ImageButton(leftDrawable, leftDrawable);
                leftBox.setSize(350f, 650f);
                leftBox.setPosition(inventoryFrame.getX() + 60f, inventoryFrame.getY() + 50f);

                Label leftTitle = new Label("Equipment", skin, "small");
                leftTitle.setFontScale(1.5f);
                leftTitle.setColor(skin.getColor("white"));
                leftTitle.setPosition(leftBox.getX() + 105f, leftBox.getY() + 480f);

                Texture leftArrowTexture = new Texture(Gdx.files.internal("images/left_arrow.png"));
                TextureRegionDrawable leftArrowDrawable = new TextureRegionDrawable(leftArrowTexture);
                ImageButton leftArrow = new ImageButton(leftArrowDrawable, leftArrowDrawable);
                leftArrow.setSize(40f, 40f);
                leftArrow.setPosition(leftBox.getX() + 20f, leftBox.getY() + 390f);

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
                prevEquipment.setSize(70f, 70f);
                prevEquipment.setPosition(leftBox.getX() + 70f, leftBox.getY() + 380f);

                currEquipment = new Image(currTexture);
                currEquipment.setSize(80f, 80f);
                currEquipment.setPosition(leftBox.getX() + 130f, leftBox.getY() + 380f);

                nextEquipment = new Image(nextTexture);
                nextEquipment.setSize(70f, 70f);
                nextEquipment.setPosition(leftBox.getX() + 198f, leftBox.getY() + 380f);

                Texture rightArrowTexture = new Texture(Gdx.files.internal("images/right_arrow.png"));
                TextureRegionDrawable rightArrowDrawable = new TextureRegionDrawable(rightArrowTexture);
                ImageButton rightArrow = new ImageButton(rightArrowDrawable, rightArrowDrawable);
                rightArrow.setSize(40f, 40f);
                rightArrow.setPosition(leftBox.getX() + 280f, leftBox.getY() + 390f);

                Texture leftSmallTitle1Texture = new Texture(Gdx.files.internal("images/Home_Button.png"));
                TextureRegionDrawable leftSmallTitle1Drawable = new TextureRegionDrawable(leftSmallTitle1Texture);
                TextButton leftSmallTitle1 = ShopUtils.createInventoryButton("Equip", skin.getColor("black"), "button",
                                1f, leftSmallTitle1Drawable, leftSmallTitle1Drawable, skin, false);
                leftSmallTitle1.setSize(115f, 35f);
                leftSmallTitle1.setPosition(leftBox.getX() + 50f, leftBox.getY() + 330f);

                Texture leftSmallTitle2Texture = new Texture(Gdx.files.internal("images/Home_Button.png"));
                TextureRegionDrawable leftSmallTitle2Drawable = new TextureRegionDrawable(leftSmallTitle2Texture);
                TextButton leftSmallTitle2 = ShopUtils.createInventoryButton("Disarm", skin.getColor("black"), "button",
                                1f, leftSmallTitle2Drawable, leftSmallTitle2Drawable, skin, false);
                leftSmallTitle2.setSize(125f, 35f);
                leftSmallTitle2.setPosition(leftBox.getX() + 175f, leftBox.getY() + 330f);

                equipmentStats = FileLoader.readClass(
                                EquipmentConfig.class,
                                Equipments.getFilepath(currentEquipment));
                description = ShopUtils.createImageTextButton(equipmentStats.name + "\n" + equipmentStats.description,
                                skin.getColor("black"), "button",
                                1f, null, null, skin, true);
                description.setSize((float) (description.getWidth() / 2.1), description.getHeight() / 5);
                description.setPosition(leftBox.getX() + 110f, leftBox.getY() + 280f);

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

                Label healthTitle = new Label("Health", skin, "small");
                healthTitle.setColor(skin.getColor("white"));
                healthTitle.setFontScale(1.5f);
                healthTitle.setPosition(rightBox.getX() + 120f, rightBox.getY() + 480f);

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

                Texture potionUseTexture = new Texture(Gdx.files.internal("images/Home_Button.png"));
                TextureRegionDrawable potionUseDrawable = new TextureRegionDrawable(potionUseTexture);
                TextButton potionUseButton = ShopUtils.createInventoryButton("USE", skin.getColor("black"), "button",
                                1f, potionUseDrawable, potionUseDrawable, skin, false);
                potionUseButton.setSize(115f, 35f);
                potionUseButton.setPosition(rightBox.getX() + 210f, rightBox.getY() + 405f);

                Texture clockTexture = new Texture(Gdx.files.internal("images/shop-clock.png"));
                TextureRegionDrawable clockDrawable = new TextureRegionDrawable(clockTexture);
                ImageButton clock = new ImageButton(clockDrawable, clockDrawable);
                clock.setSize(40f, 40f);
                clock.setPosition(rightBox.getX() + 50f, rightBox.getY() + 305f);

                clockQuantity = new Label("X" + MainArea.getInstance().getGameArea().getPlayer()
                                .getComponent(InventoryComponent.class).getItems().getOrDefault(Artefact.CLOCK, 0),
                                skin, "large");
                clockQuantity.setColor(skin.getColor("black"));
                clockQuantity.setPosition(rightBox.getX() + 100f, rightBox.getY() + 305f);

                Texture clockUseTexture = new Texture(Gdx.files.internal("images/Home_Button.png"));
                TextureRegionDrawable clockUseDrawable = new TextureRegionDrawable(clockUseTexture);
                TextButton clockUseButton = ShopUtils.createInventoryButton("USE", skin.getColor("black"), "button", 1f,
                                clockUseDrawable, clockUseDrawable, skin, false);
                clockUseButton.setSize(115f, 35f);
                clockUseButton.setPosition(rightBox.getX() + 210f, rightBox.getY() + 305f);

                Texture bedTexture = new Texture(Gdx.files.internal("images/shop-bed.png"));
                TextureRegionDrawable bedDrawable = new TextureRegionDrawable(bedTexture);
                ImageButton bed = new ImageButton(bedDrawable, bedDrawable);
                bed.setSize(40f, 40f);
                bed.setPosition(rightBox.getX() + 50f, rightBox.getY() + 205f);

                bedQuantity = new Label("X" + MainArea.getInstance().getGameArea().getPlayer()
                                .getComponent(InventoryComponent.class).getItems().getOrDefault(Artefact.BED, 0), skin,
                                "large");
                bedQuantity.setColor(skin.getColor("black"));
                bedQuantity.setPosition(rightBox.getX() + 100f, rightBox.getY() + 205f);

                Texture bedUseTexture = new Texture(Gdx.files.internal("images/Home_Button.png"));
                TextureRegionDrawable bedUseDrawable = new TextureRegionDrawable(bedUseTexture);
                TextButton bedUseButton = ShopUtils.createInventoryButton("USE", skin.getColor("black"), "button", 1f,
                                bedUseDrawable, bedUseDrawable, skin, false);
                bedUseButton.setSize(115f, 35f);
                bedUseButton.setPosition(rightBox.getX() + 210f, rightBox.getY() + 205f);

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
                                                                MainArea.getInstance().getGameArea().getPlayer()
                                                                                .getComponent(InventoryComponent.class)
                                                                                .setWeapon(currentEquipment);
                                                                MainArea.getInstance().getGameArea().getPlayer()
                                                                                .getComponent(CombatStatsComponent.class)
                                                                                .setAttackMultiplier(
                                                                                                equipmentStats.attack);
                                                                System.out.println("WEAPON equipped");
                                                                MainArea.getInstance().getGameArea().getPlayer()
                                                                                .getComponent(AnimationRenderComponent.class)
                                                                                .startAnimation(Equipments
                                                                                                .getAnimationName(
                                                                                                                currentEquipment));
                                                                attackItem.setDrawable(
                                                                                new TextureRegionDrawable(new Texture(
                                                                                                Gdx.files.internal(
                                                                                                                equipmentStats.itemBackgroundImagePath))));
                                                        } else {
                                                                MainArea.getInstance().getGameArea().getPlayer()
                                                                                .getComponent(InventoryComponent.class)
                                                                                .setArmor(currentEquipment);
                                                                MainArea.getInstance().getGameArea().getPlayer()
                                                                                .getComponent(CombatStatsComponent.class)
                                                                                .setBaseDefense(equipmentStats.attack);
                                                                System.out.println("ARMOR equipped");
                                                                defenceItem.setDrawable(
                                                                                new TextureRegionDrawable(new Texture(
                                                                                                Gdx.files.internal(
                                                                                                                equipmentStats.itemBackgroundImagePath))));
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
                                                        MainArea.getInstance().getGameArea().getPlayer()
                                                                        .getComponent(InventoryComponent.class)
                                                                        .setWeapon(Equipments.AXE);
                                                        MainArea.getInstance().getGameArea().getPlayer()
                                                                        .getComponent(CombatStatsComponent.class)
                                                                        .setAttackMultiplier(equipmentStats.attack);
                                                        System.out.println("WEAPON disarmed");
                                                        MainArea.getInstance().getGameArea().getPlayer()
                                                                        .getComponent(AnimationRenderComponent.class)
                                                                        .startAnimation(Equipments.getAnimationName(
                                                                                        Equipments.AXE));
                                                        attackItem.setDrawable(
                                                                        new TextureRegionDrawable(
                                                                                        new Texture(Gdx.files.internal(
                                                                                                        equipmentStats.itemBackgroundImagePath))));
                                                } else {
                                                        MainArea.getInstance().getGameArea().getPlayer()
                                                                        .getComponent(InventoryComponent.class)
                                                                        .setArmor(null);
                                                        MainArea.getInstance().getGameArea().getPlayer()
                                                                        .getComponent(CombatStatsComponent.class)
                                                                        .setBaseDefense(0);
                                                        System.out.println("ARMOR disarmed");
                                                        defenceItem.setDrawable(
                                                                        new TextureRegionDrawable(
                                                                                        new Texture(Gdx.files.internal(
                                                                                                        "images/shop-category-button.png"))));
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
                                                                currentEquipment = MainArea.getInstance().getGameArea()
                                                                                .getPlayer()
                                                                                .getComponent(InventoryComponent.class)
                                                                                .getEquipmentList()
                                                                                .get(0);
                                                                equipmentStats = FileLoader.readClass(
                                                                                EquipmentConfig.class,
                                                                                Equipments.getFilepath(
                                                                                                currentEquipment));
                                                                description.setText(equipmentStats.name + "\n"
                                                                                + equipmentStats.description);
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
                                                                                Equipments.getFilepath(
                                                                                                currentEquipment));
                                                                description.setText(equipmentStats.name + "\n"
                                                                                + equipmentStats.description);
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

                group.addActor(inventoryFrame);
                group.addActor(subtitle);
                group.addActor(leftBox);
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
                group.addActor(crossFrame);
                group.setVisible(false);

                stage.addActor(group);
        }

        private void closeInventory() {
                ((ForestGameArea) MainArea.getInstance().getGameArea()).exitShop();
                group.setVisible(false);
        }

        private void openInventory() {
                group.setVisible(true);
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
