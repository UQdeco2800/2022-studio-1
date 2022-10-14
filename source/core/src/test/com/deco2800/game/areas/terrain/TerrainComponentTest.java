package com.deco2800.game.areas.terrain;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.security.Provider.Service;
import java.util.ArrayList;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapRenderer;
import com.badlogic.gdx.maps.tiled.TiledMapTile;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer.Cell;
import com.badlogic.gdx.maps.tiled.tiles.StaticTiledMapTile;
import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.deco2800.game.areas.terrain.TerrainComponent.TerrainOrientation;
import com.deco2800.game.entities.Entity;
import com.deco2800.game.entities.EntityService;
import com.deco2800.game.entities.UGS;
import com.deco2800.game.extensions.GameExtension;
import com.deco2800.game.rendering.RenderService;
import com.deco2800.game.services.DayNightCycleService;
import com.deco2800.game.services.DayNightCycleStatus;
import com.deco2800.game.services.GameTime;
import com.deco2800.game.services.ServiceLocator;
import com.deco2800.game.services.configs.DayNightCycleConfig;

import org.junit.Before;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

@ExtendWith(GameExtension.class)
class TerrainComponentTest {

  @BeforeEach
  void beforeEach() {
    DayNightCycleService dayNightCycleService;

    DayNightCycleConfig config;

    EntityService entityService = new EntityService();
    ServiceLocator.registerEntityService(entityService);

    config = new DayNightCycleConfig();
    config.dawnLength = 150;
    config.dayLength = 600;
    config.duskLength = 150;
    config.nightLength = 300;
    config.maxDays = 1;
    ServiceLocator.registerTimeSource(new GameTime());
    var gameTime = Mockito.spy(ServiceLocator.getTimeSource());
    dayNightCycleService = Mockito.spy(new DayNightCycleService(gameTime, config));
    ServiceLocator.registerDayNightCycleService(dayNightCycleService);
  }

  @Test
  void shouldConvertPositionIsometric() {
    // had to change test to satisfy new tile dimensions
    TerrainComponent component = makeComponent(TerrainOrientation.ISOMETRIC, 16f);
    assertEquals(new Vector2(4.0f, 2.0f), component.tileToWorldPosition(0, 0));
    assertEquals(new Vector2(1020.0f, -18.0f), component.tileToWorldPosition(66, 61));
  }

  @Test
  void shouldConvertWorldToIsometric() {
    TerrainComponent component = makeComponent(TerrainOrientation.ISOMETRIC, 16f);
    assertEquals(new GridPoint2(3, 2), component.worldToTilePosition(44.06f, -0.937f));
    assertEquals(new GridPoint2(0, 0), component.worldToTilePosition(6f, 5f));
    assertEquals(new GridPoint2(66, 61), component.worldToTilePosition(1020f, -14.6f));
  }

  @Test
  void shouldIncrementLevel() {
    TerrainComponent component = spy(makeComponent(TerrainOrientation.ISOMETRIC, 16f));
    
    TiledMapTileLayer layer1 = new TiledMapTileLayer(0, 0, 0, 0);
    TiledMapTileLayer layer1Night = new TiledMapTileLayer(1, 1, 1, 1);
    TiledMapTileLayer layer2 = new TiledMapTileLayer(0, 0, 0, 0);
    TiledMapTileLayer layer2Night = new TiledMapTileLayer(1, 1, 1, 1);

    layer1.setVisible(true);
    layer1Night.setVisible(false);
    layer2.setVisible(false);
    layer2Night.setVisible(false);

    component.getMap().getLayers().add(layer1);
    component.getMap().getLayers().add(layer1Night);
    component.getMap().getLayers().add(layer2);
    component.getMap().getLayers().add(layer2Night);

    doNothing().when(component).updateUGS();
    doNothing().when(component).damageSunkenBuildings();

    component.incrementMapLvl();
    component.decrementMapLvl();
    
    assertEquals(component.getCurrentMapLvl(), 0);
    assertTrue(layer1.isVisible());
    assertFalse(layer1Night.isVisible());
    assertFalse(layer2.isVisible());
    assertFalse(layer2Night.isVisible());

    component.decrementMapLvl();
    assertTrue(layer1.isVisible());
    assertFalse(layer1Night.isVisible());
    assertFalse(layer2.isVisible());
    assertFalse(layer2Night.isVisible());
  }

  @Test
  void shouldDamageBuildings() {
    TerrainComponent component = spy(makeComponent(TerrainOrientation.ISOMETRIC, 16f));
    UGS ugsFake = mock(UGS.class);
    ServiceLocator.registerUGSService(ugsFake);

    Entity building1 = new Entity();
    building1.setName("trap");
    Entity building2 = new Entity();
    building2.setName("woodCutter");

    Entity sandBuilding = new Entity();
    sandBuilding.setName("wall"); // structure on sand
    Entity player = new Entity();
    player.setName("player"); // non structure entity


    ServiceLocator.getEntityService().registerNamed("trap", building1);
    ServiceLocator.getEntityService().registerNamed("woodCutter", building2);
    ServiceLocator.getEntityService().registerNamed("wall", sandBuilding);
    ServiceLocator.getEntityService().registerNamed("player", player);
    

    when(ugsFake.getEntity(any())).thenReturn(null);
    when(ugsFake.getEntity(new GridPoint2(0, 0))).thenReturn(building1);
    when(ugsFake.getEntity(new GridPoint2(1, 0))).thenReturn(building2);
    when(ugsFake.getEntity(new GridPoint2(2, 0))).thenReturn(sandBuilding);
    when(ugsFake.getEntity(new GridPoint2(3, 0))).thenReturn(player);

    when(ugsFake.getTileType(new GridPoint2(0, 0))).thenReturn("water");
    when(ugsFake.getTileType(new GridPoint2(1, 0))).thenReturn("water");
    when(ugsFake.getTileType(new GridPoint2(2, 0))).thenReturn("sand");
    when(ugsFake.getTileType(new GridPoint2(3, 0))).thenReturn("water");

    doReturn(new GridPoint2(4, 1)).when(component).getMapBounds(anyInt());

    doAnswer(new Answer() {
      @Override
      public Object answer(InvocationOnMock invocation) throws Throwable {
        String name = (String) invocation.getArguments()[0];
        switch (name) {
          case "trap":
            ServiceLocator.getEntityService().unregisterNamed("trap");
            break;
          case "woodCutter":
            ServiceLocator.getEntityService().unregisterNamed("woodCutter");
            break;
          case "wall":
            ServiceLocator.getEntityService().unregisterNamed("wall");
            break;
          case "player":
            ServiceLocator.getEntityService().unregisterNamed("person");
            break;
        }

        return null;
      }
    }).when(ugsFake).removeEntity(any());

    component.damageSunkenBuildings();
    
    assertNull(ServiceLocator.getEntityService().getNamedEntity("trap"));
    assertNull(ServiceLocator.getEntityService().getNamedEntity("woodCutter"));
    assertNotNull(ServiceLocator.getEntityService().getNamedEntity("wall"));
    assertNotNull(ServiceLocator.getEntityService().getNamedEntity("player"));
    


  }

  @Test
  void shouldChangeAtNight() {
    TerrainComponent component = spy(makeComponent(TerrainOrientation.ISOMETRIC, 16f));

    TiledMapTileLayer layer1 = new TiledMapTileLayer(0, 0, 0, 0);
    TiledMapTileLayer layer1Night = new TiledMapTileLayer(1, 1, 1, 1);

    layer1.setVisible(true);
    layer1Night.setVisible(false);

    component.getMap().getLayers().add(layer1);
    component.getMap().getLayers().add(layer1Night);

    component.partOfDayPassed(DayNightCycleStatus.DAY);
    assertTrue(layer1.isVisible());
    assertFalse(layer1Night.isVisible());

    component.partOfDayPassed(DayNightCycleStatus.NIGHT);
    assertTrue(layer1Night.isVisible());
    assertFalse(layer1.isVisible());

    component.partOfDayPassed(DayNightCycleStatus.DAY);
    assertTrue(layer1.isVisible());
    assertFalse(layer1Night.isVisible());

  }

  @Test
  void shouldUpdateUGS() {

    UGS ugsFake = mock(UGS.class);
    ServiceLocator.registerUGSService(ugsFake);
    ServiceLocator.registerRenderService(new RenderService());

    TerrainComponent component = spy(makeComponent(TerrainOrientation.ISOMETRIC, 16f));
    ServiceLocator.getEntityService().registerNamed("terrain", new Entity().addComponent(component));

    TiledMapTileLayer layer = new TiledMapTileLayer(120, 120, 1, 1);
    for (int i = 0; i < 120; i++) {
      for (int j = 0; j < 120; j++) {
        Cell cell = new Cell();
        Array<StaticTiledMapTile> tileFrames = new Array<>();
        cell.setTile(new TerrainTile(1, tileFrames, "sand"));
        layer.setCell(i, j, cell);
      }
    }

    component.getMap().getLayers().add(layer);

    ugsFake.generateUGS();

    component.updateUGS();

    verify(ugsFake, times(120 * 120)).setTileType(any(), any());
  }

  private static TerrainComponent makeComponent(TerrainOrientation orientation, float tileSize) {
    OrthographicCamera camera = mock(OrthographicCamera.class);
    TiledMap map = new TiledMap();
    IsoTileRenderer mapRenderer = mock(IsoTileRenderer.class);
    when(mapRenderer.translateScreenToIso(new Vector2(44.06f, -0.937f)))
        .thenReturn(translate(new Vector2(44.06f, -0.937f)));
    when(mapRenderer.translateScreenToIso(new Vector2(6f, 5f)))
        .thenReturn(translate(new Vector2(6f, 5f)));
    when(mapRenderer.translateScreenToIso(new Vector2(1020f, -14.6f)))
        .thenReturn(translate(new Vector2(1020f, -14.6f)));
    GridPoint2 islandSize = new GridPoint2(120, 120);
    ArrayList<ArrayList<GridPoint2>> landTilesList = new ArrayList<>();
    ArrayList<ArrayList<GridPoint2>> walls = new ArrayList<>();
    return new TerrainComponent(camera, map, (TiledMapRenderer) mapRenderer, orientation, tileSize, islandSize,
        landTilesList, walls);
  }

  private static Vector3 translate(Vector2 pos) {
    // create the isometric transform
    Matrix4 isoTransform = new Matrix4();
    isoTransform.idt();

    // isoTransform.translate(0, 32, 0);
    isoTransform.scale((float) (Math.sqrt(2.0) / 2.0), (float) (Math.sqrt(2.0) / 4.0), 1.0f);
    isoTransform.rotate(0.0f, 0.0f, 1.0f, -45);

    // ... and the inverse matrix
    Matrix4 invIsotransform = new Matrix4(isoTransform);
    invIsotransform.inv();

    Vector3 screenPos = new Vector3();
    screenPos.set(pos.x, pos.y, 0);
    screenPos.mul(invIsotransform);

    return screenPos;
  }

}
