package com.deco2800.game.entities.factories;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.deco2800.game.achievements.AchievementType;
import com.deco2800.game.areas.terrain.TerrainComponent;
import com.deco2800.game.components.CameraComponent;
import com.deco2800.game.components.CombatStatsComponent;
import com.deco2800.game.components.camera.CameraActions;
import com.deco2800.game.components.player.InventoryComponent;
import com.deco2800.game.components.player.PlayerStatsDisplay;
import com.deco2800.game.entities.Entity;
import com.deco2800.game.services.AchievementHandler;
import com.deco2800.game.services.DayNightCycleStatus;
import com.deco2800.game.services.ServiceLocator;

import java.util.Timer;
import java.util.TimerTask;

import static com.deco2800.game.entities.factories.CrystalFactory.createCrystal;

public class CrystalService {
 static boolean interval = false;
    /**
     * Spawns Crystal outside of Game Area class
     *
     * @param texture path of texture for new Crystal Entity
     *
     */
    public static void triggerCrystal(String texture) {
        Entity crystal = createCrystal(texture, "crystal2");
        ServiceLocator.getEntityService().registerNamed("crystal2", crystal);
        crystal.setPosition(new Vector2(ServiceLocator.getEntityService().getNamedEntity("crystal").getPosition().x,
                ServiceLocator.getEntityService().getNamedEntity("crystal").getPosition().y));
    }

    /**
     * Upgrades the level of the Crystal changes its texture and increases its
     * maximum health
     */
    public static void upgradeCrystal() {
        Entity crystal = ServiceLocator.getEntityService().getNamedEntity("crystal");
        int level = crystal.getComponent(CombatStatsComponent.class).getLevel();
        // crystal.dispose();
        Entity player = ServiceLocator.getEntityService().getNamedEntity("player");
        if (level == 1) {
            // crystal.addComponent(new
            // TextureRenderComponent("images/crystal_level2.png"));
            triggerCrystal("images/crystal_level2.png");
            player.getComponent(InventoryComponent.class).addGold(-2000);
            PlayerStatsDisplay.updateItems();
        } else if (level == 2) {
            ServiceLocator.getEntityService().getNamedEntity("crystal2").dispose();
            triggerCrystal("images/crystal_level3.png");
            // crystal.addComponent(new
            // TextureRenderComponent("images/crystal_level3.png"));
            ServiceLocator.getEntityService().unregisterNamed("crystal2");
            player.getComponent(InventoryComponent.class).addGold(-5000);
            PlayerStatsDisplay.updateItems();
        }
        // upgrading only increases max health and does not impact current health
        crystal.getComponent(CombatStatsComponent.class).setMaxHealth(1000 + (100 * level));
        // crystal.getComponent(CombatStatsComponent.class).setHealth(1000+(100*level));
        crystal.getComponent(CombatStatsComponent.class).setLevel(level + 1);
        screenShake();

        ServiceLocator.getAchievementHandler().getEvents().trigger(AchievementHandler.EVENT_CRYSTAL_UPGRADED,
                AchievementType.UPGRADES, 1);

    }

    public static void screenShake(){
        Entity cam = ServiceLocator.getEntityService().getNamedEntity("camera");
        CameraComponent cameraComp = cam.getComponent(CameraComponent.class);
        OrthographicCamera camera = (OrthographicCamera) cameraComp.getCamera();
        long currentGameTime = ServiceLocator.getTimeSource().getTime();
        final int[] i = {0};

        Timer time = new Timer();
        TimerTask shake = new TimerTask() {
            @Override
            public void run() {
                if (i[0] == 6){
                    /* Expand the map! */
                    Entity terrain = ServiceLocator.getEntityService().getNamedEntity("terrain");
                    terrain.getComponent(TerrainComponent.class).incrementMapLvl();
                    time.cancel();
                }
                if ( !interval) {
                    camera.translate(+5,5);
                    interval = true;
                } else {
                    camera.translate(-5,-5);
                    interval = false;
                }
                i[0]++;

            }
        };
        time.scheduleAtFixedRate(shake, 150, 150);
    }
    /**
     * Recover crystal health at dawn, day, and dusk
     */
    public static void recoverCrystalHealth(Entity crystal) {
        Timer time = new Timer();
        TimerTask recoverCrystal = new TimerTask() {
            @Override
            public void run() {
                DayNightCycleStatus status;
                if (ServiceLocator.getDayNightCycleService() != null) {
                    status = ServiceLocator.getDayNightCycleService().getCurrentCycleStatus();
                } else {
                    return;
                }
                // System.out.println(status);
                switch (status) {
                    case DAWN:
                    case DAY:
                    case DUSK:
                        CombatStatsComponent combatStatsComponent = crystal.getComponent(CombatStatsComponent.class);
                        int health = combatStatsComponent.getHealth();
                        combatStatsComponent.setHealth(health + 10);
                        break;
                    case NIGHT:
                    case NONE:
                        break;
                }
            }
        };
        time.scheduleAtFixedRate(recoverCrystal, 3000, 3000);
    }
}
