package com.deco2800.game.achievements;

import java.util.Collections;
import java.util.List;

public class AchievementFactory {

    /**
     * Creates all initial achievements in game
     *
     * @return list of initial achievements
     */
    public static List<Achievement> createInitialAchievements() {
        return List.of(
                // Stat achievements
                new Achievement(1, AchievementType.RESOURCES, true, "Gather wood", "You've gathered %d wood", List.of(20, 40, 60, 100)),
                new Achievement(2, AchievementType.RESOURCES, true, "Gather stone", "You've gathered %d stone",  List.of(20, 40, 60, 100)),
                new Achievement(3, AchievementType.RESOURCES, true, "Gather gold", "You've gathered %d gold",  Collections.emptyList()),

                new Achievement(4, AchievementType.BUILDINGS, true, "Place buildings", "You've placed %d buildings",  Collections.emptyList()),

                new Achievement(5, AchievementType.KILLS,true, "Enemy Kills", "You've killed %d enemies", Collections.emptyList()),

                new Achievement(6, AchievementType.UPGRADES, true, "Perform crystal upgrades", "You've completed %d crystal upgrades",  Collections.emptyList()),

                new Achievement(7, AchievementType.GAME, true, "Complete games", "You've completed %d games",  Collections.emptyList()),
                new Achievement(8, AchievementType.GAME, true, "Buy items from the shop", "You've bought %d items from the shop",  Collections.emptyList()),

                // Other achievements
                new Achievement(9, AchievementType.BUILDINGS, false, "Master Builder", "Place one of each type of building",  Collections.emptyList()),

                new Achievement(10, AchievementType.KILLS, false, "Saviour", "Defeat the boss",  Collections.emptyList()),

                new Achievement(11, AchievementType.GAME, false, "Untouchable!", "Complete the game without your crystal being damaged",  Collections.emptyList()),
                new Achievement(12, AchievementType.GAME, false, "Atlan", "Complete all achievements",  Collections.emptyList()),
                new Achievement(13, AchievementType.GAME, false, "buy all", "Buy every item from the shop in a single game",  Collections.emptyList()),
                new Achievement(14, AchievementType.GAME, false, "Cheapskate", "Complete the game without buying any items (other than buildings) from the shop",  Collections.emptyList()),

                new Achievement(15, AchievementType.MISC, false, "Pacifist", "Only defeat enemies using towers",  Collections.emptyList()),
                new Achievement(16, AchievementType.MISC, false, "Defender", "Survived a night without losing a building",  Collections.emptyList()),
                new Achievement(17, AchievementType.MISC, false, "Protector", "Survived a night without taking crysal damage",  Collections.emptyList()),
                new Achievement(18, AchievementType.MISC, false, "Knowledgeable", "Read the guidebook",  Collections.emptyList()),
                new Achievement(19, AchievementType.MISC, false, "Runner", "Survived a night without killing any enemies",  Collections.emptyList()),
                new Achievement(20, AchievementType.MISC, false, "No", "Lose on your first night",  Collections.emptyList()),
                new Achievement(21, AchievementType.MISC, false, "People Person", "Speak to every NPC",  Collections.emptyList())
        );
    }
}
