package com.deco2800.game.services;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.deco2800.game.achievements.Achievement;
import com.deco2800.game.achievements.AchievementType;
import com.deco2800.game.components.infrastructure.ResourceType;
import com.deco2800.game.extensions.GameExtension;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.ArrayList;

@ExtendWith(GameExtension.class)
public class AchievementHandlerTest {
    private AchievementHandler achievementHandler;

    @BeforeEach
    void beforeEach() {
        achievementHandler = new AchievementHandler();
    }


    @Test
    public void shouldLoadAchievements() {
        ArrayList<Achievement> originalAchievements = new ArrayList<>(achievementHandler.getAchievements());
        FileHandle achievementFileHandle = Gdx.files.internal("test/files/testAchievements.json");

        Assertions.assertNotEquals(originalAchievements, achievementHandler.loadAchievements(achievementFileHandle));
    }

    @Test
    public void shouldUpdateWoodResourceStatAchievement() {
        int woodAchieved = achievementHandler.getAchievementById(1).getTotalAchieved();
        achievementHandler.getEvents().trigger(AchievementHandler.EVENT_RESOURCE_ADDED, ResourceType.WOOD, 1);

        Assertions.assertEquals(woodAchieved + 1, achievementHandler.getAchievementById(1).getTotalAchieved());
    }

    @Test
    public void shouldUpdateStoneResourceStatAchievement() {
        int stoneAchieved = achievementHandler.getAchievementById(2).getTotalAchieved();
        achievementHandler.getEvents().trigger(AchievementHandler.EVENT_RESOURCE_ADDED, ResourceType.STONE, 1);

        Assertions.assertEquals(stoneAchieved + 1, achievementHandler.getAchievementById(2).getTotalAchieved());
    }

    @Test
    public void shouldUpdateGoldResourceStatAchievement() {
        int goldAchieved = achievementHandler.getAchievementById(3).getTotalAchieved();
        achievementHandler.getEvents().trigger(AchievementHandler.EVENT_RESOURCE_ADDED, ResourceType.GOLD, 1);

        Assertions.assertEquals(goldAchieved + 1, achievementHandler.getAchievementById(3).getTotalAchieved());
    }

    @Test
    public void shouldUpdateBuildingStatAchievement() {
        int buildingsAchieved = achievementHandler.getAchievementById(4).getTotalAchieved();
        achievementHandler.getEvents().trigger(AchievementHandler.EVENT_BUILDING_PLACED, AchievementType.BUILDINGS, 1);

        Assertions.assertEquals(buildingsAchieved + 1, achievementHandler.getAchievementById(4).getTotalAchieved());
    }

    @Test
    public void shouldUpdateKillsAchievement() {
        int killsAchieved = achievementHandler.getAchievementById(5).getTotalAchieved();
        achievementHandler.getEvents().trigger(AchievementHandler.EVENT_ENEMY_KILLED, AchievementType.KILLS, 1);

        Assertions.assertEquals(killsAchieved + 1, achievementHandler.getAchievementById(5).getTotalAchieved());
    }

    @Test
    public void shouldUpdateUpgradesAchievement() {
        int upgradesAchieved = achievementHandler.getAchievementById(6).getTotalAchieved();
        achievementHandler.getEvents().trigger(AchievementHandler.EVENT_CRYSTAL_UPGRADED, AchievementType.UPGRADES, 1);

        Assertions.assertEquals(upgradesAchieved + 1, achievementHandler.getAchievementById(6).getTotalAchieved());
    }

    @Test
    public void shouldCompleteStatAchievementMilestones() {
        achievementHandler.getEvents().addListener(AchievementHandler.EVENT_STAT_ACHIEVEMENT_MADE, (Achievement achievement) -> {
            Assertions.assertEquals(AchievementHandler.STAT_ACHIEVEMENT_10_MILESTONE, achievement.getTotalAchieved());
        });

        new Achievement(100, AchievementType.GAME, true, false, false, "test", "test").setTotalAchieved(10);
    }

    @Test
    public void shouldCompleteStatAchievement() {
        Achievement test = new Achievement(100, AchievementType.GAME, true, false, false, "test", "test");
        test.setTotalAchieved(50);

        achievementHandler.checkStatAchievementMilestones(test);

        Assertions.assertTrue(test.isCompleted());
    }
}
