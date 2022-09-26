package com.deco2800.game.components.maingame;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.deco2800.game.components.CombatStatsComponent;
import com.deco2800.game.services.ServiceLocator;
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
    private Group group;
//    private Table table2;
//    private Table table3;
//    private Table table4;

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

        group = new Group();
//        table1.setSize(800f,800f);

//        table2 = new Table();
//        table2.center().setPosition();
//        table2.setFillParent(true);

        Texture inventoryInterfaceTexture = new Texture(Gdx.files.internal("images/popup-border.png"));
        TextureRegionDrawable inventory = new TextureRegionDrawable(inventoryInterfaceTexture);
        ImageButton inventoryFrame = new ImageButton(inventory,inventory);
        inventoryFrame.setSize(800f,800f);
        inventoryFrame.setPosition(Gdx.graphics.getWidth() / 2 - inventoryFrame.getWidth() / 2,
                Gdx.graphics.getHeight() / 2 - inventoryFrame.getHeight() / 2);

        Texture crossTexture = new Texture(Gdx.files.internal("images/cross.png"));
        TextureRegionDrawable cross = new TextureRegionDrawable(crossTexture);
        ImageButton crossFrame = new ImageButton(cross,cross);
        crossFrame.setSize(40f,40f);
        crossFrame.setPosition(inventoryFrame.getWidth() - 400f, inventoryFrame.getHeight() - 150f);

        Label subtitle = new Label("Inventory", skin, "title");
        subtitle.setFontScale(1f);
        subtitle.setColor(skin.getColor("black"));
        subtitle.setPosition(inventoryFrame.getWidth() - 350f,inventoryFrame.getHeight() - 150f);

        Image heartImage = new Image(ServiceLocator.getResourceService().getAsset("images/uiElements/exports/heart.png", Texture.class));
        heartImage.setSize(40f,40f);
        heartImage.setPosition(inventoryFrame.getWidth(), inventoryFrame.getHeight() - 150f);

        Image healthBarImage = new Image(ServiceLocator.getResourceService().getAsset("images/healthBar.png", Texture.class));
//        int health = ServiceLocator.getStructureService().getNamedEntity("player").getComponent(CombatStatsComponent.class).getHealth();
//        Label healthAmount = new Label(Integer.toString(health), skin, "large");
        healthBarImage.setSize(200f,30f);
        healthBarImage.setPosition(inventoryFrame.getWidth() + 50f, inventoryFrame.getHeight() - 150f);

        Texture leftTexture = new Texture(Gdx.files.internal("images/box-border.png"));
        TextureRegionDrawable leftDrawable = new TextureRegionDrawable(leftTexture);
        ImageButton leftBox = new ImageButton(leftDrawable,leftDrawable);
        leftBox.setSize(350f,630f);
        leftBox.setPosition(inventoryFrame.getWidth() - 415f, inventoryFrame.getHeight() - 720f);

        Texture leftTitleTexture = new Texture(Gdx.files.internal("images/description-box.png"));
        TextureRegionDrawable leftTitleDrawable = new TextureRegionDrawable(leftTitleTexture);
        ImageButton leftTitleBox = new ImageButton(leftTitleDrawable,leftTitleDrawable);
        leftTitleBox.setSize(350f,60f);
        leftTitleBox.setPosition(leftBox.getWidth() + 25f, leftBox.getHeight() - 100f);

        Label leftTitle = new Label("Equipment", skin, "small");
        leftTitle.setColor(skin.getColor("black"));
        leftTitle.setPosition(leftBox.getWidth() + 150f, leftBox.getHeight() - 80f);

        Texture rightTexture = new Texture(Gdx.files.internal("images/box-border.png"));
        TextureRegionDrawable rightDrawable = new TextureRegionDrawable(rightTexture);
        ImageButton rightBox = new ImageButton(rightDrawable,rightDrawable);
        rightBox.setSize(350f,630f);
        rightBox.setPosition(inventoryFrame.getWidth() - 85f, inventoryFrame.getHeight() - 720f);

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
                        group.setVisible(true);
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

        crossFrame.addListener(
                new ChangeListener() {
                    @Override
                    public void changed(ChangeEvent changeEvent, Actor actor) {
                        logger.debug("Close inventory page");
                        group.setVisible(false);
                    }
                });

        rightSideTable.add(inventoryButton).right().bottom().size(150f, 150f);
        // adding building button to the right
        leftSideTable.add(shopButton).left().bottom().size(150f, 150f);
        // adding settings to the left
        leftSideTable.add(achievementsButton).left().bottom().size(100f, 100f);
        group.addActor(inventoryFrame);
        group.addActor(subtitle);
        group.addActor(heartImage);
        group.addActor(healthBarImage);
//        group.addActor(healthAmount);
        group.addActor(leftBox);
        group.addActor(rightBox);
        group.addActor(crossFrame);
        group.addActor(leftTitleBox);
        group.addActor(leftTitle);
        group.setVisible(false);

        stage.addActor(leftSideTable);
        stage.addActor(rightSideTable);
        stage.addActor(group);
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
        group.clear();
        super.dispose();
    }
}
