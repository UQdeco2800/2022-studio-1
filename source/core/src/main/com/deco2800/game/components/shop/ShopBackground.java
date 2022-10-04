package com.deco2800.game.components.shop;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.deco2800.game.ui.UIComponent;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Displays a button to exit the Main Game screen to the Main Menu screen.
 */
public class ShopBackground extends UIComponent {
    private static final Logger logger = LoggerFactory.getLogger(CommonShopComponents.class);
    private static final float Z_INDEX = 2f;
    Table rootTable;

    @Override
    public void create() {
        super.create();
        addActors();
    }

    private void addActors() {
        rootTable = new Table();
        rootTable.setFillParent(true);

        // Background Colour
        Texture colour = new Texture(Gdx.files.internal("images/shop-background.png"));
        Drawable backgroundColour = new TextureRegionDrawable(colour);
        rootTable.setBackground(backgroundColour);

        stage.addActor(rootTable);
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
        rootTable.clear();
        stage.clear();
        super.dispose();
    }
}