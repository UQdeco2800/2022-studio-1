package com.deco2800.game.entities.factories;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.deco2800.game.achievements.AchievementType;
import com.deco2800.game.areas.terrain.TerrainComponent;
import com.deco2800.game.components.CameraComponent;
import com.deco2800.game.components.CombatStatsComponent;
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
            triggerCrystal("images/crystal2.png");
            player.getComponent(InventoryComponent.class).addGold(-500);
            PlayerStatsDisplay.updateItems();
            crystal.getComponent(CombatStatsComponent.class).setMaxHealth(1200);

        } else if (level == 2) {
            ServiceLocator.getEntityService().getNamedEntity("crystal2").dispose();
            triggerCrystal("images/crystal3.png");
            // crystal.addComponent(new
            // TextureRenderComponent("images/crystal_level3.png"));
            ServiceLocator.getEntityService().unregisterNamed("crystal2");
            player.getComponent(InventoryComponent.class).addGold(-1500);
            PlayerStatsDisplay.updateItems();
            crystal.getComponent(CombatStatsComponent.class).setMaxHealth(1500);

        }
        // upgrading only increases max health and does not impact current health
        // crystal.getComponent(CombatStatsComponent.class).setHealth(1000+(100*level));
        crystal.getComponent(CombatStatsComponent.class).setLevel(level + 1);
        screenShake();

        ServiceLocator.getAchievementHandler().getEvents().trigger(AchievementHandler.EVENT_CRYSTAL_UPGRADED,
                AchievementType.UPGRADES, 1);

    }

    /**
     * Triggers shaking effect of the in-game camera
     */
    public static void screenShake(){
        Entity cam = ServiceLocator.getEntityService().getNamedEntity("camera");
        CameraComponent cameraComp = cam.getComponent(CameraComponent.class);
        OrthographicCamera camera = (OrthographicCamera) cameraComp.getCamera();
        long currentGameTime = ServiceLocator.getTimeSource().getTime();
        Entity player = ServiceLocator.getEntityService().getNamedEntity("player");

        final int[] shakeNum = {0};
        final int[] power = {1};


        Timer time = new Timer();
        TimerTask shake = new TimerTask() {
            @Override
            public void run() {
                camera.update();
                //int power = (int) (new SecureRandom().nextInt(5));

                if (shakeNum[0] == 8){
                    /* Expand the map! */
                    Entity terrain = ServiceLocator.getEntityService().getNamedEntity("terrain");
                    terrain.getComponent(TerrainComponent.class).incrementMapLvl();
                    time.cancel();
                }
                if ( !interval) {
                    camera.translate(power[0], power[0]);
                    interval = true;
                } else {
                    camera.translate(-power[0],-power[0]);
                    interval = false;
                }
                power[0]++;
                shakeNum[0]++;

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
