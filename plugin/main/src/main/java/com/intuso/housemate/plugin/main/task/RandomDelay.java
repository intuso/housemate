package com.intuso.housemate.plugin.main.task;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import com.intuso.housemate.client.api.internal.annotation.Id;
import com.intuso.housemate.client.api.internal.annotation.Property;
import com.intuso.housemate.client.api.internal.driver.TaskDriver;
import com.intuso.housemate.client.api.internal.type.TimeUnit;
import org.slf4j.Logger;

/**
 * Task that waits for a specified amount of time
 *
 */

@Id(value = "random-delay", name = "Random Delay", description = "Wait for a random amount of time")
public class RandomDelay implements TaskDriver {

    /**
     * The time unit that the quantity is specified in
     */
    @Property
    @Id(value = "unit", name = "Unit", description = "the unit of time to wait for")
    private TimeUnit unit = TimeUnit.MINUTES;

    /**
     * The quantity of time to wait which, combined with the unit, gives the time to wait
     */
    @Property
    @Id(value = "max-amount", name = "Max amount", description = "the maximum amount of time to wait")
    private Integer maxAmount = 1;

    private final Logger logger;

    /**
     * Create a new delay task
     * @param logger
     */
    @Inject
    public RandomDelay(@Assisted Logger logger,
                 @Assisted TaskDriver.Callback callback) {
        this.logger = logger;
    }

    @Override
    public void startTask() {

    }

    @Override
    public void stopTask() {

    }

    @Override
    public final void execute() {
        long delay = maxAmount * unit.getFactor();
        long actual_delay = (long)(delay * Math.random());
        logger.debug("Executing random delay of up to " + maxAmount + " " + unit + " which is " + delay + " milliseconds");
        logger.debug("Delaying for " + actual_delay + " milliseconds");

        // work out when we should stop
        long end_time = System.currentTimeMillis() + actual_delay;

        // while we need to keep waiting
        while(System.currentTimeMillis() < end_time) {
            try {
                // wait a max of 10 minutes
                logger.debug("Waiting for " + Math.min(end_time - System.currentTimeMillis(), 600000) + " milliseconds");
                Thread.sleep(Math.min(end_time - System.currentTimeMillis(), 600000));
            } catch(InterruptedException e) {
                // if interrupted then return
                return;
            }
        }

        logger.debug("Executed delay");
    }
}