package com.deco2800.game.components.shop;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
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

    Table table1;
    Table table2;
    Table table3;
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
    private TextureRegionDrawable woodUp;
    private TextButton woodFrame;
    private Texture woodTexture;

    @Override
    public void create() {
        super.create();
        addActors();
    }

    private void addActors() {

        table1 = new Table();
        table1.setFillParent(true);
        table1.top().left();

        table2 = new Table();
        table2.setFillParent(true);
        table2.top().right();

        table3 = new Table();
        table3.setFillParent(true);
        table3.top().right().padTop(150).padRight(250);

        stoneTexture = new Texture(Gdx.files.internal("images/border_stone.png"));
        stoneUp = new TextureRegionDrawable(stoneTexture);
        // TODO change gold coins to stone count in inventory when available
        stoneFrame = ShopUtils.createImageTextButton(
                Integer.toString(entity.getComponent(InventoryComponent.class).getStone()) + "    ",
                skin.getColor("black"),
                "button", 1f, stoneUp, stoneUp, skin, true);

        stoneFrame.setColor(216, 189, 151, 10);

        goldTexture = new Texture(Gdx.files.internal("images/border_coin.png"));
        goldUp = new TextureRegionDrawable(goldTexture);
        goldFrame = ShopUtils.createImageTextButton(
                Integer.toString(entity.getComponent(InventoryComponent.class).getGold()) + "    ",
                skin.getColor("black"),
                "button", 1f, goldUp, goldUp, skin, true);

        woodTexture = new Texture(Gdx.files.internal("images/border.png"));
        woodUp = new TextureRegionDrawable(woodTexture);
        woodFrame = ShopUtils.createImageTextButton(
                Integer.toString(entity.getComponent(InventoryComponent.class).getWood()) + "    ",
                skin.getColor("black"),
                "button", 1f, woodUp, woodUp, skin, true);

        exitTexture = new Texture(Gdx.files.internal("images/Home_Button.png"));
        exitUp = new TextureRegionDrawable(exitTexture);
        exitButton = ShopUtils.createImageTextButton("EXIT", skin.getColor("black"), "title", 1f, exitUp,
                exitUp,
                skin, false);

        exitButton.addListener(
                new ChangeListener() {
                    @Override
                    public void changed(ChangeEvent changeEvent, Actor actor) {
                        logger.info("Exit button clicked");
                        entity.getEvents().trigger("exit");
                    }
                });

        title = new Label("SHOP", skin, "title");
        title.setFontScale(3f);
        title.setColor(skin.getColor("black"));
        table1.add(title).pad(10, 75, 0, 0);
        table3.add(stoneFrame).width(200).height(150);// .pad(250, 0, 0, 400);
        table3.add(goldFrame).width(200).height(150);// pad(250, 0, 0, 400);
        table3.add(woodFrame).width(200).height(150);// pad(250, 0, 0, 400);
        table2.add(exitButton).top().right().pad(10);
        stage.addActor(table3);
        stage.addActor(table1);
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
        super.dispose();
    }

    public TextButton getGoldButton() {
        return this.goldFrame;
    }

    public TextButton getStoneButton() {
        return this.stoneFrame;
    }
}