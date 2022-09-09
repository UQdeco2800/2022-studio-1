package com.deco2800.game.components;

import com.badlogic.gdx.math.Vector2;
import com.deco2800.game.components.player.InventoryComponent;
import com.deco2800.game.components.player.PlayerActions;
import com.deco2800.game.components.player.PlayerStatsDisplay;
import com.deco2800.game.entities.Entity;
import com.deco2800.game.physics.components.PhysicsMovementComponent;
import com.deco2800.game.services.ServiceLocator;

import java.util.ArrayList;
import java.util.Collection;


public class EffectNearBy extends Component{
    private Boolean affectEnemy;
    private Boolean affectPlayer;
    private Boolean affectStructure;
    public static ArrayList<Entity> destroyed = new ArrayList<Entity>();
    public static ArrayList<Entity> underSpeedAffect = new ArrayList<Entity>();
    private int framesPassed;


    public EffectNearBy(Boolean affectE, Boolean affectP, Boolean affectS) {
        this.affectEnemy = affectE;
        this.affectPlayer = affectP;
        this.affectStructure = affectS;
        this.framesPassed = 0;
    }

    private Boolean withinRange(Entity attachedTo, Entity target, float range) {
        Vector2 targetPos = target.getCenterPosition();
        Vector2 currentPos = attachedTo.getCenterPosition();
        return Math.sqrt(Math.pow((targetPos.x - currentPos.x), 2) + Math.pow((targetPos.y - currentPos.y), 2)) <= range;
    }

    @Override
    public void update() {
        Collection<Entity> entities = ServiceLocator.getEntityService().getEntities();
        float range = 50;
        Entity attachedTo = this.getEntity();

        for (Entity entity: entities) {
            if (entity.equals(attachedTo) || attachedTo == null || destroyed.contains(entity) || this.framesPassed < 60) {
                this.framesPassed++;
                continue;
            }

            if (this.affectEnemy && entity.getComponent(HealthBarComponent.class) != null &&  entity.getComponent(PhysicsMovementComponent.class) != null) {
                if (withinRange(attachedTo, entity, range) && !underSpeedAffect.contains(entity)) {
                    Vector2 speed = entity.getComponent(PhysicsMovementComponent.class).getSpeed();
                    entity.getComponent(PhysicsMovementComponent.class).setNewSpeed(new Vector2(speed.x * 2, speed.y * 2));
                    //destroyed.add(entity);
                } else {
                    entity.getComponent(PhysicsMovementComponent.class).resetSpeed();
                }
            }
            this.framesPassed = 0;
        }



    }
}
