package com.deco2800.game.areas;

import com.deco2800.game.areas.terrain.TerrainFactory;
import com.deco2800.game.entities.Entity;
import com.deco2800.game.entities.EntityService;
import com.deco2800.game.extensions.GameExtension;
import com.deco2800.game.services.DayNightCycleService;
import com.deco2800.game.services.DayNightCycleStatus;
import com.deco2800.game.services.GameTime;
import com.deco2800.game.services.ServiceLocator;
import com.deco2800.game.services.configs.DayNightCycleConfig;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

@ExtendWith(GameExtension.class)
class GameAreaTest {
  @Test
  void shouldSpawnEntities() {
    TerrainFactory factory = mock(TerrainFactory.class);

    GameArea gameArea = new GameArea() {
      @Override
      public void create() {
      }
    };

    ServiceLocator.registerEntityService(new EntityService());
    Entity entity = mock(Entity.class);

    gameArea.spawnEntity(entity);
    ServiceLocator.getEntityService().register(entity);
    verify(entity).create();

    gameArea.dispose();
    verify(entity).dispose();
  }

  /**
   * Checks whether entities can be spawned at a certain part of day
   * There were many errors regarding creating entities using methods called by
   * DayNightCycleService's listener
   */
  @Test
  void shouldSpawnEntitiesAtNight() {
    DayNightCycleConfig config = new DayNightCycleConfig();
    config.dawnLength = 100;
    config.dayLength = 400;
    config.duskLength = 100;
    config.nightLength = 500;
    config.maxDays = 1;
    ServiceLocator.registerTimeSource(new GameTime());
    var gameTime = Mockito.spy(ServiceLocator.getTimeSource());
    DayNightCycleService dayNightCycleService = Mockito.spy(new DayNightCycleService(gameTime, config));

    GameArea gameArea = new GameArea() {
      @Override
      public void create() {
      }
    };

    ServiceLocator.registerEntityService(new EntityService());
    Entity entity = mock(Entity.class);

    dayNightCycleService.getEvents().addListener(DayNightCycleService.EVENT_PART_OF_DAY_PASSED,
        (DayNightCycleStatus day) -> {
          switch (day) {
            case DAWN:
              break;
            case DAY:
              break;
            case DUSK:
              break;
            case NIGHT:
              gameArea.spawnEntity(entity);
              assertEquals(DayNightCycleStatus.NIGHT, dayNightCycleService.getCurrentCycleStatus());
              verify(entity).create();
              break;
          }
        });
    dayNightCycleService.start().join();
    gameArea.dispose();
    verify(entity).dispose();
  }
}
