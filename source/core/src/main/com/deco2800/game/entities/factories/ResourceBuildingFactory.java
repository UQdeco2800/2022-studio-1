package com.deco2800.game.entities.factories;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.deco2800.game.components.CameraComponent;
import com.deco2800.game.components.CombatStatsComponent;
import com.deco2800.game.components.HealthBarComponent;
import com.deco2800.game.components.TouchAttackComponent;
import com.deco2800.game.components.infrastructure.ResourceBuilding;
import com.deco2800.game.components.player.PlayerStatsDisplay;
import com.deco2800.game.entities.Entity;
import com.deco2800.game.entities.configs.BaseEntityConfig;
import com.deco2800.game.entities.configs.ResourceBuildingConfig;
import com.deco2800.game.entities.configs.StructureConfig;
import com.deco2800.game.files.FileLoader;
import com.deco2800.game.physics.PhysicsLayer;
import com.deco2800.game.physics.PhysicsUtils;
import com.deco2800.game.physics.components.ColliderComponent;
import com.deco2800.game.physics.components.HitboxComponent;
import com.deco2800.game.physics.components.PhysicsComponent;
import com.deco2800.game.rendering.TextureRenderComponent;
import com.deco2800.game.services.ServiceLocator;
import com.deco2800.game.rendering.AnimationRenderComponent;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;

import java.util.Map;
import java.util.Objects;
import java.util.SortedMap;

/**
 * Factory to create structure entities with predefined components.
 *
 * <p>Each structure entity type should have a creation method that returns a corresponding entity.
 * Predefined entity properties can be loaded from configs stored as json files which are defined in
 * "StructureConfigs".
 *
 * <p>If needed, this factory can be separated into more specific factories for entities with
 * similar characteristics.
 */
public class ResourceBuildingFactory {
    private static final ResourceBuildingConfig configs =
            FileLoader.readClass(ResourceBuildingConfig.class, "configs/resourceBuilding.json");

    /**
     * Creates a Stone Quarry entity
     *
     * @return stone quarry entity
     */
    public static Entity createStoneQuarry() {

        AnimationRenderComponent bul_animator = new AnimationRenderComponent(ServiceLocator.getResourceService().getAsset("images/anim_demo/stonequarr.atlas", TextureAtlas.class));
        bul_animator.addAnimation("stqu", 0.5f, Animation.PlayMode.LOOP);

        Entity stoneQuarry = createBaseStructure_forAnim("images/anim_demo/stonequarr.atlas");
        BaseEntityConfig config = configs.stoneQuarry;

        stoneQuarry.addComponent(new CombatStatsComponent(config.health, config.baseAttack))
                .addComponent(bul_animator)
                .addComponent(new HealthBarComponent(75, 10));
        stoneQuarry.getComponent(AnimationRenderComponent.class).scaleEntity();
        bul_animator.startAnimation("stqu");
        stoneQuarry.setScale(1.5f, 1.2f);
        return stoneQuarry;
    }

    /**
     * Creates a Wood Quarry entity
     *
     * @return Wood quarry entity
     */
    public static Entity createWoodCutter() {

        Entity woodQuarry = createBaseStructure("images/anim_demo/woodresourcebuilding.png");
        BaseEntityConfig config = configs.woodCutter;

        woodQuarry.addComponent(new CombatStatsComponent(config.health, config.baseAttack))
                .addComponent(new HealthBarComponent(75, 10));
        woodQuarry.getComponent(TextureRenderComponent.class).scaleEntity();
        woodQuarry.setScale(1.5f, 1.2f);
        return woodQuarry;
    }

    /**
     * Creates a generic Structure to be used as a base entity by more specific Structure creation methods.
     * @param texture image representation for created structure
     * @return structure entity
     */
    public static Entity createBaseStructure(String texture) {
        Entity structure =
                new Entity()
                        .addComponent(new TextureRenderComponent(texture))
                        .addComponent(new PhysicsComponent())
                        .addComponent(new ColliderComponent().setLayer(PhysicsLayer.OBSTACLE))
                        .addComponent(new HitboxComponent().setLayer(PhysicsLayer.NPC))
                        .addComponent(new TouchAttackComponent(PhysicsLayer.NPC, 1.5f));

        structure.getComponent(PhysicsComponent.class).setBodyType(BodyDef.BodyType.StaticBody);
        structure.getComponent(TextureRenderComponent.class).scaleEntity();
        PhysicsUtils.setScaledCollider(structure, 0.9f, 0.4f);
        return structure;
    }

    private static Entity createBaseStructure_forAnim(String texture) {
        Entity structure =
                new Entity()
                        .addComponent(new PhysicsComponent())
                        .addComponent(new ColliderComponent().setLayer(PhysicsLayer.OBSTACLE))
                        .addComponent(new HitboxComponent().setLayer(PhysicsLayer.NPC))
                        .addComponent(new TouchAttackComponent(PhysicsLayer.NPC, 1.5f));

        structure.getComponent(PhysicsComponent.class).setBodyType(BodyDef.BodyType.StaticBody);
        PhysicsUtils.setScaledCollider(structure, 0.9f, 0.4f);
        structure.setScale(1.2f, 1.5f);
        return structure;
    }

    /**
     * Builds a structure at mouse position
     */
    public static void triggerBuildEvent(String name, SortedMap<String, Rectangle> structureRects) {
        Entity camera = ServiceLocator.getEntityService().getNamedEntity("camera");
        CameraComponent camComp = camera.getComponent(CameraComponent.class);
        Vector3 mousePos = camComp.getCamera().unproject(new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0));
        Vector2 mousePosV2 = new Vector2(mousePos.x, mousePos.y);
        mousePosV2.x -= 0.5;
        mousePosV2.y -= 0.5;
        String entityName = String.valueOf(ServiceLocator.getTimeSource().getTime());
        entityName = name + entityName;

        if (Objects.equals(name, "stoneQuarry")) {
            ServiceLocator.getEntityService().registerNamed(entityName, createStoneQuarry());
            ServiceLocator.getEntityService().getNamedEntity(entityName).setPosition(mousePosV2);
            Rectangle rectangle = new Rectangle(mousePosV2.x, mousePosV2.y, 1, 1);
            structureRects.put(entityName, rectangle);
        }
    }

    /**
     * Checks if a structure on the map has been clicked. If it has been clicked then that structure gets removed from the game
     * @param screenX The x coordinate, origin is in the upper left corner
     * @param screenY The y coordinate, origin is in the upper left corner
     * @return true if the point (screenX, screenY) is clear of structures else return false
     *
     */
    public static boolean[] handleClickedStructures(int screenX, int screenY, SortedMap<String, Rectangle> structureRects, boolean resourceBuildState, boolean buildEvent) {
        String clickedStructure = "";
        boolean isClear;
        boolean anyStructureHit = false;
        Entity camera = ServiceLocator.getEntityService().getNamedEntity("camera");
        CameraComponent camComp = camera.getComponent(CameraComponent.class);
        Vector3 mousePos = camComp.getCamera().unproject(new Vector3(screenX, screenY, 0));
        Vector2 mousePosV2 = new Vector2(mousePos.x, mousePos.y);
        for (Map.Entry<String, Rectangle> es : structureRects.entrySet()){
            if (es.getValue().contains(mousePosV2)) {
                clickedStructure = es.getKey();
                if (clickedStructure.contains("stoneQuarry")) {
                    PlayerStatsDisplay.updateStoneCountUI();
                    resourceBuildState = false;
                    return new boolean[]{false, resourceBuildState, buildEvent};
                } else {
                    ServiceLocator.getEntityService().getNamedEntity(es.getKey()).dispose();
                    anyStructureHit = true;
                }
            }
        }
        if (anyStructureHit) {
            buildEvent = false;
            isClear = false;

            structureRects.remove(clickedStructure);
        } else {
            isClear = true;
        }
        return new boolean[]{isClear, resourceBuildState, buildEvent};
    }

    /**
     * Toggles the build state of the player
     */
    public static boolean toggleBuildState(boolean buildState) {
        buildState = !buildState;
        return  buildState;
    }

    /**
     * Toggles resource building placement mode
     */
    public static boolean toggleResourceBuildState(boolean resourceBuildState) {
        resourceBuildState = !resourceBuildState;
        return resourceBuildState;
    }


    private ResourceBuildingFactory() {
        throw new IllegalStateException("Instantiating static util class");
    }
}
