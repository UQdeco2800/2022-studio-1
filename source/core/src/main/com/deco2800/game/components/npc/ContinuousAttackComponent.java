package com.deco2800.game.components.npc;

import com.badlogic.gdx.physics.box2d.Fixture;
import com.deco2800.game.ai.tasks.AITaskComponent;
import com.deco2800.game.components.CombatStatsComponent;
import com.deco2800.game.components.Component;
import com.deco2800.game.entities.Entity;
import com.deco2800.game.physics.BodyUserData;
import com.deco2800.game.physics.components.HitboxComponent;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class ContinuousAttackComponent extends Component {
    private ArrayList<Entity> colliders;
    private CombatStatsComponent combatStats;
    private HitboxComponent hitboxComponent;
    private Timer timer;

    @Override
    public void create() {
        colliders = new ArrayList<>();
        entity.getEvents().addListener("collisionStart", this::onCollisionStart);
        entity.getEvents().addListener("collisionEnd", this::onCollisionEnd);
        combatStats = entity.getComponent(CombatStatsComponent.class);
        hitboxComponent = entity.getComponent(HitboxComponent.class);
        timer = new Timer();
        TimerTask attackColliders = new TimerTask() {
            @Override
            public void run() {
                for (Entity e : colliders) {
//                    combatStats.hit(e.getComponent(CombatStatsComponent.class));
                    int attack = combatStats.getBaseAttack();
                    int health = e.getComponent(CombatStatsComponent.class).getHealth();
                    e.getComponent(CombatStatsComponent.class).setHealth(health - attack);
                }
            }
        };
        timer.scheduleAtFixedRate(attackColliders, 20000, 3000);
    }

    private void onCollisionStart(Fixture me, Fixture other) {
        if (hitboxComponent.getFixture() != me) {
            // Not triggered by hitbox, ignore
            return;
        }
        Entity target = ((BodyUserData) other.getBody().getUserData()).entity;
        CombatStatsComponent targetStats = target.getComponent(CombatStatsComponent.class);
        EntityClassification classifier = target.getComponent(EntityClassification.class);
        if (targetStats != null) {
            EntityClassification.NPCClassification type = EntityClassification.NPCClassification.NONE;
            if (classifier != null) {
                 type = classifier.getEntityType();
            }
            switch (type) {
                case NPC:
                case BOSS:
                case ENEMY:
                    return;
                default:
                    colliders.add(target);
            }
        }
    }

    private void onCollisionEnd(Fixture me, Fixture other) {
        if (hitboxComponent.getFixture() != me) {
            // Not triggered by hitbox, ignore
            return;
        }
        Entity target = ((BodyUserData) other.getBody().getUserData()).entity;
        if (colliders.contains(target)) {
            colliders.remove(target);
        }
    }
}