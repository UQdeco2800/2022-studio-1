package com.deco2800.game.components.achievements;

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
        entity.getEvents().addListener(AchievementDisplay.EVENT_BUILDING_BUTTON_CLICKED, this::onBuilding);
        entity.getEvents().addListener(AchievementDisplay.EVENT_GAME_BUTTON_CLICKED, this::onGame);
        entity.getEvents().addListener(AchievementDisplay.EVENT_KILL_BUTTON_CLICKED, this::onKill);
        entity.getEvents().addListener(AchievementDisplay.EVENT_RESOURCE_BUTTON_CLICKED, this::onResource);
        entity.getEvents().addListener(AchievementDisplay.EVENT_UPGRADE_BUTTON_CLICKED, this::onUpgrade);
        entity.getEvents().addListener(AchievementDisplay.EVENT_MISC_BUTTON_CLICKED, this::onMisc);
        entity.getEvents().addListener(AchievementDisplay.EVENT_EXIT_BUTTON_CLICKED, this::onExit);
    }

    /**
     * Loads the BuildingAchievementScreen
     */
    private void onBuilding(AchievementDisplay display) {
        logger.info("Building achievement screen");
        display.changeDisplay(AchievementType.BUILDINGS);
    }

    /**
     * Loads the GameAchievementScreen
     */
    private void onGame(AchievementDisplay display) {
        logger.info("Game achievement screen");
        display.changeDisplay(AchievementType.SUMMARY);
    }

    /**
     * Loads the KillAchievementScreen
     */
    private void onKill(AchievementDisplay display) {
        logger.info("Kill achievement screen");
        display.changeDisplay(AchievementType.KILLS);
    }

    /**
     * Loads the ResourceAchievementScreen
     */
    private void onResource(AchievementDisplay display) {
        logger.info("Resource achievement screen");
        display.changeDisplay(AchievementType.RESOURCES);
    }

    /**
     * Loads the UpgradeAchievementScreen
     */
    private void onUpgrade(AchievementDisplay display) {
        logger.info("Upgrade achievement screen");
        display.changeDisplay(AchievementType.UPGRADES);
    }

    /**
     * Loads the MiscAchievementScreen
     */
    private void onMisc(AchievementDisplay display) {
        logger.info("Misc achievement screen");
        display.changeDisplay(AchievementType.MISC);
    }

    /**
     * Returns to the main game screen
     */
    private void onExit(AchievementDisplay display) {
        logger.info("Exiting achievement screens");
        game.setScreen(AtlantisSinks.ScreenType.MAIN_GAME);
    }
}
