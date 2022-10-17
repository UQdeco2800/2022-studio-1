package com.deco2800.game.components.player;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.math.*;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.deco2800.game.areas.ForestGameArea;
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
import com.deco2800.game.services.DayNightCycleStatus;
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
  public static final String EVENT_PLAYER_CONTROL_TUT = "playerControlTut";
  public static final String EVENT_REMOVE_NO_MINE = "removeNoMine";
  public static final String EVENT_WALK_REV = "walk_rev";

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
          // triggerWalkEvent();
          entity.getEvents().trigger(EVENT_PLAYER_CONTROL_TUT, "UP");
          entity.getEvents().trigger(EVENT_REMOVE_NO_MINE);
          // movePlayerInUgs(walkDirection);
          // ServiceLocator.getEntityService().getNamedEntity("player").getComponent(PlayerActions.class).update();
          updatePlayerMovement(0, true);
          return true;
        case Keys.A:
          walkDirection.add(Vector2Utils.LEFT);
          entity.getEvents().trigger("ch_dir_a");
          // triggerWalkEvent();
          entity.getEvents().trigger(EVENT_PLAYER_CONTROL_TUT, "LEFT");
          entity.getEvents().trigger(EVENT_REMOVE_NO_MINE);
          // movePlayerInUgs(walkDirection);
          updatePlayerMovement(1, true);
          return true;
        case Keys.S:
          walkDirection.add(Vector2Utils.DOWN);
          entity.getEvents().trigger("ch_dir_s");
          // triggerWalkEvent();
          entity.getEvents().trigger(EVENT_PLAYER_CONTROL_TUT, "DOWN");
          entity.getEvents().trigger(EVENT_REMOVE_NO_MINE);
          // movePlayerInUgs(walkDirection);
          updatePlayerMovement(2, true);
          return true;
        case Keys.D:
          walkDirection.add(Vector2Utils.RIGHT);
          entity.getEvents().trigger("ch_dir_d");
          // triggerWalkEvent();
          entity.getEvents().trigger(EVENT_PLAYER_CONTROL_TUT, "RIGHT");
          entity.getEvents().trigger(EVENT_REMOVE_NO_MINE);
          // movePlayerInUgs(walkDirection);
          updatePlayerMovement(3, true);
          return true;
        case Keys.E:
          entity.getEvents().trigger("weapons");
          return true;
        case Keys.SPACE:
          entity.getEvents().trigger("attack");
          entity.getEvents().trigger("attack_anim");
          entity.getEvents().trigger(EVENT_PLAYER_CONTROL_TUT, "SPACE");
          entity.getEvents().trigger("skipEpilogue");
          return true;
        case Keys.N:
          ServiceLocator.getDayNightCycleService().setPartOfDayTo(DayNightCycleStatus.NIGHT);
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
    switch (keycode) {
      case Keys.Q:
        if (PlayerActions.playerAlive) {
          // entity.setScale(11f, 10.5f);
          entity.getEvents().trigger("playerDeath");
          return true;
        } else {
          return false;
        }
      case Keys.W:
        if (PlayerActions.playerAlive) {
          walkDirection.sub(Vector2Utils.UP);
          // triggerWalkEvent();
          entity.getEvents().trigger(EVENT_WALK_REV);
          // movePlayerInUgs();
          updatePlayerMovement(0, false);
          return true;
        } else {
          return false;
        }
      case Keys.A:
        if (PlayerActions.playerAlive) {
          walkDirection.sub(Vector2Utils.LEFT);
          // triggerWalkEvent();
          entity.getEvents().trigger(EVENT_WALK_REV);
          // movePlayerInUgs();
          updatePlayerMovement(1, false);
          return true;
        } else {
          return false;
        }
      case Keys.S:
        if (PlayerActions.playerAlive) {
          walkDirection.sub(Vector2Utils.DOWN);
          // triggerWalkEvent();
          entity.getEvents().trigger(EVENT_WALK_REV);
          updatePlayerMovement(2, false);
          return true;
        } else {
          return false;
        }
      case Keys.D:
        if (PlayerActions.playerAlive) {
          walkDirection.sub(Vector2Utils.RIGHT);
          // triggerWalkEvent();
          entity.getEvents().trigger(EVENT_WALK_REV);
          // movePlayerInUgs();
          updatePlayerMovement(3, false);
          return true;
        } else {
          return false;
        }
      case Keys.SPACE:
        if (PlayerActions.playerAlive) {
          entity.getEvents().trigger("attack_anim_rev");
          return true;
        } else {
          return false;
        }
      case Keys.R:
        if (ServiceLocator.getStructureService().getTempBuildState()) {
          ServiceLocator.getStructureService().rotateTempStructure();
        }
        return true;
      case Keys.PERIOD:
        ServiceLocator.getEntityService().getNamedEntity(ForestGameArea.TERRAIN).getComponent(TerrainComponent.class)
            .decrementMapLvl();
        return true;
      default:
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
      GridPoint2 loc = ServiceLocator.getEntityService().getNamedEntity(ForestGameArea.TERRAIN).getComponent(TerrainComponent.class)
          .worldToTilePosition(mousePosV2.x, mousePosV2.y);
      Vector2 worldLoc = ServiceLocator.getEntityService().getNamedEntity(ForestGameArea.TERRAIN)
          .getComponent(TerrainComponent.class).tileToWorldPosition(loc);
      float tileSize = ServiceLocator.getEntityService().getNamedEntity(ForestGameArea.TERRAIN).getComponent(TerrainComponent.class).getTileSize();
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
    Entity clickedEntity = ServiceLocator.getUGSService().getClickedEntity();


    if (pointer == Input.Buttons.LEFT) {
      if (ServiceLocator.getStructureService().getTempBuildState()) {
        Entity camera = ServiceLocator.getEntityService().getNamedEntity("camera");
        CameraComponent camComp = camera.getComponent(CameraComponent.class);
        Vector3 mousePos = camComp.getCamera().unproject(new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0));
        Vector2 mousePosV2 = new Vector2(mousePos.x, mousePos.y);
        GridPoint2 loc = ServiceLocator.getEntityService().getNamedEntity(ForestGameArea.TERRAIN)
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
        if (clickedEntity == ServiceLocator.getEntityService().getNamedEntity(CombatStatsComponent.CRYSTAL)) {
          PopUp = ServiceLocator.getEntityService().getNamedEntity("ui").getComponent(MainGameBuildingInterface.class).makeCrystalPopUp(true, screenX, screenY);
          isVisible = true;
        }


        String entityName = ServiceLocator.getStructureService().getTempEntityName();
      if (entityName != null && clickedEntity != ServiceLocator.getEntityService().getNamedEntity(CombatStatsComponent.CRYSTAL) ) {
        if (entityName.contains("tower1") || entityName.contains("wallTemp") ||
                  entityName.contains("trap") || entityName.contains("tower2")
                  || entityName.contains("tower3")) {
            StructureService.setUiPopUp(screenX, screenY);
          }
      }
      }
    }
    return true;
  }

  private void updatePlayerMovement(int key, boolean pressed) {
    getEntity().getEvents().trigger("updatePlayerPosition", key, pressed);
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
    Entity crystal = ServiceLocator.getEntityService().getNamedEntity(CombatStatsComponent.CRYSTAL);
    CombatStatsComponent combatStatsComponent = crystal.getComponent(CombatStatsComponent.class);
    int health = combatStatsComponent.getHealth();
    combatStatsComponent.setHealth(health - 30);
    // System.out.println(crystal.getComponent(CombatStatsComponent.class).getHealth());
  }


  // // GET CURRENT PLAYER ENTITY AND GRID POINT POSITION
  // Entity player = ServiceLocator.getEntityService().getNamedEntity("player");
  // GridPoint2 playerCurrentPos =
  // ServiceLocator.getEntityService().getNamedEntity(ForestGameArea.TERRAIN).getComponent(TerrainComponent.class).worldToTilePosition(player.getPosition().x,
  // player.getPosition().y);
  // String key = UGS.generateCoordinate(playerCurrentPos.x, playerCurrentPos.y);
  //
  // // FIND WHERE THE PLAYER WAS AND REPLACE THAT TILE WITH A NEW TILE OF THE
  // SAME TYPE
  // if (ServiceLocator.getUGSService().printUGS().get(key).getEntity() != player)
  // {
  //
  // Tile oldPlayerTile;
  // for (Entry<String, Tile> entry :
  // ServiceLocator.getUGSService().printUGS().entrySet()) {
  // if (entry.getValue().getEntity() == player) {
  // String currentPos = entry.getKey();
  // if (!currentPos.equals(key)) {
  // oldPlayerTile = entry.getValue();
  // String oldTileType = entry.getValue().getTileType();
  // Tile replacement = new Tile();
  // replacement.setTileType(oldTileType);
  // ServiceLocator.getUGSService().change(entry.getKey(), replacement);
  // }
  // }
  // }
  //
  // // RESET WHERE THE PLAYER IS
  // ServiceLocator.getUGSService().setEntity(playerCurrentPos, player, "player");

}
