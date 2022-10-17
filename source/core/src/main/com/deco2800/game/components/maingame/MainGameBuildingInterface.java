package com.deco2800.game.components.maingame;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Graphics;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Array;
import com.deco2800.game.areas.ForestGameArea;
import com.deco2800.game.areas.MainArea;
import com.deco2800.game.areas.terrain.TerrainComponent;
import com.deco2800.game.components.CombatStatsComponent;
import com.deco2800.game.components.player.InventoryComponent;
import com.deco2800.game.components.player.PlayerStatsDisplay;
import com.deco2800.game.components.shop.ShopUtils;
import com.deco2800.game.entities.factories.CrystalService;
import com.deco2800.game.entities.factories.StructureFactory;
import com.deco2800.game.services.ServiceLocator;
import com.deco2800.game.ui.UIComponent;
import com.deco2800.game.utils.StringDecorator;
import com.deco2800.game.entities.Entity;
import com.deco2800.game.components.HealthBarComponent;
import com.deco2800.game.utils.DrawableUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.badlogic.gdx.math.MathUtils.random;

import java.sql.Struct;

public class MainGameBuildingInterface extends UIComponent {
    private static final Logger logger = LoggerFactory.getLogger(MainGameExitDisplay.class);
    private static final float Z_INDEX = 2f;
    private Table BuildingUI = new Table();
    private Table CrystalUI;
    private Label CrystalLabel;
    private Image crystalImage;

    private boolean visibility;

    Entity buildingHealth;
    private ProgressBar progressBar;

    @Override
    public void create() {
        super.create();
        addActors();
    }

    public void addActors() {
        // Handled outside
    }

    public Table makeUIPopUp(Boolean value, float x, float y, GridPoint2 entityCords, String structureName) {

        // Building that was clicked
        Entity clickedStructure = ServiceLocator.getUGSService().getEntity(entityCords);

        int health = clickedStructure.getComponent(CombatStatsComponent.class).getHealth();
        int baseAttack = clickedStructure.getComponent(CombatStatsComponent.class).getBaseAttack();
        Label healthAmount = new Label("" + health, skin);

        float uiHeight = 300f;
        float screenHeight = Gdx.graphics.getHeight();

        y = screenHeight - y + 100;
        if (y + uiHeight > screenHeight) {
            y -= uiHeight + 100;
        }

        float uiWidth = 700f;
        float screenWidth = Gdx.graphics.getWidth();

        x = (x - 0.5f * uiWidth);
        x = Math.max(x, 0f);
        x = Math.min(x, screenWidth - uiWidth);

        visibility = value;
        BuildingUI.setVisible(true);
        BuildingUI = new Table();
        BuildingUI.setSize(uiWidth, uiHeight);
        BuildingUI.setPosition(x, y);


        Texture colour = new Texture(Gdx.files.internal("images/pop-up background.png"));
        Drawable backgroundColour = new TextureRegionDrawable(colour);

        String buildingName = structureName.replaceAll("[^A-Za-z]", "").toUpperCase();
        CrystalLabel = new Label(buildingName, skin, ForestGameArea.LARGE_FONT);

        //Heart image
        Image heartImage = new Image(ServiceLocator.getResourceService().getAsset("images/uiElements/exports/heart.png", Texture.class));
        //Health Bar image
        Image healthBarImage = new Image(
                ServiceLocator.getResourceService().getAsset("images/empty_healthbar.png", Texture.class));
        // Label healthAmount = new Label(Integer.toString(health), skin, ForestGameArea.LARGE_FONT);

        // //Health Bar image
        buildingHealth = clickedStructure;
        progressBar = buildingHealth.getComponent(HealthBarComponent.class).getProgressBar();
        progressBar.getStyle().background = DrawableUtil
                .getRectangularColouredDrawable(50, 15, Color.BROWN);
        progressBar.getStyle().knob = DrawableUtil
                .getRectangularColouredDrawable(0, 15, Color.MAROON);
        progressBar.getStyle().knobBefore = DrawableUtil
                .getRectangularColouredDrawable(50, 15, Color.MAROON);


        //upgrade button
        Texture homeButton1 = new Texture(Gdx.files.internal("images/Home_Button.png"));
        TextureRegionDrawable homeUp = new TextureRegionDrawable(homeButton1);
        TextureRegionDrawable homeDown = new TextureRegionDrawable(homeButton1);

        Texture goldenCategoryTexture = new Texture(Gdx.files.internal("images/Home_Button.png"));
        Texture redCategoryTexture = new Texture(Gdx.files.internal("images/upgradeButtonFailed.png"));
        TextureRegionDrawable goldenDrawable = new TextureRegionDrawable(goldenCategoryTexture);
        Texture brownCategoryTexture = new Texture(Gdx.files.internal("images/upgradeButtonOnclick.png"));
        TextureRegionDrawable brownDrawable = new TextureRegionDrawable(brownCategoryTexture);
        TextureRegionDrawable redDrawable = new TextureRegionDrawable(redCategoryTexture);

        // create buy button
        Boolean sufficientFunds = (MainArea.getInstance().getGameArea().getPlayer()
                .getComponent(InventoryComponent.class)
                .hasGold(2000));
        TextButton upgradeButton = ShopUtils.createImageTextButton("\n Upgrade for: " + "\n" + "2000", skin.getColor(ForestGameArea.BLACK), ForestGameArea.BUTTON_FONT, 1f,
                sufficientFunds ? brownDrawable : redDrawable,
                sufficientFunds ? goldenDrawable : redDrawable,
                skin,
                false);


        upgradeButton.addListener(
            new ChangeListener() {
                @Override
                public void changed(ChangeEvent changeEvent, Actor actor) {
                    Entity player = ServiceLocator.getEntityService().getNamedEntity(CombatStatsComponent.PLAYER);
                    //Obtain reference to player, for some reason it was being accessed as 'entity'
                    int playerGold = player.getComponent(InventoryComponent.class).getGold();
                    if (playerGold > 2000) {
                        logger.info("Sufficient resources");

                        //Get building and convert it's position to gridPoint2

                        Vector2 worldPos = clickedStructure.getPosition();
                        int worldX = Math.round(worldPos.x);
                        int worldY = Math.round(worldPos.y);

                        GridPoint2 position = ServiceLocator.getEntityService().getNamedEntity("terrain")
                                .getComponent(TerrainComponent.class).worldToTilePosition(worldX, worldY);

                        position.y += 1;
                        StructureFactory.upgradeStructure(position, clickedStructure.getName());
                        logger.info("Upgrade Button clicked");

                        if (!clickedStructure.getName().contains("wall")) {
                            //Subtract currency from inventory if ! wall
                            player.getComponent(InventoryComponent.class).addGold(-1 * 2000);
                            PlayerStatsDisplay.updateItems();
                        }

                        BuildingUI.remove();

                    } else {
                        logger.info("Insufficient resource!");
                        Sound filesound = Gdx.audio.newSound(
                                Gdx.files.internal("sounds/purchase_fail.mp3"));
                        filesound.play();
                    }
                }});


        // sell button
//        String stoneAndwood = ServiceLocator.getStructureService().SellBuilding(structureName, entityCords);
//        String[] arrOfStr = stoneAndwood.split(",");

    //    Boolean sufficientFundsSell = (MainArea.getInstance().getGameArea().getPlayer()
    //             .getComponent(InventoryComponent.class).hasStone(Integer.parseInt(arrOfStr[0])) && MainArea.getInstance()
    //            .getGameArea().getPlayer()
    //            .getComponent(InventoryComponent.class).hasWood(Integer.parseInt(arrOfStr[1])));
    //     // TextButton  sellButton = ShopUtils.createImageTextButton("\n Sell for: " + "\n" + "Wood: " + arrOfStr[1]
        //                 + " & Stone: " + arrOfStr[0]
        //         , skin.getColor("black"), "button", 1f,
        //         sufficientFundsSell ? brownDrawable : redDrawable,
        //         sufficientFundsSell ? goldenDrawable : redDrawable,
        //         skin,
        //         false);
        TextButton  sellButton = ShopUtils.createImageTextButton("\n Sell"
        , skin.getColor("black"), "button", 1f,
        // sufficientFundsSell ? brownDrawable : redDrawable,
        // sufficientFundsSell ? goldenDrawable : redDrawable,
        brownDrawable,
        goldenDrawable,
        skin,
        false);

        sellButton.addListener(
                new ChangeListener() {
                    @Override
                    public void changed(ChangeEvent changeEvent, Actor actor) {
                        logger.debug("Sell button clicked");

                        StructureFactory.handleBuildingDestruction(clickedStructure.getName());
                        PlayerStatsDisplay.updateItems();
                        // Remove building entity
                        BuildingUI.remove();
                    }
                });


        //table
        Table buildingInfo = new Table();
        buildingInfo.add(CrystalLabel).center();

        Table healthInfo = new Table();
        healthInfo.add(heartImage);
        healthInfo.stack(progressBar, healthBarImage).size(200f, 30f);
        healthInfo.add(healthAmount).padLeft(5f);


        Table leftTable = new Table();
        leftTable.padBottom(30f);
        leftTable.row();
        leftTable.add(buildingInfo);
        leftTable.row();
        leftTable.add(healthInfo);

        Table rightTable = new Table();
        rightTable.padBottom(30f);
        rightTable.add(sellButton).size(300f, 100f).center().padBottom(10f).padRight(20f).padTop(23f);
        rightTable.row();
        rightTable.add(upgradeButton).size(300f, 100f).center();

        BuildingUI.setBackground(backgroundColour);
        BuildingUI.add(leftTable);
        BuildingUI.add(rightTable);

        stage.addActor(BuildingUI);

        return BuildingUI;
    }
    


    /**
     * Create Crystal Upgrade Pop Up
     * @param value visibility value of Pop Up
     * @param x x coordinate of Crystal
     * @param y y coordinate of Crystal
     */
    public Table makeCrystalPopUp(Boolean value, float x, float y) {
        float uiHeight = 300f;
        float screenHeight = Gdx.graphics.getHeight();
        Entity crystal = ServiceLocator.getEntityService().getNamedEntity("crystal");
        int level = crystal.getComponent(CombatStatsComponent.class).getLevel();
        Entity player = ServiceLocator.getEntityService().getNamedEntity(CombatStatsComponent.PLAYER);
        int playerGold = player.getComponent(InventoryComponent.class).getGold();
        //player.getComponent(InventoryComponent.class).addGold(-9500);

        Image crystalhealth;
        String cost,health;
        TextureRegionDrawable buttonUp;
        TextureRegionDrawable buttonDown;
        Texture button;

        if (level == 1){
            cost = "500";
            health = "+200";
            crystalhealth = new Image(ServiceLocator.getResourceService().getAsset("images/crystalhealth3.png", Texture.class));
            crystalImage = new Image(
                    ServiceLocator.getResourceService().getAsset("images/crystal2.0.png", Texture.class));
            if (playerGold >= 500) {
                button = new Texture(Gdx.files.internal("images/upgrade500.2.png"));
            }
            else {
                button = new Texture(Gdx.files.internal("images/upgradeFail500.png"));
            }

        } else {
            cost = "1500";
            health = "+300";
            crystalhealth = new Image(ServiceLocator.getResourceService().getAsset("images/crystalhealth4.png", Texture.class));
            crystalImage = new Image(
                    ServiceLocator.getResourceService().getAsset("images/crystal_level3.png", Texture.class));
            if (playerGold >= 1500) {
                button = new Texture(Gdx.files.internal("images/upgrade1500.2.png"));
            }
            else {
                button = new Texture(Gdx.files.internal("images/upgradeFail1500.png"));
            }        }

        y = screenHeight - y + 100;
        if (y + uiHeight > screenHeight) {
            y -= uiHeight + 100;
        }

        float uiWidth = 650f;
        float screenWidth = Gdx.graphics.getWidth();

        x = (x - 0.5f * uiWidth);
        x = Math.max(x, 0f);
        x = Math.min(x, screenWidth - uiWidth);

        visibility = value;

        CrystalUI = new Table();
        CrystalUI.setSize(uiWidth, uiHeight);
        CrystalUI.setPosition(x, y);

        CrystalUI.setVisible(true);


        Texture colour = new Texture(Gdx.files.internal("images/pop-up background.png"));
        Drawable backgroundColour = new TextureRegionDrawable(colour);

        CrystalLabel = new Label("Crystal", skin, ForestGameArea.LARGE_FONT);
        buttonUp = new TextureRegionDrawable(button);
        buttonDown = new TextureRegionDrawable(button);
        //upgrade button

        TextButton upgradeButton = ShopUtils.createImageTextButton(
                "",
                skin.getColor(ForestGameArea.BLACK),
                ForestGameArea.BUTTON_FONT, 1f, buttonDown, buttonUp, skin, false);
        upgradeButton.addListener(
                new ChangeListener() {
                    @Override
                    public void changed(ChangeEvent changeEvent, Actor actor) {
                        logger.info("Upgrade Button clicked");

                        if (level == 1 && playerGold >= 500) {
                            logger.info("Sufficient gold to upgrade crystal");
                            CrystalService.upgradeCrystal();
                            //entity.getEvents().trigger("screenShake");
                            CrystalUI.remove();
                        }
                        else if (level == 2 && playerGold >= 1500) {

                            logger.info("Sufficient gold to upgrade crystal");
                            CrystalService.upgradeCrystal();
                            //entity.getEvents().trigger("screenShake");
                            CrystalUI.remove();
                        }

                        else {
                            logger.info("Insufficient gold to upgrade crystal!");
                            Sound filesound = Gdx.audio.newSound(
                                    Gdx.files.internal("sounds/purchase_fail.mp3"));
                            filesound.play();
                        }
                    }
                });


        Label UpgradeCrystalLabel = new Label("Upgrade Crystal?", skin, ForestGameArea.LARGE_FONT);

        //table
        Table CrystalInfo = new Table();
        CrystalInfo.add(CrystalLabel).center();

        Table leftTable = new Table();
        leftTable.padTop(10f);
        leftTable.padBottom(10f);
        //leftTable.add(CrystalInfo);
        leftTable.row();
        leftTable.add(crystalImage).size(150,200);

        Table rightTable = new Table();
        rightTable.padLeft(20f);
        rightTable.padRight(20f);
        rightTable.padBottom(10f);
        rightTable.add(UpgradeCrystalLabel);
        rightTable.row();
        Label healthLabel = new Label(health, skin, ForestGameArea.LARGE_FONT);
        //rightTable.add(crystalImage).size(1f,1f);
        rightTable.add(crystalhealth).size(300f,40f);
        rightTable.add(healthLabel).size(30f,60f).padTop(10f).padBottom(10f);
        rightTable.row();
        if(crystal.getComponent(CombatStatsComponent.class).getLevel()<3) {
            rightTable.add(upgradeButton).size(250f, 80f).center().padLeft(50f).padRight(10f).padTop(10f).padBottom(10f);
        }
        CrystalUI.setBackground(backgroundColour);
        CrystalUI.add(leftTable);
        CrystalUI.add(rightTable).padTop(10f);

        stage.addActor(CrystalUI);

        return CrystalUI;
    }

    /**
     * Create Crystal Notification when Crystal has reached max level
     * @param value visibility value of Pop Up
     */
    public Table makeCrystalNoti(Boolean value) {
        visibility = value;
        CrystalUI = new Table();
        CrystalUI.setSize(461, 200);
        CrystalUI.setPosition(730,45);
        CrystalUI.setVisible(true);
        Texture colour = new Texture(Gdx.files.internal("images/tutorials/CrystalPopUp.png"));
        Drawable backgroundColour = new TextureRegionDrawable(colour);
        CrystalUI.setBackground(backgroundColour);
        CrystalUI.bottom();
        CrystalUI.padBottom(50);
        stage.addActor(CrystalUI);

        return CrystalUI;
    }


    public boolean isVisible() {
        return visibility;

    }

    private Array<StringDecorator<Graphics.DisplayMode>> getDisplayModes(Graphics.Monitor monitor) {
        Graphics.DisplayMode[] displayModes = Gdx.graphics.getDisplayModes(monitor);
        Array<StringDecorator<Graphics.DisplayMode>> arr = new Array<>();

        for (Graphics.DisplayMode displayMode : displayModes) {
            arr.add(new StringDecorator<>(displayMode, this::prettyPrint));
        }

        return arr;
    }

    private String prettyPrint(Graphics.DisplayMode displayMode) {
        return displayMode.width + "x" + displayMode.height + ", " + displayMode.refreshRate + "hz";
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
        super.dispose();
    }
}