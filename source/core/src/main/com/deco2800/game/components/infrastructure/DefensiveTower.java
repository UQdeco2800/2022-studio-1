package com.deco2800.game.components.infrastructure;
import com.deco2800.game.components.CombatStatsComponent;
import com.deco2800.game.entities.Entity;
import com.deco2800.game.physics.PhysicsLayer;
import com.deco2800.game.physics.PhysicsUtils;
import com.deco2800.game.physics.components.ColliderComponent;
import com.deco2800.game.physics.components.HitboxComponent;
import com.deco2800.game.physics.components.PhysicsComponent;
import com.deco2800.game.rendering.TextureRenderComponent;

/*
 * Core defensiveTower class which extends the infrastructure class. Adds 
 * additional attributes such as damage, which all defensive towers possess. 
 */
public class DefensiveTower extends Infrastructure {
    //Will need to adjust these variables once we know a definitive tile size
    static float TOWERX = 0.6f; 
    static float TOWERY = 0.3f;
    static float DENSITY = 1.5f;
    int damage; //Damage of the given defensive tower
    String texture; //Texture of the tower

    /*
     * Constructor for the defensiveTower abstract class. Initialises health and
     * damage parameters. 
     */
    public DefensiveTower(int health, int damage, String texture) {
        super(health); //Sets the health of the tower
        this.damage = damage; //Sets the damage of the tower
        this.texture = texture;
    }

    /*
     * Function which creates an entity and adds the relevant components to create
     * a tower object. Uses various constants defined earlier in the file. 
     */
    public Entity createTower() {
        Entity tower = 
            new Entity()
                .addComponent(new TextureRenderComponent(texture))
                .addComponent(new PhysicsComponent())
                .addComponent(new ColliderComponent())
                //Might need to adjust hitbox layer depending? 
                .addComponent(new HitboxComponent().setLayer(PhysicsLayer.NPC))
                .addComponent(new CombatStatsComponent(this.health, this.damage));
                //Will need to add another component to display tower UI

            /*Creates collider (hitbox?) for tower based on tower / tile 
            * Dimensions
            */ 
            PhysicsUtils.setScaledCollider(tower, TOWERX, TOWERY);
            tower.getComponent(ColliderComponent.class).setDensity(DENSITY);
            tower.getComponent(TextureRenderComponent.class).scaleEntity();
            return tower;
    }
    /*
     * Function which returns the damage of the defensiveTower class.
     * Returns: Integer representing damage. 
     */
    public int getDamage(){
        return damage;
    }

}
