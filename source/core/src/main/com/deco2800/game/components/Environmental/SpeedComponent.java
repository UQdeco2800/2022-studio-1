package com.deco2800.game.components.Environmental;


import com.deco2800.game.components.Component;
import com.deco2800.game.components.player.PlayerActions;
import com.deco2800.game.entities.Entity;

public class SpeedComponent extends Component {

    private Entity player;

    @Override
    public void create(){

        System.out.println("Created");
    }

    public void update() {
        PlayerActions player_movement = this.player.getComponent(PlayerActions.class);
        player_movement.setPlayerSpeed(100, 1);
    }

    public void setPlayer(Entity player) {
        this.player = player;
    }
}
