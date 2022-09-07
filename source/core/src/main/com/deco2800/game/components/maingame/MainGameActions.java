package com.deco2800.game.components.maingame;

import com.deco2800.game.AtlantisSinks;
import com.deco2800.game.components.CombatStatsComponent;
import com.deco2800.game.components.Component;
import com.deco2800.game.components.player.InventoryComponent;
import com.deco2800.game.entities.Entity;
import com.deco2800.game.memento.CareTaker;
import com.deco2800.game.memento.Memento;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class listens to events relevant to the Main Game Screen and does
 * something when one of the
 * events is triggered.
 */
public class MainGameActions extends Component {
  private static final Logger logger = LoggerFactory.getLogger(MainGameActions.class);
  private AtlantisSinks game;
  private CareTaker playerStatus;
  private Entity player;

  public MainGameActions(AtlantisSinks game, CareTaker playerStatus, Entity player) {
    this.playerStatus = playerStatus;
    this.player = player;
    this.game = game;
  }

  @Override
  public void create() {
    entity.getEvents().addListener("exit", this::onExit);
    entity.getEvents().addListener("shop", this::openShop);
  }

  /**
   * Swaps to the Main Menu screen.
   */
  private void onExit() {
    logger.info("Exiting main game screen");
    game.setScreen(AtlantisSinks.ScreenType.MAIN_MENU, null);
  }

  /**
   * swaps screen to the shop screen, saves player's current status to the
   * caretaker to retrieve when entering shop
   * screen
   */
  private void openShop() {
    logger.info("Exiting main game screen");
    Memento currentStatus = new Memento(playerStatus.getAll().size(),
        player.getComponent(InventoryComponent.class).getGold(),
        player.getComponent(InventoryComponent.class).getStone(),
        player.getComponent(InventoryComponent.class).getWood(),
        player.getComponent(CombatStatsComponent.class).getHealth(),
        player.getComponent(InventoryComponent.class).getItems(),
        player.getComponent(CombatStatsComponent.class).getBaseAttack());
    playerStatus.add(currentStatus);
    game.setScreen(AtlantisSinks.ScreenType.SHOP, this.playerStatus);
  }

  private void openBuildingShop() {
    logger.info("Entering Building Shop");
    game.setScreen(AtlantisSinks.ScreenType.BUILD_SHOP, this.playerStatus);
  }
}
