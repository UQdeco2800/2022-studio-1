package com.deco2800.game.entities;

import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.deco2800.game.areas.terrain.TerrainComponent;
import com.deco2800.game.components.Environmental.EnvironmentalComponent;
import com.deco2800.game.services.ServiceLocator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

/**
 * Provides a global access point for entities to register themselves. This allows for iterating
 * over entities to perform updates each loop. All game entities should be registered here.
 *
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

  /**
   * Register a new entity with the entity service. The entity will be created and start updating.
   * @param entity new entity.
   */
  public void register(Entity entity) {
    logger.debug("Registering {} in entity service", entity);
    entities.add(entity);
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

  /**
   * Returns a registered named entity
   * @param name the name the entity was registered as
   * @return the registered entity or null
   */
  public Entity getNamedEntity(String name) {
    return this.namedEntities.get(name);
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
    entities.removeValue(entity, true);
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
   * Adds a new entity to hashtable
   * @param newEntity new entity to be added to the environment
   */
  public void addEntity(Entity newEntity) {
    entityMap.put(newEntity.getCenterPosition(), newEntity);
  }

  /**
   * Finds the closet entity based off euclidean distance from a given x,y point
   * @param x cell cord
   * @param y cell cord
   * @return Entity closet
   */
  public Entity findClosetEntity(int x, int y) {
    if (entityMap.values().size() == 0) {
      return null;
    }

    Entity closetEntity = null;
    float smallestDistance = 99999;

    for (Entity entity: entityMap.values()) {
      float entityX = entity.getCenterPosition().x;
      float entityY = entity.getCenterPosition().y;

      double currentDistance = Math.sqrt(Math.pow(Math.abs(x - entityX), 2) + Math.pow(Math.abs(y - entityY), 2));

      if (currentDistance < smallestDistance) {
        closetEntity = entity;
      }
    }

    return closetEntity;
  }


  /**
   * Calculates if the given entity will collide with already existing entities
   * Still in testing phase. Uses the scale x and scale y of the entity to determine collision/hitbox
   * size
   *
   * Due to isometric view world positions must be used thus the conversion from cell coordinates
   *
   * @param potentialEntity The new entity to be added to the map
   * @param xPotential the proposed x cell position of the entity
   * @param yPotential the proposed y cell position of the entity
   * @return true if a collision would occur else false
   */
  public Boolean wouldCollide(Entity potentialEntity, int xPotential, int yPotential) {
    //if empty no collisions to check:
    if (entityMap.values().size() == 0) {
      return false;
    }

    //convert to world positions
    TerrainComponent terrain = ServiceLocator.getEntityService().getNamedEntity("terrain").getComponent(TerrainComponent.class);
    float x = terrain.tileToWorldPosition(xPotential, yPotential).x;
    float y = terrain.tileToWorldPosition(xPotential, yPotential).y;

    //x,y positions of potential entity
    float potentialEntityTop = y + potentialEntity.getScale().y / 2;
    float potentialEntityBottom = y - potentialEntity.getScale().y / 2;
    float potentialEntityRight = x + potentialEntity.getScale().x / 2;
    float potentialEntityLeft = x - potentialEntity.getScale().x / 2;

    for (Entity entity: entityMap.values()) {
      //x,y positions of current entity
      float placedRight = entity.getCenterPosition().x + entity.getScale().x / 2;
      float placedLeft = entity.getCenterPosition().x - entity.getScale().x / 2;
      float placedTop = entity.getCenterPosition().y + entity.getScale().y / 2;
      float placedBottom = entity.getCenterPosition().y - entity.getScale().y / 2;

      //check if collision occurs with current entity
      if (!(potentialEntityRight <  placedLeft || potentialEntityLeft > placedRight)
              && (!(potentialEntityBottom > placedTop || potentialEntityTop < placedBottom))) {
        return true;
      }
    }
    return false;
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

  /**
   * Checks the current tile and all tiles around it for a water tile. Returns true
   *  if near water. This is necessary as world pos doesnt perfectly allign to cell positions
   *  therefore a buffer must be introduced
   * @param x the tile's x cord
   * @param y the tile's y cord
   * @return true if near water else false
   */
  public boolean isNearWater(int x, int y) {
    if (checkTileIsWater(x + 1, y) || checkTileIsWater(x - 1, y) || checkTileIsWater(x, y - 1)
            || checkTileIsWater(x, y  + 1) || checkTileIsWater(x , y)) {
      return true;
    }
    return false;
  }
}
