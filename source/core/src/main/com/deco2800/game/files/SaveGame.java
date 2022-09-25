package com.deco2800.game.files;
import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.Vector2;
import com.deco2800.game.areas.terrain.TerrainComponent;
import com.deco2800.game.components.Environmental.EnvironmentalComponent;
import com.deco2800.game.entities.Entity;
import com.deco2800.game.entities.factories.ObstacleFactory;
import com.deco2800.game.entities.factories.StructureFactory;
import com.deco2800.game.rendering.TextureRenderComponent;
import com.deco2800.game.services.ServiceLocator;
import java.util.ArrayList;
import java.util.HashMap;
import java.lang.reflect.*;
import java.util.Map;

public class SaveGame {
    private static String savePathEnvironmental = "Saves/Environmental.json";
    private static String savePathStructures = "Saves/Structures.json";

    private final static HashMap<String, Method> environmentalGeneration = new HashMap<>();
    private final static HashMap<String, Method> structureGeneration = new HashMap<>();


    private static void saveEnvironmentalObjects() throws InvocationTargetException, IllegalAccessException {

        ArrayList<Tuple> environmentalObjects = new ArrayList<>();

        for (Entity ent: ServiceLocator.getEntityService().getEntityMap().values()) {

            if (ent.getComponent(EnvironmentalComponent.class) != null && ent.getComponent(TextureRenderComponent.class) != null) {
                environmentalObjects.add(new Tuple().setTexture(ent.getComponent(TextureRenderComponent.class).getTexturePath()).setPosition(ent.getPosition()));
            }
        }

        FileLoader.writeClass(environmentalObjects, savePathEnvironmental, FileLoader.Location.LOCAL);
    }

    private static void loadEnvrionmentalObjects() throws InvocationTargetException, IllegalAccessException {
        ArrayList obstacles = FileLoader.readClass(ArrayList.class, savePathEnvironmental, FileLoader.Location.LOCAL);

        for (Object ob: obstacles) {
            Tuple obstacle = (Tuple) ob;
            Entity newEnvironmentalObject;

            if (obstacle.texture.contains("Tree") || obstacle.texture.contains("limestone-boulder") || obstacle.texture.contains("marble-stone")) {
                newEnvironmentalObject = (Entity) environmentalGeneration.get(obstacle.texture).invoke(null, obstacle.texture);
            } else {
                newEnvironmentalObject = (Entity) environmentalGeneration.get(obstacle.texture).invoke(null);
            }

            newEnvironmentalObject.setPosition(obstacle.position);
        }
    }

    private static void environmentalGenerationSetUp() throws NoSuchMethodException {
        environmentalGeneration.put("images/shipWreckBack.png", ObstacleFactory.class.getMethod("createShipwreckBack"));
        environmentalGeneration.put("images/shipWreckFront.png", ObstacleFactory.class.getMethod("createShipwreckFront"));
        environmentalGeneration.put("images/65x33_tiles/shell.png", ObstacleFactory.class.getMethod("createShell"));
        environmentalGeneration.put("images/landscape_objects/wooden-fence-60x60.png", ObstacleFactory.class.getMethod("createWoodenFence"));
        environmentalGeneration.put("images/landscape_objects/pillar.png", ObstacleFactory.class.getMethod("createPillar"));
        environmentalGeneration.put("images/landscape_objects/chalice.png", ObstacleFactory.class.getMethod("createAoeSpeedArtefact"));
        environmentalGeneration.put("images/landscape_objects/billboard.png", ObstacleFactory.class.getMethod("createBillboard"));
        environmentalGeneration.put("images/landscape_objects/geyser.png", ObstacleFactory.class.getMethod("createGeyser"));
        environmentalGeneration.put("images/landscape_objects/cypress-tree-60x100.png", ObstacleFactory.class.getMethod("createSpikyTree"));
        environmentalGeneration.put("images/landscape_objects/vines.png", ObstacleFactory.class.getMethod("createVine"));
        environmentalGeneration.put( "images/landscape_objects/limestone-boulder-60x60.png", ObstacleFactory.class.getMethod("createRock", String.class));
        environmentalGeneration.put("images/landscape_objects/marble-stone-60x40.png", ObstacleFactory.class.getMethod("createRock", String.class));
        environmentalGeneration.put("images/landscape_objects/leftPalmTree.png", ObstacleFactory.class.getMethod("createTree", String.class));
        environmentalGeneration.put("images/landscape_objects/rightPalmTree.png", ObstacleFactory.class.getMethod("createTree", String.class));
        environmentalGeneration.put("images/landscape_objects/groupPalmTrees.png", ObstacleFactory.class.getMethod("createTree", String.class));
    }

    private static void structureGenerationSetUp() throws NoSuchMethodException {
        structureGeneration.put("images/TOWER1I.png", StructureFactory.class.getMethod("createTower1", int.class));
        structureGeneration.put("images/TOWER1II.png", StructureFactory.class.getMethod("createTower1", int.class));
        structureGeneration.put("images/TOWER1III.png", StructureFactory.class.getMethod("createTower1", int.class));
        structureGeneration.put("images/TOWER2I.png", StructureFactory.class.getMethod("createTower2", int.class));
        structureGeneration.put("images/TOWER2II.png", StructureFactory.class.getMethod("createTower2", int.class));
        structureGeneration.put("images/TOWRER2III.png", StructureFactory.class.getMethod("createTower2", int.class));
        structureGeneration.put("images/TOWER2III.png", StructureFactory.class.getMethod("createTower2", int.class));
        structureGeneration.put("images/TOWER3I.png", StructureFactory.class.getMethod("createTower3", int.class));
        structureGeneration.put("images/TOWER3II.png", StructureFactory.class.getMethod("createTower3", int.class));
        structureGeneration.put("images/TOWER3III.png", StructureFactory.class.getMethod("createTower3", int.class));
        structureGeneration.put("images/trap.png", StructureFactory.class.getMethod("createTrap"));
        structureGeneration.put("images/Wall-right.png", StructureFactory.class.getMethod("createWall"));
    }


    private static void saveStructures() {
        ArrayList<Tuple> structuresList = new ArrayList<>();

        Map<String, Entity> structures = ServiceLocator.getStructureService().getAllNamedEntities();

        for (String name: structures.keySet()) {
            if (structures.get(name).getComponent(TextureRenderComponent.class) != null) {
                structuresList.add(new Tuple().setName(name).setPosition(structures.get(name).getPosition()).setTexture(structures.get(name).getComponent(TextureRenderComponent.class).getTexturePath()));
            }
        }
        FileLoader.writeClass(structuresList, savePathStructures, FileLoader.Location.LOCAL);
    }


    private static void loadStructures() throws InvocationTargetException, IllegalAccessException {
        ArrayList structures = FileLoader.readClass(ArrayList.class, savePathStructures, FileLoader.Location.LOCAL);

        for (Object st: structures) {
            Tuple structureRepresentation = (Tuple) st;
            Entity structure;

            if (structureRepresentation.texture.contains("TOWER")) {

                int count = 1;

                for (int i = 0; i < structureRepresentation.texture.length(); i++) {
                    if (structureRepresentation.texture.indexOf(i) == 'I') {
                        count++;
                    }
                }

                structure = (Entity) structureGeneration.get(structureRepresentation.texture).invoke(null, count);
            } else {

                structure = (Entity) structureGeneration.get(structureRepresentation.texture).invoke(null);
            }


            structure.setPosition(structureRepresentation.position);
            structure.setName(structureRepresentation.name);

            ServiceLocator.getStructureService().registerNamed(structure.getName(), structure);
            ServiceLocator.getEntityService().registerNamed(structure.getName(), structure);
        }
    }


    public static void saveGameState() {

        try {

            environmentalGenerationSetUp();
            saveEnvironmentalObjects();

            structureGenerationSetUp();
            saveStructures();
        } catch (InvocationTargetException | IllegalAccessException | NoSuchMethodException ignored) {

        }
    }

    public static void loadGameState() {

        try {

            structureGenerationSetUp();
            loadStructures();

            environmentalGenerationSetUp();
            loadEnvrionmentalObjects();


        } catch (InvocationTargetException | IllegalAccessException | NoSuchMethodException ignored) {
            System.out.println("ERROR OCCURED: " + ignored);
        }

    }


}
