package com.deco2800.game.areas.terrain;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapRenderer;
import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.deco2800.game.areas.terrain.TerrainComponent.TerrainOrientation;
import com.deco2800.game.extensions.GameExtension;
import com.deco2800.game.services.DayNightCycleService;
import com.deco2800.game.services.GameTime;
import com.deco2800.game.services.ServiceLocator;
import com.deco2800.game.services.configs.DayNightCycleConfig;

import org.junit.Before;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;

@ExtendWith(GameExtension.class)
class TerrainComponentTest {

  @BeforeEach
  void beforeEach() {
    DayNightCycleService dayNightCycleService;

    DayNightCycleConfig config;

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

  private static TerrainComponent makeComponent(TerrainOrientation orientation, float tileSize) {
    OrthographicCamera camera = mock(OrthographicCamera.class);
    TiledMap map = mock(TiledMap.class);
    IsoTileRenderer mapRenderer = mock(IsoTileRenderer.class);
    when(mapRenderer.translateScreenToIso(new Vector2(44.06f, -0.937f)))
        .thenReturn(translate(new Vector2(44.06f, -0.937f)));
    when(mapRenderer.translateScreenToIso(new Vector2(6f, 5f)))
        .thenReturn(translate(new Vector2(6f, 5f)));
    when(mapRenderer.translateScreenToIso(new Vector2(1020f, -14.6f)))
        .thenReturn(translate(new Vector2(1020f, -14.6f)));
    GridPoint2 islandSize = new GridPoint2(120, 120);
    ArrayList<ArrayList<GridPoint2>> landTilesList = new ArrayList<>();
    return new TerrainComponent(camera, map, (TiledMapRenderer) mapRenderer, orientation, tileSize, islandSize,
        landTilesList);
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
