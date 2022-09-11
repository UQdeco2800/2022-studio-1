package com.deco2800.game.entities.factories;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;
import com.deco2800.game.ai.tasks.AITaskComponent;
import com.deco2800.game.components.CombatStatsComponent;
import com.deco2800.game.components.HealthBarComponent;
import com.deco2800.game.components.TouchAttackComponent;
import com.deco2800.game.components.npc.GhostAnimationController;
import com.deco2800.game.components.tasks.ChaseTask;
import com.deco2800.game.components.tasks.RangedMovementTask;
import com.deco2800.game.components.tasks.WanderTask;
import com.deco2800.game.entities.Entity;
import com.deco2800.game.entities.Enemy;
import com.deco2800.game.entities.configs.BaseEntityConfig;
import com.deco2800.game.entities.configs.EnemyConfig;
import com.deco2800.game.entities.configs.GhostKingConfig;
import com.deco2800.game.entities.configs.NPCConfigs;
import com.deco2800.game.files.FileLoader;
import com.deco2800.game.physics.PhysicsLayer;
import com.deco2800.game.physics.PhysicsUtils;
import com.deco2800.game.physics.components.ColliderComponent;
import com.deco2800.game.physics.components.HitboxComponent;
import com.deco2800.game.physics.components.PhysicsComponent;
import com.deco2800.game.physics.components.PhysicsMovementComponent;
import com.deco2800.game.rendering.AnimationRenderComponent;
import com.deco2800.game.rendering.TextureRenderComponent;
import com.deco2800.game.services.ServiceLocator;

/**
 * Factory to create non-playable character (NPC) entities with predefined components.
 *
 * <p>Each NPC entity type should have a creation method that returns a corresponding entity.
 * Predefined entity properties can be loaded from configs stored as json files which are defined in
 * "NPCConfigs".
 *
 * <p>If needed, this factory can be separated into more specific factories for entities with
 * similar characteristics.
 */
public class NPCFactory {
  private static final NPCConfigs configs =
      FileLoader.readClass(NPCConfigs.class, "configs/NPCs.json");

  /**
   * Creates a ghost entity.
   *
   * @param target entity to chase
   * @return entity
   */
  public static Entity createGhost(Entity target) {
    Entity ghost = createBaseEnemy(target);
    BaseEntityConfig config = configs.ghost;

    AnimationRenderComponent animator =
        new AnimationRenderComponent(
            ServiceLocator.getResourceService().getAsset("images/ghost.atlas", TextureAtlas.class));
    animator.addAnimation("angry_float", 0.1f, Animation.PlayMode.LOOP);
    animator.addAnimation("float", 0.1f, Animation.PlayMode.LOOP);

    ghost
        .addComponent(new CombatStatsComponent(config.health, config.baseAttack))
        .addComponent(animator)
            .addComponent(new HealthBarComponent(100, 10))
        .addComponent(new GhostAnimationController());

    ghost.getComponent(AnimationRenderComponent.class).scaleEntity();
    return ghost;
  }

  /**
   * Creates a ghost king entity.
   *
   * @param target entity to chase
   * @return entity
   */
  public static Entity createGhostKing(Entity target) {
    Entity ghostKing = createBaseEnemy(target);
    GhostKingConfig config = configs.ghostKing;

    AnimationRenderComponent animator =
        new AnimationRenderComponent(
            ServiceLocator.getResourceService()
                .getAsset("images/ghostKing.atlas", TextureAtlas.class));
    animator.addAnimation("float", 0.1f, Animation.PlayMode.LOOP);
    animator.addAnimation("angry_float", 0.1f, Animation.PlayMode.LOOP);

    ghostKing
        .addComponent(new CombatStatsComponent(config.health, config.baseAttack))
        .addComponent(animator)
            .addComponent(new HealthBarComponent(100, 10))
        .addComponent(new GhostAnimationController());

    ghostKing.getComponent(AnimationRenderComponent.class).scaleEntity();
    return ghostKing;
  }

  /**
   * Creates a pirate Crab entity
   *
   * @param target entity to chase
   * @return Entity
   */
  public static Entity createPirateCrabEnemy(Entity target) {
    Entity pirateCrabEnemy = createBaseEnemy(target);
    EnemyConfig config = configs.pirateCrab;

    TextureRenderComponent textureRenderComponent = new TextureRenderComponent("images/pirate_crab_SW.png");

    // Add combat stats, health bar and texture renderer to the pirate crab entity
    pirateCrabEnemy
            .addComponent(new CombatStatsComponent(config.health, config.baseAttack))
            .addComponent(new HealthBarComponent(100, 10))
            .addComponent(textureRenderComponent);


    pirateCrabEnemy.getComponent(TextureRenderComponent.class).scaleEntity();
    ServiceLocator.getEntityService().registerNamed("pirateCrabEnemy@" + pirateCrabEnemy.getId(), pirateCrabEnemy);

    return pirateCrabEnemy;
  }

  public static Entity createElectricEelEnemy(Entity target, Entity crystal) {
    Entity ElectricEelEnemy = createBaseRangeNPC(target, crystal);
    EnemyConfig config = configs.ElectricEel;
    TextureRenderComponent textureRenderComponent = new TextureRenderComponent("images/Eel_Bright_SW.png");

    ElectricEelEnemy
            .addComponent(new CombatStatsComponent(config.health, config.baseAttack))
            .addComponent(new HealthBarComponent(100, 10))
            .addComponent(textureRenderComponent);

    ElectricEelEnemy.getComponent(TextureRenderComponent.class).scaleEntity();
    ServiceLocator.getEntityService().registerNamed("electricEelEnemy@" + ElectricEelEnemy.getId(), ElectricEelEnemy);

    return ElectricEelEnemy;
  }

//  public static Entity createStarFish(Entity target) {
//    Entity starFish = createBaseRangeNPC(target);
//    EnemyConfig config = configs.starfish;
//
//    /** AnimationRenderComponent animator =
//            new AnimationRenderComponent(
//                    ServiceLocator.getResourceService()
//                            .getAsset("images/ghostKing.atlas", TextureAtlas.class));
//    animator.addAnimation("float", 0.1f, Animation.PlayMode.LOOP);
//    animator.addAnimation("angry_float", 0.1f, Animation.PlayMode.LOOP);
//
//    starFish
//            .addComponent(new CombatStatsComponent(config.health, config.baseAttack))
//            .addComponent(animator)
//            .addComponent(new HealthBarComponent(100, 10))
//            .addComponent(new GhostAnimationController()); */
//
//    starFish.getComponent(AnimationRenderComponent.class).scaleEntity();
//    return starFish;
//  }


  /**
   * Creates a generic NPC to be used as a base entity by more specific NPC creation methods.
   *
   * @return entity
   */
  private static Entity createBaseNPC() {
    Entity npc =
        new Entity()
            .addComponent(new PhysicsComponent())
            .addComponent(new PhysicsMovementComponent())
            .addComponent(new ColliderComponent())
            .addComponent(new HitboxComponent().setLayer(PhysicsLayer.NPC));

    PhysicsUtils.setScaledCollider(npc, 0.9f, 0.4f);
    return npc;
  }

  /**
   * For luke
   * */
  // TODO: Luke make this look better and fix up NPC != enemy
  private static Enemy createBaseEnemy(Entity target) {
    AITaskComponent aiComponent =
            new AITaskComponent()
                    .addTask(new WanderTask(new Vector2(2f, 2f), 2f))
                    .addTask(new ChaseTask(target, 10, 3f, 4f));
    Enemy enemy =
            (Enemy) new Enemy()
                    .addComponent(new PhysicsComponent())
                    .addComponent(new PhysicsMovementComponent())
                    .addComponent(new ColliderComponent())
                    .addComponent(new HitboxComponent().setLayer(PhysicsLayer.NPC))
                    .addComponent(new TouchAttackComponent(PhysicsLayer.PLAYER, 1.5f))
                    .addComponent(aiComponent);

    PhysicsUtils.setScaledCollider(enemy, 0.9f, 0.4f);
    return enemy;
  }

  /**
   * Creates a Ranged NPC to be used as an enemy. This uses different AI component values
   * to create different enemy behaviour
   *
   * @return entity
   */
  private static Enemy createBaseRangeNPC(Entity target, Entity crystal) {
    //Vector2 RangeHitbox = new Vector2(2f, 1f);
    AITaskComponent aiComponent =
            new AITaskComponent()
                    .addTask(new WanderTask(new Vector2(3f, 3f), 2f))
                    .addTask(new RangedMovementTask(crystal, 20, 2f, 4f, 6f))
                    .addTask(new RangedMovementTask(target, 10, 2f, 4f, 6f));
    Enemy enemy =
            (Enemy) new Enemy()
                    .addComponent(new PhysicsComponent())
                    .addComponent(new PhysicsMovementComponent())
                    .addComponent(new ColliderComponent())
                    .addComponent(new HitboxComponent().setLayer(PhysicsLayer.RangeNPC))
                    .addComponent(new TouchAttackComponent(PhysicsLayer.PLAYER))
                    .addComponent(aiComponent);

    PhysicsUtils.setScaledCollider(enemy, 0.9f, 0.4f);
    return enemy;
  }

  private NPCFactory() {
    throw new IllegalStateException("Instantiating static util class");
  }
}
