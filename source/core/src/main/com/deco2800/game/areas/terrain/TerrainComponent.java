package com.deco2800.game.areas.terrain;

import java.security.Provider.Service;
import java.util.ArrayList;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.Map;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapRenderer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.renderers.BatchTiledMapRenderer;
import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.deco2800.game.components.CombatStatsComponent;
import com.deco2800.game.entities.Entity;
import com.deco2800.game.entities.EntityService;
import com.deco2800.game.entities.UGS;
import com.deco2800.game.physics.PhysicsLayer;
import com.deco2800.game.physics.components.ColliderComponent;
import com.deco2800.game.physics.components.PhysicsComponent;
import com.deco2800.game.rendering.DayNightCycleComponent;
import com.deco2800.game.rendering.RenderComponent;
import com.deco2800.game.rendering.TextureRenderComponent;
import com.deco2800.game.services.DayNightCycleService;
import com.deco2800.game.services.DayNightCycleStatus;
import com.deco2800.game.services.RangeService;
import com.deco2800.game.services.ServiceLocator;

import javax.management.ValueExp;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Render a tiled terrain for a given tiled map and orientation. A terrain is a
 * map of tiles that
 * shows the 'ground' in the game. Enabling/disabling this component will
 * show/hide the terrain.
 */
public class TerrainComponent extends RenderComponent {

  private static final Logger logger = LoggerFactory.getLogger(TerrainComponent.class);

  private int currentMapLvl = 0;
  private int isNight = 0;
  private ArrayList<ArrayList<GridPoint2>> landTilesList;
  private ArrayList<ArrayList<GridPoint2>> walls;

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
      ArrayList<ArrayList<GridPoint2>> landTilesList, ArrayList<ArrayList<GridPoint2>> walls) {
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

    ServiceLocator.getDayNightCycleService().getEvents().addListener(DayNightCycleService.EVENT_PART_OF_DAY_PASSED,
        this::partOfDayPassed);

    this.landTilesList = landTilesList;
    this.walls = walls;

  }

  public Vector2 tileToWorldPosition(GridPoint2 tilePos) {
    return tileToWorldPosition(tilePos.x, tilePos.y);
  }

  /**
   * float x must be world position x - using camera.unproject
   * float y must be world position y - using camera.unproject
   */
  public GridPoint2 worldToTilePosition(float x, float y) {
    Vector2 screenPosition = new Vector2(x, y);
    Vector3 tilePosition = ((IsoTileRenderer) tiledMapRenderer).translateScreenToIso(screenPosition);
    GridPoint2 tilePos = new GridPoint2((int) (tilePosition.x + (tileSize / 2)),
        (int) (tilePosition.y - (tileSize / 2)));
    tilePos = new GridPoint2((int) (tilePos.x / tileSize), (int) (tilePos.y / tileSize));
    return tilePos;
  }

  public Vector2 tileToWorldPosition(int x, int y) {
    switch (orientation) {
      case HEXAGONAL:
        float hexLength = tileSize / 2;
        float yOffset = (x % 2 == 0) ? 0.5f * tileSize : 0f;
        return new Vector2(x * (tileSize + hexLength) / 2, y + yOffset);
      case ISOMETRIC:
        return new Vector2(((x + y) * tileSize / 2) + (tileSize / 4), ((y - x) * tileSize / 4) + (tileSize / 8));
      case ORTHOGONAL:
        return new Vector2(x * tileSize, y * tileSize);
      default:
        return null;
    }
  }

  public int getCurrentMapLvl() {
    return currentMapLvl;
  }

  public ArrayList<ArrayList<GridPoint2>> getWalls() {
    return walls;
  }

  /**
   * Damages all buildings that are on water
   */
  protected void damageSunkenBuildings() {

    String[] buildingNames = { "wall", "tower", "trap", "stoneQuarry", "woodCutter" };

    UGS ugs = ServiceLocator.getUGSService();
    GridPoint2 mapBounds = getMapBounds(currentMapLvl * 2 + isNight);
    for (int x = 0; x < mapBounds.x; x++) {
      for (int y = 0; y < mapBounds.y; y++) {
        Entity entity = ugs.getEntity(new GridPoint2(x, y));
        if (entity != null && ugs.getTileType(new GridPoint2(x, y)).equals("water")) {
          String name = entity.getName();

          for (String s : buildingNames) {
            if (name.contains(s)) {
              System.out.println("{Entity Details} => [Name: " + name + "]" + " [ID: " + entity.getId() + "]");
              ugs.removeEntity(name);
            }
          }
        }
      }
    }

  }

  /**
   * Updates the UGS in response to map state changing: updates each coordinate in
   * the UGS to the new tile type.
   */
  protected void updateUGS() {
    TiledMapTileLayer currentLayer = getTileMapTileLayer(currentMapLvl * 2 + isNight);
    UGS ugs = ServiceLocator.getUGSService();
    for (int x = 0; x < currentLayer.getWidth(); x++) {
      for (int y = 0; y < currentLayer.getHeight(); y++) {
        String name = ((TerrainTile) currentLayer.getCell(x, y).getTile()).getName();
        ugs.setTileType(new GridPoint2(x, y), name);
      }
    }
  }

  /**
   * Expands the map by hiding the current layer, and making the next level
   * visible
   */
  public void incrementMapLvl() {

    int newLevelNum = (currentMapLvl + 1) * 2 + isNight;
    if (newLevelNum > getMap().getLayers().size() - 1) {
      logger.error("TerrainComponent[incrementMapLvl] => incremented level number is outside the bounds of layers");
      return;
    }

    getMap().getLayers().get(currentMapLvl * 2 + isNight).setVisible(false);
    this.currentMapLvl++;
    getMap().getLayers().get(newLevelNum).setVisible(true);
    updateUGS();
  }

  /**
   * Shrinks the map by hiding the current layer, and making the previous level
   * visible.
   */
  public void decrementMapLvl() {
    int newLevelNum = (currentMapLvl - 1) * 2 + isNight;
    if (newLevelNum < 0) {
      logger.error("TerrainComponent[decrementMapLvl] => incremented level number is outside the bounds of layers");
      return;
    }
    getMap().getLayers().get(currentMapLvl * 2 + isNight).setVisible(false);
    this.currentMapLvl--;
    getMap().getLayers().get(newLevelNum).setVisible(true);

    // Update coordinate-tile type mapping in UGS
    updateUGS();

    // Damage any buildings over-taken by water
    damageSunkenBuildings();

  }

  /**
   * Changes the current map level to the night version when night comes around
   * 
   * @param partOfDay day night cycle time period
   */
  public void partOfDayPassed(DayNightCycleStatus partOfDay) {
    System.out.println(partOfDay.name());
    if (partOfDay == DayNightCycleStatus.DAY) {
      getMap().getLayers().get(currentMapLvl * 2 + 1).setVisible(false);
      getMap().getLayers().get(currentMapLvl * 2).setVisible(true);
      isNight = 0;
    } else {
      getMap().getLayers().get(currentMapLvl * 2).setVisible(false);
      getMap().getLayers().get(currentMapLvl * 2 + 1).setVisible(true);
      isNight = 1;
    }
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
