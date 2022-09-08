package com.deco2800.game.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Array;
import com.deco2800.game.components.CameraComponent;
import com.deco2800.game.components.maingame.MainGameBuildingInterface;
import com.deco2800.game.components.player.PlayerStatsDisplay;
import com.deco2800.game.entities.factories.StructureFactory;
import com.deco2800.game.services.ServiceLocator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


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
public class StructureService extends EntityService{
  private static final Logger logger = LoggerFactory.getLogger(StructureService.class);
  private static final int INITIAL_CAPACITY = 40;

  private final Array<Entity> structureEntities = new Array<>(false, INITIAL_CAPACITY);

  private final Map<String, Entity> namedStructureEntities = new HashMap<>();

  public static HashMap<String, Table> tables = new HashMap();

  private static boolean uiIsVisible;

  private static Table table1;



  /**
   * Register a new entity with the entity service. The entity will be created and start updating.
   * @param entity new entity.
   */
  @Override
  public void register(Entity entity) {
    logger.debug("Registering {} in entity service", entity);
    structureEntities.add(entity);
    entity.create();
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
   * @param structureRects map of all structure selection rectangles to the structure name in game entity list
   */
  public static void triggerBuildEvent(String name, SortedMap<String, Rectangle> structureRects) {
    Entity camera = ServiceLocator.getEntityService().getNamedEntity("camera");
    CameraComponent camComp = camera.getComponent(CameraComponent.class);
    Vector3 mousePos = camComp.getCamera().unproject(new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0));
    Vector2 mousePosV2 = new Vector2(mousePos.x, mousePos.y);
    mousePosV2.x -= 0.5;
    mousePosV2.y -= 0.5;
    String entityName = String.valueOf(ServiceLocator.getTimeSource().getTime());
    entityName = name + entityName;
    if (!uiIsVisible) {
      if (Objects.equals(name, "wall")) {
        ServiceLocator.getStructureService().registerNamed(entityName, StructureFactory.createWall());
        ServiceLocator.getStructureService().getNamedEntity(entityName).setPosition(mousePosV2);
        Rectangle rectangle = new Rectangle(mousePosV2.x, mousePosV2.y, 1, 1);
        structureRects.put(entityName, rectangle);
      } else if (Objects.equals(name, "tower1")) {
        ServiceLocator.getStructureService().registerNamed(entityName, StructureFactory.createTower1(1));
        ServiceLocator.getStructureService().getNamedEntity(entityName).setPosition(mousePosV2);
        Rectangle rectangle = new Rectangle(mousePosV2.x, mousePosV2.y, 1, 1);
        structureRects.put(entityName, rectangle);
      }
    }
  }

  /**
   * Checks if a structure on the map has been clicked. If it has been clicked then that structure gets removed from the game
   * @param screenX The x coordinate, origin is in the upper left corner
   * @param screenY The y coordinate, origin is in the upper left corner
   * @param structureRects map of all structure selection rectangles to the structure name in game entity list
   * @param resourceBuildState true if building resource building false otherwise
   * @param buildEvent true if currently in a build event false otherwise
   * @return list of booleans[]{true if the point (screenX, screenY) is clear of structures else return false,
   *                            resourceBuildState, buildEvent}
   *
   */
  public static boolean[] handleClickedStructures(int screenX, int screenY, SortedMap<String, Rectangle> structureRects, boolean resourceBuildState, boolean buildEvent) {
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
        //ServiceLocator.getStructureService().getNamedEntity(es.getKey()).dispose();
        anyStructureHit = true;
        //This block of code executes when the user clicks a structure
      } else {
        //This block of code executes when the user clicks, and it is not a structure
        if (uiIsVisible) {
          table1.remove();
          toggleUIisVisible();
        }
      }
    }
    if (anyStructureHit) {
      buildEvent = false;
      isClear = false;
      table1 = ServiceLocator.getEntityService().getNamedEntity("ui").getComponent(MainGameBuildingInterface.class).makeUIPopUp(true);
      toggleUIisVisible();
      //structureRects.remove(clickedStructure);
    } else {
      isClear = true;
    }
    return new boolean[]{isClear, resourceBuildState, buildEvent};
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

  /**
   * Method stub for returning structureRects map
   */
  public SortedMap<String, Rectangle> getStructureRects() {
      
    SortedMap<String, Rectangle> structureRects = new TreeMap<>();
    return structureRects;

  }
}
