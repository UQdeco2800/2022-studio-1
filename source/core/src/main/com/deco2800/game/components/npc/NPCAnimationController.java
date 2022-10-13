package com.deco2800.game.components.npc;

import com.deco2800.game.components.Component;
import com.deco2800.game.rendering.AnimationRenderComponent;

public class NPCAnimationController extends Component {
    private AnimationRenderComponent animator;

    public void create() {
        super.create();
        animator = this.entity.getComponent(AnimationRenderComponent.class);
        entity.getEvents().addListener("NPC", this::movingNPC);

    }

    private void movingNPC() {
        animator.startAnimation("NPC");
    }
}
