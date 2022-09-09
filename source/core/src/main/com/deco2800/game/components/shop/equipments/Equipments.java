package com.deco2800.game.components.shop.equipments;

public enum Equipments {
    AXE,
    SWORD,
    BOW_AND_ARROW,
    TRIDENT,
    LV1_CHESTPLATE,
    LV2_CHESTPLATE,
    LV3_CHESTPLATE,
    LV1_HELMET,
    LV2_HELMET,
    LV3_HELMET;


    public static String getFilepath(Equipments equipment) {
        return switch (equipment) {
            case AXE -> "configs/equipments/DummyWeapon.json";
            case SWORD -> "configs/equipments/DummyWeapon.json";
            case BOW_AND_ARROW -> "configs/equipments/DummyWeapon.json";
            case TRIDENT -> "configs/equipments/DummyWeapon.json";
            case LV1_CHESTPLATE -> "configs/equipments/DummyArmor.json";
            case LV2_CHESTPLATE -> "configs/equipments/DummyArmor.json";
            case LV3_CHESTPLATE -> "configs/equipments/DummyArmor.json";
            case LV1_HELMET -> "configs/equipments/DummyArmor.json";
            case LV2_HELMET -> "configs/equipments/DummyArmor.json";
            case LV3_HELMET -> "configs/equipments/DummyArmor.json";
        };
    }

}
