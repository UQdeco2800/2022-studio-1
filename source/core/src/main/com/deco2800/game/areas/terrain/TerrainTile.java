package com.deco2800.game.areas.terrain;

import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.tiled.tiles.AnimatedTiledMapTile;
import com.badlogic.gdx.maps.tiled.tiles.StaticTiledMapTile;
import com.badlogic.gdx.utils.Array;

/**
 * Custom terrain tile implementation for tiled map terrain that stores
 * additional properties we
 * may want to have in the game, such as audio, walking speed, traversability by
 * AI, etc.
 */
public class TerrainTile extends AnimatedTiledMapTile {
  private int id;
  private BlendMode blendMode = BlendMode.ALPHA;
  private float offsetX;
  private float offsetY;

  private final String name;

  public TerrainTile(float interval, Array<StaticTiledMapTile> frameTiles, String name) {
    super(interval, frameTiles);
    this.name = name;
  }

  @Override
  public int getId() {
    return id;
  }

  @Override
  public void setId(int id) {
    this.id = id;
  }

  public String getName() {
    return this.name;
  }

  @Override
  public BlendMode getBlendMode() {
    return blendMode;
  }

  @Override
  public void setBlendMode(BlendMode blendMode) {
    this.blendMode = blendMode;
  }

  /**
   * Not required for game, unimplemented
   * 
   * @return null
   */
  @Override
  public MapProperties getProperties() {
    return null;
  }

  /**
   * Not required for game, unimplemented
   * 
   * @return null
   */
  @Override
  public MapObjects getObjects() {
    return null;
  }
}
