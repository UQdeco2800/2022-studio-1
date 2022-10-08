package com.deco2800.game.services;

import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.Vector2;
import com.deco2800.game.areas.MainArea;
import com.deco2800.game.areas.terrain.TerrainComponent;
import com.deco2800.game.components.player.InventoryComponent;
import com.deco2800.game.components.shop.artefacts.Artefact;
import com.deco2800.game.components.shop.equipments.Equipments;
import com.deco2800.game.entities.Entity;
import com.deco2800.game.entities.Tile;
import com.deco2800.game.entities.UGS;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

public class RangeService {

    /**
     * This function allows you to pass in an entity and retrieve an array containing every entity store in the UGS that
     * are in the 8 squares around it: (Top Left, Directly Above, Top Right, Right, Bottom Right, Directly Bellow, Bottom Left, Left)
     * @param middle the entity whose perimeter you want to find
     * @return an array list containing the perimeter in the form above
     */
    public ArrayList<Entity> perimeter(Entity middle) {
        ArrayList<Entity> radialPerimeter = new ArrayList<>();
        Vector2 entityPos = middle.getPosition();
        GridPoint2 gridPos =
                ServiceLocator.getEntityService().getNamedEntity("terrain").getComponent(TerrainComponent.class).worldToTilePosition(entityPos.x, entityPos.y);

        GridPoint2 stringPlayerAboveLeft = new GridPoint2(gridPos.x - 1, gridPos.y - 1);
        Entity aboveLeft = ServiceLocator.getUGSService().getEntity(stringPlayerAboveLeft);
        radialPerimeter.add(aboveLeft);

        GridPoint2 stringPlayerAbove = new GridPoint2(gridPos.x, gridPos.y - 1);
        Entity above = ServiceLocator.getUGSService().getEntity(stringPlayerAbove);
        radialPerimeter.add(above);

        GridPoint2 stringPlayerAboveRight = new GridPoint2(gridPos.x + 1, gridPos.y - 1);
        Entity aboveRight = ServiceLocator.getUGSService().getEntity(stringPlayerAboveRight);
        radialPerimeter.add(aboveRight);

        GridPoint2 stringPlayerRight = new GridPoint2(gridPos.x + 1, gridPos.y);
        Entity right = ServiceLocator.getUGSService().getEntity(stringPlayerRight);
        radialPerimeter.add(right);

        GridPoint2 stringPlayerBottomRight = new GridPoint2(gridPos.x + 1, gridPos.y + 1);
        Entity bottomRight = ServiceLocator.getUGSService().getEntity(stringPlayerBottomRight);
        radialPerimeter.add(bottomRight);

        GridPoint2 stringPlayerBottom = new GridPoint2(gridPos.x, gridPos.y + 1);
        Entity bottom = ServiceLocator.getUGSService().getEntity(stringPlayerBottom);
        radialPerimeter.add(bottom);

        GridPoint2 stringPlayerBottomLeft = new GridPoint2(gridPos.x - 1, gridPos.y + 1);
        Entity bottomLeft = ServiceLocator.getUGSService().getEntity(stringPlayerBottomLeft);
        radialPerimeter.add(bottomLeft);

        GridPoint2 stringPlayerLeft = new GridPoint2(gridPos.x - 1, gridPos.y);
        Entity left = ServiceLocator.getUGSService().getEntity(stringPlayerLeft);
        radialPerimeter.add(left);

        return radialPerimeter;
    }

    /**
     * This function returns whats in the UGS in the tile where the player is standing. This is a short term solution
     * to the enemies not yet moving in the UGS.
     * @return the string generated key that is stored in the hashmap of where the player is standing
     */
    public GridPoint2 getPlayerTile() {
        Entity player = ServiceLocator.getEntityService().getNamedEntity("player");
        Vector2 playerPos = player.getPosition();
        GridPoint2 gPlayerPos = ServiceLocator.getEntityService().getNamedEntity("terrain").getComponent(TerrainComponent.class).worldToTilePosition(playerPos.x, playerPos.y);
        return gPlayerPos;
    }

    /**
     * This function filters through the UGS and returns a list of all entities that are registered in the UGS
     * @return an array containing all entities that are within the UGS
     */
    public ArrayList<Entity> registeredInUGS() {
        ArrayList<Entity> inUGS = new ArrayList<>();
        Collection<Tile> values = ServiceLocator.getUGSService().printUGS().values();
        for (Tile i : values) {
            if (i.getEntity() != null) {
                inUGS.add(i.getEntity());
            }
        }
         return inUGS;
    }

    /**
     * This function allows the user to check if an entity is in range of the player. So enemies can use this to attack
     * etc.
     * @param toCompare an entity that you want to see if its around the player
     * @return true if its within the players perimeter and false if its not
     */
    public Boolean playerInRangeOf (Entity toCompare) {
        Entity player = ServiceLocator.getEntityService().getNamedEntity("player");
        ArrayList<Entity> ugs = registeredInUGS();
        boolean inRange = false;
        if (ugs.contains(toCompare)) {
            ArrayList<Entity> aroundPlayer = perimeter(player);
            HashMap<Artefact, Integer> invent = MainArea.getInstance().getGameArea().getPlayer().getComponent(InventoryComponent.class).getItems();
            if (invent.containsKey(Equipments.BOW_AND_ARROW)) {
                if (aroundPlayer.contains(toCompare)) {
                    inRange = true;
                }
            } else /*if (invent.containsKey(Equipments.SWORD) || invent.containsKey(Equipments.AXE) || invent.containsKey(Equipments.TRIDENT)) */ {
                if (aroundPlayer.contains(toCompare)) {
                    inRange = true;
                }
            }
        }
        return inRange;
    }

    /**
     * This function was semi created to come back to in Sprint 4 to try and aid buildings team. Alot of it depreicated
     * with the new UGS
     * @param toCompare an entity that you want to see if its around the player
     */
//    public Boolean withinTowerRange (Entity toCompare) {
//        boolean inRange = false;
//        ArrayList<Entity> withinRange = new ArrayList<>();
//        for (String i : ServiceLocator.getEntityService().getAllNamedEntities().keySet()) {
//            if (i.contains("tower")) {
//                for (String j : ServiceLocator.getEntityService().getAllNamedEntities().keySet()) {
//                    if (j.contains("nemy")) {
//                        Vector2 towerPos = ServiceLocator.getEntityService().getAllNamedEntities().get(i).getPosition();
//                        Vector2 enemyPos = ServiceLocator.getEntityService().getAllNamedEntities().get(j).getPosition();
//                        if (Math.abs(towerPos.x - enemyPos.x) < 10 && Math.abs(towerPos.y - enemyPos.y) < 10) {
//                            withinRange.add(ServiceLocator.getEntityService().getAllNamedEntities().get(j));
//                        }
//                    }
//                }
//            }
//        }
//        // Then loop through all of the enemies within this "in tower range" list add "hit" them and deduct health
//        // Just dont know how to be constantly checking for this
//        // Could u add this as a listener event per clock tick?? idk?
//        return inRange;
//    }



}
