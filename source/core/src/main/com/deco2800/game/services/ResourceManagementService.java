package com.deco2800.game.services;

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

/**
 * Manages automatic resource generation for resource buildings (stone quarries and wood cutters)
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

    /**
     * Checks if it is morning and that it's not the first day. Triggers resource collection if so
     */
    public void triggerCollectEvent() {
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
