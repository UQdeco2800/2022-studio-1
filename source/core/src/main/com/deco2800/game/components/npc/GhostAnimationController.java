package com.deco2800.game.components.npc;

import com.deco2800.game.components.Component;
import com.deco2800.game.entities.Entity;
import com.deco2800.game.rendering.AnimationRenderComponent;

/**
 * This class listens to events relevant to a ghost entity's state and plays the animation when one
 * of the events is triggered.
 */
public class GhostAnimationController extends Component {
  AnimationRenderComponent animator;

  @Override
  public void create() {
    super.create();
    animator = this.entity.getComponent(AnimationRenderComponent.class);
    entity.getEvents().addListener("wanderStart", this::animateWander);
    entity.getEvents().addListener("chaseStart", this::animateChase);

    //Eel animations for each direction
    entity.getEvents().addListener("fr", this::animateFrontRight);
    entity.getEvents().addListener("fl", this::animateFrontLeft);
    entity.getEvents().addListener("br", this::animateBackRight);
    entity.getEvents().addListener("bl", this::animateBackLeft);
  }

  void animateWander() {
    //animator.startAnimation("float");
  }

  void animateChase() {
    //animator.startAnimation("angry_float");
  }

  void animateFrontLeft(){animator.startAnimation("fl");}
  void animateFrontRight(){animator.startAnimation("fr");}
  void animateBackRight(){animator.startAnimation("br");}
  void animateBackLeft(){animator.startAnimation("bl");}
}
