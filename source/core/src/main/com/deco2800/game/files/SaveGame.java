package com.deco2800.game.files;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.utils.Json;
import com.deco2800.game.components.Component;
import com.deco2800.game.components.Environmental.CollisionEffectComponent;
import com.deco2800.game.components.Environmental.EnvironmentalComponent;
import com.deco2800.game.entities.Entity;
import com.deco2800.game.entities.EntityService;
import com.deco2800.game.entities.factories.ObstacleFactory;
import com.deco2800.game.physics.PhysicsLayer;
import com.deco2800.game.physics.PhysicsUtils;
import com.deco2800.game.physics.components.ColliderComponent;
import com.deco2800.game.physics.components.PhysicsComponent;
import com.deco2800.game.rendering.TextureRenderComponent;
import com.deco2800.game.services.ServiceLocator;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class SaveGame {
    private static String savePath = "Saves/1.json";
    //private Json

    private static void saveEntityService(int slotNumber) {
        Entity environmentalObject = new Entity();
        environmentalObject.addComponent(new CollisionEffectComponent(CollisionEffectComponent.CollisionEffect.KNOCKBACK, 1))
        .addComponent(new EnvironmentalComponent()).addComponent(new ColliderComponent().setLayer(PhysicsLayer.OBSTACLE));

        ArrayList<Entity> environmentalObjects = new ArrayList<>();
        Entity test = new Entity();
        for (Entity ent: ServiceLocator.getEntityService().getEntityMap().values()) {
            if (ent.getComponent(EnvironmentalComponent.class) != null) {
                test.addComponent(ent.getComponent(EnvironmentalComponent.class));
                test.setPosition(ent.getPosition());
                break;
            }
        }




        //Texture is a no go
        //test.add(environmentalObject.getComponent(TextureRenderComponent.class));

        FileLoader.writeClass(test, savePath, FileLoader.Location.LOCAL);

        //Entity tree_return = FileLoader.readClass(Entity.class, savePath, FileLoader.Location.LOCAL);
        //System.out.println(tree_return.getCenterPosition());
        Entity testReturned = FileLoader.readClass(Entity.class, savePath, FileLoader.Location.LOCAL);

        System.out.println(testReturned + "TEST RETURNED");

        testReturned.addComponent(new TextureRenderComponent("images/crystal.png"));
        testReturned.setScale(new Vector2(1,1));
        ServiceLocator.getEntityService().register(testReturned);
        //testReturned.setPosition();

    }

    public static void saveGameState(int slotNumber) {
        saveEntityService(slotNumber);
    }


}
