package com.deco2800.game.memento;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;

class CareTakerTest {
    @Test
    void addMementoToRecord() {
        Memento dummy = new Memento(1, 2, 3, 4,5, new HashMap(), 5, 6
                ,null, null, null);
        CareTaker test = CareTaker.getInstance();
        test.add(dummy);
        assertEquals(dummy, test.getLast());
    }

    @Test
    void getMementoFromRecord() {
        Memento dummy1 = new Memento(1, 2, 3, 4,5, new HashMap(), 5,
                6,null, null, null);
        Memento dummy2 = new Memento(2, 3, 4, 5,6, new HashMap(), 7,
                8,null, null, null);
        CareTaker test = CareTaker.getInstance();
        test.add(dummy1);
        assertEquals(dummy1, test.getLast());
        test.add(dummy2);
        assertEquals(dummy2, test.getLast());
        assertNotEquals(dummy1, test.getLast());
    }
}
