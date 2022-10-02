package com.deco2800.game.services;

import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.Vector2;
import com.deco2800.game.areas.MainArea;
import com.deco2800.game.areas.terrain.TerrainComponent;
import com.deco2800.game.components.maingame.MainGameActions;
import com.deco2800.game.components.player.InventoryComponent;
import com.deco2800.game.components.shop.artefacts.Artefact;
import com.deco2800.game.components.shop.equipments.Equipments;
import com.deco2800.game.entities.Entity;
import com.deco2800.game.entities.Tile;
import com.deco2800.game.entities.UGS;
import com.sun.tools.javac.Main;

import java.awt.image.AreaAveragingScaleFilter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;

public class RangeService {

    public ArrayList<Entity> perimeter(Entity middle) {
        ArrayList<Entity> radialPerimeter = new ArrayList<>();
        Vector2 entityPos = middle.getPosition();
        GridPoint2 gridPos = ServiceLocator.getEntityService().getNamedEntity("terrain").getComponent(TerrainComponent.class).worldToTilePosition(entityPos.x, entityPos.y);
        String stringPos = UGS.generateCoordinate(gridPos.x, gridPos.y);

        String stringPlayerAboveLeft = UGS.generateCoordinate(gridPos.x - 1, gridPos.y - 1);
        Entity aboveLeft = ServiceLocator.getUGSService().getEntity(stringPlayerAboveLeft);
        radialPerimeter.add(aboveLeft);

        String stringPlayerAbove = UGS.generateCoordinate(gridPos.x, gridPos.y - 1);
        Entity above = ServiceLocator.getUGSService().getEntity(stringPlayerAbove);
        radialPerimeter.add(above);

        String stringPlayerAboveRight = UGS.generateCoordinate(gridPos.x + 1, gridPos.y - 1);
        Entity aboveRight = ServiceLocator.getUGSService().getEntity(stringPlayerAboveRight);
        radialPerimeter.add(aboveRight);

        String stringPlayerRight = UGS.generateCoordinate(gridPos.x + 1, gridPos.y);
        Entity right = ServiceLocator.getUGSService().getEntity(stringPlayerRight);
        radialPerimeter.add(right);

        String stringPlayerBottomRight = UGS.generateCoordinate(gridPos.x + 1, gridPos.y + 1);
        Entity bottomRight = ServiceLocator.getUGSService().getEntity(stringPlayerBottomRight);
        radialPerimeter.add(bottomRight);

        String stringPlayerBottom = UGS.generateCoordinate(gridPos.x, gridPos.y + 1);
        Entity bottom = ServiceLocator.getUGSService().getEntity(stringPlayerBottom);
        radialPerimeter.add(bottom);

        String stringPlayerBottomLeft = UGS.generateCoordinate(gridPos.x - 1, gridPos.y + 1);
        Entity bottomLeft = ServiceLocator.getUGSService().getEntity(stringPlayerBottomLeft);
        radialPerimeter.add(bottomLeft);

        String stringPlayerLeft = UGS.generateCoordinate(gridPos.x - 1, gridPos.y);
        Entity left = ServiceLocator.getUGSService().getEntity(stringPlayerLeft);
        radialPerimeter.add(left);

        return radialPerimeter;
    }

    //using this to test if we can get the players new tile position. Important for range.
    public void testingPurposes() {
        String location = UGS.generateCoordinate(60, 60);
        Entity bing = ServiceLocator.getUGSService().getEntity(location);
        Vector2 playerPos = MainArea.getInstance().getGameArea().getPlayer().getPosition();
        GridPoint2 gridPlayerPos = ServiceLocator.getEntityService().getNamedEntity("terrain").getComponent(TerrainComponent.class).worldToTilePosition(playerPos.x, playerPos.y);
        String stringPlayerPos = UGS.generateCoordinate(gridPlayerPos.x, gridPlayerPos.y);
        Entity findOut = ServiceLocator.getUGSService().getEntity(stringPlayerPos);
    }

    public ArrayList<Entity> registeredInUGS() {
        ArrayList<Entity> inUGS = new ArrayList<>();
        for (Tile i : ServiceLocator.getUGSService().printUGS().values()) {
            if (i.getEntity() != null) {
                inUGS.add(i.getEntity());
            }
        }
        return inUGS;
    }



    public Boolean playerInRangeOf (Entity toCompare) {
        Entity player = MainArea.getInstance().getGameArea().getPlayer();
        ArrayList<Entity> ugs = registeredInUGS();
        ArrayList<Entity> aroundPlayer = perimeter(player);
        boolean inRange = false;
        Vector2 entityPos = toCompare.getPosition();
        Vector2 playerPos = MainArea.getInstance().getGameArea().getPlayer().getPosition();
        HashMap<Artefact, Integer> invent = MainArea.getInstance().getGameArea().getPlayer().getComponent(InventoryComponent.class).getItems();

        if (invent.containsKey(Equipments.BOW_AND_ARROW)) {
            if (Math.abs(playerPos.x - entityPos.x) < 15 && Math.abs(playerPos.y - entityPos.y) < 15) {
                inRange = true;
            }
        } else /*if (invent.containsKey(Equipments.SWORD) || invent.containsKey(Equipments.AXE) || invent.containsKey(Equipments.TRIDENT)) */{
            if (Math.abs(playerPos.x - entityPos.x) < 5 && Math.abs(playerPos.y - entityPos.y) < 5) {
                inRange = true;
            }
        }

        // DOES THE PLAYER AUTOMATTICALLY START WITH THE AXE?

        return inRange;
    }

    public void withinTowerRange (Entity toCompare) {
        ArrayList<Entity> withinRange = new ArrayList<>();
        for (String i : ServiceLocator.getEntityService().getAllNamedEntities().keySet()) {
            if (i.contains("tower")) {
                for (String j : ServiceLocator.getEntityService().getAllNamedEntities().keySet()) {
                    if (j.contains("nemy")) {
                        Vector2 towerPos = ServiceLocator.getEntityService().getAllNamedEntities().get(i).getPosition();
                        Vector2 enemyPos = ServiceLocator.getEntityService().getAllNamedEntities().get(j).getPosition();
                        if (Math.abs(towerPos.x - enemyPos.x) < 10 && Math.abs(towerPos.y - enemyPos.y) < 10) {
                            withinRange.add(ServiceLocator.getEntityService().getAllNamedEntities().get(j));
                        }
                    }
                }
            }
        }
        // Then loop through all of the enemies within this "in tower range" list add "hit" them and deduct health
        // Just dont know how to be constantly checking for this
        // Could u add this as a listener event per clock tick?? idk?
    }



}
