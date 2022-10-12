package com.deco2800.game.utils.math;

import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

import java.security.SecureRandom;

public class RandomUtils {
  public static Vector2 random(Vector2 start, Vector2 end) {
    return new Vector2(new SecureRandom().nextFloat(end.x - start.x + 1) + start.x,
                       new SecureRandom().nextFloat(end.y - start.y + 1) + start.y);
    //return new Vector2(MathUtils.random(start.x, end.x), MathUtils.random(start.y, end.y));
  }

  public static GridPoint2 random(GridPoint2 start, GridPoint2 end) {
    return new GridPoint2(new SecureRandom().nextInt(end.x - start.x + 1) + start.x,
                          new SecureRandom().nextInt(end.y - start.y + 1) + start.y);
    //return new GridPoint2(MathUtils.random(start.x, end.x), MathUtils.random(start.y, end.y));
  }

  private RandomUtils() {
    throw new IllegalStateException("Instantiating static util class");
  }
}
