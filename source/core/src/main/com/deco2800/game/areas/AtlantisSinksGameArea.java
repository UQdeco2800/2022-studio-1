package com.deco2800.game.areas;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.deco2800.game.areas.terrain.TerrainFactory;
import com.deco2800.game.areas.terrain.TerrainTile;
import com.deco2800.game.components.CameraComponent;
import com.deco2800.game.entities.Entity;
import com.deco2800.game.entities.factories.CrystalFactory;
import com.deco2800.game.entities.factories.ObstacleFactory;
import com.deco2800.game.entities.factories.PlayerFactory;
import com.deco2800.game.memento.CareTaker;
import com.deco2800.game.services.ResourceService;
import com.deco2800.game.services.ServiceLocator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;



public class AtlantisSinksGameArea extends GameArea {

    private static final Logger logger = LoggerFactory.getLogger(AtlantisSinksGameArea.class);
    private static final GridPoint2 PLAYER_SPAWN = new GridPoint2(60, 60);

    private static final String[] gameTextures = {
            "images/Centaur_Back_left.png",
            "images/Centaur_Back_right.png",
            "images/Centaur_left.png",
            "images/Centaur_right.png",
            "images/tree.png",
            "images/grass_1.png",
            "images/grass_2.png",
            "images/grass_3.png",
            "images/hex_grass_1.png",
            "images/hex_grass_2.png",
            "images/hex_grass_3.png",
            "images/iso_grass_1.png",
            "images/iso_grass_2.png",
            "images/iso_grass_3.png",
            "images/500_grassTile.png",
            "images/500_waterFullTile.png",
            "images/500_waterAndDirtFullTile.png",
            "images/waterFinalVersion.png",
            "images/fullSizedDirt.png",
            "images/waterDirtMerged.png",
            "images/trial3GrassTile.png",
            "images/wallTransparent.png",
            "images/landscape_objects/almond-tree-60x62.png",
            "images/landscape_objects/fig-tree-60x62.png",
            "images/landscape_objects/limestone-boulder-60x60.png",
            "images/landscape_objects/marble-stone-60x40.png",
            "images/landscape_objects/vines.png",
            "images/landscape_objects/cypress-tree-60x100.png",
            "images/landscape_objects/geyser.png",
            "images/landscape_objects/billboard.png",
            "images/landscape_objects/chalice.png",
            "images/landscape_objects/pillar.png",
            "images/landscape_objects/wooden-fence-60x60.png",
            "images/pirate_crab_NE.png",
            "images/pirate_crab_NW.png",
            "images/pirate_crab_SE.png",
            "images/pirate_crab_SW.png",
            "images/crystal.png",
            "images/stoneQuarryTest.png",
            "images/wall-right.png",
            "images/mini_tower.png",
            "images/Eel_Bright_SW.png",
            "images/Eel_Bright_NE.png",
            "images/Eel_Bright_NW.png",
            "images/Eel_Bright_SW.png"
    };

    private static final String[] gameSounds = { "sounds/sword_swing.mp3" };
    public static final String[] walkSound = { "sounds/footsteps_grass_single.mp3" };
    private static final String backgroundMusic = "sounds/bgm_dusk.mp3";
    private static final String[] gameMusic = { backgroundMusic };

    public AtlantisSinksGameArea(TerrainFactory terrainFactory, CareTaker playerStatus) {
        super();
        this.playerStatus = playerStatus;
        this.terrainFactory = terrainFactory;
    }

    @Override
    public void create() {
        loadAssets();
        ServiceLocator.getGameService().setUpEntities(120);
        spawnTerrain();
        crystal = spawnCrystal(60, 60);

        player = spawnPlayer(PLAYER_SPAWN);
        player.getEvents().addListener("attack", this::attack);
    }

    private Entity spawnPlayer(GridPoint2 playerLocation) {
        Entity newPlayer = PlayerFactory.loadPlayer(playerStatus);
        ServiceLocator.getGameService().registerEntity(playerLocation, "phil", newPlayer);
        return newPlayer;
    }

    private Entity spawnCrystal(int x_pos, int y_pos) {
        Entity crystal = CrystalFactory.createCrystal();
        while (ServiceLocator.getEntityService().wouldCollide(crystal, x_pos, y_pos)) {
            x_pos++;
        }
        ServiceLocator.getEntityService().addEntity(crystal);
        spawnEntityAt(crystal, new GridPoint2(x_pos, y_pos), true, true);
        return crystal;
    }

    private void spawnTerrain() {
        // Background terrain
        terrain = terrainFactory.createTerrain(TerrainFactory.TerrainType.FOREST_DEMO_ISO);
        Entity terrainEntity = new Entity().addComponent(terrain);

        areaEntities.add(terrainEntity);
        ServiceLocator.getEntityService().registerNamed("terrain", terrainEntity);

        // Terrain walls
        float tileSize = terrain.getTileSize();
        System.out.println(tileSize);
        GridPoint2 tileBounds = terrain.getMapBounds(0);
        Vector2 worldBounds = new Vector2(tileBounds.x * tileSize, tileBounds.y * tileSize);

//        spawnWorldBorders();
    }

    private void spawnWorldBorders() {
        GridPoint2 mapSize = terrainFactory.getMapSize();

        TiledMapTileLayer tiledMapTileLayer = terrain.getTileMapTileLayer(0);

        for (int x = 1; x < mapSize.x - 1; x++) {
            for (int y = 1; y < mapSize.y - 1; y++) {
                TerrainTile tile = (TerrainTile) tiledMapTileLayer.getCell(x, y).getTile();

                TerrainTile above = (TerrainTile) tiledMapTileLayer.getCell(x, y + 1).getTile();
                TerrainTile below = (TerrainTile) tiledMapTileLayer.getCell(x, y - 1).getTile();
                TerrainTile left = (TerrainTile) tiledMapTileLayer.getCell(x - 1, y).getTile();
                TerrainTile right = (TerrainTile) tiledMapTileLayer.getCell(x + 1, y).getTile();
                TerrainTile rightAbove = (TerrainTile) tiledMapTileLayer.getCell(x + 1, y + 1).getTile();
                TerrainTile rightBelow = (TerrainTile) tiledMapTileLayer.getCell(x + 1, y - 1).getTile();
                TerrainTile leftAbove = (TerrainTile) tiledMapTileLayer.getCell(x - 1, y + 1).getTile();
                TerrainTile leftBelow = (TerrainTile) tiledMapTileLayer.getCell(x - 1, y - 1).getTile();

                if (tile.getName().equals("grass")) {
                    if (above.getName().equals("water")) {
                        createBorderWall(x, y + 1);
                    }
                    if (below.getName().equals("cliff") || below.getName().equals("cliffLeft")) {
                        createBorderWall(x, y - 1);
                    }
                    if (left.getName().equals("water")) {
                        createBorderWall(x - 1, y);
                    }
                    if (right.getName().equals("cliff") || right.getName().equals("cliffRight")) {
                        createBorderWall(x + 1, y);
                    }
                    if (rightAbove.getName().equals("water") || rightAbove.getName().equals("cliffRight")
                            || rightAbove.getName().equals("cliff")) {
                        createBorderWall(x + 1, y + 1);
                    }
                    if (rightBelow.getName().equals("cliff")) {
                        createBorderWall(x + 1, y - 1);
                    }
                    if (leftAbove.getName().equals("water")) {
                        createBorderWall(x - 1, y + 1);
                    }
                    if (leftBelow.getName().equals("water") || leftBelow.getName().equals("cliff")
                            || leftBelow.getName().equals("cliffLeft")) {
                        createBorderWall(x - 1, y + 1);
                    }
                }
            }
        }
    }

    private void createBorderWall(int x, int y) {
        //Fix this to match Luke's stuff
        Entity wall = ObstacleFactory.createWall(1f, 0.5f);
        super.spawnEntityAt(wall, new GridPoint2(x, y), false, false);
    }

    private void loadAssets() {
        logger.debug("Loading assets");
        ResourceService resourceService = ServiceLocator.getResourceService();
        resourceService.loadTextures(gameTextures);
        resourceService.loadSounds(gameSounds);
        resourceService.loadSounds(walkSound);
        resourceService.loadMusic(gameMusic);

        while (!resourceService.loadForMillis(10)) {
            // This could be upgraded to a loading screen
            logger.info("Loading... {}%", resourceService.getProgress());
        }
    }

    private void playMusic() {
        Music music = ServiceLocator.getResourceService().getAsset(backgroundMusic, Music.class);
        music.setLooping(true);
        music.setVolume(0.3f);
        music.play();
    }

    private float _map(float x, float in_min, float in_max, float out_min, float out_max) {
        return (x - in_min) * (out_max - out_min) / (in_max - in_min) + out_min;
    }


    private GridPoint2 worldPosToTilePos(Vector2 coords) {
        Entity camera = ServiceLocator.getEntityService().getNamedEntity("camera");
        CameraComponent camComp = camera.getComponent(CameraComponent.class);
        Vector2 viewPortCoords = new Vector2(camComp.getCamera().viewportWidth, camComp.getCamera().viewportHeight);
        System.out.println(viewPortCoords);
        Vector3 mousePos = camComp.getCamera().unproject(new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0));
        float worldWidth = terrain.getTileMapTileLayer(0).getWidth();
        float worldHeight = terrain.getTileMapTileLayer(0).getHeight();
        float tileWidth = terrain.getTileMapTileLayer(0).getTileWidth();
        float tileHeight = terrain.getTileMapTileLayer(0).getTileHeight();
        GridPoint2 tilePos = new GridPoint2();
        return tilePos;
    }

    private void attack() {
        Entity player = ServiceLocator.getEntityService().getNamedEntity("phil");
        Vector2 playerWorldPos = player.getCenterPosition();
        System.out.println(playerWorldPos);
        GridPoint2 playerGridPos = worldPosToTilePos(playerWorldPos);
        System.out.println(playerGridPos);
        player.setPosition(terrain.tileToWorldPosition(new GridPoint2(50,50)));
    }

    private void unloadAssets() {
        logger.debug("Unloading assets");
        ResourceService resourceService = ServiceLocator.getResourceService();
        resourceService.unloadAssets(gameTextures);
        resourceService.unloadAssets(gameSounds);
        resourceService.unloadAssets(walkSound);
        resourceService.unloadAssets(gameMusic);
    }

    @Override
    public void dispose() {
        super.dispose();
        ServiceLocator.getResourceService().getAsset(backgroundMusic, Music.class).stop();
        this.unloadAssets();
    }
}
