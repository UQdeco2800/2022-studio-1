package com.deco2800.game.components.player;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.deco2800.game.components.CameraComponent;
import com.deco2800.game.components.CombatStatsComponent;
import com.deco2800.game.components.maingame.MainGameBuildingInterface;
import com.deco2800.game.entities.Entity;
import com.deco2800.game.entities.factories.CrystalFactory;
import com.deco2800.game.input.InputComponent;
import com.deco2800.game.memento.Originator;
import com.deco2800.game.services.ServiceLocator;
import com.deco2800.game.utils.math.Vector2Utils;
import net.dermetfan.gdx.physics.box2d.PositionController;

import java.util.*;



/**
 * Input handler for the player for keyboard and touch (mouse) input.
 * This input handler only uses keyboard input.
 */
public class KeyboardPlayerInputComponent extends InputComponent {
  private final Vector2 walkDirection = Vector2.Zero.cpy();

  private boolean buildState = false;
  private boolean resourceBuildState = false;

  private boolean buildEvent = false;

  private SortedMap<String, Rectangle> structureRects = new TreeMap<>();

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
        return true;
      case Keys.A:
        walkDirection.add(Vector2Utils.LEFT);
        entity.getEvents().trigger("ch_dir_a");
        triggerWalkEvent();
        return true;
      case Keys.S:
        walkDirection.add(Vector2Utils.DOWN);
        entity.getEvents().trigger("ch_dir_s");
        triggerWalkEvent();
        return true;
      case Keys.D:
        walkDirection.add(Vector2Utils.RIGHT);
        entity.getEvents().trigger("ch_dir_d");
        triggerWalkEvent();
        return true;
      case Keys.SPACE:
        entity.getEvents().trigger("attack");
        entity.getEvents().trigger("attack_anim");
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
      case Keys.U:
        triggerCrystalUpgrade();
        return true;
      case Keys.R:
        triggerCrystalRestoreHealth();
        return true;
      case Keys.N:
        resourceBuildState = ServiceLocator.getStructureService().toggleResourceBuildState(resourceBuildState);
        return true;
      case Keys.SPACE:
        return true;
      default:
        return false;
    }
  }

  /** @see InputProcessor#touchDown(int, int, int, int) */
  @Override
  public boolean touchDown (int screenX, int screenY, int pointer, int button) {

    CrystalFactory.crystalClicked(screenX,screenY);
    if (pointer == Input.Buttons.LEFT) {
      if (buildState) {
        buildEvent = true;
        boolean isClear = false;
        if (!structureRects.isEmpty()) {
          boolean[] updatedValues = ServiceLocator.getStructureService().handleClickedStructures(screenX, screenY, structureRects, resourceBuildState, buildEvent);
          isClear = updatedValues[0];
          resourceBuildState = updatedValues[1];
          buildEvent = updatedValues[2];
        } else {
          isClear = true;
        }
        if (isClear) {
          if (resourceBuildState) {
            ServiceLocator.getStructureService().triggerBuildEvent("wall", structureRects);
          } else {
            ServiceLocator.getStructureService().triggerBuildEvent("tower1", structureRects);
          }
        }
      }
    }
    return true;
  }

  /** @see InputProcessor#touchDragged(int, int, int) */
  @Override
  public boolean touchDragged (int screenX, int screenY, int pointer) {
    if (buildState) {
      if (buildEvent) {
        if (pointer == Input.Buttons.LEFT) {
          Entity camera = ServiceLocator.getEntityService().getNamedEntity("camera");
          CameraComponent camComp = camera.getComponent(CameraComponent.class);
          Vector3 mousePos = camComp.getCamera().unproject(new Vector3(screenX, screenY, 0));
          Vector2 mousePosV2 = new Vector2(mousePos.x, mousePos.y);
          mousePosV2.x -= 0.5;
          mousePosV2.y -= 0.5;
          ServiceLocator.getStructureService().getLastEntity().setPosition(mousePosV2);
          structureRects.get(structureRects.lastKey()).setPosition(mousePosV2);
        }
      }
    }
    return true;
  }

  /** @see InputProcessor#touchUp(int, int, int, int) */
  @Override
  public boolean touchUp(int screenX, int screenY, int pointer, int button) {
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
    //System.out.println(crystal.getComponent(CombatStatsComponent.class).getHealth());

  }

  /**
   * Triggers crystal upgrade to imitate crystal being levelled up (for testing purposes)
   */
  private void triggerCrystalUpgrade() {
    //System.out.println(ServiceLocator.getEntityService().getNamedEntity("crystal"));
    Entity crystal = ServiceLocator.getEntityService().getNamedEntity("crystal");
    CombatStatsComponent combatStatsComponent = crystal.getComponent(CombatStatsComponent.class);
    int level = combatStatsComponent.getLevel();
    combatStatsComponent.setLevel(level + 1);
    level = combatStatsComponent.getLevel();
    if (level == 2) {
      combatStatsComponent.setMaxHealth(1100);
      combatStatsComponent.setHealth(1100);
    } else if (level == 3) {
      combatStatsComponent.setMaxHealth(1200);
      combatStatsComponent.setHealth(1200);
    } else if (level == 4) {
      combatStatsComponent.setMaxHealth(1300);
      combatStatsComponent.setHealth(1300);
    } else if (level == 5) {
      combatStatsComponent.setMaxHealth(1400);
      combatStatsComponent.setHealth(1400);
    } else if (level > 5) {
      System.out.println("Crystal has reached max level");
    }
    // System.out.println(crystal.getComponent(CombatStatsComponent.class).getLevel());
    // System.out.println(crystal.getComponent(CombatStatsComponent.class).getHealth());
  }

  /**
   * Triggers crystal restore health to can be used in the shopping feature (for testing purposes)
   */
  private void triggerCrystalRestoreHealth() {
    //System.out.println(ServiceLocator.getEntityService().getNamedEntity("crystal"));
    Entity crystal = ServiceLocator.getEntityService().getNamedEntity("crystal");
    CombatStatsComponent combatStatsComponent = crystal.getComponent(CombatStatsComponent.class);
    InventoryComponent inventoryComponent = entity.getComponent(InventoryComponent.class);
    int gold = inventoryComponent.getGold();
    int health = combatStatsComponent.getHealth();
    int maxHealth = combatStatsComponent.getMaxHealth();
    if (maxHealth - health >= 50) {
      if (gold >= 5) {
        inventoryComponent.setGold(gold - 5);
        gold = inventoryComponent.getGold();
        combatStatsComponent.setHealth(health + 50);
      } else {
        System.out.println("Gold insufficient");
      }
    } else if (maxHealth - health >= 40) {
      if (gold >= 4) {
        inventoryComponent.setGold(gold - 4);
        gold = inventoryComponent.getGold();
        combatStatsComponent.setHealth(health + 40);
      } else {
        System.out.println("Gold insufficient");
      }
    } else if (maxHealth - health >= 30) {
      if (gold >= 3) {
        inventoryComponent.setGold(gold - 3);
        gold = inventoryComponent.getGold();
        combatStatsComponent.setHealth(health + 30);
      } else {
        System.out.println("Gold insufficient");
      }
    } else if (maxHealth - health >= 20) {
      if (gold >= 2) {
        inventoryComponent.setGold(gold - 2);
        gold = inventoryComponent.getGold();
        combatStatsComponent.setHealth(health + 20);
      } else {
        System.out.println("Gold insufficient");
      }
    } else if (maxHealth - health >= 10) {
      if (gold >= 1) {
        inventoryComponent.setGold(gold - 1);
        gold = inventoryComponent.getGold();
        combatStatsComponent.setHealth(health + 10);
      } else {
        System.out.println("Gold insufficient");
      }
    } else {
      System.out.println("Crystal has reached max health");
    }
    System.out.println(gold);
  }
}
