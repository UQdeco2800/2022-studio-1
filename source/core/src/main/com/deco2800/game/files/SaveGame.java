package com.deco2800.game.files;

import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.utils.Json;
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
        Entity environmentalObject = new Entity()
                .addComponent(new EnvironmentalComponent().setObstacle(EnvironmentalComponent.EnvironmentalObstacle.TREE));
                /*.addComponent(new CollisionEffectComponent(collisionEffect, speedModifier));
        environmentalObject.setName("EnvironmentalObject");
        environmentalObject.setCollectable(false);
        environmentalObject.getComponent(PhysicsComponent.class).setBodyType(BodyDef.BodyType.StaticBody);
        environmentalObject.getComponent(TextureRenderComponent.class).scaleEntity();
        environmentalObject.scaleHeight(heightScale);
        PhysicsUtils.setScaledCollider(environmentalObject, scaleX, scaleY);
        return environmentalObject;*/

        FileLoader.writeClass(environmentalObject, savePath, FileLoader.Location.LOCAL);

        Entity tree_return = FileLoader.readClass(Entity.class, savePath, FileLoader.Location.LOCAL);
        System.out.println(tree_return.getCenterPosition());

    }

    public static void saveGameState(int slotNumber) {
        saveEntityService(slotNumber);
    }


}
