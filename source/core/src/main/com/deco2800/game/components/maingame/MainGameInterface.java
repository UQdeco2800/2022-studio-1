package com.deco2800.game.components.maingame;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
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
    private Table leftSideTable;
    private Table rightSideTable;
    private Table table1;
    private Table table2;

    private boolean checkHiden = false;
    @Override
    public void create() {
        super.create();
        addActors();
    }

    private void addActors() {
        rightSideTable = new Table();
        rightSideTable.bottom().right();
        rightSideTable.setFillParent(true);

        leftSideTable = new Table();
        leftSideTable.bottom().left();
        leftSideTable.setFillParent(true);

        table1 = new Table();
        table1.center();
//        table1.setSize(50f,50f);
        table1.setFillParent(true);

        Texture inventoryInterfaceTexture = new Texture(Gdx.files.internal("images/popup-border.png"));
        TextureRegionDrawable inventory = new TextureRegionDrawable(inventoryInterfaceTexture);
        ImageButton inventoryFrame = new ImageButton(inventory,inventory);
//        inventoryFrame.setSize(100,100);

        // Entering the Shop Button
        Texture shopTexture = new Texture(Gdx.files.internal("images/Shop.png"));
        TextureRegionDrawable upShop = new TextureRegionDrawable(shopTexture);
        TextureRegionDrawable downShop = new TextureRegionDrawable(shopTexture);
        ImageButton shopButton = new ImageButton(upShop, downShop);

        // Entering the Inventory Button -- need to add the inventory button
        Texture inventoryTexture = new Texture(Gdx.files.internal("images/inventory.png"));
        TextureRegionDrawable downInventory = new TextureRegionDrawable(inventoryTexture);
        TextureRegionDrawable upInventory = new TextureRegionDrawable(inventoryTexture);
        ImageButton inventoryButton = new ImageButton(upInventory, downInventory);

        // the achievements button
        Texture achievementsTexture = new Texture(Gdx.files.internal("images/Achievements.png"));
        TextureRegionDrawable upAchievements = new TextureRegionDrawable(achievementsTexture);
        TextureRegionDrawable downAchievements = new TextureRegionDrawable(achievementsTexture);
        ImageButton achievementsButton = new ImageButton(upAchievements, downAchievements);

        // Triggers an event when the button is pressed.
        inventoryButton.addListener(
                new ChangeListener() {
                    @Override
                    public void changed(ChangeEvent changeEvent, Actor actor) {
                        logger.debug("Inventory button clicked");
                        entity.getEvents().trigger("inventory");
                        if(checkHiden == false){
                            table1.setVisible(true);
                            checkHiden = !checkHiden;
                        }
                    }
                });

        // Trigger for an achievement
        achievementsButton.addListener(
                new ChangeListener() {
                    @Override
                    public void changed(ChangeEvent changeEvent, Actor actor) {
                        logger.debug("Achievement button clicked");
                        entity.getEvents().trigger("achievement");
                    }
                });


        // trigger for shop button
        shopButton.addListener(
                new ChangeListener() {
                    @Override
                    public void changed(ChangeEvent changeEvent, Actor actor) {
                        logger.debug("Shop button clicked");
                        entity.getEvents().trigger("shop");
                    }
                });

        rightSideTable.add(inventoryButton).right().bottom().size(150f, 150f);
        // adding building button to the right
        leftSideTable.add(shopButton).left().bottom().size(150f, 150f);
        // adding settings to the left
        leftSideTable.add(achievementsButton).left().bottom().size(100f, 100f);
        table1.add(inventoryFrame);
        table1.setVisible(false);

        stage.addActor(leftSideTable);
        stage.addActor(rightSideTable);
        stage.addActor(table1);
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
        table1.clear();
        super.dispose();
    }
}
