package com.deco2800.game.achievements;


import java.util.ArrayList;

public class AchievementData {
    private long LastSaved;
    private ArrayList<Achievement> achievements;

    public long getLastSaved() {
        return LastSaved;
    }

    public void setLastSaved(long lastSaved) {
        LastSaved = lastSaved;
    }

    public ArrayList<Achievement> getAchievements() {
        return this.achievements;
    }


    public void setAchievements(ArrayList<Achievement> achievements) {
        this.achievements = achievements;
    }


    public AchievementData() {

    }
    public AchievementData(long lastSaved, ArrayList<Achievement> achievements) {
        LastSaved = lastSaved;
        this.achievements = achievements;
    }
}
