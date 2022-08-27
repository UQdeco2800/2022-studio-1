package com.deco2800.game.components.maingame;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.deco2800.game.ui.UIComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Displays a button to exit the Main Game screen to the Main Menu screen.
 */
public class MainGameInterface extends UIComponent {
    private static final Logger logger = LoggerFactory.getLogger(MainGameExitDisplay.class);
    private static final float Z_INDEX = 2f;
    private Table table;
    private Table table2;

    @Override
    public void create() {
        super.create();
        addActors();
    }

    private void addActors() {
        table = new Table();
        table.bottom().right();
        table.setFillParent(true);
        table2 = new Table();
        table2.bottom().left();

        //Entering the system button
        Texture settingTexture = new Texture(Gdx.files.internal("images/uiElements/buttons/Shop_Button.png"));
        TextureRegionDrawable upSetting = new TextureRegionDrawable(settingTexture);
        TextureRegionDrawable downSetting = new TextureRegionDrawable(settingTexture);
        ImageButton settingButton = new ImageButton(upSetting,downSetting);

        //Entering the Shop Button
        Texture shopTexture = new Texture(Gdx.files.internal("images/uiElements/buttons/Shop_Button.png"));
        TextureRegionDrawable upShop = new TextureRegionDrawable(shopTexture);
        TextureRegionDrawable downShop = new TextureRegionDrawable(shopTexture);
        ImageButton shopButton = new ImageButton(upShop,downShop);


        //Entering the Inventory Button -- need to add the inventory button
        Texture inventoryTexture = new Texture(Gdx.files.internal("images/uiElements/buttons/Shop_Button.png"));
        TextureRegionDrawable upInventory = new TextureRegionDrawable(inventoryTexture);
        TextureRegionDrawable downInventory = new TextureRegionDrawable(inventoryTexture);
        ImageButton inventoryButton = new ImageButton(upInventory,downInventory);

        //Entering the building button
        Texture buildingTexture = new Texture(Gdx.files.internal("images/Building_Button.png"));
        TextureRegionDrawable upBuilding = new TextureRegionDrawable(buildingTexture);
        TextureRegionDrawable downBuilding = new TextureRegionDrawable(buildingTexture);
        ImageButton buildingButton = new ImageButton(upBuilding,downBuilding);


        // Triggers an event when the button is pressed.
        inventoryButton.addListener(
                new ChangeListener() {
                    @Override
                    public void changed(ChangeEvent changeEvent, Actor actor) {
                        logger.debug("Inventory button clicked");
                        entity.getEvents().trigger("inventory");
                    }
                });

        buildingButton.addListener(
                new ChangeListener() {
                    @Override
                    public void changed(ChangeEvent changeEvent, Actor actor) {
                        logger.debug("Building button clicked");
                        entity.getEvents().trigger("building");
                    }
                });


        table.row();

        table.add(inventoryButton).right().bottom();
        //adding building button to the right
        table.add(buildingButton).right().bottom();
        //adding shop button to the left
        table2.add(shopButton).left().bottom();
        //adding settings to the left
        table2.add(settingButton).left().bottom();


        stage.addActor(table);
        stage.addActor(table2);
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
        table.clear();
        table2.clear();
        super.dispose();
    }
}


