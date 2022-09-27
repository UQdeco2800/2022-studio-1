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

  private TiledMap tiledMap;

  private static ArrayList<ArrayList<ArrayList<Integer>>> levels;

  private static ArrayList<ArrayList<GridPoint2>> spawnableTilesList; //
  private static ArrayList<ArrayList<GridPoint2>> bordersPositionList; // These data structures need to be
  private static ArrayList<ArrayList<GridPoint2>> landTilesList; // made more efficient in a later sprint

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
    landTilesList = new ArrayList<>();

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
    float tileWorldSize = 1f;
    ResourceService resourceService = ServiceLocator.getResourceService();

    TextureRegion water = new TextureRegion(resourceService.getAsset("images/65x33_tiles/water.png", Texture.class));
    GridPoint2 tilePixelSize = new GridPoint2(water.getRegionWidth(), water.getRegionHeight());

    TiledMap tiledMap = createMap(tilePixelSize);
    TiledMapRenderer renderer = createRenderer(tiledMap, tileWorldSize / tilePixelSize.x);

    return new TerrainComponent(camera, tiledMap, renderer, orientation, tileWorldSize, island_size, landTilesList);
  }

  private TiledMapRenderer createRenderer(TiledMap tiledMap, float tileScale) {
    return new IsometricTiledMapRenderer(tiledMap, tileScale);
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
  private TiledMap createMap(GridPoint2 tileSize) {
    tiledMap = new TiledMap();

    ResourceService resourceService = ServiceLocator.getResourceService();

    TerrainTile waterTile = loadTile("water", TileType.WATER, resourceService);
    TerrainTile sandTile = loadTile("sand", TileType.SAND, resourceService);
    TerrainTile[] seaweedTiles = new TerrainTile[3];
    TerrainTile shorelineTile = loadTile("shoreline", TileType.SHORELINE, resourceService);

    TerrainTile waterNightTile = loadTile("water_night", TileType.WATER, resourceService);
    TerrainTile sandNightTile = loadTile("sand_night", TileType.SAND, resourceService);
    TerrainTile seaweedNightTiles[] = new TerrainTile[3];
    TerrainTile shorelineNightTile = loadTile("shoreline_night", TileType.SHORELINE, resourceService);

    for (int i = 1; i < 4; i++) {
      seaweedTiles[i - 1] = loadTile("seaweed_" + i, TileType.SAND, resourceService);
      seaweedNightTiles[i - 1] = loadTile("seaweed_" + i + "_night", TileType.SAND, resourceService);
    }

    // Levels will be added to the array like so:
    // {Level0, Level0Night, Level1, Level1Night, ... , LevelN, levelNNight}

    for (int i = 0; i < levels.size(); i++) {

      TiledMapTileLayer dayLayer = new TiledMapTileLayer(MAP_SIZE.x, MAP_SIZE.y, tileSize.x, tileSize.y);
      createLevel(dayLayer, waterTile, sandTile, seaweedTiles, shorelineTile, i, MAP_SIZE);
      fillWater(dayLayer, waterTile);

      TiledMapTileLayer nightLayer = new TiledMapTileLayer(MAP_SIZE.x, MAP_SIZE.y, tileSize.x, tileSize.y);
      createLevel(nightLayer, waterNightTile, sandNightTile, seaweedNightTiles, shorelineNightTile, i, MAP_SIZE);
      fillWater(nightLayer, waterNightTile);

      if (i != 0) {
        nightLayer.setVisible(false);
      }
      dayLayer.setVisible(false);

      tiledMap.getLayers().add(dayLayer);
      tiledMap.getLayers().add(nightLayer);
    }

    return tiledMap;
  }

  private TerrainTile loadTile(String tileName, TileType tileType, ResourceService resourceService) {
    TextureRegion tex = new TextureRegion(
        resourceService.getAsset("images/65x33_tiles/" + tileName + ".png", Texture.class));
    if (tileType == TileType.SAND) {
      return new TerrainTile(tex, "sand");
    } else if (tileType == TileType.WATER) {
      return new TerrainTile(tex, "water");
    } else {
      return new TerrainTile(tex, "shoreline");
    }
  }

  private static void createLevel(TiledMapTileLayer layer, TerrainTile waterTile,
      TerrainTile sandTile, TerrainTile[] seaweedTiles, TerrainTile shorelineTile, int levelNum,
      GridPoint2 map_size) {

    ArrayList<ArrayList<Integer>> level = levels.get(levelNum);
    ArrayList<GridPoint2> spawnableTiles = new ArrayList<>();
    ArrayList<GridPoint2> landTiles = new ArrayList<>();

    int xoff = (int) (Math.floor((map_size.x - level.size()) / 2));
    int yoff = (int) (Math.floor((map_size.y - level.get(0).size()) / 2));

    for (int x = 0; x < level.size(); x++) {
      for (int y = 0; y < level.get(x).size(); y++) {

        Cell cell = new Cell();

        // check if land bit is set
        if ((level.get(x).get(y) & 1) > 0) {

          landTiles.add(new GridPoint2(x + xoff + 1, y + yoff + 1));

          // Randomly choose a land tile to use
          // - 1/8 chance for ground tile, seaweed1 tile, seaweed 2 tile
          // - 5/8 chance for sand tile
          int r = (int) (Math.random() * 7);

          if (r < 3) {
            cell.setTile(seaweedTiles[r]);
          } else {
            cell.setTile(sandTile);
          }

        } else if ((level.get(x).get(y) & (1 << 1)) > 0) { // check for shoreline bit
          cell.setTile(shorelineTile);
        } else {
          cell.setTile(waterTile);
        }

        layer.setCell(x + xoff + 1, y + yoff + 1, cell);

        // check to see if shoreline bit is set

        // check to see if spawnable bit is set
        if ((level.get(x).get(y) & (1 << 2)) > 0) {
          spawnableTiles.add(new GridPoint2(x + xoff + 1, y + yoff + 1));
        }

      }
    }

    spawnableTilesList.add(spawnableTiles);
    landTilesList.add(landTiles);
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

  public enum TileType {
    WATER,
    SAND,
    SHORELINE
  }
}
