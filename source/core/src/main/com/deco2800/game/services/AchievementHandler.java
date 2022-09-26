package com.deco2800.game.services;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonWriter;
import com.deco2800.game.achievements.Achievement;
import com.deco2800.game.achievements.AchievementData;
import com.deco2800.game.achievements.AchievementFactory;
import com.deco2800.game.achievements.AchievementType;
import com.deco2800.game.components.infrastructure.ResourceType;
import com.deco2800.game.events.EventHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Service for handling the loading, updating and saving of game achievements
 */
public class AchievementHandler {
    /**
     * Event string for crystal upgrade
     */
    public static final String EVENT_CRYSTAL_UPGRADED = "crystalUpgraded";

    /**
     * Event string for building placement
     */
    public static final String EVENT_BUILDING_PLACED = "buildingPlaced";

    /**
     * Event string for enemies killed
     */
    public static final String EVENT_ENEMY_KILLED = "Enemy Killed";

    /**
     * Event string for resources being added
     */
    public static final String EVENT_RESOURCE_ADDED = "resourceAdded";

    /**
     * Event string for stat achievement completion
     */
    public static final String EVENT_STAT_ACHIEVEMENT_MADE = "statAchieved";

    /* for non stat achievements */
    /**
     * Event string for regular achievement completion
     */
    public static final String EVENT_ACHIEVEMENT_MADE = "achievementMade";

    /**
     * Default stat achievement first level
     */
    public static final int STAT_ACHIEVEMENT_1_MILESTONE = 1;

    /**
     * Default stat achievement second level
     */
    public static final int STAT_ACHIEVEMENT_10_MILESTONE = 10;

    /**
     * Default stat achievement third level
     */
    public static final int STAT_ACHIEVEMENT_25_MILESTONE = 25;

    /**
     * Default stat achievement forth level
     */
    public static final int STAT_ACHIEVEMENT_50_MILESTONE = 50;

    /**
     * Achievement handler event handler
     */
    private final EventHandler events;

    /**
     * List of achievements being managed by the service
     */
    private final List<Achievement> achievements;

    /**
     * Map of stat achievements to their stat milestones
     */
    private final Map<Integer, List<Integer>> customStatMilestones;

    /**
     * File handler for the player achievement file
     */
    private final FileHandle achievementsFileHandle = Gdx.files.external("AtlantisSinks/playerAchievements.json");

    /**
     * Used for reading and writing to the player achievement file
     */
    private final Json json;

    /**
     * Stores the last time the status of the achievement handler was saved to the player achievement file
     */
    private long lastSaved;

    /**
     * Logs status updated from the service
     */
    private final Logger logger = LoggerFactory.getLogger(AchievementHandler.class);

    /**
     * Initialise the achievement handler. Uses default achievements if no achievement
     * file already exists
     */
    public AchievementHandler() {
        this.events = new EventHandler();

        json = new Json();
        json.setElementType(AchievementData.class,"achievements", Achievement.class);
        json.setOutputType(JsonWriter.OutputType.json);

        if (Files.exists(Path.of(Gdx.files.getExternalStoragePath() + achievementsFileHandle.path()))){
            //Load from file
            this.achievements = this.loadAchievements(achievementsFileHandle);
        }
        else {
            this.achievements = AchievementFactory.createInitialAchievements();
            this.saveAchievements();
        }

        this.customStatMilestones = AchievementFactory.createCustomStatAchievementMileStones();

        listenOnEvents();
    }


    /**
     * Listens for events from outside
     */
    private void listenOnEvents() {
        this.events.addListener(EVENT_CRYSTAL_UPGRADED, this::updateStatAchievementByType);
        this.events.addListener(EVENT_BUILDING_PLACED, this::updateStatAchievementByType);
        this.events.addListener(EVENT_ENEMY_KILLED, this::updateStatAchievementByType);

        // resource stat listeners
        this.events.addListener(EVENT_RESOURCE_ADDED, this::updateResourceStatOnResourceAdded);
    }

    /**
     * Getter method for the achievement list
     * @return List
     */
    public List<Achievement> getAchievements() {
        return this.achievements;
    }

    /**
     * Getter method for returning the events from AchievementHandler
     * @return EventHandler
     */
    public EventHandler getEvents() {
        return this.events;
    }

    /**
     * Saves the current state of the achievement list with the current time
     */
    public void saveAchievements() {
        this.lastSaved = System.currentTimeMillis();

        AchievementData achievementData = new AchievementData(this.lastSaved, this.achievements);

        achievementsFileHandle.writeString(json.prettyPrint(achievementData),false);
    }

    /**
     * Loads the achievement list from the achievement file
     * @param fH FileHandle
     * @return ArrayList
     */
    public List<Achievement> loadAchievements(FileHandle fH) {
        AchievementData data = json.fromJson(AchievementData.class, fH);
        this.lastSaved = data.getLastSaved();

        return data.getAchievements();
    }

    /**
     * Runs the achievement handler. Updates status of achievements in the list when progress is
     * made towards them.
     */
    public void run() {
        // while game is running do:
        // save state every x seconds?

        // On game stop
        saveAchievements();
    }

    /**
     * Updates the stats of a resource stat achievement
     *
     * @param resourceType the resource type (WOOD, STONE, GOLD)
     * @param amount the amount of resource being added
     */
    private void updateResourceStatOnResourceAdded(ResourceType resourceType, int amount) {

      Optional<Achievement> achievementOptional =  switch (resourceType) {
            case WOOD -> achievements.stream().filter(a -> a.getId() == 1).findFirst();
            case STONE -> achievements.stream().filter(a -> a.getId() ==2).findFirst();
            case GOLD -> achievements.stream().filter(a -> a.getId() == 3).findFirst();
          case ARTIFACT -> Optional.empty();
        };

      achievementOptional.ifPresent(a -> incrementTotalAchievedForStatAchievement(a, amount));
      saveAchievements();
    }

    /**
     * Basic method to update the stat type achievements when changes are made to the game state.
     * @param type AchievementType
     */
    public void updateStatAchievementByType(AchievementType type, int increase) {
        // no stat achievements fall into misc type so shouldn't have to deal with them
        Achievement achievement = new Achievement();

        switch (type) {
            case RESOURCES:
                // handled outside
                break;
            case BUILDINGS:
                // update resources achievement progress
                achievement = this.achievements.get(3);
                break;
            case KILLS:
                achievement = this.achievements.get(4);
                break;
            case UPGRADES:
                achievement = this.achievements.get(5);
                break;
            case GAME:
                achievement = this.achievements.get(7);
                // update game stats achievement
        }

        incrementTotalAchievedForStatAchievement(achievement, increase);
        saveAchievements();
    }

    /**
     * Correct updates the total achieved for a stat achievement.
     * checks if a milestone is reached and broadcasts message
     *
     * @param achievement the stat achievement
     * @param increase amount to increase by
     */
    public void incrementTotalAchievedForStatAchievement(Achievement achievement, int increase) {

        achievement.setTotalAchieved(achievement.getTotalAchieved() + increase);
        checkStatAchievementMilestones(achievement);
    }


    /**
     * Specific to Stat achievements.
     * Check whether a new achievement milestone is reached and broadcasts it.
     * If the last milestone is reached all achievements for that stat have been achieved.
     *
     * @param achievement the stat achievement
     */
    public void checkStatAchievementMilestones(Achievement achievement) {
        long totalAchieved = achievement.getTotalAchieved();

        // use standard milestones
        if (customStatMilestones.get(achievement.getId()) == null) {
            if (totalAchieved == STAT_ACHIEVEMENT_1_MILESTONE ||
                    totalAchieved == STAT_ACHIEVEMENT_10_MILESTONE ||
                    totalAchieved == STAT_ACHIEVEMENT_25_MILESTONE ||
                    totalAchieved == STAT_ACHIEVEMENT_50_MILESTONE) {
                broadcastStatAchievementMilestoneReached(achievement);
            }

            // has last milestone been achieved
            if (totalAchieved == STAT_ACHIEVEMENT_50_MILESTONE) {
                achievement.setCompleted(true);
            }
        } else { // use custom milestones
            for(Integer milestone : customStatMilestones.get(achievement.getId())) {
                if (milestone == totalAchieved) {
                    broadcastStatAchievementMilestoneReached(achievement);
                }
            }

            // Has last milestone been achieved
            if (customStatMilestones.get(achievement.getId()).get(customStatMilestones.get(achievement.getId()).size() - 1) == totalAchieved) {
                achievement.setCompleted(true);
            }
        }


    }

    /**
     * Broadcast the new achievement milestone reached to interested parties.
     * This is useful for any UI elements that need to display a popup more specific
     * to stat events.
     * <p>
     * TotalAchieved should reflect the newly achieved milestone
     *
     * @param achievement the stat achievement with the new milestone
     */
    private void broadcastStatAchievementMilestoneReached(Achievement achievement) {
        this.events.trigger(EVENT_STAT_ACHIEVEMENT_MADE, achievement);
        logger.info("Stat achievement of type: {} has been achieved", achievement.getAchievementType());
    }

    /**
     * Broadcast that a none stat achievement has been reached
     *
     * @param achievement the non-stat achievement
     */
    private void broadcastAchievementMade(Achievement achievement) {
        this.events.trigger(EVENT_ACHIEVEMENT_MADE, achievement);
    }

    /**
     * Marks an achievement completed
     * @param id the id of the a
     * @param broadcast whether to broadcast a completion message
     *                  true if so false otherwise
     */
    public void markAchievementCompletedById(long id, boolean broadcast) {

        achievements.stream().filter(a -> a.getId() == id).findFirst().ifPresent(a -> {
            a.setCompleted(true);
            if (broadcast) {
                broadcastAchievementMade(a);
            }
        });
    }
}
