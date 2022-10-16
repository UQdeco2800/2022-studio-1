package com.deco2800.game.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.Vector;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Array;
import com.deco2800.game.areas.terrain.TerrainComponent;
import com.deco2800.game.components.CameraComponent;
import com.deco2800.game.components.CombatStatsComponent;
import com.deco2800.game.components.maingame.MainGameBuildingInterface;
import com.deco2800.game.components.player.InventoryComponent;
import com.deco2800.game.entities.factories.ResourceBuildingFactory;
import com.deco2800.game.entities.factories.StructureFactory;
import com.deco2800.game.rendering.TextureRenderComponent;
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

  private static Array<Entity> structureEntities = new Array<>(false, INITIAL_CAPACITY); // Deprecate

  private final Map<String, Entity> namedStructureEntities = new HashMap<String, Entity>(); // Deprecate

  private static boolean uiIsVisible;

  private static Table uiPopUp;

  private static Entity tempEntity;

  private static Boolean buildingTempEntity = false;

  private static String tempEntityName;

  private static int orientation = 0;

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
   * Registers an entity with a name, so it can be found later
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
          structure = StructureFactory.createWall(entityName, false, orientation);
          break;
        case "tower1":
          structure = StructureFactory.createTower1(1, entityName, false, orientation);
          break;
        case "tower2":
          structure = StructureFactory.createTower2(1, entityName, false, orientation);
          break;
        case "tower3":
          structure = StructureFactory.createTower3(1, entityName, false);
          break;
        case "trap":
          structure = StructureFactory.createTrap(entityName, false);
          break;
        case "turret":
          structure = StructureFactory.createTurret(entityName);
          structureEntities.add(structure);
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
      ServiceLocator.getUGSService().setEntity(gridPos, structure, entityName);
      ServiceLocator.getUGSService().addStructure(structure);
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
    //MAKE ALL TEMP STRUCTURES TRANSPARENT TO ADD TO THE GHOST STRUCTURE EFFECT
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

    // @TODO change to switch statement for efficiency
    if (Objects.equals(name, "wall")) {
      tempEntity = StructureFactory.createWall(entityName, true, orientation);
    } else if (Objects.equals(name, "tower1")) {
      tempEntity = StructureFactory.createTower1(1, entityName, true, orientation);
    } else if (Objects.equals(name, "tower2")) {
      tempEntity = StructureFactory.createTower2(1, entityName, true, orientation);
    } else if (Objects.equals(name, "woodCutter")) {
      tempEntity = ResourceBuildingFactory.createWoodCutter(entityName);
    } else if (Objects.equals(name, "tower3")) {
      tempEntity = StructureFactory.createTower3(1, entityName, true);
    } else if (Objects.equals(name, "trap")) {
      tempEntity = StructureFactory.createTrap(entityName, true);
    } else if (Objects.equals(name, "stoneQuarry")) {
      tempEntity = ResourceBuildingFactory.createStoneQuarry(entityName);
    } else if (Objects.equals(name, "turret")) {
      tempEntity = StructureFactory.createTurret(entityName);
    }
    // Update achievements for structures/building
    //This is not a successfully built building, so I don't think it warrants an achievement
    //ServiceLocator.getAchievementHandler().getEvents().trigger(AchievementHandler.EVENT_ON_TEMP_STRUCTURE_PLACED, name);

    buildingTempEntity = true;
    tempEntityName = entityName;
    ServiceLocator.getEntityService().registerNamed(entityName, tempEntity);
    float tileSize = ServiceLocator.getEntityService().getNamedEntity("terrain").getComponent(TerrainComponent.class).getTileSize();
    worldLoc.x -= tileSize/4;
    worldLoc.y -= tileSize/8;
    tempEntity.setPosition(worldLoc);
    drawVisualFeedback(loc, "structure");
  }

  /**
   * Draw the coloured tiles around a structure to show the player where they can build a structure
   * @param centerCoord location of the structure in gridPoint2
   * @param entityType type of entity being checked
   */
  public static void drawVisualFeedback(GridPoint2 centerCoord, String entityType) {
    HashMap<GridPoint2, String> surroundingTiles = ServiceLocator.getUGSService().getSurroundingTiles(centerCoord, entityType, 1);
    for (GridPoint2 mapPos: surroundingTiles.keySet()) {
      String entityName = "visual" + mapPos.toString();
      Entity visualTile;
      if (surroundingTiles.get(mapPos).equals("empty")) {
        visualTile = StructureFactory.createVisualFeedbackTile(entityName, "images/65x33_tiles/validTile.png");
      } else {
        visualTile = StructureFactory.createVisualFeedbackTile(entityName, "images/65x33_tiles/invalidTile.png");
      }
      ServiceLocator.getEntityService().registerNamed(entityName, visualTile);
      Vector2 worldLoc = ServiceLocator.getEntityService().getNamedEntity("terrain").getComponent(TerrainComponent.class).tileToWorldPosition(mapPos);
      float tileSize = ServiceLocator.getEntityService().getNamedEntity("terrain").getComponent(TerrainComponent.class).getTileSize();
      worldLoc.x -= tileSize/4;
      worldLoc.y -= tileSize/8;
      visualTile.setPosition(worldLoc);
    }
  }

  /**
   * Clear the coloured visual tiles by disposing of them
   */
  public static void clearVisualTiles() {
    for (Entity e : ServiceLocator.getEntityService().getAllNamedEntities().values()) {
      String entityName = e.getName();
      if (entityName != null) {
        if (entityName.contains("visual")) {
          e.dispose();
        }
      }
    }
  }

  /**
   * Rotate the current temp structure
   */
  public static void rotateTempStructure() {
    
    toggleStructureOrientation();
    
    ServiceLocator.getEntityService().getNamedEntity(getTempEntityName()).dispose();
    clearVisualTiles();
    String entityName = getTempEntityName();
    entityName = entityName.replace("Temp", "");
    buildTempStructure(entityName);
  }

  /**
   * get the orientation of the structure being built (facing forward or facing right)
   * @return
   */
  public static int getStructureOrientation() {return orientation;}

  /**
   * toggle the structure orientation from 0 to 1 or from 1 to 0
   */
  public static void toggleStructureOrientation() {
    if (orientation == 1) {
      orientation = 0;
    } else {
      orientation = 1;
    }
  }

  public static void setUiPopUp(int screenX, int screenY) {
    //getting the building location on the map
    Entity camera = ServiceLocator.getEntityService().getNamedEntity("camera");
    CameraComponent camComp = camera.getComponent(CameraComponent.class);
    Vector3 mousePos = camComp.getCamera().unproject(new Vector3(screenX, screenY, 0));
    Vector2 mousePosV2 = new Vector2(mousePos.x, mousePos.y);
    GridPoint2 mapPos = ServiceLocator.getEntityService().getNamedEntity("terrain").getComponent(TerrainComponent.class)
            .worldToTilePosition(mousePosV2.x, mousePosV2.y);
    //if UI is false on click then the pop-up should appear
    if (!uiIsVisible) {
      try {
        String structureName = ServiceLocator.getUGSService().getEntity(mapPos).getName();

        uiPopUp = ServiceLocator.getEntityService().getNamedEntity("ui").getComponent(MainGameBuildingInterface.class)
                .makeUIPopUp(true, screenX, screenY, mapPos, structureName);
        uiIsVisible = true;

        // else the pop-up will be removed
      } catch(NullPointerException e) {
        logger.debug("Null error in UGS, you did not click a building");
      }
    } else {
      uiPopUp.remove();
      uiIsVisible = false;
    }
  }

  public String SellBuilding(String buildingName, GridPoint2 entityCords) {
    Entity clickedStructure = ServiceLocator.getUGSService().getEntity(entityCords);
    int health = clickedStructure.getComponent(CombatStatsComponent.class).getHealth();

    Entity player = ServiceLocator.getEntityService().getNamedEntity("player");
    int stone = 0;
    int wood = 0;


   if (health < 10) {
     stone = 10;
     wood = 4;
     player.getComponent(InventoryComponent.class).addStone(10);
     player.getComponent(InventoryComponent.class).addWood(4);

   } else if (health < 20) {
     stone = 20;
     wood = 8;

   }else if( health < 30) {
     stone = 30;
     wood = 5;
   }else if(health < 40) {
     stone = 40;
     wood = 8;

   }else if (health < 50) {
     stone = 50;
     wood = 10;

   }else if (health < 60) {
     stone = 60;
     wood = 15;

   } else if (health < 70) {
     stone = 70;
     wood = 20;
   }else if (health < 80) {
     stone = 80;
     wood = 25;


   }else if (health < 90) {
     stone = 90;
     wood = 30;
   }
    String stoneAndWood = stone + "," + wood;
    return stoneAndWood;
  }

}