package com.deco2800.game.services;

public class DayNightCycleService {
    private boolean ended;
    //private CycleStatus currentCycleStatus;
    private int currentDayNumber;
    private long currentDayMillis;

    public DayNightCycleService(GameTime timer) {
        this.ended = false;
        this.currentDayNumber = 0;
        this.currentDayMillis = timer.getTime();
    }

    /*
     * public CycleStatus getCurrentCycleStatus() {
     *     return this.currentCycleStatus;
     * }
     */

    public int getCurrentDayNumber() {
        return this.currentDayNumber;
    }

    public long getCurrentDayMillis() {
        return this.currentDayMillis;
    }

    public boolean hasEnded() {
        return this.ended;
    }

    public void start() {

    }

    public void pause() {

    }

    public void run() {

    }
}
