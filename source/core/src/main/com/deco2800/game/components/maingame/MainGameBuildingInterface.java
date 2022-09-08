package com.deco2800.game.components.maingame;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g3d.Renderable;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.deco2800.game.components.shop.ShopUtils;
import com.deco2800.game.components.player.PlayerStatsDisplay.*;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.deco2800.game.components.shop.ShopUtils;
import com.deco2800.game.services.ServiceLocator;
import com.deco2800.game.ui.UIComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;

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
//        Table rootTable = new Table();
//        rootTable.add(makeTable(false));
//        float x = 500f;
//        float y = 500f;
//        rootTable.setPosition(x,y);
////        rootTable.add(makeUIPopUp(false));
//        stage.addActor(rootTable);
    }

    public Table makeTable(Boolean val) {
        Table t = new Table();
        t.setVisible(val);
        Label l = new Label("Hello world", skin, "large");
        t.add(l);
        t.setPosition(500f, 500f);
        stage.addActor(t);
        return t;
    }

    public void setTableVisibility(Table table, Boolean state) {
        table.remove();
        //table.setVisible(state);
        //stage.addActor(table);
    }

    public Table makeUIPopUp(Boolean value) {
        visability = value;

        BuildingUI = new Table();
        BuildingUI.padBottom(100f);
        BuildingUI.center();
        BuildingUI.setSize(800f,800f);

        BuildingUI.setVisible(visability);

        // add popup
        //insert pop up texture
        Texture colour = new Texture(Gdx.files.internal("images/shop-buy-button.png"));
        Drawable backgroundColour = new TextureRegionDrawable(colour);

        //insert pop up label (with name of the building)
        String buildingType = "Get Building type here";
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
                "Upgrade for:",
                skin.getColor("black"),
                "button", 1f, homeDown, homeUp, skin, true);


        // sell button
        TextButton sellButton = ShopUtils.createImageTextButton(
                "Sell for:",
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
        upgradeButton.addListener(
                new ChangeListener() {
                    @Override
                    public void changed(ChangeEvent changeEvent, Actor actor) {
                        logger.debug("upgrade building");
                        entity.getEvents().trigger("upgrade");
                    }
                });



        //table
        Table buildingHealthInfo = new Table();
        buildingHealthInfo.add(heartImage).size(50f);
        buildingHealthInfo.add(healthBarImage).size(200f,30f);

        Table coinRelatedButton = new Table();
        coinRelatedButton.add(upgradeButton).size(200f, 50f).center().padRight(10f);
        coinRelatedButton.add(sellButton).size(200f, 50f).center();

        BuildingUI.setBackground(backgroundColour);
        BuildingUI.add(buildingName).pad(10f);
        BuildingUI.row();
        BuildingUI.add(buildingHealthInfo).padBottom(10f);
        BuildingUI.row();
        BuildingUI.add(coinRelatedButton);

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