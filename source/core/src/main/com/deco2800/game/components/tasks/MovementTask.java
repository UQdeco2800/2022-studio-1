package com.deco2800.game.components.tasks;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.Vector;
import com.badlogic.gdx.math.Vector2;
import com.deco2800.game.ai.tasks.DefaultTask;
import com.deco2800.game.areas.terrain.TerrainComponent;
import com.deco2800.game.entities.UGS;
import com.deco2800.game.physics.components.PhysicsMovementComponent;
import com.deco2800.game.services.GameTime;
import com.deco2800.game.services.ServiceLocator;

import java.security.Provider.Service;
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
  private Vector2 origin;
  private GameTime gameTime;
  private long updateMoveTimeDelta = 1500;
  private long lastUpdateMoveTime = 0;
  private float stopDistance = 0;

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
    origin = owner.getEntity().getCenterPosition();
    path = generatePath();
  }

  @Override
  public void update() {
    if (gameTime.getTime() > lastUpdateMoveTime + updateMoveTimeDelta) {
      if (path != null && path.size() > 0) {
        if (ServiceLocator.getUGSService().checkEntityPlacement(new GridPoint2(path.get(0).x, path.get(0).y),
            "enemy")) {
          move(path.remove(0));
        }
      }

      // if (path == null || path.size() == 0) {
      // generatePath();
      // }

      lastUpdateMoveTime = gameTime.getTime();
    }

  }

  /**
   * Moves the entity to the specified tile
   * 
   * @param targetPosition tile coordinates to move to
   */
  private void move(LinkedPoint targetPosition) {
    UGS ugs = ServiceLocator.getUGSService();

    String originPositionString = ugs.getStringByEntity(owner.getEntity());
    String split[] = originPositionString.split(",");
    GridPoint2 playerCurrentPos = new GridPoint2(Integer.parseInt(split[0]), Integer.parseInt(split[1]));

    GridPoint2 movementVector = new GridPoint2(targetPosition.x - playerCurrentPos.x,
        targetPosition.y - playerCurrentPos.y);

    ugs.moveEntity(owner.getEntity(), playerCurrentPos, movementVector.x, movementVector.y);
  }

  /**
   * Heuristic for the Path finding algorithm. Checks the following conditions:
   * - First checks to see if the manhattan distance from the neighbour tile to
   * the target position is smaller than the distance from the original tile to
   * the target tile.
   * - Then checks if the tile is valid (not null) in the ugs.
   * - Then checks to see if the tile is not occupied
   * 
   * If all of these conditions are true, then the tile is valid
   * 
   * @param tile        neighbouring tile to check
   * @param origin      original tile
   * @param targetPoint end point
   * @return true if tile is valid, false otherwise.
   */
  private boolean validateTile(LinkedPoint tile, LinkedPoint origin, LinkedPoint targetPoint) {
    UGS ugs = ServiceLocator.getUGSService();

    // Heuristic - Manhattan distance
    int xDiff = Math.abs(targetPoint.x - tile.x);
    int yDiff = Math.abs(targetPoint.y - tile.y);

    int orgnXDiff = Math.abs(targetPoint.x - origin.x);
    int orgnYDiff = Math.abs(targetPoint.y - origin.y);

    if ((xDiff + yDiff) > (orgnXDiff + orgnYDiff + 2)) {
      return false;
    }

    if (ugs.getTile(ugs.generateCoordinate(tile.x, tile.y)) == null) {
      return false;
    }

    if (ugs.getEntity(new GridPoint2(tile.x, tile.y)) == null) {
      return true;
    }
    return false;
  }

  /**
   * Checks neighbouring tiles of the origin tile to see if they're valid or not,
   * and returns those that are.
   * 
   * @param origin      original tile
   * @param targetPoint endpoint
   * @return list of valid surrounding tiles
   */
  private ArrayList<LinkedPoint> findSurrounding(LinkedPoint origin, LinkedPoint targetPoint) {

    ArrayList<LinkedPoint> openTiles = new ArrayList<>();

    LinkedPoint up = new LinkedPoint(origin.x, origin.y + 1, origin);
    if (up.equals(targetPoint)) {
      openTiles.add(up);
      return openTiles;
    }

    if (validateTile(up, origin, targetPoint)) {
      openTiles.add(up);
    }

    LinkedPoint down = new LinkedPoint(origin.x, origin.y - 1, origin);
    if (down.equals(targetPoint)) {
      openTiles.clear();
      openTiles.add(down);
      return openTiles;
    }

    if (validateTile(down, origin, targetPoint)) {
      openTiles.add(down);
    }

    LinkedPoint left = new LinkedPoint(origin.x - 1, origin.y, origin);
    if (left.equals(targetPoint)) {
      openTiles.clear();
      openTiles.add(left);
      return openTiles;
    }

    if (validateTile(left, origin, targetPoint)) {
      openTiles.add(left);
    }

    LinkedPoint right = new LinkedPoint(origin.x + 1, origin.y, origin);
    if (right.equals(targetPoint)) {
      openTiles.clear();
      openTiles.add(right);
      return openTiles;
    }

    if (validateTile(right, origin, targetPoint)) {
      openTiles.add(right);
    }
    return openTiles;
  }

  /**
   * Generates the ~shortest~ path to the target position based on the owners
   * original position. This uses a simplified version of Djikstra's shortest path
   * algorithm: it checks all the starting points neighbouring tiles against a
   * heuristic to see if they're valid, then adds them to a list to check. These
   * tiles are then also have their neighbouring tiles checked against the
   * heuristic until the target position is found.
   * 
   * This algorithm has a fairly large overhead and is quite slow in large
   * distances. If the map plans to get any larger, change this algorithm.
   * 
   * @return path for the entity to follow as a list of points to move to.
   */
  private ArrayList<LinkedPoint> generatePath() {
    GridPoint2 ownerPos = ServiceLocator.getEntityService().getNamedEntity("terrain")
        .getComponent(TerrainComponent.class)
        .worldToTilePosition(origin.x, origin.y);

    GridPoint2 targetPos = ServiceLocator.getEntityService().getNamedEntity("terrain")
        .getComponent(TerrainComponent.class).worldToTilePosition(target.x, target.y);

    LinkedPoint startPosition = new LinkedPoint(ownerPos.x, ownerPos.y, null);
    LinkedPoint targetPosition = new LinkedPoint(targetPos.x, targetPos.y, null);

    boolean pathFound = false;

    ArrayList<LinkedPoint> path = new ArrayList<>();
    ArrayList<LinkedPoint> closed = new ArrayList<>();

    if (startPosition.equals(targetPosition))
      return null;

    closed.add(startPosition);

    while (!pathFound) {

      ArrayList<LinkedPoint> newPoints = new ArrayList<>();

      for (int i = 0; i < closed.size(); i++) {
        LinkedPoint point = closed.get(i);

        for (LinkedPoint surroundingPoint : findSurrounding(point, targetPosition)) {
          // System.out.println("Checking surrounding point: " +
          // surroundingPoint.toString());
          if (!closed.contains(surroundingPoint) && !newPoints.contains(surroundingPoint)) {
            newPoints.add(surroundingPoint);
          }
        }
      }

      for (LinkedPoint point : newPoints) {
        closed.add(point);

        if (point.equals(targetPosition)) { // path completed
          pathFound = true;
          break;
        }
      }

      if (!pathFound && newPoints.isEmpty()) {
        return null;
      }
    }

    LinkedPoint point = closed.get(closed.size() - 1);
    while (point.equals(targetPosition) || point.previous != null) {
      path.add(0, point);
      point = point.previous;
    }

    return path;

  }

  public void setTarget(Vector2 target) {
    this.target = target;
  }

  public void updateOrigin(Vector2 originPosition) {
    this.origin = originPosition;
    path = generatePath();
  }

  public ArrayList<LinkedPoint> getPath() {
    return this.path;
  }
}

/**
 * LinkedPoint represents a coordinate, and knows the previous coordinate it
 * spawned from.
 */
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

  @Override
  public int hashCode() {
    return super.hashCode();
  }
}
