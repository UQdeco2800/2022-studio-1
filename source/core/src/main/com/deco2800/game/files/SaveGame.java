package com.deco2800.game.files;
import com.deco2800.game.components.CombatStatsComponent;
import com.deco2800.game.areas.ForestGameArea;
import com.deco2800.game.components.DayNightClockComponent;
import com.deco2800.game.components.Environmental.EnvironmentalComponent;
import com.deco2800.game.components.player.InventoryComponent;
import com.deco2800.game.components.player.PlayerStatsDisplay;
import com.deco2800.game.components.shop.artefacts.Artefact;
import com.deco2800.game.components.shop.equipments.Equipments;
import com.deco2800.game.entities.Entity;
import com.deco2800.game.entities.EntityService;
import com.deco2800.game.entities.configs.CrystalConfig;
import com.deco2800.game.entities.factories.*;
import com.deco2800.game.events.EventHandler;
import com.deco2800.game.rendering.AnimationRenderComponent;
import com.deco2800.game.rendering.TextureRenderComponent;
import com.deco2800.game.services.DayNightCycleService;
import com.deco2800.game.services.ServiceLocator;
import java.io.File;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.ArrayList;
import java.util.HashMap;
import java.lang.reflect.*;
import java.util.Map;

/**
 * Class that handles all save game mechanics
 */
public class SaveGame {
    private static final Logger logger = LoggerFactory.getLogger(SaveGame.class);
    private static String savePathEnvironmental = "Saves/Environmental.json";
    private static String savePathStructures = "Saves/Structures.json";
    private static String savePathCrystal = "Saves/Crystal.json";
    private static String savePathPlayer = "Saves/Player.json";
    private static String saveGameData = "Saves/GameData.json";

    private final static HashMap<String, Method> environmentalGeneration = new HashMap<>();
    private final static HashMap<String, Method> structureGeneration = new HashMap<>();

    /**
     * Saves environmental objects to enviromental via the use of json
     */
    private static void saveEnvironmentalObjects() {
        logger.debug("Begin Saving Environment");
        ArrayList<Tuple> environmentalObjects = new ArrayList<>();

        //loop through all entities and check they have an environmental component, save texture and position
        for (Entity ent: ServiceLocator.getEntityService().getEntityMap().values()) {

            if (ent.getComponent(EnvironmentalComponent.class) != null && ent.getComponent(TextureRenderComponent.class) != null) {
                environmentalObjects.add(new Tuple().setTexture(ent.getComponent(TextureRenderComponent.class).getTexturePath()).setPosition(ent.getPosition()).setName(ent.getName()));

            }
        }

        FileLoader.writeClass(environmentalObjects, savePathEnvironmental, FileLoader.Location.LOCAL);
        logger.debug("Finished Saving Environment");
    }

    /**
     * Loads all environmental objects from saves/environmental via the use of json
     *
     * @throws InvocationTargetException throw error when invoking methods fails
     * @throws IllegalAccessException throw error when invoking method fails
     */
    private static void loadEnvrionmentalObjects() throws InvocationTargetException, IllegalAccessException {
        logger.debug("Begin Loading Environment");
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

            newEnvironmentalObject.setName(obstacle.name);
            ServiceLocator.getEntityService().register(newEnvironmentalObject);
            ServiceLocator.getEntityService().registerNamed(newEnvironmentalObject.getName(), newEnvironmentalObject);

        }
        logger.debug("Finished Loading Environment");
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
        logger.debug("Begin Saving Structures");
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
        logger.debug("Finished Saving Structures");
    }

    /**
     * Load and store all structures back onto the map
     *
     * @throws InvocationTargetException when invoking method fails due to invalid method
     * @throws IllegalAccessException when invoking method fails due to permisions
     */
    private static void loadStructures() throws InvocationTargetException, IllegalAccessException {
        logger.debug("Begin Loading Structures");
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
        logger.debug("Finished Loading Structures");
    }

    /**
     * Saves the crystal to a JSON file
     */
    private static void saveCrystal() {
        logger.debug("Begin Saving Crystal");
        String name = "crystal";
        Entity crystal = ServiceLocator.getEntityService().getNamedEntity(name);

        if (crystal == null) {
            return;
        }

        // save crystal level, health, texture, name, and position
        Tuple crystalRepresentation = new Tuple().setName(name).setPosition(crystal.getPosition());
        crystalRepresentation.setLevel(crystal.getComponent(CombatStatsComponent.class).getLevel());
        crystalRepresentation.setHealth(crystal.getComponent(CombatStatsComponent.class).getHealth());
        crystalRepresentation.setTexture(crystal.getComponent(TextureRenderComponent.class).getTexturePath());

        FileLoader.writeClass(crystalRepresentation, savePathCrystal, FileLoader.Location.LOCAL);
        logger.debug("End Saving Crystal");
    }

    /**
     * Reads crystal from json file
     * @throws InvocationTargetException when invoking method fails due to invalid method
     * @throws IllegalAccessException when invoking method fails due to permisions
     */
    private static void loadCrystal() throws InvocationTargetException, IllegalAccessException {
        logger.debug("Begin Loading Crystal");
        Tuple crystalRepresentation = FileLoader.readClass(Tuple.class, savePathCrystal, FileLoader.Location.LOCAL);
        CrystalConfig crystalStats = FileLoader.readClass(CrystalConfig.class, "configs/crystal.json");
        if (crystalRepresentation != null) {
            Entity crystal = ServiceLocator.getEntityService().getNamedEntity("crystal");
//                    CrystalFactory.createCrystal(crystalRepresentation.texture, crystalRepresentation.name);
            if (crystal == null) {
                return;
            }
            crystal.setPosition(crystalRepresentation.position);
            for (int i = crystalStats.level; i < crystalRepresentation.level; i++) {
                CrystalFactory.upgradeCrystal();
            }
            crystal.getComponent(CombatStatsComponent.class).setHealth(crystalRepresentation.health);
        }
        logger.debug("End Loading Crystal");
    }

    private static void savePlayer() {
        logger.debug("Begin Saving Player");
        String name = "player";
        Entity player = ServiceLocator.getEntityService().getNamedEntity(name);
        if (player == null) {
            return;
        }
        // save player status - look at CareTaker and Memento which would for sure be the more elegant way to do this
        // in sprint 4, but I don't want to mess with that code this sprint to avoid conflicts
        HashMap<String, Object> status = new HashMap();
        status.put("gold", player.getComponent(InventoryComponent.class).getGold());
        status.put("stone", player.getComponent(InventoryComponent.class).getStone());
        status.put("wood", player.getComponent(InventoryComponent.class).getWood());
        status.put("health", player.getComponent(CombatStatsComponent.class).getHealth());
        status.put("items", player.getComponent(InventoryComponent.class).getItems());
        status.put("attack", player.getComponent(CombatStatsComponent.class).getBaseAttack());
        status.put("defence", player.getComponent(CombatStatsComponent.class).getBaseDefense());
        status.put("weapon", player.getComponent(InventoryComponent.class).getWeapon());
        status.put("chestplate", player.getComponent(InventoryComponent.class).getChestplate());
        status.put("helmet", player.getComponent(InventoryComponent.class).getHelmet());

        Tuple p = new Tuple().setPosition(player.getPosition()).setName(name).setPlayerState(status);
        FileLoader.writeClass(p, savePathPlayer, FileLoader.Location.LOCAL);
        logger.debug("End Saving Player");
    }

    /**
     * note that this assumes the player has been created already
     */
    private static void loadPlayer() {
        logger.debug("Begin Loading Player");
        String name = "player";
        Tuple p = FileLoader.readClass(Tuple.class, savePathPlayer, FileLoader.Location.LOCAL);
        Entity player = ServiceLocator.getEntityService().getNamedEntity(name);

        if (player == null) {
            return;
        }

        if (p != null) {
            HashMap<String, Object> d = p.playerState;
            player.getComponent(InventoryComponent.class).setGold((int) d.get("gold"));
            player.getComponent(InventoryComponent.class).setStone((int) d.get("stone"));
            player.getComponent(InventoryComponent.class).setWood((int) d.get("wood"));
            player.getComponent(CombatStatsComponent.class).setHealth((int) d.get("health"));
            player.getComponent(InventoryComponent.class).setItems((HashMap<Artefact, Integer>) d.get("items"));
            player.getComponent(CombatStatsComponent.class).setBaseAttack((int) d.get("attack"));
            player.getComponent(CombatStatsComponent.class).setBaseDefense((int) d.get("defence"));
            player.getComponent(InventoryComponent.class).setWeapon((Equipments) d.get("weapon"));
            player.getComponent(InventoryComponent.class).setChestplate((Equipments) d.get("chestplate"));
            player.getComponent(InventoryComponent.class).setHelmet((Equipments) d.get("helmet"));

            player.setPosition(p.position);
            player.getComponent(PlayerStatsDisplay.class).updateResourceAmount();
        }
        logger.debug("End Loading Player");
    }

    private static void saveGameData() {
        logger.debug("Begin Saving Game Related Data");
        DayNightCycleService t = ServiceLocator.getDayNightCycleService();
        t.currentDayNumber = ServiceLocator.getDayNightCycleService().currentDayNumber;
        FileLoader.writeClass(t, saveGameData, FileLoader.Location.LOCAL);
        logger.debug("Finished Saving Game Related Data");
    }

    private static void loadGameData() {
        logger.debug("Begin Loading Game Data");
        DayNightCycleService savedDayNightCycle = FileLoader.readClass(DayNightCycleService.class, saveGameData, FileLoader.Location.LOCAL);
        DayNightCycleService currentService = ServiceLocator.getDayNightCycleService();

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

        logger.debug("Finished Loading Game Data");
    }

    /**
     * Save all game assets to the save game folder
     */
    public static void saveGameState() {
        logger.debug("Begin Saving");
        try {

            environmentalGenerationSetUp();
            saveEnvironmentalObjects();

            structureGenerationSetUp();
            saveStructures();

            saveCrystal();
            savePlayer();

            saveGameData();

        } catch (NoSuchMethodException ignored) {

        }
        logger.debug("Finished Saving");
    }

    /**
     * Load all game assets from the save game folder
     */
    public static void loadGameState() {
        logger.debug("Begin Loading");
        try {

            structureGenerationSetUp();
            loadStructures();

            environmentalGenerationSetUp();
            loadEnvrionmentalObjects();
            loadCrystal();
            loadPlayer();

            loadGameData();

        } catch (InvocationTargetException | IllegalAccessException | NoSuchMethodException ignored) {
            logger.error("ERROR OCCURED: " + ignored);
        }
        logger.debug("Finished Loading");
    }


}
