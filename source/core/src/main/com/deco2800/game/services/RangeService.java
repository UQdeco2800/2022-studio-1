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
     * are in the 8 squares around it + the players square: (Under Player, Top Left, Directly Above, Top Right, Right, Bottom Right, Directly Bellow, Bottom Left, Left)
     * @param middle the entity whose perimeter you want to find
     * @return an array list containing the perimeter in the form above
     */
    public ArrayList<Entity> perimeter(Entity middle, GridPoint2 gridPos) {

        ArrayList<Entity> radialPerimeter = new ArrayList<>();
//        Vector2 entityPos = middle.getPosition();
//        GridPoint2 gridPos =
//                ServiceLocator.getEntityService().getNamedEntity("terrain").getComponent(TerrainComponent.class).worldToTilePosition(entityPos.x, entityPos.y);
        Equipments invent = MainArea.getInstance().getGameArea().getPlayer().getComponent(InventoryComponent.class).getWeapon();

        // Add whats under the player
        GridPoint2 stringUnderThePlayer = new GridPoint2(gridPos.x, gridPos.y);
        Entity underThePlayer = ServiceLocator.getUGSService().getEntity(stringUnderThePlayer);
        radialPerimeter.add(underThePlayer);

        // Add the top row of the first perimeter (right to left)
        GridPoint2 stringPlayerAboveLeft = new GridPoint2(gridPos.x - 1, gridPos.y - 1);
        Entity aboveLeft = ServiceLocator.getUGSService().getEntity(stringPlayerAboveLeft);
        radialPerimeter.add(aboveLeft);
        GridPoint2 stringPlayerAbove = new GridPoint2(gridPos.x, gridPos.y - 1);
        Entity above = ServiceLocator.getUGSService().getEntity(stringPlayerAbove);
        radialPerimeter.add(above);
        GridPoint2 stringPlayerAboveRight = new GridPoint2(gridPos.x + 1, gridPos.y - 1);
        Entity aboveRight = ServiceLocator.getUGSService().getEntity(stringPlayerAboveRight);
        radialPerimeter.add(aboveRight);

        // Add the right side of the first perimeter
        GridPoint2 stringPlayerRight = new GridPoint2(gridPos.x + 1, gridPos.y);
        Entity right = ServiceLocator.getUGSService().getEntity(stringPlayerRight);
        radialPerimeter.add(right);

        // Add the bottom row of the first perimeter (left to right)
        GridPoint2 stringPlayerBottomRight = new GridPoint2(gridPos.x + 1, gridPos.y + 1);
        Entity bottomRight = ServiceLocator.getUGSService().getEntity(stringPlayerBottomRight);
        radialPerimeter.add(bottomRight);
        GridPoint2 stringPlayerBottom = new GridPoint2(gridPos.x, gridPos.y + 1);
        Entity bottom = ServiceLocator.getUGSService().getEntity(stringPlayerBottom);
        radialPerimeter.add(bottom);
        GridPoint2 stringPlayerBottomLeft = new GridPoint2(gridPos.x - 1, gridPos.y + 1);
        Entity bottomLeft = ServiceLocator.getUGSService().getEntity(stringPlayerBottomLeft);
        radialPerimeter.add(bottomLeft);

        // Add the left side of the first perimeter
        GridPoint2 stringPlayerLeft = new GridPoint2(gridPos.x - 1, gridPos.y);
        Entity left = ServiceLocator.getUGSService().getEntity(stringPlayerLeft);
        radialPerimeter.add(left);

        if (invent.equals(Equipments.BOW_AND_ARROW)) {
            // Add the top row of the second perimeter (right to left)
            GridPoint2 stringPlayerTopTopLeft = new GridPoint2(gridPos.x - 2, gridPos.y + 2);
            Entity topTopLeft = ServiceLocator.getUGSService().getEntity(stringPlayerTopTopLeft);
            radialPerimeter.add(topTopLeft);
            GridPoint2 stringPlayerTopTopMidLeft = new GridPoint2(gridPos.x - 1, gridPos.y + 2);
            Entity topTopMidLeft = ServiceLocator.getUGSService().getEntity(stringPlayerTopTopMidLeft);
            radialPerimeter.add(topTopMidLeft);
            GridPoint2 stringPlayerTopTopMid = new GridPoint2(gridPos.x, gridPos.y + 2);
            Entity topTopMid = ServiceLocator.getUGSService().getEntity(stringPlayerTopTopMid);
            radialPerimeter.add(topTopMid);
            GridPoint2 stringPlayerTopTopMidRight = new GridPoint2(gridPos.x + 1, gridPos.y + 2);
            Entity topTopMidRight = ServiceLocator.getUGSService().getEntity(stringPlayerTopTopMidRight);
            radialPerimeter.add(topTopMidRight);
            GridPoint2 stringPlayerTopTopRight = new GridPoint2(gridPos.x + 2, gridPos.y + 2);
            Entity topTopRight = ServiceLocator.getUGSService().getEntity(stringPlayerTopTopRight);
            radialPerimeter.add(topTopRight);

            // Add right side of the second perimeter (top to bottom)
            GridPoint2 stringPlayerRightRightTop = new GridPoint2(gridPos.x + 2, gridPos.y + 1);
            Entity rightRightTop = ServiceLocator.getUGSService().getEntity(stringPlayerRightRightTop);
            radialPerimeter.add(rightRightTop);
            GridPoint2 stringPlayerRightRightMid = new GridPoint2(gridPos.x + 2, gridPos.y);
            Entity rightRightMid = ServiceLocator.getUGSService().getEntity(stringPlayerRightRightMid);
            radialPerimeter.add(rightRightMid);
            GridPoint2 stringPlayerRightRightBot = new GridPoint2(gridPos.x + 2, gridPos.y - 1);
            Entity rightRightBot = ServiceLocator.getUGSService().getEntity(stringPlayerRightRightBot);
            radialPerimeter.add(rightRightBot);

            // Add bottom side of the second perimeter (left to right)
            GridPoint2 stringPlayerBotBotRight = new GridPoint2(gridPos.x + 2, gridPos.y - 2);
            Entity botBotRight = ServiceLocator.getUGSService().getEntity(stringPlayerBotBotRight);
            radialPerimeter.add(botBotRight);
            GridPoint2 stringPlayerBotBotMidRight = new GridPoint2(gridPos.x + 1, gridPos.y - 2);
            Entity botBotMidRight = ServiceLocator.getUGSService().getEntity(stringPlayerBotBotMidRight);
            radialPerimeter.add(botBotMidRight);
            GridPoint2 stringPlayerBotBotMid = new GridPoint2(gridPos.x, gridPos.y - 2);
            Entity botBotMid = ServiceLocator.getUGSService().getEntity(stringPlayerBotBotMid);
            radialPerimeter.add(botBotMid);
            GridPoint2 stringPlayerBotBotMidLeft = new GridPoint2(gridPos.x - 1, gridPos.y - 2);
            Entity botBotMidLeft = ServiceLocator.getUGSService().getEntity(stringPlayerBotBotMidLeft);
            radialPerimeter.add(botBotMidLeft);
            GridPoint2 stringPlayerBotBotLeft = new GridPoint2(gridPos.x - 2, gridPos.y - 2);
            Entity botBotLeft = ServiceLocator.getUGSService().getEntity(stringPlayerBotBotLeft);
            radialPerimeter.add(botBotLeft);

            // Add left side of the second perimeter (bottom to top)
            GridPoint2 stringPlayerLeftLeftBot = new GridPoint2(gridPos.x - 2, gridPos.y - 1);
            Entity leftLeftBot = ServiceLocator.getUGSService().getEntity(stringPlayerLeftLeftBot);
            radialPerimeter.add(leftLeftBot);
            GridPoint2 stringPlayerLeftLeftMid = new GridPoint2(gridPos.x - 2, gridPos.y);
            Entity leftLeftMid = ServiceLocator.getUGSService().getEntity(stringPlayerLeftLeftMid);
            radialPerimeter.add(leftLeftMid);
            GridPoint2 stringPlayerLeftLeftTop = new GridPoint2(gridPos.x - 1, gridPos.y);
            Entity leftLeftTop = ServiceLocator.getUGSService().getEntity(stringPlayerLeftLeftTop);
            radialPerimeter.add(leftLeftTop);
        }


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
        GridPoint2 playerPos = ServiceLocator.getEntityService().getNamedEntity("terrain").getComponent(TerrainComponent.class).worldToTilePosition(player.getPosition().x, player.getPosition().y + 1);
        ArrayList<Entity> ugs = registeredInUGS();
        boolean inRange = false;
        if (ugs.contains(toCompare)) {
            ArrayList<Entity> aroundPlayer = perimeter(player, playerPos);
            if (aroundPlayer.contains(toCompare)) {
                inRange = true;
            }
        }
        return inRange;
    }


}
