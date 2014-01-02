package com.intuso.housemate.server.plugin.main.task;

import com.google.inject.Inject;
import com.intuso.housemate.api.HousemateException;
import com.intuso.housemate.object.server.real.ServerRealProperty;
import com.intuso.housemate.object.server.real.ServerRealResources;
import com.intuso.housemate.object.server.real.ServerRealTask;
import com.intuso.housemate.object.server.real.ServerRealTaskOwner;
import com.intuso.housemate.object.real.impl.type.IntegerType;
import com.intuso.housemate.object.real.impl.type.TimeUnit;
import com.intuso.housemate.object.real.impl.type.TimeUnitType;

/**
 * Task that waits for a specified amount of time
 *
 */
public class RandomDelay extends ServerRealTask {

    /**
     * The time unit that the quantity is specified in
     */
    private final ServerRealProperty<TimeUnit> unit;

    /**
     * The quantity of time to wait which, combined with the unit, gives the time to wait
     */
    private final ServerRealProperty<Integer> maxAmount;

    /**
     * Create a new delay task
     * @param resources
     * @param name
     * @throws HousemateException
     */
    @Inject
    public RandomDelay(ServerRealResources resources, String id, String name, String description,
                       ServerRealTaskOwner owner, TimeUnitType timeUnitType, IntegerType integerType) {
        super(resources, id, name, description, owner);
        unit = new ServerRealProperty<TimeUnit>(resources, "unit", "Unit", "the unit of time to wait for",
                timeUnitType, TimeUnit.MINUTES);
        maxAmount = new ServerRealProperty<Integer>(resources, "amount", "Amount", "the amount of time to wait",
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