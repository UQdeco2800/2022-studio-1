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
import com.deco2800.game.entities.Entity;
import com.deco2800.game.entities.factories.StructureFactory;
import com.deco2800.game.input.InputComponent;
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
        triggerWalkEvent();
        return true;
      case Keys.A:
        walkDirection.add(Vector2Utils.LEFT);
        triggerWalkEvent();
        return true;
      case Keys.S:
        walkDirection.add(Vector2Utils.DOWN);
        triggerWalkEvent();
        return true;
      case Keys.D:
        walkDirection.add(Vector2Utils.RIGHT);
        triggerWalkEvent();
        return true;
      case Keys.SPACE:
        entity.getEvents().trigger("attack");
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
        toggleBuildState();
        return true;
      case Keys.O:
        triggerCrystalAttacked();
        return true;
      case Keys.U:
        triggerCrystalUpgrade();
        return true;
      case Keys.N:
        toggleResourceBuildState();
        return true;
      default:
        return false;
    }
  }

  /** @see InputProcessor#touchDown(int, int, int, int) */
  @Override
  public boolean touchDown (int screenX, int screenY, int pointer, int button) {
    if (pointer == Input.Buttons.LEFT) {
      if (buildState) {
        buildEvent = true;
        boolean isClear = false;
        if (!structureRects.isEmpty()) {
          isClear = handleClickedStructures(screenX, screenY);
        } else {
          isClear = true;
        }
        if (isClear) {
          if (resourceBuildState) {
            triggerBuildEvent("stonequarry");
          } else {
            triggerBuildEvent("wall");
          }
        }
      }
    }
    return true;
  }

  /**
   * Checks if a structure on the map has been clicked. If it has been clicked then that structure gets removed from the game
   * @param screenX The x coordinate, origin is in the upper left corner
   * @param screenY The y coordinate, origin is in the upper left corner
   * @return true if the point (screenX, screenY) is clear of structures else return false
   *
   */
  private boolean handleClickedStructures(int screenX, int screenY) {
    String clickedStructure = "";
    boolean isClear;
    boolean anyStructureHit = false;
    Entity camera = ServiceLocator.getEntityService().getNamedEntity("camera");
    CameraComponent camComp = camera.getComponent(CameraComponent.class);
    Vector3 mousePos = camComp.getCamera().unproject(new Vector3(screenX, screenY, 0));
    Vector2 mousePosV2 = new Vector2(mousePos.x, mousePos.y);
    for (Map.Entry<String, Rectangle> es : structureRects.entrySet()){
      if (es.getValue().contains(mousePosV2)) {
        clickedStructure = es.getKey();
        if (clickedStructure.contains("stonequarry")) {
          PlayerStatsDisplay.stoneCount += 100;
          PlayerStatsDisplay.stoneCurrencyLabel.setText(PlayerStatsDisplay.stoneCount);
          resourceBuildState = false;
          return false;
        } else {
          ServiceLocator.getEntityService().getNamedEntity(es.getKey()).dispose();
          anyStructureHit = true;
        }
      }
    }
    if (anyStructureHit) {
      buildEvent = false;
      isClear = false;

      structureRects.remove(clickedStructure);
    } else {
      isClear = true;
    }
    return isClear;
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
          ServiceLocator.getEntityService().getLastEntity().setPosition(mousePosV2);
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
      entity.getEvents().trigger("anim_player");
      entity.getEvents().trigger("walk", walkDirection);
    }
  }

  /**
   * Toggles the build state of the player
   */
  private void toggleBuildState() {
    buildState = !buildState;
  }

  /**
   * Toggles resource building placement mode
   */
  private void toggleResourceBuildState() {
    resourceBuildState = !resourceBuildState;
  }

  /**
   * Builds a structure at mouse position
   */
  private void triggerBuildEvent(String name) {
    Entity camera = ServiceLocator.getEntityService().getNamedEntity("camera");
    CameraComponent camComp = camera.getComponent(CameraComponent.class);
    Vector3 mousePos = camComp.getCamera().unproject(new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0));
    Vector2 mousePosV2 = new Vector2(mousePos.x, mousePos.y);
    mousePosV2.x -= 0.5;
    mousePosV2.y -= 0.5;
    String entityName = String.valueOf(ServiceLocator.getTimeSource().getTime());
    entityName = name + entityName;

    if (Objects.equals(name, "wall")) {
      ServiceLocator.getEntityService().registerNamed(entityName, StructureFactory.createWall());
      ServiceLocator.getEntityService().getNamedEntity(entityName).setPosition(mousePosV2);
      Rectangle rectangle = new Rectangle(mousePosV2.x, mousePosV2.y, 1, 1);
      structureRects.put(entityName, rectangle);
    } else if (Objects.equals(name, "stonequarry")) {
      ServiceLocator.getEntityService().registerNamed(entityName, StructureFactory.createStoneQuarry());
      ServiceLocator.getEntityService().getNamedEntity(entityName).setPosition(mousePosV2);
      Rectangle rectangle = new Rectangle(mousePosV2.x, mousePosV2.y, 1, 1);
      structureRects.put(entityName, rectangle);
    } else if (Objects.equals(name, "tower1")) {
      ServiceLocator.getEntityService().registerNamed(entityName, StructureFactory.createTower1());
      ServiceLocator.getEntityService().getNamedEntity(entityName).setPosition(mousePosV2);
      Rectangle rectangle = new Rectangle(mousePosV2.x, mousePosV2.y, 1, 1);
      structureRects.put(entityName, rectangle);
    }
  }

  /**
   * Damages crystal to imitate crystal being attacked (for testing purposes)
   */
  private void triggerCrystalAttacked() {
    Entity crystal = ServiceLocator.getEntityService().getNamedEntity("crystal");
    CombatStatsComponent combatStatsComponent = crystal.getComponent(CombatStatsComponent.class);
    int health = combatStatsComponent.getHealth();
    combatStatsComponent.setHealth(health - 10);
    //System.out.println(crystal.getComponent(CombatStatsComponent.class).getHealth());

  }

  /**
   * Triggers crystal upgrade to imitate crystal being levelled up (for testing purposes)
   */
  private void triggerCrystalUpgrade() {
    Entity crystal = ServiceLocator.getEntityService().getNamedEntity("crystal");
    crystal.getComponent(CombatStatsComponent.class).upgrade();
//    System.out.println(crystal.getComponent(CombatStatsComponent.class).getHealth());
//    System.out.println(crystal.getComponent(CombatStatsComponent.class).getLevel());
  }
}
