package com.deco2800.game.areas.terrain;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapRenderer;
import com.badlogic.gdx.maps.tiled.TiledMapTile;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer.Cell;
import com.badlogic.gdx.maps.tiled.renderers.HexagonalTiledMapRenderer;
import com.badlogic.gdx.maps.tiled.renderers.IsometricTiledMapRenderer;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.GridPoint2;
import com.deco2800.game.areas.terrain.TerrainComponent.TerrainOrientation;
import com.deco2800.game.components.CameraComponent;
import com.deco2800.game.services.ResourceService;
import com.deco2800.game.services.ServiceLocator;

/** Factory for creating game terrains. */
public class TerrainFactory {
  private final GridPoint2 MAP_SIZE = new GridPoint2(120, 120);

  private GridPoint2 island_size = new GridPoint2();

  private TerrainTile grassTile;
  private TerrainTile waterTile;
  private TerrainTile sandTile;

  private TiledMap tiledMap;

  private static ArrayList<ArrayList<ArrayList<Integer>>> levels;

  private static ArrayList<ArrayList<GridPoint2>> spawnableTilesList;

  private final OrthographicCamera camera;
  private final TerrainOrientation orientation;

  /**
   * Create a terrain factory with Orthogonal orientation
   *
   * @param cameraComponent Camera to render terrains to. Must be ortographic.
   */
  public TerrainFactory(CameraComponent cameraComponent) {
    this(cameraComponent, TerrainOrientation.ISOMETRIC);

    loadLevels();

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

  private void loadLevels() {
    levels = new ArrayList<>();
    spawnableTilesList = new ArrayList<>();
    try {
      for (int i = 0; i < 5; i++) {

        ArrayList<ArrayList<Integer>> levelDetails = new ArrayList<>();
        FileHandle handle = Gdx.files.internal("map-levels/level" + i + ".txt");
        BufferedReader in = new BufferedReader(handle.reader());

        int ci;
        int y = 0;

        levelDetails.add(new ArrayList<>());
        while ((ci = in.read()) != -1) {
          char c = (char) ci;
          if (c == '\n') {
            levelDetails.add(new ArrayList<>());
            y++;
            continue;
          }

          levelDetails.get(y).add(c - '0'); // convert character integer to corresponding integer
        }

        levels.add(levelDetails);
        in.close();
      }
    } catch (Exception e) {
      e.printStackTrace();
    }

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
            resourceService.getAsset("images/65x33_tiles/beachV1.png", Texture.class));

        TextureRegion isoWater = new TextureRegion(
            resourceService.getAsset("images/65x33_tiles/65x33v1Water.png", Texture.class));
        TextureRegion isoSand = new TextureRegion(
            resourceService.getAsset("images/65x33_tiles/beachV1.png", Texture.class));

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

    for (int i = 0; i < levels.size(); i++) {

      TiledMapTileLayer layer = new TiledMapTileLayer(MAP_SIZE.x, MAP_SIZE.y, tileSize.x, tileSize.y);
      createLevel(layer, grassTile, waterTile, sandTile, i, MAP_SIZE);
      fillWater(layer, waterTile);

      if (i != 0) {
        layer.setVisible(false);
      }

      tiledMap.getLayers().add(layer);
    }

    return tiledMap;
  }

  private static void createLevel(TiledMapTileLayer layer, TerrainTile grassTile, TerrainTile waterTile,
      TerrainTile sandTile, int levelNum, GridPoint2 map_size) {

    ArrayList<ArrayList<Integer>> level = levels.get(levelNum);
    ArrayList<GridPoint2> spawnableTiles = new ArrayList<>();

    int xoff = (int) (Math.floor((map_size.x - level.size()) / 2));
    int yoff = (int) (Math.floor((map_size.y - level.get(0).size()) / 2));

    for (int x = 0; x < level.size(); x++) {
      for (int y = 0; y < level.get(x).size(); y++) {

        Cell cell = new Cell();

        if ((level.get(x).get(y) & 1) > 0) {
          cell.setTile(sandTile);
        } else {
          cell.setTile(waterTile);
        }

        layer.setCell(x + xoff + 1, y + yoff + 1, cell);

        if ((level.get(x).get(y) & (1 << 1)) > 0) {
          // spawn world border
        }

        if ((level.get(x).get(y) & (1 << 2)) > 0) {
          spawnableTiles.add(new GridPoint2(x + xoff + 1, y + yoff + 1));
        }

      }
    }

    spawnableTilesList.add(spawnableTiles);
  }

  private void fillWater(TiledMapTileLayer layer, TerrainTile waterTile) {
    for (int x = 0; x < MAP_SIZE.x; x++) {
      for (int y = 0; y < MAP_SIZE.y; y++) {
        if (layer.getCell(x, y) == null) {
          Cell cell = new Cell();
          cell.setTile(waterTile);
          layer.setCell(x, y, cell);
        }
      }
    }
  }

  public GridPoint2 getIslandSize() {
    return this.island_size;
  }

  public GridPoint2 getMapSize() {
    return MAP_SIZE;
  }

  public ArrayList<GridPoint2> getSpawnableTiles(int level) {
    return spawnableTilesList.get(level);
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
