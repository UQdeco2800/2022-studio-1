package com.deco2800.game.components.player;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.deco2800.game.achievements.AchievementType;
import com.deco2800.game.areas.MainArea;
import com.deco2800.game.components.CombatStatsComponent;
import com.deco2800.game.components.Component;
import com.deco2800.game.entities.Entity;
import com.deco2800.game.physics.components.PhysicsComponent;
import com.deco2800.game.services.AchievementHandler;
import com.deco2800.game.services.ServiceLocator;
import java.util.ArrayList;

/**
 * Action component for interacting with the player. Player events should be
 * initialised in create() and should call methods within this class when triggered
 */
public class PlayerActions extends Component {
  private Vector2 MAX_SPEED = new Vector2(12.5f, 12.5f); // Metres per second
  private static final Vector2 DEFAULT_MAX_SPEED = new Vector2(12.5f, 12.5f); // Metres per second

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

    entity.getEvents().addListener("playerDeath", this::die);
  }

  @Override
  public void update() {
    Entity camera = ServiceLocator.getEntityService().getNamedEntity("camera");
    if (moving) {
      updateSpeed();

      // Player position is based on bottom left corner of the texture, so we add to
      // the position of the camera to account for this.

      Vector2 playerCenterPos = entity.getPosition();
      playerCenterPos.add(0.7f, 1f);

      camera.getEvents().trigger("playerMovementPan", playerCenterPos);
    } else {
      camera.getEvents().trigger("stopPlayerMovementPan");
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
    // walkSound.play();
    this.entity.getEvents().trigger("showPrompts");
  }

  /**
   * Stops the player from walking.
   */
  void stopWalking() {
    this.walkDirection = Vector2.Zero.cpy();
    updateSpeed();
    moving = false;
    this.entity.getEvents().trigger("showPrompts");
  }

  /**
   * Makes the player attack.
   */
  void attack() {
    Entity current = MainArea.getInstance().getGameArea().getPlayer();
    Entity closestEnemy = null;
    Entity closestEntity = null;

    ArrayList<Entity> radius = ServiceLocator.getRangeService().perimeter(current);
    String underMe = ServiceLocator.getRangeService().getPlayerTile();
    for (Entity i : radius) {
      if (i != null && i.getName().contains("Mr.")) {
        closestEnemy = i;
        break;
      }
    }
    for (Entity i : radius) {
      if (i != null && !i.getName().contains("Mr.")) {
        closestEntity = i;
        break;
      }
    }
    if (ServiceLocator.getUGSService().getEntity(underMe) != null) {
      if (ServiceLocator.getUGSService().getEntity(underMe).getName().contains("Mr.")) {
        closestEnemy = ServiceLocator.getUGSService().getEntity(underMe);
      } else if (!ServiceLocator.getUGSService().getEntity(underMe).getName().contains("Mr.")) {
        closestEntity = ServiceLocator.getUGSService().getEntity(underMe);
      }
    }

    if (closestEnemy != null) {
      CombatStatsComponent enemyTarget = closestEnemy.getComponent(CombatStatsComponent.class);
      if (null != enemyTarget && ServiceLocator.getRangeService().playerInRangeOf(closestEnemy)) {
        CombatStatsComponent combatStats = ServiceLocator.getEntityService().getNamedEntity("player")
                .getComponent(CombatStatsComponent.class);
        enemyTarget.hit(combatStats);
        if (enemyTarget.getHealth() < 1) {
          closestEnemy.dispose();
          this.entity.getEvents().trigger("enemyKill");
          ServiceLocator.getAchievementHandler().getEvents().trigger(AchievementHandler.EVENT_ENEMY_KILLED, AchievementType.KILLS, 1);
        } else {
          enemyTarget.setHealth(enemyTarget.getHealth());
          System.out.println(enemyTarget.getHealth());
        }
      }
    } else if (closestEntity != null) {
      if (closestEntity.isCollectable()) {
        closestEntity.collectResources();
        closestEntity.dispose();
        PlayerStatsDisplay.updateItems();
      }
    }
    this.entity.getEvents().trigger("showPrompts");

  }

  /**
   * Kill the entity
   */
  public void die() {
    entity.getEvents().trigger("death_anim");
//    entity.dispose();

  }

}
// deprecated attack function
//    closestEnemy = ServiceLocator.getEntityService().findClosestEnemy((int) current.getPosition().x,
//        (int) current.getPosition().y);
//    Entity closestEntity = ServiceLocator.getEntityService().findClosetEntity((int) current.getPosition().x,
//        (int) current.getPosition().y);
//
//    if (null != closestEnemy) {
//      CombatStatsComponent enemyTarget = closestEnemy.getComponent(CombatStatsComponent.class);
//      if (null != enemyTarget && ServiceLocator.getRangeService().playerInRangeOf(closestEnemy)) {
//        CombatStatsComponent combatStats = ServiceLocator.getEntityService().getNamedEntity("player")
//            .getComponent(CombatStatsComponent.class);
//        System.out.println(enemyTarget.getHealth());
//        enemyTarget.hit(combatStats);
//        if (enemyTarget.getHealth() < 1) {
//          closestEnemy.dispose();
//          this.entity.getEvents().trigger("enemyKill");
//          ServiceLocator.getAchievementHandler().getEvents().trigger(AchievementHandler.EVENT_ENEMY_KILLED, AchievementType.KILLS, 1);
//        } else {
//          enemyTarget.setHealth(enemyTarget.getHealth());
//          System.out.println(enemyTarget.getHealth());
//        }
//      }
//    } else if (null != closestEntity) {
//      if (null == closestEntity.getName()) {
//        return;
//      }
//      if (closestEntity.isCollectable() && ServiceLocator.getRangeService().playerInRangeOf(closestEntity)) {
//        closestEntity.collectResources();
//        closestEntity.dispose();
//        PlayerStatsDisplay.updateItems();
//      }
//    }
//    this.entity.getEvents().trigger("showPrompts");
//  }

// Sound attackSound =
// ServiceLocator.getResourceService().getAsset("sounds/sword_swing.mp3",
// Sound.class);
// attackSound.play();
