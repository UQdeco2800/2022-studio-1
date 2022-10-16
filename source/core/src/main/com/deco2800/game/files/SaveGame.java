package com.deco2800.game.files;

import com.badlogic.gdx.math.GridPoint2;
import com.deco2800.game.components.CombatStatsComponent;
import com.deco2800.game.components.Environmental.EnvironmentalComponent;
import com.deco2800.game.components.player.InventoryComponent;
import com.deco2800.game.components.player.PlayerStatsDisplay;
import com.deco2800.game.components.shop.artefacts.Artefact;
import com.deco2800.game.components.shop.artefacts.ShopBuilding;
import com.deco2800.game.components.shop.equipments.Equipments;
import com.deco2800.game.entities.Entity;
import com.deco2800.game.entities.configs.CrystalConfig;
import com.deco2800.game.entities.factories.*;
import com.deco2800.game.events.EventHandler;
import com.deco2800.game.rendering.AnimationRenderComponent;
import com.deco2800.game.rendering.TextureRenderComponent;
import com.deco2800.game.services.DayNightCycleService;
import com.deco2800.game.services.ServiceLocator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.lang.reflect.*;

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

        // loop through all entities and check they have an environmental component,
        // save texture and position
        for (Entity ent : ServiceLocator.getEntityService().getAllNamedEntities().values()) {

            if (ent.getComponent(EnvironmentalComponent.class) != null && (ent.getComponent(TextureRenderComponent.class) != null || ent.getComponent(AnimationRenderComponent.class) != null)) {
                environmentalObjects
                        .add(new Tuple().setTexture(ent.getComponent(TextureRenderComponent.class).getTexturePath())
                                .setPosition(ent.getPosition()).setName(ent.getName()).setTileString(ServiceLocator.getUGSService().getStringByEntity(ent))
                        .setCreationMethod(ent.getCreationMethod()));
            }
        }

        FileLoader.writeClass(environmentalObjects, savePathEnvironmental, FileLoader.Location.LOCAL);
        logger.debug("Finished Saving Environment");
    }

    /**
     * Loads all environmental objects from saves/environmental via the use of json
     *
     * @throws InvocationTargetException throw error when invoking methods fails
     * @throws IllegalAccessException    throw error when invoking method fails
     */
    private static void loadEnvrionmentalObjects() throws InvocationTargetException, IllegalAccessException, NoSuchMethodException {
        logger.debug("Begin Loading Environment");
        ArrayList obstacles = FileLoader.readClass(ArrayList.class, savePathEnvironmental, FileLoader.Location.LOCAL);

        for (Object ob : obstacles) {
            Tuple obstacle = (Tuple) ob;
            Entity newEnvironmentalObject;

            if (obstacle.texture.contains("Tree") || obstacle.texture.contains("limestone-boulder")
                    || obstacle.texture.contains("marble-stone")) {
                newEnvironmentalObject = (Entity) ObstacleFactory.class.getMethod(obstacle.creationMethod, String.class).invoke(null,
                        obstacle.texture);
            } else {
                newEnvironmentalObject = (Entity) ObstacleFactory.class.getMethod(obstacle.creationMethod).invoke(null);
            }

            newEnvironmentalObject.setPosition(obstacle.position);
            newEnvironmentalObject.setName(obstacle.name);

            int x_tile = Integer.parseInt(Arrays.asList(obstacle.tileString.split(",")).get(0));
            int y_tile = Integer.parseInt(Arrays.asList(obstacle.tileString.split(",")).get(1));

            ServiceLocator.getUGSService().setEntity(new GridPoint2(x_tile, y_tile), newEnvironmentalObject, newEnvironmentalObject.getName());
        }
        logger.debug("Finished Loading Environment");
    }

    /**
     * Save all structures
     */
    private static void saveStructures() {
        logger.debug("Begin Saving Structures");
        ArrayList<Tuple> structuresList = new ArrayList<>();

        // loop through all entities saving texture, position and name of structure

        for (Entity entity: ServiceLocator.getUGSService().getStructures()) {
            if (entity.getComponent(TextureRenderComponent.class) != null || entity.getComponent(AnimationRenderComponent.class) != null) {
                Tuple storage = new Tuple();
                if (entity.getRotation() != -1) {
                    storage.setRotation(entity.getRotation());
                }

                structuresList.add(storage.setName(entity.getName())
                        .setCreationMethod(entity.getCreationMethod())
                        .setPosition(entity.getPosition())
                        .setTileString(ServiceLocator.getUGSService().getStringByEntity(entity)));
            }
        }
        FileLoader.writeClass(structuresList, savePathStructures, FileLoader.Location.LOCAL);
        logger.debug("Finished Saving Structures");
    }

    /**
     * Load and store all structures back onto the map
     *
     * @throws InvocationTargetException when invoking method fails due to invalid
     *                                   method
     * @throws IllegalAccessException    when invoking method fails due to
     *                                   permisions
     */
    private static void loadStructures() throws InvocationTargetException, IllegalAccessException, NoSuchMethodException {
        logger.debug("Begin Loading Structures");
        ArrayList structures = FileLoader.readClass(ArrayList.class, savePathStructures, FileLoader.Location.LOCAL);

        // loop through all structures and go to correct generation method
        for (Object st : structures) {
            Tuple structureRepresentation = (Tuple) st;
            Entity structure;

            if (structureRepresentation.name.contains("tower")) {
                structure = (Entity) StructureFactory.class.getMethod(structureRepresentation.creationMethod, int.class, String.class, Boolean.class).invoke(null,1, structureRepresentation.name, false);
            } else if (structureRepresentation.name.contains("wall")) {
                structure = (Entity) StructureFactory.class.getMethod(structureRepresentation.creationMethod, String.class, Boolean.class, int.class).invoke(null,
                        structureRepresentation.name, false, structureRepresentation.rotation);
            } else {
                structure = (Entity) StructureFactory.class.getMethod(structureRepresentation.creationMethod, String.class, Boolean.class).invoke(null, structureRepresentation.name, false);
            }

            structure.setName(structureRepresentation.name);

            int x_tile = Integer.parseInt(Arrays.asList(structureRepresentation.tileString.split(",")).get(0));
            int y_tile = Integer.parseInt(Arrays.asList(structureRepresentation.tileString.split(",")).get(1));

            ServiceLocator.getUGSService().setEntity(new GridPoint2(x_tile, y_tile), structure, structure.getName());
            ServiceLocator.getUGSService().addStructure(structure);
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
     * Reads crystal from json file. Assumes the crystal already exists in the game
     * engine.
     * 
     * @throws InvocationTargetException when invoking method fails due to invalid
     *                                   method
     * @throws IllegalAccessException    when invoking method fails due to
     *                                   permisions
     */
    private static void loadCrystal() throws InvocationTargetException, IllegalAccessException {
        logger.debug("Begin Loading Crystal");
        Tuple crystalRepresentation = FileLoader.readClass(Tuple.class, savePathCrystal, FileLoader.Location.LOCAL);
        CrystalConfig crystalStats = FileLoader.readClass(CrystalConfig.class, "configs/crystal.json");
        if (crystalRepresentation != null) {
            Entity crystal = ServiceLocator.getEntityService().getNamedEntity("crystal");
            // CrystalFactory.createCrystal(crystalRepresentation.texture,
            // crystalRepresentation.name);
            if (crystal == null) {
                return;
            }
            crystal.setPosition(crystalRepresentation.position);
            for (int i = crystalStats.level; i < crystalRepresentation.level; i++) {
                CrystalService.upgradeCrystal();
            }
            crystal.getComponent(CombatStatsComponent.class).setHealth(crystalRepresentation.health);
        }
        logger.debug("End Loading Crystal");
    }

    /**
     * saves the player to a JSON file
     */
    private static void savePlayer() {
        logger.debug("Begin Saving Player");
        String name = "player";
        Entity player = ServiceLocator.getEntityService().getNamedEntity(name);
        if (player == null) {
            return;
        }
        // save player status - look at CareTaker and Memento which would for sure be
        // the more elegant way to do this
        // in sprint 4, but I don't want to mess with that code this sprint to avoid
        // conflicts
        // lmao this wont work if you close the game at which point caretaker/memento would need to be seralized which is just a further extension of this and is more work
        HashMap<String, Object> status = new HashMap();
        status.put("gold", player.getComponent(InventoryComponent.class).getGold());
        status.put("stone", player.getComponent(InventoryComponent.class).getStone());
        status.put("wood", player.getComponent(InventoryComponent.class).getWood());
        status.put("health", player.getComponent(CombatStatsComponent.class).getHealth());
        status.put("items", player.getComponent(InventoryComponent.class).getItems());
        status.put("attack", player.getComponent(CombatStatsComponent.class).getAttackMultiplier());
        status.put("defence", player.getComponent(CombatStatsComponent.class).getBaseDefense());
        status.put("weapon", player.getComponent(InventoryComponent.class).getWeapon());
        status.put("armor", player.getComponent(InventoryComponent.class).getArmor());
        status.put("equipmentList", player.getComponent(InventoryComponent.class).getEquipmentList());
        status.put("buildings", player.getComponent(InventoryComponent.class).getBuildings());

        Tuple p = new Tuple().setPosition(player.getPosition()).setName(name).setPlayerState(status);
        FileLoader.writeClass(p, savePathPlayer, FileLoader.Location.LOCAL);
        logger.debug("End Saving Player");
    }

    /**
     * Loads the player from the JSON file. Assumes the player already exists in the
     * game engine
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
            player.getComponent(CombatStatsComponent.class).setAttackMultiplier((int) d.get("attack"));
            player.getComponent(CombatStatsComponent.class).setBaseDefense((int) d.get("defence"));
            player.getComponent(InventoryComponent.class).setWeapon((Equipments) d.get("weapon"));
            player.getComponent(InventoryComponent.class).setArmor((Equipments) d.get("armor"));

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
        DayNightCycleService savedDayNightCycle = FileLoader.readClass(DayNightCycleService.class, saveGameData,
                FileLoader.Location.LOCAL);
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
        saveCrystal();
        savePlayer();
        saveEnvironmentalObjects();
        //structureGenerationSetUp();
        saveStructures();

        saveGameData();

        logger.debug("Finished Saving");
    }

    /**
     * Load all game assets from the save game folder
     */
    public static void loadGameState() {
        logger.debug("Begin Loading");
        try {

            loadStructures();
            loadEnvrionmentalObjects();
            loadCrystal();
            loadPlayer();

            //loadGameData();

        } catch (InvocationTargetException | IllegalAccessException | NoSuchMethodException ignored) {
            logger.error("ERROR OCCURED: " + ignored);
        }
        logger.debug("Finished Loading");
    }

}
