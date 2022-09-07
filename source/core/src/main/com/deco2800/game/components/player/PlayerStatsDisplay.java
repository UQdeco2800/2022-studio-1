package com.deco2800.game.components.player;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.deco2800.game.components.CombatStatsComponent;
import com.deco2800.game.components.HealthBarComponent;
import com.deco2800.game.entities.Entity;
import com.deco2800.game.services.ServiceLocator;
import com.deco2800.game.ui.UIComponent;

/**
 * A ui component for displaying player stats, e.g. health.
 */
public class PlayerStatsDisplay extends UIComponent {
  Table table;
  private Image heartImage;
  private Image healthBarImage;
  private Image coinImage;
  private Label coinLabel;
  private Image crystalImage;
  private ProgressBar progressBar;
  private Image crystalBarImage;
  private Label crystalLabel;

  private Image stoneCurrencyImage;
  //private Label stoneCurrencyLabel;
  public static Label stoneCurrencyLabel;

  public static int stoneCount = 0;
  Entity crystal = ServiceLocator.getEntityService().getNamedEntity("crystal");


  /**
   * Creates reusable ui styles and adds actors to the stage.
   */
  @Override
  public void create() {
    super.create();
    addActors();
    //will be used to update health
    //entity.getEvents().addListener("updateHealth", this::updatePlayerHealthUI);
    crystal.getEvents().addListener("updateHealth", this::updateCrystalHealthUI);

//    if(crystal.getComponent(CombatStatsComponent.class).getHealth() == 900){
//      healthBarImage.remove();
//    }

  }



  /**
   * Creates actors and positions them on the stage using a table.
   * @see Table for positioning options
   */
  private void addActors() {
    table = new Table();
    table.top().left();
    table.setFillParent(true);
    table.padTop(45f).padLeft(5f);

    //Coin image
    coinImage = new Image(ServiceLocator.getResourceService().getAsset("images/uiElements/exports/coin.png", Texture.class));

    //Coin text - set as 0, for placeholder
    int coin = entity.getComponent(InventoryComponent.class).getGold();
    CharSequence coinText = String.format("x %d", coin);
    coinLabel = new Label(coinText, skin, "large");

    // Heart image
    heartImage = new Image(ServiceLocator.getResourceService().getAsset("images/uiElements/exports/heart.png", Texture.class));

    //Health Bar Image
    healthBarImage = new Image(ServiceLocator.getResourceService().getAsset("images/healthBar.png", Texture.class ));
    // Health text level - grabbing percentile - to populate health bar
    //int health = entity.getComponent(CombatStatsComponent.class).getHealth();

    //Crystal image
    crystalImage =  new Image(ServiceLocator.getResourceService().getAsset("images/uiElements/exports/crystal.png", Texture.class));

    //Crystal bar
    progressBar = crystal.getComponent(HealthBarComponent.class).getProgressBar();
    crystalBarImage = new Image(ServiceLocator.getResourceService().getAsset("images/healthBar.png", Texture.class ));
    //crystal health text
    int crystalHealth = crystal.getComponent(CombatStatsComponent.class).getHealth();
    CharSequence healthText = String.format("%d", crystalHealth);
    crystalLabel = new Label(healthText, skin, "large");


    //Stone image
    stoneCurrencyImage = new Image(ServiceLocator.getResourceService().getAsset("images/uiElements/exports/stoneSuperior.png", Texture.class));

    //Stone text. 0 as an initial set up
    int stone = entity.getComponent(InventoryComponent.class).getStone();
   // CharSequence stoneCount = String.format("x %d", stone);
    stoneCurrencyLabel = new Label(String.valueOf(stoneCount), skin, "large");





    table.add(heartImage).pad(5);
    table.add(healthBarImage).size(200f, 30f).pad(5);
    table.row();
    table.add(crystalImage);
    //table.add(crystalBarImage).size(190f,30f).pad(5);
    table.stack(crystalBarImage,progressBar).size(190f,30f).pad(5);
    //table.add(progressBar).size(190f,30f).pad(5);
    table.add(crystalLabel);
    table.row();
    table.add(coinImage);
    table.add(coinLabel).pad(0,0,0,0).left();
    table.row();
    table.add(stoneCurrencyImage);
    table.add(stoneCurrencyLabel).pad(0,0,0,0).left();
    
    table.row();
    stage.addActor(table);
  }

  @Override
  public void draw(SpriteBatch batch)  {
    // draw is handled by the stage
  }

  public void updateCrystalHealthUI(int health) {
    CharSequence text = String.format("%d", health);
    crystalLabel.setText(text);
  }


  @Override
  public void dispose() {
    super.dispose();
    heartImage.remove();
    coinLabel.remove();
    coinImage.remove();
    healthBarImage.remove();
//    crystalBarImage.remove();
    crystalImage.remove();
    crystalLabel.remove();
    progressBar.remove();
    stoneCurrencyImage.remove();
    stoneCurrencyLabel.remove();
  }
}
