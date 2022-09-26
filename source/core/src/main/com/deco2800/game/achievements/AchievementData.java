package com.deco2800.game.achievements;


import java.util.ArrayList;
import java.util.List;

public class AchievementData {
    private long LastSaved;

    private ArrayList achievements;

    public long getLastSaved() {
        return LastSaved;
    }

    public void setLastSaved(long lastSaved) {
        LastSaved = lastSaved;
    }

    public List<Achievement> getAchievements() {
        return this.achievements;
    }


    public void setAchievements(ArrayList achievements) {
        this.achievements = achievements;
    }


    public AchievementData() {

    }
    public AchievementData(long lastSaved, ArrayList achievements) {
        LastSaved = lastSaved;
        this.achievements = achievements;
    }
}
