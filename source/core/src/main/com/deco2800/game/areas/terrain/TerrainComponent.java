package com.deco2800.game.areas.terrain;

import java.util.ArrayList;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapRenderer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.renderers.BatchTiledMapRenderer;
import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.Vector2;
import com.deco2800.game.entities.Entity;
import com.deco2800.game.rendering.DayNightCycleComponent;
import com.deco2800.game.rendering.RenderComponent;
import com.deco2800.game.screens.MainGameScreen;
import com.deco2800.game.services.ServiceLocator;

/**
 * Render a tiled terrain for a given tiled map and orientation. A terrain is a
 * map of tiles that
 * shows the 'ground' in the game. Enabling/disabling this component will
 * show/hide the terrain.
 */
public class TerrainComponent extends RenderComponent {
  private static final int TERRAIN_LAYER = 0;
  private int currentMapLvl = 0;
  private ArrayList<ArrayList<GridPoint2>> bordersList;
  private ArrayList<ArrayList<GridPoint2>> landTilesList;

  private final TiledMap tiledMap;
  private final TiledMapRenderer tiledMapRenderer;
  private final OrthographicCamera camera;
  private final TerrainOrientation orientation;
  private final float tileSize;
  private GridPoint2 island_size;

  private SpriteBatch batchedMapTileSpriteBatch;

  private DayNightCycleComponent dayNightCycleComponent;

  public TerrainComponent(
      OrthographicCamera camera,
      TiledMap map,
      TiledMapRenderer renderer,
      TerrainOrientation orientation,
      float tileSize,
      GridPoint2 island_size,
      ArrayList<ArrayList<GridPoint2>> bordersPositionList,
      ArrayList<ArrayList<GridPoint2>> landTilesList) {
    this.camera = camera;
    this.tiledMap = map;
    this.orientation = orientation;
    this.tileSize = tileSize;
    this.tiledMapRenderer = renderer;
    this.island_size = island_size;
    if (renderer != null) {
      try {
        this.batchedMapTileSpriteBatch = (SpriteBatch) ((BatchTiledMapRenderer) renderer).getBatch();
      } catch (ClassCastException e) {
        // issue caused when being mocked
        this.batchedMapTileSpriteBatch = null;
      }
    }
    // Assuming render service is created first. otherwise day/night shader will not
    // be applied
    if (ServiceLocator.getRenderService() != null) {
      this.dayNightCycleComponent = ServiceLocator.getRenderService().getDayNightCycleComponent();
    }

    this.bordersList = bordersPositionList;
    this.landTilesList = landTilesList;

  }

  public void spawnIslandBorders(int levelNum) {
    // Dispose of current old borders
    Entity oldWall = null;
    while ((oldWall = ServiceLocator.getEntityService().getNamedEntity("wall")) != null) {
      oldWall.dispose();
    }

    // Create new borders [Note: Make sure walls are registered as a named entity so
    // we can dispose of them above]
    ArrayList<GridPoint2> borders = bordersList.get(levelNum); // list of tile positions for where the border should be
                                                               // placed

  }

  public Vector2 tileToWorldPosition(GridPoint2 tilePos) {
    return tileToWorldPosition(tilePos.x, tilePos.y);
  }

  public GridPoint2 worldToTilePosition(float x, float y) {
    x = (((x/0.5f)-(y/0.25f))/2);
    y = (((y/0.25f)+(x/0.5f))/2);
    return new GridPoint2((int) x, (int) y);
  }

  public Vector2 tileToWorldPosition(int x, int y) {
    switch (orientation) {
      case HEXAGONAL:
        float hexLength = tileSize / 2;
        float yOffset = (x % 2 == 0) ? 0.5f * tileSize : 0f;
        return new Vector2(x * (tileSize + hexLength) / 2, y + yOffset);
      case ISOMETRIC:
        return new Vector2((x + y) * tileSize / 2, (y - x) * tileSize / 4);
      case ORTHOGONAL:
        return new Vector2(x * tileSize, y * tileSize);
      default:
        return null;
    }
  }

  public int getCurrentMapLvl() {
    return currentMapLvl;
  }

  /**
   * Expands the map by hiding the current layer, and making the next level
   * visible
   */
  public void incrementMapLvl() {
    getMap().getLayers().get(currentMapLvl).setVisible(false);
    this.currentMapLvl++;
    getMap().getLayers().get(currentMapLvl).setVisible(true);
  }

  /**
   * Shrinks the map by hiding the current layer, and making the previous level
   * visible.
   */
  public void decrementMapLvl() {
    getMap().getLayers().get(currentMapLvl).setVisible(false);
    this.currentMapLvl--;
    getMap().getLayers().get(currentMapLvl).setVisible(true);
  }

  public float getTileSize() {
    return tileSize;
  }

  public GridPoint2 getMapBounds(int layer) {
    TiledMapTileLayer terrainLayer = (TiledMapTileLayer) tiledMap.getLayers().get(layer);
    return new GridPoint2(terrainLayer.getWidth(), terrainLayer.getHeight());
  }

  public TiledMap getMap() {
    return tiledMap;
  }

  public ArrayList<GridPoint2> getLandTiles() {
    return landTilesList.get(currentMapLvl);
  }

  public void setIslandSize(int x, int y) {
    this.island_size.x = x;
    this.island_size.y = y;
  }

  public GridPoint2 getIslandSize() {
    return this.island_size;
  }

  @Override
  public void draw(SpriteBatch batch) {
    tiledMapRenderer.setView(camera);
    // render night affect (using tiledmapRenderer batch)
    if (dayNightCycleComponent != null && batchedMapTileSpriteBatch != null) {
      dayNightCycleComponent.render(batchedMapTileSpriteBatch);
    }
    tiledMapRenderer.render();
  }

  @Override
  public void dispose() {
    tiledMap.dispose();
    super.dispose();
  }

  public TiledMapTileLayer getTileMapTileLayer(int layer) {
    TiledMapTileLayer terrainLayer = (TiledMapTileLayer) tiledMap.getLayers().get(layer);
    return terrainLayer;
  }

  @Override
  public float getZIndex() {
    return 0f;
  }

  @Override
  public int getLayer() {
    return getCurrentMapLvl();
  }

  public enum TerrainOrientation {
    ORTHOGONAL,
    ISOMETRIC,
    HEXAGONAL
  }
}
