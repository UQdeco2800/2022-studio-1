package com.deco2800.game.components.maingame;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextTooltip;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.deco2800.game.ui.UIComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Displays a button to exit the Main Game screen to the Main Menu screen.
 */
public class MainGameExitDisplay extends UIComponent {
  private static final Logger logger = LoggerFactory.getLogger(MainGameExitDisplay.class);
  private static final float Z_INDEX = 2f;
  private Table table;

  @Override
  public void create() {
    super.create();
    addActors();
  }

  private void addActors() {
    table = new Table();
    table.top().right();
    table.setFillParent(true);

    // Entering the back button
    Texture backTexture = new Texture(Gdx.files.internal("images/backButton.png"));
    Texture backTextureHover = new Texture(Gdx.files.internal("images/backButton_hover.png"));
    TextureRegionDrawable upBack = new TextureRegionDrawable(backTexture);
    TextureRegionDrawable downBack = new TextureRegionDrawable(backTexture);
    TextureRegionDrawable checkedBack = new TextureRegionDrawable(backTextureHover);
    ImageButton backButton = new ImageButton(upBack, downBack, checkedBack);

    // Triggers an event when the button is pressed.
    backButton.addListener(
        new ClickListener() {
          @Override
          public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
            logger.debug("Exit button clicked");
            if (enabled) {
              entity.getEvents().trigger("exit");
            }

            return true;
          }
        });
    // Adds hover state to button
    backButton.addListener(
        new InputListener() {
          @Override
          public void enter(InputEvent event, float x, float y, int pointer, Actor actor) {
            backButton.setChecked(true);
          }

          @Override
          public void exit(InputEvent event, float x, float y, int pointer, Actor actor) {
            backButton.setChecked(false);
          }
        });
    backButton.addListener(
        new TextTooltip("Back to Main Menu", skin));

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
          public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
            logger.debug("Settings button clicked on game page");
            logger.info("Game paused");
            entity.getEvents().trigger("settings");
            return true;
          }
        });
    // Adds hover state to button
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
        new TextTooltip("  Settings", skin));

    // Inserting load Button
    Texture loadTexture = new Texture(Gdx.files.internal("images/loadButton.png"));
    Texture loadTextureHover = new Texture(Gdx.files.internal("images/loadButton_hover.png"));
    TextureRegionDrawable upLoad = new TextureRegionDrawable(loadTexture);
    TextureRegionDrawable downLoad = new TextureRegionDrawable(loadTexture);
    TextureRegionDrawable checkedLoad = new TextureRegionDrawable(loadTextureHover);
    ImageButton loadButton = new ImageButton(upLoad, downLoad, checkedLoad);

    // Triggers load event when button is pressed
    loadButton.addListener(
        new ClickListener() {
          @Override
          public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
            logger.debug("Load button clicked on game page");
            entity.getEvents().trigger("load");
            return true;
          }
        });
    // Adds hover state to button
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
        });
    loadButton.addListener(
        new TextTooltip("Load Game", skin));

    // Inserting save Button
    Texture saveTexture = new Texture(Gdx.files.internal("images/saveButton.png"));
    Texture saveTextureHover = new Texture(Gdx.files.internal("images/saveButton_hover.png"));
    TextureRegionDrawable upSave = new TextureRegionDrawable(saveTexture);
    TextureRegionDrawable downSave = new TextureRegionDrawable(saveTexture);
    TextureRegionDrawable checkedSave = new TextureRegionDrawable(saveTextureHover);
    ImageButton saveButton = new ImageButton(upSave, downSave, checkedSave);

    // Triggers save event when button is pressed
    saveButton.addListener(
        new ClickListener() {
          @Override
          public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
            logger.debug("Save button clicked on game page");
            entity.getEvents().trigger("save");
            return true;
          }
        });
    // Adds hover state to button
    saveButton.addListener(
        new InputListener() {
          @Override
          public void enter(InputEvent event, float x, float y, int pointer, Actor actor) {
            saveButton.setChecked(true);
          }

          @Override
          public void exit(InputEvent event, float x, float y, int pointer, Actor actor) {
            saveButton.setChecked(false);
          }
        });
    saveButton.addListener(
        new TextTooltip("Save Game", skin));

    table.add(saveButton).size(50f).pad(5);
    table.add(loadButton).size(50f).pad(5);
    table.add(settingsButton).size(50f).pad(5);
    table.add(backButton).size(50f).pad(5);
    table.row();
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
