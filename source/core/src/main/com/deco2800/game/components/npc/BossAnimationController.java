package com.deco2800.game.components.npc;

import com.badlogic.gdx.math.Vector2;
import com.deco2800.game.areas.terrain.TerrainFactory;
import com.deco2800.game.components.Component;
import com.deco2800.game.entities.Entity;
import com.deco2800.game.rendering.AnimationRenderComponent;

public class BossAnimationController extends Component {
    private AnimationRenderComponent animator;

    public void create() {
        super.create();
        animator = entity.getComponent(AnimationRenderComponent.class);
    }

    public void startAnimation(Entity target) {
        Vector2 tarPos = target.getCenterPosition();
        Vector2 ePos = entity.getCenterPosition();
        double x = tarPos.x - ePos.x;
        double y = tarPos.y - ePos.y;
        double mag = Math.sqrt(Math.pow(x, 2d) + Math.pow(y, 2));
        x = x / mag;
        y = y / mag;
        if (x + y > 0) {
            animator.startAnimation("boss_side2");
        } else {
            animator.startAnimation("boss_frame");
        }
    }
}
