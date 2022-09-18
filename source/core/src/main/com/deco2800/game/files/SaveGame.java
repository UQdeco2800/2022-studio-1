package com.deco2800.game.files;

import com.badlogic.gdx.utils.Json;
import com.deco2800.game.entities.Entity;
import com.deco2800.game.entities.EntityService;
import com.deco2800.game.entities.factories.ObstacleFactory;
import com.deco2800.game.services.ServiceLocator;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class SaveGame {
    private static String savePath = "Saves/1";

    private static void saveEntityService(int slotNumber) {
        Entity tree = ObstacleFactory.createTree();

        //Json json = new Json();
        //System.out.println(json.prettyPrint(tree));
        //FileLoader.writeClass(tree, savePath, FileLoader.Location.LOCAL);
    }

    public static void saveGameState(int slotNumber) {
        saveEntityService(slotNumber);
    }


}
