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
import com.deco2800.game.components.shop.artefacts.ShopBuilding;
import com.deco2800.game.entities.StructureService;
import com.deco2800.game.entities.configs.ShopBuildingConfig;
import com.deco2800.game.files.FileLoader;
import com.deco2800.game.services.AchievementHandler;
import com.deco2800.game.services.ServiceLocator;
import com.deco2800.game.ui.UIComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class BuildingShopDisplay extends UIComponent {
        private static final Logger logger = LoggerFactory.getLogger(BuildingShopDisplay.class);
        private static final float Z_INDEX = 2f;

        Table itemDisplay;
        Group buildingShop;

        private CircularLinkedList<ShopBuilding> stock;
        private Node<ShopBuilding> current;
        private ShopBuildingConfig stats;
        private ShopBuildingConfig prevStats;
        private ShopBuildingConfig nextStats;

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
                entity.getEvents().addListener("buildingShop", this::openShop);
                entity.getEvents().addListener("closeAll", this::closeShop);
                addActor();
        }

        private void addActor() {
                buildingShop = new Group();
                itemDisplay = new Table();
                // itemDisplay.setFillParent(true);
                itemDisplay.setSize((float) (Gdx.graphics.getWidth() * 0.7), (float) (Gdx.graphics.getHeight() * 0.7));
                Texture shopInterfaceTexture = new Texture(Gdx.files.internal("images/popup-border.png"));
                TextureRegionDrawable shop = new TextureRegionDrawable(shopInterfaceTexture);
                itemDisplay.setBackground(shop);
                itemDisplay.setPosition(Gdx.graphics.getWidth() / 2f - itemDisplay.getWidth() / 2f,
                                Gdx.graphics.getHeight() / 2f - itemDisplay.getHeight() / 2f);

                // Create linked list of the available shop stock
                stock = new CircularLinkedList<ShopBuilding>();
                List<ShopBuilding> buildingOptions = ShopBuilding.getAllBuildingTypes();
                for (ShopBuilding e : buildingOptions) {
                        stock.add(e);
                }
                itemNumber = new Label("", skin, "button");
                itemNumber.setFontScale(1.5f);
                itemNumber.setColor(skin.getColor("black"));

                // Create the current building to display
                currentTexture = new Texture(
                                Gdx.files.internal("images/shop-items-framed/category-button-clicked.png"));
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
                                "",
                                skin.getColor("black"),
                                "font_small", 1f,
                                goldenDrawable, goldenDrawable,
                                skin,
                                true);

                // create description sticker
                descriptionDisplay = ShopUtils.createImageTextButton(
                                "",
                                skin.getColor("black"),
                                "button", 1f,
                                brownDrawable, brownDrawable, skin,
                                true);

                // create buy button
                buyButton = ShopUtils.createImageTextButton("BUY", skin.getColor("black"), "button", 1f,
                                goldenDrawable,
                                clickDrawable,
                                skin,
                                false);

                // create the back button
                backTexture = new Texture(Gdx.files.internal("images/backButton.png"));
                upBack = new TextureRegionDrawable(backTexture);
                backButton = new ImageButton(upBack, upBack);

                subtitle = new Label("Buildings", skin, "title");
                subtitle.setFontScale(1.5f);
                subtitle.setPosition(Gdx.graphics.getWidth() / 2f - Gdx.graphics.getWidth() * 0.07f,
                                Gdx.graphics.getHeight() / 2f + Gdx.graphics.getHeight() * 0.25f);
                subtitle.setColor(skin.getColor("black"));

                prevItem.setPosition(Gdx.graphics.getWidth() / 2f - Gdx.graphics.getWidth() * 0.13f,
                                Gdx.graphics.getHeight() / 2f + Gdx.graphics.getHeight() * 0.07f);
                prevItem.setSize(Gdx.graphics.getWidth() * 0.058f, Gdx.graphics.getWidth() * 0.058f);

                currentItem.setPosition(Gdx.graphics.getWidth() / 2f - Gdx.graphics.getWidth() * 0.04f,
                                Gdx.graphics.getHeight() / 2f + Gdx.graphics.getHeight() * 0.05f);
                currentItem.setSize(Gdx.graphics.getWidth() * 0.087f, Gdx.graphics.getWidth() * 0.087f);
                nextItem.setPosition(Gdx.graphics.getWidth() / 2f + Gdx.graphics.getWidth() * 0.07f,
                                Gdx.graphics.getHeight() / 2f + Gdx.graphics.getHeight() * 0.07f);
                nextItem.setSize(Gdx.graphics.getWidth() * 0.058f, Gdx.graphics.getWidth() * 0.058f);

                itemNumber.setPosition(Gdx.graphics.getWidth() / 2f - Gdx.graphics.getWidth() * 0.05f,
                                Gdx.graphics.getHeight() / 2f /* - Gdx.graphics.getHeight() * 0.05f */);
                descriptionDisplay.setPosition(Gdx.graphics.getWidth() / 2f - Gdx.graphics.getWidth() * 0.2f,
                                Gdx.graphics.getHeight() / 2f - Gdx.graphics.getHeight() * 0.35f);
                descriptionDisplay.setSize(Gdx.graphics.getWidth() * 0.4f, Gdx.graphics.getWidth() * 0.3f);
                // itemDisplay.setPosition(Gdx.graphics.getWidth() / 2f,
                // Gdx.graphics.getHeight() * 0.361f);

                leftButton.setPosition(Gdx.graphics.getWidth() / 2f - Gdx.graphics.getWidth() * 0.2f,
                                Gdx.graphics.getHeight() / 2f + Gdx.graphics.getHeight() * 0.10f);
                leftButton.setSize(Gdx.graphics.getWidth() * 0.029f, Gdx.graphics.getWidth() * 0.029f);

                rightButton.setPosition(Gdx.graphics.getWidth() / 2f + Gdx.graphics.getWidth() * 0.17f,
                                Gdx.graphics.getHeight() / 2f + Gdx.graphics.getHeight() * 0.10f);
                rightButton.setSize(Gdx.graphics.getWidth() * 0.029f, Gdx.graphics.getWidth() * 0.029f);

                buyButton.setPosition(Gdx.graphics.getWidth() / 2f + Gdx.graphics.getWidth() * 0.2f,
                                Gdx.graphics.getHeight() / 2f - Gdx.graphics.getHeight() * 0.33f);
                buyButton.setSize(Gdx.graphics.getWidth() * 0.094f, Gdx.graphics.getHeight() * 0.156f);

                priceDisplay.setPosition(Gdx.graphics.getWidth() * 0.2f,
                                Gdx.graphics.getHeight() / 2f - Gdx.graphics.getHeight() * 0.33f);
                priceDisplay.setSize(Gdx.graphics.getWidth() * 0.094f, Gdx.graphics.getHeight() * 0.156f);

                backButton.setPosition(Gdx.graphics.getWidth() / 2f + Gdx.graphics.getWidth() * 0.25f,
                                Gdx.graphics.getHeight() / 2f + Gdx.graphics.getHeight() * 0.25f);
                backButton.setSize(40f, 40f);

                buildingShop.addActor(itemDisplay);
                buildingShop.addActor(leftButton);
                buildingShop.addActor(prevItem);
                buildingShop.addActor(currentItem);
                buildingShop.addActor(nextItem);
                buildingShop.addActor(rightButton);
                buildingShop.addActor(itemNumber);
                buildingShop.addActor(descriptionDisplay);
                buildingShop.addActor(buyButton);
                buildingShop.addActor(priceDisplay);
                buildingShop.addActor(subtitle);
                buildingShop.addActor(backButton);
                buildingShop.setVisible(false);
                stage.addActor(buildingShop);

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
                                        }
                                });
        }

        private void buyItem() {
                if (MainArea.getInstance().getGameArea().getPlayer()
                                .getComponent(InventoryComponent.class)
                                .hasStone(stats.stoneCost) &&
                                MainArea.getInstance().getGameArea().getPlayer()
                                                .getComponent(InventoryComponent.class)
                                                .hasWood(stats.woodCost)) {
                        logger.info("Sufficient resources");
                        MainArea.getInstance().getGameArea().getPlayer()
                                        .getComponent(InventoryComponent.class)
                                        .addWood(-1 * stats.woodCost);
                        MainArea.getInstance().getGameArea().getPlayer()
                                        .getComponent(InventoryComponent.class)
                                        .addStone(-1 * stats.stoneCost);
                        Sound rockSound = Gdx.audio.newSound(
                                        Gdx.files.internal("sounds/rock.mp3"));
                        rockSound.play();
                        closeShop();
                        StructureService.buildTempStructure(stats.placementName);
                        // Trigger events for achievements
                        ServiceLocator.getAchievementHandler().getEvents()
                                        .trigger(AchievementHandler.EVENT_SHOP_ITEM_BOUGHT, 14);
                } else {
                        logger.info("Insufficient resource!");
                        Sound filesound = Gdx.audio.newSound(
                                        Gdx.files.internal("sounds/purchase_fail.mp3"));
                        filesound.play();
                }
                descriptionDisplay.setText(stats.name + "\n" + stats.description + "\n");
                sufficientFunds = MainArea.getInstance().getGameArea().getPlayer()
                                .getComponent(InventoryComponent.class)
                                .hasStone(stats.stoneCost)
                                && MainArea.getInstance().getGameArea().getPlayer()
                                                .getComponent(InventoryComponent.class)
                                                .hasWood(stats.woodCost);
                buyButton.getStyle().up = sufficientFunds ? goldenDrawable
                                : redDrawable;
                buyButton.getStyle().down = sufficientFunds ? clickDrawable
                                : redDrawable;
                buyButton.getStyle().checked = sufficientFunds ? goldenDrawable
                                : redDrawable;

                MainArea.getInstance().getGameArea().getPlayer().getComponent(PlayerStatsDisplay.class)
                                .updateResourceAmount();
                entity.getEvents().trigger("updateBuilding");
        }

        private void carouselLeft() {
                Node<ShopBuilding> temp = current;
                current = stock.head.prev;
                stock.head = stock.head.prev;
                stock.tail = temp.prev;

                prevStats = FileLoader.readClass(ShopBuildingConfig.class,
                                ShopBuilding.getFilepath(current.prev.t));
                stats = FileLoader.readClass(ShopBuildingConfig.class,
                                ShopBuilding.getFilepath(current.t));
                nextStats = FileLoader.readClass(ShopBuildingConfig.class,
                                ShopBuilding.getFilepath(current.next.t));

                priceDisplay.setText(
                                "Stone: " + Integer.toString(stats.woodCost) + " Wood: "
                                                + Integer.toString(stats.woodCost));

                sufficientFunds = MainArea.getInstance().getGameArea().getPlayer()
                                .getComponent(InventoryComponent.class)
                                .hasStone(stats.stoneCost)
                                && MainArea.getInstance().getGameArea().getPlayer()
                                                .getComponent(InventoryComponent.class)
                                                .hasWood(stats.woodCost);
                buyButton.getStyle().up = sufficientFunds ? goldenDrawable
                                : redDrawable;
                buyButton.getStyle().down = sufficientFunds ? clickDrawable
                                : redDrawable;
                buyButton.getStyle().checked = sufficientFunds ? goldenDrawable
                                : redDrawable;

                descriptionDisplay
                                .setText(stats.name + "\n" + stats.description + "\n");
                i = i == 1 ? ShopBuilding.getAllBuildingTypes().size() : i - 1;
                itemNumber.setText("Item " + i + "/" + ShopBuilding.getAllBuildingTypes().size());
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
                Node<ShopBuilding> temp = current;
                current = stock.head.next;
                stock.head = stock.head.next;
                stock.tail = temp;

                // read the stats of the new current
                prevStats = FileLoader.readClass(ShopBuildingConfig.class,
                                ShopBuilding.getFilepath(current.prev.t));
                stats = FileLoader.readClass(ShopBuildingConfig.class,
                                ShopBuilding.getFilepath(current.t));
                nextStats = FileLoader.readClass(ShopBuildingConfig.class,
                                ShopBuilding.getFilepath(current.next.t));

                priceDisplay.setText("Stone: " + Integer.toString(stats.stoneCost)
                                + " Wood: "
                                + Integer.toString(stats.woodCost));
                sufficientFunds = MainArea.getInstance().getGameArea().getPlayer()
                                .getComponent(InventoryComponent.class)
                                .hasStone(stats.stoneCost)
                                && MainArea.getInstance().getGameArea().getPlayer()
                                                .getComponent(InventoryComponent.class)
                                                .hasWood(stats.woodCost);
                buyButton.getStyle().up = sufficientFunds ? goldenDrawable
                                : redDrawable;
                buyButton.getStyle().down = sufficientFunds ? clickDrawable
                                : redDrawable;
                buyButton.getStyle().checked = sufficientFunds ? goldenDrawable
                                : redDrawable;
                descriptionDisplay
                                .setText(stats.name + "\n" + stats.description + "\n");
                i = i == ShopBuilding.getAllBuildingTypes().size() ? 1 : i + 1;
                itemNumber.setText("Item " + i + "/" + ShopBuilding.getAllBuildingTypes().size());
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
                buildingShop.setVisible(true);
        }

        private void closeShop() {
                buildingShop.setVisible(false);
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
