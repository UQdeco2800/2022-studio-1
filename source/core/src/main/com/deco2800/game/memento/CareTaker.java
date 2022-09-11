package com.deco2800.game.memento;

import java.util.ArrayList;
import java.util.List;

/**
 * the caretaker object records and stores the player state into the internal memory for the engine to retrieve in the
 * future
 */
public class CareTaker {
    private static CareTaker playerStatus = null;
    private List<Memento> mementoList = new ArrayList<Memento>();

    public static CareTaker getInstance() {
        if (playerStatus == null) {
            playerStatus = new CareTaker();
        }
        return playerStatus;
    }

    public static void deleteAll() {
        playerStatus = null;
    }
    /**
     * adds the selected memento into the caretaker
     * @param state - the memento to be stored
     */
    public void add(Memento state){
        mementoList.add(state);
    }

    /**
     * retrieve the last memento saved
     * @return - the last memento
     */
    public Memento getLast(){
        if (mementoList.size() == 0) {
            return null;
        }

        return mementoList.get(mementoList.size() - 1);
    }

    /**
     * retrieve the entire list of the caretaker
     * @return - the list of the previously saved memento
     */
    public int size() {
        return mementoList.size();
    }
}
