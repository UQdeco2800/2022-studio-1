package com.deco2800.game.components.maingame;

import com.deco2800.game.AtlantisSinks;
import com.deco2800.game.components.CombatStatsComponent;
import com.deco2800.game.components.Component;
import com.deco2800.game.components.player.InventoryComponent;
import com.deco2800.game.entities.Entity;
import com.deco2800.game.files.SaveGame;
import com.deco2800.game.memento.CareTaker;
import com.deco2800.game.memento.Memento;
import com.deco2800.game.services.DayNightCycleService;
import com.deco2800.game.services.DayNightCycleStatus;
import com.deco2800.game.services.ServiceLocator;
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
  private Entity player;

  public MainGameActions(AtlantisSinks game, Entity player) {
    this.player = player;
    this.game = game;
  }

  @Override
  public void create() {
    entity.getEvents().addListener("exit", this::onExit);
    entity.getEvents().addListener("settings", this::onSettings);
    entity.getEvents().addListener("guideBook", this::openGuidebook);
    entity.getEvents().addListener("achievement", this::onAchievements);
    entity.getEvents().addListener("save", this::onSave);
    entity.getEvents().addListener("load", this::onLoad);
    ServiceLocator.getDayNightCycleService().getEvents().addListener(DayNightCycleService.EVENT_PART_OF_DAY_PASSED,
        this::onFirstNight);
  }

  private void onDeath() {
    logger.info("Testing epilogue screen");
    CareTaker.deleteAll();
    game.setScreen(AtlantisSinks.ScreenType.STORY_LINE_EPILOGUE);
  }

  /**
   * Swaps to the Main Menu screen.
   */
  private void onExit() {
    logger.info("Exiting main game screen");
    CareTaker.deleteAll();
    game.setScreen(AtlantisSinks.ScreenType.MAIN_MENU);
  }

  private void openGuidebook() {
    CareTaker playerStatus = CareTaker.getInstance();
    Memento currentStatus = new Memento(playerStatus.size(),
        player.getComponent(InventoryComponent.class).getGold(),
        player.getComponent(InventoryComponent.class).getStone(),
        player.getComponent(InventoryComponent.class).getWood(),
        player.getComponent(CombatStatsComponent.class).getHealth(),
        player.getComponent(InventoryComponent.class).getItems(),
        player.getComponent(InventoryComponent.class).getBuildings(),
        player.getComponent(CombatStatsComponent.class).getBaseAttack(),
        player.getComponent(CombatStatsComponent.class).getBaseDefense(),
        player.getComponent(InventoryComponent.class).getWeapon(),
        player.getComponent(InventoryComponent.class).getArmor(),
        player.getComponent(InventoryComponent.class).getEquipmentList());
    playerStatus.add(currentStatus);
    ServiceLocator.getDayNightCycleService().pause();

    logger.info("Exiting main game screen");
    game.setScreen(AtlantisSinks.ScreenType.GUIDEBOOK);
  }

  /**
   * Intended for saving a game state.
   * WIP!!!
   * Save functionality is not actually implemented.
   */
  private void onSave() {
    logger.info("Save game starting");
    SaveGame.saveGameState();
    logger.info("Save game finished");
  }

  /**
   * Load the game state from the playable state
   */
  private void onLoad() {
    logger.info("Load game staring");
    CareTaker.deleteAll();
    game.setScreen(AtlantisSinks.ScreenType.MAIN_GAME_LOAD);
    SaveGame.loadGameState();
    logger.info("Load game finished");
  }

  /**
   * Swaps to Settings screen
   */
  private void onSettings() {
    logger.info("Launching settings screen");

    Memento currentStatus = new Memento(CareTaker.getInstance().size(),
        player.getComponent(InventoryComponent.class).getGold(),
        player.getComponent(InventoryComponent.class).getStone(),
        player.getComponent(InventoryComponent.class).getWood(),
        player.getComponent(CombatStatsComponent.class).getHealth(),
        player.getComponent(InventoryComponent.class).getItems(),
        player.getComponent(InventoryComponent.class).getBuildings(),
        player.getComponent(CombatStatsComponent.class).getAttackMultiplier(),
        player.getComponent(CombatStatsComponent.class).getBaseDefense(),
        player.getComponent(InventoryComponent.class).getWeapon(),
        player.getComponent(InventoryComponent.class).getArmor(),
        player.getComponent(InventoryComponent.class).getEquipmentList());
    CareTaker.getInstance().add(currentStatus);
    game.setSettingsScreen(AtlantisSinks.ScreenType.MAIN_GAME);
  }

  private void onFirstNight(DayNightCycleStatus partOfDay) {
    switch (partOfDay) {
      case DAWN:
        break;
      case DAY:
        // testing purpose only
        // game.setScreen(AtlantisSinks.ScreenType.STORY_LINE_EPILOGUE);
        break;
      case DUSK:
        break;
      case NIGHT:
        // logger.info("Playing first night scene");
        // implement game data save functionality
        // ServiceLocator.getDayNightCycleService().pause();
        // game.setScreen(AtlantisSinks.ScreenType.FIRST_NIGHT);
        break;
    }
  }

  private void onAchievements() {
    logger.info("Exiting main game screen");

    CareTaker playerStatus = CareTaker.getInstance();
    Memento currentStatus = new Memento(playerStatus.size(),
        player.getComponent(InventoryComponent.class).getGold(),
        player.getComponent(InventoryComponent.class).getStone(),
        player.getComponent(InventoryComponent.class).getWood(),
        player.getComponent(CombatStatsComponent.class).getHealth(),
        player.getComponent(InventoryComponent.class).getItems(),
        player.getComponent(InventoryComponent.class).getBuildings(),
        player.getComponent(CombatStatsComponent.class).getBaseAttack(),
        player.getComponent(CombatStatsComponent.class).getBaseDefense(),
        player.getComponent(InventoryComponent.class).getWeapon(),
        player.getComponent(InventoryComponent.class).getArmor(),
        player.getComponent(InventoryComponent.class).getEquipmentList());
    playerStatus.add(currentStatus);

    ServiceLocator.getDayNightCycleService().pause();
    game.setScreen(AtlantisSinks.ScreenType.ACHIEVEMENT);
  }
}
