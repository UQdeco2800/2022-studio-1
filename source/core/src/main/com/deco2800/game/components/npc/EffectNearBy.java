package com.deco2800.game.components.npc;

import com.badlogic.gdx.math.Vector2;
import com.deco2800.game.components.CombatStatsComponent;
import com.deco2800.game.components.Component;
import com.deco2800.game.entities.Entity;
import com.deco2800.game.physics.components.PhysicsMovementComponent;
import com.deco2800.game.services.ServiceLocator;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Component that applies affects to enemies tagged with the ENEMY enum type
 * Some affects
 */
public class EffectNearBy extends Component {
    private Boolean affectEnemy;
    private Boolean affectPlayer;
    private Boolean affectStructure;
    public static ArrayList<Entity> destroyed = new ArrayList<Entity>();
    public static ArrayList<Entity> underSpeedAffect = new ArrayList<Entity>();
    public static ArrayList<Entity> underAttackBuff = new ArrayList<Entity>();

    private int framesPassed;
    private Boolean speedEnable;
    private Boolean regenEnable;
    private Boolean attackBuff;
    private final Float RANGE = 20f;


    public EffectNearBy(Boolean affectE, Boolean affectP, Boolean affectS) {
        this.affectEnemy = affectE;
        this.affectPlayer = affectP;
        this.affectStructure = affectS;
        this.framesPassed = 0;
        this.speedEnable = false;
        this.regenEnable = false;
        this.attackBuff = false;
    }

    private Boolean withinRange(Entity attachedTo, Entity target, float range) {
        Vector2 targetPos = target.getCenterPosition();
        Vector2 currentPos = attachedTo.getCenterPosition();
        return Math.sqrt(Math.pow((targetPos.x - currentPos.x), 2) + Math.pow((targetPos.y - currentPos.y), 2)) <= range;
    }

    @Override
    public void update() {
        Collection<Entity> entities = ServiceLocator.getEntityService().getEntities();
        Entity attachedTo = this.getEntity();

        for (Entity entity: entities) {
            if (entity.equals(attachedTo) || attachedTo == null || destroyed.contains(entity) || this.framesPassed < 60) {
                this.framesPassed++;
                continue;
            }

            applySpeed(entity);
            applyRegen(entity);

            this.framesPassed = 0;
        }
    }

    private void applySpeed(Entity entity) {
        Entity attachedTo = this.getEntity();
        if (this.speedEnable) {
            EntityClassification t =  entity.getComponent(EntityClassification.class);
            if (t == null) {
                return;
            }
            EntityClassification.NPCClassification type = t.getEntityType();

            if (this.canBeApplied(type)) {
                if (withinRange(attachedTo, entity, RANGE)) {
                    if (entity.getComponent(PhysicsMovementComponent.class) == null) {
                        return;
                    }
                    Vector2 speed = entity.getComponent(PhysicsMovementComponent.class).getSpeed();
                    entity.getComponent(PhysicsMovementComponent.class).setNewSpeed(new Vector2(speed.x * 0f, speed.y * 0f));

                    if (!underSpeedAffect.contains(entity)) {
                        underSpeedAffect.add(entity);
                    }

                } else {
                    entity.getComponent(PhysicsMovementComponent.class).resetSpeed();
                    underSpeedAffect.remove(entity);
                }
            }

        }
    }

    private void applyAttackBuff(Entity entity) {
        Entity attachedTo = this.getEntity();
        if (this.attackBuff) {
            EntityClassification t =  entity.getComponent(EntityClassification.class);
            if (t == null) {
                return;
            }
            EntityClassification.NPCClassification type = t.getEntityType();

            if (this.canBeApplied(type)) {
                if (withinRange(attachedTo, entity, RANGE)) {
                    if (entity.getComponent(CombatStatsComponent.class) == null) {
                        return;
                    }

                    int currentAttack = entity.getComponent(CombatStatsComponent.class).getBaseAttack();
                    entity.getComponent(CombatStatsComponent.class).addAttack(1);

                    if (underAttackBuff.contains(entity)) {
                        underAttackBuff.add(entity);
                    }
                } else {
                    entity.getComponent(CombatStatsComponent.class).revertAttack();
                    underAttackBuff.remove(entity);
                }
            }

        }
    }

    private void applyRegen(Entity entity) {
        Entity attachedTo = this.getEntity();
        if (this.regenEnable) {
            EntityClassification t =  entity.getComponent(EntityClassification.class);
            if (t == null) {
                return;
            }
            EntityClassification.NPCClassification type = t.getEntityType();

            if (this.canBeApplied(type)) {
                if (withinRange(attachedTo, entity, RANGE)) {
                    if (entity.getComponent(CombatStatsComponent.class) == null) {
                        return;
                    }
                    int currentHealth = entity.getComponent(CombatStatsComponent.class).getHealth();
                    entity.getComponent(CombatStatsComponent.class).setHealth(currentHealth + 1);
                }
            }
        }
    }

    public Boolean canBeApplied(EntityClassification.NPCClassification type) {
        EntityClassification.NPCClassification enemy = EntityClassification.NPCClassification.ENEMY;
        EntityClassification.NPCClassification player = EntityClassification.NPCClassification.PLAYER;
        if (type == enemy && affectEnemy) {
            return true;
        }

        if (type == player && affectPlayer) {
            return true;
        }

        return false;
    }

    public void enableSpeed() {
        this.speedEnable = true;
    }

    public void disableSpeed() {
        this.speedEnable = false;
    }

    public void enableRegen() {
        this.regenEnable = true;
    }

    public void disableRegen() {
        this.regenEnable = false;
    }

    public void enableAttackDamageBuff() {
        this.attackBuff = true;
    }

    public void disableAttackDamageBuff() {
        this.attackBuff = false;
    }
}


