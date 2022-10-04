package com.deco2800.game.components.player;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.math.*;
import com.deco2800.game.areas.terrain.TerrainComponent;
import com.deco2800.game.areas.terrain.TerrainFactory;
import com.deco2800.game.components.CameraComponent;
import com.deco2800.game.components.CombatStatsComponent;
import com.deco2800.game.components.maingame.MainGameBuildingInterface;
import com.deco2800.game.entities.Entity;
import com.deco2800.game.entities.NpcService;
import com.deco2800.game.entities.factories.CrystalFactory;
import com.deco2800.game.input.InputComponent;
import com.deco2800.game.memento.Originator;
import com.deco2800.game.services.ServiceLocator;
import com.deco2800.game.utils.math.Vector2Utils;


import java.util.*;

/**
 * Input handler for the player for keyboard and touch (mouse) input.
 * This input handler only uses keyboard input.
 */
public class KeyboardPlayerInputComponent extends InputComponent {
  private final Vector2 walkDirection = Vector2.Zero.cpy();

  private boolean buildState = false;
  private boolean removeState = false;
  private boolean upgradeState = false;

  private boolean resourceBuildState = false;

  private boolean buildEvent = false;
  private boolean removeEvent = false;

  private boolean upgradeEvent = false;

  private String[] structureNames = {"wall", "tower1", "tower2", "tower3", "trap", "stonequarry", "woodCutter"};

  private int structureSelect = 0;

  public KeyboardPlayerInputComponent() {
    super(5);
  }

  /**
   * Triggers player events on specific keycodes.
   *
   * @return whether the input was processed
   * @see InputProcessor#keyDown(int)
   */
  @Override
  public boolean keyDown(int keycode) {
    switch (keycode) {
      case Keys.W:
        walkDirection.add(Vector2Utils.UP);
        entity.getEvents().trigger("ch_dir_w");
        triggerWalkEvent();
        entity.getEvents().trigger("playerControlTut", "UP");
        return true;
      case Keys.A:
        walkDirection.add(Vector2Utils.LEFT);
        entity.getEvents().trigger("ch_dir_a");
        triggerWalkEvent();
        entity.getEvents().trigger("playerControlTut", "LEFT");
        return true;
      case Keys.S:
        walkDirection.add(Vector2Utils.DOWN);
        entity.getEvents().trigger("ch_dir_s");
        triggerWalkEvent();
        entity.getEvents().trigger("playerControlTut", "DOWN");
        return true;
      case Keys.D:
        walkDirection.add(Vector2Utils.RIGHT);
        entity.getEvents().trigger("ch_dir_d");
        triggerWalkEvent();
        entity.getEvents().trigger("playerControlTut", "RIGHT");
        return true;
      case Keys.E:
        entity.getEvents().trigger("weapons");
        return true;
      case Keys.SPACE:
        entity.getEvents().trigger("attack");
        entity.getEvents().trigger("attack_anim");
        entity.getEvents().trigger("playerControlTut", "SPACE");
        return true;
      default:
        return false;
    }
  }

  /**
   * Triggers player events on specific keycodes.
   *
   * @return whether the input was processed
   * @see InputProcessor#keyUp(int)
   */
  @Override
  public boolean keyUp(int keycode) {
    switch (keycode) {
      case Keys.Q:
        entity.setScale(11f, 10.5f);
        entity.getEvents().trigger("playerDeath");
        return true;
      case Keys.W:
        walkDirection.sub(Vector2Utils.UP);
        triggerWalkEvent();
        return true;
      case Keys.A:
        walkDirection.sub(Vector2Utils.LEFT);
        triggerWalkEvent();
        return true;
      case Keys.S:
        walkDirection.sub(Vector2Utils.DOWN);
        triggerWalkEvent();
        return true;
      case Keys.D:
        walkDirection.sub(Vector2Utils.RIGHT);
        triggerWalkEvent();
        return true;
      case Keys.B:
        buildState = ServiceLocator.getStructureService().toggleBuildState(buildState);
        return true;
      case Keys.O:
        triggerCrystalAttacked();
        return true;
      case Keys.R:
        triggerCrystalRestoreHealth();
        return true;
      case Keys.N:
        if (buildState) {
          structureSelect += 1;
        }
        return true;
      case Keys.H:
        for(int i = 0; i <=10; i++) {
//          for(int j = 0; j<=120; j++) {
          Vector2 pos = ServiceLocator.getEntityService().getNamedEntity("terrain").getComponent(TerrainComponent.class).tileToWorldPosition(i,0);
          System.out.println(pos);
        }
        for(int i = 0; i <=10; i++) {
//          for(int j = 0; j<=120; j++) {
          Vector2 pos = ServiceLocator.getEntityService().getNamedEntity("terrain").getComponent(TerrainComponent.class).tileToWorldPosition(i,1);
          System.out.println(pos);
        }
//        }
      case Keys.Y:
        if (buildState) {
          buildState = ServiceLocator.getStructureService().toggleBuildState(buildState);
        }
        if (upgradeState) {
          upgradeState = ServiceLocator.getStructureService().toggleUpgradeState(upgradeState);
        }
        removeState = ServiceLocator.getStructureService().toggleRemoveState(removeState);
        return true;
      case Keys.U:
        if (buildState) {
          buildState = ServiceLocator.getStructureService().toggleBuildState(buildState);
        }
        if (removeState) {
          removeState = ServiceLocator.getStructureService().toggleRemoveState(removeState);
        }
        upgradeState = ServiceLocator.getStructureService().toggleUpgradeState(upgradeState);
        return true;
      case Keys.SPACE:
        entity.getEvents().trigger("attack_anim_rev");
        return true;
      default:
        return false;
    }
  }

  /** @see InputProcessor#touchDown(int, int, int, int) */
  @Override
  public boolean touchDown(int screenX, int screenY, int pointer, int button) {
    CrystalFactory.crystalClicked(screenX, screenY);
    NpcService.npcClicked(screenX,screenY);
    return true;
  }

  @Override
  public boolean mouseMoved(int screenX, int screenY) {

//    Entity camera = ServiceLocator.getEntityService().getNamedEntity("camera");
//    CameraComponent camComp = camera.getComponent(CameraComponent.class);
//    Vector3 mousePos = camComp.getCamera().unproject(new Vector3(screenX, screenY, 0));
//    Vector2 mousePosV2 = new Vector2(mousePos.x, mousePos.y);
//    System.out.println(mousePosV2);
//    GridPoint2 tilePos = ServiceLocator.getEntityService().getNamedEntity("terrain").getComponent(TerrainComponent.class).worldToTilePosition(mousePosV2.x, mousePosV2.y);
//    System.out.println(tilePos);

    return true;
  }

  /** @see InputProcessor#touchUp(int, int, int, int) */
  @Override
  public boolean touchUp(int screenX, int screenY, int pointer, int button) {
    if (pointer == Input.Buttons.LEFT) {
      if (buildState) {
        buildEvent = true;
        boolean isClear = false;
        boolean[] updatedValues = ServiceLocator.getStructureService().handleClicks(screenX, screenY, resourceBuildState, buildEvent, removeEvent, upgradeEvent);
        isClear = updatedValues[0];
        resourceBuildState = updatedValues[1];
        buildEvent = updatedValues[2];
        if (isClear) {
          int i = structureSelect % (structureNames.length);
          ServiceLocator.getStructureService().triggerBuildEvent(structureNames[i]);
        }
      } else if (removeState) {
        removeEvent = true;
        boolean isClear = false;
        boolean[] updatedValues = ServiceLocator.getStructureService().handleClicks(screenX, screenY, resourceBuildState, buildEvent, removeEvent, upgradeEvent);
        isClear = updatedValues[0];
        removeEvent = updatedValues[3];
      } else if (upgradeState) {
        upgradeEvent = true;
        boolean isClear = false;
        boolean[] updatedValues = ServiceLocator.getStructureService().handleClicks(screenX, screenY, resourceBuildState, buildEvent, removeEvent, upgradeEvent);
        isClear = updatedValues[0];
        upgradeEvent = updatedValues[4];
      }
    }

    if (buildState) {
      if (buildEvent) {
        if (pointer == Input.Buttons.LEFT) {
          buildEvent = false;
        }
      }
    }
    return true;
  }

  private void triggerWalkEvent() {
    if (walkDirection.epsilonEquals(Vector2.Zero)) {
      entity.getEvents().trigger("walkStop");
    } else {
      entity.getEvents().trigger("walk", walkDirection);
    }
  }

  /**
   * Damages crystal to imitate crystal being attacked (for testing purposes)
   */
  private void triggerCrystalAttacked() {
    Entity crystal = ServiceLocator.getEntityService().getNamedEntity("crystal");
    CombatStatsComponent combatStatsComponent = crystal.getComponent(CombatStatsComponent.class);
    int health = combatStatsComponent.getHealth();
    combatStatsComponent.setHealth(health - 30);
    // System.out.println(crystal.getComponent(CombatStatsComponent.class).getHealth());
  }




  /**
   * Triggers crystal restore health to can be used in the shopping feature (for
   * testing purposes)
   */
  private void triggerCrystalRestoreHealth() {
    Entity crystal = ServiceLocator.getEntityService().getNamedEntity("crystal");
    CombatStatsComponent combatStatsComponent = crystal.getComponent(CombatStatsComponent.class);
    InventoryComponent inventoryComponent = entity.getComponent(InventoryComponent.class);
    int gold = inventoryComponent.getGold();
    int health = combatStatsComponent.getHealth();
    int maxHealth = combatStatsComponent.getMaxHealth();
    if (maxHealth - health >= 50) {
      if (gold >= 5) {
        inventoryComponent.setGold(gold - 5);
        combatStatsComponent.setHealth(health + 50);
      } else {
        System.out.println("Gold insufficient");
      }
    } else if (maxHealth - health >= 40) {
      if (gold >= 4) {
        inventoryComponent.setGold(gold - 4);
        combatStatsComponent.setHealth(health + 40);
      } else {
        System.out.println("Gold insufficient");
      }
    } else if (maxHealth - health >= 30) {
      if (gold >= 3) {
        inventoryComponent.setGold(gold - 3);
        combatStatsComponent.setHealth(health + 30);
      } else {
        System.out.println("Gold insufficient");
      }
    } else if (maxHealth - health >= 20) {
      if (gold >= 2) {
        inventoryComponent.setGold(gold - 2);
        combatStatsComponent.setHealth(health + 20);
      } else {
        System.out.println("Gold insufficient");
      }
    } else if (maxHealth - health >= 10) {
      if (gold >= 1) {
        inventoryComponent.setGold(gold - 1);
        combatStatsComponent.setHealth(health + 10);
      } else {
        System.out.println("Gold insufficient");
      }
    } else {
      System.out.println("Crystal has reached max health");
    }
    // System.out.println(inventoryComponent.getGold());
  }
}
