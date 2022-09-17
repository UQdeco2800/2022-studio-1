package com.deco2800.game.components.Guidebook;

import com.badlogic.gdx.Gdx;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.deco2800.game.components.shop.ShopBuildingDisplay;
import com.deco2800.game.ui.UIComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GuidebookDisplay extends UIComponent {
    private static final Logger logger = LoggerFactory.getLogger(GuidebookDisplay.class);
    private static final float Z_INDEX = 2f;

    private boolean visability;



    @Override
    public void create() {
        super.create();
        addActors();
    }

    private void addActors() {

    }

    public Table GuidebookPopup () {
        Table book = new Table();
        Table page1 = new Table();

        book.add(page1);

        stage.addActor(book);
        return book;
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
        stage.clear();
        super.dispose();
    }
}
