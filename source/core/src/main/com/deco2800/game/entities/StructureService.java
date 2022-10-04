package com.deco2800.game.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Array;
import com.deco2800.game.areas.terrain.TerrainComponent;
import com.deco2800.game.components.CameraComponent;
import com.deco2800.game.components.maingame.MainGameBuildingInterface;
import com.deco2800.game.components.player.PlayerStatsDisplay;
import com.deco2800.game.entities.configs.BaseStructureConfig;
import com.deco2800.game.entities.factories.StructureFactory;
import com.deco2800.game.entities.factories.ResourceBuildingFactory;
import com.deco2800.game.files.SaveGame;
import com.deco2800.game.services.ServiceLocator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.Objects;

/**
 * Provides a global access point for entities to register themselves. This allows for iterating
 * over entities to perform updates each loop. All game entities should be registered here.
 *
 * Avoid adding additional state here! Global access is often the easy but incorrect answer to
 * sharing data.
 */
public class StructureService extends EntityService {
  private static final Logger logger = LoggerFactory.getLogger(StructureService.class);
  private static final int INITIAL_CAPACITY = 40;

  private final Array<Entity> structureEntities = new Array<>(false, INITIAL_CAPACITY);

  private final Map<String, Entity> namedStructureEntities = new HashMap<String, Entity>();

  public static HashMap<String, Table> tables = new HashMap<String, Table>();

  private static boolean uiIsVisible;

  private static Table table1;

  private static String structureName;

  private static String structureKey;
  
  private static HashMap<String, Tile> tiles = new HashMap<String, Tile>();


  /**
   * Register a new entity with the entity service. The entity will be created and start updating.
   * @param entity new entity.
   */
  @Override
  public void register(Entity entity) {
    logger.debug("Registering {} in entity service", entity);
    structureEntities.add(entity);
//    entity.create();
  }

  /**
   * Registers an entity with a name so it can be found later
   *
   * @param name the name to register it as (must be unique or will overwrite)
   * @param entity the entity to register
   */
  @Override
  public void registerNamed(String name, Entity entity) {
    this.namedStructureEntities.put(name, entity);
    this.register(entity);
  }

  /**
   * Returns a registered named entity
   * @param name the name the entity was registered as
   * @return the registered entity or null
   */
  @Override
  public Entity getNamedEntity(String name) {
    return this.namedStructureEntities.get(name);
  }

  /**
   * Returns the last registered entity
   * @return the last registered entity or null
   */
  @Override
  public Entity getLastEntity() {
    return this.structureEntities.get(this.structureEntities.size-1);
  }

  /**
   * Returns all registered entities
   * @return all registered entities or null
   */
  @Override
  public Map<String, Entity> getAllNamedEntities() {
    return this.namedStructureEntities;
  }


  /**
   * Unregister an entity with the entity service. The entity will be removed and stop updating.
   * @param entity entity to be removed.
   */
  @Override
  public void unregister(Entity entity) {
    logger.debug("Unregistering {} in entity service", entity);
    structureEntities.removeValue(entity, true);
  }

  /**
   * Update all registered entities. Should only be called from the main game loop.
   */
  @Override
  public void update() {
    for (Entity entity : structureEntities) {
      entity.earlyUpdate();
      entity.update();
    }
  }

  /**
   * Dispose all entities.
   */
  @Override
  public void dispose() {
    for (Entity entity : structureEntities) {
      entity.dispose();
    }
  }

  public static void toggleUIisVisible() {
    uiIsVisible = !uiIsVisible;
  }

  /** Builds a structure at mouse position
   * @param name name of the structure in game entity list
   */
  public static void triggerBuildEvent(String name) {
    Entity camera = ServiceLocator.getEntityService().getNamedEntity("camera");
    CameraComponent camComp = camera.getComponent(CameraComponent.class);
    Vector3 mousePos = camComp.getCamera().unproject(new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0));
    Vector2 mousePosV2 = new Vector2(mousePos.x, mousePos.y);
    GridPoint2 loc = ServiceLocator.getEntityService().getNamedEntity("terrain").getComponent(TerrainComponent.class).worldToTilePosition(mousePosV2.x, mousePosV2.y);
    Vector2 worldLoc = ServiceLocator.getEntityService().getNamedEntity("terrain").getComponent(TerrainComponent.class).tileToWorldPosition(loc);
    String entityName = loc.toString();
    entityName = name + entityName;
    String stringTileCoords = ServiceLocator.getUGSService().generateCoordinate(loc.x, loc.y);

    structureKey = name;
    if (!uiIsVisible) {
      if (ServiceLocator.getUGSService().checkEntityPlacement(stringTileCoords, "structure")) {
        if (Objects.equals(name, "wall")) {
          Entity wall = StructureFactory.createWall(entityName);
          ServiceLocator.getEntityService().registerNamed(entityName, wall);
          ServiceLocator.getStructureService().registerNamed(entityName, wall);
          ServiceLocator.getUGSService().setEntity(stringTileCoords, wall);
          logger.info("ugs@{} ==> {}", stringTileCoords, ServiceLocator.getUGSService().getEntity(stringTileCoords).getName());
          wall.setPosition(worldLoc);
        } else if (Objects.equals(name, "tower1")) {
          Entity tower1 = StructureFactory.createTower1(1, entityName);
          ServiceLocator.getEntityService().registerNamed(entityName, tower1);
          ServiceLocator.getStructureService().registerNamed(entityName, tower1);
          ServiceLocator.getUGSService().setEntity(stringTileCoords, tower1);
          logger.info("ugs@{} ==> {}", stringTileCoords, ServiceLocator.getUGSService().getEntity(stringTileCoords).getName());
          tower1.setPosition(worldLoc);
        } else if (Objects.equals(name, "tower2")) {
          Entity tower2 = StructureFactory.createTower2(1, entityName);
          ServiceLocator.getEntityService().registerNamed(entityName, tower2);
          ServiceLocator.getStructureService().registerNamed(entityName, tower2);
          ServiceLocator.getUGSService().setEntity(stringTileCoords, tower2);
          logger.info("ugs@{} ==> {}", stringTileCoords, ServiceLocator.getUGSService().getEntity(stringTileCoords));
          tower2.setPosition(worldLoc);
        } else if (Objects.equals(name, "woodCutter")) {
          Entity woodCutter = ResourceBuildingFactory.createWoodCutter();
          ServiceLocator.getEntityService().registerNamed(entityName, woodCutter);
          ServiceLocator.getUGSService().setEntity(stringTileCoords, woodCutter);
          logger.info("ugs@{} ==> {}", stringTileCoords, ServiceLocator.getUGSService().getEntity(stringTileCoords).getName());
          woodCutter.setPosition(worldLoc);
        } else if (Objects.equals(name, "tower3")) {
          Entity tower3 = StructureFactory.createTower3(1, entityName);
          ServiceLocator.getEntityService().registerNamed(entityName, tower3);
          ServiceLocator.getStructureService().registerNamed(entityName, tower3);
          ServiceLocator.getUGSService().setEntity(stringTileCoords, tower3);
          logger.info("ugs@{} ==> {}", stringTileCoords, ServiceLocator.getUGSService().getEntity(stringTileCoords).getName());
          tower3.setPosition(worldLoc);
        } else if (Objects.equals(name, "trap")) {
          Entity trap = StructureFactory.createTrap(entityName);
          ServiceLocator.getEntityService().registerNamed(entityName, trap);
          ServiceLocator.getStructureService().registerNamed(entityName, trap);
          ServiceLocator.getUGSService().setEntity(stringTileCoords, trap);
          logger.info("ugs@{} ==> {}", stringTileCoords, ServiceLocator.getUGSService().getEntity(stringTileCoords).getName());
          trap.setPosition(worldLoc);
        } else if (Objects.equals(name, "stonequarry")) {
          Entity stonequarry = ResourceBuildingFactory.createStoneQuarry();
          ServiceLocator.getEntityService().registerNamed(entityName, stonequarry);
          ServiceLocator.getUGSService().setEntity(stringTileCoords, stonequarry);
          logger.info("ugs@{} ==> {}", stringTileCoords, ServiceLocator.getUGSService().getEntity(stringTileCoords).getName());
          stonequarry.setPosition(worldLoc);
        }
      }
    } else {
      if (uiIsVisible) {
        table1.remove();
        toggleUIisVisible();
      }
    }
  }

  /**
   * Checks if a structure on the map has been clicked. If it has been clicked then that structure gets removed from the game
   * @param screenX The x coordinate, origin is in the upper left corner
   * @param screenY The y coordinate, origin is in the upper left corner
   * @param resourceBuildState true if building resource building false otherwise
   * @param buildEvent true if currently in a build event false otherwise
   * @return list of booleans[]{true if the point (screenX, screenY) is clear of structures else return false,
   *                            resourceBuildState, buildEvent}
   */
  public static boolean[] handleClicks(int screenX, int screenY, boolean resourceBuildState, boolean buildEvent, boolean removeEvent, boolean upgradeEvent) {
    SaveGame.saveGameState();
    boolean isClear = false;
    boolean structureHit = false;
    Entity camera = ServiceLocator.getEntityService().getNamedEntity("camera");
    CameraComponent camComp = camera.getComponent(CameraComponent.class);
    Vector3 mousePos = camComp.getCamera().unproject(new Vector3(screenX, screenY, 0));
    Vector2 mousePosV2 = new Vector2(mousePos.x, mousePos.y);
    GridPoint2 mapPos = ServiceLocator.getEntityService().getNamedEntity("terrain").getComponent(TerrainComponent.class).worldToTilePosition(mousePosV2.x, mousePosV2.y);
    String strCoord = ServiceLocator.getUGSService().generateCoordinate(mapPos.x, mapPos.y);
    if (ServiceLocator.getUGSService().getEntity(strCoord) != null) {
      String name = ServiceLocator.getUGSService().getEntity(strCoord).getName();
      if (name.contains("tower1") || name.contains("wall") || name.contains("trap") || name.contains("tower2") || name.contains("tower3")) {
        structureHit = true;
        structureName = name;
        if (buildEvent) {
          if (!uiIsVisible) {
            table1 = ServiceLocator.getEntityService().getNamedEntity("ui").getComponent(MainGameBuildingInterface.class).makeUIPopUp(true, screenX, screenY, structureName, structureName);
            toggleUIisVisible();
          }
        } else if (removeEvent) {
          StructureFactory.handleBuildingDestruction(name);
          removeEvent = false;
        } else if (upgradeEvent) {
          logger.info("UPGRADE EVENT");
          StructureFactory.upgradeStructure(name);
          upgradeEvent = false;
        }
      } else {
        if (uiIsVisible) {
          table1.remove();
          toggleUIisVisible();
        }
      }
    }
    if (structureHit) {
      buildEvent = false;
      isClear = false;
    } else {
      isClear = true;
    }
    return new boolean[]{isClear, resourceBuildState, buildEvent, removeEvent, upgradeEvent};
  }

  /**
   * Toggles the build state of the player
   * @param buildState true if currently in build state false otherwise
   */
  public static boolean toggleBuildState(boolean buildState) {
    buildState = !buildState;
    return  buildState;
  }

  /**
   * Toggles resource building placement mode
   * @param resourceBuildState true if building resource building false otherwise
   */
  public static boolean toggleResourceBuildState(boolean resourceBuildState) {
    resourceBuildState = !resourceBuildState;
    return resourceBuildState;
  }

  /**
   * Method stub for returning a given entity's name
   * @param entity
   */
  public String getName(Entity entity) {
    return "";
  }

  public boolean toggleRemoveState(boolean removeState) {
    removeState = !removeState;
    return  removeState;
  }

    public boolean toggleUpgradeState(boolean upgradeState) {
      upgradeState = !upgradeState;
      return  upgradeState;
    }
    
}