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
  private static final GridPoint2 INITIAL_ISLAND_SIZE = new GridPoint2(30, 30);
  private static final int TUFT_TILE_COUNT = 30;
  private static final int ROCK_TILE_COUNT = 30;

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
   * Create a terrain factory
   *
   * @param cameraComponent Camera to render terrains to. Must be orthographic.
   * @param orientation     orientation to render terrain at
   */
  public TerrainFactory(CameraComponent cameraComponent, TerrainOrientation orientation) {
    this.camera = (OrthographicCamera) cameraComponent.getCamera();
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
        TextureRegion isoGrass = new TextureRegion(resourceService.getAsset("images/iso_grass_1.png", Texture.class));
        TextureRegion isoTuft = new TextureRegion(resourceService.getAsset("images/iso_grass_2.png", Texture.class));
        TextureRegion isoRocks = new TextureRegion(resourceService.getAsset("images/iso_grass_3.png", Texture.class));
        TextureRegion isoWater = new TextureRegion(
            resourceService.getAsset("images/iso_water_placeholder.png", Texture.class));
        TextureRegion isoCliff = new TextureRegion(resourceService.getAsset("images/iso_cliff.png", Texture.class));
        TextureRegion isoCliffLeft = new TextureRegion(
            resourceService.getAsset("images/iso_cliff_edge_left.png", Texture.class));
        TextureRegion isoCliffRight = new TextureRegion(
            resourceService.getAsset("images/iso_cliff_edge_right.png", Texture.class));
        return createForestDemoTerrain(0.5f, isoGrass, isoTuft, isoRocks, isoWater, isoCliff, isoCliffLeft,
            isoCliffRight);
      default:
        return null;
    }
  }

  private TerrainComponent createForestDemoTerrain(
      float tileWorldSize, TextureRegion grass, TextureRegion grassTuft, TextureRegion rocks, TextureRegion water,
      TextureRegion cliff, TextureRegion cliffLeft, TextureRegion cliffRight) {
    GridPoint2 tilePixelSize = new GridPoint2(grass.getRegionWidth(), grass.getRegionHeight());
    TiledMap tiledMap = createForestDemoTiles(tilePixelSize, grass, grassTuft, rocks, water, cliff, cliffLeft,
        cliffRight);
    TiledMapRenderer renderer = createRenderer(tiledMap, tileWorldSize / tilePixelSize.x);
    return new TerrainComponent(camera, tiledMap, renderer, orientation, tileWorldSize);
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

  private TiledMap createForestDemoTiles(
      GridPoint2 tileSize, TextureRegion grass, TextureRegion grassTuft, TextureRegion rocks, TextureRegion water,
      TextureRegion cliff, TextureRegion cliffLeft, TextureRegion cliffRight) {
    TiledMap tiledMap = new TiledMap();
    TerrainTile grassTile = new TerrainTile(grass);
    TerrainTile grassTuftTile = new TerrainTile(grassTuft);
    TerrainTile rockTile = new TerrainTile(rocks);
    TerrainTile waterTile = new TerrainTile(water);
    TerrainTile cliffTile = new TerrainTile(cliff);
    TerrainTile cliffRightTile = new TerrainTile(cliffRight);
    TerrainTile cliffLeftTile = new TerrainTile(cliffLeft);
    TiledMapTileLayer layer = new TiledMapTileLayer(MAP_SIZE.x, MAP_SIZE.y, tileSize.x, tileSize.y);

    // Create base grass
    fillTiles(layer, INITIAL_ISLAND_SIZE, MAP_SIZE, waterTile, grassTile, cliffTile, cliffRightTile, cliffLeftTile);

    // Add some grass and rocks
    // fillTilesAtRandom(layer, MAP_SIZE, grassTuftTile, TUFT_TILE_COUNT);
    // fillTilesAtRandom(layer, MAP_SIZE, rockTile, ROCK_TILE_COUNT);

    tiledMap.getLayers().add(layer);
    return tiledMap;
  }

  private static void fillTilesAtRandom(
      TiledMapTileLayer layer, GridPoint2 mapSize, TerrainTile tile, int amount) {
    GridPoint2 min = new GridPoint2(0, 0);
    GridPoint2 max = new GridPoint2(mapSize.x - 1, mapSize.y - 1);

    for (int i = 0; i < amount; i++) {
      GridPoint2 tilePos = RandomUtils.random(min, max);
      Cell cell = layer.getCell(tilePos.x, tilePos.y);
      cell.setTile(tile);
    }
  }

  private static void fillTiles(TiledMapTileLayer layer, GridPoint2 islandSize, GridPoint2 mapSize, TerrainTile water,
      TerrainTile land, TerrainTile cliffTile, TerrainTile cliffRightTile, TerrainTile cliffLeftTile) {

    int waterWidth = (int) Math.floor((mapSize.x - islandSize.x) / 2);
    int waterHeight = (int) Math.floor((mapSize.y - islandSize.y) / 2);

    // Fill island tiles
    for (int x = waterWidth; x <= islandSize.x + waterWidth; x++) {
      for (int y = waterHeight; y <= islandSize.y + waterHeight; y++) {
        Cell cell = new Cell();
        cell.setTile(land);
        layer.setCell(x, y, cell);
      }
    }

    // Add Cliff Corners
    Cell leftCorner = new Cell();
    leftCorner.setTile(cliffLeftTile);
    Cell rightCorner = new Cell();
    rightCorner.setTile(cliffRightTile);
    layer.setCell(waterWidth, waterHeight - 1, leftCorner);
    layer.setCell(waterWidth + islandSize.x + 1, waterHeight + islandSize.y, rightCorner);

    // Add Cliffs -- left side
    for (int x = waterWidth + 1; x <= waterWidth + islandSize.x + 1; x++) {
      Cell cell = new Cell();
      cell.setTile(cliffTile);
      layer.setCell(x, waterHeight - 1, cell);
    }

    // Add Cliffs -- right side
    for (int y = waterHeight; y < waterHeight + islandSize.y; y++) {
      Cell cell = new Cell();
      cell.setTile(cliffTile);
      layer.setCell(waterWidth + islandSize.x + 1, y, cell);
    }

    // Fill remaining tiles with water
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
