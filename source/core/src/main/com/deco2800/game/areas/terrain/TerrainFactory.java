package com.deco2800.game.areas.terrain;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapRenderer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer.Cell;
import com.badlogic.gdx.maps.tiled.renderers.HexagonalTiledMapRenderer;
import com.badlogic.gdx.maps.tiled.renderers.IsometricTiledMapRenderer;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.GridPoint2;
import com.deco2800.game.areas.terrain.TerrainComponent.TerrainOrientation;
import com.deco2800.game.components.CameraComponent;
import com.deco2800.game.utils.math.RandomUtils;
import com.deco2800.game.services.ResourceService;
import com.deco2800.game.services.ServiceLocator;

/** Factory for creating game terrains. */
public class TerrainFactory {
  private final GridPoint2 MAP_SIZE = new GridPoint2(120, 120);
  private GridPoint2 island_size = new GridPoint2(20, 20);
  private static final int SAND_SIZE = 2;

  private TerrainTile grassTile;
  private TerrainTile waterTile;
  private TerrainTile sandTile;

  private TiledMap tiledMap;

  private final OrthographicCamera camera;
  private final TerrainOrientation orientation;

  /**
   * Create a terrain factory with Orthogonal orientation
   *
   * @param cameraComponent Camera to render terrains to. Must be ortographic.
   */
  public TerrainFactory(CameraComponent cameraComponent) {
    this(cameraComponent, TerrainOrientation.ISOMETRIC);
  }

  /**
   * Create a terrain factory`
   *
   * @param cameraComponent Camera to render terrains to. Must be orthographic.
   * @param orientation     orientation to render terrain at
   */
  public TerrainFactory(CameraComponent cameraComponent, TerrainOrientation orientation) {
    this.camera = (OrthographicCamera) cameraComponent.getCamera();
    this.camera.zoom += 0.2;
    this.orientation = orientation;
  }

  /**
   * Create a terrain of the given type, using the orientation of the factory.
   * This can be extended
   * to add additional game terrains.
   *
   * @param terrainType Terrain to create
   * @return Terrain component which renders the terrain
   */
  public TerrainComponent createTerrain(TerrainType terrainType) {
    ResourceService resourceService = ServiceLocator.getResourceService();
    switch (terrainType) {
      case FOREST_DEMO_ISO:
        TextureRegion isoGrass = new TextureRegion(
            resourceService.getAsset("images/grassTest.png", Texture.class));

        TextureRegion isoWater = new TextureRegion(
            resourceService.getAsset("images/waterTest.png", Texture.class));
        TextureRegion isoSand = new TextureRegion(resourceService.getAsset("images/sandTest.png", Texture.class));

        return createForestDemoTerrain(1f, isoGrass, isoWater, isoSand);
      default:
        return null;
    }
  }

  /**
   * Initializes renderer and calls createForestDemoTiles to create
   * tiledMap.
   * 
   * @param tileWorldSize size of tiles in terms of world units
   * @param grass         Grass texture
   * @param water         Water texture
   * @param sand          Sand texture
   * @return new TerrainComponent
   */
  private TerrainComponent createForestDemoTerrain(
      float tileWorldSize, TextureRegion grass, TextureRegion water,
      TextureRegion sand) {
    GridPoint2 tilePixelSize = new GridPoint2(water.getRegionWidth(), water.getRegionHeight());
    TiledMap tiledMap = createForestDemoTiles(tilePixelSize, grass, water, sand);
    TiledMapRenderer renderer = createRenderer(tiledMap, tileWorldSize / tilePixelSize.x);
    return new TerrainComponent(camera, tiledMap, renderer, orientation, tileWorldSize, island_size);
  }

  private TiledMapRenderer createRenderer(TiledMap tiledMap, float tileScale) {
    switch (orientation) {
      case ORTHOGONAL:
        return new OrthogonalTiledMapRenderer(tiledMap, tileScale);
      case ISOMETRIC:
        return new IsometricTiledMapRenderer(tiledMap, tileScale);
      case HEXAGONAL:
        return new HexagonalTiledMapRenderer(tiledMap, tileScale);
      default:
        return null;
    }
  }

  /**
   * Creates new TiledMap and calls fillTiles to generate the base layer for it
   * 
   * @param tileSize size of tiles
   * @param grass    grass texture
   * @param water    water texture
   * @param sand     sand texture
   * @return new TiledMap with the generated map contained
   */
  private TiledMap createForestDemoTiles(
      GridPoint2 tileSize, TextureRegion grass, TextureRegion water, TextureRegion sand) {
    tiledMap = new TiledMap();
    grassTile = new TerrainTile(grass, "grass");
    waterTile = new TerrainTile(water, "water");
    sandTile = new TerrainTile(sand, "cliff");
    TiledMapTileLayer layer = new TiledMapTileLayer(MAP_SIZE.x, MAP_SIZE.y, tileSize.x, tileSize.y);

    // Create base grass
    fillTiles(layer, island_size, MAP_SIZE, waterTile, grassTile, sandTile);

    tiledMap.getLayers().add(layer);
    return tiledMap;
  }

  private static void fillTilesAtRandom(TiledMapTileLayer layer, GridPoint2 mapSize, TerrainTile tile, int amount) {
    GridPoint2 min = new GridPoint2(0, 0);
    GridPoint2 max = new GridPoint2(mapSize.x - 1, mapSize.y - 1);

    for (int i = 0; i < amount; i++) {
      GridPoint2 tilePos = RandomUtils.random(min, max);
      Cell cell = layer.getCell(tilePos.x, tilePos.y);
      cell.setTile(tile);
    }
  }

  /**
   * Calls fill functions to generate the map
   * 
   * @param layer          map layer to add to
   * @param islandSize     size of the island in tiles
   * @param mapSize        size of the map in tiles
   * @param water          water tile
   * @param land           land tile
   * @param cliffTile      cliff tile
   * @param cliffRightTile cliff edge right tile
   * @param cliffLeftTile  cliff edge left tile
   */
  private static void fillTiles(TiledMapTileLayer layer, GridPoint2 islandSize, GridPoint2 mapSize, TerrainTile water,
      TerrainTile land, TerrainTile sand) {

    int waterWidth = (int) Math.floor((mapSize.x - islandSize.x) / 2);
    int waterHeight = (int) Math.floor((mapSize.y - islandSize.y) / 2);

    GridPoint2 waterDimensions = new GridPoint2(waterWidth, waterHeight);

    fillSand(layer, islandSize, mapSize, waterDimensions, sand);

    fillLand(layer, islandSize, mapSize, waterDimensions, land);

    fillWater(layer, islandSize, mapSize, waterDimensions, water);
  }

  /**
   * Fills grass layer of island based on dimensions of island
   * 
   * @param layer           map layer to add to
   * @param islandSize      island size in tiles
   * @param mapSize         map size in tiles
   * @param waterDimensions tile distances from edge of map to island
   * @param land            land tile
   */
  public static void fillLand(TiledMapTileLayer layer, GridPoint2 islandSize, GridPoint2 mapSize,
      GridPoint2 waterDimensions, TerrainTile land) {
    for (int x = waterDimensions.x; x < islandSize.x + waterDimensions.x; x++) {
      for (int y = waterDimensions.y; y < islandSize.y + waterDimensions.y; y++) {
        Cell cell = new Cell();
        cell.setTile(land);
        layer.setCell(x, y, cell);
      }
    }
  }

  /**
   * Generates cliff tiles and cliff edge tiles based on island size
   * and cliff height.
   * 
   * @param layer           map layer to add to
   * @param islandSize      island size in tiles
   * @param mapSize         map size in tiles
   * @param waterDimensions tile distances from edge of map to island
   * @param sandTile        sandTile
   */
  public static void fillSand(TiledMapTileLayer layer, GridPoint2 islandSize, GridPoint2 mapSize,
      GridPoint2 waterDimensions,
      TerrainTile sandTile) {

    // Add Sand along x axis: bottom left and top right of island

    for (int x = waterDimensions.x - SAND_SIZE; x < waterDimensions.x + islandSize.x + SAND_SIZE; x++) {
      for (int i = 0; i < SAND_SIZE; i++) {
        Cell bLeft = new Cell();
        bLeft.setTile(sandTile);
        layer.setCell(x, waterDimensions.y - (1 + i), bLeft);

        Cell tRight = new Cell();
        tRight.setTile(sandTile);
        layer.setCell(x, waterDimensions.y + islandSize.y + i, tRight);
      }
    }

    // Add Sand along y axis: botoom right and top left of island

    for (int y = waterDimensions.y; y < waterDimensions.y + islandSize.y; y++) {
      for (int i = 0; i < SAND_SIZE; i++) {
        Cell tLeft = new Cell();
        tLeft.setTile(sandTile);
        layer.setCell(waterDimensions.x - (1 + i), y, tLeft);

        Cell bRight = new Cell();
        bRight.setTile(sandTile);
        layer.setCell(waterDimensions.x + islandSize.y + i, y, bRight);
      }
    }

  }

  /**
   * Fills remaining empty cells with water
   * 
   * @param layer           map layer to add to
   * @param islandSize      island size in tiles
   * @param mapSize         map size in tiles
   * @param waterDimensions tile distances from edge of map to island
   * @param water           water tile
   */
  public static void fillWater(TiledMapTileLayer layer, GridPoint2 islandSize, GridPoint2 mapSize,
      GridPoint2 waterDimensions, TerrainTile water) {
    for (int x = 0; x < mapSize.x; x++) {
      for (int y = 0; y < mapSize.y; y++) {
        if (layer.getCell(x, y) == null) {
          Cell cell = new Cell();
          cell.setTile(water);
          layer.setCell(x, y, cell);
        }
      }
    }
  }

  /**
   * Adds a new ring of playable land around the island and generates
   * cliffs
   *
   * @param layer  TiledMapTileLayer containing the map
   * @param amount number of rings to add
   */
  public void expandIsland(TiledMapTileLayer layer, int amount) {
    island_size.x += amount;
    island_size.y += amount;

    int waterWidth = (int) Math.floor((MAP_SIZE.x - island_size.x) / 2);
    int waterHeight = (int) Math.floor((MAP_SIZE.y - island_size.y) / 2);

    GridPoint2 waterDimensions = new GridPoint2(waterWidth, waterHeight);

    fillLand(layer, island_size, MAP_SIZE, waterDimensions, grassTile);
    fillSand(layer, island_size, MAP_SIZE, waterDimensions, sandTile);
  }

  /**
   * Remove rings of land around the island, ensuring that the new
   * island size is greater than or equal to the initial island size
   *
   * @param layer  TiledMapTileLayer containing the map
   * @param amount number of rings to remove
   */
  public void scaleDownIsland(TiledMapTileLayer layer, int amount) {
    if (island_size.x - amount < 3 || island_size.y < 3) {
      return;
    }

    int waterWidth = (int) Math.floor((MAP_SIZE.x - island_size.x) / 2);
    int waterHeight = (int) Math.floor((MAP_SIZE.y - island_size.y) / 2);

    GridPoint2 waterDimensions = new GridPoint2(waterWidth, waterHeight);

    expandIsland(layer, 0 - amount);
    fillWater(layer, island_size, MAP_SIZE, waterDimensions, waterTile);
  }

  public GridPoint2 getIslandSize() {
    return this.island_size;
  }

  public GridPoint2 getMapSize() {
    return MAP_SIZE;
  }

  /**
   * This enum should contain the different terrains in your game, e.g. forest,
   * cave, home, all with
   * the same oerientation. But for demonstration purposes, the base code has the
   * same level in 3
   * different orientations.
   */
  public enum TerrainType {
    FOREST_DEMO,
    FOREST_DEMO_ISO,
    FOREST_DEMO_HEX
  }
}
