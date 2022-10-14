package com.deco2800.game.components.player;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.math.*;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.deco2800.game.areas.terrain.TerrainComponent;
import com.deco2800.game.areas.terrain.TerrainFactory;
import com.deco2800.game.components.CameraComponent;
import com.deco2800.game.components.CombatStatsComponent;
import com.deco2800.game.components.maingame.MainGameBuildingInterface;
import com.deco2800.game.components.maingame.MainGameNpcInterface;
import com.deco2800.game.entities.*;
import com.deco2800.game.entities.factories.CrystalFactory;
import com.deco2800.game.input.InputComponent;
import com.deco2800.game.memento.Originator;
import com.deco2800.game.rendering.TextureRenderComponent;
import com.deco2800.game.services.ServiceLocator;
import com.deco2800.game.utils.math.Vector2Utils;

//import java.io.Serial;     // this had an error not sure what the go is???

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
  private static Table PopUp;
  private static boolean isVisible;


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
          //entity.setScale(11f, 10.5f);
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
        case Keys.R:
          if (ServiceLocator.getStructureService().getTempBuildState()) {
            ServiceLocator.getStructureService().rotateTempStructure();
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
    if (isVisible) {
      PopUp.remove();
      isVisible = false;
    }
    Boolean onClick = false;
    Entity clickedEntity = ServiceLocator.getUGSService().getClickedEntity();


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
        }
      } else {
        // crystal has been clicked
        if (clickedEntity == ServiceLocator.getEntityService().getNamedEntity("crystal")) {
          PopUp = ServiceLocator.getEntityService().getNamedEntity("ui").getComponent(MainGameBuildingInterface.class).makeCrystalPopUp(true, screenX, screenY);
          isVisible = true;
        }


        String entityName = ServiceLocator.getStructureService().getTempEntityName();
      if (entityName != null && clickedEntity != ServiceLocator.getEntityService().getNamedEntity("crystal") ) {
        if (!onClick) {
          if (entityName.contains("tower1") || entityName.contains("wall") ||
                  entityName.contains("trap") || entityName.contains("tower2")
                  || entityName.contains("tower3")) {
            onClick = true;
            StructureService.setUiPopUp(screenX, screenY, onClick);
          }
        } else {
          onClick = false;
        }
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
