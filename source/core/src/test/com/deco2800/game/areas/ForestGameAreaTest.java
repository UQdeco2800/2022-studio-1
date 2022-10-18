package com.deco2800.game.areas;
import com.deco2800.game.areas.terrain.TerrainComponent;
import com.deco2800.game.entities.EntityService;
import com.deco2800.game.extensions.GameExtension;
import com.deco2800.game.input.InputService;
import com.deco2800.game.rendering.RenderService;
import com.deco2800.game.rendering.Renderer;
import net.dermetfan.gdx.assets.AnnotationAssetManager;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import com.badlogic.gdx.math.*;
import com.badlogic.gdx.utils.Array;
import com.deco2800.game.areas.terrain.EnvironmentalCollision;
import com.deco2800.game.components.CombatStatsComponent;
import com.deco2800.game.components.infrastructure.ResourceType;
import com.deco2800.game.components.npc.BossAnimationController;
import com.deco2800.game.files.SaveGame;
import com.deco2800.game.services.DayNightCycleService;
import com.deco2800.game.services.DayNightCycleStatus;
import com.deco2800.game.entities.factories.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.badlogic.gdx.audio.Music;
import com.deco2800.game.areas.terrain.TerrainFactory;
import com.deco2800.game.areas.terrain.TerrainFactory.TerrainType;
import com.deco2800.game.components.Environmental.EnvironmentalComponent;
import com.deco2800.game.components.Environmental.ValueTuple;
import com.deco2800.game.components.Environmental.EnvironmentalComponent.EnvironmentalObstacle;
import com.deco2800.game.entities.Entity;
import com.deco2800.game.components.gamearea.GameAreaDisplay;
import com.deco2800.game.services.ResourceService;
import com.deco2800.game.services.ServiceLocator;

import java.security.SecureRandom;
import java.util.*;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

@ExtendWith(GameExtension.class)
class ForestGameAreaTest {

    @Test
    void shouldDisplayUI() {
        Entity ui = mock(Entity.class);
        GameAreaDisplay gameAreaDisplay = new GameAreaDisplay("Atlantis Sinks");
        ui.addComponent(gameAreaDisplay);

        verify(ui).addComponent(gameAreaDisplay);
    }



}
