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
import com.deco2800.game.entities.*;
import com.deco2800.game.entities.factories.CrystalFactory;
import com.deco2800.game.input.InputComponent;
import com.deco2800.game.memento.Originator;
import com.deco2800.game.rendering.TextureRenderComponent;
import com.deco2800.game.services.ServiceLocator;
import com.deco2800.game.utils.math.Vector2Utils;

import java.io.Serial;
import java.util.HashMap;
import java.util.Map.Entry;

import java.util.*;

/**
 * Input handler for the player for keyboard and touch (mouse) input.
 * This input handler only uses keyboard input.
 */
public class KeyboardPlayerInputComponent extends InputComponent {
  private final Vector2 walkDirection = Vector2.Zero.cpy();
  private Boolean keyState;

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
    if (PlayerActions.playerAlive) {
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
          entity.getEvents().trigger("skipEpilogue");
          return true;
        default:
          return false;
      }
    } else {
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
    if (PlayerActions.playerAlive) {
      switch (keycode) {
        case Keys.Q:
          entity.setScale(11f, 10.5f);
          entity.getEvents().trigger("playerDeath");
          return true;
        case Keys.W:
          walkDirection.sub(Vector2Utils.UP);
          triggerWalkEvent();
          movePlayerInUgs();
          return true;
        case Keys.A:
          walkDirection.sub(Vector2Utils.LEFT);
          triggerWalkEvent();
          movePlayerInUgs();
          return true;
        case Keys.S:
          walkDirection.sub(Vector2Utils.DOWN);
          triggerWalkEvent();
          return true;
        case Keys.D:
          walkDirection.sub(Vector2Utils.RIGHT);
          triggerWalkEvent();
          movePlayerInUgs();
          return true;
        case Keys.O:
          triggerCrystalAttacked();
          return true;
        case Keys.R:
          if (ServiceLocator.getStructureService().getTempBuildState()) {
            ServiceLocator.getStructureService().rotateTempStructure();
          } else {
            triggerCrystalRestoreHealth();
          }

          return true;
        case Keys.SPACE:
          entity.getEvents().trigger("attack_anim_rev");
          return true;
        case Keys.PERIOD:
          ServiceLocator.getEntityService().getNamedEntity("terrain").getComponent(TerrainComponent.class)
              .decrementMapLvl();
          return true;
        default:
          return false;
      }
    } else {
      return false;
    }
  }

  /** @see InputProcessor#touchDown(int, int, int, int) */
  @Override
  public boolean touchDown(int screenX, int screenY, int pointer, int button) {
    CrystalFactory.crystalClicked(screenX, screenY);
    NpcService.npcClicked(screenX, screenY);
    return true;
  }

  @Override
  public boolean mouseMoved(int screenX, int screenY) {
    if (ServiceLocator.getStructureService().getTempBuildState()) {
      ServiceLocator.getStructureService().clearVisualTiles();
      Entity camera = ServiceLocator.getEntityService().getNamedEntity("camera");
      CameraComponent camComp = camera.getComponent(CameraComponent.class);
      Vector3 mousePos = camComp.getCamera().unproject(new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0));
      Vector2 mousePosV2 = new Vector2(mousePos.x, mousePos.y);
      GridPoint2 loc = ServiceLocator.getEntityService().getNamedEntity("terrain").getComponent(TerrainComponent.class)
          .worldToTilePosition(mousePosV2.x, mousePosV2.y);
      Vector2 worldLoc = ServiceLocator.getEntityService().getNamedEntity("terrain")
          .getComponent(TerrainComponent.class).tileToWorldPosition(loc);
      float tileSize = ServiceLocator.getEntityService().getNamedEntity("terrain").getComponent(TerrainComponent.class).getTileSize();
      worldLoc.x -= tileSize/4;
      worldLoc.y -= tileSize/8;
      ServiceLocator.getStructureService().drawVisualFeedback(loc, "structure");
      ServiceLocator.getEntityService().getNamedEntity(ServiceLocator.getStructureService().getTempEntityName())
          .setPosition(worldLoc);

    }
    return true;
  }

  /** @see InputProcessor#touchUp(int, int, int, int) */
  @Override
  public boolean touchUp(int screenX, int screenY, int pointer, int button) {
    if (pointer == Input.Buttons.LEFT) {
      if (ServiceLocator.getStructureService().getTempBuildState()) {
        Entity camera = ServiceLocator.getEntityService().getNamedEntity("camera");
        CameraComponent camComp = camera.getComponent(CameraComponent.class);
        Vector3 mousePos = camComp.getCamera().unproject(new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0));
        Vector2 mousePosV2 = new Vector2(mousePos.x, mousePos.y);
        GridPoint2 loc = ServiceLocator.getEntityService().getNamedEntity("terrain")
            .getComponent(TerrainComponent.class).worldToTilePosition(mousePosV2.x, mousePosV2.y);
        String entityName = ServiceLocator.getStructureService().getTempEntityName();
        entityName = entityName.replace("Temp", "");
        if (ServiceLocator.getStructureService().buildStructure(entityName, loc)) {
          ServiceLocator.getEntityService().getNamedEntity(ServiceLocator.getStructureService().getTempEntityName())
              .dispose();
          ServiceLocator.getStructureService().setTempBuildState(false);
          ServiceLocator.getStructureService().clearVisualTiles();
//          triggerUIBuildingPopUp(screenX, screenY); //Not Functional
        }
      } else {
        Entity clickedEntity = ServiceLocator.getUGSService().getClickedEntity();
        if (clickedEntity == ServiceLocator.getEntityService().getNamedEntity("crystal")) {
          System.out.println("You clicked the crystal");
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

  private void triggerUIBuildingPopUp(int screenX, int screenY) {
    String name = ServiceLocator.getStructureService().getTempEntityName();
    if (name.contains("tower1") || name.contains("wall") || name.contains("trap") || name.contains("tower2")
        || name.contains("tower3"))
      StructureService.setUiPopUp(screenX, screenY);
  }

  private void movePlayerInUgs() {
    // GET CURRENT PLAYER ENTITY AND GRID POINT POSITION
    Entity player = ServiceLocator.getEntityService().getNamedEntity("player");
    GridPoint2 playerCurrentPos = ServiceLocator.getEntityService().getNamedEntity("terrain").getComponent(TerrainComponent.class).worldToTilePosition(player.getPosition().x, player.getPosition().y);
    String key = UGS.generateCoordinate(playerCurrentPos.x, playerCurrentPos.y);

    // FIND WHERE THE PLAYER WAS AND REPLACE THAT TILE WITH A NEW TILE OF THE SAME TYPE
    Tile oldPlayerTile;
    for (Entry<String, Tile> entry : ServiceLocator.getUGSService().printUGS().entrySet()) {
      if (entry.getValue().getEntity() == player) {
        String currentPos = entry.getKey();
        if (!currentPos.equals(key)) {
          oldPlayerTile = entry.getValue();
          String oldTileType = entry.getValue().getTileType();
          Tile replacement = new Tile();
          replacement.setTileType(oldTileType);
          ServiceLocator.getUGSService().change(entry.getKey(), replacement);
        }
      }
    }

    // RESET WHERE THE PLAYER IS
    ServiceLocator.getUGSService().setEntity(playerCurrentPos, player, "player");




//
//    switch (direction) {
//      case "right":
//        // move right 1 square
//        ServiceLocator.getUGSService().moveEntity(player, playerCurrentPos, 1, 0, "player");
//        return;
//      case "left":
//        // move left 1 square
//        ServiceLocator.getUGSService().moveEntity(player, playerCurrentPos, -1, 0, "player");
//        return;
//      case "up":
//        // move up 1 square
//        ServiceLocator.getUGSService().moveEntity(player, playerCurrentPos, 0, 1, "player");
//        return ;
//      case "down":
//        // move down 1 square
//        ServiceLocator.getUGSService().moveEntity(player, playerCurrentPos, 0, -1, "player");
//    }

  }
}
