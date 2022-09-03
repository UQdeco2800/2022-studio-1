package com.deco2800.game.services;

/**
 * Service for managing the Day and Night cycle of the game.
 */
public class DayNightCycleService {
    private boolean ended;
    //private CycleStatus currentCycleStatus;
    private int currentDayNumber;
    private long currentDayMillis;

    public DayNightCycleService(GameTime timer) {
        this.ended = false;
        this.currentDayNumber = 1;
        this.currentDayMillis = timer.getTime();
    }

    /*
     * public CycleStatus getCurrentCycleStatus() {
     *     return this.currentCycleStatus;
     * }
     */

    /**
     * Returns the current day number.
     *
     * @return int
     */
    public int getCurrentDayNumber() {
        return this.currentDayNumber;
    }

    /**
     * Returns the current number of milliseconds since the current day night cycle started.
     *
     * @return long
     */
    public long getCurrentDayMillis() {
        return this.currentDayMillis;
    }

    /**
     * Returns whether the current day night cycle has ended.
     *
     * @return boolean
     */
    public boolean hasEnded() {
        return this.ended;
    }

    /**
     * Starts the day night cycle for the game.
     */
    public void start() {

    }

    /**
     * Pauses the timer for the day night cycle.
     */
    public void pause() {

    }

    /**
     * Main loop for the service that updates the game status.
     */
    public void run() {

    }
}
