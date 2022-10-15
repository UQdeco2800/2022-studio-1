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
import com.deco2800.game.components.CombatStatsComponent;
import com.deco2800.game.components.player.InventoryComponent;
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


public class MainGameBuildingInterface extends UIComponent {
    private static final Logger logger = LoggerFactory.getLogger(MainGameExitDisplay.class);
    private static final float Z_INDEX = 2f;
    private Table BuildingUI;
    private Table CrystalUI;
    private Label CrystalLabel;
    private Image crystalImage;


    private boolean visability;


    Entity buildingHealth;
    private ProgressBar progressBar;


    @Override
    public void create() {
        super.create();
        addActors();
    }

    public void addActors() {

    }



    public Table makeUIPopUp(Boolean value, float x, float y, GridPoint2 entityCords, String structureName) {

        //Building that was clicked
        Entity clickedStructure = ServiceLocator.getUGSService().getEntity(entityCords);

        // code below will work later but crashed at the moment
        int health = clickedStructure.getComponent(CombatStatsComponent.class).getHealth();
        int baseAttack = clickedStructure.getComponent(CombatStatsComponent.class).getBaseAttack();
        int sell = 0;

        float uiHeight = 200f;
        float screenHeight = Gdx.graphics.getHeight();


        y = screenHeight - y + 100;
        if (y + uiHeight > screenHeight) {
            y -= uiHeight + 100;
        }

        float uiWidth = 650f;
        float screenWidth = Gdx.graphics.getWidth();

        x = (x - 0.5f * uiWidth);
        x = Math.max(x, 0f);
        x = Math.min(x, screenWidth - uiWidth);

        visability = value;
        BuildingUI.setVisible(true);
        BuildingUI = new Table();
        BuildingUI.setSize(uiWidth, uiHeight);
        BuildingUI.setPosition(x, y);


        Texture colour = new Texture(Gdx.files.internal("images/pop-up background.png"));
        Drawable backgroundColour = new TextureRegionDrawable(colour);

        String buildingName = structureName.replaceAll("[^A-Za-z]", "").toUpperCase();
        CrystalLabel = new Label(buildingName, skin, "large");

        // Heart image
        Image heartImage = new Image(ServiceLocator.getResourceService().getAsset("images/uiElements/exports/heart.png", Texture.class));

        //Health Bar Image
        Image healthBarImage = new Image(ServiceLocator.getResourceService().getAsset("images/empty_healthbar.png", Texture.class));

//        //Health Bar image
        buildingHealth = clickedStructure;
        progressBar = buildingHealth.getComponent(HealthBarComponent.class).getProgressBar();
        progressBar.getStyle().background = DrawableUtil
                .getRectangularColouredDrawable(50, 15,  Color.BROWN);
        progressBar.getStyle().knob = DrawableUtil
                .getRectangularColouredDrawable(0, 15, Color.MAROON);
        progressBar.getStyle().knobBefore = DrawableUtil
                .getRectangularColouredDrawable(50, 15, Color.MAROON);


        //upgrade button
        Texture homeButton1 = new Texture(Gdx.files.internal("images/Home_Button.png"));
        TextureRegionDrawable homeUp = new TextureRegionDrawable(homeButton1);
        TextureRegionDrawable homeDown = new TextureRegionDrawable(homeButton1);
        TextButton upgradeButton = ShopUtils.createImageTextButton(
                "Upgrade for:" + "\n" + "100",
                skin.getColor("black"),
                "button", 1f, homeDown, homeUp, skin, false);

        upgradeButton.addListener(
            new ChangeListener() {
                @Override
                public void changed(ChangeEvent changeEvent, Actor actor) {
                    Entity player = ServiceLocator.getEntityService().getNamedEntity("player");
                    //Obtain reference to player, for some reason it was being accessed as 'entity'

                    logger.info("Upgrade Button clicked");

                    if (player.getComponent(InventoryComponent.class).hasGold(100)) {
                        logger.info("Sufficient resources");

                        //Subtract currency from inventory
                        player.getComponent(InventoryComponent.class).addGold(-1 * 100);

                        //Get building and convert it's position to gridPoint2
                        Vector2 position = clickedStructure.getPosition();
                        GridPoint2 gridPoint2 = new GridPoint2((int) position.x, (int) position.y);
                        
                        StructureFactory.upgradeStructure(gridPoint2, clickedStructure.getName());
                    } else {
                        logger.info("Insufficient resource!");
                        Sound filesound = Gdx.audio.newSound(
                            Gdx.files.internal("sounds/purchase_fail.mp3"));
                        filesound.play();
                    }
                } 
            }
        );

        // sell button
        TextButton sellButton = ShopUtils.createImageTextButton(
                "Sell" + "\n",
                skin.getColor("black"),
                "button", 1f, homeDown, homeUp, skin, false);


        //event handlers for buttons -- sell and upgrade
        sellButton.addListener(
                new ChangeListener() {
                    @Override
                    public void changed(ChangeEvent changeEvent, Actor actor) {
                        logger.debug("Sell button clicked");
                        StructureFactory.handleBuildingDestruction(entity.getName());
                        //Entity player = ServiceLocator.getEntityService().getNamedEntity("player");
                        //player.getComponent(InventoryComponent.class).addStone(sell);
                    }
                });


        //table
        Table buildingInfo = new Table();
        buildingInfo.add(CrystalLabel).center();

        Table healthInfo = new Table();
        healthInfo.add(heartImage);
        healthInfo.stack(progressBar, healthBarImage).size(200f,30f);

        //healthInfo.add(healthAmount);

        Table leftTable = new Table();
        leftTable.padBottom(30f);
        leftTable.row();
        leftTable.add(buildingInfo);
        leftTable.row();
        leftTable.add(healthInfo);


        Table rightTable = new Table();
        rightTable.padBottom(30f);
        rightTable.add(sellButton).size(250f, 80f).center().padBottom(10f).padRight(20f).padTop(23f);
        rightTable.row();
        rightTable.add(upgradeButton).size(250f, 80f).center();

        BuildingUI.setBackground(backgroundColour);
        BuildingUI.add(leftTable);
        BuildingUI.add(rightTable);


        stage.addActor(BuildingUI);

        return BuildingUI;
    }

    public Table makeCrystalPopUp(Boolean value, float x, float y) {
        float uiHeight = 200f;
        float screenHeight = Gdx.graphics.getHeight();
        Entity crystal = ServiceLocator.getEntityService().getNamedEntity("crystal");
        int level = crystal.getComponent(CombatStatsComponent.class).getLevel();
        Entity player = ServiceLocator.getEntityService().getNamedEntity("player");
        int playerGold = player.getComponent(InventoryComponent.class).getGold();
        Image crystalhealth;
        String cost,health;
        if (level == 1){
            cost = "2000";
            health = "+100";
            crystalhealth = new Image(ServiceLocator.getResourceService().getAsset("images/crystalhealth.png", Texture.class));

        } else {
            cost = "5000";
            health = "+200";
            crystalhealth = new Image(ServiceLocator.getResourceService().getAsset("images/crystalhealth2.png", Texture.class));

        }

        y = screenHeight - y + 100;
        if (y + uiHeight > screenHeight) {
            y -= uiHeight + 100;
        }

        float uiWidth = 650f;
        float screenWidth = Gdx.graphics.getWidth();

        x = (x - 0.5f * uiWidth);
        x = Math.max(x, 0f);
        x = Math.min(x, screenWidth - uiWidth);

        visability = value;

        CrystalUI = new Table();
        CrystalUI.setSize(uiWidth, uiHeight);
        CrystalUI.setPosition(x, y);


        CrystalUI.setVisible(true);

        // add popup
        //insert pop up texture
        Texture colour = new Texture(Gdx.files.internal("images/pop-up background.png"));
        Drawable backgroundColour = new TextureRegionDrawable(colour);

        CrystalLabel = new Label("CRYSTAL", skin, "large");

        //upgrade button
        Texture homeButton1 = new Texture(Gdx.files.internal("images/Home_Button.png"));
        TextureRegionDrawable homeUp = new TextureRegionDrawable(homeButton1);
        TextureRegionDrawable homeDown = new TextureRegionDrawable(homeButton1);
        TextButton upgradeButton = ShopUtils.createImageTextButton(
                "Upgrade" + "\n" + cost,
                skin.getColor("black"),
                "button", 1f, homeDown, homeUp, skin, false);

        upgradeButton.addListener(
                new ChangeListener() {
                    @Override
                    public void changed(ChangeEvent changeEvent, Actor actor) {
                        logger.info("Upgrade Button clicked");

                        if (level == 1 && playerGold >= 2000) {
                            logger.info("Sufficient gold to upgrade crystal");
//                            Entity camera = ServiceLocator.getEntityService().getNamedEntity("camera");
//                            CameraComponent camComp = camera.getComponent(CameraComponent.class);
//
//                                float x = (random.nextFloat() - 0.5f) * 2 * .2f;
//                                float y = (random.nextFloat() - 0.5f) * 2 * .2f;
//                                float z = (random.nextFloat() - 0.5f) * 2 * .2f;
//
//                                // Set the camera to this new x/y position
//                                camComp.getCamera().translate(-50,-y,-z);
                            //screenShakeComponent.ScreenShake(.2f, .1f);
                            //screenShakeComponent.tick(ServiceLocator.getTimeSource().getDeltaTime());
                            CrystalService.upgradeCrystal();
                            entity.getEvents().trigger("screenShake");
                            CrystalUI.remove();
                        }
                        else if (level == 2 && playerGold >= 5000) {
                            logger.info("Sufficient gold to upgrade crystal");
                            CrystalService.upgradeCrystal();
                            entity.getEvents().trigger("screenShake");
                            CrystalUI.remove();
                        }

                        else if (level == 3){
                            logger.info("Crystal has reached Max Level");
                        }
                        else {
                            logger.info("Insufficient gold to upgrade crystal!");
                            Sound filesound = Gdx.audio.newSound(
                            Gdx.files.internal("sounds/purchase_fail.mp3"));
                            filesound.play();
                        }
                    }
                }
        );
        if (level == 1) {
            crystalImage = new Image(ServiceLocator.getResourceService().getAsset("images/crystal_level2.png", Texture.class));
        } else {
            crystalImage = new Image(ServiceLocator.getResourceService().getAsset("images/crystal_level3.png", Texture.class));
        }

        //table
        Table CrystalInfo = new Table();
        CrystalInfo.add(CrystalLabel).center();



        //healthInfo.add(healthAmount);

        Table leftTable = new Table();
        leftTable.padTop(10f);
        leftTable.padBottom(10f);
        leftTable.row();
        leftTable.add(CrystalInfo);
        leftTable.row();
        leftTable.add(crystalImage);


        Table rightTable = new Table();
        rightTable.padLeft(20f);
        rightTable.padRight(20f);
        rightTable.padBottom(10f);
        Image heartImage = new Image(ServiceLocator.getResourceService().getAsset("images/uiElements/exports/heart.png", Texture.class));
        Label crystalLabel = new Label(health, skin, "large");
        rightTable.add(crystalhealth).size(300f,50f);
        rightTable.add(crystalLabel).size(50f,60f).padTop(10f).padBottom(10f);
        //rightTable.add(healthLabel);
        rightTable.row();
        if(crystal.getComponent(CombatStatsComponent.class).getLevel()<3) {
            rightTable.add(upgradeButton).size(250f, 80f).center().padLeft(50f).padRight(10f).padTop(10f).padBottom(10f);
        }
        else {
//            Label crystalLabel = new Label("Crystal has reached max level", skin, "large");
//            rightTable.add(crystalLabel).size(250f, 80f).center();
        }

        CrystalUI.setBackground(backgroundColour);
        CrystalUI.add(leftTable);
        CrystalUI.add(rightTable).padTop(10f);


        stage.addActor(CrystalUI);

        return CrystalUI;
    }

    public boolean isVisability() {
        return visability;
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