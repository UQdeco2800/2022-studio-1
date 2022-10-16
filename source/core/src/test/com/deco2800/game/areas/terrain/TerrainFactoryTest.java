package com.deco2800.game.areas.terrain;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mockConstruction;
import static org.mockito.Mockito.spy;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.MockedConstruction;
import org.mockito.Mockito;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.renderers.IsometricTiledMapRenderer;
import com.deco2800.game.areas.terrain.TerrainFactory.TerrainType;
import com.deco2800.game.components.CameraComponent;
import com.deco2800.game.entities.EntityService;
import com.deco2800.game.extensions.GameExtension;
import com.deco2800.game.input.InputService;
import com.deco2800.game.rendering.RenderService;
import com.deco2800.game.rendering.Renderer;
import com.deco2800.game.services.DayNightCycleService;
import com.deco2800.game.services.GameTime;
import com.deco2800.game.services.ResourceService;
import com.deco2800.game.services.ServiceLocator;
import com.deco2800.game.services.configs.DayNightCycleConfig;

@ExtendWith(GameExtension.class)
class TerrainFactoryTest {

        TerrainFactory terrainFactory;
        TerrainComponent component;
        Renderer renderer;

        @BeforeEach
        void beforeEach() {

                String[] forestTextures = {"test/files/invalidTile.png"};

                DayNightCycleConfig config;

                config = new DayNightCycleConfig();
                config.dawnLength = 150;
                config.dayLength = 600;
                config.duskLength = 150;
                config.nightLength = 300;
                config.maxDays = 1;

                ServiceLocator.registerTimeSource(new GameTime());
                var gameTime = Mockito.spy(ServiceLocator.getTimeSource());

                DayNightCycleService dayNightCycleService = Mockito.spy(new DayNightCycleService(gameTime, config));

                ServiceLocator.registerDayNightCycleService(dayNightCycleService);
                ServiceLocator.registerInputService(new InputService());
                        ServiceLocator.registerEntityService(new EntityService());
                ServiceLocator.registerRenderService(new RenderService());

                ResourceService resourceService = spy(new ResourceService());
                resourceService.loadTextures(forestTextures);
                ServiceLocator.registerResourceService(resourceService);

                while (!resourceService.loadForMillis(1)) {
                        ;
                }

                doReturn(resourceService.getAsset("test/files/invalidTile.png", Texture.class)).when(ServiceLocator.getResourceService()).getAsset(any(), any());
                

                CameraComponent cameraComponent = new CameraComponent();
                terrainFactory = spy(new TerrainFactory(cameraComponent));

                try (MockedConstruction<IsoTileRenderer> mockedRenderer = mockConstruction(IsoTileRenderer.class)) {
                        this.component = terrainFactory.createTerrain(TerrainType.FOREST_DEMO_ISO);
                }

               
        }

        @Test
        void shouldLoadLevels() {

                assertNotNull(this.component.getMap());
                assertTrue(this.component.getMap().getLayers().size() > 0);
        }

        @Test
        void shouldLoadTiles() {
                TiledMapTileLayer layer = this.component.getTileMapTileLayer(0);

                boolean waterTileFound = false;
                boolean sandTileFound = false;
                boolean shorelineTileFound = false;
                boolean invalidTileFound = false;

                for (int i = 0; i < layer.getHeight(); i++) {
                        for (int j = 0; j < layer.getWidth(); j++) {
                                String tileType = ((TerrainTile) layer.getCell(i, j).getTile()).getName();
                                switch (tileType) {
                                        case "water":
                                                waterTileFound = true;
                                                break;
                                        case "sand":
                                                sandTileFound = true;
                                                break;
                                        case "shoreline":
                                                shorelineTileFound = true;
                                                break;
                                        default:
                                                invalidTileFound = true;
                                }

                                if (invalidTileFound) {
                                        fail("Tile created without a type");
                                }

                        }
                }

                assertTrue(waterTileFound && sandTileFound && shorelineTileFound);

        }

        @Test
        void shouldFillMap() {
                TiledMapTileLayer layer = this.component.getTileMapTileLayer(0);

                for (int i = 0; i < layer.getHeight(); i++) {
                        for (int j = 0; j < layer.getWidth(); j++) {
                                if (layer.getCell(i, j) == null || layer.getCell(i, j).getTile() == null) {
                                        fail("Found missing tile in the map");
                                }
                        }
                }

                return;
        }
}
