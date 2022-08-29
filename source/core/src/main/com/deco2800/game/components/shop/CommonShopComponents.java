package com.deco2800.game.components.shop;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.deco2800.game.components.player.InventoryComponent;
import com.deco2800.game.ui.UIComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Displays a button to exit the Main Game screen to the Main Menu screen.
 */
public class CommonShopComponents extends UIComponent {
    private static final Logger logger = LoggerFactory.getLogger(CommonShopComponents.class);
    private static final float Z_INDEX = 2f;

    private float width;
    private float height;

    private Label title;

    private TextureRegionDrawable stoneUp;
    private TextButton stoneFrame;
    private Texture stoneTexture;

    private TextureRegionDrawable goldUp;
    private TextButton goldFrame;
    private Texture goldTexture;

    private TextureRegionDrawable exitUp;
    private Texture exitTexture;
    private TextButton exitButton;

    @Override
    public void create() {
        super.create();
        addActors();
    }

    private void addActors() {
        width = stage.getWidth();
        height = stage.getHeight();

        stoneTexture = new Texture(Gdx.files.internal("images/border_stone.png"));
        stoneUp = new TextureRegionDrawable(stoneTexture);
        // TODO change gold coins to stone count in inventory when available
        stoneFrame = ShopUtils.createImageTextButton(
                Integer.toString(entity.getComponent(InventoryComponent.class).getStone()) + "    ",
                skin.getColor("black"),
                "title", 1f, stoneUp, stoneUp, skin, true);
        stoneFrame.setTransform(true);
        stoneFrame.getLabel().setScale(3f);
        stoneFrame.setSize(200, 200);
        stoneFrame.setPosition(1100, 700);
        stoneFrame.setColor(216, 189, 151, 10);

        goldTexture = new Texture(Gdx.files.internal("images/border_coin.png"));
        goldUp = new TextureRegionDrawable(goldTexture);
        goldFrame = ShopUtils.createImageTextButton(
                Integer.toString(entity.getComponent(InventoryComponent.class).getGold()) + "    ",
                skin.getColor("black"),
                "title", 1f, goldUp, goldUp, skin, true);
        goldFrame.setTransform(true);
        goldFrame.getLabel().setScale(3f);
        goldFrame.setSize(200, 200);
        goldFrame.setPosition(1100, 780);
        exitTexture = new Texture(Gdx.files.internal("images/Home_Button.png"));
        exitUp = new TextureRegionDrawable(exitTexture);
        exitButton = ShopUtils.createImageTextButton("EXIT", skin.getColor("black"), "title", 1f, exitUp,
                exitUp,
                skin, false);
        exitButton.setPosition(width * 0.85f, height * 0.85f);
        exitButton.addListener(
                new ChangeListener() {
                    @Override
                    public void changed(ChangeEvent changeEvent, Actor actor) {
                        logger.info("Exit button clicked");
                        entity.getEvents().trigger("exit");
                    }
                });

        title = new Label("SHOP", skin, "title");
        title.setPosition(width * 0.05f, height * 0.90f);
        title.setFontScale(4f);
        title.setColor(skin.getColor("black"));
        stage.addActor(title);
        stage.addActor(stoneFrame);
        stage.addActor(goldFrame);
        stage.addActor(exitButton);
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

    public TextButton getGoldButton() {
        return this.goldFrame;
    }

    public TextButton getStoneButton() {
        return this.stoneFrame;
    }
}