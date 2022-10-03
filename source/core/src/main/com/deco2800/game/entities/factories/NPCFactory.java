package com.deco2800.game.entities.factories;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.deco2800.game.ai.tasks.AITaskComponent;
import com.deco2800.game.components.CombatStatsComponent;
import com.deco2800.game.components.npc.*;
import com.deco2800.game.components.HealthBarComponent;
import com.deco2800.game.components.TouchAttackComponent;
import com.deco2800.game.components.tasks.*;
import com.deco2800.game.components.tasks.RangedMovementTask;
import com.deco2800.game.components.tasks.WanderTask;
import com.deco2800.game.entities.Entity;
import com.deco2800.game.entities.configs.*;
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
import com.deco2800.game.rendering.DayNightCycleComponent;
import com.deco2800.game.rendering.TextureRenderComponent;
import com.deco2800.game.services.ServiceLocator;

import java.util.HashMap;
import java.util.Map;

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
            .addComponent(new ContinuousAttackComponent(3000, 3000))
            .addComponent(textureRenderComponent)
            .addComponent(new EntityClassification(EntityClassification.NPCClassification.ENEMY));

    pirateCrabEnemy.getComponent(TextureRenderComponent.class).scaleEntity();
    ServiceLocator.getEntityService().registerNamed("pirateCrabEnemy@" + pirateCrabEnemy.getId(), pirateCrabEnemy);


    return pirateCrabEnemy;
  }

  //Kept code to change back to texture if need be
  public static Entity createElectricEelEnemy(Entity target, Entity crystal) {
    Entity ElectricEelEnemy = createBaseRangeNPC(target, crystal);
    EnemyConfig config = configs.ElectricEel;
    //TextureRenderComponent textureRenderComponent = new TextureRenderComponent("images/Eel_Bright_SW.png");

    AnimationRenderComponent animator =
            new AnimationRenderComponent(
                    ServiceLocator.getResourceService().getAsset("images/eel_animations/eel.atlas", TextureAtlas.class));
    animator.addAnimation("fl", 0.1f, Animation.PlayMode.LOOP);
    animator.addAnimation("fr", 0.1f, Animation.PlayMode.LOOP);
    animator.addAnimation("bl", 0.1f, Animation.PlayMode.LOOP);
    animator.addAnimation("br", 0.1f, Animation.PlayMode.LOOP);

    ElectricEelEnemy
            .addComponent(new CombatStatsComponent(config.health, config.baseAttack))
            .addComponent(new HealthBarComponent(100, 10))
            .addComponent(animator)
            //.addComponent(textureRenderComponent);
            .addComponent(new GhostAnimationController())
            .addComponent(new EntityClassification(EntityClassification.NPCClassification.ENEMY));

    //ElectricEelEnemy.getComponent(AnimationRenderComponent.class).startAnimation("fl");
    ElectricEelEnemy.getComponent(AnimationRenderComponent.class).scaleEntity();
    //ElectricEelEnemy.getComponent(TextureRenderComponent.class).scaleEntity();
    ServiceLocator.getEntityService().registerNamed("electricEelEnemy@" + ElectricEelEnemy.getId(), ElectricEelEnemy);
    ElectricEelEnemy.setScale(1.2f, 1.2f);

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
  public static Entity createStarFish(Entity target, Entity crystal) {
    Entity ninjaStarfish = createBaseRangeNPC(target, crystal);
    EnemyConfig config = configs.ninjaStarfish;
    TextureRenderComponent textureRenderComponent = new TextureRenderComponent("images/starfish.png");
    /** AnimationRenderComponent animator =
            new AnimationRenderComponent(
                    ServiceLocator.getResourceService()
                            .getAsset("images/ghostKing.atlas", TextureAtlas.class));
    animator.addAnimation("float", 0.1f, Animation.PlayMode.LOOP);
    animator.addAnimation("angry_float", 0.1f, Animation.PlayMode.LOOP);
    */
    ninjaStarfish
            .addComponent(new CombatStatsComponent(config.health, config.baseAttack))
            .addComponent(new HealthBarComponent(100, 10))
            .addComponent(textureRenderComponent)
            .addComponent(new DayNightCycleComponent());

    ninjaStarfish.getComponent(TextureRenderComponent.class).scaleEntity();
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
                    .addTask(new RangedMovementTask(crystal, 10, 2f, 20f, 30f))
                    .addTask(new RangedMovementTask(target, 20, 2f, 3f, 5f));
    Enemy enemy =
            (Enemy) new Enemy()
                    .addComponent(new PhysicsComponent())
                    .addComponent(new PhysicsMovementComponent())
                    .addComponent(new ColliderComponent())
                    .addComponent(new HitboxComponent().setLayer(PhysicsLayer.RangeNPC))
                    .addComponent(new TouchAttackComponent(PhysicsLayer.RangeNPC))
                    .addComponent(aiComponent);

    PhysicsUtils.setScaledCollider(enemy, 0.9f, 0.4f);
    return enemy;
  }

  /**
   * Creates a generic NPC to be used as a base entity by more specific NPC creation methods.
   *
   * @return entity
   */

  public static Entity createBaseNPC() {

    String[] NPC_textures = { "images/npcs/NPC-V2.1.png",
        //     "images/npc1.png",
            "images/npcs/NPC-V2.2.png" };

     //int index = (int) ((Math.random() * (NPC_textures.length)));
        int index = 1;

        AITaskComponent aiComponent =
            new AITaskComponent()
                .addTask(new WanderTask(new Vector2(30f, 30f), 2f));
        Entity npc =
            new Entity()
                .addComponent(new PhysicsComponent())
                .addComponent(new PhysicsMovementComponent())
                .addComponent(new ColliderComponent())
                //.addComponent(new HitboxComponent().setLayer(PhysicsLayer.NPC))
                .addComponent(new EntityClassification(EntityClassification.NPCClassification.NPC))
                .addComponent(new TextureRenderComponent(NPC_textures[index]))
                .addComponent(aiComponent);

        if (index == 1){
          npc.setName("SpecialNPC");
        }
        
        else {
          npc.setName("NPC");
        }
        
        npc.setCollectable(false);
        // npc.setScale(5f, 5f);

        PhysicsUtils.setScaledCollider(npc, 0.9f, 0.4f);
        return npc;
      }

      public static Entity createNPC() {
        Entity NPC = createBaseNPC();
        // NPCConfig config = configs.NPC1;
        //ServiceLocator.getEntityService().registerNamed("ArmoryNPC" + ArmoryNPC.getId(), ArmoryNPC);
        // if (NPC.getName()=="SpecialNPC"){
        AnimationRenderComponent animator =
                 new AnimationRenderComponent(
                         ServiceLocator.getResourceService().getAsset("images/npc_animations/npc.atlas", TextureAtlas.class));
        animator.addAnimation("NPC", 0.1f, Animation.PlayMode.LOOP);
        NPC.addComponent(animator);
        NPC.getComponent(AnimationRenderComponent.class).scaleEntity();
        NPC.getComponent(AnimationRenderComponent.class).startAnimation("NPC");
                //  .addComponent(textureRenderComponent);
        // }
          return NPC;
      }

  private NPCFactory() {
    throw new IllegalStateException("Instantiating static util class");
  }
}
