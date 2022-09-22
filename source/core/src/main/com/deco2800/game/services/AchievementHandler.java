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
        Json json = new Json();

        this.f.writeString(json.prettyPrint(this.achievements), false);
    }

    public void loadAchievements(FileHandle fH) {
        Json json = new Json();

        this.achievements = json.fromJson(ArrayList.class, Achievement.class, fH);
    }

    public void run() {
        // Update achievement status'

        // On game stop
        saveAchievements();
    }
}
