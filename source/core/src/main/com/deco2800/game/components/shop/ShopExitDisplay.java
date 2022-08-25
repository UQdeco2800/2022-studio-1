package com.deco2800.game.components.shop;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.deco2800.game.ui.UIComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Displays a button to exit the Main Game screen to the Main Menu screen.
 */
public class ShopExitDisplay extends UIComponent {
    private static final Logger logger = LoggerFactory.getLogger(ShopExitDisplay.class);
    private static final float Z_INDEX = 2f;
    private Table table;

    private TextureRegionDrawable up;

    private TextureRegionDrawable down;
    private Texture returnTexture;

    private Texture shopTexture;

    private Image shop_background;

    @Override
    public void create() {
        super.create();
        addActors();
    }

    private void addActors() {
        table = new Table();
        table.top().right();
        table.setFillParent(true);

        returnTexture = new Texture(Gdx.files.internal("images/uiElements/buttons/Home_Button.png"));
        up = new TextureRegionDrawable(returnTexture);
        down = new TextureRegionDrawable(returnTexture);
        ImageButton backBtn = new ImageButton(up,down);

        shopTexture = new Texture(Gdx.files.internal("images/shop-interface.png"));
        shop_background = new Image(shopTexture);
        shop_background.setPosition(480,300);
        stage.addActor(shop_background);

        // Triggers an event when the button is pressed.
        backBtn.addListener(
                new ChangeListener() {
                    @Override
                    public void changed(ChangeEvent changeEvent, Actor actor) {
                        logger.debug("Exit button clicked");
                        entity.getEvents().trigger("exit");
                    }
                });
        table.add(backBtn).padTop(10f).padRight(10f);
        stage.addActor(table);
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
        super.dispose();
    }
}

