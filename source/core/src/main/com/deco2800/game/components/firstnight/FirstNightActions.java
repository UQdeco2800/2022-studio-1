package com.deco2800.game.components.firstnight;

import com.deco2800.game.AtlantisSinks;
import com.deco2800.game.components.CombatStatsComponent;
import com.deco2800.game.components.Component;
import com.deco2800.game.components.player.InventoryComponent;
import com.deco2800.game.memento.CareTaker;
import com.deco2800.game.memento.Originator;
import com.deco2800.game.services.DayNightCycleService;
import com.deco2800.game.services.DayNightCycleStatus;
import com.deco2800.game.services.ServiceLocator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class listens to events relevant to the story line Screen and does something when one of the
 * events is triggered.
 */
public class FirstNightActions extends Component {
    private static final Logger logger = LoggerFactory.getLogger(com.deco2800.game.components.firstnight.FirstNightActions.class);
    private final AtlantisSinks game;
    //private CareTaker playerStatus;

    public FirstNightActions(AtlantisSinks game) {
        this.game = game;
    }

    @Override
    public void create() {
        //entity.getEvents().addListener("next", this::onSkip);
    }

    /**
     * Swaps to the Main Game screen.
     */
    private void onSkip() {
        logger.info("Resume game after first night");
        //implement function to retrieve the player status
        game.setScreen(AtlantisSinks.ScreenType.MAIN_GAME, AtlantisSinks.ScreenType.MAIN_GAME_LOAD);
    }
}