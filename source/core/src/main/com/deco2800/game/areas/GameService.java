package com.deco2800.game.areas;

import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.deco2800.game.entities.Entity;
import com.deco2800.game.entities.EntityService;
import com.deco2800.game.services.ServiceLocator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

/**
 * Provides a global access point for entities, Ui components and initialised map to register themselves.
 * This allows for iterating over these hashmaps to perform updates each loop. Everything in the game should be
 * registered here.
 */
public class GameService {

    private static final Logger logger = LoggerFactory.getLogger(GameService.class);
    private static final int INITIAL_CAPACITY = 40;

    private Hashtable<GridPoint2, String> uiMap = new Hashtable<>();
    private Hashtable<GridPoint2, String> entityMap = new Hashtable<>();
    private Hashtable<GridPoint2, String> mapMap = new Hashtable<>();

    /**
     * Register a new ui component with the ui map. The ui component will be created and start updating.
     * @param location the gridpoint on where its initialised
     * @param entity the entity you want to initialise
     * @param name the name of the uq component
     */
    public void registerUi(GridPoint2 location, String name, Entity entity) {
        logger.debug("Registering {} @ {} in ui service", name, location);
        uiMap.put(location, name);
        entity.create();
    }

    /**
     * Register a new entity component with the entity map. The entity component will be created and start updating.
     * @param location the gridpoint on where its initialised
     * @param entity the entity you want to initialise
     * @param name the name of the uq component
     */
    public void registerEntity(GridPoint2 location, String name, Entity entity) {
        logger.debug("Registering {} @ {} in ui service", name, location);
        entityMap.put(location, name);
        ServiceLocator.getEntityService().registerNamed(name, entity);
    }

    /**
     * Register a new entity component with the entity map. The entity component will be created and start updating.
     * @param location the gridpoint on where its initialised
     * @param entity the entity you want to initialise
     * @param name the name of the uq component
     */
    public void registerMap(GridPoint2 location, String name, Entity entity) {
        logger.debug("Registering {} @ {} in ui service", name, location);
        mapMap.put(location, name);
        ServiceLocator.getEntityService().registerNamed(name, entity);
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





}
