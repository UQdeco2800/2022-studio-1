package com.deco2800.game.components.mainmenu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.deco2800.game.services.ServiceLocator;
import com.deco2800.game.ui.UIComponent;
import jdk.jfr.Label;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.xpath.XPath;

/**
 * A ui component for displaying the Main menu.
 */
public class MainMenuDisplay extends UIComponent {
  private static final Logger logger = LoggerFactory.getLogger(MainMenuDisplay.class);
  private static final float Z_INDEX = 2f;
  private Table table;
  private TextureRegionDrawable homeUp;
  private TextureRegionDrawable homeDown;
  private Texture homeButton;

  private TextureRegionDrawable loadUp;
  private TextureRegionDrawable loadDown;
  private Texture loadButton;

  private TextureRegionDrawable settingsUp;
  private TextureRegionDrawable settingsDown;
  private Texture settingsButton;

  private TextureRegionDrawable exitUp;

  private TextureRegionDrawable exitDown;

  private Texture exitButton;


  @Override
  public void create() {
    super.create();
    addActors();
  }

  private void addActors() {
    table = new Table();
    table.setFillParent(true);
    Image title =
        new Image(
            ServiceLocator.getResourceService()
                .getAsset("images/atlantissinkstitle.png", Texture.class));

    // Team 10 - TJ's attempt for background colour
    // backgroundColour = new backgroundColour(Gdx.files.internal("images/atlantisBasicBackground.png"));
    // backgroundColour.setColor(0, 0, 128); // r, g, b, a
    // table.setBackground(backgroundColour);

    // inserting home Button
    homeButton = new Texture(Gdx.files.internal("images/uiElements/exports/start_button_with_text.png"));
    homeUp = new TextureRegionDrawable(homeButton);
    homeDown = new TextureRegionDrawable(homeButton);
    ImageButton homeButton = new ImageButton(homeUp,homeDown);

    // inserting load Button
    loadButton = new Texture(Gdx.files.internal("images/Home_Button.png"));
    loadUp = new TextureRegionDrawable(loadButton);
    loadDown = new TextureRegionDrawable(loadButton);
    ImageButton loadButton = new ImageButton(loadUp, loadDown);

    // inserting settings Button
    settingsButton = new Texture(Gdx.files.internal("images/uiElements/exports/settings.png"));
    settingsUp = new TextureRegionDrawable(settingsButton);
    settingsDown = new TextureRegionDrawable(settingsButton);
    ImageButton settingsButton = new ImageButton(settingsUp, settingsDown);

    // inserting exit Button
    exitButton = new Texture(Gdx.files.internal("images/uiElements/exports/exit_button.png"));
    exitUp = new TextureRegionDrawable(exitButton);
    exitDown = new TextureRegionDrawable(exitButton);
    ImageButton exitButton = new ImageButton(exitUp, exitDown);

    TextButton startBtn = new TextButton("Start", skin);
    TextButton loadBtn = new TextButton("Load", skin);
    TextButton settingsBtn = new TextButton("Settings", skin);
    TextButton exitBtn = new TextButton("Exit", skin);

    // Triggers an event when the button is pressed
    homeButton.addListener(
        new ChangeListener() {
          @Override
          public void changed(ChangeEvent changeEvent, Actor actor) {
            logger.debug("Start button clicked");
            entity.getEvents().trigger("start");
          }
        });

    loadButton.addListener(
        new ChangeListener() {
          @Override
          public void changed(ChangeEvent changeEvent, Actor actor) {
            logger.debug("Load button clicked");
            entity.getEvents().trigger("load");
          }
        });

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


    table.add(title).size(1000f,400f);
    table.row();
    table.add(homeButton).padTop(15f);
    table.row();
    table.add(loadButton).padTop(15f);
    table.row();
    table.add(settingsButton).padTop(15f);
    table.row();
    table.add(exitButton).padTop(15f);

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
