package com.deco2800.game.components.player;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.deco2800.game.components.Component;
import com.deco2800.game.rendering.AnimationRenderComponent;
import com.deco2800.game.components.shop.equipments.Equipments;
import java.util.List;
//import com.deco2800.game.components.shop;;
/**
 * This class listens to events relevant to a main player entity's state and plays the animation when one
 * of the events is triggered.
 */

public class AnimationController extends Component {
  AnimationRenderComponent animator;
  int weapon = 0;
  //String[] wea_lst = {"bna_get_", "swo_get_", "tri_get_", "axe_get","arm_get_", "shi_get_", "hel_get_"};
  String temp_anim = "axe_w";
  String temp_dir = "w";
  
  @Override
  public void create() {
    super.create();
    animator = this.entity.getComponent(AnimationRenderComponent.class);
    entity.getEvents().addListener("ch_dir_w", this::ch_dir_w);
    entity.getEvents().addListener("ch_dir_a", this::ch_dir_a);
    entity.getEvents().addListener("ch_dir_s", this::ch_dir_s);
    entity.getEvents().addListener("ch_dir_d", this::ch_dir_d);
    entity.getEvents().addListener("walk_rev", this::walk_rev);

    entity.getEvents().addListener("attack_anim", this::attack_anim);
    entity.getEvents().addListener("attack_anim_rev", this::attack_anim_rev);
    entity.getEvents().addListener("death_anim", this::death_anim);
    entity.getEvents().addListener("after_death", this::after_death);
    entity.getEvents().addListener("weapons", this::weapons);
    
  }

  void wrapper(String dir){
    String anim = animator.getCurrentAnimation();
    animator.stopAnimation();
    if (anim.length() > 2) {
      anim = anim.substring(0, 4).concat(dir);
      animator.removeAnimation(anim);
      animator.addAnimation(anim, 0.15f, Animation.PlayMode.LOOP_REVERSED);
      animator.startAnimation(anim);
    }else{
      animator.startAnimation(dir);
    }
    temp_dir = dir; 
  }

  void ch_dir_w() {
    wrapper("w");    
  }

  void ch_dir_a() {
    wrapper("a");
  }

  void ch_dir_s() {
    wrapper("s");
  }

  void ch_dir_d() {
    wrapper("d");
  }

  void walk_rev() {
    String anim = animator.getCurrentAnimation();
    animator.removeAnimation(anim);
    animator.addAnimation(anim, 0.15f, Animation.PlayMode.REVERSED);
    animator.startAnimation(anim);
  }

  void attack_anim(){    
    String anim_to_start = animator.getCurrentAnimation(); 
    temp_anim = anim_to_start; 
        
    if (anim_to_start.startsWith("axe")) {
      animator.stopAnimation();
      animator.startAnimation("axe_att");
    } else if (anim_to_start.startsWith("arm") || anim_to_start.startsWith("hel")){;}
     else {
      animator.stopAnimation();
      if (anim_to_start.endsWith("a") || anim_to_start.endsWith("s")) {
        animator.startAnimation(anim_to_start.substring(0, 4).concat("att_as"));
      } else {
        animator.startAnimation(anim_to_start.substring(0, 4).concat("att_wd"));
      }
    }
  }

  void attack_anim_rev(){
    while (animator.isFinished()) {
      animator.stopAnimation();
      animator.startAnimation(temp_anim);
    }     
  }

  void death_anim(){    
    animator.startAnimation("death_anim");
  }

  void after_death() {
    animator.startAnimation("axe_w");
  }

  void weapons(){
    
    String[] wea_lst = converter(entity.getComponent(InventoryComponent.class).getEquipmentList());
    int counter = weapon % wea_lst.length;
    String anim_to_play = "";
    
    if (wea_lst[counter].equals("axe_get")) {
      anim_to_play = wea_lst[counter];
    }
    else if (temp_dir.equals("w") || temp_dir.equals("d")) {
      anim_to_play = wea_lst[counter].concat("wd");
    } else {
      anim_to_play = wea_lst[counter].concat("as");
    }    
    
    animator.stopAnimation();
    animator.startAnimation(anim_to_play);
    weapon += 1;
  }

  String[] converter(List<Equipments> lst){
    int size = lst.size();
    int count = 0;
    String[] res = new String[size];
    for (Equipments equipments : lst) {
      switch (equipments) {
        case AXE:
          res[count] = "axe_get";
          break;

        case SWORD:
          res[count] = "swo_get_";
          break;

        case BOW_AND_ARROW:
          res[count] = "bna_get_";
          break;

        case TRIDENT:
          res[count] = "tri_get_";
          break;
        
        case SHIELD:
          res[count] = "shi_get_";
          break;

        case CHESTPLATE:
          res[count] = "arm_get_";
          break;

        case HELMET:
          res[count] = "hel_get_";
          break;

        default:
          break;
      }
      count += 1;
    }
    return res;
  }
}
