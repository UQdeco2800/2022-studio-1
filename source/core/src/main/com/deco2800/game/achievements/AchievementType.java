package com.deco2800.game.achievements;

/**
 * Represents each type of achievement in the game
 * each type can have many achievements
 */
public enum AchievementType {
    RESOURCES("Resources","image.png"),
    BUILDINGS("Buildings","image.png"),
    KILLS("Kills","image.png"),
    UPGRADES("Upgrades","image.png"),
    GAME ("Game Achievement","image.png"),
    MISC ("Misc", "image.png");

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
