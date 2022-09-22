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

        // On game stop
        saveAchievements();
    }
}
