package com.deco2800.game.services;

        import com.badlogic.gdx.scenes.scene2d.ui.Image;
        import com.badlogic.gdx.scenes.scene2d.ui.Label;
        import com.badlogic.gdx.utils.Array;
        import com.badlogic.gdx.utils.Disposable;
        import com.deco2800.game.areas.MainArea;
        import com.deco2800.game.components.infrastructure.ResourceBuilding;
        import com.deco2800.game.components.infrastructure.ResourceType;
        import com.deco2800.game.components.player.InventoryComponent;
        import com.deco2800.game.components.player.PlayerStatsDisplay;
        import com.deco2800.game.entities.Entity;
        import com.deco2800.game.entities.EntityService;
        import com.deco2800.game.events.EventHandler;
        import org.slf4j.Logger;
        import org.slf4j.LoggerFactory;

        import java.util.ArrayList;
        import java.util.HashMap;
        import java.util.Map;

/**
 * Service for loading resources, e.g. textures, texture atlases, sounds, music, etc. Add new load
 * methods when new types of resources are added to the game.
 */
public class ResourceManagementService {

    private static final Logger logger = LoggerFactory.getLogger(ResourceManagementService.class);

    EventHandler resourceEvents = new EventHandler();

    public ResourceManagementService() {
        ServiceLocator.getDayNightCycleService().getEvents().addListener("morning", this::triggerCollectEvent);
    }

    public int getWoodBuildings() {
        int count = 0;
        for (String i : ServiceLocator.getStructureService().getAllNamedEntities().keySet()) {
            if (i.contains("woodCutter")) {
                count += 1;
            }
        }
        return count;
    }

    public int getStoneBuildings() {
        int count = 0;
        for (String i : ServiceLocator.getStructureService().getAllNamedEntities().keySet()) {
            if (i.contains("stonequarry")) {
                count += 1;
            }
        }
        return count;
    }

    public void triggerCollectEvent() {
        // checks if it is morning and that it's not the first day.
        // first day check has been disabled temporarily for ease of debugging
        if (ServiceLocator.getDayNightCycleService().getCurrentCycleStatus() == DayNightCycleStatus.DAY) {
                //&& ServiceLocator.getDayNightCycleService().getCurrentDayNumber() != 1) {
            collectResources();
        }
    }

    /**
     * Retrieves the resources produced and adds them to the players inventory.
     */
    public void collectResources() {
        MainArea.getInstance().getGameArea().getPlayer().getComponent(InventoryComponent.class).addResources(ResourceType.WOOD,
                ServiceLocator.getResourceManagementService().getWoodBuildings() * 20);
        MainArea.getInstance().getGameArea().getPlayer().getComponent(InventoryComponent.class).addResources(ResourceType.STONE,
                ServiceLocator.getResourceManagementService().getStoneBuildings() * 20);
        PlayerStatsDisplay.updateItems();
    }


    public EventHandler getEvents() {
        return resourceEvents;
    }
}
