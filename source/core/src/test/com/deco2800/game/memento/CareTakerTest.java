package com.deco2800.game.memento;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class CareTakerTest {
    @Test
    void addMementoToRecord() {
        Memento dummy = new Memento(1, 2, 3, 4, new ArrayList<>(), 5);
        CareTaker test = new CareTaker();
        test.add(dummy);
        assertEquals(dummy, test.get(0));
    }

    @Test
    void getMementoFromRecord() {
        Memento dummy1 = new Memento(1, 2, 3, 4, new ArrayList<>(), 5);
        Memento dummy2 = new Memento(2, 3, 4, 5, new ArrayList<>(), 6);
        CareTaker test = new CareTaker();
        test.add(dummy1);
        test.add(dummy2);
        assertEquals(dummy1, test.get(0));
        assertEquals(dummy2, test.get(1));
        assertNotEquals(dummy1, test.get(1));
    }

    @Test
    void getAllMementoFromRecord() {
        Memento dummy1 = new Memento(1, 2, 3, 4, new ArrayList<>(), 5);
        Memento dummy2 = new Memento(2, 3, 4, 5, new ArrayList<>(), 6);
        CareTaker test = new CareTaker();
        test.add(dummy1);
        test.add(dummy2);
        assertEquals(dummy1, test.getAll().get(0));
        assertNotEquals(dummy1, test.getAll().get(1));
        assertEquals(dummy2, test.getAll().get(1));
    }
}
