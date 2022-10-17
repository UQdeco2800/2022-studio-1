package com.deco2800.game.files;

import com.badlogic.gdx.math.Vector2;

import java.util.HashMap;

public class Tuple {

    public String texture;
    public Vector2 position;
    public String name;
    public int level;
    public int health;
    public HashMap<String, Object> playerState;
    public String tileString;
    public String creationMethod;
    public int structureLevel;
    public int rotation;

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

    public Tuple setLevel(int level) {
        this.level = level;
        return this;
    }

    public Tuple setHealth(int health) {
        this.health = health;
        return this;
    }

    public Tuple setPlayerState(HashMap<String, Object> playerState) {
        this.playerState = playerState;
        return this;
    }

    public Tuple setTileString(String tileString) {
        this.tileString = tileString;
        return this;
    }

    public Tuple setCreationMethod(String className) {
        this.creationMethod = className;
        return this;
    }

    public Tuple setStructureLevel(int level) {
        this.structureLevel = level;
        return this;
    }

    public Tuple setRotation(int rotation) {
        this.rotation = rotation;
        return this;
    }
}