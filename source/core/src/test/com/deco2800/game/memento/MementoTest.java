package com.deco2800.game.memento;

import com.deco2800.game.components.shop.artefacts.Artefact;
import com.deco2800.game.components.shop.artefacts.ShopBuilding;
import com.deco2800.game.components.shop.equipments.Equipments;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class MementoTest {

    @Test
    void shouldGetState() {
        Memento test = new Memento(1, 2, 3, 4, 5, null, null, 5,
                6,null, null, null);
        assertEquals(1, test.getState());
    }

    @Test
    void shouldGetGold() {
        Memento test = new Memento(1, 2, 3, 4, 5, null, null, 5,
                6,null, null, null);
        assertEquals(2, test.getGold());
    }

    @Test
    void shouldGetHealth() {
        Memento test = new Memento(1, 2, 3, 4, 5, null, null , 5,
                6,null, null, null);
        assertEquals(5, test.getCurrentHealth());
    }

    @Test
    void shouldGetItem() {
        HashMap<Artefact, Integer> item = new HashMap<>();
        item.put(Artefact.HEALTH_POTION, 4);
        Memento test = new Memento(1, 2, 3, 4, 5, item, null, 5,
                6,null, null, null);
        assertEquals(4, item.get(Artefact.HEALTH_POTION));
    }

    @Test
    void shouldGetAttack() {
        Memento test = new Memento(1, 2, 3, 4, 5, null, null, 5,
                6,null, null, null);
        assertEquals(5, test.getAttack());
    }

    @Test
    void shouldGetStone() {
        Memento test = new Memento(1, 2, 3, 4, 5, null, null, 5,
                6,null, null, null);
        assertEquals(3, test.getStone());
    }

    @Test
    void shouldGetWood() {
        Memento test = new Memento(1, 2, 3, 4, 5, null, null, 5,
                6,null, null, null);
        assertEquals(4, test.getWood());
    }

    @Test
    void shouldGetDefense() {
        Memento test = new Memento(1, 2, 3, 4, 5, null, null, 5,
                6,null, null, null);
        assertEquals(6, test.getDefense());
    }

    @Test
    void shouldGetAxe() {
        Memento test = new Memento(1, 2, 3, 4, 5, null, null, 5,
                6,Equipments.AXE, null, null);
        assertEquals(Equipments.AXE, test.getWeapon());
    }

    @Test
    void shouldGetArmor() {
        Memento test = new Memento(1, 2, 3, 4, 5, null, null, 5,
                6,null, Equipments.CHESTPLATE, null);
        assertEquals(Equipments.CHESTPLATE, test.getArmor());
    }

    @Test
    void shouldGetItemList() {
        HashMap<Artefact, Integer> dummy = new HashMap<>();
        dummy.put(Artefact.HEALTH_POTION, 1);
        Memento test = new Memento(1, 2, 3, 4, 5, dummy, null, 5,
                6, null, null, null);
        assertEquals(1, test.getItemList().get(Artefact.HEALTH_POTION));
    }

    @Test
    void shouldGetEquipmentList() {
        List<Equipments> dummy = new ArrayList<>();
        dummy.add(Equipments.CHESTPLATE);
        Memento test = new Memento(1, 2, 3, 4, 5, null, null , 5,
                6,null, null, dummy);
        assertEquals(Equipments.CHESTPLATE, test.getEquipmentsList().get(0));
    }

    @Test
    void shouldGetBuildings() {
        HashMap<ShopBuilding, Integer> dummy = new HashMap<>();
        dummy.put(ShopBuilding.TOWER1, 3);
        Memento test = new Memento(1, 2, 3, 4, 5, null, dummy , 5,
                6,null, null, null);
        assertEquals(3, test.getBuildings().get(ShopBuilding.TOWER1));
    }


}
