package com.deco2800.game.components.player;

import com.deco2800.game.components.Component;
import com.deco2800.game.rendering.AnimationRenderComponent;

/**
 * This class listens to events relevant to a ghost entity's state and plays the animation when one
 * of the events is triggered.
 */
public class PlayerAnimationController extends Component {
  AnimationRenderComponent animator;

  @Override
  public void create() {
    super.create();
    animator = this.entity.getComponent(AnimationRenderComponent.class);
    entity.getEvents().addListener("anim_player", this::animate);
    entity.getEvents().addListener("anim_player_leaf", this::animate_leaf);
  }

  void animate() {
    System.out.println("Succesfully reached");
    animator.startAnimation("box_boy");
  }

  void animate_leaf() {
    System.out.println("Succesfully reached1");
    animator.startAnimation("box_boy_leaf");
  }
}
