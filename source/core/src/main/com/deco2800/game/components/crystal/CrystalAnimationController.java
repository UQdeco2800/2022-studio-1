package com.deco2800.game.components.crystal;
import com.deco2800.game.components.Component;
import com.deco2800.game.rendering.AnimationRenderComponent;

public class CrystalAnimationController extends Component {
    private AnimationRenderComponent animator;

    @Override
    public void create() {
        super.create();
        animator = entity.getComponent(AnimationRenderComponent.class);
        // ServiceLocator.getEntityService().getNamedEntity("crystal").getEvents().addListener("desCrystal", this::destroyedCrystal);
        entity.getEvents().addListener("desCrystal", this::destroyedCrystal);
        entity.getEvents().addListener("lastCrystal", this::lastCrystal);
    }

    private void destroyedCrystal() {
        animator.startAnimation("pcrystal");
    }

    private void lastCrystal(){
        animator.startAnimation("plast");
    }
}