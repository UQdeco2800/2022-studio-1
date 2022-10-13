package com.deco2800.game.components.achievements;

import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.deco2800.game.AtlantisSinks;
import com.deco2800.game.achievements.AchievementType;
import com.deco2800.game.components.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Dictates actions taken by the achievement screen when buttons have been clicked
 */
public class AchievementActions extends Component {
    /**
     * Logger class for the AchievementActions class
     */
    private static final Logger logger = LoggerFactory.getLogger(AchievementActions.class);

    /**
     * Game instance for AtlantisSinks
     */
    private final AtlantisSinks game;

    /**
     * Initialises the achievement screen actions
     * @param game AtlantisSinks
     */
    public AchievementActions(AtlantisSinks game) {
        this.game = game;
    }

    /**
     * Registers listeners for all buttons in the AchievementBaseScreen
     */
    @Override
    public void create() {
        entity.getEvents().addListener(AchievementDisplay.EVENT_EXIT_BUTTON_CLICKED, this::onExit);
    }

    /**
     * Returns to the main game screen
     * @param displayTable Table
     */
    private void onExit(Table displayTable, Table navigationTable) {
        logger.info("Exiting achievement screens");
        displayTable.clear();
        game.setScreen(AtlantisSinks.ScreenType.MAIN_GAME);
    }
}
