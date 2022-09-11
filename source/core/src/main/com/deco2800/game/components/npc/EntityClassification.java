package com.deco2800.game.components.npc;

import com.deco2800.game.components.Component;

/**
 * Classification System for NPC to be used by EffectsNearBY
 * and other releated checking of entity types
 */
public class EntityClassification extends Component {

    /**
     * Types of entities
     */
    public enum NPCClassification {
        NONE,
        BOSS,
        ENEMY,
        PLAYER;
    }

    private NPCClassification entityType;

    /**
     * Constructor method for Entity Classification
     * @param type the type of entity
     */
    public EntityClassification(NPCClassification type) {
        this.entityType = type;
    }

    /**
     * Set entity type, useful for reassigning ENEMY to BOSS
     * @param type NPCClassifcation to set
     */
    public void setEntityType(NPCClassification type) {
        this.entityType = type;
    }

    /**
     * @return Returns the entity type
     */
    public NPCClassification getEntityType() {
        return this.entityType;
    }
}
