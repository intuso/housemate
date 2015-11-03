package com.intuso.housemate.server.plugin.main.task;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import com.intuso.housemate.client.real.api.internal.annotations.Property;
import com.intuso.housemate.client.real.api.internal.driver.TaskDriver;
import com.intuso.housemate.client.real.api.internal.type.TimeUnit;
import com.intuso.housemate.plugin.api.internal.TypeInfo;
import com.intuso.utilities.log.Log;

/**
 * Task that waits for a specified amount of time
 *
 */

@TypeInfo(id = "random-delay", name = "Random Delay", description = "Wait for a random amount of time")
public class RandomDelay implements TaskDriver {

    /**
     * The time unit that the quantity is specified in
     */
    @Property(id = "unit", name = "Unit", description = "the unit of time to wait for", typeId = "time-unit")
    private TimeUnit unit = TimeUnit.MINUTES;

    /**
     * The quantity of time to wait which, combined with the unit, gives the time to wait
     */
    @Property(id = "max-amount", name = "Max amount", description = "the maximum amount of time to wait", typeId = "integer")
    private Integer maxAmount = 1;

    private final Log log;

    /**
     * Create a new delay task
     * @param log
     */
    @Inject
    public RandomDelay(Log log,
                 @Assisted TaskDriver.Callback callback) {
        this.log = log;
    }

    @Override
    public final void execute() {
        long delay = maxAmount * unit.getFactor();
        long actual_delay = (long)(delay * Math.random());
        log.d("Executing random delay of up to " + maxAmount + " " + unit + " which is " + delay + " milliseconds");
        log.d("Delaying for " + actual_delay + " milliseconds");

        // work out when we should stop
        long end_time = System.currentTimeMillis() + actual_delay;

        // while we need to keep waiting
        while(System.currentTimeMillis() < end_time) {
            try {
                // wait a max of 10 minutes
                log.d("Waiting for " + Math.min(end_time - System.currentTimeMillis(), 600000) + " milliseconds");
                Thread.sleep(Math.min(end_time - System.currentTimeMillis(), 600000));
            } catch(InterruptedException e) {
                // if interrupted then return
                return;
            }
        }

        log.d("Executed delay");
    }
}