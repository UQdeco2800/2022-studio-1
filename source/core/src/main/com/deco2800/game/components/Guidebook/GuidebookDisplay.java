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
        book.center();



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

      String labelContent = "This is just random words to fill the page, hopefully in the future it will be content for the game :)";
      labelContent = lineAmount(labelContent);
      Label sampleContent = new Label(labelContent, skin, "large");



      Table page1 = new Table();
      page1.setFillParent(true);
      page1.center();

      Table leftPage = new Table();
      leftPage.setFillParent(true);
      leftPage.left();
      leftPage.padLeft(100f).padRight(100f);

      Table rightPage = new Table();
      rightPage.setFillParent(true);
      rightPage.right();
      rightPage.padRight(100f);

      rightPage.add(title).center();
      rightPage.row();
      rightPage.add(sampleContent).right();

      leftPage.add(title);
      leftPage.row();
      rightPage.add(sampleContent).left();

      page1.add(rightPage);
      page1.add(leftPage);


      book.add(bookImage).center().size(1770f, 900f);
      book.add(page1);

      stage.addActor(book);
    }

    public String lineAmount(String labelContent) {
        String str = labelContent;
        if(labelContent.length() >= 50) {
           str = labelContent.replace("(.{0,50}\b)","$1\n");
        }
        return str;
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
