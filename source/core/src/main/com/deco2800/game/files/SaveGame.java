package com.deco2800.game.files;
import com.deco2800.game.components.DayNightClockComponent;
import com.deco2800.game.components.Environmental.EnvironmentalComponent;
import com.deco2800.game.entities.Entity;
import com.deco2800.game.entities.factories.ObstacleFactory;
import com.deco2800.game.entities.factories.ResourceBuildingFactory;
import com.deco2800.game.entities.factories.StructureFactory;
import com.deco2800.game.events.EventHandler;
import com.deco2800.game.rendering.AnimationRenderComponent;
import com.deco2800.game.rendering.TextureRenderComponent;
import com.deco2800.game.services.DayNightCycleService;
import com.deco2800.game.services.ServiceLocator;
import java.util.ArrayList;
import java.util.HashMap;
import java.lang.reflect.*;
import java.util.Map;

/**
 * Class that handles all save game mechanics
 */
public class SaveGame {
    private static String savePathEnvironmental = "Saves/Environmental.json";
    private static String savePathStructures = "Saves/Structures.json";
    private static String saveGameData = "Saves/GameData.json";

    private final static HashMap<String, Method> environmentalGeneration = new HashMap<>();
    private final static HashMap<String, Method> structureGeneration = new HashMap<>();

    /**
     * Saves environmental objects to enviromental via the use of json
     */
    private static void saveEnvironmentalObjects() {

        ArrayList<Tuple> environmentalObjects = new ArrayList<>();

        //loop through all entities and check they have an environmental component, save texture and position
        for (Entity ent: ServiceLocator.getEntityService().getEntityMap().values()) {

            if (ent.getComponent(EnvironmentalComponent.class) != null && ent.getComponent(TextureRenderComponent.class) != null) {
                environmentalObjects.add(new Tuple().setTexture(ent.getComponent(TextureRenderComponent.class).getTexturePath()).setPosition(ent.getPosition()));
            }
        }

        FileLoader.writeClass(environmentalObjects, savePathEnvironmental, FileLoader.Location.LOCAL);
    }

    /**
     * Loads all environmental objects from saves/environmental via the use of json
     *
     * @throws InvocationTargetException throw error when invoking methods fails
     * @throws IllegalAccessException throw error when invoking method fails
     */
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

    /**
     * Helper method that generates a map mapping textures to each corresponding creator method in obstacle factory
     *
     * @throws NoSuchMethodException if the method doesnt exist or has been changed
     */
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

    /**
     * Helper method that generates a map for textures to corresponding creator method in obstacle factory
     *
     * @throws NoSuchMethodException if the method does not exist
     */
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
        structureGeneration.put("wood", ResourceBuildingFactory.class.getMethod("createWoodCutter"));
        structureGeneration.put("stonequarry", ResourceBuildingFactory.class.getMethod("createStoneQuarry"));
    }

    /**
     * Save all structures
     */
    private static void saveStructures() {
        ArrayList<Tuple> structuresList = new ArrayList<>();

        Map<String, Entity> structures = ServiceLocator.getStructureService().getAllNamedEntities();

        //loop through all entities saving texture, position and name of structure
        for (String name: structures.keySet()) {
            if (structures.get(name).getComponent(TextureRenderComponent.class) != null) {
                structuresList.add(new Tuple().setName(name).setPosition(structures.get(name).getPosition()).setTexture(structures.get(name).getComponent(TextureRenderComponent.class).getTexturePath()));
            } else if (structures.get(name).getComponent(AnimationRenderComponent.class) != null) {
                if (name.contains("wood")) {
                    structuresList.add(new Tuple().setName(name).setPosition(structures.get(name).getPosition()).setTexture("wood"));
                } else if (name.contains("stonequarry")){
                    structuresList.add(new Tuple().setName(name).setPosition(structures.get(name).getPosition()).setTexture("stonequarry"));
                }

            }
        }


        FileLoader.writeClass(structuresList, savePathStructures, FileLoader.Location.LOCAL);
    }

    /**
     * Load and store all structures back onto the map
     *
     * @throws InvocationTargetException when invoking method fails due to invalid method
     * @throws IllegalAccessException when invoking method fails due to permisions
     */
    private static void loadStructures() throws InvocationTargetException, IllegalAccessException {
        ArrayList structures = FileLoader.readClass(ArrayList.class, savePathStructures, FileLoader.Location.LOCAL);

        //loop through all structures and go to correct generation method
        for (Object st: structures) {
            Tuple structureRepresentation = (Tuple) st;
            Entity structure;

            if (structureRepresentation.texture.contains("TOWER")) {

                int count = 1;

                //count level of tower
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


    private static void saveGameData() {
        DayNightCycleService t = ServiceLocator.getDayNightCycleService();
        FileLoader.writeClass(t, saveGameData, FileLoader.Location.LOCAL);
    }

    private static void loadGameData() {
        DayNightCycleService savedDayNightCycle = FileLoader.readClass(DayNightCycleService.class, saveGameData, FileLoader.Location.LOCAL);
        DayNightCycleService currentService = ServiceLocator.getDayNightCycleService();

        ServiceLocator.getDayNightCycleService().getEvents();
        System.out.println("CURRENT DAY IS " + savedDayNightCycle.currentDayNumber);
        currentService.currentCycleStatus = savedDayNightCycle.currentCycleStatus;
        currentService.lastCycleStatus = savedDayNightCycle.lastCycleStatus;
        currentService.currentDayNumber = savedDayNightCycle.currentDayNumber;
        currentService.currentDayMillis = savedDayNightCycle.currentDayMillis;
        currentService.timePaused = savedDayNightCycle.timePaused;
        currentService.totalDurationPaused = savedDayNightCycle.totalDurationPaused;
        currentService.isPaused = savedDayNightCycle.isPaused;
        currentService.isStarted = savedDayNightCycle.isStarted;
        currentService.config = savedDayNightCycle.config;
        currentService.timer = savedDayNightCycle.timer;
        currentService.timeSinceLastPartOfDay = savedDayNightCycle.timeSinceLastPartOfDay;
        currentService.timePerHalveOfPartOfDay = savedDayNightCycle.timePerHalveOfPartOfDay;
        currentService.partOfDayHalveIteration = savedDayNightCycle.partOfDayHalveIteration;
        currentService.lastPartOfDayHalveIteration = savedDayNightCycle.lastPartOfDayHalveIteration;
    }

    /**
     * Save all game assets to the save game folder
     */
    public static void saveGameState() {

        try {

            environmentalGenerationSetUp();
            saveEnvironmentalObjects();

            structureGenerationSetUp();
            saveStructures();

            saveGameData();

        } catch (NoSuchMethodException ignored) {

        }
    }

    /**
     * Load all game assets from the save game folder
     */
    public static void loadGameState() {

        try {

            structureGenerationSetUp();
            loadStructures();

            environmentalGenerationSetUp();
            loadEnvrionmentalObjects();

            loadGameData();

        } catch (InvocationTargetException | IllegalAccessException | NoSuchMethodException ignored) {
            System.out.println("ERROR OCCURED: " + ignored);
        }

    }


}
