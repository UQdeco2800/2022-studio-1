package com.deco2800.game.components.Guidebook;

import com.badlogic.gdx.Gdx;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.deco2800.game.components.shop.ShopBuildingDisplay;
import com.deco2800.game.components.shop.ShopUtils;
import com.deco2800.game.services.ServiceLocator;
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


        // inserting back Button
        Texture backTexture = new Texture(Gdx.files.internal("images/backButton.png"));
        TextureRegionDrawable exitUp = new TextureRegionDrawable(backTexture);
        TextureRegionDrawable exitDown = new TextureRegionDrawable(backTexture);
        ImageButton backButton = new ImageButton(exitUp, exitDown);

        Texture titleTexture = new Texture(Gdx.files.internal("images/uiElements/exports/guidebook-heading-frame.png"));
        TextureRegionDrawable titleUp = new TextureRegionDrawable(titleTexture);
        TextureRegionDrawable titleDown = new TextureRegionDrawable(titleTexture);
        TextButton title = ShopUtils.createImageTextButton(
                "Enter Page Title",
                skin.getColor("black"),
                "button", 1f, titleUp, titleDown, skin, true);

        backButton.addListener(
                new ChangeListener() {
                    @Override
                    public void changed(ChangeEvent changeEvent, Actor actor) {
                        logger.debug("Back button clicked");
                        entity.getEvents().trigger("exit");
                    }
                });

        Table leftPage = new Table();
        leftPage.setFillParent(true);

        Table rightPage = new Table();
        rightPage.setFillParent(true);

        
        book.add(title).left().top().padLeft(20f);
        book.add(backButton).size(30f,30f).top().right();


        stage.addActor(book);

        System.out.println("Got the display");
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
