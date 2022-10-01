package com.deco2800.game.memento;

import com.deco2800.game.components.shop.artefacts.Artefact;
import com.deco2800.game.components.shop.equipments.Equipments;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class OriginatorTest {
    @Test
    void shouldGetState() {
        Originator test = new Originator(1);
        assertEquals(1, test.getState());
    }

    @Test
    void shouldSaveGold() {
        Originator test = new Originator(1);
        test.setGold(100);
        assertEquals(100, test.getGold());
    }

    @Test
    void shouldSaveWood() {
        Originator test = new Originator(1);
        test.setWood(100);
        assertEquals(100, test.getWood());
    }

    @Test
    void shouldSaveHealth() {
        Originator test = new Originator(1);
        test.setCurrentHealth(100);
        assertEquals(100, test.getCurrentHealth());
    }

    @Test
    void shouldSaveAttack() {
        Originator test = new Originator(1);
        test.setAttack(100);
        assertEquals(100, test.getAttack());
    }

    @Test
    void shouldSaveStone() {
        Originator test = new Originator(1);
        test.setStone(100);
        assertEquals(100, test.getStone());
    }

    @Test
    void shouldSaveEmptyItemList() {
        Originator test = new Originator(1);
        HashMap<Artefact, Integer> item = new HashMap<>();
        test.setItems(item);
        assertTrue(item.equals(test.getItemList()));
    }

    @Test
    void shouldSaveWeapon() {
        Originator test = new Originator(1);
        test.setWeapon(Equipments.AXE);
        assertEquals(Equipments.AXE, test.getWeapon());
    }

    @Test
    void shouldSaveArmor() {
        Originator test = new Originator(1);
        test.setArmor(Equipments.CHESTPLATE);
        assertEquals(Equipments.CHESTPLATE, test.getArmor());
    }

    @Test
    void shouldSaveArmorInList() {
        Originator test = new Originator(1);
        List<Equipments> equipmentsList = new ArrayList<>();
        equipmentsList.add(Equipments.CHESTPLATE);
        test.setEquipmentsList(equipmentsList);
        assertEquals(Equipments.CHESTPLATE, test.getEquipmentsList().get(0));
    }

    @Test
    void shouldSaveDefense() {
        Originator test = new Originator(1);
        test.setDefense(10);
        assertEquals(10, test.getDefense());
    }
}
