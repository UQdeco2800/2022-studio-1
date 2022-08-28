package com.deco2800.game.components.Environmental;


import com.deco2800.game.components.Environmental.EnvironmentalComponent;

/** Tuple for passing the values of environmental objects */
public class ValueTuple<ResourceTypes, Integer> {

    public final ResourceTypes type;
    public final Integer value;

    public ValueTuple(ResourceTypes type, Integer value) {
        this.type = type;
        this.value = value;
    }
}
