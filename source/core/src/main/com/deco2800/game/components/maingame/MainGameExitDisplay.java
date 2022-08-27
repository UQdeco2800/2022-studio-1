package com.deco2800.game.components.maingame;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
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
  private Table table2;
  private Table table3;


  @Override
  public void create() {
    super.create();
    addActors();
  }

  private void addActors() {
    table3 = new Table();
    table3.bottom().left();
    table2 = new Table();
    //table2.bottom().right();
    table = new Table();
    table.top().right();
    table.setFillParent(true);

    //TextButton mainMenuBtn = new TextButton("Exit", skin);

    //Entering the Shop Button
    Texture shopTexture = new Texture(Gdx.files.internal("images/uiElements/buttons/Shop_Button.png"));
    TextureRegionDrawable upShop = new TextureRegionDrawable(shopTexture);
    TextureRegionDrawable downShop = new TextureRegionDrawable(shopTexture);
    ImageButton shopButton = new ImageButton(upShop,downShop);

    //Entering the Inventory Button -- need to add the inventory button
    Texture inventoryTexture = new Texture(Gdx.files.internal("images/uiElements/buttons/Shop_Button.png"));
    TextureRegionDrawable upInventory = new TextureRegionDrawable(inventoryTexture);
    TextureRegionDrawable downInventory = new TextureRegionDrawable(inventoryTexture);
    ImageButton inventoryButton = new ImageButton(upInventory,downInventory);

    //Entering the settings button
    Texture backTexture = new Texture(Gdx.files.internal("images/backButton.png"));
    TextureRegionDrawable upBack = new TextureRegionDrawable(backTexture);
    TextureRegionDrawable downBack = new TextureRegionDrawable(backTexture);
    ImageButton backButton = new ImageButton(upBack,downBack);

    //Entering the settings button
    Texture buildingTexture = new Texture(Gdx.files.internal("images/Building_Button.png"));
    TextureRegionDrawable upBuilding = new TextureRegionDrawable(buildingTexture);
    TextureRegionDrawable downBuilding = new TextureRegionDrawable(buildingTexture);
    ImageButton buildingButton = new ImageButton(upBuilding,downBuilding);


    // Triggers an event when the button is pressed.
    backButton.addListener(
      new ChangeListener() {
        @Override
        public void changed(ChangeEvent changeEvent, Actor actor) {
          logger.debug("Exit button clicked");
          entity.getEvents().trigger("exit");
        }
      });

    table.add(backButton).size(50f).pad(5);
    table.row();
    table3.add(shopButton).left().bottom();
    table2.add(inventoryButton);
    table2.add(buildingButton);

    stage.addActor(table);
    stage.addActor(table2);
    stage.addActor(table3);
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
