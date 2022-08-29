package com.deco2800.game.components.player;

import com.deco2800.game.components.Component;
import com.deco2800.game.components.shop.artefacts.Artefact;
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
  private List<Artefact> items = new ArrayList<>();

  public InventoryComponent(int gold) { // need to update constructor to take multiple ints
    setGold(gold);
    setStone(gold);
  }

  /**
   * Returns the player's gold.
   *
   * @return entity's health
   */
  public int getGold() {
    return this.gold;
  }

  /**
   * Returns the player's stone.
   *
   * @return entity's health
   */

  public int getStone() {
    return this.stone;
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
   * Adds to the player's gold. The amount added can be negative.
   * 
   * @param gold gold to add
   */
  public void addGold(int gold) {
    setGold(this.gold + gold);
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

  /**
   * Adds to the player's stone. The amount added can be negative.
   * 
   * @param stone stone to add
   */
  public void addStone(int stone) {
    setStone(this.stone + stone);
  }
}
