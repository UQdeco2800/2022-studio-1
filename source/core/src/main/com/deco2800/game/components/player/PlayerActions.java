package com.deco2800.game.components.player;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.deco2800.game.achievements.AchievementType;
import com.deco2800.game.areas.MainArea;
import com.deco2800.game.areas.terrain.TerrainComponent;
import com.deco2800.game.components.CombatStatsComponent;
import com.deco2800.game.components.Component;
import com.deco2800.game.components.shop.artefacts.Equipment;
import com.deco2800.game.components.shop.equipments.Equipments;
import com.deco2800.game.entities.Entity;
import com.deco2800.game.entities.Tile;
import com.deco2800.game.entities.UGS;
import com.deco2800.game.physics.components.PhysicsComponent;
import com.deco2800.game.services.AchievementHandler;
import com.deco2800.game.services.DayNightCycleService;
import com.deco2800.game.services.DayNightCycleStatus;
import com.deco2800.game.services.ServiceLocator;

import java.util.*;

/**
 * Action component for interacting with the player. Player events should be
 * initialised in create() and should call methods within this class when
 * triggered
 */
public class PlayerActions extends Component {
  private Vector2 MAX_SPEED = new Vector2(12.5f, 12.5f); // Metres per second
  private static final Vector2 DEFAULT_MAX_SPEED = new Vector2(12.5f, 12.5f); // Metres per second

  private PhysicsComponent physicsComponent;
  private Vector2 walkDirection = Vector2.Zero.cpy();
  private Vector2 faceDirecetion = Vector2.X.cpy();
  private boolean moving = false;
  private long elapsedTime = ServiceLocator.getTimeSource().getTime() / 1000;

  public static boolean playerAlive = true;
  int updateSpeedCount;

  private Timer timer;
  TimerTask dieTask;

  @Override
  public void create() {
    physicsComponent = entity.getComponent(PhysicsComponent.class);
    entity.getEvents().addListener("walk", this::walk);
    entity.getEvents().addListener("walkStop", this::stopWalking);
    entity.getEvents().addListener("attack", this::attack);
    entity.getEvents().addListener("playerDeath", this::die);
    timer = new Timer();
    ServiceLocator.getDayNightCycleService().getEvents().addListener(DayNightCycleService.EVENT_PART_OF_DAY_PASSED,
        this::respawn);
    playerAlive = true;
  }

  @Override
  public void update() {
    Entity camera = ServiceLocator.getEntityService().getNamedEntity("camera");
    Vector2 playerCenterPos = ServiceLocator.getEntityService().getNamedEntity("player").getPosition();
    camera.setPosition(playerCenterPos);
    if (moving) {
      updateSpeed();

      // run my elapsed ttime shit

      // Player position is based on bottom left corner of the texture, so we add to
      // the position of the camera to account for this.
//      Vector2 playerCenterPos = entity.getPosition();
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
    updateSpeedCount += 1;
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
    Vector2 player = ServiceLocator.getUGSService().getEntityByName("player").getPosition();
    GridPoint2 gridPos = ServiceLocator.getEntityService().getNamedEntity("terrain").getComponent(TerrainComponent.class).worldToTilePosition(player.x, player.y + 1);
    boolean didItWork = ServiceLocator.getUGSService().checkEntityPlacement(gridPos, "player");

    Entity closestEnemy = null;
    Entity closestEntity = null;

    ArrayList<Entity> radius = ServiceLocator.getRangeService().perimeter(current, gridPos);
    for (Entity i : radius) {
      if (i != null && i.getName() != null && i.getName().contains("Mr.")) {
        closestEnemy = i;
        break;
      }
    }
    for (Entity i : radius) {
      if (i != null && i.getName() != null && !i.getName().contains("Mr.") && !i.getName().equals("player")) {
        closestEntity = i;
        break;
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
          ServiceLocator.getAchievementHandler().getEvents().trigger(AchievementHandler.EVENT_ENEMY_KILLED,
              AchievementType.KILLS, 1);
        } else {
          enemyTarget.setHealth(enemyTarget.getHealth());
          System.out.println(enemyTarget.getHealth());
        }
      }
    } else if (closestEntity != null) {
      if (closestEntity.isCollectable()) {
        closestEntity.collectResources();
        ServiceLocator.getUGSService().dispose(closestEntity);
        closestEntity.dispose();
        PlayerStatsDisplay.updateItems();
      }
    }
    this.entity.getEvents().trigger("showPrompts");
  }

  /**
   * Kills the player
   */
  public void die() {
    entity.getEvents().trigger("death_anim");
    entity.setScale(11f, 10.5f);
    playerAlive = false;
    dieTask = new TimerTask() {
      @Override
      public void run() {
        // hide the character sprite
        entity.setScale(0.1F, 0.1F);
      }
    };
    timer.schedule(dieTask, 1000);
  }

  /**
   * Respawns the player
   */
  public void respawn(DayNightCycleStatus partOfDay) {
    if (partOfDay == DayNightCycleStatus.DAY) {
      if (ServiceLocator.getDayNightCycleService().getCurrentCycleStatus() == DayNightCycleStatus.DAY) {
        // respawn
        entity.getEvents().trigger("after_death");
        entity.setScale(10.5f, 9.5f);
        playerAlive = true;
        entity.getComponent(CombatStatsComponent.class).setHealth(100);
      }
    }
  }

//  public void updateEnemyPosInUgs () {
//    // Initialise
//    ArrayList<Entity> enemyList = null;
//    if (ServiceLocator.getEntityService() != null && ServiceLocator.getEntityService().getEnemyEntities() != null) {
//      enemyList = new ArrayList<>(ServiceLocator.getEntityService().getEnemyEntities());
//    }
//
//    for (Entity enemy : enemyList) {
//      // Get old key (place in UGS)
//      String enemyOldKey = "";
//      String oldTileType = "";
//      String enemyName = null;
//      for (Map.Entry<String, Tile> entry : ServiceLocator.getUGSService().printUGS().entrySet()) {
//        if (entry.getValue().getEntity() == enemy) {
//          enemyOldKey = entry.getKey();
//          oldTileType = entry.getValue().getTileType();
//          enemyName = entry.getValue().getEntity().getName();
//          break;
//        }
//      }
//      int oldGridPos = Integer.parseInt(enemyOldKey.substring(1,5));
//
//      // Get new key (place in UGS)
//      Vector2 enemyPosVect = enemy.getPosition();
//      GridPoint2 enemyPosGrid = ServiceLocator.getEntityService().getNamedEntity("terrain").getComponent(TerrainComponent.class).worldToTilePosition(enemyPosVect.x, enemyPosVect.y);
//      String enemyNewKey = UGS.generateCoordinate(enemyPosGrid.x, enemyPosGrid.y);
//      // Delete old tile and give new tile in UGS
//      if (!Objects.equals(enemyOldKey, enemyNewKey)) {
//        Tile replacement = new Tile();
//        replacement.setTileType(oldTileType);
//        ServiceLocator.getUGSService().change(enemyOldKey, replacement);
//        ServiceLocator.getUGSService().setEntity(enemyPosGrid, entity, enemyName);
//        entity.setPosition(enemyPosVect);
//      }
//    }
//  }

}
