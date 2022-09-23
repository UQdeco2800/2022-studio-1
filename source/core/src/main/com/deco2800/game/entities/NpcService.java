package com.deco2800.game.entities;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.deco2800.game.components.CameraComponent;
import com.deco2800.game.services.ServiceLocator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import java.util.HashMap;
import java.util.Map;

/**
 * Provides a global access point for entities to register themselves. This allows for iterating
 * over entities to perform updates each loop. All game entities should be registered here.
 *
 * Avoid adding additional state here! Global access is often the easy but incorrect answer to
 * sharing data.
 */
public class NpcService extends EntityService {
    private static final Logger logger = LoggerFactory.getLogger(StructureService.class);
    private static final int INITIAL_CAPACITY = 40;

    private final Array<Entity> npcEntities = new Array<>(false, INITIAL_CAPACITY);

    private final Map<String, Entity> namedNpc = new HashMap<String, Entity>();
    private int npcNum = 0;



    public void setNpcNum(int num) {
        this.npcNum = num;
    }

    public int getNpcNum() {
        return npcNum;
    }




    /**
     * Register a new entity with the entity service. The entity will be created and start updating.
     * @param entity new entity.
     */
    @Override
    public void register(Entity entity) {
        logger.debug("Registering {} in entity service", entity);
        npcEntities.add(entity);
    }

    /**
     * Registers an entity with a name so it can be found later
     *
     * @param name the name to register it as (must be unique or will overwrite)
     * @param entity the entity to register
     */
    @Override
    public void registerNamed(String name, Entity entity) {
        this.namedNpc.put(name, entity);
        this.register(entity);
    }

    /**
     * Returns a registered named entity
     * @param name the name the entity was registered as
     * @return the registered entity or null
     */
    @Override
    public Entity getNamedEntity(String name) {
        return this.namedNpc.get(name);
    }

    /**
     * Returns the last registered entity
     * @return the last registered entity or null
     */
    @Override
    public Entity getLastEntity() {
        return this.npcEntities.get(this.npcEntities.size-1);
    }

    /**
     * Returns all registered entities
     * @return all registered entities or null
     */
    @Override
    public Map<String, Entity> getAllNamedEntities() {
        return this.namedNpc;
    }


    /**
     * Unregister an entity with the entity service. The entity will be removed and stop updating.
     * @param entity entity to be removed.
     */
    @Override
    public void unregister(Entity entity) {
        logger.debug("Unregistering {} in entity service", entity);
        npcEntities.removeValue(entity, true);
    }

    /**
     * Update all registered entities. Should only be called from the main game loop.
     */
    @Override
    public void update() {
        for (Entity entity : npcEntities) {
            entity.earlyUpdate();
            entity.update();
        }
    }

    /**
     * Dispose all entities.
     */
    @Override
    public void dispose() {
        for (Entity entity : npcEntities) {
            entity.dispose();
        }
    }

    public static boolean npcClicked(int screenX, int screenY) {
        Entity camera = ServiceLocator.getEntityService().getNamedEntity("camera");
        CameraComponent camComp = camera.getComponent(CameraComponent.class);
        Vector3 mousePos = camComp.getCamera().unproject(new Vector3(screenX, screenY, 0));
        Vector2 mousePosV2 = new Vector2(mousePos.x, mousePos.y);
        mousePosV2.x -= 0.5;
        mousePosV2.y -= 0.5;

        for (int i = 0; i < ServiceLocator.getNpcService().getNpcNum(); i++) {
            Entity NPC = ServiceLocator.getNpcService().getNamedEntity(String.valueOf(i));
            float xPos = NPC.getPosition().x;
            float yPos = NPC.getPosition().y;

            if (xPos-0.2 < mousePosV2.x && mousePosV2.x < xPos+0.2) {
                if (yPos-0.2 < mousePosV2.y && mousePosV2.y < yPos+0.2) {
                    System.out.println("npc clicked");
                    //initiate conversation
                    return true;
                }
            }
        }

        return false;
    }



}