package com.deco2800.game.memento;
import com.deco2800.game.components.shop.artefacts.Artefact;
import com.deco2800.game.components.shop.artefacts.BestSword;
import org.junit.jupiter.api.Test;
import org.mockito.internal.matchers.Or;

import java.util.ArrayList;
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
        List<Artefact> item = new ArrayList<>();
        test.setItems(item);
        assertTrue(item.equals(test.getItemList()));
    }

    @Test
    void shouldSaveNonEmptyItemList() {
        Originator test = new Originator(1);
        List<Artefact> item = new ArrayList<>();
        item.add(new BestSword());
        test.setItems(item);
        assertTrue(item.equals(test.getItemList()));
    }
}
