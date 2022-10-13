package com.deco2800.game.physics.components;

import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.deco2800.game.ai.movement.MovementController;
import com.deco2800.game.areas.terrain.TerrainComponent;
import com.deco2800.game.components.Component;
import com.deco2800.game.entities.Entity;
import com.deco2800.game.entities.Tile;
import com.deco2800.game.entities.UGS;
import com.deco2800.game.services.ServiceLocator;
import com.deco2800.game.utils.math.Vector2Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Map;
import java.util.Objects;

/** Movement controller for a physics-based entity. */
public class PhysicsMovementComponent extends Component implements MovementController {
  private static final Logger logger = LoggerFactory.getLogger(PhysicsMovementComponent.class);

  private Vector2 maxSpeed = Vector2Utils.ONE;
  private Vector2 defaultMaxSpeed = Vector2Utils.ONE;

  private PhysicsComponent physicsComponent;
  private Vector2 targetPosition;
  private boolean movementEnabled = true;

  @Override
  public void create() {
    physicsComponent = entity.getComponent(PhysicsComponent.class);
    this.defaultMaxSpeed = new Vector2(1,1);
  }

  @Override
  public void update() {
    if (movementEnabled && targetPosition != null) {
      Body body = physicsComponent.getBody();
      updateDirection(body);
    }
  }

  /**
   * Enable/disable movement for the controller. Disabling will immediately set velocity to 0.
   *
   * @param movementEnabled true to enable movement, false otherwise
   */
  @Override
  public void setMoving(boolean movementEnabled) {
    this.movementEnabled = movementEnabled;
    if (!movementEnabled) {
      Body body = physicsComponent.getBody();
      setToVelocity(body, Vector2.Zero);
    }
  }

  @Override
  public boolean getMoving() {
    return movementEnabled;
  }

  /** @return Target position in the world */
  @Override
  public Vector2 getTarget() {
    return targetPosition;
  }

  /**
   * Set a target to move towards. The entity will be steered towards it in a straight line, not
   * using pathfinding or avoiding other entities.
   *
   * @param target target position
   */
  @Override
  public void setTarget(Vector2 target) {
    logger.trace("Setting target to {}", target);
    this.targetPosition = target;
  }

  private void updateDirection(Body body) {
    Vector2 desiredVelocity = getDirection().scl(maxSpeed);
    setToVelocity(body, desiredVelocity);
    if (ServiceLocator.getTimeSource().getTime() % 500 < 2) {
      updateEnemyPosInUgs();
    }
  }

  private void setToVelocity(Body body, Vector2 desiredVelocity) {
    // impulse force = (desired velocity - current velocity) * mass
    Vector2 velocity = body.getLinearVelocity();
    Vector2 impulse = desiredVelocity.cpy().sub(velocity).scl(body.getMass());
    body.applyLinearImpulse(impulse, body.getWorldCenter(), true);
  }

  private Vector2 getDirection() {
    // Move towards targetPosition based on our current position
    return targetPosition.cpy().sub(entity.getPosition()).nor();
  }

  /**
   * @return the current speed of the entity
   */
  public Vector2 getSpeed() {
    return this.maxSpeed;
  }

  /**
   * Sets the fault speed of the entity
   * @param speed vector of speed in x,y
   */
  public void setOriginalSpeed(Vector2 speed) {
    this.defaultMaxSpeed = speed;
    this.setNewSpeed(speed);
  }

  /**
   * set new speed which can differ to default speed
   * @param speed vector of speed in x,y
   */
  public void setNewSpeed(Vector2 speed) {
    this.maxSpeed = speed;
  }

  /**
   * Reset Entity to Original Speed
   */
  public void resetSpeed() {
    this.maxSpeed = defaultMaxSpeed;
  }

  public void updateEnemyPosInUgs () {
    // Initialise
    ArrayList<Entity> enemyList = null;
    if (ServiceLocator.getEntityService() != null && ServiceLocator.getEntityService().getEnemyEntities() != null) {
      enemyList = new ArrayList<>(ServiceLocator.getEntityService().getEnemyEntities());
    }

    for (Entity enemy : enemyList) {
      if (enemy.getName() != null && enemy.getName().contains("Mr.")) {
        // Get old key (place in UGS)
        String enemyOldKey = "";
        String oldTileType = "";
        String enemyName = null;
        for (Map.Entry<String, Tile> entry : ServiceLocator.getUGSService().printUGS().entrySet()) {
          if (entry.getValue().getEntity() == enemy) {
            enemyOldKey = entry.getKey();
            oldTileType = entry.getValue().getTileType();
            enemyName = entry.getValue().getEntity().getName();
            break;
          }
        }
//      int oldGridPos = Integer.parseInt(enemyOldKey.substring(1,5));

        // Get new key (place in UGS)
        Vector2 enemyPosVect = enemy.getPosition();
        GridPoint2 enemyPosGrid = ServiceLocator.getEntityService().getNamedEntity("terrain").getComponent(TerrainComponent.class).worldToTilePosition(enemyPosVect.x, enemyPosVect.y + 1);
        String enemyNewKey = UGS.generateCoordinate(enemyPosGrid.x, enemyPosGrid.y);
        // Delete old tile and give new tile in UGS
        if (!Objects.equals(enemyOldKey, enemyNewKey) && !enemyOldKey.equals("")) {
          Tile replacement = new Tile();
          replacement.setTileType(oldTileType);
          ServiceLocator.getUGSService().change(enemyOldKey, replacement);
          ServiceLocator.getUGSService().setEntity(enemyPosGrid, entity, enemyName);
          entity.setPosition(enemyPosVect);
        }
      }
    }
  }
}
