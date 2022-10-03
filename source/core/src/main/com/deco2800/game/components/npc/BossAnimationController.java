package com.deco2800.game.components.npc;

import com.deco2800.game.components.Component;
import com.deco2800.game.rendering.AnimationRenderComponent;

public class BossAnimationController extends Component {
    private AnimationRenderComponent animator;

    public void create() {
        super.create();
        animator = entity.getComponent(AnimationRenderComponent.class);
        entity.getEvents().addListener("r", this::movingRight);
        entity.getEvents().addListener("l", this::movingLeft);
    }

    private void movingRight() {
        animator.startAnimation("boss_frame");
    }

    private void movingLeft() {
        animator.startAnimation("boss_frame");
    }
}
