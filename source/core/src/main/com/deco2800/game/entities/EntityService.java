package com.deco2800.game.entities;

import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.deco2800.game.areas.terrain.TerrainComponent;
import com.deco2800.game.services.ServiceLocator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

/**
 * Provides a global access point for entities to register themselves. This allows for iterating
 * over entities to perform updates each loop. All game entities should be registered here.
 * Avoid adding additional state here! Global access is often the easy but incorrect answer to
 * sharing data.
 */
public class EntityService {
  private static final Logger logger = LoggerFactory.getLogger(EntityService.class);
  private static final int INITIAL_CAPACITY = 40;

  private final Array<Entity> entities = new Array<>(false, INITIAL_CAPACITY);
  private final Map<String, Entity> namedEntities = new HashMap<>();

  private Hashtable<Vector2, Entity> entityMap = new Hashtable<>();
  private Hashtable<String, List<Boolean>> tileMapping = new Hashtable<>();
  private boolean currentWorldStep;
  private ArrayList<Entity> toDestroyEntities = new ArrayList<>();

  //You may ask why a second map instead of entities? I honestly have no clue
  //but this was the only way I could get a list of entities without crashing while looping in a component
  private Hashtable<Integer, Entity> enemyMap = new Hashtable<>();

  /**
   * Register a new entity with the entity service. The entity will be created and start updating.
   * @param entity new entity.
   */
  public void register(Entity entity) {
    logger.debug("Registering {} in entity service", entity);
    entities.add(entity);
    enemyMap.put(entity.getId(), entity);
    entity.create();
  }

  /**
   * Registers an entity with a name so it can be found later
   *
   * @param name the name to register it as (must be unique or will overwrite)
   * @param entity the entity to register
   */
  public void registerNamed(String name, Entity entity) {
    this.namedEntities.put(name, entity);
    this.register(entity);
  }

  public void setCurrentWorldStep(Boolean step) {
    this.currentWorldStep = step;
  }

  public boolean getCurrentWorldStep() {
    return this.currentWorldStep;
  }

  public ArrayList<Entity> getToDestroyEntities() {
    return this.toDestroyEntities;
  }

  public void addToDestroyEntities(Entity e) {
    this.toDestroyEntities.add(e);
  }


  /**
   * Returns a registered named entity
   * @param name the name the entity was registered as
   * @return the registered entity or null
   */
  public Entity getNamedEntity(String name) {
    return this.namedEntities.get(name);
  }

  public void unregisterNamed(String key) {
    this.namedEntities.remove(key);
  }

  /**
   * Returns the last registered entity
   * @return the last registered entity or null
   */
  public Entity getLastEntity() {
    return this.entities.get(this.entities.size-1);
  }

  /**
   * Returns all registered entities
   * @return all registered entities or null
   */
  public Map<String, Entity> getAllNamedEntities() {
    return this.namedEntities;
  }


  /**
   * Unregister an entity with the entity service. The entity will be removed and stop updating.
   * @param entity entity to be removed.
   */
  public void unregister(Entity entity) {
    logger.debug("Unregistering {} in entity service", entity);
    if (ServiceLocator.getUGSService() != null) {
      ServiceLocator.getUGSService().dispose(entity);
    }
    entities.removeValue(entity, true);
    Vector2 toRemove = null;
    for (Map.Entry<Vector2, Entity> e : entityMap.entrySet()) {
      if (e.getValue().equals(entity)) {
        toRemove = e.getKey();
      }
    }
    if (null != toRemove) {
      entityMap.remove(toRemove);
    }
  }

  public void removeNamedEntity (String name, Entity entity) {
    logger.debug("Unregistering {} in entity service", entity);
    this.namedEntities.remove(name, entity);
    this.unregister(entity);
  }

  /**
   * Update all registered entities. Should only be called from the main game loop.
   */
  public void update() {
    for (Entity entity : entities) {
      entity.earlyUpdate();
      entity.update();
    }
  }

  /**
   * Dispose all entities.
   */
  public void dispose() {
    for (Entity entity : entities) {
      entity.dispose();
    }
  }

  /**
   * @return Collection of entities stored on the map
   */
  public Collection<Entity> getEntities() {
    return entityMap.values();
  }

  /**
   * @return Collection of entities stored on the map. This must be done for looping within components
   */
  public Collection<Entity> getEnemyEntities() {
    return entityMap.values();
  }

  /**
   * Adds a new entity to hashtable
   * @param newEntity new entity to be added to the environment
   */
  public void addEntity(Entity newEntity) {
    if (newEntity.getCenterPosition() == null) {
      entityMap.put(new Vector2(0,0), newEntity);
    } else {
      entityMap.put(newEntity.getCenterPosition(), newEntity);
    }
  }

  /**
   * Finds the closet entity based off euclidean distance from a given x,y point
   * @param x cell cord
   * @param y cell cord
   * @return Entity closet
   */
  public Entity findClosestEntity(int x, int y) {
    if (entityMap.values().size() == 0) {
      return null;
    }

    Entity closetEntity = null;
     float smallestDistance = 99999;

    for (Entity entity: entityMap.values()) {
      if (!(entity instanceof Enemy) && (!"player".equalsIgnoreCase(entity.getName()))) {
        float entityX = entity.getCenterPosition().x;
        float entityY = entity.getCenterPosition().y;

        double currentDistance = Math.sqrt(Math.pow(Math.abs(x - entityX), 2) + Math.pow(Math.abs(y - entityY), 2));

        if (currentDistance < smallestDistance) {
          closetEntity = entity;
          smallestDistance = (float) currentDistance;
        }
      }
    }

    return closetEntity;
  }

  /**
   * Finds the closet entity based off euclidean distance from a given x,y point
   * @param x cell cord
   * @param y cell cord
   * @return Entity closet
   */
  public Entity findClosestEnemy(int x, int y) {
    if (entityMap.values().size() == 0) {
      return null;
    }

    Entity closetEntity = null;
    float smallestDistance = 99999;

    for (Entity entity: entityMap.values()) {
      if (entity instanceof Enemy && (!"player".equalsIgnoreCase(entity.getName()))) {
        float entityX = entity.getCenterPosition().x;
        float entityY = entity.getCenterPosition().y;

        double currentDistance = Math.sqrt(Math.pow(Math.abs(x - entityX), 2) + Math.pow(Math.abs(y - entityY), 2));

        if (currentDistance < smallestDistance) {
          closetEntity = entity;
          smallestDistance = (float) currentDistance;
        }
      }
    }

    return closetEntity;
  }

  /**
   * Checks a given tile x,y is water
   * @param x the x pos of the tile
   * @param y the y pos of the tile
   * @return true if the tile is water else false
   */
  private boolean checkTileIsWater(int x, int y) {
    TerrainComponent terrain = ServiceLocator.getEntityService().getNamedEntity("terrain").getComponent(TerrainComponent.class);
    TiledMapTileLayer layer = (TiledMapTileLayer) terrain.getMap().getLayers().get(0);

    if (x >= layer.getWidth() || y >= layer.getHeight() || x < 0 || y < 0) {
      return true;
    }

    String tile = layer.getCell(x, y).getTile().getTextureRegion().getTexture().toString();

    if (tileMapping.containsKey(tile)) {
      return tileMapping.get(tile).get(0);
    }
    return false;
  }
}
