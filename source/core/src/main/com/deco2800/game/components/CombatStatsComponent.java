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
 * Component used to store information related to combat such as health, attack, etc. Any entities
 * which engage it combat should have an instance of this class registered. This class can be
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
  private int maxHealth = 10000;

  public CombatStatsComponent(int health, int baseAttack) {
    setHealth(health);
    setBaseAttack(baseAttack);
    this.baseHealth = health;
    this.currentAttack = baseAttack;
  }

  public CombatStatsComponent(int health, int baseAttack, int defense) {
    setHealth(health);
    setBaseAttack(baseAttack);
    setBaseDefense(defense);
  }

//  /**
//   * Combat Stats Component with extra parameter level to enable levelling up of entities
//   */
//  public CombatStatsComponent(int health, int baseAttack, int defense, int level) {
//    setHealth(health);
//    this.baseHealth = health;
//    setBaseAttack(baseAttack);
//    setLevel(level);
//    this.currentAttack = baseAttack;
//  }
  /**
   * Combat Stats Component with two extra parameter level & maxHealth to enable increase of maxHealth with each level upgrade
   */
  public CombatStatsComponent(int health, int baseAttack, int level, int maxHealth) {
    setHealth(health);
    this.baseHealth = health;
    setBaseAttack(baseAttack);
    setLevel(level);
    this.currentAttack = baseAttack;
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

  public int getLevel(){ return level; }

  /**
   * Sets the entity's health. Health has a minimum bound of 0.
   *
   * @param health health
   */
  public void setHealth(int health) {
    if (health >= 0) {
      if ( health <= maxHealth) {
        this.health = health;
      }else {
        logger.error("health value cannot exceed max health");
      }
    } else {
      this.health = 0;
    }
    if (entity != null) {
      entity.getEvents().trigger("updateHealth", this.health);
    }
  }

  public void setLevel(int level) {
    this.level = level;

    if (entity != null) {
      entity.getEvents().trigger("updateLevel", this.level);
    }
  }

  public void setMaxHealth(int maxHealth) {
    this.maxHealth = maxHealth;

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

  /**
   * Returns the entity's base attack damage.
   *
   * @return base attack damage
   */
  public int getBaseAttack() {
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
      this.currentAttack = attack;
    } else {
      logger.error("Can not set base attack to a negative attack value");
    }
  }

  public void hit(CombatStatsComponent attacker) {
    int newHealth = getHealth() - attacker.getBaseAttack();
    setHealth(newHealth);
    Sound hurtSound = Gdx.audio.newSound(Gdx.files.internal("sounds/hurt.mp3"));
    hurtSound.play();
  }


  /**
   * Returns the base health of the entity
   * @return int
   */
  public void upgrade() {

    //crystal.dispose();
    if (this.level == 1) {
      CrystalFactory.triggerCrystal("images/crystal_level2.png");
    } else if (this.level == 2) {
      Entity crystal = ServiceLocator.getEntityService().getNamedEntity("crystal2");
      crystal.dispose();
      CrystalFactory.triggerCrystal("images/crystal_level3.png");
    }
    //System.out.println(ServiceLocator.getEntityService().getAllNamedEntities());
    //System.out.println(ServiceLocator.getEntityService().getNamedEntity("crystal"));
    if (this.level <= 5) {
      //addHealth((1000-this.health)+(50*this.level));
      System.out.println(this.health);
      setHealth(this.health += 50);
      setLevel(this.level + 1);
      //System.out.println(this.health);
    } else System.out.println("Crystal has reached max level");
  }




  public void setBaseDefense(int defense) {
    this.defense = defense;
  }
  public int getBaseDefense() {
    return defense;
  }
/**
 * Returns the base health of the entity
 * @return int 
 */

  public int getBaseHealth() {
    return this.baseHealth;
  }
  public int getMaxHealth() {
    return this.maxHealth;
  }

}
