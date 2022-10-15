package com.deco2800.game.components;

import java.util.List;

import com.deco2800.game.entities.Entity;
import com.deco2800.game.physics.components.HitboxComponent;

public class AOEDamageComponent extends Component {

        private short targetLayer;
        private int rangeRadius;
        private int numTargets;
        private Entity[] targets;

        private CombatStatsComponent combatStats;
        private HitboxComponent hitbox;

        public AOEDamageComponent(short targetLayer, int numTargets, int radiusRange) {
                this.targetLayer = targetLayer;
                this.numTargets = numTargets;
                this.rangeRadius = radiusRange;

                targets = new Entity[numTargets];
        }

        @Override
        public void create() {
                combatStats = entity.getComponent(CombatStatsComponent.class);
                hitbox = entity.getComponent(HitboxComponent.class);
        }

        @Override
        public void update() {
                updateTargets();
                damageTargets();
        }

        private void updateTargets() {

        }

        private void damageTargets() {

        }
}
