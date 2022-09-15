package com.deco2800.game.components.Guidebook;

import com.deco2800.game.AtlantisSinks;
import com.deco2800.game.components.maingame.MainGameActions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.text.html.parser.Entity;
import java.awt.*;

public class GuidebookActions extends Component {
    private static final Logger logger = LoggerFactory.getLogger(MainGameActions.class);
    private AtlantisSinks game;
    private Entity player;


    public GuidebookActions(AtlantisSinks game, Entity player) {
        this.player = player;
        this.game = game;
    }

    public void create() {

    }
}
