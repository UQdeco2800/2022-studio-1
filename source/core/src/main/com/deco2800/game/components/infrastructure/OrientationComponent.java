package com.deco2800.game.components.infrastructure;

import com.badlogic.gdx.Input.Orientation;
import com.deco2800.game.components.Component;

public class OrientationComponent extends Component {

        private int orientation = 0;

        public int getOrientation() {
                return orientation;
        }

        public void setOrientation(int orientation) {
                this.orientation = orientation;
        }

        public OrientationComponent(int orientation) {
                this.orientation = orientation;
        }

}
