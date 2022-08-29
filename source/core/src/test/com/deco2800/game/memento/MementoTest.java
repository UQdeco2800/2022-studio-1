package com.deco2800.game.memento;
import com.deco2800.game.components.shop.artefacts.Artefact;
import com.deco2800.game.components.shop.artefacts.BestSword;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class MementoTest {

    @Test
    void shouldGetState() {
        Memento test = new Memento(1, 2, 3, new ArrayList<>(), 5);
        assertEquals(1, test.getState());
    }

    @Test
    void shouldGetGold() {
        Memento test = new Memento(1, 2, 3, new ArrayList<>(), 5);
        assertEquals(2, test.getGold());
    }

    @Test
    void shouldGetHealth() {
        Memento test = new Memento(1, 2, 3, new ArrayList<>(), 5);
        assertEquals(3, test.getCurrentHealth());
    }

    @Test
    void shouldGetEmptyItem() {
        List<Artefact> item = new ArrayList<>();
        Memento test = new Memento(1, 2, 3, item, 5);
        assertTrue(item.equals(test.getItemList()));
    }

    @Test
    void shouldGetItemList() {
        List<Artefact> item = new ArrayList<>();
        item.add(new BestSword());
        Memento test = new Memento(1, 2, 3, item, 5);
        assertTrue(item.equals(test.getItemList()));
    }

    @Test
    void shouldGetAttack() {
        Memento test = new Memento(1, 2, 3, new ArrayList<>(), 5);
        assertEquals(5, test.getAttack());
    }
}
