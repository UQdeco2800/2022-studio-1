package com.deco2800.game.components.maingame;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.deco2800.game.components.player.InventoryComponent;
import com.deco2800.game.components.shop.ShopUtils;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
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

    //Calling Building stats from JSON file
    private static final StructureConfig buildingStats=
            FileLoader.readClass(StructureConfig.class, "configs/structure.json");

    private String stoneQuarryGoldAmount = Integer.toString(buildingStats.stoneQuarry.gold);



    @Override
    public void create() {
        super.create();
        addActors();
    }

    public void addActors() {

    }

    public Table makeUIPopUp(Boolean value, float x, float y, String structureName) {
        float uiWidth = 800f;
        float uiHeight = 400f;
        float screenHeight = Gdx.graphics.getHeight();
        float screenWidth = Gdx.graphics.getWidth();
        int gold = 0;
        int health = 0;
        int baseAttack = 0;
        int sell = 0;

        switch (structureName) {
            case "wall":
                gold = buildingStats.wall.gold;
                health = buildingStats.wall.health;
                baseAttack = buildingStats.wall.baseAttack;
                break;
            case "tower1":
                gold = buildingStats.tower1.gold;
                health = buildingStats.tower1.health;
                baseAttack = buildingStats.tower1.baseAttack;
                break;
            case "stoneQuarry":
                gold = buildingStats.stoneQuarry.gold;
                health = buildingStats.stoneQuarry.health;
                baseAttack = buildingStats.stoneQuarry.health;
                break;
        }
//
//
//
//        x = (float) (x - 0.5 * uiWidth);
//        x = Math.max(x, 0f);
//        x = Math.min(x, screenWidth - uiWidth);
//
//        y = screenHeight - y;
//        y = Math.min(y, screenHeight - uiHeight);

        System.out.println(x);
        System.out.println(y);

        visability = value;

        BuildingUI = new Table();
        BuildingUI.setSize(uiWidth,uiHeight);
        BuildingUI.setPosition(x, y);

        BuildingUI.setVisible(visability);

        // add popup
        //insert pop up texture
        Texture colour = new Texture(Gdx.files.internal("images/shop-buy-button.png"));
        Drawable backgroundColour = new TextureRegionDrawable(colour);

        //insert pop up label (with name of the building)
        String buildingType = structureName;
        buildingName = new Label(buildingType, skin, "large");

        // Insert building health image and bar
        // Heart image
        Image heartImage = new Image(ServiceLocator.getResourceService().getAsset("images/uiElements/exports/heart.png", Texture.class));

        //Health Bar Image
        Image healthBarImage = new Image(ServiceLocator.getResourceService().getAsset("images/healthBar.png", Texture.class ));
        // Health text level - grabbing percentile - to populate health bar
        // will need to talk to team 7 about the building health


        //upgrade button
        Texture homeButton1 = new Texture(Gdx.files.internal("images/Home_Button.png"));
        TextureRegionDrawable homeUp = new TextureRegionDrawable(homeButton1);
        TextureRegionDrawable homeDown = new TextureRegionDrawable(homeButton1);
        TextButton upgradeButton = ShopUtils.createImageTextButton(
                "Upgrade for:" +  Integer.toString(gold),
                skin.getColor("black"),
                "button", 1f, homeDown, homeUp, skin, true);


        // sell button
        TextButton sellButton = ShopUtils.createImageTextButton(
                "Sell for:" + Integer.toString(sell),
                skin.getColor("black"),
                "button", 1f, homeDown, homeUp, skin, true);


        //event handlers for buttons -- sell and upgrade
        sellButton.addListener(
                new ChangeListener() {
                    @Override
                    public void changed(ChangeEvent changeEvent, Actor actor) {
                        logger.debug("Sell building");
                        entity.getEvents().trigger("sell");
                    }
                });

        //.hasGold is a thing in entity could be usuful to change balance
        int finalGold = gold;
        upgradeButton.addListener(
                new ChangeListener() {
                    @Override
                    public void changed(ChangeEvent changeEvent, Actor actor) {
                        logger.debug("upgrade building");
                        entity.getEvents().trigger("upgrade");
                        int coin = entity.getComponent(InventoryComponent.class).getGold();
                        entity.getComponent(InventoryComponent.class).hasGold(finalGold);
                        int updateGold = finalGold - coin;
                        entity.getComponent(InventoryComponent.class).setGold(updateGold);
                    }
                });

        //table
        Table buildingInfo = new Table();
        buildingInfo.add(buildingName).center();

        Table healthInfo = new Table();
        healthInfo.add(heartImage);
        healthInfo.add(healthBarImage).size(200f,30f);

        Table leftTable = new Table();
        leftTable.padBottom(30f);
        leftTable.row();
        leftTable.add(buildingInfo);
        leftTable.row();
        leftTable.add(healthInfo);


        Table rightTable = new Table();
        rightTable.padBottom(30f);
        rightTable.add(sellButton).size(250f, 45f).center().padBottom(10f).padRight(20f);
        rightTable.row();
        rightTable.add(upgradeButton).size(250f, 45f).center().padBottom(10f);

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