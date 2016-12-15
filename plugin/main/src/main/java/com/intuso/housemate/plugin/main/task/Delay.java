package com.intuso.housemate.plugin.main.task;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import com.intuso.housemate.plugin.api.internal.annotations.Id;
import com.intuso.housemate.plugin.api.internal.annotations.Property;
import com.intuso.housemate.plugin.api.internal.driver.TaskDriver;
import com.intuso.housemate.plugin.api.internal.type.TimeUnit;
import org.slf4j.Logger;

/**
 * Task that waits for a specified amount of time
 *
 */
@Id(value = "delay", name = "Delay", description = "Delays for a set amount of time")
public class Delay implements TaskDriver {

    /**
     * The time unit that the quantity is specified in
     */
    @Property("time-unit")
    @Id(value = "unit", name = "Unit", description = "the unit of time to wait for")
    private TimeUnit unit = TimeUnit.MINUTES;

    /**
     * The quantity of time to wait which, combined with the unit, gives the time to wait
     */
    @Property("integer")
    @Id(value = "amount", name = "Amount", description = "the amount of time to wait")
    private Integer amount = 1;

    private final Logger logger;

    /**
     * Create a new delay task
     * @param logger
     */
    @Inject
    public Delay(@Assisted Logger logger,
                 @Assisted TaskDriver.Callback callback) {
        this.logger = logger;
    }

    @Override
    public void start() {

    }

    @Override
    public void stop() {

    }

    @Override
    public final void execute() {
        long delay = amount * unit.getFactor();
        logger.debug("Executing delay of " + amount + " " + unit + " which is " + delay + " milliseconds");

        // work out when we should stop
        long end_time = System.currentTimeMillis() + delay;

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