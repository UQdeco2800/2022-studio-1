package com.deco2800.game.components.player;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.deco2800.game.components.CombatStatsComponent;
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
  private Image crystalBarImage;

  private Image stoneCurrencyImage;
  private Label stoneCurrencyLabel;



  /**
   * Creates reusable ui styles and adds actors to the stage.
   */
  @Override
  public void create() {
    super.create();
    addActors();
    //will be used to update health
    //entity.getEvents().addListener("updateHealth", this::updatePlayerHealthUI);
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
    float coinSideLenghth =30f;
    coinImage = new Image(ServiceLocator.getResourceService().getAsset("images/coin.png", Texture.class));

    //Coin text - set as 0, for placeholder
    int coin = 0;
    CharSequence coinText = String.format("Coins: %d", coin);
    coinLabel = new Label(coinText, skin, "large");

    // Heart image
    float heartSideLength = 30f;
    heartImage = new Image(ServiceLocator.getResourceService().getAsset("images/heart.png", Texture.class));

    //Health Bar Image
    healthBarImage = new Image(ServiceLocator.getResourceService().getAsset("images/healthBar.png", Texture.class ));
    // Health text level - grabbing percentile - to populate health bar
    int health = entity.getComponent(CombatStatsComponent.class).getHealth();

    //Crystal image
    float crystalSideLength =30f;
    crystalImage =  new Image(ServiceLocator.getResourceService().getAsset("images/crystal.png", Texture.class));

    //Crystal bar
    crystalBarImage = new Image(ServiceLocator.getResourceService().getAsset("images/healthBar.png", Texture.class ));

    //Stone image
    float stoneSize =30f;
    stoneCurrencyImage = new Image(ServiceLocator.getResourceService().getAsset("images/stone.png", Texture.class));

    //Stone text. 0 as an initial set up
    int stone = 0;
    CharSequence stoneCount = String.format("Stone: %d", stone);
    stoneCurrencyLabel = new Label(stoneCount, skin, "large");





    table.add(heartImage).size(heartSideLength).pad(5);
    table.add(healthBarImage).size(200f, 30f).pad(5);
    table.row();
    table.add(crystalImage).size(crystalSideLength);
    table.add(crystalBarImage).size(200f,30f).pad(5);
    table.row();
    table.add(coinImage).size(coinSideLenghth);
    table.add(coinLabel);
    table.row();
    table.add(stoneCurrencyImage).size(stoneSize);
    table.add(stoneCurrencyLabel);
    table.row();
    stage.addActor(table);
  }

  @Override
  public void draw(SpriteBatch batch)  {
    // draw is handled by the stage
  }



  @Override
  public void dispose() {
    super.dispose();
    heartImage.remove();
    coinLabel.remove();
    coinImage.remove();
    healthBarImage.remove();
    crystalBarImage.remove();
    crystalImage.remove();
    stoneCurrencyImage.remove();
    stoneCurrencyLabel.remove();
  }
}
