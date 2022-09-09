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

import java.util.ArrayList;

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
    Entity closestToMe =
            MainArea.getInstance().getGameArea().getEntityMapping().findClosetEntity((int) current.getPosition().x,
                    (int) current.getPosition().y);

    ArrayList<String> keys = new ArrayList<>();
    for (String i : ServiceLocator.getEntityService().getAllNamedEntities().keySet()) {
      keys.add(i);
    }

    if (closestToMe instanceof Enemy) {
      // Work out how to damage health
//      ServiceLocator.getEntityService().getNamedEntity(closestToMe).getComponent().
      System.out.println("I am an enemy kill me.");
//      ServiceLocator.getEntityService().getNamedEntity("Tree").getComponent(CombatStatsComponent.class).=;
    } else {
      int id = closestToMe.getId();
      for (String i : keys) {
        if (i.contains("" + id)) {
          String sub = i.substring(0, i.indexOf("@"));
          switch (sub) {
            case "Tree":
              ServiceLocator.getEntityService().getNamedEntity(i).dispose();
              // ADD TO WOOD COUNTER
              break;
            case "Rock":
              ServiceLocator.getEntityService().getNamedEntity(i).dispose();
              // ADD TO STONE COUNTER
              break;
            case "Vine":
              ServiceLocator.getEntityService().getNamedEntity(i).dispose();
              // ADD TO WOOD COUNTER
              break;
            case "Geyser":
              ServiceLocator.getEntityService().getNamedEntity(i).dispose();
              // ADD TO STONE COUNTER
              break;
            case "Fence":
              ServiceLocator.getEntityService().getNamedEntity(i).dispose();
              // ADD TO WOOD COUNTER
              break;
            case "Pillar":
              ServiceLocator.getEntityService().getNamedEntity(i).dispose();
              // ADD TO STONE COUNTER
              break;
            case "SpikeyBush":
              ServiceLocator.getEntityService().getNamedEntity(i).dispose();
              // ADD TO WOOD COUNTER
              break;
            case "Billboard":
              ServiceLocator.getEntityService().getNamedEntity(i).dispose();
              // ADD TO WOOD COUNTER
              break;
          }

        }
      }
    }



  }

}
