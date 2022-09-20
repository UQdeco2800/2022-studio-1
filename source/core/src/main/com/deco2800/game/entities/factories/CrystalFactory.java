package com.deco2800.game.entities.factories;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.deco2800.game.areas.terrain.TerrainComponent;
import com.deco2800.game.components.*;
import com.deco2800.game.entities.Entity;
import com.deco2800.game.entities.configs.CrystalConfig;
import com.deco2800.game.files.FileLoader;
import com.deco2800.game.physics.PhysicsLayer;
import com.deco2800.game.physics.PhysicsUtils;
import com.deco2800.game.physics.components.ColliderComponent;
import com.deco2800.game.physics.components.HitboxComponent;
import com.deco2800.game.physics.components.PhysicsComponent;
import com.deco2800.game.rendering.TextureRenderComponent;
import com.deco2800.game.services.DayNightCycleStatus;
import com.deco2800.game.services.ServiceLocator;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Factory to create crystal entity.
 *
 * <p>
 * Predefined crystal properties are loaded from a config stored as a json file
 * and should have
 * the properties stores in 'CrystalConfig'.
 */

public class CrystalFactory {
    private static final CrystalConfig crystalStats = FileLoader.readClass(CrystalConfig.class, "configs/crystal.json");

    /**
     * Creates a crystal entity.
     * 
     * @return entity
     */
    public static Entity createCrystal(String texture, String name) {
        Entity crystal = new Entity()
                .addComponent(new TextureRenderComponent(texture))
                .addComponent(new PhysicsComponent())
                .addComponent(new ColliderComponent().setLayer(PhysicsLayer.PLAYER))
                .addComponent(new HitboxComponent().setLayer(PhysicsLayer.PLAYER))
                .addComponent(new TouchAttackComponent(PhysicsLayer.PLAYER, 1.5f));

        crystal.addComponent(new CombatStatsComponent(crystalStats.health, crystalStats.baseAttack,
                crystalStats.defense, crystalStats.level, 1000))
                .addComponent(new HealthBarComponent(50, 10));
        crystal.setName("crystal");
        crystal.setCollectable(false);
        ServiceLocator.getEntityService().registerNamed(name, crystal);
        ServiceLocator.getEntityService().getNamedEntity("crystal").setTileGridPosition(new GridPoint2(60, 60));


        crystal.getComponent(PhysicsComponent.class).setBodyType(BodyDef.BodyType.StaticBody);
        crystal.getComponent(TextureRenderComponent.class).scaleEntity();
        // crystal.scaleHeight(2);
        PhysicsUtils.setScaledCollider(crystal, 1f, 0.5f);
        return crystal;
    }

    /**
     * Spawns Crystal outside of Game Area class
     *
     */
    public static void triggerCrystal(String texture) {
        Entity crystal = createCrystal(texture, "crystal2");
        ServiceLocator.getEntityService().registerNamed("crystal2", crystal);
        crystal.setPosition(new Vector2(60, 0));
    }

    /**
     * Upgrades the level of the Crystal changes its texture and increases its
     * maximum health
     */
    public static void upgradeCrystal() {
        Entity crystal = ServiceLocator.getEntityService().getNamedEntity("crystal");
        int level = crystal.getComponent(CombatStatsComponent.class).getLevel();
        // crystal.dispose();
        if (level == 1) {
            // crystal.addComponent(new
            // TextureRenderComponent("images/crystal_level2.png"));
            triggerCrystal("images/crystal_level2.png");
        } else if (level == 2) {
            ServiceLocator.getEntityService().getNamedEntity("crystal2").dispose();
            CrystalFactory.triggerCrystal("images/crystal_level3.png");
            // crystal.addComponent(new
            // TextureRenderComponent("images/crystal_level3.png"));
            ServiceLocator.getEntityService().unregisterNamed("crystal2");
        }
        if (level < 3) {
            // upgrading only increases max health and does not impact current health

            crystal.getComponent(CombatStatsComponent.class).setMaxHealth(1000 + (100 * level));
            // crystal.getComponent(CombatStatsComponent.class).setHealth(1000+(100*level));
            crystal.getComponent(CombatStatsComponent.class).setLevel(level + 1);

            /* Expand the map! */
            Entity terrain = ServiceLocator.getEntityService().getNamedEntity("terrain");
            terrain.getComponent(TerrainComponent.class).incrementMapLvl();

        } else
            System.out.println("Crystal has reached max level");
    }

    /**
     * Determine if crystal is being clicked
     */
    public static boolean crystalClicked(int screenX, int screenY) {
        //testing crystal upgrade on click
        Entity camera = ServiceLocator.getEntityService().getNamedEntity("camera");
        CameraComponent camComp = camera.getComponent(CameraComponent.class);
        Vector3 mousePos = camComp.getCamera().unproject(new Vector3(screenX, screenY, 0));
        Vector2 mousePosV2 = new Vector2(mousePos.x, mousePos.y);
        mousePosV2.x -= 0.5;
        mousePosV2.y -= 0.5;
        //System.out.println(mousePosV2);
        if (59.8 < mousePosV2.x && mousePosV2.x < 60.2) {
            if (-0.375 < mousePosV2.y && mousePosV2.y < 0.375) {
//                crystal.getComponent(CombatStatsComponent.class).upgrade();
                upgradeCrystal();
                return true;
            }
        }
        return false;
    }

    /**
     * Recover crystal health at dawn, day, and dusk
     */
    public static void recoverCrystalHealth(Entity crystal) {
        Timer time = new Timer();
        TimerTask recoverCrystal = new TimerTask() {
            @Override
            public void run() {
                DayNightCycleStatus status =  ServiceLocator.getDayNightCycleService().getCurrentCycleStatus();
                //System.out.println(status);
                switch (status){
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

    /**
     * Used for testing whether the function is exist
     * @param methodName
     * @return
     */
    public boolean hasMethod(String methodName) {
        switch(methodName) {
            case "createCrystal": 
                return true;
            case "triggerCrystal":
                return true;
            case "upgradeCrystal":
                return true;
            case "crystalClicked":
                return true;
            case "recoverCrystalHealth":
                return true;
            default:
                return false;
        }
    } 

    private CrystalFactory() {
        throw new IllegalStateException("Instantiating static util class");
    }
}
