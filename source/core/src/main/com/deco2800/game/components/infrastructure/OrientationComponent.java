package com.deco2800.game.components.infrastructure;

import com.badlogic.gdx.Input.Orientation;
import com.deco2800.game.components.Component;

public class OrientationComponent extends Component {

        private int orientation = 0;

        /**
         * get the orientation of the entity
         * @return orientation of the entity 0 or 1
         */
        public int getOrientation() {
                return orientation;
        }

        /**
         * set the orientation of a structure
         * @param orientation of a structure 0 or 1
         */
        public void setOrientation(int orientation) {
                this.orientation = orientation;
        }

        /**
         * orientation component constructor
         * initializes entity orientation
         * @param orientation of the entity 0 or 1
         */
        public OrientationComponent(int orientation) {
                this.orientation = orientation;
        }

}
