package com.deco2800.game.components.player;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
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
import com.deco2800.game.services.GameTime;
import com.deco2800.game.services.ServiceLocator;

import java.util.*;

/**
 * Action component for interacting with the player. Player events should be
 * initialised in create() and should call methods within this class when
 * triggered
 */
public class PlayerActions extends Component {
  public static final String EVENT_SHOW_PROMPTS = "showPrompts";
  private Vector2 MAX_SPEED = new Vector2(12.5f, 12.5f); // Metres per second
  private static final Vector2 DEFAULT_MAX_SPEED = new Vector2(12.5f, 12.5f); // Metres per second

  private PhysicsComponent physicsComponent;
  private Vector2 walkDirection = Vector2.Zero.cpy();
  private Vector2 faceDirecetion = Vector2.X.cpy();
  private boolean moving = false;
  private long elapsedTime = ServiceLocator.getTimeSource().getTime() / 1000;

  // key in respect to index order : w, a, s, d
  private boolean[] movementKeyPressed = { false, false, false, false };
  private long lastMovementTime = 0;
  private int movementTimeDelta = 300;

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
    entity.getEvents().addListener("updatePlayerPosition", this::updatePlayerMovement);
    timer = new Timer();
    ServiceLocator.getDayNightCycleService().getEvents().addListener(DayNightCycleService.EVENT_PART_OF_DAY_PASSED,
        this::respawn);
    playerAlive = true;
  }

  @Override
  public void update() {
    Entity camera = ServiceLocator.getEntityService().getNamedEntity("camera");
    Vector2 playerCenterPos = ServiceLocator.getEntityService().getNamedEntity(CombatStatsComponent.PLAYER).getCenterPosition();

    moving = false;
    for (int i = 0; i < 4; i++) {
      moving = moving || movementKeyPressed[i];
    }

    if (moving) {
      long gameTime = ServiceLocator.getTimeSource().getTime();

      if (gameTime > lastMovementTime) {
        float posX = movementKeyPressed[3] ? 1 : 0;
        float negX = movementKeyPressed[1] ? 1 : 0;
        float posY = movementKeyPressed[0] ? 1 : 0;
        float negY = movementKeyPressed[2] ? 1 : 0;

        Vector2 movement = new Vector2(posX - negX, posY - negY);
        movePlayerInUgs(movement);

        lastMovementTime = gameTime + movementTimeDelta;
      }

      playerCenterPos.add(0.7f, 1f);
      camera.getEvents().trigger("playerMovementPan", playerCenterPos);
    } else {
      camera.getEvents().trigger("stopPlayerMovementPan");
    }
  }

  public void updatePlayerMovement(int key, boolean pressed) {
    movementKeyPressed[key] = pressed;
  }

  public void movePlayerInUgs(Vector2 direction) {
    Entity player = ServiceLocator.getEntityService().getNamedEntity(CombatStatsComponent.PLAYER);
    String keyOfPlayer = "";
    for (Map.Entry<String, Tile> entry : ServiceLocator.getUGSService().printUGS().entrySet()) {
      if (entry.getValue().getEntity() == player) {
        keyOfPlayer = entry.getKey();
      }
    }
    String keyCoorSplit[] = keyOfPlayer.split(",");
    GridPoint2 playerCurrentPos = new GridPoint2(Integer.parseInt(keyCoorSplit[0]), Integer.parseInt(keyCoorSplit[1]));

    ServiceLocator.getUGSService().moveEntity(player, playerCurrentPos, direction.x, direction.y);

    // uncomment in case of emergency
    // switch (direction.toString()) {
    // case "(1.0,0.0)":
    // // move right 1 square
    // ServiceLocator.getUGSService().moveEntity(player, playerCurrentPos, 1, 0);
    // return;
    // case "(-1.0,0.0)":
    // // move left 1 square
    // ServiceLocator.getUGSService().moveEntity(player, playerCurrentPos, -1, 0);
    // return;
    // case "(0.0,1.0)":
    // // move up 1 square
    // ServiceLocator.getUGSService().moveEntity(player, playerCurrentPos, 0, 1);
    // return;
    // case "(0.0,-1.0)":
    // // move down 1 square
    // ServiceLocator.getUGSService().moveEntity(player, playerCurrentPos, 0, -1);
    // }
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
    this.entity.getEvents().trigger(EVENT_SHOW_PROMPTS);
  }

  /**
   * Stops the player from walking.
   */
  void stopWalking() {
    this.walkDirection = Vector2.Zero.cpy();
    updateSpeed();
    moving = false;
    this.entity.getEvents().trigger(EVENT_SHOW_PROMPTS);
  }

  /**
   * Makes the player attack.
   */
  void attack() {
    Entity current = MainArea.getInstance().getGameArea().getPlayer();
    Vector2 player;
    if (ServiceLocator.getUGSService().getEntityByName(CombatStatsComponent.PLAYER) != null) {
      player = ServiceLocator.getUGSService().getEntityByName(CombatStatsComponent.PLAYER).getPosition();
    } else {
      return;
    }

    GridPoint2 gridPos = ServiceLocator.getEntityService().getNamedEntity("terrain")
        .getComponent(TerrainComponent.class).worldToTilePosition(player.x, player.y + 1);



    Entity closestEnemy = null;
    Entity closestEntity = null;

    ArrayList<Entity> radius = ServiceLocator.getRangeService().perimeter(current, gridPos);
    for (Entity i : radius) {
      if (i != null && i.getName() != null && i.getName().contains("Mr.")) {
        closestEnemy = i;
        break;
      }
    }
    boolean mine = false;
    for (Entity i : radius) {
      if (i != null && i.getName() != null && !i.getName().contains("Mr.") && !i.getName().equals(CombatStatsComponent.PLAYER)
          && i.isCollectable()) {
        if (current.getComponent(InventoryComponent.class).getWeapon() == Equipments.AXE) {
          mine = false;
          closestEntity = i;
          break;
        } else if (!current.getComponent(InventoryComponent.class).getWeapon().equals(Equipments.AXE)) {
          mine = true;
        }
      }
    }

    if (closestEnemy != null) {
      mine = false;
      CombatStatsComponent enemyTarget = closestEnemy.getComponent(CombatStatsComponent.class);
      if (null != enemyTarget && ServiceLocator.getRangeService().playerInRangeOf(closestEnemy)) {
        CombatStatsComponent combatStats = ServiceLocator.getEntityService().getNamedEntity(CombatStatsComponent.PLAYER)
            .getComponent(CombatStatsComponent.class);
        enemyTarget.hit(combatStats);
        if (enemyTarget.getHealth() < 1) {
          closestEnemy.collectResources();
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
      }
    }
    PlayerStatsDisplay.updateItems();
    this.entity.getEvents().trigger(EVENT_SHOW_PROMPTS);
    if (mine) {
      this.entity.getEvents().trigger("noMine");
    }
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
    if (partOfDay == DayNightCycleStatus.DAY && !playerAlive) {
      if (ServiceLocator.getDayNightCycleService().getCurrentCycleStatus() == DayNightCycleStatus.DAY) {
        // respawn
        entity.getEvents().trigger("after_death");
        entity.setScale(10.5f, 9.5f);
        playerAlive = true;
        entity.getComponent(CombatStatsComponent.class).setHealth(100);
      }
    }
  }
}
