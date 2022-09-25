package com.deco2800.game.services;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonWriter;
import com.deco2800.game.achievements.Achievement;
import com.deco2800.game.achievements.AchievementData;
import com.deco2800.game.achievements.AchievementFactory;
import com.deco2800.game.achievements.AchievementType;
import com.deco2800.game.events.EventHandler;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

/**
 * Service for handling the loading, updating and saving of game achievements
 */
public class AchievementHandler {
    public static final String EVENT_CRYSTAL_UPGRADED = "crystalUpgraded";
    public static final String EVENT_BUILDING_PLACED = "buildingPlaced";
    private EventHandler events;
    private final List<Achievement> achievements;
    private final FileHandle achievementsFileHandle = Gdx.files.external("AtlantisSinks/playerAchievements.json");
    private final Json json;
    private long lastSaved;


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

        AchievementData achievementData = new AchievementData(this.lastSaved,
                new ArrayList<>(this.achievements));

        achievementsFileHandle.writeString(json.prettyPrint(achievementData),false);
    }

    /**
     * Loads the achievement list from the achievement file
     * @param fH FileHandle
     * @return ArrayList
     */
    public ArrayList<Achievement> loadAchievements(FileHandle fH) {
        AchievementData data = json.fromJson(AchievementData.class, fH);
        this.lastSaved = data.getLastSaved();

        return data.getAchievements();
    }

    /**
     * Runs the achievement handler. Updates status of achievements in the list when progress is
     * made towards them.
     */
    public void run() {
        // Update achievement status'
        // Need listeners for stat achievements
        this.events.addListener(EVENT_CRYSTAL_UPGRADED, this::updateStatAchievement);
        this.events.addListener(EVENT_BUILDING_PLACED, this::updateStatAchievement);

        // while game is running do:
        // save state every x seconds?

        // On game stop
        saveAchievements();
    }

    /**
     * Basic method to update the stat type achievements when changes are made to the game state.
     * @param type AchievementType
     */
    public void updateStatAchievement(AchievementType type, int increase) {
        // no stat achievements fall into misc type so shouldn't have to deal with them
        Achievement achievement = new Achievement();

        switch (type) {
            case RESOURCES:
                // update resources achievement progress
                // achievements 0, 1, 2
                achievement = this.achievements.get(0);
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

        // need to add csv values to achievement factory
        achievement.setTotalAchieved(achievement.getTotalAchieved() + increase);
        String[] data = achievement.getAchievementData().split(",");

        if ((Integer.parseInt(data[data.length - 1])) <= achievement.getTotalAchieved()) {
            achievement.setCompleted(true);
        }

        saveAchievements();
    }
}
