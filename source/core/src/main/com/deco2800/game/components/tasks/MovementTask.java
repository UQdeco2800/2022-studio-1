package com.deco2800.game.components.tasks;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.Vector;
import com.badlogic.gdx.math.Vector2;
import com.deco2800.game.ai.tasks.DefaultTask;
import com.deco2800.game.areas.terrain.TerrainComponent;
import com.deco2800.game.physics.components.PhysicsMovementComponent;
import com.deco2800.game.services.GameTime;
import com.deco2800.game.services.ServiceLocator;

import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Move to a given position, finishing when you get close enough. Requires an
 * entity with a
 * PhysicsMovementComponent.
 */
public class MovementTask extends DefaultTask {
  private static final Logger logger = LoggerFactory.getLogger(MovementTask.class);

  private Vector2 target;
  private GameTime gameTime;
  private long updateTimeDelta = 500;
  private long lastUpdateTime = 0;
  private float stopDistance;

  private ArrayList<LinkedPoint> path;
  private int currentStep = 0;

  public MovementTask(Vector2 target) {
    this.target = target;
    this.gameTime = ServiceLocator.getTimeSource();
  }

  public MovementTask(Vector2 target, float stopDistance) {
    this(target);
    this.stopDistance = stopDistance;
  }

  @Override
  public void start() {
    super.start();

    path = generatePath();

  }

  @Override
  public void update() {

    if (gameTime.getTime() > lastUpdateTime + updateTimeDelta) {

      path = generatePath();
      if (path != null) {
        move(path.remove(0));
      }
      lastUpdateTime = gameTime.getTime();
    }

  }

  private void move(LinkedPoint targetPosition) {

  }

  private ArrayList<LinkedPoint> findSurrounding(LinkedPoint origin) {

    ArrayList<LinkedPoint> openTiles = new ArrayList<>();

    for (int x = -1; x < 2; x += 2) {
      for (int y = -1; y < 2; y += 2) {

        LinkedPoint tile = new LinkedPoint(origin.x + x, origin.y + y, origin);

        if (ServiceLocator.getUGSService().getEntity(new GridPoint2(tile.x, tile.y)) == null) {
          openTiles.add(tile);
        }
      }
    }

    return openTiles;
  }

  private ArrayList<LinkedPoint> generatePath() {
    GridPoint2 ownerPos = ServiceLocator.getEntityService().getNamedEntity("terrain")
        .getComponent(TerrainComponent.class)
        .worldToTilePosition(owner.getEntity().getCenterPosition().x, owner.getEntity().getCenterPosition().y);

    GridPoint2 targetPos = ServiceLocator.getEntityService().getNamedEntity("terrain")
        .getComponent(TerrainComponent.class).worldToTilePosition(target.x, target.y);

    LinkedPoint startPosition = new LinkedPoint(ownerPos.x, ownerPos.y, null);
    LinkedPoint targetPosition = new LinkedPoint(targetPos.x, targetPos.y, null);

    boolean pathFound = false;

    ArrayList<LinkedPoint> path = new ArrayList<>();
    ArrayList<LinkedPoint> used = new ArrayList<>();

    if (startPosition.equals(targetPosition))
      return null;

    used.add(startPosition);

    while (!pathFound) {

      ArrayList<LinkedPoint> newPoints = new ArrayList<>();

      for (int i = 0; i < used.size(); i++) {
        LinkedPoint point = used.get(i);

        for (LinkedPoint surroundingPoint : findSurrounding(point)) {
          if (!used.contains(surroundingPoint) && !newPoints.contains(surroundingPoint)) {
            newPoints.add(surroundingPoint);
          }
        }
      }

      for (LinkedPoint point : newPoints) {
        used.add(point);
        if (point.equals(targetPosition)) { // path completed
          pathFound = true;
          break;
        }
      }

      if (!pathFound && newPoints.isEmpty()) {
        return null;
      }
    }

    LinkedPoint point = used.get(used.size() - 1);
    while (point.previous != null) {
      path.add(0, point);
      point = point.previous;
    }

    return path;

  }
}

class LinkedPoint {

  public int x;
  public int y;
  public LinkedPoint previous;

  public LinkedPoint(int x, int y, LinkedPoint previous) {
    this.x = x;
    this.y = y;
    this.previous = previous;
  }

  @Override
  public String toString() {
    return "{" + x + ", " + y + "}";
  }

  @Override
  public boolean equals(Object o) {
    if (!(o instanceof LinkedPoint))
      return false;
    LinkedPoint obj = (LinkedPoint) o;
    return obj.x == this.x && obj.y == this.y;
  }

}
