package com.deco2800.game.entities.configs;

import com.deco2800.game.components.shop.equipments.Equipments;

import java.util.List;

/**
 * Defines the properties stored in player config files to be loaded by the Player Factory.
 */
public class PlayerConfig extends BaseEntityConfig  {
  public int gold = 1;
  public int stone = 1;
  public int wood = 1;
  public int baseDefense = 1;
  public String favouriteColour = "none";
}
