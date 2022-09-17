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
  private final GridPoint2 MAP_SIZE = new GridPoint2(10, 10);

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
    this.camera.zoom = 5f;
    this.orientation = orientation;
  }

  private void loadLevels() {
    levels = new ArrayList<>();
    spawnableTilesList = new ArrayList<>();
    bordersPositionList = new ArrayList<>();
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
    ResourceService resourceService = ServiceLocator.getResourceService();
    switch (terrainType) {
      case FOREST_DEMO_ISO:
        TextureRegion isoWater = new TextureRegion(
            resourceService.getAsset("images/65x33_tiles/dayWaterTile.png", Texture.class));
        TextureRegion isoSand = new TextureRegion(
            resourceService.getAsset("images/65x33_tiles/beachV1.png", Texture.class));
        TextureRegion isoGround = new TextureRegion(
            resourceService.getAsset("images/65x33_tiles/groundTileV1.png", Texture.class));
        TextureRegion isoSeaweed1 = new TextureRegion(
            resourceService.getAsset("images/65x33_tiles/seaweedV4.png", Texture.class));
        TextureRegion isoSeaweed2 = new TextureRegion(
            resourceService.getAsset("images/65x33_tiles/seaweedV5.png", Texture.class));
        return createForestDemoTerrain(8f, isoWater, isoSand, isoGround, isoSeaweed1, isoSeaweed2);
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
      float tileWorldSize, TextureRegion water, TextureRegion sand, TextureRegion ground,
      TextureRegion seaweed1, TextureRegion seaweed2) {
    GridPoint2 tilePixelSize = new GridPoint2(water.getRegionWidth(), water.getRegionHeight());
    TiledMap tiledMap = createForestDemoTiles(tilePixelSize, water, sand, ground, seaweed1, seaweed2);
    TiledMapRenderer renderer = createRenderer(tiledMap, tileWorldSize / tilePixelSize.x);
    return new TerrainComponent(camera, tiledMap, renderer, orientation, tileWorldSize, island_size,
        bordersPositionList, landTilesList);
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
      GridPoint2 tileSize, TextureRegion water, TextureRegion sand, TextureRegion ground, TextureRegion seaweed1,
      TextureRegion seaweed2) {
    tiledMap = new TiledMap();

    TerrainTile waterTile = new TerrainTile(water, "water");
    TerrainTile sandTile = new TerrainTile(sand, "sand");
    TerrainTile groundTile = new TerrainTile(ground, "sand");
    TerrainTile seaweedTile1 = new TerrainTile(seaweed1, "sand");
    TerrainTile seaweedTile2 = new TerrainTile(seaweed2, "sand");

    for (int i = 0; i < levels.size(); i++) {

      TiledMapTileLayer layer = new TiledMapTileLayer(MAP_SIZE.x, MAP_SIZE.y, tileSize.x, tileSize.y);
      createLevel(layer, waterTile, sandTile, groundTile, seaweedTile1, seaweedTile2, i, MAP_SIZE);
      fillWater(layer, waterTile);

      if (i != 0) {
        layer.setVisible(false);
      }

      tiledMap.getLayers().add(layer);
    }

    return tiledMap;
  }

  private static void createLevel(TiledMapTileLayer layer, TerrainTile waterTile,
      TerrainTile sandTile, TerrainTile groundTile, TerrainTile seaweedTile1, TerrainTile seaweedTile2, int levelNum,
      GridPoint2 map_size) {

    ArrayList<ArrayList<Integer>> level = levels.get(levelNum);
    ArrayList<GridPoint2> spawnableTiles = new ArrayList<>();
    ArrayList<GridPoint2> borders = new ArrayList<>();
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
          int r = (int) (Math.random() * 7) + 1;
          switch (r) {
            case 1:
              cell.setTile(groundTile);
              break;
            case 2:
              cell.setTile(seaweedTile1);
              break;
            case 3:
              cell.setTile(seaweedTile2);
            default:
              cell.setTile(sandTile);
          }

        } else {
          cell.setTile(waterTile);
        }

        layer.setCell(x + xoff + 1, y + yoff + 1, cell);

        // check to see if border bit is set
        if ((level.get(x).get(y) & (1 << 1)) > 0) {
          borders.add(new GridPoint2(x + xoff, y + yoff + 1));
        }

        // check to see if spawnable bit is set
        if ((level.get(x).get(y) & (1 << 2)) > 0) {
          spawnableTiles.add(new GridPoint2(x + xoff + 1, y + yoff + 1));
        }

      }
    }

    bordersPositionList.add(borders);
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
}
