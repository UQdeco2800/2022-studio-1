package com.deco2800.game.components;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.deco2800.game.entities.Entity;
import com.deco2800.game.entities.factories.CrystalFactory;
import com.deco2800.game.rendering.TextureRenderComponent;
import com.deco2800.game.services.ServiceLocator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Component used to store information related to combat such as health, attack,
 * etc. Any entities
 * which engage it combat should have an instance of this class registered. This
 * class can be
 * extended for more specific combat needs.
 */
public class CombatStatsComponent extends Component {

  private static final Logger logger = LoggerFactory.getLogger(CombatStatsComponent.class);
  private int health;
  private int baseHealth;
  private int baseAttack;
  private int level;
  private int defense;
  private int currentAttack;
  private int attackMultiplier;
  private int maxHealth = 10000;

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
   * to current health
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
   * Sets the entity's health. Health has a minimum bound of 0.
   *
   * @param health health
   */
  public void setHealth(int health) {
    if (health >= 0) {
      if (health <= maxHealth) {
        this.health = health;
      }
      // }else {
      // logger.info("max health is reached");
      // }
    } else {
      this.health = 0;
    }

    if (entity != null) {
      entity.getEvents().trigger("updateHealth", this.health);
    }
  }

  public void setLevel(int level) {
    if (level >= 1) {
      this.level = level;
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
      this.currentAttack = attack;
    } else {
      logger.error("Can not set base attack to a negative attack value");
    }
  }

  public void hit(CombatStatsComponent attacker) {
    int newHealth = getHealth() - attacker.getBaseAttack() / (defense != 0 ? defense : 1);
    setHealth(newHealth);
    Sound hurtSound = Gdx.audio.newSound(Gdx.files.internal("sounds/hurt.mp3"));
    hurtSound.play();
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
