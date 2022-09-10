package com.deco2800.game.components.shop.equipments;

import java.util.Arrays;
import java.util.List;

public enum Equipments {
    AXE,
    SWORD,
    BOW_AND_ARROW,
    TRIDENT,
    SHIELD,
    LV1_CHESTPLATE,
    LV2_CHESTPLATE,
    LV3_CHESTPLATE,
    LV1_HELMET,
    LV2_HELMET,
    LV3_HELMET;

    public static String getFilepath(Equipments equipment) {
        return switch (equipment) {
            case AXE -> "configs/equipments/axe.json";
            case SWORD -> "configs/equipments/sword.json";
            case BOW_AND_ARROW -> "configs/equipments/bowAndArrow.json";
            case TRIDENT -> "configs/equipments/triton.json";
            case SHIELD -> "configs/equipments/shield.json";
            case LV1_CHESTPLATE -> "configs/equipments/DummyArmor.json";
            case LV2_CHESTPLATE -> "configs/equipments/DummyArmor.json";
            case LV3_CHESTPLATE -> "configs/equipments/DummyArmor.json";
            case LV1_HELMET -> "configs/equipments/DummyArmor.json";
            case LV2_HELMET -> "configs/equipments/DummyArmor.json";
            case LV3_HELMET -> "configs/equipments/DummyArmor.json";
        };
    }

    public static List<Equipments> getAllEquipmentTypes() {
        List<Equipments> all = Arrays.asList(Equipments.class.getEnumConstants());
        return all;
    }

}
