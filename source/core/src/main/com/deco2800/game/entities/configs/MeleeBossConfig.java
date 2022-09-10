package com.deco2800.game.entities.configs;

import com.badlogic.gdx.math.Vector2;

/**
 * Stats for Melee Boss enemy
 */
public class MeleeBossConfig extends BaseEntityConfig {
    public int health = 10;
    public int baseAttack = 2;
    public Vector2 speed = new Vector2(0.3f, 0.3f);
}
