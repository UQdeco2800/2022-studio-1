package com.deco2800.game.components.gamearea;

import com.deco2800.game.areas.terrain.TerrainComponent;
import com.deco2800.game.components.Component;

public class GameAreaActions extends Component {
        @Override
        public void create() {
                entity.getEvents().addListener("expandIsland", this::expandIsland);
        }

        void expandIsland() {

        }
}
