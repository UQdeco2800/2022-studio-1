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
public class EntityService {
  private static final Logger logger = LoggerFactory.getLogger(EntityService.class);
  private static final int INITIAL_CAPACITY = 16;

  private final Array<Entity> entities = new Array<>(false, INITIAL_CAPACITY);

  private final Map<String, Entity> namedEntities = new HashMap<>();

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
   * Unregister an entity with the entity service. The entity will be removed and stop updating.
   * @param entity entity to be removed.
   */
  public void unregister(Entity entity) {
    logger.debug("Unregistering {} in entity service", entity);
    entities.removeValue(entity, true);
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
}
