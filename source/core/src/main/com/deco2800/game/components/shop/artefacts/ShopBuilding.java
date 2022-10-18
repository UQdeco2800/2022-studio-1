package com.deco2800.game.components.shop.artefacts;

import java.util.Arrays;
import java.util.List;

public enum ShopBuilding {
    WALL,
    TOWER1,
    TOWER2,
    TOWER3,
    TRAP1,
    TRAP2,
    WOODCUTTER,
    QUARRY;

    public static String getFilepath(ShopBuilding shopBuilding) {
        return switch (shopBuilding) {
            case WALL -> "configs/buildings/wall.json";
            case TOWER1 -> "configs/buildings/tower1.json";
            case TOWER2 -> "configs/buildings/tower2.json";
            case TOWER3 -> "configs/buildings/tower3.json";
            case TRAP1 -> "configs/buildings/trap1.json";
            case TRAP2 -> "configs/buildings/trap2.json";
            case WOODCUTTER -> "configs/buildings/woodCutter.json";
            case QUARRY -> "configs/buildings/quarry.json";
        };
    }

    public static List<ShopBuilding> getAllBuildingTypes() {
        List<ShopBuilding> all = Arrays.asList(ShopBuilding.class.getEnumConstants());
        return all;
    }

}
