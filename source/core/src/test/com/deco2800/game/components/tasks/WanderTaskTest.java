package com.deco2800.game.components.tasks;

import com.deco2800.game.extensions.GameExtension;
import com.deco2800.game.services.GameTime;
import com.deco2800.game.services.ServiceLocator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(GameExtension.class)
@ExtendWith(MockitoExtension.class)
class WanderTaskTest {
  @Mock
  GameTime gameTime;

  @BeforeEach
  void beforeEach() {
    ServiceLocator.registerTimeSource(gameTime);
  }

//  @Test
//  void shouldTriggerEvent() {
//    WanderTask wanderTask = new WanderTask(Vector2Utils.ONE, 1f);
//
//    AITaskComponent aiTaskComponent = new AITaskComponent().addTask(wanderTask);
//    Entity entity = new Entity().addComponent(aiTaskComponent).addComponent(new PhysicsMovementComponent());
//    entity.create();
//
//    // Register callbacks
//    EventListener0 callback = mock(EventListener0.class);
//    entity.getEvents().addListener("wanderStart", callback);
//
//    wanderTask.start();
//
//    verify(callback).handle();
//  }
}