package com.deco2800.game.components.tasks;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer.Cell;
import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.deco2800.game.ai.tasks.TaskRunner;
import com.deco2800.game.areas.terrain.IsoTileRenderer;
import com.deco2800.game.areas.terrain.TerrainComponent;
import com.deco2800.game.areas.terrain.TerrainComponent.TerrainOrientation;
import com.deco2800.game.areas.terrain.TerrainTile;
import com.deco2800.game.entities.Entity;
import com.deco2800.game.entities.EntityService;
import com.deco2800.game.entities.UGS;
import com.deco2800.game.events.EventHandler;
import com.deco2800.game.extensions.GameExtension;
import com.deco2800.game.services.DayNightCycleService;
import com.deco2800.game.services.GameTime;
import com.deco2800.game.services.ServiceLocator;
import com.deco2800.game.utils.math.Vector2Utils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyFloat;
import static org.mockito.Mockito.*;

@ExtendWith(GameExtension.class)
class MovementTaskTest {
  @BeforeEach
  void beforeEach() {
      DayNightCycleService dnservice = mock(DayNightCycleService.class);
      ServiceLocator.registerDayNightCycleService(dnservice);
      doReturn(new EventHandler()).when(dnservice).getEvents();

      EntityService entityService = mock(EntityService.class);
      ServiceLocator.registerEntityService(entityService);

      TiledMap tiledMap = new TiledMap();
      TiledMapTileLayer layer = new TiledMapTileLayer(120, 120, 65, 33);
      TerrainTile tile = mock(TerrainTile.class);
      doReturn("water").when(tile).getName();
      for (int i = 0; i < 120; i++) {
        for (int j = 0; j < 120; j++) {
          Cell cell = new Cell();
          cell.setTile(tile);
          layer.setCell(i, j, cell);
        }
      }

      tiledMap.getLayers().add(layer);
      tiledMap.getLayers().add(layer);
      

      OrthographicCamera camera = mock(OrthographicCamera.class);

      IsoTileRenderer isoTileRenderer = mock(IsoTileRenderer.class);

      when(isoTileRenderer.translateScreenToIso(any())).thenAnswer(new Answer<Vector3>() {
        @Override
        public Vector3 answer(InvocationOnMock invocation) throws Throwable {
          Object args[] = invocation.getArguments();
          return translate((Vector2) args[0]);
        }
      });

      TerrainComponent terrainComponent = new TerrainComponent(camera, tiledMap, isoTileRenderer, 
            TerrainOrientation.ISOMETRIC, 16f, null, null, null);

      Entity terrain = new Entity().addComponent(terrainComponent);
      doReturn(terrain).when(ServiceLocator.getEntityService()).getNamedEntity("terrain");

      UGS ugs = spy(new UGS());
      ugs.generateUGS();
      ServiceLocator.registerUGSService(ugs);

  }

  @Test
  void shouldGeneratePath() {
    Entity owner = new Entity();
    owner.setName("entity");
    owner.setPosition(Vector2Utils.ONE);
    
    Vector2 target = new Vector2(10f, 10f);
    MovementTask movementTask = spy(new MovementTask(target));
    TaskRunner taskRunner = mock(TaskRunner.class);
    doReturn(owner).when(taskRunner).getEntity();
    movementTask.create(taskRunner);
    
    GridPoint2 targetPos = ServiceLocator.getEntityService()
        .getNamedEntity("terrain")
        .getComponent(TerrainComponent.class)
        .worldToTilePosition(target.x, target.y);

    LinkedPoint targePoint = new LinkedPoint(targetPos.x, targetPos.y, null);

    movementTask.start();
    assertTrue(movementTask.getPath().size() > 0);
    assertTrue(movementTask.getPath().get(movementTask.getPath().size() - 1).equals(targePoint));
  }

  @Test
  void shouldMoveOnUpdate() {
    GameTime gametime = mock(GameTime.class);
    long time = 50000;
    doReturn(time).when(gametime).getTime();
    ServiceLocator.registerTimeSource(gametime);

    Entity owner = new Entity();
    owner.setName("entity");
    ServiceLocator.getUGSService().setEntity(new GridPoint2(0,0), owner, "entity");
    owner.setPosition(Vector2Utils.ONE);
    
    Vector2 target = new Vector2(10f, 10f);
    MovementTask movementTask = spy(new MovementTask(target));
    TaskRunner taskRunner = mock(TaskRunner.class);
    doReturn(owner).when(taskRunner).getEntity();
    movementTask.create(taskRunner);
    
    GridPoint2 targetPos = ServiceLocator.getEntityService()
        .getNamedEntity("terrain")
        .getComponent(TerrainComponent.class)
        .worldToTilePosition(target.x, target.y);

    LinkedPoint targePoint = new LinkedPoint(targetPos.x, targetPos.y, null);

    movementTask.start();

    int length = movementTask.getPath().size();

    movementTask.update();
    assertTrue(length > movementTask.getPath().size());
    verify(ServiceLocator.getUGSService(), times(1)).moveEntity(any(), any(), anyFloat(), anyFloat());
  }

  @Test
  void shouldPathAroundObjects() {
    Entity owner = new Entity();
    owner.setName("entity");
    ServiceLocator.getUGSService().setEntity(new GridPoint2(0,0), owner, "entity");
    owner.setPosition(ServiceLocator.getEntityService().getNamedEntity("terrain").getComponent(TerrainComponent.class).tileToWorldPosition(0, 11));
    
    for (int i = -5; i < 5; i++) {
      Entity obstacle = mock(Entity.class);
      ServiceLocator.getUGSService().setEntity(new GridPoint2(5, 10 + i), obstacle, "obstacle@" + i);
    }

    Vector2 target = ServiceLocator.getEntityService().getNamedEntity("terrain").getComponent(TerrainComponent.class).tileToWorldPosition(10,11);
    MovementTask movementTask = spy(new MovementTask(target));
    TaskRunner taskRunner = mock(TaskRunner.class);
    doReturn(owner).when(taskRunner).getEntity();
    movementTask.create(taskRunner);
    
    GridPoint2 targetPos = ServiceLocator.getEntityService()
        .getNamedEntity("terrain")
        .getComponent(TerrainComponent.class)
        .worldToTilePosition(target.x, target.y);

    LinkedPoint targePoint = new LinkedPoint(targetPos.x, targetPos.y, null);

    movementTask.start();

    assertNotNull(movementTask.getPath());
    assertTrue(movementTask.getPath().size() > 0);
    for (int i = 0; i < movementTask.getPath().size(); i++) {
      LinkedPoint point = movementTask.getPath().get(i);
      assertTrue(ServiceLocator.getUGSService().checkEntityPlacement(new GridPoint2(point.x, point.y), "test"));
    }

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
  // @Test
  // void shouldMoveOnStart() {
  // Vector2 target = new Vector2(10f, 10f);
  // MovementTask task = new MovementTask(target);

  // task.create(() -> entity);
  // task.start();
  // assertTrue(movementComponent.getMoving());
  // assertEquals(target, movementComponent.getTarget());
  // assertEquals(Status.ACTIVE, task.getStatus());
  // }

  // @Test
  // void shouldStopWhenClose() {
  // MovementTask task = new MovementTask(new Vector2(10f, 10f), 2f);
  // Entity entity = new Entity().addComponent(new PhysicsComponent());
  // PhysicsMovementComponent movementComponent = new PhysicsMovementComponent();
  // entity.addComponent(movementComponent);
  // entity.setPosition(5f, 5f);
  // entity.create();

  // task.create(() -> entity);
  // task.start();
  // task.update();
  // assertTrue(movementComponent.getMoving());
  // assertEquals(Status.ACTIVE, task.getStatus());

  // entity.setPosition(10f, 9f);
  // task.update();
  // assertFalse(movementComponent.getMoving());
  // assertEquals(Status.FINISHED, task.getStatus());
  // }

}