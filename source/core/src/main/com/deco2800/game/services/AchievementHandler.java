package com.deco2800.game.services;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Json;
import com.deco2800.game.achievements.Achievement;
import com.deco2800.game.achievements.AchievementType;

import java.util.ArrayList;

public class AchievementHandler {
    private ArrayList<Achievement> achievements;
    FileHandle f = Gdx.files.internal("Achievements/playerAchievements.json");


    public AchievementHandler(String achievementList) {
        if (f.length() == 0) {
            FileHandle newAchievements = Gdx.files.internal(achievementList);
            loadAchievements(newAchievements);
        } else {
            loadAchievements(f);
        }
    }

    public ArrayList<Achievement> getAchievements() {
        return this.achievements;
    }

    public void saveAchievements() {

    }

    public void loadAchievements(FileHandle fH) {

    }

    public void run() {
        // Update achievement status'
        // Need listeners for stat achievements

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
