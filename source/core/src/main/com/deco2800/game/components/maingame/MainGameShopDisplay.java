package com.deco2800.game.components.maingame;

import com.deco2800.game.ui.UIComponent;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class MainGameShopDisplay extends UIComponent {
    private static final Logger logger = LoggerFactory.getLogger(MainGameShopDisplay.class);
    private static final float Z_INDEX = 2f;
    private Table table;

    @Override
    public void create() {
        super.create();
        addActors();
    }

    private void addActors() {
        table = new Table();
        table.bottom().right();
        table.setFillParent(true);


        TextButton shopButton = new TextButton("Shop", skin);

        // Triggers an event when the button is pressed.
        shopButton.addListener(
                new ChangeListener() {
                    @Override
                    public void changed(ChangeEvent changeEvent, Actor actor) {
                        logger.debug("Shop button clicked");
                        // will need to add a shop trigger to take to new page
                        // once shop is implemented
                        //entity.getEvents().trigger("shop");
                    }
                }
        );

        table.add(shopButton).padBottom(10f).padRight(10f);

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

