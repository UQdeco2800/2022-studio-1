package com.deco2800.game.services;

import com.deco2800.game.concurrency.JobSystem;
import com.deco2800.game.events.EventHandler;
import com.deco2800.game.services.configs.DayNightCycleConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.CompletableFuture;

/**
 * Service for managing the Day and Night cycle of the game.
 */
public class DayNightCycleService {

    public static final String EVENT_DAY_PASSED = "dayPassed";

    public static final String EVENT_PART_OF_DAY_PASSED = "partOfDayPassed";

    private static final Logger logger = LoggerFactory.getLogger(DayNightCycleService.class);
    private volatile boolean ended;

    private DayNightCycleStatus currentCycleStatus;

    private DayNightCycleStatus lastCycleStatus;
    private int currentDayNumber;
    private long currentDayMillis;

    private boolean isPaused;

    private boolean isStarted;

    private DayNightCycleConfig config;
    private GameTime timer;

    private EventHandler events;

    public DayNightCycleService(GameTime timer, DayNightCycleConfig config) {
        this.events = new EventHandler(); //
        this.ended = false;
        //this.currentCycleStatus = CycleStatus.DAWN;
        this.isStarted = false;
        this.isPaused = false;
        this.currentDayNumber = 0;
        this.currentDayMillis = timer.getTime();
        this.config = config;
        this.timer = timer;
    }


    public DayNightCycleStatus getCurrentCycleStatus() {
        return this.currentCycleStatus;
    }

    /**
     * Used to get the last part of day before the current one
     *
     * NOTE: helps with testing behaviour
     *
     * @return last part of day
     */
    public DayNightCycleStatus getLastCycleStatus() {
        return this.lastCycleStatus;
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
     *
     * @return a future that can be used to join.
     */
    public CompletableFuture<Object> start() {

        if (this.isStarted && !this.isPaused) {
            throw new IllegalStateException("The timer has already been started");
        }

        if (this.isPaused) {
            // Resuming a timer
            this.isPaused = false;
            return null; // Avoid running another async job
        }

        this.isStarted = true;

        return JobSystem.launch(() -> {
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
                    if (this.currentDayMillis >= config.dawnLength) {
                        this.setPartOfDayTo(DayNightCycleStatus.DAY);
                    } else if (this.currentDayMillis >= config.dayLength + config.dawnLength &&
                            this.currentCycleStatus == DayNightCycleStatus.DAY) {
                    } else if (this.currentDayMillis >= config.dayLength + config.dawnLength) {
                        this.setPartOfDayTo(DayNightCycleStatus.DUSK);
                    } else if (this.currentDayMillis >= config.duskLength + config.dayLength + config.dawnLength
                            && this.currentCycleStatus == DayNightCycleStatus.DUSK) {
                    } else if (this.currentDayMillis >= config.duskLength + config.dayLength + config.dawnLength) {
                         this.setPartOfDayTo(DayNightCycleStatus.NIGHT);
                        // Notify entities it is now NIGHT
                    } else if (this.currentDayMillis >= config.nightLength + config.duskLength + config.dayLength +
                            config.dawnLength && this.currentCycleStatus == DayNightCycleStatus.NIGHT) {
                    } else if (this.currentDayMillis >= config.nightLength + config.duskLength + config.dayLength +
                            config.dawnLength) {
                        // Check if number of days == max number of days
                        if (this.currentDayNumber == config.maxDays - 1) {
                            // End the game
                            this.stop();
                            break;
                        }

                        this.setPartOfDayTo(DayNightCycleStatus.DAWN);
                        this.currentDayNumber++;
                        // Notify entities that it is now DAY
                        events.trigger(EVENT_DAY_PASSED, this.currentDayNumber);

                        this.currentDayMillis = 0;
                    }
                }
            }
        }
    }

    /**
     * Helps make testing easy in mocking. It's used  change to the next
     * part of the day.
     *
     * @param nextPartOfDay next part of the day to change to as a DayNightCycleStatus
     */
    public void setPartOfDayTo(DayNightCycleStatus nextPartOfDay) {
        this.lastCycleStatus = currentCycleStatus;
        this.currentCycleStatus = nextPartOfDay;
        // helps with testing
        this.events.trigger(EVENT_PART_OF_DAY_PASSED, nextPartOfDay);
    }

    /**
     * To enable events to be subscribed to
     *
     * @return the event handler for the service
     */
    public EventHandler getEvents() {
        return events;
    }

    public  static void main(String... args) {
        var config = new DayNightCycleConfig();
        config.dawnLength = 500;
        config.dayLength = 2000;
        config.duskLength = 500;
        config.nightLength = 3000;
        config.maxDays = 1;
        ServiceLocator.registerTimeSource(new GameTime());
        var gameTime = ServiceLocator.getTimeSource();
        var dayNightCycleService = new DayNightCycleService(gameTime, config);
        dayNightCycleService.start().join();
    }


}
