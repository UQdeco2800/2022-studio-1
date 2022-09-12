package com.deco2800.game.components.maingame;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.deco2800.game.components.CombatStatsComponent;
import com.deco2800.game.components.player.InventoryComponent;
import com.deco2800.game.components.shop.ShopUtils;
import com.deco2800.game.entities.configs.BaseStructureConfig;
import com.deco2800.game.entities.configs.StructureConfig;
import com.deco2800.game.files.FileLoader;
import com.deco2800.game.services.ServiceLocator;
import com.deco2800.game.ui.UIComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class MainGameBuildingInterface extends UIComponent {
    private static final Logger logger = LoggerFactory.getLogger(MainGameExitDisplay.class);
    private static final float Z_INDEX = 2f;
    private Table BuildingUI;
    private Label buildingName;

    private boolean visability;


    @Override
    public void create() {
        super.create();
        addActors();
    }

    public void addActors() {

    }

    public Table makeUIPopUp(Boolean value, float x, float y, String structureName, String structureKey) {
        float uiWidth = 650f;
        float uiHeight = 200f;
        float screenHeight = Gdx.graphics.getHeight();
        float screenWidth = Gdx.graphics.getWidth();

        // code below will work later but crashed at the moment
        //int gold = ServiceLocator.getStructureService().getNamedEntity(structureName).getComponent(InventoryComponent.class).getGold();
        int health = ServiceLocator.getStructureService().getNamedEntity(structureName).getComponent(CombatStatsComponent.class).getHealth();
        int baseAttack = ServiceLocator.getStructureService().getNamedEntity(structureName).getComponent(CombatStatsComponent.class).getBaseAttack();
        int sell = 0;


        x = (float) (x - 0.5 * uiWidth);
        x = Math.max(x, 0f);
        x = Math.min(x, screenWidth - uiWidth);

        y = screenHeight - y;
        y = Math.min(y, screenHeight - uiHeight);

        visability = value;

        BuildingUI = new Table();
        BuildingUI.setSize(uiWidth, uiHeight);
        BuildingUI.setPosition(x, y);

        BuildingUI.setVisible(visability);

        // add popup
        //insert pop up texture
        Texture colour = new Texture(Gdx.files.internal("images/pop-up background.png"));
        Drawable backgroundColour = new TextureRegionDrawable(colour);

        //insert pop up label (with name of the building)
        String buildingType = structureKey + " ";
        buildingName = new Label(buildingType, skin, "large");

        // Insert building health image and bar
        // Heart image
        Image heartImage = new Image(ServiceLocator.getResourceService().getAsset("images/uiElements/exports/heart.png", Texture.class));

        //Health Bar Image
        Image healthBarImage = new Image(ServiceLocator.getResourceService().getAsset("images/healthBar.png", Texture.class));
        Label healthAmount = new Label(Integer.toString(health), skin, "large");


        //upgrade button
        Texture homeButton1 = new Texture(Gdx.files.internal("images/Home_Button.png"));
        TextureRegionDrawable homeUp = new TextureRegionDrawable(homeButton1);
        TextureRegionDrawable homeDown = new TextureRegionDrawable(homeButton1);
        TextButton upgradeButton = ShopUtils.createImageTextButton(
                "Upgrade for:" + "\n" + "100",
                skin.getColor("black"),
                "button", 1f, homeDown, homeUp, skin, true);


        // sell button
        TextButton sellButton = ShopUtils.createImageTextButton(
                "Sell for:" + "\n" + sell,
                skin.getColor("black"),
                "button", 1f, homeDown, homeUp, skin, true);


        //event handlers for buttons -- sell and upgrade
        sellButton.addListener(
                new ChangeListener() {
                    @Override
                    public void changed(ChangeEvent changeEvent, Actor actor) {
                        logger.debug("Sell building clicked");
                        entity.getComponent(InventoryComponent.class).addStone(sell);
                    }
                });

        upgradeButton.addListener(
                new ChangeListener() {
                    @Override
                    public void changed(ChangeEvent changeEvent, Actor actor) {
                        logger.debug("upgrade building clicked");

                        if (entity.getComponent(InventoryComponent.class).hasGold(100)) {
                            logger.info("Sufficient funds");
                            entity.getComponent(InventoryComponent.class).addGold(-1 * 100);

                        } else {
                            logger.info("Insufficient funds");
                        }
                    }
                });

        //table
        Table buildingInfo = new Table();
        buildingInfo.add(buildingName).center();

        Table healthInfo = new Table();
        healthInfo.add(heartImage);
        healthInfo.add(healthBarImage).size(200f,30f);

        healthInfo.add(healthAmount);

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