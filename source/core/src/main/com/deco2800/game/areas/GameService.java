package com.deco2800.game.areas;

import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.SerializationException;
import com.deco2800.game.areas.terrain.TerrainComponent;
import com.deco2800.game.entities.Entity;
import com.deco2800.game.entities.EntityService;
import com.deco2800.game.files.FileLoader;
import com.deco2800.game.services.ServiceLocator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ssl.SSLEngineResult;
import java.util.*;

/**
 * Provides a global access point for entities, Ui components and initialised map to register themselves.
 * This allows for iterating over these hashmaps to perform updates each loop. Everything in the game should be
 * registered here.
 */

// hasmap <locaton, hashmap<name, tile type>>
public class GameService {

    private static final Logger logger = LoggerFactory.getLogger(GameService.class);
    private static final int INITIAL_CAPACITY = 40;

    private HashMap<GridPoint2, String> uiMap = new HashMap<>();
    private HashMap<GridPoint2, HashMap<String, String>> entityMap = new HashMap<>();

    public void setUpEntities (int mapSize) {
        for (int i = 0; i < mapSize; i++) {
            for (int j = 0; j < mapSize; j++) {
                entityMap.put(new GridPoint2(i, j), new HashMap<>() {{put("name", null);}});
                entityMap.put(new GridPoint2(i, j), new HashMap<>() {{put("tiletype", null);}});
                entityMap.put(new GridPoint2(i, j), new HashMap<>() {{put("health", null);}});
            }
        }
    }

    /**
     * Register a new entity component with the entity map. The entity component will be created and start updating.
     * @param location the gridpoint on where its initialised
     * @param entity the entity you want to initialise
     * @param name the name of the uq component
     */
    public void registerEntity(GridPoint2 location, String name, Entity entity) {
        logger.debug("Registering {} @ {} in ui service", name, location);
        entityMap.get(location).replace("name", null, name);
        ServiceLocator.getEntityService().registerNamed(name, entity);
        ServiceLocator.getEntityService().getLastEntity().setPosition(ServiceLocator.getEntityService().getNamedEntity("terrain").getComponent(TerrainComponent.class).tileToWorldPosition(location));
    }

    /**
     * Register a new ui component with the ui map. The ui component will be created and start updating.
     * @param location the gridpoint on where its initialised
     * @param entity the entity you want to initialise
     * @param name the name of the uq component
     */
    public void registerUi(GridPoint2 location, String name, Entity entity) {
        logger.debug("Registering {} @ {} in ui service", name, location);
        uiMap.put(location, new HashMap<String, String> mapFeatures );
        entity.create();
    }

    /**
     * Unregister an entity with the entity service. The entity will be removed and stop updating.
     * @param entity entity to be removed.
     */
    public void unregisterMap(GridPoint2 location, String name, Entity entity) {
        logger.debug("Unregistering {} in entity service", name);
        mapMap.remove(location, name);
        ServiceLocator.getEntityService().removeNamedEntity(name, entity);
    }


    /**
     * Unregister an entity with the entity service using its name. The entity will be removed and stop updating.
     * @param entity entity to be removed.
     */
    public void removeNamedEntity (GridPoint2 location, String name, Entity entity) {
        logger.debug("Unregistering {} in entity service", entity);
        if (uiMap.containsValue(name)) {
            uiMap.remove(location);
        } else if (entityMap.get(location).containsValue(name)) {
            entityMap.get(location).remove(name);
        }
        ServiceLocator.getEntityService().removeNamedEntity(name, entity);
    }

    /**
     * Dispose all entities.
     */
    public void dispose() {
        uiMap.clear();
        mapMap.clear();
        entityMap.clear();
        ServiceLocator.getEntityService().dispose();
    }







}
