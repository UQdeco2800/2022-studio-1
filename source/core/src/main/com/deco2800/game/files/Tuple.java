package com.deco2800.game.files;

import com.badlogic.gdx.math.Vector2;

public class Tuple {

    public String texture;
    public Vector2 position;
    public String name;

    public Tuple setTexture(String texture) {
        this.texture = texture;
        return this;
    }

    public Tuple setPosition(Vector2 position) {
        this.position = position;
        return this;
    }

    public Tuple setName(String name) {
        this.name = name;
        return this;
    }
}