package com.intuso.housemate.broker.plugin.main.task;

import com.google.inject.Inject;
import com.intuso.housemate.api.HousemateException;
import com.intuso.housemate.object.broker.real.BrokerRealProperty;
import com.intuso.housemate.object.broker.real.BrokerRealResources;
import com.intuso.housemate.object.broker.real.BrokerRealTask;
import com.intuso.housemate.object.broker.real.BrokerRealTaskOwner;
import com.intuso.housemate.object.real.impl.type.IntegerType;
import com.intuso.housemate.object.real.impl.type.TimeUnit;
import com.intuso.housemate.object.real.impl.type.TimeUnitType;

/**
 * Task that waits for a specified amount of time
 *
 */
public class RandomDelay extends BrokerRealTask {

    /**
     * The time unit that the quantity is specified in
     */
    private final BrokerRealProperty<TimeUnit> unit;

    /**
     * The quantity of time to wait which, combined with the unit, gives the time to wait
     */
    private final BrokerRealProperty<Integer> maxAmount;

    /**
     * Create a new delay task
     * @param resources
     * @param name
     * @throws HousemateException
     */
    @Inject
    public RandomDelay(BrokerRealResources resources, String id, String name, String description,
                       BrokerRealTaskOwner owner, TimeUnitType timeUnitType, IntegerType integerType) {
        super(resources, id, name, description, owner);
        unit = new BrokerRealProperty<TimeUnit>(resources, "unit", "Unit", "the unit of time to wait for",
                timeUnitType, TimeUnit.MINUTES);
        maxAmount = new BrokerRealProperty<Integer>(resources, "amount", "Amount", "the amount of time to wait",
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