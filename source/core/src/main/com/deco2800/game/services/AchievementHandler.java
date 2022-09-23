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
    private List<Achievement> achievements;
    private final FileHandle achievementsFileHandle = Gdx.files.external("AtlantisSinks/playerAchievements.json");
    private Json json;


    public AchievementHandler() {
        json = new Json();
        json.setElementType(AchievementData.class,"achievements", Achievement.class);
        json.setOutputType(JsonWriter.OutputType.json);
        if (Files.exists(Path.of(achievementsFileHandle.path()))){
            //Load from file

        }
        else {
            this.achievements = AchievementFactory.createInitialAchievements();
            AchievementData achievementData =new AchievementData(System.currentTimeMillis() ,new ArrayList(this.achievements));
            achievementsFileHandle.writeString(json.prettyPrint(achievementData),false);
        }
    }

    public List<Achievement> getAchievements() {
        return this.achievements;
    }

    public void saveAchievements() {

    }

    public void loadAchievements(FileHandle fH) {

    }

    public void run() {
        // Update achievement status'
        // Need listeners for stat achievements

        // while game is running do:
        // save state every x seconds?

        // On game stop
        saveAchievements();
    }

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
