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
        entity.getEvents().addListener(AchievementDisplay.EVENT_SUMMARY_BUTTON_CLICKED, this::onSummary);
        entity.getEvents().addListener(AchievementDisplay.EVENT_BUILDING_BUTTON_CLICKED, this::onBuilding);
        entity.getEvents().addListener(AchievementDisplay.EVENT_GAME_BUTTON_CLICKED, this::onGame);
        entity.getEvents().addListener(AchievementDisplay.EVENT_KILL_BUTTON_CLICKED, this::onKill);
        entity.getEvents().addListener(AchievementDisplay.EVENT_RESOURCE_BUTTON_CLICKED, this::onResource);
        entity.getEvents().addListener(AchievementDisplay.EVENT_UPGRADE_BUTTON_CLICKED, this::onUpgrade);
        entity.getEvents().addListener(AchievementDisplay.EVENT_MISC_BUTTON_CLICKED, this::onMisc);
        entity.getEvents().addListener(AchievementDisplay.EVENT_EXIT_BUTTON_CLICKED, this::onExit);
    }

    /**
     * Populates the display table with the achievement summary
     * @param displayTable Table
     */
    private void onSummary(Table displayTable) {
        logger.info("Achievement Summary screen");
        AchievementDisplay.changeDisplay(displayTable, AchievementType.SUMMARY);
    }

    /**
     * Populates display table with building achievements
     * @param displayTable Table
     */
    private void onBuilding(Table displayTable) {
        logger.info("Building achievement screen");
        AchievementDisplay.changeDisplay(displayTable, AchievementType.BUILDINGS);
    }

    /**
     * Populates the display table with game achievements
     * @param displayTable Table
     */
    private void onGame(Table displayTable) {
        logger.info("Game achievement screen");
        AchievementDisplay.changeDisplay(displayTable, AchievementType.GAME);
    }

    /**
     * Populates the display table with kill achievements
     * @param displayTable Table
     */
    private void onKill(Table displayTable) {
        logger.info("Kill achievement screen");
        AchievementDisplay.changeDisplay(displayTable, AchievementType.KILLS);
    }

    /**
     * Populates the display table with resources achievements
     * @param displayTable Table
     */
    private void onResource(Table displayTable) {
        logger.info("Resource achievement screen");
        AchievementDisplay.changeDisplay(displayTable, AchievementType.RESOURCES);
    }

    /**
     * Populates the display table with upgrade achievements
     * @param displayTable Table
     */
    private void onUpgrade(Table displayTable) {
        logger.info("Upgrade achievement screen");
        AchievementDisplay.changeDisplay(displayTable, AchievementType.UPGRADES);
    }

    /**
     * Populates the display table with misc achievements
     * @param displayTable Table
     */
    private void onMisc(Table displayTable) {
        logger.info("Misc achievement screen");
        AchievementDisplay.changeDisplay(displayTable, AchievementType.MISC);
    }

    /**
     * Returns to the main game screen
     * @param displayTable Table
     */
    private void onExit(Table displayTable) {
        logger.info("Exiting achievement screens");
        displayTable.clear();
        game.setScreen(AtlantisSinks.ScreenType.MAIN_GAME, AtlantisSinks.ScreenType.ACHIEVEMENT);
    }
}
