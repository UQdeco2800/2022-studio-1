package com.deco2800.game.areas;

import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Disposable;
import com.deco2800.game.areas.terrain.EnvironmentalCollision;
import com.deco2800.game.areas.terrain.TerrainComponent;
import com.deco2800.game.entities.Entity;
import com.deco2800.game.services.ServiceLocator;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Represents an area in the game, such as a level, indoor area, etc. An area
 * has a terrain and
 * other entities to spawn on that terrain.
 *
 * <p>
 * Support for enabling/disabling game areas could be added by making this a
 * Component instead.
 */
public abstract class GameArea implements Disposable {
  protected TerrainComponent terrain;
  protected List<Entity> areaEntities;

  protected Entity player;
  protected Entity crystal;
  protected EnvironmentalCollision entityMapping;

  protected GameArea() {
    areaEntities = new ArrayList<>();
  }

  /** Create the game area in the world. */
  public abstract void create();

  /** Dispose of all internal entities in the area */
  public void dispose() {
    for (Entity entity : areaEntities) {
      entity.dispose();
    }
  }

  /**
   * Spawn entity at its current position
   *
   * @param entity Entity (not yet registered)
   */
  protected void spawnEntity(Entity entity) {
    areaEntities.add(entity);
  }

  protected void removeEntity(Entity entity) {
    areaEntities.remove(entity);
    ServiceLocator.getEntityService().unregister(entity);
  }

  /**
   * Spawn entity on a given tile. Requires the terrain to be set first.
   *
   * @param entity  Entity (not yet registered)
   * @param tilePos tile position to spawn at
   * @param centerX true to center entity X on the tile, false to align the bottom
   *                left corner
   * @param centerY true to center entity Y on the tile, false to align the bottom
   *                left corner
   */
  protected void spawnEntityAt(
      Entity entity, GridPoint2 tilePos, boolean centerX, boolean centerY) {
    Vector2 worldPos = terrain.tileToWorldPosition(tilePos);
    entity.setTileGridPosition(tilePos);
    entity.setPosition(worldPos);
    spawnEntity(entity);
  }

  protected boolean isWallHere(GridPoint2 tilePos) {
    Vector2 worldPos = terrain.tileToWorldPosition(tilePos);

    Iterator<Entity> itr = areaEntities.listIterator();
    while (itr.hasNext()) {
      Entity entity = itr.next();
      Vector2 entityPos = entity.getPosition();
      if (entity.getName() == null || entity.getName().equals("wall")) {
        continue;
      }
      if (worldPos.x == entityPos.x && worldPos.y == entityPos.y) {
        return true;
      }
    }
    return false;
  }

  public Entity getPlayer() {
    return player;
  }

  public EnvironmentalCollision getEntityMapping() {
    return entityMapping;
  }

}
