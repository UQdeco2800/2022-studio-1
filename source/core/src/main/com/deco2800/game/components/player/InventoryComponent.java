package com.deco2800.game.components.player;

import com.deco2800.game.components.Component;
import com.deco2800.game.components.shop.artefacts.Artefact;
import com.deco2800.game.components.shop.equipments.Equipments;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * A component intended to be used by the player to track their inventory.
 *
 * Currently only stores the gold and stone amount but can be extended for more
 * advanced functionality such as storing items.
 * Can also be used as a more generic component for other entities.
 */
public class InventoryComponent extends Component {
  private static final Logger logger = LoggerFactory.getLogger(InventoryComponent.class);
  private int gold;
  private int stone;
  private int wood;
  private Equipments weapon;
  private Equipments helmet;
  private Equipments chestplate;

  private List<Artefact> items = new ArrayList<>();

  public InventoryComponent(int gold, int stone, int wood,
                            Equipments weapon, Equipments chestplate, Equipments helmet) {
    setGold(gold);
    setStone(stone);
    setWood(wood);
    setWeapon(weapon);
    setHelmet(helmet);
    setChestplate(chestplate);
  }

  public InventoryComponent(int gold, int stone, int wood) {
    setGold(gold);
    setStone(stone);
    setWood(wood);
  }
  public void setWeapon(Equipments weapon) {
    this.weapon = weapon;
  }

  public void setHelmet(Equipments helmet) {
    this.helmet = helmet;
  }

  public void setChestplate(Equipments chestplate) {
    this.chestplate = chestplate;
  }

  public Equipments getWeapon() {
    return this.weapon;
  }

  public Equipments getHelmet() {
    return this.helmet;
  }

  public Equipments getChestplate() {
    return this.chestplate;
  }

  /**
   * Returns the player's gold.
   *
   * @return entity's gold
   */
  public int getGold() {
    return this.gold;
  }

  /**
   * Returns the player's stone.
   *
   * @return entity's stone
   */

  public int getStone() {
    return this.stone;
  }

  /**
   * Returns the player's wood.
   *
   * @return entity's wood
   */

  public int getWood() {
    return this.wood;
  }

  /**
   * Returns if the player has a certain amount of gold.
   * 
   * @param gold required amount of gold
   * @return player has greater than or equal to the required amount of gold
   */
  public Boolean hasGold(int gold) {
    return this.gold >= gold;
  }

  /**
   * Returns if the player has a certain amount of stone.
   * 
   * @param stone required amount of stone
   * @return player has greater than or equal to the required amount of stone
   */
  public Boolean hasStone(int stone) {
    return this.stone >= stone;
  }

  /**
   * Returns if the player has a certain amount of wood.
   * 
   * @param wood required amount of stone
   * @return player has greater than or equal to the required amount of wood
   */
  public Boolean hasWood(int wood) {
    return this.wood >= wood;
  }

  /**
   * Sets the player's gold. Gold has a minimum bound of 0.
   *
   * @param gold gold
   */
  public void setGold(int gold) {
    if (gold >= 0) {
      this.gold = gold;
    } else {
      this.gold = 0;
    }
    logger.debug("Setting gold to {}", this.gold);
  }

  /**
   * Sets the player's stone. Stone has a minimum bound of 0.
   *
   * @param stone currency stone
   */
  public void setStone(int stone) {
    if (stone >= 0) {
      this.stone = stone;
    } else {
      this.stone = 0;
    }
    logger.debug("Setting stone to {}", this.stone);
  }

  /**
   * Sets the player's wood. Stone has a minimum bound of 0.
   *
   * @param wood currency wood
   */
  public void setWood(int wood) {
    if (wood >= 0) {
      this.wood = wood;
    } else {
      this.wood = 0;
    }
    logger.debug("Setting wood to {}", this.wood);
  }

  /**
   * Adds to the player's gold. The amount added can be negative.
   * 
   * @param gold gold to add
   */
  public void addGold(int gold) {
    setGold(this.gold + gold);
  }

  /**
   * Adds to the player's stone. The amount added can be negative.
   *
   * @param stone stone to add
   */
  public void addStone(int stone) {
    setStone(this.stone + stone);
  }

  /**
   * Adds to the player's wood. The amount added can be negative.
   *
   * @param wood stone to add
   */
  public void addWood(int wood) {
    setWood(this.wood + wood);
  }

  public void setItems(List<Artefact> items) {
    this.items = items;
  }

  public void addItems(Artefact item) {
    this.items.add(item);
  }

  public List<Artefact> getItems() {
    return this.items;
  }
}
