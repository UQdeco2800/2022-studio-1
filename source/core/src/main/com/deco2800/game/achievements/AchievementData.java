package com.deco2800.game.achievements;


import java.util.ArrayList;

public class AchievementData {
    private long LastSaved;
    private ArrayList achievements;

    public long getLastSaved() {
        return LastSaved;
    }

    public void setLastSaved(long lastSaved) {
        LastSaved = lastSaved;
    }

    public ArrayList getAchievements() {
        return achievements;
    }

    public void setAchievements(ArrayList achievements) {
        this.achievements = achievements;
    }

    public AchievementData(long lastSaved, ArrayList achievements) {
        LastSaved = lastSaved;
        this.achievements = achievements;
    }
}
