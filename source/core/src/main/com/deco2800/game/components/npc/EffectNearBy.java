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
 * Some affects include regen on enemies, speed buffs and attack/damage buffs
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
    private final Float RANGE = 7f;

    /**
     * Constructor method for effects nearby component. Tells the buff to what to affect
     * @param affectE affect enemies?
     * @param affectP affect players?
     * @param affectS affect structures?
     */
    public EffectNearBy(Boolean affectE, Boolean affectP, Boolean affectS) {
        this.affectEnemy = affectE;
        this.affectPlayer = affectP;
        this.affectStructure = affectS;
        this.framesPassed = 0;
        this.speedEnable = false;
        this.regenEnable = false;
        this.attackBuff = false;
    }

    /**
     * Calculate whether or not the entity is within range of the attached entity
     * @param attachedTo current entity EffectNearBy is attached to
     * @param target current target entity
     * @param range range in world pos in float
     * @return true/false if the entity is within range
     */
    private Boolean withinRange(Entity attachedTo, Entity target, float range) {
        Vector2 targetPos = target.getCenterPosition();
        Vector2 currentPos = attachedTo.getCenterPosition();
        return Math.sqrt(Math.pow((targetPos.x - currentPos.x), 2) + Math.pow((targetPos.y - currentPos.y), 2)) <= range;
    }

    /**
     * Every 60 frames/updates run affects such as apply speed, apply regen, apply attack buff
     * etc
     */
    @Override
    public void update() {
        Collection<Entity> entities = ServiceLocator.getEntityService().getEnemyEntities();

        Entity attachedTo = this.getEntity();

        for (Entity ent: entities) {

            if (ent.equals(attachedTo) || attachedTo == null || destroyed.contains(ent) || this.framesPassed < 60) {
                this.framesPassed++;
                continue;
            }

            applySpeed(ent);
            applyRegen(ent);
            applyAttackBuff(ent);

            this.framesPassed = 0;
        }
    }

    /**
     * Apply speed buff to targeted entity
     * @param entity the entity to recieve the speed buff
     */
    private void applySpeed(Entity entity) {
        Entity attachedTo = this.getEntity();

        if (this.speedEnable) {

            //error check component incase of invalid setup
            EntityClassification t =  entity.getComponent(EntityClassification.class);
            if (t == null) {
                return;
            }
            EntityClassification.NPCClassification type = t.getEntityType();

            //check if it can apply the type and whether it is within range
            if (this.canBeApplied(type)) {
                if (withinRange(attachedTo, entity, RANGE)) {

                    if (entity.getComponent(PhysicsMovementComponent.class) == null) {
                        return;
                    }
                    Vector2 speed = entity.getComponent(PhysicsMovementComponent.class).getSpeed();
                    entity.getComponent(PhysicsMovementComponent.class).setNewSpeed(new Vector2(speed.x * 1.5f, speed.y * 1.5f));

                    if (!underSpeedAffect.contains(entity)) {
                        underSpeedAffect.add(entity);
                    }

                } else {
                    //remove speed buff if needed
                    entity.getComponent(PhysicsMovementComponent.class).resetSpeed();
                    underSpeedAffect.remove(entity);
                }
            }
        }
    }

    /**
     * apply attack buff of 1 to target entity
     * @param entity entity to target
     */
    private void applyAttackBuff(Entity entity) {
        Entity attachedTo = this.getEntity();

        //check to see if it recieves buff
        if (this.attackBuff) {

            //error checking to see if it has a classification
            EntityClassification t =  entity.getComponent(EntityClassification.class);
            if (t == null) {
                return;
            }
            EntityClassification.NPCClassification type = t.getEntityType();

            //apply regen if enabled and within range
            if (this.canBeApplied(type)) {
                if (withinRange(attachedTo, entity, RANGE)) {
                    if (entity.getComponent(CombatStatsComponent.class) == null) {
                        return;
                    }

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

    /**
     * Entity to apply regen affect to
     * @param entity entity to raget
     */
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

    /**
     * checks to see if the given type can be affected by buffs
     * @param type the type of entity
     * @return true/false if it can be affected by the buff
     */
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

    /**
     * enable speed buff
     */
    public void enableSpeed() {
        this.speedEnable = true;
    }

    /**
     * disable speed buff
     */
    public void disableSpeed() {
        this.speedEnable = false;
    }

    /**
     * enable regen buff
     */
    public void enableRegen() {
        this.regenEnable = true;
    }

    /**
     * disable regen buff
     */
    public void disableRegen() {
        this.regenEnable = false;
    }

    /**
     * enable attack damage buff
     */
    public void enableAttackDamageBuff() {
        this.attackBuff = true;
    }

    /**
     * disable attack damage buff
     */
    public void disableAttackDamageBuff() {
        this.attackBuff = false;
    }
}


