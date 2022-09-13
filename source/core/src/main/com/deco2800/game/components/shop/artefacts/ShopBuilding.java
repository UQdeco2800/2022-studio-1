package com.deco2800.game.components.shop.artefacts;

import java.util.Arrays;
import java.util.List;

public enum ShopBuilding {
    WALL,
    ATTACK_BUILDING,
    QUARRY;

    public static String getFilepath(ShopBuilding shopBuilding) {
        return switch (shopBuilding) {
            case WALL -> "configs/buildings/wall.json";
            case ATTACK_BUILDING -> "configs/buildings/attackBuilding.json";
            case QUARRY -> "configs/buildings/quarry.json";
        };
    }

    public static List<ShopBuilding> getAllBuildingTypes() {
        List<ShopBuilding> all = Arrays.asList(ShopBuilding.class.getEnumConstants());
        return all;
    }

}
