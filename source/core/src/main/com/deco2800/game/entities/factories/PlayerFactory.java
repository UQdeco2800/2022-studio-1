package com.deco2800.game.entities.factories;

import com.deco2800.game.components.CombatStatsComponent;
import com.deco2800.game.components.HealthBarComponent;
import com.deco2800.game.components.player.AnimationController;
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

    //e.g. "w" - when w is pressed, "wa" - during attack, when w is pressed before
    AnimationRenderComponent player_start = new AnimationRenderComponent(ServiceLocator.getResourceService()
    .getAsset("images/anim_demo/mainchar_anim_final.atlas", TextureAtlas.class));
    player_start.addAnimation("w", 0.1f, Animation.PlayMode.LOOP);
    player_start.addAnimation("wa", 0.1f, Animation.PlayMode.LOOP);
    player_start.addAnimation("a", 0.1f, Animation.PlayMode.LOOP);
    player_start.addAnimation("aa", 0.1f, Animation.PlayMode.LOOP);
    player_start.addAnimation("s", 0.1f, Animation.PlayMode.LOOP);
    player_start.addAnimation("sa", 0.1f, Animation.PlayMode.LOOP);
    player_start.addAnimation("d", 0.1f, Animation.PlayMode.LOOP);
    player_start.addAnimation("da", 0.1f, Animation.PlayMode.LOOP);

    Entity player =
        new Entity()
            .addComponent(player_start)
            .addComponent(new PhysicsComponent())
            .addComponent(new ColliderComponent().setLayer(PhysicsLayer.PLAYER))
            .addComponent(new AnimationController())
            .addComponent(new HitboxComponent().setLayer(PhysicsLayer.PLAYER))
            .addComponent(new PlayerActions())
            .addComponent(new CombatStatsComponent(stats.health, stats.baseAttack, stats.baseDefense))
            .addComponent(new HealthBarComponent(100, 10))
            .addComponent(new InventoryComponent(stats.gold, stats.stone, stats.wood
                    , stats.weapon, stats.helmet, stats.chestplate))
            .addComponent(inputComponent)            
            .addComponent(new PlayerStatsDisplay())
            .addComponent(new TouchAttackComponent(PhysicsLayer.NPC, 1.5f));

    player.setName("player");
    player.setCollectable(false);

    ServiceLocator.getEntityService().registerNamed("player", player);
    PhysicsUtils.setScaledCollider(player, 0.6f, 0.3f);
    player.getComponent(ColliderComponent.class).setDensity(1.5f);
    player.getComponent(AnimationRenderComponent.class).startAnimation("w");
    player.getComponent(AnimationRenderComponent.class).scaleEntity();
    player.setScale(1.2f, 1.2f);
    return player;
  }

  public static Entity loadPlayer() {
    if (CareTaker.getInstance().getLast() == null) {
      return createPlayer();
    } else {
      Memento lastStatus = CareTaker.getInstance().getLast();
      Entity player = createPlayer();
      player.getComponent(CombatStatsComponent.class).setHealth(lastStatus.getCurrentHealth());
      player.getComponent(CombatStatsComponent.class).setBaseAttack(lastStatus.getAttack());
      player.getComponent(CombatStatsComponent.class).setBaseDefense(lastStatus.getDefense());
      player.getComponent(InventoryComponent.class).setGold(lastStatus.getGold());
      player.getComponent(InventoryComponent.class).setItems(lastStatus.getItemList());
      player.getComponent(InventoryComponent.class).setStone(lastStatus.getStone());
      player.getComponent(InventoryComponent.class).setWood(lastStatus.getWood());
      player.getComponent(InventoryComponent.class).setWeapon(lastStatus.getWeapon());
      player.getComponent(InventoryComponent.class).setChestplate(lastStatus.getChestplate());
      player.getComponent(InventoryComponent.class).setHelmet(lastStatus.getHelmet());

      player.getComponent(PlayerStatsDisplay.class).updateItems();
      return player;
    }
  }

  private PlayerFactory() {
    throw new IllegalStateException("Instantiating static util class");
  }
}
