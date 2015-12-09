package com.intuso.housemate.server.plugin.main.condition;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import com.intuso.housemate.client.real.api.internal.annotations.Property;
import com.intuso.housemate.client.real.api.internal.annotations.TypeInfo;
import com.intuso.housemate.client.real.api.internal.driver.ConditionDriver;
import com.intuso.housemate.client.real.api.internal.type.Time;
import org.slf4j.Logger;

import java.util.Calendar;

/**
 * Condition that is true during a set period of time
 *
 */
@TypeInfo(id = "time-of-the-day", name = "Time of the Day", description = "Condition that is true for certain parts of the day")
public class TimeOfTheDay implements ConditionDriver {

    /**
     * Start of the day = midnight = 00:00:00.000
     */
    public final static Time DAY_START = new Time(0, 0, 0);

    /**
     * End of the day = midnight minus 1 millisecond = 23:59:59.999
     */
    public final static Time DAY_END = new Time(23, 59, 59);

    /**
     * Maximum time to wait for = 10 minutes
     */
    private final static long WAIT_MAX = 10 * 60 * 1000;

    /**
     * String version of the time that, once passed, makes the condition unsatisfied. Default is the start of the day
     */
    @Property("time")
    @TypeInfo(id = "before", name = "Before", description = "The condition is satisfied when the current time is before this time")
    private Time before;

    /**
     * String version of the time that, until reached, makes the condition unsatisfied
     */
    @Property("time")
    @TypeInfo(id = "after", name = "After", description = "The condition is satisfied when the current time is after this time")
    private Time after;

    private final Logger logger;
    private final ConditionDriver.Callback callback;

    /**
     * thread that runs to tell listener when condition is (un)satisfied
     */
    private Thread monitor;

    @Inject
    public TimeOfTheDay(Logger logger,
                        @Assisted ConditionDriver.Callback callback) {
        this.logger = logger;
        this.callback = callback;
    }

    @Override
    public boolean hasChildConditions() {
        return false;
    }

    /**
     * Check if the current time of day satisfies the condition
     * @return true iff current time is after after and before before
     */
    protected final boolean doesNowSatisfy() {
        Time cur_time = getTime();

        // if the after time is before the before time then return true if current time is between them
        if(after.compareTo(before) <= 0)
            return (cur_time.compareTo(after) >= 0 && cur_time.compareTo(before) <= 0);

        // else "satisfied" period spans midnight so has to be after after or before before
        return (cur_time.compareTo(after) >= 0 || cur_time.compareTo(before) <= 0);
    }

    /**
     * get the current time since midnight as a long
     * @return the number of milliseconds from midnight until now
     */
    private static Time getTime() {
        Calendar c = Calendar.getInstance();
        return new Time(c.get(Calendar.HOUR_OF_DAY), c.get(Calendar.MINUTE), c.get(Calendar.SECOND));
    }

    /**
     * Wait for the next occurrence of a time of day
     * @param time the time to wait until
     * @param logger logger to use
     */
    protected static void waitUntilNext(Time time, Logger logger) throws InterruptedException {

        logger.debug("Waiting until current time of day in milliseconds since midnight is " + time);

        // get the current time
        Time cur_time = getTime();

        // if we're already past, then wait until we go past midnight
        if(cur_time.compareTo(time) > 0) {

            logger.debug("Time to wait until is after midnight, waiting until past midnight");

            // save the old time, then wait a bit
            Time old_time = cur_time;
            logger.debug("Now = " + cur_time + ", end = " + time + ", waiting for " + Math.min(WAIT_MAX, DAY_END.minus(cur_time)));
            Thread.sleep(Math.min(WAIT_MAX, DAY_END.minus(cur_time)));

            // past midnight is when the current time is less than the last time we saw
            while(!Thread.currentThread().isInterrupted() && (cur_time = getTime()).compareTo(old_time) >= 0) {
                // wait for a max of 10 minutes
                logger.debug("Now = " + cur_time + ", end = " + time + ", waiting for " + Math.min(WAIT_MAX, DAY_END.minus(cur_time)));
                Thread.sleep(Math.min(WAIT_MAX, DAY_END.minus(cur_time)));
            }

            logger.debug("Past midnight");
        }

        // loop until the current time is past the given time
        while(!Thread.currentThread().isInterrupted() && (cur_time = getTime()).compareTo(time) < 0) {
            // wait for max 10 mins
            long toWait = Math.min(WAIT_MAX, time.minus(cur_time));
            logger.debug("Now = " + cur_time + ", end = " + time + ", waiting for " + toWait);
            Thread.sleep(Math.min(WAIT_MAX, toWait));
        }

        logger.debug("Reached time to wait until");
    }

    @Override
    public void start() {
        // start monitoring the time of the day
        monitor = new TimeMonitorThread();
        monitor.start();
        callback.conditionSatisfied(doesNowSatisfy());
    }

    @Override
    public void stop() {
        if(monitor != null) {
            monitor.interrupt();
            monitor = null;
        }
    }

    /**
     * Thread that monitors the time of the day and calls conditionSatisfied as appropriate based
     * on the before and after times parsed by the super class
     * @author Tom Clabon
     *
     */
    private class TimeMonitorThread extends Thread {
        @Override
        public void run() {

            try {
                // we enter the loop expecting it to be unsatisfied,
                // so check if it's already satisfied and if it is then wait until it's not
                if(doesNowSatisfy()) {

                    // wait until unsatisfied so that loop works properly
                    logger.debug("Waiting for condition to become unsatisfied");
                    waitUntilNext(before, logger);
                    callback.conditionSatisfied(false);
                }

                // loop until thread is stopped
                logger.debug("Entering time monitor loop");
                while(!isInterrupted()) {

                    // wait until we next go past the after time
                    logger.debug("Waiting until time of the day goes past the after time");
                    waitUntilNext(after, logger);
                    logger.debug("Time of the day is after the after time");

                    // condition is now satisfied
                    callback.conditionSatisfied(true);

                    // wait until we next go past the before time
                    logger.debug("Waiting until time of the day goes past the before time");
                    waitUntilNext(before, logger);
                    logger.debug("Time of the day is after the before time");

                    // condition is now unsatisfied
                    callback.conditionSatisfied(false);
                }
            } catch(InterruptedException e) {
                logger.warn("TimeMonitor thread interrupted");
            }
        }
    }
}