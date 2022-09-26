package com.deco2800.game.components.achievements;

import com.deco2800.game.AtlantisSinks;
import com.deco2800.game.components.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AchievementActions extends Component {
    private static final Logger logger = LoggerFactory.getLogger(AchievementActions.class);
    private final AtlantisSinks game;

    public AchievementActions(AtlantisSinks game) {
        this.game = game;
    }

    @Override
    public void create() {
        entity.getEvents().addListener(AchievementBaseDisplay.EVENT_BUILDING_BUTTON_CLICKED, this::onBuilding);
        entity.getEvents().addListener(AchievementBaseDisplay.EVENT_GAME_BUTTON_CLICKED, this::onGame);
        entity.getEvents().addListener(AchievementBaseDisplay.EVENT_KILL_BUTTON_CLICKED, this::onKill);
        entity.getEvents().addListener(AchievementBaseDisplay.EVENT_RESOURCE_BUTTON_CLICKED, this::onResource);
        entity.getEvents().addListener(AchievementBaseDisplay.EVENT_UPGRADE_BUTTON_CLICKED, this::onUpgrade);
        entity.getEvents().addListener(AchievementBaseDisplay.EVENT_MISC_BUTTON_CLICKED, this::onMisc);
        entity.getEvents().addListener(AchievementBaseDisplay.EVENT_EXIT_BUTTON_CLICKED, this::onExit);
    }

    private void onBuilding() {
        logger.info("Building achievement screen");
    }

    private void onGame() {
        logger.info("Game achievement screen");
    }

    private void onKill() {
        logger.info("Kill achievement screen");
    }

    private void onResource() {
        logger.info("Resource achievement screen");
    }

    private void onUpgrade() {
        logger.info("Upgrade achievement screen");
    }

    private void onMisc() {
        logger.info("Misc achievement screen");
    }

    private void onExit() {
        logger.info("Exiting achievement screens");
        game.setScreen(AtlantisSinks.ScreenType.MAIN_GAME);
    }
}
