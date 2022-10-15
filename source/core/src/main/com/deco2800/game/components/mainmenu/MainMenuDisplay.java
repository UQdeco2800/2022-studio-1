package com.deco2800.game.components.mainmenu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
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
  private int currentStep = 1;


  @Override
  public void create() {
    super.create();
    addActors();
  }

  public void nextFrame() {
      currentStep = (currentStep % 55) + 1;
  }

  public Table getDisplay() {
      return rootTable;
  }

  public void updateDisplay() {
      if (rootTable == null) {
          rootTable = display();
          return;
      }
      Texture colour = new Texture(Gdx.files.internal("images/atlantis_background/atlantis_background (" + currentStep + ").png"));
      Drawable backgroundColour = new TextureRegionDrawable(colour);
      rootTable.setBackground(backgroundColour);
  }

  public Table display() {

      float screenHeight = Gdx.graphics.getHeight();

      float titleHeight = screenHeight * 0.55f;
      float buttonHeight = screenHeight * 0.1f;

      rootTable = new Table();
      rootTable.setFillParent(true);

      Table settingsTable = new Table();

      Table mainTable = new Table();
      Image title =
              new Image(
                      ServiceLocator.getResourceService()
                              .getAsset("images/uiElements/exports/title.png", Texture.class));

      // Background Colour
      Texture colour = new Texture(Gdx.files.internal("images/atlantis_background/atlantis_background (" + currentStep + ").png"));
      Drawable backgroundColour = new TextureRegionDrawable(colour);
      rootTable.setBackground(backgroundColour);

      // inserting home Button
      Texture homeButton1 = new Texture(Gdx.files.internal("images/uiElements/exports/start_button.png"));
      Texture homeButton2 = new Texture(Gdx.files.internal("images/uiElements/exports/start_button_hover.png"));
      TextureRegionDrawable homeUp = new TextureRegionDrawable(homeButton1);
      TextureRegionDrawable homeDown = new TextureRegionDrawable(homeButton1);
      TextureRegionDrawable homeChecked = new TextureRegionDrawable(homeButton2);
      ImageButton homeButton = new ImageButton(homeUp, homeDown, homeChecked);

      // inserting load Button TEXTURE IS STILL WIP
      Texture loadButton1 = new Texture(Gdx.files.internal("images/uiElements/exports/load_button.png"));
      Texture loadButton2 = new Texture(Gdx.files.internal("images/uiElements/exports/load_button_hover.png"));
      TextureRegionDrawable loadUp = new TextureRegionDrawable(loadButton1);
      TextureRegionDrawable loadDown = new TextureRegionDrawable(loadButton1);
      TextureRegionDrawable loadChecked = new TextureRegionDrawable(loadButton2);
      ImageButton loadButton = new ImageButton(loadUp, loadDown, loadChecked);

      // inserting exit Button
      Texture exitButton1 = new Texture(Gdx.files.internal("images/uiElements/exports/exit_button.png"));
      Texture exitButton2 = new Texture(Gdx.files.internal("images/offButton_OnClick.png"));
      TextureRegionDrawable exitUp = new TextureRegionDrawable(exitButton1);
      TextureRegionDrawable exitDown = new TextureRegionDrawable(exitButton1);
      TextureRegionDrawable exitButtonChecked = new TextureRegionDrawable(exitButton2);
      ImageButton exitButton = new ImageButton(exitUp, exitDown, exitButtonChecked);

      // Triggers an event when the button is pressed
      homeButton.addListener(
              new ClickListener() {
                  @Override
                  public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
                      logger.debug("Start button clicked");
                      entity.getEvents().trigger("start");
                      return true;
                  }
              });

      //Adds hover state to button
      homeButton.addListener(
              new InputListener() {
                  @Override
                  public void enter(InputEvent event, float x, float y, int pointer, Actor actor) {
                      homeButton.setChecked(true);
                  }

                  @Override
                  public void exit(InputEvent event, float x, float y, int pointer, Actor actor) {
                      homeButton.setChecked(false);
                  }
              });

      //triggers load event when pressed
      loadButton.addListener(
              new ClickListener(){
                  @Override
                  public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
                      logger.debug("Load button clicked");
                      entity.getEvents().trigger("load");
                      return true;
                  }
              }
      );

      //Adds hover state to button
      loadButton.addListener(
              new InputListener() {
                  @Override
                  public void enter(InputEvent event, float x, float y, int pointer, Actor actor) {
                      loadButton.setChecked(true);
                  }

                  @Override
                  public void exit(InputEvent event, float x, float y, int pointer, Actor actor) {
                      loadButton.setChecked(false);
                  }
              }
      );

      // Entering the system button
      Texture settingTexture = new Texture(Gdx.files.internal("images/settingsGame.png"));
      Texture settingTextureHover = new Texture(Gdx.files.internal("images/settingsGame_hover.png"));
      TextureRegionDrawable upSetting = new TextureRegionDrawable(settingTexture);
      TextureRegionDrawable downSetting = new TextureRegionDrawable(settingTexture);
      TextureRegionDrawable checkedSetting = new TextureRegionDrawable(settingTextureHover);
      ImageButton settingsButton = new ImageButton(upSetting, downSetting, checkedSetting);

      // Settings Button
      settingsButton.addListener(
              new ClickListener() {
                  @Override
                  public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
                      logger.debug("Settings button clicked on game page");
                      logger.info("Game paused");
                      entity.getEvents().trigger("settings");
                      return true;
                  }
              });
      //Adds hover state to button
      settingsButton.addListener(
              new InputListener() {
                  @Override
                  public void enter(InputEvent event, float x, float y, int pointer, Actor actor) {
                      settingsButton.setChecked(true);
                  }

                  @Override
                  public void exit(InputEvent event, float x, float y, int pointer, Actor actor) {
                      settingsButton.setChecked(false);
                  }
              });
      settingsButton.addListener(
              new TextTooltip("  Settings",skin)
      );

      exitButton.addListener(
              new ClickListener(){
                  @Override
                  public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
                      logger.debug("Exit button clicked");
                      entity.getEvents().trigger("exit");
                      return true;
                  }
              });

      exitButton.addListener(
              new InputListener() {
                  @Override
                  public void enter(InputEvent event, float x, float y, int pointer, Actor actor) {
                      exitButton.setChecked(true);
                  }

                  @Override
                  public void exit(InputEvent event, float x, float y, int pointer, Actor actor) {
                      exitButton.setChecked(false);
                  }
              }
      );

      mainTable.add(title).padTop(0.5f * buttonHeight).size(titleHeight * title.getWidth() / title.getHeight(), titleHeight);
      mainTable.row();
      mainTable.add(homeButton).size(buttonHeight * homeButton.getWidth() / homeButton.getHeight(), buttonHeight);
      mainTable.row();
      mainTable.add(loadButton).size(buttonHeight * loadButton.getWidth() / loadButton.getHeight(), buttonHeight);
      mainTable.row();
      mainTable.add(exitButton).size(buttonHeight * exitButton.getWidth() / exitButton.getHeight(), buttonHeight);

      settingsTable.add(settingsButton).expandX().expandY().right().bottom().size(50f,50f);

      rootTable.add(mainTable).expandX();
      rootTable.row();
      rootTable.add(settingsTable).fillX();

      stage.addActor(rootTable);
      return rootTable;
  }
  private void addActors() {
  }

  @Override
  public void draw(SpriteBatch batch) {
    // draw is handled by the stage
  }


  @Override
  public void dispose() {
    rootTable.clear();
    super.dispose();
  }
}
