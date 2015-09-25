package com.intuso.housemate.server.plugin.main.task;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import com.intuso.housemate.client.real.api.internal.RealProperty;
import com.intuso.housemate.client.real.api.internal.RealTask;
import com.intuso.housemate.client.real.api.internal.factory.task.RealTaskOwner;
import com.intuso.housemate.client.real.api.internal.impl.type.IntegerType;
import com.intuso.housemate.client.real.api.internal.impl.type.TimeUnit;
import com.intuso.housemate.client.real.api.internal.impl.type.TimeUnitType;
import com.intuso.housemate.comms.api.internal.payload.TaskData;
import com.intuso.housemate.plugin.api.internal.TypeInfo;
import com.intuso.utilities.listener.ListenersFactory;
import com.intuso.utilities.log.Log;

/**
 * Task that waits for a specified amount of time
 *
 */

@TypeInfo(id = "random-delay", name = "Random Delay", description = "Wait for a random amount of time")
public class RandomDelay extends RealTask {

    /**
     * The time unit that the quantity is specified in
     */
    private final RealProperty<TimeUnit> unit;

    /**
     * The quantity of time to wait which, combined with the unit, gives the time to wait
     */
    private final RealProperty<Integer> maxAmount;

    /**
     * Create a new delay task
     * @param log
     */
    @Inject
    public RandomDelay(Log log,
                       ListenersFactory listenersFactory,
                       @Assisted TaskData data,
                       @Assisted RealTaskOwner owner,
                       TimeUnitType timeUnitType, IntegerType integerType) {
        super(log, listenersFactory, "random-delay", data, owner);
        unit = new RealProperty<>(log, listenersFactory, "unit", "Unit", "the unit of time to wait for",
                timeUnitType, TimeUnit.MINUTES);
        maxAmount = new RealProperty<>(log, listenersFactory, "amount", "Amount", "the amount of time to wait",
                integerType, 1);
        getProperties().add(unit);
        getProperties().add(maxAmount);
    }

    @Override
    public final void execute() {
        long delay = maxAmount.getTypedValue() * unit.getTypedValue().getFactor();
        long actual_delay = (long)(delay * Math.random());
        getLog().d("Executing random delay of up to " + maxAmount + " " + unit + " which is " + delay + " milliseconds");
        getLog().d("Delaying for " + actual_delay + " milliseconds");

        // work out when we should stop
        long end_time = System.currentTimeMillis() + actual_delay;

        // while we need to keep waiting
        while(System.currentTimeMillis() < end_time) {
            try {
                // wait a max of 10 minutes
                getLog().d("Waiting for " + Math.min(end_time - System.currentTimeMillis(), 600000) + " milliseconds");
                Thread.sleep(Math.min(end_time - System.currentTimeMillis(), 600000));
            } catch(InterruptedException e) {
                // if interrupted then return
                return;
            }
        }

        getLog().d("Executed delay");
    }
}