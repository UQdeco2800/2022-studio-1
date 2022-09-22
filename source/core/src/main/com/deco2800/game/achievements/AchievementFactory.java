package com.deco2800.game.achievements;

import java.util.List;

public class AchievementFactory {

    /**
     * Creates all initial achievements in game
     *
     * @return list of initial achievements
     */
    public static List<Achievement> createInitialAchievements() {
        return List.of(
                new Achievement(1, AchievementType.KILLS,true, "Enemy Kills", "You've killed %d enemies"),
                new Achievement(2, AchievementType.KILLS, false, "Saviour", "Defeat the boss"),
                new Achievement(4, AchievementType.KILLS, false, "Javelin", "Speared (trident) the boss in the heart")
        );
    }
}
