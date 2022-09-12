package com.deco2800.game.entities.factories;

import com.deco2800.game.components.CombatStatsComponent;
import com.deco2800.game.components.HealthBarComponent;
import com.deco2800.game.components.player.InventoryComponent;
import com.deco2800.game.components.player.PlayerActions;
import com.deco2800.game.components.player.PlayerStatsDisplay;
import com.deco2800.game.components.TouchAttackComponent;
import com.deco2800.game.entities.Entity;
import com.deco2800.game.entities.configs.PlayerConfig;
import com.deco2800.game.files.FileLoader;
import com.deco2800.game.input.InputComponent;
import com.deco2800.game.memento.CareTaker;
import com.deco2800.game.memento.Memento;
import com.deco2800.game.physics.PhysicsLayer;
import com.deco2800.game.physics.PhysicsUtils;
import com.deco2800.game.physics.components.ColliderComponent;
import com.deco2800.game.physics.components.HitboxComponent;
import com.deco2800.game.physics.components.PhysicsComponent;
import com.deco2800.game.rendering.TextureRenderComponent;
import com.deco2800.game.rendering.AnimationRenderComponent;
import com.deco2800.game.services.ServiceLocator;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;


/**
 * Factory to create a player entity.
 *
 * <p>
 * Predefined player properties are loaded from a config stored as a json file
 * and should have
 * the properties stores in 'PlayerConfig'.
 */
public class PlayerFactory {
  private static final PlayerConfig stats = FileLoader.readClass(PlayerConfig.class, "configs/player.json");

  /**
   * Create a player entity.
   * 
   * @return entity
   */
  public static Entity createPlayer() {
    InputComponent inputComponent = ServiceLocator.getInputService().getInputFactory().createForPlayer();

    //TextureRenderComponent player_start = new TextureRenderComponent("images/Centaur_left.png");
    AnimationRenderComponent player_start = new AnimationRenderComponent(ServiceLocator.getResourceService()
    .getAsset("images/anim_demo/mainchar.atlas", TextureAtlas.class));
    player_start.addAnimation("w", 0.1f, Animation.PlayMode.LOOP);
    player_start.addAnimation("a", 0.1f, Animation.PlayMode.LOOP);
    player_start.addAnimation("s", 0.1f, Animation.PlayMode.LOOP);
    player_start.addAnimation("d", 0.1f, Animation.PlayMode.LOOP);

    Entity player =
        new Entity()
            .addComponent(player_start)
            .addComponent(new PhysicsComponent())
            .addComponent(new ColliderComponent())
            .addComponent(new HitboxComponent().setLayer(PhysicsLayer.PLAYER))
            .addComponent(new PlayerActions())
            .addComponent(new CombatStatsComponent(stats.health, stats.baseAttack))
            .addComponent(new HealthBarComponent(100, 10))
            .addComponent(new InventoryComponent(stats.gold, stats.stone, stats.wood))
            .addComponent(inputComponent)            
            .addComponent(new PlayerStatsDisplay())
            .addComponent(new TouchAttackComponent(PhysicsLayer.NPC, 1.5f));

    PhysicsUtils.setScaledCollider(player, 0.6f, 0.3f);
    player.getComponent(ColliderComponent.class).setDensity(1.5f);
    player.getComponent(AnimationRenderComponent.class).scaleEntity();
    player.getComponent(AnimationRenderComponent.class).startAnimation("d");
    return player;
  }

  public static Entity loadPlayer(CareTaker playerStatus) {
    if (playerStatus.getAll().size() == 0) {
      return createPlayer();
    } else {
      Memento lastStatus = playerStatus.get(playerStatus.getAll().size() - 1);
      Entity player = createPlayer();
      player.getComponent(CombatStatsComponent.class).setHealth(lastStatus.getCurrentHealth());
      player.getComponent(CombatStatsComponent.class).setBaseAttack(lastStatus.getAttack());
      player.getComponent(InventoryComponent.class).setGold(lastStatus.getGold());
      player.getComponent(InventoryComponent.class).setItems(lastStatus.getItemList());
      player.getComponent(InventoryComponent.class).setStone(lastStatus.getStone());
      return player;
    }
  }

  private PlayerFactory() {
    throw new IllegalStateException("Instantiating static util class");
  }
}
