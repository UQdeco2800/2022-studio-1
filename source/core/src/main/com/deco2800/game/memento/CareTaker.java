package com.deco2800.game.memento;

import java.util.ArrayList;
import java.util.List;

/**
 * the caretaker object records and stores the player state into the internal player for the engine to retrieve in the
 * future
 */
public class CareTaker {
    private List<Memento> mementoList = new ArrayList<Memento>();

    /**
     * adds the selected memento into the caretaker
     * @param state - the memento to be stored
     */
    public void add(Memento state){
        mementoList.add(state);
    }

    /**
     * retrieve specific memento based on the id
     * @param index - id of the memento
     * @return - selected memento
     */
    public Memento get(int index){
        return mementoList.get(index);
    }

    /**
     * retrieve the entire list of the caretaker
     * @return - the list of the previously saved memento
     */
    public List<Memento> getAll() {
        return mementoList;
    }
}
