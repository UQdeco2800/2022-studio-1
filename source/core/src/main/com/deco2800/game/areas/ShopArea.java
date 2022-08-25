package com.deco2800.game.areas;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.deco2800.game.areas.terrain.TerrainFactory;
import com.deco2800.game.components.gamearea.GameAreaDisplay;
import com.deco2800.game.entities.Entity;
import com.deco2800.game.entities.factories.NPCFactory;
import com.deco2800.game.entities.factories.ObstacleFactory;
import com.deco2800.game.entities.factories.PlayerFactory;
import com.deco2800.game.services.ResourceService;
import com.deco2800.game.services.ServiceLocator;
import com.deco2800.game.utils.math.GridPoint2Utils;
import com.deco2800.game.utils.math.RandomUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ShopArea extends GameArea {
    private static final Logger logger = LoggerFactory.getLogger(ShopArea.class);

    private static final String[] shopTextures = {
            "images/shop-interface.png",
    };
    private final TerrainFactory terrainFactory;

    private Texture texture;

    public ShopArea(TerrainFactory terrainFactory) {
        super();
        this.terrainFactory = terrainFactory;
    }

    /** Create the game area, including terrain, static entities (trees), dynamic entities (player) */
    @Override
    public void create() {

        spawnTerrain();

    }

    private void spawnTerrain() {
        texture = new Texture(Gdx.files.internal("images/shop-interface.png"));
    }


}
