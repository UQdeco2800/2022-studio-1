package com.deco2800.game.files;
import com.badlogic.gdx.math.Vector2;
import com.deco2800.game.components.Environmental.EnvironmentalComponent;
import com.deco2800.game.entities.Entity;
import com.deco2800.game.rendering.TextureRenderComponent;
import com.deco2800.game.services.ServiceLocator;
import java.util.ArrayList;
import java.util.HashMap;


public class SaveGame {
    private static String savePath = "Saves/1.json";
    //private Json
    private final static HashMap<EnvironmentalComponent.EnvironmentalObstacle, Runnable> environmentalGeneration = new HashMap<>();

    private SaveGame() {
        //environmentalGeneration.put()
    }

    private static void saveEntityService(int slotNumber) {

        ArrayList<HashMap<String, Vector2>> environmentalObjects = new ArrayList<>();

        for (Entity ent: ServiceLocator.getEntityService().getEntityMap().values()) {
            HashMap<String, Vector2> currentMapping = new HashMap<>();

            if (ent.getComponent(EnvironmentalComponent.class) != null && ent.getComponent(TextureRenderComponent.class) != null) {
                currentMapping.put(ent.getComponent(TextureRenderComponent.class).getTexturePath(), ent.getPosition());
                environmentalObjects.add(currentMapping);
            }
        }

        FileLoader.writeClass(environmentalObjects, savePath, FileLoader.Location.LOCAL);

        //ArrayList testReturned = FileLoader.readClass(ArrayList.class, savePath, FileLoader.Location.LOCAL);

    }

    public static void saveGameState(int slotNumber) {
        saveEntityService(slotNumber);
    }


}
