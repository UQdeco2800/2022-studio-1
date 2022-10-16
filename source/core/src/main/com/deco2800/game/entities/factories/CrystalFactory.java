package com.deco2800.game.entities.factories;

import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.deco2800.game.achievements.AchievementType;
import com.deco2800.game.areas.terrain.TerrainComponent;
import com.deco2800.game.components.*;
import com.deco2800.game.components.camera.CameraActions;
import com.deco2800.game.components.maingame.MainGameBuildingInterface;
import com.deco2800.game.components.maingame.MainGameNpcInterface;
import com.deco2800.game.components.player.InventoryComponent;
import com.deco2800.game.components.player.PlayerStatsDisplay;
import com.deco2800.game.entities.Entity;
import com.deco2800.game.entities.configs.CrystalConfig;
import com.deco2800.game.files.FileLoader;
import com.deco2800.game.files.SaveGame;
import com.deco2800.game.physics.PhysicsLayer;
import com.deco2800.game.physics.PhysicsUtils;
import com.deco2800.game.physics.components.ColliderComponent;
import com.deco2800.game.physics.components.HitboxComponent;
import com.deco2800.game.physics.components.PhysicsComponent;
import com.deco2800.game.rendering.TextureRenderComponent;
import com.deco2800.game.services.AchievementHandler;
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

        crystal.getComponent(PhysicsComponent.class).setBodyType(BodyDef.BodyType.StaticBody);
        crystal.getComponent(TextureRenderComponent.class).scaleEntity();
        crystal.scaleHeight(15);
        PhysicsUtils.setScaledCollider(crystal, 1f, 0.5f);
        return crystal;
    }


    private CrystalFactory() {
        throw new IllegalStateException("Instantiating static util class");
    }
}
