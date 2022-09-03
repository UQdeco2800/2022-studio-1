package com.deco2800.game.services;

import com.deco2800.game.concurrency.JobSystem;
import com.deco2800.game.files.FileLoader;
import com.deco2800.game.services.configs.DayNightCycleConfig;

import java.util.concurrent.CompletableFuture;

/**
 * Service for managing the Day and Night cycle of the game.
 */
public class DayNightCycleService {
    private boolean ended;
    //private CycleStatus currentCycleStatus;
    private int currentDayNumber;
    private long currentDayMillis;

    private boolean isPaused;

    private boolean isStarted;

    private DayNightCycleConfig config;

    public DayNightCycleService(GameTime timer) {
        this.ended = false;
        this.isStarted = false;
        this.isPaused = false;
        this.currentDayNumber = 1;
        this.currentDayMillis = timer.getTime();
        this.config = FileLoader.readClass(DayNightCycleConfig.class, "configs/DayNight.json");
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

        if (this.isStarted && !this.isPaused) {
            throw new IllegalStateException("The timer has already been started");
        }

        if (this.isPaused) {
            // Resuming a timer
            this.isPaused = false;
            return; // Avoid running another async job
        }

        CompletableFuture<Void> job = JobSystem.launch(() -> {
            this.isStarted = true;
            this.run();
            return null;
        });
    }

    /**
     * Pauses the timer for the day night cycle.
     */
    public void pause() {
        this.isPaused = true;
    }

    /**
     * Main loop for the service that updates the game status.
     */
    public void run() {

        while (true) {

            if (!isPaused) {
                //TODO: progress cycle to next
            }

            /**
             * TODO: Sleep the amount of current cycle before heading to next cycle
             * Change current cycle.
             *
             *
             *
             */
        }
    }


}
