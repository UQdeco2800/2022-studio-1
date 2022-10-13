package com.deco2800.game.components.settingsmenu;

import com.deco2800.game.AtlantisSinks;
import com.deco2800.game.areas.ForestGameArea;
import com.deco2800.game.components.Component;
import com.deco2800.game.components.maingame.MainGameActions;
import com.deco2800.game.entities.Entity;
import com.deco2800.game.services.ResourceService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class SettingsActions extends Component {
    private static final Logger logger = LoggerFactory.getLogger(MainGameActions.class);
    private AtlantisSinks game;
    private Entity player;
    private Boolean musicStatus;
    private Boolean soundEffectStatus;

    public void settingsActions(AtlantisSinks game, Entity player) {
        this.player = player;
        this.game = game;
    }

    @Override
    public void create() {
        entity.getEvents().addListener("musicOff", this::musicOff);
        entity.getEvents().addListener("musicOn", this::musicOn);
    }

    public Boolean musicOff() {
        return false;
    }

    public Boolean musicOn() {
        return true;
    }
}
