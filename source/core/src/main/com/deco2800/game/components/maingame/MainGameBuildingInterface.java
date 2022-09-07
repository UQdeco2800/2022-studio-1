package com.deco2800.game.components.maingame;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Actor;
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
import com.deco2800.game.ui.UIComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MainGameBuildingInterface extends UIComponent {
    private static final Logger logger = LoggerFactory.getLogger(MainGameExitDisplay.class);
    private static final float Z_INDEX = 2f;
    private Table BuildingUI;
    private Label buildingName;

    public boolean visability;


    @Override
    public void create() {
        super.create();
        addActors();
    }

    private void addActors() {
        visability = false;

        BuildingUI = new Table();
        BuildingUI.padBottom(100f).setFillParent(true);
        BuildingUI.center();
        BuildingUI.setSize(500f,1000f);
        BuildingUI.setVisible(visability);

        // add popup
        //insert pop up texture
        Texture colour = new Texture(Gdx.files.internal("images/shop-buy-button.png"));
        Drawable backgroundColour = new TextureRegionDrawable(colour);

        //insert pop up label (with name of the building)
        String buildingType = "Get Building type here";
        buildingName = new Label(buildingType, skin, "large");

        //Health bar of building, love heart png

        // Insert building info label


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
        BuildingUI.setBackground(backgroundColour);
        BuildingUI.add(buildingName);
        BuildingUI.add(upgradeButton).size(200f, 100f).center();
        BuildingUI.add(sellButton).size(200f, 100f).center();


        stage.addActor(BuildingUI);

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