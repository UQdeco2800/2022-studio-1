package com.deco2800.game.components.player;

import com.deco2800.game.extensions.GameExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;

@ExtendWith(GameExtension.class)
class InventoryComponentTest {
  @Test
  void shouldSetGetGold() {
    InventoryComponent inventory = new InventoryComponent(100, 100, 100);
    assertEquals(100, inventory.getGold());

    inventory.setGold(150);
    assertEquals(150, inventory.getGold());

    inventory.setGold(-50);
    assertEquals(0, inventory.getGold());
  }

  @Test
  void shouldCheckHasGold() {
    InventoryComponent inventory = new InventoryComponent(150, 150, 150);
    assertTrue(inventory.hasGold(100));
    assertFalse(inventory.hasGold(200));
  }

  @Test
  void shouldAddGold() {
    InventoryComponent inventory = new InventoryComponent(100, 150, 150);
    inventory.addGold(-500);
    assertEquals(0, inventory.getGold());

    inventory.addGold(100);
    inventory.addGold(-20);
    assertEquals(80, inventory.getGold());
  }

  @Test
  void shouldSetGetStone() {
    InventoryComponent inventory = new InventoryComponent(100, 100, 100);
    assertEquals(100, inventory.getStone());

    inventory.setStone(150);
    assertEquals(150, inventory.getStone());

    inventory.setStone(-50);
    assertEquals(0, inventory.getStone());
  }

  @Test
  void shouldCheckHasStone() {
    InventoryComponent inventory = new InventoryComponent(150, 150, 150);
    assertTrue(inventory.hasStone(100));
    assertFalse(inventory.hasStone(200));
  }

  @Test
  void shouldAddStone() {
    InventoryComponent inventory = new InventoryComponent(100, 150, 150);
    inventory.addStone(-500);
    assertEquals(0, inventory.getStone());

    inventory.addStone(100);
    inventory.addStone(-20);
    assertEquals(80, inventory.getStone());
  }
}
