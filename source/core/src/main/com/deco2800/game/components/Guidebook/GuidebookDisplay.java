package com.deco2800.game.components.Guidebook;

import com.badlogic.gdx.Gdx;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.deco2800.game.components.shop.ShopBuildingDisplay;
import com.deco2800.game.ui.UIComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class GuidebookDisplay extends UIComponent {
    private static final Logger logger = LoggerFactory.getLogger(GuidebookDisplay.class);
    private static final float Z_INDEX = 2f;

    private boolean visability;

    private Table book;



    @Override
    public void create() {
        super.create();
        addActors();
    }

    private void addActors() {
        book = new Table();
        book.setFillParent(true);


        //Background Colour
        Texture colour = new Texture(Gdx.files.internal("images/guidebook-open.png"));
        Drawable backgroundColour = new TextureRegionDrawable(colour);
        book.setBackground(backgroundColour);

        // inserting exit Button
        Texture exitButton1 = new Texture(Gdx.files.internal("images/uiElements/exports/exit_button.png"));
        TextureRegionDrawable exitUp = new TextureRegionDrawable(exitButton1);
        TextureRegionDrawable exitDown = new TextureRegionDrawable(exitButton1);
        ImageButton exitButton = new ImageButton(exitUp, exitDown);

        exitButton.addListener(
                new ChangeListener() {
                    @Override
                    public void changed(ChangeEvent changeEvent, Actor actor) {
                        logger.debug("Exit button clicked");
                        entity.getEvents().trigger("exit");
                    }
                });

        book.add(exitButton);
        stage.addActor(book);
    }

//    public Table GuidebookPopup () {
//        Table book = new Table();
//        Table page1 = new Table();
//        //Background Colour
//        Texture colour = new Texture(Gdx.files.internal("images/uiElements/startscreen/guidebook-open.png"));
//        Drawable backgroundColour = new TextureRegionDrawable(colour);
//        book.setBackground(backgroundColour);
//
//        stage.addActor(book);
//        return book;
//    }

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
