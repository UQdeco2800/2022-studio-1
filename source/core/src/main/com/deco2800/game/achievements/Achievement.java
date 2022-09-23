package com.deco2800.game.achievements;

import java.util.Objects;


/**
 * A class that represents an achievement. It
 * can be persisted.
 */
public class Achievement {

    private int id;

    private AchievementType achievementType;
    private String name;
    private String description;
    private int totalAchieved;
    private boolean isCompleted;
    private boolean isStat;

    private String achievementData; // CSV or JSON formatted data specific to this particular achievement

    public Achievement() {

    }

    public Achievement(int id, AchievementType achievementType, boolean isStat, String name, String description) {
        this.id = id;
        this.achievementType = achievementType;
        this.name = name;
        this.description = description;
        this.isCompleted = false;
        this.isStat = isStat;
        this.achievementData = "";
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getTotalAchieved() {
        return totalAchieved;
    }

    public void setTotalAchieved(int totalAchieved) {
        this.totalAchieved = totalAchieved;
    }

    public boolean isCompleted() {
        return isCompleted;
    }

    public void setCompleted(boolean completed) {
        isCompleted = completed;
    }

    public String getAchievementData() {
        return achievementData;
    }

    public void setAchievementData(String achievementData) {
        this.achievementData = achievementData;
    }

    public AchievementType getAchievementType() {
        return achievementType;
    }

    public void setAchievementType(AchievementType achievementType) {
        this.achievementType = achievementType;
    }

    public boolean isStat() {
        return isStat;
    }

    public void setStat(boolean stat) {
        isStat = stat;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Achievement that = (Achievement) o;
        return id == that.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
