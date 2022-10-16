package com.deco2800.game.components;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.Vector2;
import com.deco2800.game.areas.terrain.TerrainComponent;
import com.deco2800.game.entities.Entity;
import com.deco2800.game.entities.UGS;
import com.deco2800.game.physics.components.HitboxComponent;
import com.deco2800.game.services.GameTime;
import com.deco2800.game.services.ServiceLocator;

public class AOEDamageComponent extends Component {

        private short targetLayer;
        private int rangeRadius;
        private int numTargets;
        private Entity[] targets;

        private CombatStatsComponent combatStats;
        private HitboxComponent hitbox;

        private GameTime gameTime;
        private int updateRate = 5000; // time inbetween each update
        private long lastUpdate = 0;

        public AOEDamageComponent(int numTargets, int radiusRange, int attackRate) {
                this.targetLayer = targetLayer;
                this.numTargets = numTargets;
                this.rangeRadius = radiusRange;
                this.updateRate = attackRate;

                targets = new Entity[numTargets];

                this.gameTime = ServiceLocator.getTimeSource();
        }

        @Override
        public void create() {
                combatStats = entity.getComponent(CombatStatsComponent.class);
                hitbox = entity.getComponent(HitboxComponent.class);
        }

        @Override
        public void update() {

                if (gameTime.getTime() > lastUpdate + updateRate) {
                        updateTargets();
                        damageTargets();
                        lastUpdate = gameTime.getTime();
                }
        }

        private void updateTargets() {

                targets = new Entity[numTargets];

                UGS ugs = ServiceLocator.getUGSService();
                Vector2 ownerWorldPosition = getEntity().getCenterPosition();

                GridPoint2 ownerTilePositon = ServiceLocator.getEntityService().getNamedEntity("terrain")
                                .getComponent(TerrainComponent.class)
                                .worldToTilePosition(ownerWorldPosition.x, ownerWorldPosition.y);

                ArrayList<GridPoint2> tilesInRadius = generateCircle(ownerTilePositon);

                for (int i = 0; i < tilesInRadius.size(); i++) {
                        Entity target = ugs.getEntity(ownerTilePositon);

                        if (target == null)
                                continue;

                        if (target.getName().contains("Mr")) {
                                boolean slotFilled = false;
                                for (int j = 0; j < numTargets; j++) {
                                        if (targets[j] == null) {
                                                targets[j] = target;
                                                slotFilled = true;
                                                break;
                                        }
                                }

                                if (slotFilled)
                                        break;

                                for (int j = 0; j < numTargets; j++) {
                                        if (getDistanceToTarget(target) < getDistanceToTarget(targets[j])) {
                                                targets[j] = target;
                                        }
                                }
                        }
                }

        }

        private ArrayList<GridPoint2> generateCircle(GridPoint2 c) {

                ArrayList<GridPoint2> circlePoints = new ArrayList<>();

                HashMap<Integer, ArrayList<Integer>> outerPoints = new HashMap<>();

                int d = 3 - (2 * rangeRadius);
                int x = 0;
                int y = rangeRadius;

                do {
                        addValue(outerPoints, c.x + x, c.y + y);
                        addValue(outerPoints, c.x + x, c.y - y);
                        addValue(outerPoints, c.x - x, c.y + y);
                        addValue(outerPoints, c.x - x, c.y - y);
                        addValue(outerPoints, c.x + y, c.x + x);
                        addValue(outerPoints, c.x + y, c.x - x);
                        addValue(outerPoints, c.x - y, c.x + x);
                        addValue(outerPoints, c.x - y, c.x + x);

                        if (d < 0) {
                                d = d + (4 * x) + 6;
                        } else {
                                d = d + 4 * (x - y) + 10;
                                y--;
                        }
                        x++;
                } while (x <= y);

                for (Entry<Integer, ArrayList<Integer>> entry : outerPoints.entrySet()) {

                        List<Integer> l = entry.getValue();

                        int min = l.get(0).intValue();
                        int max = l.get(l.size() - 1).intValue();

                        for (int i = 0; i < l.size(); i++) {

                                if (l.get(i).intValue() < min) {
                                        min = l.get(i).intValue();
                                        continue;
                                }

                                if (l.get(i).intValue() > max) {
                                        max = l.get(i).intValue();
                                }
                        }

                        for (int i = min; i <= max; i++) {
                                circlePoints.add(new GridPoint2(entry.getKey().intValue(), i));
                        }

                }

                return circlePoints;

        }

        private void addValue(HashMap<Integer, ArrayList<Integer>> map, int x, int y) {
                map.computeIfAbsent(Integer.valueOf(x), k -> new ArrayList<>()).add(Integer.valueOf(y));
        }

        private float getDistanceToTarget(Entity target) {
                return this.getEntity().getPosition().dst(target.getPosition());
        }

        private void damageTargets() {
                for (int i = 0; i < numTargets; i++) {

                        if (targets[i] == null) {
                                continue;
                        }
                        targets[i].getComponent(CombatStatsComponent.class).hit(combatStats);
                }
        }

}
