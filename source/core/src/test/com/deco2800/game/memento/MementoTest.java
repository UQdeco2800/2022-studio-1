package com.deco2800.game.memento;

import com.deco2800.game.components.shop.artefacts.Artefact;
import com.deco2800.game.components.shop.artefacts.BestSword;
import com.deco2800.game.components.shop.equipments.Equipments;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class MementoTest {

    @Test
    void shouldGetState() {
        Memento test = new Memento(1, 2, 3, 4,5, new ArrayList<>(), 5, 6,
                null, null, null);
        assertEquals(1, test.getState());
    }

    @Test
    void shouldGetGold() {
        Memento test = new Memento(1, 2, 3, 4,5, new ArrayList<>(), 5, 6,
                null, null, null);
        assertEquals(2, test.getGold());
    }

    @Test
    void shouldGetHealth() {
        Memento test = new Memento(1, 2, 3, 4,5, new ArrayList<>(), 5, 6,
                null, null, null);
        assertEquals(5, test.getCurrentHealth());
    }

    @Test
    void shouldGetEmptyItem() {
        List<Artefact> item = new ArrayList<>();
        Memento test = new Memento(1, 2, 3, 4,5, item, 5, 6,
                null, null, null);
        assertTrue(item.equals(test.getItemList()));
    }

    @Test
    void shouldGetItemList() {
        List<Artefact> item = new ArrayList<>();
        item.add(new BestSword());
        Memento test = new Memento(1, 2, 3, 4,5, item, 5, 6,
                null, null, null);
        assertTrue(item.equals(test.getItemList()));
    }

    @Test
    void shouldGetAttack() {
        Memento test = new Memento(1, 2, 3, 4,5, new ArrayList<>(), 5, 6,
                null, null, null);
        assertEquals(5, test.getAttack());
    }

    @Test
    void shouldGetStone() {
        Memento test = new Memento(1, 2, 3, 4,5, new ArrayList<>(), 5, 6,
                null, null, null);
        assertEquals(3, test.getStone());
    }

    @Test
    void shouldGetWood() {
        Memento test = new Memento(1, 2, 3, 4,5, new ArrayList<>(), 5, 6,
                null, null, null);
        assertEquals(4, test.getWood());
    }

    @Test
    void shouldGetDefense() {
        Memento test = new Memento(1, 2, 3, 4,5, new ArrayList<>(), 5, 6,
                null, null, null);
        assertEquals(6, test.getDefense());
    }

    @Test
    void shouldGetAxe() {
        Memento test = new Memento(1, 2, 3, 4,5, new ArrayList<>(), 5, 6,
                Equipments.AXE, null, null);
        assertEquals(Equipments.AXE, test.getWeapon());
    }

    @Test
    void shouldGetChestplate() {
        Memento test = new Memento(1, 2, 3, 4,5, new ArrayList<>(), 5, 6,
                null, Equipments.LV1_CHESTPLATE, null);
        assertEquals(Equipments.LV1_CHESTPLATE, test.getChestplate());
    }

    @Test
    void shouldGetHelmet() {
        Memento test = new Memento(1, 2, 3, 4,5, new ArrayList<>(), 5, 6,
                null, null, Equipments.LV1_HELMET);
        assertEquals(Equipments.LV1_HELMET, test.getHelmet());
    }

}
