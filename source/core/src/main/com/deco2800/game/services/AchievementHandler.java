package com.deco2800.game.services;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonWriter;
import com.deco2800.game.achievements.Achievement;
import com.deco2800.game.achievements.AchievementData;
import com.deco2800.game.achievements.AchievementFactory;
import com.deco2800.game.achievements.AchievementType;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class AchievementHandler {
    private final List<Achievement> achievements;
    private final FileHandle achievementsFileHandle = Gdx.files.external("AtlantisSinks/playerAchievements.json");
    private final Json json;
    private long lastSaved;


    /**
     * Initialise the achievement handler. Uses default achievements if no achievement
     * file already exists
     */
    public AchievementHandler() {
        json = new Json();
        json.setElementType(AchievementData.class,"achievements", Achievement.class);
        json.setOutputType(JsonWriter.OutputType.json);

        if (Files.exists(Path.of(Gdx.files.getExternalStoragePath() + achievementsFileHandle.path()))){
            //Load from file
            this.achievements = this.loadAchievements(achievementsFileHandle);   // <- this is the real line but method broken
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

        // while game is running do:
        // save state every x seconds?

        // On game stop
        saveAchievements();
    }

    /**
     * Basic method to update the stat type achievements when changes are made to the game state.
     * @param type AchievementType
     */
    public void updateStatAchievement(AchievementType type) {
        // no stat achievements fall into misc type so shouldn't have to deal with them
        switch (type) {
            case RESOURCES:
                // update resources achievement progress
                break;
            case BUILDINGS:
                // update resources achievement progress
                break;
            case KILLS:
                //
                break;
            case UPGRADES:
                //
                break;
            case GAME:
                // update game stats achievement
        }
    }
}
