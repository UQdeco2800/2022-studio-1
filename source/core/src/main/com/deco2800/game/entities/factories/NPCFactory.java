package com.deco2800.game.entities.factories;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.deco2800.game.ai.tasks.AITaskComponent;
import com.deco2800.game.components.CombatStatsComponent;
import com.deco2800.game.components.npc.*;
import com.deco2800.game.components.HealthBarComponent;
import com.deco2800.game.components.TouchAttackComponent;
import com.deco2800.game.components.player.AnimationController;
import com.deco2800.game.components.tasks.*;
import com.deco2800.game.entities.Entity;
import com.deco2800.game.entities.configs.*;
import com.deco2800.game.entities.Enemy;
import com.deco2800.game.entities.configs.EnemyConfig;
import com.deco2800.game.entities.configs.NPCConfigs;
import com.deco2800.game.events.EventHandler;
import com.deco2800.game.files.FileLoader;
import com.deco2800.game.physics.PhysicsLayer;
import com.deco2800.game.physics.PhysicsUtils;
import com.deco2800.game.physics.components.ColliderComponent;
import com.deco2800.game.physics.components.HitboxComponent;
import com.deco2800.game.physics.components.PhysicsComponent;
import com.deco2800.game.physics.components.PhysicsMovementComponent;
import com.deco2800.game.rendering.AnimationRenderComponent;
import com.deco2800.game.rendering.TextureRenderComponent;
import com.deco2800.game.services.AchievementHandler;
import com.deco2800.game.services.ServiceLocator;

import java.security.SecureRandom;

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
   * Creates a pirate Crab entity
   *
   * @param target entity to chase
   * @return Entity
   */
  public static Entity createPirateCrabEnemy(Entity target) {
    Entity pirateCrabEnemy = createBaseEnemy(target);
    EnemyConfig config = configs.pirateCrab;

    AnimationRenderComponent animator = new AnimationRenderComponent(
            ServiceLocator.getResourceService().getAsset("images/crab_animations/crab_animation.atlas", TextureAtlas.class));
    animator.addAnimation("frame", 0.1f, Animation.PlayMode.LOOP);

    // Add combat stats, health bar and texture renderer to the pirate crab entity
    pirateCrabEnemy
            .addComponent(new CombatStatsComponent(config.health, config.baseAttack))
            .addComponent(new HealthBarComponent(100, 10))
            .addComponent(animator)
            .addComponent(new ContinuousAttackComponent(3000, 3000))
            .addComponent(new EntityClassification(EntityClassification.NPCClassification.ENEMY));

    return pirateCrabEnemy;
  }

  //Kept code to change back to texture if need be
  public static Entity createElectricEelEnemy(Entity target, Entity crystal) {
    Entity ElectricEelEnemy = createBaseRangeNPC(target, crystal);
    EnemyConfig config = configs.ElectricEel;

    AnimationRenderComponent animator =
            new AnimationRenderComponent(
                    ServiceLocator.getResourceService().getAsset("images/eel_animations/eel.atlas", TextureAtlas.class));
    animator.addAnimation("fl", 0.1f, Animation.PlayMode.LOOP);
    animator.addAnimation("fr", 0.1f, Animation.PlayMode.LOOP);
    animator.addAnimation("bl", 0.1f, Animation.PlayMode.LOOP);
    animator.addAnimation("br", 0.1f, Animation.PlayMode.LOOP);

    ElectricEelEnemy
            .addComponent(new CombatStatsComponent(config.health, 0))
            .addComponent(new HealthBarComponent(100, 10))
            .addComponent(animator)
            //.addComponent(textureRenderComponent);
            .addComponent(new AnimationController())
            .addComponent(new EntityClassification(EntityClassification.NPCClassification.ENEMY));

    ElectricEelEnemy.setName("ElectricEel");
    ElectricEelEnemy.setCollectable(false);

    PhysicsUtils.setScaledCollider(ElectricEelEnemy, 12f, 12f);
    ElectricEelEnemy.getComponent(ColliderComponent.class).setDensity(1.5f);
    ElectricEelEnemy.getComponent(AnimationRenderComponent.class).startAnimation("fl");
    ElectricEelEnemy.getComponent(AnimationRenderComponent.class).scaleEntity();
    ElectricEelEnemy.setScale(12f, 12f);

    return ElectricEelEnemy;
  }

  /**
   * Creates a melee boss entity
   *
   * @param target entity to chase
   * @return Entity
   */
  public static Entity createMeleeBoss(Entity target) {
    Entity boss = createBaseEnemy(target);
    MeleeBossConfig config = configs.meleeBossEnemy;
    config.speed = new Vector2(0.3f, 0.3f);
    AnimationRenderComponent animator = new AnimationRenderComponent(
            ServiceLocator.getResourceService().getAsset("images/final_boss_animations/final_boss.atlas", TextureAtlas.class));
    animator.addAnimation("boss_frame", 0.1f, Animation.PlayMode.LOOP);
    animator.addAnimation("boss_side2", 0.1f, Animation.PlayMode.LOOP);

    // Add combat stats, health bar and texture renderer to the pirate crab entity
    boss
            .addComponent(new CombatStatsComponent(config.health, config.baseAttack))
            .addComponent(new HealthBarComponent(100, 10))
            .addComponent(animator)
            .addComponent(new TouchAttackComponent(PhysicsLayer.NPC, 0f))
            .addComponent(new EffectNearBy(true, true, true))
            .addComponent(new ContinuousAttackComponent(3000, 3000))
            .addComponent(new BossAnimationController());

    boss.setScale(19, 14);
//    boss.getComponent(AnimationRenderComponent.class).scaleEntity();
    boss.getComponent(PhysicsMovementComponent.class).setOriginalSpeed(config.speed);
    boss.getComponent(EffectNearBy.class).enableSpeed();
    boss.getComponent(EffectNearBy.class).enableRegen();
    boss.getComponent(EffectNearBy.class).enableAttackDamageBuff();
    boss.getComponent(EntityClassification.class).setEntityType(EntityClassification.NPCClassification.BOSS);
    
    return boss;
  }

// Create starfish as a new entity
  public static Entity createStarFishEnemy(Entity target, Entity crystal) {
    Entity ninjaStarfish = createBaseRangeNPC(target, crystal);
    EnemyConfig config = configs.ninjaStarfish;
    //TextureRenderComponent textureRenderComponent = new TextureRenderComponent("images/starfish.png");

    AnimationRenderComponent animator =
            new AnimationRenderComponent(
                    ServiceLocator.getResourceService().getAsset("images/starfish_animation/starfish.atlas", TextureAtlas.class));
    animator.addAnimation("fl", 0.1f, Animation.PlayMode.LOOP);
    animator.addAnimation("fr", 0.1f, Animation.PlayMode.LOOP);
    animator.addAnimation("bl", 0.1f, Animation.PlayMode.LOOP);
    animator.addAnimation("br", 0.1f, Animation.PlayMode.LOOP);


    ninjaStarfish
            .addComponent(new CombatStatsComponent(config.health, config.baseAttack))
            .addComponent(new HealthBarComponent(100, 10))
            .addComponent(animator)
            //.addComponent(textureRenderComponent);
            .addComponent(new AnimationController())
            .addComponent(new EntityClassification(EntityClassification.NPCClassification.ENEMY));

    ServiceLocator.getEntityService().registerNamed("NinjaStarfish",  ninjaStarfish);
    PhysicsUtils.setScaledCollider(ninjaStarfish, 12f, 12f);
    ninjaStarfish.getComponent(ColliderComponent.class).setDensity(1.5f);
    ninjaStarfish.getComponent(AnimationRenderComponent.class).startAnimation("fl");
    ninjaStarfish.getComponent(AnimationRenderComponent.class).scaleEntity();
    ninjaStarfish.setScale(12f, 12f);

    return ninjaStarfish;
  }

  /**
   * For luke
   * */
  // TODO: Luke make this look better and fix up NPC != enemy
  private static Enemy createBaseEnemy(Entity target) {
    AITaskComponent aiComponent =
            new AITaskComponent()
                    .addTask(new WanderTask(new Vector2(2f, 2f), 2f))
                    .addTask(new MeleePursueTask(target))
                    .addTask(new MeleeAvoidObstacleTask(target));
    Enemy enemy =
            (Enemy) new Enemy()
                    .addComponent(new PhysicsComponent())
                    .addComponent(new PhysicsMovementComponent())
                    .addComponent(new ColliderComponent())
                    .addComponent(new HitboxComponent().setLayer(PhysicsLayer.NPC))
                    .addComponent(new TouchAttackComponent(PhysicsLayer.PLAYER, 1.5f))
                    .addComponent(new EntityClassification(EntityClassification.NPCClassification.ENEMY))
                    .addComponent(aiComponent);

    enemy.setCreationMethod(Thread.currentThread().getStackTrace()[2].getMethodName());
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
                    .addTask(new RangedMovementTask(crystal, 10, 2f, 50f, 60f))
                    .addTask(new RangedMovementTask(target, 20, 2f, 50f, 60f))
                    .addTask(new MeleeAvoidObstacleTask(target))
                    .addTask(new ShootTask(target, 30, 50f, 60f))
                    .addTask(new ShootTask(crystal, 30, 50f, 60f));
    Enemy enemy =
            (Enemy) new Enemy()
                    .addComponent(new PhysicsComponent())
                    .addComponent(new PhysicsMovementComponent())
                    .addComponent(new ColliderComponent())
                    .addComponent(new HitboxComponent().setLayer(PhysicsLayer.RangeNPC))
                    .addComponent(new TouchAttackComponent(PhysicsLayer.RangeNPC))
                    .addComponent(aiComponent);

    enemy.setCreationMethod(Thread.currentThread().getStackTrace()[2].getMethodName());
    PhysicsUtils.setScaledCollider(enemy, 0.9f, 0.4f);
    return enemy;
  }

  /**
   * Creates a generic NPC to be used as a base entity by more specific NPC creation methods.
   *
   * @return entity
   */

  public static Entity createBaseNPC() {
        AITaskComponent aiComponent =
            new AITaskComponent()
                .addTask(new WanderTask(new Vector2(30f, 30f), 2f));
        Entity npc =
            new Entity()
                .addComponent(new PhysicsComponent())
                .addComponent(new PhysicsMovementComponent())
                .addComponent(new ColliderComponent())
                .addComponent(new HitboxComponent().setLayer(PhysicsLayer.NPC))
                .addComponent(new EntityClassification(EntityClassification.NPCClassification.NPC))

                .addComponent(new TextureRenderComponent("images/npcs/NPC-V2.1.png"))
                .addComponent(aiComponent);

        npc.setCollectable(false);

        PhysicsUtils.setScaledCollider(npc, 0.9f, 0.4f);
        return npc;
      }

      /**
       * Creates a new special NPC.
       * @return special NPC.
       */
      public static Entity createSpecialNPC() {
        Entity NPC = createBaseNPC();
        NPC.setName("SpecialNPC");
        AnimationRenderComponent animator =
                 new AnimationRenderComponent(
                         ServiceLocator.getResourceService().getAsset("images/npc_animations/npc.atlas", TextureAtlas.class));
        animator.addAnimation("NPC", 0.1f, Animation.PlayMode.LOOP);
        NPC.addComponent(animator);
        NPC.getComponent(AnimationRenderComponent.class).scaleEntity();
        NPC.getComponent(AnimationRenderComponent.class).startAnimation("NPC");
       // NPC.addComponent(new TextureRenderComponent("images/npc1.png"));
        NPC.setScale(7f,7f);

          return NPC;
      }

      /**
       * Creates a new normal NPC.
       * @return normal NPC
       */
    public static Entity createNormalNPC() {
          String[] NPC_textures = { "images/npcs/NPC-V2.1.png",
              "images/npcs/NPC-V2.2.png" };

//      int index = (int) ((Math.random() * (NPC_textures.length)));
      int index = (int) (new SecureRandom().nextInt(NPC_textures.length));
      Entity NPC = createBaseNPC();
      NPC.addComponent(new TextureRenderComponent(NPC_textures[index]));
      NPC.setName("NormalNPC");
      NPC.getComponent(TextureRenderComponent.class).scaleEntity();

      return NPC;
    }

  private NPCFactory() {
    throw new IllegalStateException("Instantiating static util class");
  }
}
