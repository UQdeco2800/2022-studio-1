package com.deco2800.game.components.maingame;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.deco2800.game.areas.ForestGameArea;
import com.deco2800.game.areas.MainArea;
import com.deco2800.game.components.achievements.AchievementInterface;
import com.deco2800.game.ui.UIComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Displays a button to exit the Main Game screen to the Main Menu screen.
 */
public class MainGameInterface extends UIComponent {
  private static final Logger logger = LoggerFactory.getLogger(MainGameExitDisplay.class);
  private static final float Z_INDEX = 2f;
  private Table leftSideTable;
  private Table rightSideTable;

  @Override
  public void create() {
    super.create();
    addActors();
  }

  private void addActors() {
    rightSideTable = new Table();
    rightSideTable.bottom().right();
    rightSideTable.setFillParent(true);

    leftSideTable = new Table();
    leftSideTable.bottom().left();
    leftSideTable.setFillParent(true);

    // Entering the Shop Button
    Texture shopTexture = new Texture(Gdx.files.internal("images/Shop.png"));
    Texture shopTextureOnClick = new Texture(Gdx.files.internal("images/shop_onClick.png"));
    TextureRegionDrawable upShop = new TextureRegionDrawable(shopTexture);
    TextureRegionDrawable downShop = new TextureRegionDrawable(shopTexture);
    TextureRegionDrawable shopChecked = new TextureRegionDrawable(shopTextureOnClick);
    ImageButton shopButton = new ImageButton(upShop, downShop, shopChecked);

    shopButton.addListener(
        new InputListener() {
          @Override
          public void enter(InputEvent event, float x, float y, int pointer, Actor actor) {
            shopButton.setChecked(true);
          }

          @Override
          public void exit(InputEvent event, float x, float y, int pointer, Actor actor) {
            shopButton.setChecked(false);
          }
        });

    // Entering the Building Button -- need to add the building button
    Texture buildingTexture = new Texture(Gdx.files.internal("images/build-button.png"));
    Texture buildingOnClickTexture = new Texture(Gdx.files.internal("images/build-button.png"));
    TextureRegionDrawable downBuilding = new TextureRegionDrawable(buildingTexture);
    TextureRegionDrawable upBuilding = new TextureRegionDrawable(buildingTexture);
    TextureRegionDrawable buildingChecked = new TextureRegionDrawable(buildingOnClickTexture);
    ImageButton buildingButton = new ImageButton(upBuilding, downBuilding, buildingChecked);

    buildingButton.addListener(
        new InputListener() {
          @Override
          public void enter(InputEvent event, float x, float y, int pointer, Actor actor) {
            buildingButton.setChecked(true);
          }

          @Override
          public void exit(InputEvent event, float x, float y, int pointer, Actor actor) {
            buildingButton.setChecked(false);
          }
        });

    // Entering the Inventory Button -- need to add the inventory button
    Texture inventoryTexture = new Texture(Gdx.files.internal("images/inventory.png"));
    Texture inventoryOnClickTexture = new Texture(Gdx.files.internal("images/inventory_OnClick.png"));
    TextureRegionDrawable downInventory = new TextureRegionDrawable(inventoryTexture);
    TextureRegionDrawable upInventory = new TextureRegionDrawable(inventoryTexture);
    TextureRegionDrawable inventoryChecked = new TextureRegionDrawable(inventoryOnClickTexture);
    ImageButton inventoryButton = new ImageButton(upInventory, downInventory, inventoryChecked);

    inventoryButton.addListener(
        new InputListener() {
          @Override
          public void enter(InputEvent event, float x, float y, int pointer, Actor actor) {
            inventoryButton.setChecked(true);
          }

          @Override
          public void exit(InputEvent event, float x, float y, int pointer, Actor actor) {
            inventoryButton.setChecked(false);
          }
        });

    // the achievements button
    Texture achievementsTexture = new Texture(Gdx.files.internal("images/achievemnets_onclick.png"));
    Texture achievementsTextureOnClick = new Texture(Gdx.files.internal("images/Achievements.png"));
    TextureRegionDrawable upAchievements = new TextureRegionDrawable(achievementsTexture);
    TextureRegionDrawable downAchievements = new TextureRegionDrawable(achievementsTexture);
    TextureRegionDrawable achievementsChecked = new TextureRegionDrawable(achievementsTextureOnClick);
    ImageButton achievementsButton = new ImageButton(upAchievements, downAchievements, achievementsChecked);

    // Adds hover state to achievements
    achievementsButton.addListener(
        new InputListener() {
          @Override
          public void enter(InputEvent event, float x, float y, int pointer, Actor actor) {
            achievementsButton.setChecked(true);
          }

          @Override
          public void exit(InputEvent event, float x, float y, int pointer, Actor actor) {
            achievementsButton.setChecked(false);
          }
        });

    // Guidebook button
    Texture guideBookTexture = new Texture(Gdx.files.internal("images/guidebook.png"));
    Texture guideBookTextureCheck = new Texture(Gdx.files.internal("images/guideBookCheck.png"));
    TextureRegionDrawable upGuidebook = new TextureRegionDrawable(guideBookTexture);
    TextureRegionDrawable downGuidebook = new TextureRegionDrawable(guideBookTexture);
    TextureRegionDrawable guidebookCheck = new TextureRegionDrawable(guideBookTextureCheck);
    ImageButton guideBookButton = new ImageButton(upGuidebook, downGuidebook, guidebookCheck);

    // Adds hover state to achievements
    guideBookButton.addListener(
        new InputListener() {
          @Override
          public void enter(InputEvent event, float x, float y, int pointer, Actor actor) {
            guideBookButton.setChecked(true);
          }

          @Override
          public void exit(InputEvent event, float x, float y, int pointer, Actor actor) {
            guideBookButton.setChecked(false);
          }
        });

    // trigger for guidebook
    guideBookButton.addListener(new ClickListener() {
      @Override
      public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
        logger.debug("Guidebook button clicked");
        entity.getEvents().trigger("guideBook");
        return true;
      }
    });

    // Triggers an event when the button is pressed.
    inventoryButton.addListener(
        new ClickListener() {
          @Override
          public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
            logger.debug("Inventory button clicked");
            entity.getEvents().trigger(MainGameActions.EVENT_CLOSE_ALL);
            entity.getEvents().trigger("inventory");
            return true;
          }
        });

    // Triggers building popup when the button is pressed.
    buildingButton.addListener(
        new ClickListener() {
          @Override
          public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
            logger.debug("Building button clicked");
            entity.getEvents().trigger(MainGameActions.EVENT_CLOSE_ALL);
            entity.getEvents().trigger("buildingShop");
            return true;
          }
        });

    // Trigger for an achievement
    achievementsButton.addListener(
        new ClickListener() {
          @Override
          public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
            logger.debug("Achievement button clicked");
            entity.getEvents().trigger(MainGameActions.EVENT_CLOSE_ALL);
            entity.getEvents().trigger(AchievementInterface.EVENT_OPEN_ACHIEVEMENTS);
            return true;
          }
        });

    // trigger for shop button
    shopButton.addListener(
        new ClickListener() {
          @Override
          public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
            logger.debug("Shop button clicked");
            entity.getEvents().trigger(MainGameActions.EVENT_CLOSE_ALL);
            entity.getEvents().trigger("shop");
            ((ForestGameArea) MainArea.getInstance().getGameArea()).playShopMusic();
            return true;
          }
        });

    rightSideTable.add(guideBookButton).right().bottom().size(100f, 100f);

    rightSideTable.add(guideBookButton).right().bottom().size(100f, 100f);
    rightSideTable.add(buildingButton).right().bottom().size(150f, 150f);
    rightSideTable.add(inventoryButton).right().bottom().size(150f, 150f);
    // adding building button to the right
    leftSideTable.add(shopButton).left().bottom().size(150f, 150f);
    // adding settings to the left
    leftSideTable.add(achievementsButton).left().bottom().size(100f, 100f);

    stage.addActor(leftSideTable);
    stage.addActor(rightSideTable);
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
    super.dispose();
  }
}
