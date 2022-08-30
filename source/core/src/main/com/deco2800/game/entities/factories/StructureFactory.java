package com.deco2800.game.entities.factories;

import com.badlogic.gdx.physics.box2d.BodyDef;
import com.deco2800.game.components.CombatStatsComponent;
import com.deco2800.game.components.HealthBarComponent;
import com.deco2800.game.components.TouchAttackComponent;
import com.deco2800.game.entities.Entity;
import com.deco2800.game.entities.configs.BaseEntityConfig;
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
public class StructureFactory {
  private static final StructureConfig configs =
      FileLoader.readClass(StructureConfig.class, "configs/structure.json");


  /**
   * Creates a wall entity.
   *
   * @return specialised Wall entity
   */
  public static Entity createWall() {
    Entity wall = createBaseStructure("images/wall-right.png");
    BaseEntityConfig config = configs.wall; //For some reason it errors if I use configs.wall :o

    wall.addComponent(new CombatStatsComponent(config.health, config.baseAttack))
            .addComponent(new HealthBarComponent(75, 10));
    return wall;
  }

  /**
   * Creates a tower1 entity.
   *
   * //@param target entity to chase
   * @return entity
   */
  public static Entity createTower1() {
    Entity tower1 = createBaseStructure("images/mini_tower.png");
    BaseEntityConfig config = configs.tower1;

    tower1.addComponent(new CombatStatsComponent(config.health, config.baseAttack))
            .addComponent(new HealthBarComponent(75, 10));
    return tower1;
  }

  /**
   * Creates a Stone Quarry entity
   *
   * @return stone quarry entity
   */
  public static Entity createStoneQuarry() {

    AnimationRenderComponent bul_animator = new AnimationRenderComponent(ServiceLocator.getResourceService().getAsset("images/anim_demo/res_bul_1.atlas", TextureAtlas.class));
    bul_animator.addAnimation("bul_1", 0.5f, Animation.PlayMode.LOOP);

    Entity stoneQuarry = createBaseStructure_forAnim("images/anim_demo/res_bul_1.atlas");
    BaseEntityConfig config = configs.stoneQuarry;

    stoneQuarry.addComponent(new CombatStatsComponent(config.health, config.baseAttack))
            .addComponent(bul_animator)
            .addComponent(new HealthBarComponent(75, 10));
    stoneQuarry.getComponent(AnimationRenderComponent.class).scaleEntity();
    bul_animator.startAnimation("bul_1");
    return stoneQuarry;
  }

  /**
   * Creates a generic Structure to be used as a base entity by more specific Structure creation methods.
   *
   * @return entity
   */
  private static Entity createBaseStructure(String texture) {
    /* //This is where the defence (aiming and shooting) tasks will be added
    AITaskComponent aiComponent =
        new AITaskComponent()
            .addTask(new WanderTask(new Vector2(2f, 2f), 2f))
            .addTask(new ChaseTask(target, 10, 3f, 4f));*/
    Entity structure =
        new Entity()
                .addComponent(new TextureRenderComponent(texture))
            .addComponent(new PhysicsComponent())
            .addComponent(new ColliderComponent().setLayer(PhysicsLayer.OBSTACLE))
            .addComponent(new HitboxComponent().setLayer(PhysicsLayer.NPC))
            .addComponent(new TouchAttackComponent(PhysicsLayer.NPC, 1.5f));
            //.addComponent(aiComponent);

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
            //.addComponent(aiComponent);

    structure.getComponent(PhysicsComponent.class).setBodyType(BodyDef.BodyType.StaticBody);
    PhysicsUtils.setScaledCollider(structure, 0.9f, 0.4f);
    return structure;
  }


  private StructureFactory() {
    throw new IllegalStateException("Instantiating static util class");
  }
}
