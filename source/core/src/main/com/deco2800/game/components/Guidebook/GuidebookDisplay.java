package com.deco2800.game.components.Guidebook;

import com.badlogic.gdx.Gdx;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.deco2800.game.components.shop.ShopBuildingDisplay;
import com.deco2800.game.components.shop.ShopUtils;
import com.deco2800.game.services.ServiceLocator;
import com.deco2800.game.ui.UIComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Array;
import java.util.ArrayList;


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
        book.center();

        float screenHeight = Gdx.graphics.getHeight();
        float screenWidth = Gdx.graphics.getWidth();

        float bookHeight = screenHeight * 0.8f;
        float bookWidth = bookHeight * 1.618f;

        float pageHeight = 0.8f * bookHeight;

      //Background Colour
//      Texture colour = new Texture(Gdx.files.internal("images/guidebook-open.png"));
//      Drawable backgroundColour = new TextureRegionDrawable(colour);

      Image bookImage = new Image(ServiceLocator.getResourceService().getAsset("images/guidebook-open.png", Texture.class));

      Texture titleTexture = new Texture(Gdx.files.internal("images/uiElements/exports/guidebook-heading-frame.png"));
      TextureRegionDrawable titleUp = new TextureRegionDrawable(titleTexture);
      TextureRegionDrawable titleDown = new TextureRegionDrawable(titleTexture);
      TextButton title = ShopUtils.createImageTextButton(
              "Enter Page Title",
               skin.getColor("black"),
               "button", 1f, titleUp, titleDown, skin, true);

      String labelContent = "This is just random words to fill the page\n, hopefully in the future it will be content for the game :) This is just random words to fill the page, hopefully in the future it will be content for the game :) This is just random words to fill the page, hopefully in the future it will be content for the game :)This is just random words to fill the page, hopefully in the future it will be content for the game :)";
      int pixelsPerCharacter = 10;
      int charactersPerLine = (int) (0.7 * 0.5 * bookWidth / pixelsPerCharacter);
      labelContent = format(labelContent, charactersPerLine);
      Label leftContent = new Label(labelContent, skin, "small");
      Label leftContent2 = new Label(labelContent, skin, "small");
      Label rightContent = new Label(labelContent, skin, "small");

      leftContent.setAlignment(Align.topLeft);
      rightContent.setAlignment(Align.topLeft);
      leftContent2.setAlignment(Align.topLeft);

      Table page1 = new Table();
      page1.debug();
      page1.setSize(bookWidth, pageHeight);
      page1.setPosition((screenWidth - bookWidth) / 2f, (screenHeight - pageHeight) / 2f);

      Table leftPage = new Table();
      leftPage.debug();
      leftPage.setSize(0.5f * bookWidth, bookHeight);
      leftPage.add(leftContent).expandX().fillX().expandY().fillY().pad(0.07f * bookWidth).padTop(0f).padBottom(0f);

      Table rightPage = new Table();
      rightPage.debug();
      rightPage.setSize(0.5f * bookWidth, bookHeight);
      rightPage.add(rightContent).expandX().fillX().expandY().fillY().top().pad(0.07f * bookWidth).padTop(0f).padBottom(0f);

      page1.row().fillX().expandX().fillY().expandY();
      page1.add(leftPage);
      page1.add(rightPage);


      book.add(bookImage).center().size(bookWidth, bookHeight);
      book.add(page1);

      stage.addActor(book);
      stage.addActor(page1);
    }
    
    public String format(String labelContent, int numberPerLine) {
        StringBuilder formattedString = new StringBuilder();
        int count = 0;
        for (String word: labelContent.split(" ")) {
            if (count + word.length() >= numberPerLine) {
                formattedString.append('\n');
                count = 0;
            }
            formattedString.append(word).append(" ");
            count += word.length() + 1;
        }
        return formattedString.toString();
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
