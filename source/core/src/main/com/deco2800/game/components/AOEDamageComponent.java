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
import com.deco2800.game.services.ServiceLocator;

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

                UGS ugs = ServiceLocator.getUGSService();
                Vector2 ownerWorldPosition = getEntity().getCenterPosition();

                GridPoint2 ownerTilePositon = ServiceLocator.getEntityService().getNamedEntity("terrain")
                                .getComponent(TerrainComponent.class)
                                .worldToTilePosition(ownerWorldPosition.x, ownerWorldPosition.y);

                ArrayList<GridPoint2> tilesInRadius = generateCircle(ownerTilePositon);
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

        private void damageTargets() {

        }
}
