package com.deco2800.game.components.mainmenu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.*;
import com.deco2800.game.services.ServiceLocator;
import com.deco2800.game.ui.UIComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A ui component for displaying the Main menu.
 */
public class MainMenuDisplay extends UIComponent {
  private static final Logger logger = LoggerFactory.getLogger(MainMenuDisplay.class);
  private static final float Z_INDEX = 2f;
  private Table rootTable;


  @Override
  public void create() {
    super.create();
    addActors();
  }

  private void addActors() {

    rootTable = new Table();
    rootTable.setFillParent(true);

    Table settingsTable = new Table();

    Table mainTable = new Table();
    Image title =
        new Image(
            ServiceLocator.getResourceService()
                .getAsset("images/uiElements/exports/title.png", Texture.class));


    // Background Colour
    Texture colour = new Texture(Gdx.files.internal("images/uiElements/exports/background.png"));
    Drawable backgroundColour = new TextureRegionDrawable(colour);
    rootTable.setBackground(backgroundColour);


    // inserting home Button
    Texture homeButton1 = new Texture(Gdx.files.internal("images/uiElements/exports/start_button.png"));
    TextureRegionDrawable homeUp = new TextureRegionDrawable(homeButton1);
    TextureRegionDrawable homeDown = new TextureRegionDrawable(homeButton1);
    ImageButton homeButton = new ImageButton(homeUp, homeDown);

    // inserting settings Button
    Texture settingsButton1 = new Texture(Gdx.files.internal("images/uiElements/exports/settings.png"));
    TextureRegionDrawable settingsUp = new TextureRegionDrawable(settingsButton1);
    TextureRegionDrawable settingsDown = new TextureRegionDrawable(settingsButton1);
    ImageButton settingsButton = new ImageButton(settingsUp, settingsDown);

    // inserting exit Button
    Texture exitButton1 = new Texture(Gdx.files.internal("images/uiElements/exports/exit_button.png"));
    TextureRegionDrawable exitUp = new TextureRegionDrawable(exitButton1);
    TextureRegionDrawable exitDown = new TextureRegionDrawable(exitButton1);
    ImageButton exitButton = new ImageButton(exitUp, exitDown);

//    TextButton startBtn = new TextButton("Start", skin);
//    TextButton loadBtn = new TextButton("Load", skin);
//    TextButton settingsBtn = new TextButton("Settings", skin);
//    TextButton exitBtn = new TextButton("Exit", skin);

    // Triggers an event when the button is pressed
    homeButton.addListener(
        new ChangeListener() {
          @Override
          public void changed(ChangeEvent changeEvent, Actor actor) {
            logger.debug("Start button clicked");
            entity.getEvents().trigger("start");
          }
        });

//    loadButton.addListener(
//        new ChangeListener() {
//          @Override
//          public void changed(ChangeEvent changeEvent, Actor actor) {
//            logger.debug("Load button clicked");
//            entity.getEvents().trigger("load");
//          }
//        });

    settingsButton.addListener(
        new ChangeListener() {
          @Override
          public void changed(ChangeEvent changeEvent, Actor actor) {
            logger.debug("Settings button clicked");
            entity.getEvents().trigger("settings");
          }
        });

    exitButton.addListener(
        new ChangeListener() {
          @Override
          public void changed(ChangeEvent changeEvent, Actor actor) {
            logger.debug("Exit button clicked");
            entity.getEvents().trigger("exit");
          }
        });

    mainTable.add(title).padBottom(50f);
    mainTable.row();
    mainTable.add(homeButton);
    mainTable.row();
    mainTable.add(exitButton);

    settingsTable.add(settingsButton).expandX().expandY().right().bottom().pad(0f, 0f, 0f, 0f);

    rootTable.add(mainTable).expandX();
    rootTable.row();
    rootTable.add(settingsTable).fillX();

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
    super.dispose();
  }
}
