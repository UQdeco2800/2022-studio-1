package com.deco2800.game.components.shop.equipments;

import java.util.Arrays;
import java.util.List;

public enum Equipments {
    AXE,
    SWORD,
    BOW_AND_ARROW,
    TRIDENT,
    SHIELD,
    CHESTPLATE,
    HELMET;

    public static String getFilepath(Equipments equipment) {
        return switch (equipment) {
            case AXE -> "configs/equipments/axe.json";
            case SWORD -> "configs/equipments/sword.json";
            case BOW_AND_ARROW -> "configs/equipments/bowAndArrow.json";
            case TRIDENT -> "configs/equipments/triton.json";
            case SHIELD -> "configs/equipments/shield.json";
            case CHESTPLATE -> "configs/equipments/chestplate.json";
            case HELMET -> "configs/equipments/helmet.json";
        };
    }

    public static List<Equipments> getAllEquipmentTypes() {
        List<Equipments> all = Arrays.asList(Equipments.class.getEnumConstants());
        return all;
    }

}
