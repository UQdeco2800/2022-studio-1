package com.deco2800.game.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.Vector;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Array;
import com.deco2800.game.areas.terrain.TerrainComponent;
import com.deco2800.game.components.CameraComponent;
import com.deco2800.game.components.maingame.MainGameBuildingInterface;
import com.deco2800.game.entities.factories.ResourceBuildingFactory;
import com.deco2800.game.entities.factories.StructureFactory;
import com.deco2800.game.services.AchievementHandler;
import com.deco2800.game.services.ServiceLocator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Provides a global access point for entities to register themselves. This
 * allows for iterating
 * over entities to perform updates each loop. All game entities should be
 * registered here.
 *
 * Avoid adding additional state here! Global access is often the easy but
 * incorrect answer to
 * sharing data.
 */
public class StructureService extends EntityService {
  private static final Logger logger = LoggerFactory.getLogger(StructureService.class);
  private static final int INITIAL_CAPACITY = 40;

  private final Array<Entity> structureEntities = new Array<>(false, INITIAL_CAPACITY); // Deprecate

  private final Map<String, Entity> namedStructureEntities = new HashMap<String, Entity>(); // Deprecate

  private static boolean uiIsVisible;

  private static Table uiPopUp;

  private static Entity tempEntity;

  private static Boolean buildingTempEntity = false;

  private static String tempEntityName;

  /**
   * Register a new entity with the entity service. The entity will be created and
   * start updating.
   *
   * @param entity new entity.
   */
  @Override
  public void register(Entity entity) {
    logger.debug("Registering {} in entity service", entity);
    structureEntities.add(entity);
    // entity.create();
  }

  /**
   * Registers an entity with a name so it can be found later
   *
   * @param name   the name to register it as (must be unique or will overwrite)
   * @param entity the entity to register
   */
  @Override
  public void registerNamed(String name, Entity entity) {
    this.namedStructureEntities.put(name, entity);
    this.register(entity);
  }

  /**
   * Returns a registered named entity
   *
   * @param name the name the entity was registered as
   * @return the registered entity or null
   */
  @Override
  public Entity getNamedEntity(String name) {
    return this.namedStructureEntities.get(name);
  }

  /**
   * Returns the last registered entity
   *
   * @return the last registered entity or null
   */
  @Override
  public Entity getLastEntity() {
    return this.structureEntities.get(this.structureEntities.size - 1);
  }

  /**
   * Returns all registered entities
   *
   * @return all registered entities or null
   */
  @Override
  public Map<String, Entity> getAllNamedEntities() {
    return this.namedStructureEntities;
  }

  /**
   * Unregister an entity with the entity service. The entity will be removed and
   * stop updating.
   *
   * @param entity entity to be removed.
   */
  @Override
  public void unregister(Entity entity) {
    logger.debug("Unregistering {} in entity service", entity);
    structureEntities.removeValue(entity, true);
  }

  /**
   * Update all registered entities. Should only be called from the main game
   * loop.
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

  /**
   * Builds a structure at specific location, gridPos. the type of structure built
   * is
   * determined by the specific structure name, structureName.
   *
   * @param structureName name of the structure to build (also type of structure)
   * @param gridPos       location of where the structure is to be built
   * @return true if building was build successfully, false otherwise
   */
  public static Boolean buildStructure(String structureName, GridPoint2 gridPos) {
    String entityName = gridPos.toString();
    entityName = structureName + entityName;
    Vector2 worldPosition = ServiceLocator.getEntityService().getNamedEntity("terrain")
        .getComponent(TerrainComponent.class).tileToWorldPosition(gridPos);

    Entity structure;
    if (ServiceLocator.getUGSService().checkEntityPlacement(gridPos, "structure")) {
      switch (structureName) {
        case "wall":
          structure = StructureFactory.createWall(entityName);
          break;

        case "tower1":
          structure = StructureFactory.createTower1(1, entityName);
          break;

        case "tower2":
          structure = StructureFactory.createTower2(1, entityName);
          break;

        case "tower3":
          structure = StructureFactory.createTower3(1, entityName);
          break;

        case "trap":
          structure = StructureFactory.createTrap(entityName);
          break;

        case "stoneQuarry":
          structure = ResourceBuildingFactory.createStoneQuarry(entityName);
          break;

        case "woodCutter":
          structure = ResourceBuildingFactory.createWoodCutter(entityName);
          break;

        default:
          return false;
      }

      structure.setPosition(worldPosition);
      ServiceLocator.getUGSService().setEntity(gridPos, structure, entityName);
      return true;
    }
    return false;
  }

  /**
   * gets the building temp entity state
   *
   * @return True if building a building from the inventory or False if not
   *         building a building from the inventory
   */
  public static Boolean getTempBuildState() {
    return buildingTempEntity;
  }

  /**
   * gets the temp building name
   *
   * @return name of the temp entity
   */
  public static String getTempEntityName() {
    return tempEntityName;
  }

  /**
   * Set the building temp entity state
   *
   * @param state state to set the buildingTempEntity state to.
   */
  public void setTempBuildState(Boolean state) {
    buildingTempEntity = state;
  }

  /**
   * Builds a structure and locks it to the mouse as the user decides where to
   * build it
   *
   * @param name of the tempStructureEntity
   */
  public static void buildTempStructure(String name) {
    Entity camera = ServiceLocator.getEntityService().getNamedEntity("camera");
    CameraComponent camComp = camera.getComponent(CameraComponent.class);
    Vector3 mousePos = camComp.getCamera().unproject(new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0));
    Vector2 mousePosV2 = new Vector2(mousePos.x, mousePos.y);
    GridPoint2 loc = ServiceLocator.getEntityService().getNamedEntity("terrain").getComponent(TerrainComponent.class)
        .worldToTilePosition(mousePosV2.x, mousePosV2.y);
    Vector2 worldLoc = ServiceLocator.getEntityService().getNamedEntity("terrain").getComponent(TerrainComponent.class)
        .tileToWorldPosition(loc);
    String entityName = "Temp";
    entityName = name + entityName;

    if (Objects.equals(name, "wall")) {
      tempEntity = StructureFactory.createWall(entityName);
    } else if (Objects.equals(name, "tower1")) {
      tempEntity = StructureFactory.createTower1(1, entityName);
    } else if (Objects.equals(name, "tower2")) {
      tempEntity = StructureFactory.createTower2(1, entityName);
    } else if (Objects.equals(name, "woodCutter")) {
      tempEntity = ResourceBuildingFactory.createWoodCutter(entityName);
    } else if (Objects.equals(name, "tower3")) {
      tempEntity = StructureFactory.createTower3(1, entityName);
    } else if (Objects.equals(name, "trap")) {
      tempEntity = StructureFactory.createTrap(entityName);
    } else if (Objects.equals(name, "stoneQuarry")) {
      tempEntity = ResourceBuildingFactory.createStoneQuarry(entityName);
    }
    // Update achievements for structures/building
    ServiceLocator.getAchievementHandler().getEvents().trigger(AchievementHandler.EVENT_ON_TEMP_STRUCTURE_PLACED, name);

    buildingTempEntity = true;
    tempEntityName = entityName;
    ServiceLocator.getEntityService().registerNamed(entityName, tempEntity);
    tempEntity.setPosition(worldLoc);
  }

  public static void setUiPopUp(int screenX, int screenY) {

    // getting the building location on the map
    Entity camera = ServiceLocator.getEntityService().getNamedEntity("camera");
    CameraComponent camComp = camera.getComponent(CameraComponent.class);
    Vector3 mousePos = camComp.getCamera().unproject(new Vector3(screenX, screenY, 0));
    Vector2 mousePosV2 = new Vector2(mousePos.x, mousePos.y);
    GridPoint2 mapPos = ServiceLocator.getEntityService().getNamedEntity("terrain").getComponent(TerrainComponent.class)
        .worldToTilePosition(mousePosV2.x, mousePosV2.y);
    // building name
    String structureName = ServiceLocator.getUGSService().getEntity(mapPos).getName();
    // if UI is false on click then the pop-up should appear
    if (!uiIsVisible) {
      uiPopUp = ServiceLocator.getEntityService().getNamedEntity("ui").getComponent(MainGameBuildingInterface.class)
          .makeUIPopUp(true, screenX, screenY, mapPos, structureName);
      uiIsVisible = true;
      // else the pop-up will be removed
    } else {
      if (uiIsVisible) {
        uiPopUp.remove();
        uiIsVisible = false;
      }
    }
  }

}