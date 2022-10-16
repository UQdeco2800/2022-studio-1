package com.deco2800.game.areas;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.deco2800.game.areas.terrain.TerrainComponent;
import com.deco2800.game.areas.terrain.TerrainFactory;
import com.deco2800.game.areas.terrain.TerrainTile;
import com.deco2800.game.components.CameraComponent;
import com.deco2800.game.components.settingsmenu.SettingsMenuDisplay;
import com.deco2800.game.entities.Entity;
import com.deco2800.game.entities.factories.CrystalFactory;
import com.deco2800.game.entities.factories.ObstacleFactory;
import com.deco2800.game.entities.factories.PlayerFactory;
import com.deco2800.game.services.ResourceService;
import com.deco2800.game.services.ServiceLocator;
import com.deco2800.game.utils.math.RandomUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serial;
import java.util.Map;

public class GuidebookArea extends GameArea {

    private static final Logger logger = LoggerFactory.getLogger(GuidebookArea.class);

    private static final String shopMusic = "sounds/shopping_backgroundmusic-V1.mp3";
    private static final String[] gameMusic = { shopMusic };

    public GuidebookArea() {
        super();
    }

    @Override
    public void create() {
        loadAssets();
        playMusic();
    }

    private void loadAssets() {
        logger.debug("Loading assets");
        ResourceService resourceService = ServiceLocator.getResourceService();
        resourceService.loadMusic(gameMusic);

        while (!resourceService.loadForMillis(10)) {
            // This could be upgraded to a loading screen
            logger.info("Loading guidebook assets... {}%", resourceService.getProgress());

        }
    }

    private void playMusic() {
        Music music = ServiceLocator.getResourceService().getAsset(shopMusic, Music.class);
        music.setLooping(true);
        music.setVolume(0.3f);
        music.play();
    }

    private void unloadAssets() {
        logger.debug("Unloading assets");
        ResourceService resourceService = ServiceLocator.getResourceService();
        resourceService.unloadAssets(gameMusic);
    }

    @Override
    public void dispose() {
        super.dispose();
        ServiceLocator.getResourceService().getAsset(shopMusic, Music.class).stop();
        this.unloadAssets();
    }
}
