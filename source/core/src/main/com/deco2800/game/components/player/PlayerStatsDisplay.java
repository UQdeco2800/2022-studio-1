package com.deco2800.game.components.player;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.deco2800.game.areas.MainArea;
import com.deco2800.game.components.CombatStatsComponent;
import com.deco2800.game.components.HealthBarComponent;
import com.deco2800.game.entities.Entity;
import com.deco2800.game.entities.factories.CrystalFactory;
import com.deco2800.game.services.DayNightCycleService;
import com.deco2800.game.services.DayNightCycleStatus;
import com.deco2800.game.services.ServiceLocator;
import com.deco2800.game.ui.UIComponent;
import com.deco2800.game.utils.DrawableUtil;

import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

/**
 * A ui component for displaying player stats, e.g. health.
 */
public class PlayerStatsDisplay extends UIComponent {
  Table table;
  private Image heartImage;
  private Image healthBarImage;
  private ProgressBar healthprogressBar;

  private Image crystalImage;
  private ProgressBar progressBar;
  private Image crystalBarImage;
  private Label crystalLabel;

  private Image stoneCurrencyImage;
  private static Label stoneCurrencyLabel;

  private Image coinImage;
  private static Label coinLabel;

  private Image woodImage;
  private static Label woodLabel;

  DayNightCycleStatus status;


  Entity crystal;
  Entity health;
  private static int stoneCount = 0;

  //Entity resourceBuilding = ServiceLocator.getEntityService().getNamedEntity("stoneQuarry");

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
//    Entity healthBar = ServiceLocator.getStructureService().getNamedEntity("player");
//    healthprogressBar = healthBar.getComponent(HealthBarComponent.class).getProgressBar();
//    healthprogressBar.getStyle().background = DrawableUtil.getRectangularColouredDrawable(50, 15, Color.BROWN);
//    progressBar.getStyle().knob = DrawableUtil
//            .getRectangularColouredDrawable(0, 15, Color.RED);
//    progressBar.getStyle().knobBefore = DrawableUtil
//            .getRectangularColouredDrawable(50, 15, Color.RED);

    //Crystal image
    crystalImage =  new Image(ServiceLocator.getResourceService().getAsset("images/uiElements/exports/crystal.png", Texture.class));

    //Get crystal health bar and customise
    crystal = ServiceLocator.getEntityService().getNamedEntity("crystal");
    progressBar = crystal.getComponent(HealthBarComponent.class).getProgressBar();
            progressBar.getStyle().background = DrawableUtil
                    .getRectangularColouredDrawable(50, 15,  Color.BROWN);
            progressBar.getStyle().knob = DrawableUtil
                    .getRectangularColouredDrawable(0, 15, Color.VIOLET);
            progressBar.getStyle().knobBefore = DrawableUtil
                    .getRectangularColouredDrawable(50, 15, Color.VIOLET);

    crystalBarImage = new Image(ServiceLocator.getResourceService().getAsset("images/empty_healthbar.png", Texture.class ));
    //crystal health text
    int crystalHealth = crystal.getComponent(CombatStatsComponent.class).getHealth();
    CharSequence healthText = String.format("%d", crystalHealth);
    crystalLabel = new Label(healthText, skin, "large");

    //Stone image
    stoneCurrencyImage = new Image(ServiceLocator.getResourceService().getAsset("images/uiElements/exports/stoneSuperior.png", Texture.class));

    //Stone text. 0 as an initial set up
    int stone = entity.getComponent(InventoryComponent.class).getStone();
    CharSequence stoneCount = String.format("x %d", stone);

    stoneCurrencyLabel = new Label(String.valueOf(stoneCount), skin, "large");

   // wood counter
    woodImage = new Image(ServiceLocator.getResourceService().getAsset("images/log.png", Texture.class));

    int woodCountInt = entity.getComponent(InventoryComponent.class).getWood();
    CharSequence woodCount = String.format("x %d", woodCountInt);

    woodLabel = new Label(String.valueOf(woodCount), skin, "large");

    CrystalFactory.recoverCrystalHealth(crystal);

    table.add(heartImage).pad(5);
    // table.stack(healthprogressBar, healthBarImage).size(200f, 30f).pad(5);
    table.add(healthBarImage).size(200f, 30f).pad(5);
    table.row();
    table.add(crystalImage);
    table.stack(progressBar,crystalBarImage).size(190f,30f).pad(5);

    table.add(crystalLabel);
    table.row();
    table.add(coinImage);
    table.add(coinLabel).pad(0,0,0,0).left();
    table.row();
    table.add(stoneCurrencyImage);
    table.add(stoneCurrencyLabel).pad(0,0,0,0).left();
    table.row();
    table.add(woodImage).size(50f);
    table.add(woodLabel).left();

    
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

  public static void updateItems() {
    CharSequence stone = String.format("x %d", MainArea.getInstance().getGameArea().getPlayer().getComponent(InventoryComponent.class).getStone());
    stoneCurrencyLabel.setText(stone);
    CharSequence gold = String.format("x %d",  MainArea.getInstance().getGameArea().getPlayer().getComponent(InventoryComponent.class).getGold());
    coinLabel.setText(gold);
    CharSequence wood = String.format("x %d", MainArea.getInstance().getGameArea().getPlayer().getComponent(InventoryComponent.class).getWood());
    coinLabel.setText(wood);
  }

  public void updateResourceAmount() {
    CharSequence gold = String.format("x %d", entity.getComponent(InventoryComponent.class).getGold());
    coinLabel.setText(gold);

    CharSequence stone = String.format("x %d", entity.getComponent(InventoryComponent.class).getStone());
    stoneCurrencyLabel.setText(stone);

    CharSequence wood = String.format("x %d", entity.getComponent(InventoryComponent.class).getWood());
    woodLabel.setText(wood);
  }

  public static void updateStoneCountUI() {
    int stone = 0;
    HashMap<String, Entity> namedEntities = (HashMap<String, Entity>) ServiceLocator.getEntityService().getAllNamedEntities();
    int quarryCount = 0;
    for (Map.Entry<String, Entity> entry : namedEntities.entrySet()) {
        if (entry.getKey().contains("stoneQuarry")) {
          quarryCount += 1;
        }
    }
    stone = stoneCount + quarryCount * 100;
    stoneCount = stone;
    CharSequence count = String.format("%d", stone);
    stoneCurrencyLabel.setText(count);
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
    crystalLabel.remove();
    progressBar.remove();
    stoneCurrencyImage.remove();
    stoneCurrencyLabel.remove();
  }

//  /**
//   * Recover crystal health at dawn, day, and dusk
//   */
//  private void recoverCrystalHealth(DayNightCycleStatus partOfDay) {
//    System.out.println(partOfDay);
//    switch (partOfDay){
//      case DAWN:
//      case DAY:
//      case DUSK:
//        Timer time = new Timer();
//        TimerTask recoverCrystal = new TimerTask() {
//        @Override
//            public void run() {
//                // DayNightCycleStatus status =  ServiceLocator.getDayNightCycleService().getCurrentCycleStatus();
//                if (status != DayNightCycleStatus.NIGHT) {
//                    Entity crystal = ServiceLocator.getEntityService().getNamedEntity("crystal");
//                    CombatStatsComponent combatStatsComponent = crystal.getComponent(CombatStatsComponent.class);
//                    int health = combatStatsComponent.getHealth();
//                    combatStatsComponent.setHealth(health + 10);
//                }
//            }
//        };
//        time.scheduleAtFixedRate(recoverCrystal, 5000, 5000);
//        break;
//      case NIGHT:
//      case NONE:
//        break;
//    }
//  }
}
