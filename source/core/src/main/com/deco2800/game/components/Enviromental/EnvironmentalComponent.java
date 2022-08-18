package com.deco2800.game.components.Enviromental;

import com.deco2800.game.components.Component;

public class EnvironmentalComponent extends Component {

    enum EnvironmentalType {
        UNDEFINED(0),
        WOOD(10),
        ROCK(20),
        COBWEB(0);

        public int resourceValue;

        EnvironmentalType(int resourceValue) {
            this.resourceValue = resourceValue;
        }
    }

    private EnvironmentalType type;
    private Integer speedModifier;

    @Override
    public void create() {
        this.type = EnvironmentalType.UNDEFINED;
        this.speedModifier = 1;
    }

    @Override
    public void update() {
        System.out.println(this.type.resourceValue);
    }

    public EnvironmentalType getType() {
        return this.type;
    }

    public Integer getResourceAmount() {
        return this.type.resourceValue;
    }

    @Override
    public String toString() {
        return "";
    }


}
