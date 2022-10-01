package com.deco2800.game.achievements;

/**
 * Represents each type of achievement in the game
 * each type can have many achievements
 */
public enum AchievementType {
    RESOURCES("Resources","images/achievements/Resource_Icon.png",
            "images/achievements/Resource_64x64_Icon.png"),
    BUILDINGS("Buildings","images/achievements/Building_Icon.png",
            "images/achievements/Building_64x64_Icon.png"),
    KILLS("Kills","images/achievements/Kill_Icon.png",
            "images/achievements/Kill_64x64_Icon.png"),
    UPGRADES("Upgrades","images/achievements/Upgrade_Icon.png",
            "images/achievements/Upgrade_64x64_Icon.png"),
    GAME ("Game Achievement","images/achievements/Game_Icon.png",
            "images/achievements/Game_64x64_Icon.png"),
    MISC ("Misc", "images/achievements/Misc_Icon.png",
            "images/achievements/Misc_64x64_Icon.png");

    private final String title;
    private final String image;

    private final String popupImage;

    AchievementType(String title, String image, String popupImage) {
        this.image = image;
        this.title = title;
        this.popupImage = popupImage;
    }

    public String getImage() {
        return image;
    }

    public String getTitle() {
        return title;
    }

    public String getPopupImage() {
        return popupImage;
    }
};
