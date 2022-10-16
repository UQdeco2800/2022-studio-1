package com.deco2800.game.achievements;

import java.util.List;
import java.util.Map;

public class AchievementFactory {


    /**
     * Creates a map for custom stat achievements milestones.
     * The keys are achievement ids and the list values are milestones
     *
     * @return map of stat milestones
     */
    public static Map<Integer, List<Integer>> createCustomStatAchievementMileStones() {
        return Map.of(1, List.of(50, 60, 100, 150),
                      2, List.of(10, 20, 50, 100));
    }
    /**
     * Creates all initial achievements in game
     *
     * @return list of initial achievements
     */
    public static List<Achievement> createInitialAchievements() {
        return List.of(
                // Stat achievements
                new Achievement(1, AchievementType.RESOURCES, true, false, false, "Gather wood", "You've gathered %d wood"),
                new Achievement(2, AchievementType.RESOURCES, true, false, false, "Gather stone", "You've gathered %d stone"),
                new Achievement(3, AchievementType.RESOURCES, true, false, false, "Gather gold", "You've gathered %d gold"),

                new Achievement(4, AchievementType.BUILDINGS, true, false, false, "Place buildings", "You've placed %d buildings"),

                new Achievement(5, AchievementType.KILLS,true, false, false, "Enemy Kills", "You've killed %d enemies"),

                new Achievement(6, AchievementType.UPGRADES, true, false, false, "Perform crystal upgrades", "You've completed %d crystal upgrades"),

                new Achievement(7, AchievementType.GAME, true, false, false, "Complete games", "You've completed %d games"),
                new Achievement(8, AchievementType.GAME, true, false, false, "Buy items from the shop", "You've bought %d items from the shop"),

                // Other achievements
                new Achievement(9, AchievementType.BUILDINGS, false, false, false, "Master Builder", "Place one of each type of structure"),

                new Achievement(10, AchievementType.KILLS, false, false, false, "Saviour", "Defeat the boss"),

                new Achievement(11, AchievementType.GAME, false, true, false, "Untouchable!", "Complete the game without crystal damage"),
                new Achievement(12, AchievementType.GAME, false, false, false, "Atlan", "Complete all achievements"),
                new Achievement(13, AchievementType.GAME, false, true, false, "buy all", "Buy every item from the shop in a single game"),
                //Complete the game without buying any items (other than buildings) from the shop - too long truncated
                new Achievement(14, AchievementType.GAME, false, true, false, "Cheapskate", "Complete the game without buying any items"),

                //new Achievement(15, AchievementType.MISC, false, true, false, "Pacifist", "Only defeat enemies using towers"),
                //new Achievement(16, AchievementType.MISC, false, false, true, "Defender", "Survived a night without losing a building"),
                new Achievement(17, AchievementType.MISC, false, false, true, "Protector", "Survived a night without taking crystal damage"),
                new Achievement(18, AchievementType.MISC, false, false, false, "Knowledgeable", "Read the guidebook"),
                //new Achievement(19, AchievementType.MISC, false, false, true, "Runner", "Survived a night without killing any enemies"),
                new Achievement(20, AchievementType.MISC, false, false, true, "No", "Lose on your first night")
                //new Achievement(21, AchievementType.MISC, false, false, false, "People Person", "Speak to every NPC")
        );
    }
}
