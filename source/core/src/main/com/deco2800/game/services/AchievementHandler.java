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
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Service for handling the loading, updating and saving of game achievements
 */
public class AchievementHandler {
    /**
     * Event string for if the game was won
     */
    public static final String EVENT_GAME_WON = "gameWon";

    /**
     * Event string for items bought in the shop
     */
    public static final String EVENT_SHOP_ITEM_BOUGHT = "itemBought";

    /**
     * Event string for boss kill
     */
    public static final String EVENT_BOSS_KILL = "bossKilled";

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
    private final FileHandle achievementsFileHandle = Gdx.files.external("AtlantisSinks/playerAchievementsVersion2.json");

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

        // Non-stat achievements
        this.events.addListener(EVENT_BOSS_KILL, this::checkAchievementStatus);
        this.events.addListener(EVENT_SHOP_ITEM_BOUGHT, this::incrementOneRunAchievement);
        this.events.addListener(EVENT_GAME_WON, this::resetOneRunAchievements);
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
     * Getter method for last time achievements were saved
     * @return long
     */
    public long getLastSaved() {
        return this.lastSaved;
    }

    /**
     * Saves the current state of the achievement list with the current time
     */
    public void saveAchievements() {
        this.lastSaved = System.currentTimeMillis();

        AchievementData achievementData = new AchievementData(this.lastSaved, new ArrayList<>(this.achievements));

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
                achievement = this.getAchievementById(4);
                break;
            case KILLS:
                achievement = this.getAchievementById(5);
                break;
            case UPGRADES:
                achievement = this.getAchievementById(6);
                break;
            case GAME:
                // handled outside
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
     * Checks whether a milestone has been achieved for an achievement given a milestone number (1, 2,3,4)
     *
     * @param achievement the achievement to check
     * @param milestoneNumber the milestone number to check
     * @return true if milestone has been achieved false otherwise
     */
    public boolean isMilestoneAchieved(Achievement achievement, int milestoneNumber) {
        int totalAchieved = achievement.getTotalAchieved();

        // use standard milestones
        if (customStatMilestones.get(achievement.getId()) == null) {
            return switch (milestoneNumber) {
                case 1 -> totalAchieved >= STAT_ACHIEVEMENT_1_MILESTONE;
                case 2 -> totalAchieved >= STAT_ACHIEVEMENT_10_MILESTONE;
                case 3 -> totalAchieved >= STAT_ACHIEVEMENT_25_MILESTONE;
                case 4 -> totalAchieved >= STAT_ACHIEVEMENT_50_MILESTONE;
                default -> false;
            };
        } else { // use custom milestones
           return achievement.getTotalAchieved() >= customStatMilestones.get(achievement.getId()).get(milestoneNumber - 1);
        }
    }

    /**
     * Gets the total to achieve for a milestone
     *
     * @param achievement the achievement to get the total for the milestone for
     * @param milestoneNumber the milestone number
     * @return total for the milestone
     */
    public int getMilestoneTotal(Achievement achievement, int milestoneNumber) {
        int totalAchieved = achievement.getTotalAchieved();

        // use standard milestones
        if (customStatMilestones.get(achievement.getId()) == null) {
            return switch (milestoneNumber) {
                case 1 -> STAT_ACHIEVEMENT_1_MILESTONE;
                case 2 -> STAT_ACHIEVEMENT_10_MILESTONE;
                case 3 -> STAT_ACHIEVEMENT_25_MILESTONE;
                case 4 -> STAT_ACHIEVEMENT_50_MILESTONE;
                default -> 0;
            };
        } else { // use custom milestones
            int i = 1;
            for(Integer milestone : customStatMilestones.get(achievement.getId())) {
                    if (i == milestoneNumber) {
                        return milestone;
                    }
                i++;
            }
        }

        return 0;
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
     * Broadcast that a non-stat achievement has been reached
     *
     * @param achievement the non-stat achievement
     */
    private void broadcastAchievementMade(Achievement achievement) {
        this.events.trigger(EVENT_ACHIEVEMENT_MADE, achievement);
    }

    /**
     * Checks if an achievement has previously been completed, completes it if not
     * @param id long
     */
    public void checkAchievementStatus(long id) {
        if (!getAchievementById((int) id).isCompleted()) {
            markAchievementCompletedById(id, true);
        }
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

    /**
     * Returns an achievement based on its id
     * @param id int
     * @return Achievement if it exists, null otherwise
     */
    public Achievement getAchievementById(int id) {
        for (Achievement achievement : this.achievements) {
            if (achievement.getId() == id) {
                return achievement;
            }
        }

        return null;
    }

    /**
     * Checks if all achievements of a specific type are completed
     * @param type AchievementType
     * @return boolean
     */
    public boolean allCompleted(AchievementType type) {
        for (Achievement achievement : this.achievements) {
            if (achievement.getAchievementType() == type && !achievement.isCompleted()) {
                return false;
            }
        }

        return true;
    }

    /**
     * Resets progress of achievements that need to be completed in a single run of the game.
     */
    public void resetOneRunAchievements(boolean won) {
        if (won) {
            getAchievementById(7).setTotalAchieved(getAchievementById(7).getTotalAchieved() + 1);
            checkStatAchievementMilestones(getAchievementById(7));
        }

        for (Achievement toCheck : achievements) {
            if (toCheck.isOneRun()) {
                if (!toCheck.isCompleted() && won && toCheck.getTotalAchieved() == 0) {
                    markAchievementCompletedById(toCheck.getId(), true);
                } else {
                    toCheck.setTotalAchieved(0);
                }
            }
        }

        logger.info("Reset one run achievements");
        saveAchievements();
    }

    /**
     * Increments the one run achievement total
     * @param id int
     */
    public void incrementOneRunAchievement(int id) {
        if (id == 14) {
            getAchievementById(8).setTotalAchieved(getAchievementById(8).getTotalAchieved() + 1);
            checkStatAchievementMilestones(getAchievementById(8));
        }

        Achievement achievement = getAchievementById(id);
        achievement.setTotalAchieved(achievement.getTotalAchieved() + 1);

        saveAchievements();
    }
}
