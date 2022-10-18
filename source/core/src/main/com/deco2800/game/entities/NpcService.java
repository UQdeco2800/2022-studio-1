package com.deco2800.game.entities;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Array;
import com.deco2800.game.components.CameraComponent;
import com.deco2800.game.components.maingame.MainGameNpcInterface;
import com.deco2800.game.services.ServiceLocator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Provides a global access point for NPC entities to register themselves. This allows for iterating
 * over NPC entities to perform updates each loop. All NPC entities should be registered here.
 *
 */
public class NpcService extends EntityService {
    private static final Logger logger = LoggerFactory.getLogger(StructureService.class);
    private static final int INITIAL_CAPACITY = 40;

    private final Array<Entity> npcEntities = new Array<>(false, INITIAL_CAPACITY);

    private final Map<String, Entity> namedNpc = new HashMap<String, Entity>();
    private int npcNum = 0;
    private static Table Conversation;
    private static boolean isVisible;
    private int npcType;

    public void setNpcNum(int num) {
        if (num < 0){
            //logger.error("number of NPCs cannot be negative");
        }else {
            this.npcNum = num;
        }
    }

    public int getNpcNum() {
        return npcNum;
    }

    public void setNpcType(int num) {
        this.npcType = num;
    }

    public int getNpcType() {
        return npcType;
    }


    /**
     * Register a new entity with the NPC service. The entity will be created and start updating.
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
     * Unregister an entity with the NPC service. The entity will be removed and stop updating.
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

    /**
     * Determine if NPCs on map are being clicked on
     * @param screenX x coordinate
     * @param screenY y coordinate
     */
    public static boolean npcClicked(int screenX, int screenY) {
        Entity camera = ServiceLocator.getEntityService().getNamedEntity("camera");
        CameraComponent camComp = camera.getComponent(CameraComponent.class);
        Vector3 mousePos = camComp.getCamera().unproject(new Vector3(screenX, screenY, 0));
        Vector2 mousePosV2 = new Vector2(mousePos.x, mousePos.y);
        mousePosV2.x -= 0.5;
        mousePosV2.y -= 0.5;
//        System.out.println("mouse x:" +mousePosV2.x);
//        System.out.println("mouse y:" +mousePosV2.y);

        if (isVisible) {
            Conversation.remove();
            isVisible = false;
        }

        for (int i = 0; i < ServiceLocator.getNpcService().getNpcNum(); i++) {
            Entity NPC = ServiceLocator.getNpcService().getNamedEntity(String.valueOf(i));
            float xPos = NPC.getPosition().x;
            float yPos = NPC.getPosition().y;

//            System.out.println(xPos);
//            System.out.println(yPos);
            if (xPos+2.6 < mousePosV2.x && mousePosV2.x < xPos+4.6) {
                if (yPos+1.3 < mousePosV2.y && mousePosV2.y < yPos+5.6) {
//            if (xPos+1 < mousePosV2.x && mousePosV2.x < xPos+3) {
//                if (yPos+0.5 < mousePosV2.y && mousePosV2.y < yPos+4) {

                    //System.out.println("npc clicked");
                    //initiate conversationS
                    if(Objects.equals(NPC.getName(), "SpecialNPC")) {
                        Conversation = ServiceLocator.getEntityService().getNamedEntity("ui").getComponent(MainGameNpcInterface.class).makeUIPopUp(true);
                        isVisible = true;
                    }
                    return true;
                }
            }

        }

        return false;
    }



}