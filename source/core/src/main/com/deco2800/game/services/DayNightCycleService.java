package com.deco2800.game.services;

import com.badlogic.gdx.Gdx;
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

    public static final String EVENT_INTERMITTENT_PART_OF_DAY_CLOCK = "moveClock";

    private transient static final Logger logger = LoggerFactory.getLogger(DayNightCycleService.class);
    private volatile boolean ended;

    public DayNightCycleStatus currentCycleStatus;

    public DayNightCycleStatus lastCycleStatus;

    public int currentDayNumber;
    public long currentDayMillis;

    public long timePaused;

    public long totalDurationPaused;

    public boolean isPaused;

    public boolean isStarted;

    public DayNightCycleConfig config;
    public GameTime timer;

    public long timeSinceLastPartOfDay;

    public long timePerHalveOfPartOfDay;

    public int partOfDayHalveIteration;

    public int lastPartOfDayHalveIteration;

    private transient EventHandler events;

    /**
     * Empty method here for save game functionality DO NOT USE
     */
    public DayNightCycleService() {}

    public DayNightCycleService(GameTime timer, DayNightCycleConfig config) {
        this.events = new EventHandler(); //

        this.ended = false;
        this.isStarted = false;
        this.isPaused = false;

        this.currentCycleStatus = DayNightCycleStatus.NONE;

        this.totalDurationPaused = 0;
        this.currentDayNumber = 0;
        this.currentDayMillis = timer.getTime();

        this.config = config;
        this.timer = timer;
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
     * Returns whether the day night cycle service has been started
     *
     * @return boolean
     */
    public boolean hasStarted() {
        return this.isStarted;
    }

    /**
     * Returns whether the day night cycle is paused.
     *
     * @return boolean
     */
    public boolean isPaused() {
        return this.isPaused;
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
     * Used to get the last part of day before the current one
     * <p>
     * NOTE: helps with testing behaviour
     *
     * @return last part of day
     */
    public DayNightCycleStatus getLastCycleStatus() {
        return this.lastCycleStatus;
    }

    /**
     * Returns the last time game time that the timer was paused.
     *
     * @return long
     */
    public long getTimePaused() {
        return this.timePaused;
    }

    /**
     * Returns total amount of time that the timer has been paused for.
     *
     * @return long
     */
    public long getTotalDurationPaused() {
        return this.totalDurationPaused;
    }

    /**
     * Returns the current day number.
     *
     * @return int
     */
    public int getCurrentDayNumber() {
        // days begin at 0
        return this.currentDayNumber + 1;
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
     * Returns the game timer
     *
     * @return GameTime
     */
    public GameTime getTimer() {
        return this.timer;
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
            logger.info("Day/night cycle resumed");
            this.isPaused = false;
            return null; // Avoid running another async job
        }

        this.isStarted = true;
        this.setPartOfDayTo(DayNightCycleStatus.DAWN);

        return JobSystem.launch(() -> {
            try {
                this.run();
            } catch (InterruptedException e) {
                logger.error(e.getMessage());
                Thread.currentThread().interrupt();
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

    public void resume() {
        this.ended = false;
    }

    /**
     * Pauses the timer for the day night cycle.
     */
    public void pause() {
        this.isPaused = true;
        this.timePaused = this.currentDayMillis;
        logger.info("Day/night cycle paused");
    }

    /**
     * Main loop for the service that updates the game status.
     */
    public void run() throws InterruptedException {
        long durationPaused = 0;

        while (!this.ended) {

            if (!this.isPaused) {
                if (durationPaused != 0) {
                    this.totalDurationPaused += durationPaused;
                    durationPaused = 0;
                }

                // Move clock for parts of day with more than one half
                if (this.currentCycleStatus == DayNightCycleStatus.DAY ||
                        this.currentCycleStatus == DayNightCycleStatus.NIGHT) {
                    long elapsed = this.currentDayMillis - timeSinceLastPartOfDay;
                    
                    if ((elapsed >= timePerHalveOfPartOfDay * partOfDayHalveIteration) &&
                            partOfDayHalveIteration != lastPartOfDayHalveIteration) {
                        Gdx.app.postRunnable(() -> {
                            events.trigger(EVENT_INTERMITTENT_PART_OF_DAY_CLOCK, this.currentCycleStatus);
                        });

                        partOfDayHalveIteration++;
                    }
                }

                // Definitely a better way to do this but this works for now
                this.currentDayMillis = this.timer.getTime() - (this.currentDayNumber * (config.nightLength +
                        config.duskLength + config.dayLength + config.dawnLength)) - this.totalDurationPaused;

                if (this.currentDayMillis >= config.dawnLength && this.currentCycleStatus == DayNightCycleStatus.DAWN) {
                    this.setPartOfDayTo(DayNightCycleStatus.DAY);
                } else if (this.currentDayMillis >= config.dayLength + config.dawnLength &&
                        this.currentCycleStatus == DayNightCycleStatus.DAY) {
                    this.setPartOfDayTo(DayNightCycleStatus.DUSK);
                } else if (this.currentDayMillis >= config.duskLength + config.dayLength + config.dawnLength
                        && this.currentCycleStatus == DayNightCycleStatus.DUSK) {
                    this.setPartOfDayTo(DayNightCycleStatus.NIGHT);
                    // Notify entities it is now NIGHT
                } else if (this.currentDayMillis >= config.nightLength + config.duskLength + config.dayLength +
                        config.dawnLength && this.currentCycleStatus == DayNightCycleStatus.NIGHT) {
                    // Check if number of days == max number of days
                    if (this.currentDayNumber == config.maxDays - 1) {
                        // End the game
                        this.stop();

                        Gdx.app.postRunnable(() -> {
                            events.trigger(EVENT_DAY_PASSED, this.currentDayNumber + 1);
                        });
                        return;
                    }

                    this.setPartOfDayTo(DayNightCycleStatus.DAWN);
                    // Notify entities that it is now DAY
                    this.currentDayNumber++;
                    Gdx.app.postRunnable(() -> {
                        events.trigger(EVENT_DAY_PASSED, this.currentDayNumber);
                    });


                    this.currentDayMillis = 0;
                }

            } else {
                // Keep track of how long the game has been paused this time.
                durationPaused = this.timer.getTimeSince(this.timePaused);
            }

            Thread.sleep(100);
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
        Gdx.app.postRunnable(() -> {
            this.events.trigger(EVENT_PART_OF_DAY_PASSED, nextPartOfDay);
        });
        this.timeSinceLastPartOfDay = this.timer.getTime();
        if (nextPartOfDay == DayNightCycleStatus.NIGHT) {
            this.timePerHalveOfPartOfDay = config.nightLength / 2;
            lastPartOfDayHalveIteration = 2;
            this.partOfDayHalveIteration = 1;
        }
        if (nextPartOfDay == DayNightCycleStatus.DAY) {
            this.timePerHalveOfPartOfDay = config.dayLength / 4;
            lastPartOfDayHalveIteration = 4;
            this.partOfDayHalveIteration = 1;
        }
    }

    /**
     * To enable events to be subscribed to
     *
     * @return the event handler for the service
     */
    public EventHandler getEvents() {
        return events;
    }
}
