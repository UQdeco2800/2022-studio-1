package com.deco2800.game.components.player;

import com.deco2800.game.components.Component;
import com.deco2800.game.rendering.AnimationRenderComponent;
import com.deco2800.game.components.shop.equipments.Equipments;
//import com.deco2800.game.components.shop;;
/**
 * This class listens to events relevant to a main player entity's state and plays the animation when one
 * of the events is triggered.
 */

class Node<T> {
  T t;
  public Node<T> next;
  public Node<T> prev;

  public Node(T t) {
      this.t = t;
  }
}

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

    entity.getEvents().addListener("attack_anim", this::attack_anim);
    entity.getEvents().addListener("attack_anim_rev", this::attack_anim_rev);
    entity.getEvents().addListener("death_anim", this::death_anim);
    
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

  void attack_anim(){    
    String anim_to_start = animator.getCurrentAnimation();  
    animator.stopAnimation();
    animator.startAnimation(anim_to_start.concat("a"));
  }

  void attack_anim_rev(){    
    String anim_to_start = animator.getCurrentAnimation();  
    animator.stopAnimation();
    animator.startAnimation(anim_to_start.substring(0, 1));
  }

  void death_anim(){    
    animator.startAnimation("death_anim");
  }
}
