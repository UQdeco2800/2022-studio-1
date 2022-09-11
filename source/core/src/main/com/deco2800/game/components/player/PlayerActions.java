package com.deco2800.game.components.player;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.deco2800.game.areas.MainArea;
import com.deco2800.game.components.CombatStatsComponent;
import com.deco2800.game.components.Component;
import com.deco2800.game.entities.Enemy;
import com.deco2800.game.entities.Entity;
import com.deco2800.game.physics.components.PhysicsComponent;
import com.deco2800.game.services.ServiceLocator;
import com.deco2800.game.rendering.TextureRenderComponent;

import java.security.Provider;
import java.util.ArrayList;
import java.util.HashSet;

/**
 * Action component for interacting with the player. Player events should be initialised in create()
 * and when triggered should call methods within this class.
 */
public class PlayerActions extends Component {
  private Vector2 MAX_SPEED = new Vector2(3f, 3f); // Metres per second
  private static final Vector2 DEFAULT_MAX_SPEED = new Vector2(3f, 3f); //Metres per second

  private PhysicsComponent physicsComponent;
  private Vector2 walkDirection = Vector2.Zero.cpy();
  private Vector2 faceDirecetion = Vector2.X.cpy();
  private boolean moving = false;

  @Override
  public void create() {
    physicsComponent = entity.getComponent(PhysicsComponent.class);
    entity.getEvents().addListener("walk", this::walk);
    entity.getEvents().addListener("walkStop", this::stopWalking);
    entity.getEvents().addListener("attack", this::attack);
  }

  @Override
  public void update() {
    if (moving) {
      updateSpeed();
    }
  }

  public Vector2 getPlayerSpeed() {
    return MAX_SPEED;
  }

  public void resetPlayerSpeed() {
    MAX_SPEED = DEFAULT_MAX_SPEED;
  }

  private void updateSpeed() {
    Body body = physicsComponent.getBody();
    Vector2 velocity = body.getLinearVelocity();
    Vector2 desiredVelocity = walkDirection.cpy().scl(MAX_SPEED);
    // impulse = (desiredVel - currentVel) * mass
    Vector2 impulse = desiredVelocity.sub(velocity).scl(body.getMass());
    body.applyLinearImpulse(impulse, body.getWorldCenter(), true);
  }

  /**
   * Moves the player towards a given direction.
   *
   * @param direction direction to move in
   */
  void walk(Vector2 direction) {
    this.walkDirection = direction;
    this.faceDirecetion = direction;
    moving = true;
    Sound walkSound = ServiceLocator.getResourceService().getAsset("sounds/footsteps_grass_single.mp3", Sound.class);
    walkSound.play();
  }

  /**
   * Stops the player from walking.
   */
  void stopWalking() {
    this.walkDirection = Vector2.Zero.cpy();
    updateSpeed();
    moving = false;
  }

  /**
   * Makes the player attack.
   */
  void attack() {
    System.out.println("Attacking...");
    Entity current = MainArea.getInstance().getGameArea().getPlayer();
    Entity closestEnemy =
            ServiceLocator.getEntityService().findClosestEnemy((int) current.getPosition().x,
                    (int) current.getPosition().y);
    Entity closestEntity =  ServiceLocator.getEntityService().findClosetEntity((int) current.getPosition().x,
            (int) current.getPosition().y);

    if (null != closestEnemy) {
      System.out.println("I am an enemy kill me.");
      CombatStatsComponent enemyTarget = closestEnemy.getComponent(CombatStatsComponent.class);
      if (null != enemyTarget) {
        CombatStatsComponent combatStats =
                ServiceLocator.getEntityService().getNamedEntity("player").getComponent(CombatStatsComponent.class);
        System.out.println(enemyTarget.getHealth());
        enemyTarget.hit(combatStats);
        if (enemyTarget.getHealth() < 1) {
          closestEnemy.dispose();
        } else {
          enemyTarget.setHealth(enemyTarget.getHealth());
          System.out.println(enemyTarget.getHealth());
        }
      }
    } else if (null != closestEntity) {
      if (null == closestEntity.getName()) {
        return;
      }
      if (closestEntity.isCollectable()) {
        closestEntity.collectResources();
        closestEntity.dispose();
      }
    }
  }
}

//    Sound attackSound = ServiceLocator.getResourceService().getAsset("sounds/sword_swing.mp3", Sound.class);
//    attackSound.play();
