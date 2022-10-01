package com.deco2800.game.services;

import com.badlogic.gdx.math.Vector2;
import com.deco2800.game.areas.MainArea;
import com.deco2800.game.entities.Entity;

public class RangeService {

    public RangeService() {}

    public Boolean playerInRangeOf (Entity toCompare) {

        boolean inRange = false;
        Vector2 entityPos = toCompare.getPosition();
        Vector2 playerPos = MainArea.getInstance().getGameArea().getPlayer().getPosition();

        if (Math.abs(playerPos.x - entityPos.x) < 5 && Math.abs(playerPos.y - entityPos.y) < 5) {
            inRange = true;
        }

        return inRange;

    }


}
