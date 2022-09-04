package com.deco2800.game.services;

import com.deco2800.game.concurrency.JobSystem;
import com.deco2800.game.files.FileLoader;
import com.deco2800.game.services.configs.DayNightCycleConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.CompletableFuture;

/**
 * Service for managing the Day and Night cycle of the game.
 */
public class DayNightCycleService {
    private static final Logger logger = LoggerFactory.getLogger(DayNightCycleService.class);
    private volatile boolean ended;
    private DayNightCycleStatus currentCycleStatus;
    private int currentDayNumber;
    private long currentDayMillis;

    private boolean isPaused;

    private boolean isStarted;

    private DayNightCycleConfig config;
    private GameTime timer;

    public DayNightCycleService(GameTime timer) {
        this.ended = false;
        this.currentCycleStatus = DayNightCycleStatus.NONE;
        this.isStarted = false;
        this.isPaused = false;
        this.currentDayNumber = 0;
        this.currentDayMillis = timer.getTime();
        this.config = FileLoader.readClass(DayNightCycleConfig.class, "configs/DayNight.json");
        this.timer =  timer;
    }

    /**
     * Returns the current status of the day night cycle.
     *
     * @return DayNightCycleStatus
     */
    public DayNightCycleStatus getCurrentCycleStatus() {
        return this.currentCycleStatus;
    }


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

        this.isStarted = true;
        this.currentCycleStatus = DayNightCycleStatus.DAWN;

        CompletableFuture<Void> job = JobSystem.launch(() -> {
            try {
                this.run();
            } catch (InterruptedException e) {
                logger.error(e.getMessage());
            }

            return null;
        });
    }

    /**
     * Stops the day night cycle timer for the game.
     */
    public void stop() {
        this.ended = true;
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
    public void run() throws InterruptedException {

        while (!this.ended) {

            if (!this.isPaused) {
                //TODO: progress cycle to next

                // Definitely a better way to do this but this works for now
                this.currentDayMillis = this.timer.getTime() - (this.currentDayNumber * (config.nightLength +
                        config.duskLength + config.dayLength + config.dawnLength));

                if (this.currentDayMillis >= config.dawnLength && this.currentCycleStatus == DayNightCycleStatus.DAWN) {
                    this.currentCycleStatus = DayNightCycleStatus.DAY;
                } else if (this.currentDayMillis >= config.dayLength + config.dawnLength &&
                        this.currentCycleStatus == DayNightCycleStatus.DAY) {
                    this.currentCycleStatus = DayNightCycleStatus.DUSK;
                } else if (this.currentDayMillis >= config.duskLength + config.dayLength + config.dawnLength
                        && this.currentCycleStatus == DayNightCycleStatus.DUSK) {
                    this.currentCycleStatus = DayNightCycleStatus.NIGHT;
                    // Notify entities it is now NIGHT
                } else if (this.currentDayMillis >= config.nightLength + config.duskLength + config.dayLength +
                        config.dawnLength && this.currentCycleStatus == DayNightCycleStatus.NIGHT) {
                    // Check if number of days == max number of days
                    if (this.currentDayNumber == config.maxDays - 1) {
                        // End the game
                        this.stop();
                        return;
                    }

                    this.currentCycleStatus = DayNightCycleStatus.DAWN;
                    // Notify entities that it is now DAY
                    this.currentDayNumber++;

                    this.currentDayMillis = 0;
                }
            }

            /**
             * TODO: Sleep the amount of current cycle before heading to next cycle
             * Change current cycle.
             *
             *
             *
             */
            Thread.sleep(1000);
        }
    }


}
