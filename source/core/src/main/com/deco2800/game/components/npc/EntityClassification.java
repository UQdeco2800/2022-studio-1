package com.deco2800.game.components.npc;

import com.deco2800.game.components.Component;
import com.deco2800.game.entities.Entity;

public class EntityClassification extends Component {

    public enum NPCClassification {
        NONE,
        BOSS,
        ENEMY,
        PLAYER;
    }

    private NPCClassification entityType;

    public EntityClassification(NPCClassification type) {
        this.entityType = type;
    }

    public void setEntityType(NPCClassification type) {
        this.entityType = type;
    }

    public NPCClassification getEntityType() {
        return this.entityType;
    }
}
