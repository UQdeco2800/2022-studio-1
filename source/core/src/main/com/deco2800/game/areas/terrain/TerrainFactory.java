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
  private static final GridPoint2 MAP_SIZE = new GridPoint2(120, 120);
  private static GridPoint2 island_size = new GridPoint2(5, 5);
  private static final int CLIFF_HEIGHT = 1;

  private TerrainTile grassTile;
  private TerrainTile waterTile;
  private TerrainTile cliffTile;
  private TerrainTile cliffRightTile;
  private TerrainTile cliffLeftTile;

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
            resourceService.getAsset("images/trial3GrassTile.png", Texture.class));

        TextureRegion isoWater = new TextureRegion(
            resourceService.getAsset("images/waterFinalVersion.png", Texture.class));
        TextureRegion isoCliff = new TextureRegion(resourceService.getAsset("images/fullSizedDirt.png", Texture.class));
        TextureRegion isoCliffLeft = new TextureRegion(
            resourceService.getAsset("images/waterDirtMerged.png", Texture.class));
        TextureRegion isoCliffRight = new TextureRegion(
            resourceService.getAsset("images/waterDirtMerged.png", Texture.class));
        isoCliffRight.flip(true, false);

        return createForestDemoTerrain(3f, isoGrass, isoWater, isoCliff, isoCliffLeft,
            isoCliffRight);
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
   * @param cliff         Cliff texture
   * @param cliffLeft     Cliff edge left texture
   * @param cliffRight    Cliff edge right texture
   * @return new TerrainComponent
   */
  private TerrainComponent createForestDemoTerrain(
      float tileWorldSize, TextureRegion grass, TextureRegion water,
      TextureRegion cliff, TextureRegion cliffLeft, TextureRegion cliffRight) {
    GridPoint2 tilePixelSize = new GridPoint2(grass.getRegionWidth(), grass.getRegionHeight());
    TiledMap tiledMap = createForestDemoTiles(tilePixelSize, grass, water, cliff, cliffLeft,
        cliffRight);
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
   * @param tileSize   size of tiles
   * @param grass      grass texture
   * @param water      water texture
   * @param cliff      cliff texture
   * @param cliffLeft  cliff edge left texture
   * @param cliffRight cliff edge right texture
   * @return new TiledMap with the generated map contained
   */
  private TiledMap createForestDemoTiles(
      GridPoint2 tileSize, TextureRegion grass, TextureRegion water, TextureRegion cliff, TextureRegion cliffLeft,
      TextureRegion cliffRight) {
    tiledMap = new TiledMap();
    grassTile = new TerrainTile(grass, "grass");
    waterTile = new TerrainTile(water, "water");
    cliffTile = new TerrainTile(cliff, "cliff");
    cliffRightTile = new TerrainTile(cliffRight, "cliffRight");
    cliffLeftTile = new TerrainTile(cliffLeft, "cliffLeft");
    TiledMapTileLayer layer = new TiledMapTileLayer(MAP_SIZE.x, MAP_SIZE.y, tileSize.x, tileSize.y);

    // Create base grass
    fillTiles(layer, island_size, MAP_SIZE, waterTile, grassTile, cliffTile, cliffRightTile, cliffLeftTile);

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
      TerrainTile land, TerrainTile cliffTile, TerrainTile cliffRightTile, TerrainTile cliffLeftTile) {

    int waterWidth = (int) Math.floor((mapSize.x - islandSize.x) / 2);
    int waterHeight = (int) Math.floor((mapSize.y - islandSize.y) / 2);

    GridPoint2 waterDimensions = new GridPoint2(waterWidth, waterHeight);

    fillLand(layer, islandSize, mapSize, waterDimensions, land);
    fillCliffs(layer, islandSize, mapSize, waterDimensions, cliffTile, cliffRightTile, cliffLeftTile);
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
   * @param cliffTile       cliff tile
   * @param cliffRightTile  cliff edge right tile
   * @param cliffLeftTile   cliff edge left tile
   */
  public static void fillCliffs(TiledMapTileLayer layer, GridPoint2 islandSize, GridPoint2 mapSize,
      GridPoint2 waterDimensions,
      TerrainTile cliffTile,
      TerrainTile cliffRightTile, TerrainTile cliffLeftTile) {

    // Cliff Edges
    for (int i = 0; i < CLIFF_HEIGHT; i++) {
      Cell cornerLeft = new Cell();
      Cell cornerRight = new Cell();

      cornerLeft.setTile(cliffLeftTile);
      cornerRight.setTile(cliffRightTile);

      layer.setCell(waterDimensions.x + i, waterDimensions.y - (i + 1), cornerLeft);
      layer.setCell(waterDimensions.x + islandSize.x + i, waterDimensions.y + islandSize.y - i - 1, cornerRight);
    }

    // Add Cliffs -- left side
    for (int x = waterDimensions.x + 1; x <= waterDimensions.x + islandSize.x; x++) {
      for (int i = 0; i < CLIFF_HEIGHT; i++) {
        Cell cell = new Cell();
        cell.setTile(cliffTile);
        layer.setCell(x + i, waterDimensions.y - (1 + i), cell);
      }
    }

    // Add Cliffs -- right side
    for (int y = waterDimensions.y; y < waterDimensions.y + islandSize.y - 1; y++) {
      for (int i = 0; i < CLIFF_HEIGHT; i++) {
        Cell cell = new Cell();
        cell.setTile(cliffTile);
        layer.setCell(waterDimensions.x + islandSize.x + i, y - i, cell);
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
    fillCliffs(layer, island_size, MAP_SIZE, waterDimensions, cliffTile, cliffRightTile, cliffLeftTile);
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
