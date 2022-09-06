package com.deco2800.game.entities;

import com.badlogic.gdx.utils.Array;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

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
}
