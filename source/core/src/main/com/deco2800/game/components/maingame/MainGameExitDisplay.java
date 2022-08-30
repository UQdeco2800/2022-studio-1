package com.deco2800.game.components.maingame;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
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

    // TextButton mainMenuBtn = new TextButton("Exit", skin);

    // Entering the back button
    Texture backTexture = new Texture(Gdx.files.internal("images/backButton.png"));
    TextureRegionDrawable upBack = new TextureRegionDrawable(backTexture);
    TextureRegionDrawable downBack = new TextureRegionDrawable(backTexture);
    ImageButton backButton = new ImageButton(upBack, downBack);



    // Triggers an event when the button is pressed.
    backButton.addListener(
        new ChangeListener() {
          @Override
          public void changed(ChangeEvent changeEvent, Actor actor) {
            logger.debug("Exit button clicked");
            entity.getEvents().trigger("exit");
          }
        });

    // Entering the system button
    Texture settingTexture = new Texture(Gdx.files.internal("images/settingsGame.png"));
    TextureRegionDrawable upSetting = new TextureRegionDrawable(settingTexture);
    TextureRegionDrawable downSetting = new TextureRegionDrawable(settingTexture);
    ImageButton settingsButton = new ImageButton(upSetting, downSetting);

    // Settings Button
    settingsButton.addListener(
        new ChangeListener() {
          @Override
          public void changed(ChangeEvent changeEvent, Actor actor) {
            logger.debug("Settings button clicked on game page");
            logger.info("Game paused");
            entity.getEvents().trigger("setting game page");
          }
        });

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
