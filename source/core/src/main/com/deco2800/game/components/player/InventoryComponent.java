package com.deco2800.game.components.player;

import com.deco2800.game.components.Component;
import com.deco2800.game.components.infrastructure.ResourceType;
import com.deco2800.game.components.shop.artefacts.Artefact;
import com.deco2800.game.components.shop.artefacts.ShopBuilding;
import com.deco2800.game.components.shop.equipments.Equipments;
import com.deco2800.game.entities.configs.EquipmentConfig;
import com.deco2800.game.memento.CareTaker;
import com.deco2800.game.services.AchievementHandler;
import com.deco2800.game.services.ServiceLocator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.deco2800.game.components.infrastructure.ResourceType.*;

/**
 * A component intended to be used by the player to track their inventory.
 *
 * Currently only stores the gold and stone amount but can be extended for more
 * advanced functionality such as storing items.
 * Can also be used as a more generic component for other entities.
 */
public class InventoryComponent extends Component {
  private static final Logger logger = LoggerFactory.getLogger(InventoryComponent.class);
  private Equipments weapon;
  private Equipments armor;

  private HashMap<ResourceType, Integer> inventory = new HashMap<>();
  private List<Equipments> equipmentList = new ArrayList<>();

  private HashMap<Artefact, Integer> items = new HashMap<>();
  private HashMap<ShopBuilding, Integer> buildings = new HashMap<>();

  private AchievementHandler achievementHandler;

  public InventoryComponent(int gold, int stone, int wood) {
    inventory.put(GOLD, gold);
    inventory.put(STONE, stone);
    inventory.put(WOOD, wood);
    equipmentList.add(Equipments.AXE);
    setWeapon(Equipments.AXE);

    achievementHandler = ServiceLocator.getAchievementHandler();
  }

  public void setWeapon(Equipments weapon) {
    this.weapon = weapon;
  }

  public void setArmor(Equipments armor) {
    this.armor = armor;
  }

  public Equipments getWeapon() {
    return this.weapon;
  }

  public Equipments getArmor() {
    return this.armor;
  }

  /**
   * Returns the player's gold.
   *
   * @return entity's gold
   */
  public int getGold() {
    return inventory.get(GOLD);
  }

  /**
   * Returns the player's stone.
   *
   * @return entity's stone
   */

  public int getStone() {
    return inventory.get(STONE);
  }

  /**
   * Returns the player's wood.
   *
   * @return entity's wood
   */

  public int getWood() {
    return inventory.get(WOOD);
  }

  /**
   * Returns if the player has a certain amount of gold.
   * 
   * @param gold required amount of gold
   * @return player has greater than or equal to the required amount of gold
   */
  public Boolean hasGold(int gold) {
    return inventory.get(GOLD) >= gold;
  }

  /**
   * Returns if the player has a certain amount of stone.
   * 
   * @param stone required amount of stone
   * @return player has greater than or equal to the required amount of stone
   */
  public Boolean hasStone(int stone) {
    return inventory.get(STONE) >= stone;
  }

  /**
   * Returns if the player has a certain amount of wood.
   * 
   * @param wood required amount of stone
   * @return player has greater than or equal to the required amount of wood
   */
  public Boolean hasWood(int wood) {
    return inventory.get(WOOD) >= wood;
  }

  /**
   * Sets the player's gold. Gold has a minimum bound of 0.
   *
   * @param gold gold
   */
  public void setGold(int gold) {
    inventory.replace(GOLD, Math.max(gold, 0));
    logger.debug("Setting gold to {}", inventory.get(GOLD));
  }

  /**
   * Sets the player's stone. Stone has a minimum bound of 0.
   *
   * @param stone currency stone
   */
  public void setStone(int stone) {
    inventory.replace(STONE, Math.max(stone, 0));
    logger.debug("Setting stone to {}", inventory.get(STONE));
  }

  /**
   * Sets the player's wood. Stone has a minimum bound of 0.
   *
   * @param wood currency wood
   */
  public void setWood(int wood) {
    inventory.replace(WOOD, Math.max(wood, 0));
    logger.debug("Setting wood to {}", inventory.get(WOOD));
  }

  /**
   * Adds to the player's gold. The amount added can be negative.
   * 
   * @param gold gold to add
   */
  public void addGold(int gold) {
    setGold(inventory.get(GOLD) + gold);
  }

  /**
   * Adds to the player's stone. The amount added can be negative.
   *
   * @param stone stone to add
   */
  public void addStone(int stone) {
    setStone(inventory.get(STONE) + stone);
  }

  /**
   * Adds to the player's wood. The amount added can be negative.
   *
   * @param wood stone to add
   */
  public void addWood(int wood) {
    setWood(inventory.get(WOOD) + wood);
  }

  public void addResources(ResourceType resourceType, int amount) {
    inventory.replace(resourceType, inventory.get(resourceType) + amount);
    this.triggerResourceAddedEvent(resourceType, amount);
  }

  public void setEquipmentList(List<Equipments> equipmentsList) {
    this.equipmentList = equipmentsList;
  }

  public List<Equipments> getEquipmentList() {
    return equipmentList;
  }

  public void addEquipmentToList(Equipments equipment) {
    equipmentList.add(equipment);
  }

  public int countInEquipmentList(Equipments equipment) {
    if (equipmentList.contains(equipment)) {
      return 1;
    } else {
      return 0;
    }
  }

  public void setItems(HashMap<Artefact, Integer> items) {
    this.items = items;
  }

  public void setBuildings(HashMap<ShopBuilding, Integer> buildings) {
    this.buildings = buildings;
  }

  public void addBuilding(ShopBuilding building) {
    if (buildings.get(building) == null) {
      buildings.put(building, 1);
    } else {
      buildings.replace(building, buildings.get(building) + 1);
    }
  }

  public int getBuildingCount(ShopBuilding building) {
    if (buildings.get(building) == null) {
      return 0;
    } else {
      return buildings.get(building);
    }
  }

  public HashMap<ShopBuilding, Integer> getBuildings() {
    return buildings;
  }

  public void addItems(Artefact item) {
    if (items.get(item) == null) {
      items.put(item, 1);
    } else {
      items.replace(item, items.get(item) + 1);
    }
  }

  public Boolean useItem (Artefact item) {
    if (items.getOrDefault(item, 0) >= 1) {
      items.replace(item, items.get(item) - 1);
      return true;
    }
    return false;
  }

  public Boolean placeBuilding(ShopBuilding building) {
    if (buildings.getOrDefault(building, 0) >= 1) {
      buildings.replace(building, buildings.get(building) - 1);
      if (buildings.get(building) == 0) {
        buildings.remove(building);
      }
      return true;
    }
    return false;
  }

  public HashMap<Artefact, Integer> getItems() {
    return this.items;
  }

  public int getItemCount(Artefact item) {
    if (items.get(item) == null) {
      return 0;
    } else {
      return items.get(item);
    }
  }

  /**
   * Triggers an event that a resource has been added for achievements handler
   * 
   * @param resourceType the type of resource (WOOD, STONE, GOLD)
   * @param amount       the amount of the resource added
   */
  private void triggerResourceAddedEvent(ResourceType resourceType, int amount) {
    this.achievementHandler.getEvents().trigger(AchievementHandler.EVENT_RESOURCE_ADDED, resourceType, amount);
  }
}
