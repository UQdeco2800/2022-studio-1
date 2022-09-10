package com.deco2800.game.components.player;

import com.deco2800.game.components.Component;
import com.deco2800.game.rendering.AnimationRenderComponent;

/**
 * This class listens to events relevant to a main player entity's state and plays the animation when one
 * of the events is triggered.
 */
public class AnimationController extends Component {
  AnimationRenderComponent animator;

  @Override
  public void create() {
    super.create();
    animator = this.entity.getComponent(AnimationRenderComponent.class);
    entity.getEvents().addListener("ch_dir_w", this::ch_dir_w);
    entity.getEvents().addListener("ch_dir_a", this::ch_dir_a);
    entity.getEvents().addListener("ch_dir_s", this::ch_dir_s);
    entity.getEvents().addListener("ch_dir_d", this::ch_dir_d);
    
  }

  void ch_dir_w() {
    animator.stopAnimation();
    animator.startAnimation("w");
  }

  void ch_dir_a() {
    animator.stopAnimation();
    animator.startAnimation("a");
  }

  void ch_dir_s() {
    animator.stopAnimation();
    animator.startAnimation("s");
  }

  void ch_dir_d() {
    animator.stopAnimation();
    animator.startAnimation("d");
  }
}
