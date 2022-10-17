package com.deco2800.game.components;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.deco2800.game.components.infrastructure.ResourceType;
import com.deco2800.game.entities.Entity;
import com.deco2800.game.entities.factories.CrystalFactory;
import com.deco2800.game.rendering.TextureRenderComponent;
import com.deco2800.game.services.AchievementHandler;
import com.deco2800.game.services.ServiceLocator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Component used to store information related to combat such as health, attack,
 * etc. Any entities
 * which engage it combat should have an instance of this class registered. This
 * class can be
 * extended for more specific combat needs.
 */
public class CombatStatsComponent extends Component {
  public static final String CRYSTAL = "crystal";
  public static final String PLAYER = "player";

  private static final Logger logger = LoggerFactory.getLogger(CombatStatsComponent.class);
  private int health;
  private int baseHealth;
  private int baseAttack;
  private int level;
  private int defense;
  private int currentAttack;
  private int attackMultiplier;
  private int maxHealth = 10000;
  private boolean invincible = false;

  public CombatStatsComponent(int health, int baseAttack) {
    setHealth(health);
    setBaseAttack(baseAttack);
    setAttackMultiplier(1);
    this.baseHealth = health;
    this.currentAttack = baseAttack;
  }

  public CombatStatsComponent(int health, int baseAttack, int defense) {
    setHealth(health);
    setBaseAttack(baseAttack);
    setBaseDefense(defense);
    setAttackMultiplier(1);
  }

  /**
   * Combat Stats Component with extra parameter level to enable levelling up of
   * entities
   */
  public CombatStatsComponent(int health, int baseAttack, int defense, int level) {
    setHealth(health);
    this.baseHealth = health;
    setBaseAttack(baseAttack);
    setLevel(level);
    setBaseDefense(defense);
    this.currentAttack = baseAttack;
  }

  /**
   * Combat Stats Component with maxHealth parameter to enable increase of
   * maxHealth with each level upgrade independent
   * to current health (For crystal)
   * Implements baseAttack, defense which are no use to crystal as constructor with 3 parameters is already present
   *
   */
  public CombatStatsComponent(int health, int baseAttack, int defense, int level, int maxHealth) {
    setHealth(health);
    setBaseAttack(baseAttack);
    setLevel(level);
    setBaseDefense(defense);
    setMaxHealth(maxHealth);
  }

  /**
   * Returns true if the entity's has 0 health, otherwise false.
   *
   * @return is player dead
   */
  public Boolean isDead() {
    return health == 0;
  }

  /**
   * Returns the entity's health.
   *
   * @return entity's health
   */
  public int getHealth() {
    return health;
  }

  public int getLevel() {
    return level;
  }

  /**
   * Sets the entity's health. Health must be greater than 0.
   * If the health value to be set exceeds the entities maximum health, it is capped at the maxHealth value.
   * If the health value is 0 or less, the entity is killed.
   *
   * @param health health
   */
  public void setHealth(int health) {
    if (health > 0) {
      if (health > maxHealth) {
        this.health = maxHealth;
      } else {
        if (entity != null && Objects.equals(entity.getName(), CRYSTAL) && this.health > health) {
          ServiceLocator.getAchievementHandler().getEvents().trigger(AchievementHandler.EVENT_CRYSTAL_DAMAGED, 11);
        }

        this.health = health;
      }
    } else {
      this.health = 0;
      if (entity != null && entity.getName() != null) {

        // create an enemy list to contain all enemies
        String[] enemies = {"Zero", "Crab", "Electricity", "Starfish"};
        // remove enemies if health point is 0
        for (String enemy : enemies) {
          if (entity != null && entity.getName().contains(enemy) && isDead()) {
            if (entity.getName().contains("Zero")) {
              ServiceLocator.getAchievementHandler().getEvents().trigger(AchievementHandler.EVENT_BOSS_KILL, 10L);
            }
            ServiceLocator.getEntityService().addToDestroyEntities(entity);
          }
        }

        if (entity != null && Objects.equals(entity.getName(), CRYSTAL)) {
          killEntity(CRYSTAL);
        }
        if (entity != null && Objects.equals(entity.getName(), PLAYER)) {
          killEntity(PLAYER);
        }
      }
    }

    if (entity != null) {
      entity.getEvents().trigger("updateHealth", this.health);
    }
  }

  /**
   * Triggers listener events when certain entities should be killed. Each entity can be handled separately.
   *
   * @param entityName the name of the entity to kill
   */
  public void killEntity(String entityName) {
    //String entityName = entity.getName()
    switch (entityName) {
      case PLAYER:
        entity.getEvents().trigger("playerDeath");
        break;
      case CRYSTAL:
        Timer time = new Timer();
        TimerTask destroyedAnimation = new TimerTask() {
            @Override
            public void run() {
              ServiceLocator.getEntityService().getNamedEntity(CRYSTAL).getEvents().trigger("desCrystal");
            }
        };
        TimerTask lastAnimation = new TimerTask() {
            @Override
            public void run() {
              ServiceLocator.getEntityService().getNamedEntity(CRYSTAL).getEvents().trigger("lastCrystal");
            }
        };
        TimerTask destroyed = new TimerTask() {
            @Override
            public void run() {
              ServiceLocator.getEntityService().getNamedEntity(CRYSTAL).getEvents().trigger("crystalDeath");
            }
        };
        time.scheduleAtFixedRate(destroyedAnimation, 0, 1000);
        time.scheduleAtFixedRate(lastAnimation, 1000, 1000);
        time.scheduleAtFixedRate(destroyed, 1000, 1000);
        break;
      default:
        //do nothing
    }
  }

  public void setLevel(int level) {
    if (level >= 1) {
      this.level = level;
      // create an enemy list to contain all enemies
      String[] enemies = {"Crab", "Electricity", "Starfish"};
      // remove enemies if health point is 0
      for (String enemy : enemies) {
        if (entity != null && entity.getName().contains(enemy)) {
          switch (enemy) {
            case "Carb": {
              if (level == 1) {
                setMaxHealth(50);
                setHealth(50);
                setBaseAttack(10);
                setBaseDefense(5);
                entity.setResourceType(ResourceType.GOLD);
                entity.setResourceAmount(10);
              } else if (level == 2) {
                setMaxHealth(60);
                setHealth(60);
                setBaseAttack(15);
                setBaseDefense(15);
                entity.setResourceType(ResourceType.GOLD);
                entity.setResourceAmount(20);
              } else if (level == 3) {
                setMaxHealth(75);
                setHealth(75);
                setBaseAttack(20);
                setBaseDefense(30);
                entity.setResourceType(ResourceType.GOLD);
                entity.setResourceAmount(30);
              }
            }
            case "Electricity": {
              if (level == 1) {
                setMaxHealth(30);
                setHealth(30);
                setBaseAttack(20);
                setBaseDefense(0);
                entity.setResourceType(ResourceType.GOLD);
                entity.setResourceAmount(20);
              } else if (level == 2) {
                setMaxHealth(35);
                setHealth(35);
                setBaseAttack(30);
                setBaseDefense(5);
                entity.setResourceType(ResourceType.GOLD);
                entity.setResourceAmount(25);
              } else if (level == 3) {
                setMaxHealth(45);
                setHealth(45);
                setBaseAttack(40);
                setBaseDefense(15);
                entity.setResourceType(ResourceType.GOLD);
                entity.setResourceAmount(40);
              }
            }
            case "Starfish": {
              if (level == 1) {
                setMaxHealth(30);
                setHealth(30);
                setBaseAttack(20);
                setBaseDefense(0);
                entity.setResourceType(ResourceType.GOLD);
                entity.setResourceAmount(20);
              } else if (level == 2) {
                setMaxHealth(35);
                setHealth(35);
                setBaseAttack(30);
                setBaseDefense(5);
                entity.setResourceType(ResourceType.GOLD);
                entity.setResourceAmount(25);
              } else if (level == 3) {
                setMaxHealth(40);
                setHealth(40);
                setBaseAttack(40);
                setBaseDefense(15);
                entity.setResourceType(ResourceType.GOLD);
                entity.setResourceAmount(35);
              }
            }
          }
        }
      }
    }

    if (entity != null) {
      entity.getEvents().trigger("updateLevel", this.level);
    } else if (level < 1) {
      logger.error("level cannot be 0 or minus");
    }
  }

  /**
   * Sets the entity's maximum health. Maximum health has a minimum bound of 0.
   *
   * @param maxHealth maxHealth
   */
  public void setMaxHealth(int maxHealth) {
    if (maxHealth > 0) {
      this.maxHealth = maxHealth;
    }

    if (entity != null) {
      entity.getEvents().trigger("updateMaxHealth", this.maxHealth);
    }
  }

  /**
   * Adds to the player's health. The amount added can be negative.
   *
   * @param health health to add
   */
  public void addHealth(int health) {
    if (this.health + health <= maxHealth) {
      setHealth(this.health + health);
    }
  }

  public void setAttackMultiplier(int multiplier) {
    this.attackMultiplier = multiplier;
    revertAttack();
    addAttack(baseAttack * (multiplier - 1));
  }

  public int getAttackMultiplier() {
    return attackMultiplier;
  }
  /**
   * Returns the entity's base attack damage.
   *
   * @return base attack damage
   */
  public int getBaseAttack() {
    return baseAttack;
  }

  public int getCurrentAttack() {
    return currentAttack;
  }
  public void addAttack(int attackPower) {
    currentAttack += attackPower;
  }

  public void revertAttack() {
    currentAttack = baseAttack;
  }

  /**
   * Sets the entity's attack damage. Attack damage has a minimum bound of 0.
   *
   * @param attack Attack damage
   */
  public void setBaseAttack(int attack) {
    if (attack >= 0) {
      this.baseAttack = attack;
    } else {
      logger.error("Can not set base attack to a negative attack value");
    }
  }

  public void hit(CombatStatsComponent attacker) {
    if (!invincible) {
      int newHealth = getHealth() - attacker.getBaseAttack() / (defense != 0 ? defense : 1);
      setHealth(newHealth);
      Sound hurtSound = Gdx.audio.newSound(Gdx.files.internal("sounds/hurt.mp3"));
      hurtSound.play();
    }
  }

  public void setInvincibility (Boolean state) {
    this.invincible = state;
  }

  public void setBaseDefense(int defense) {
    this.defense = defense;
  }

  public int getBaseDefense() {
    return defense;
  }

  /**
   * Returns the base health of the entity
   * 
   * @return int
   */
  public int getBaseHealth() {
    return this.baseHealth;
  }

  public int getMaxHealth() {
    return this.maxHealth;
  }
}
