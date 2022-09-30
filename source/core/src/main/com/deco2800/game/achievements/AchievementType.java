package com.deco2800.game.achievements;

/**
 * Represents each type of achievement in the game
 * each type can have many achievements
 */
public enum AchievementType {
    RESOURCES("Resources","images/achievements/Resource_Icon.png"),
    BUILDINGS("Buildings","images/achievements/Building_Icon.png"),
    KILLS("Kills","images/achievements/Kill_Icon.png"),
    UPGRADES("Upgrades","images/achievements/Upgrade_Icon.png"),
    GAME ("Game Achievement","images/achievements/Game_Icon.png"),
    MISC ("Misc", "images/achievements/Misc_Icon.png");

    private final String title;
    private final String image;

    AchievementType(String title, String image) {
        this.image = image;
        this.title = image;
    }

    public String getImage() {
        return image;
    }

    public String getTitle() {
        return title;
    }
};
