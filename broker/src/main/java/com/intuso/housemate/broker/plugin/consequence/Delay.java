package com.intuso.housemate.broker.plugin.consequence;

import com.intuso.housemate.object.broker.real.BrokerRealProperty;
import com.intuso.housemate.object.broker.real.BrokerRealResources;
import com.intuso.housemate.api.HousemateException;
import com.intuso.housemate.object.broker.real.consequence.BrokerRealConsequence;
import com.intuso.housemate.object.real.impl.type.IntegerType;
import com.intuso.housemate.object.real.impl.type.TimeUnit;
import com.intuso.housemate.object.real.impl.type.TimeUnitType;

/**
 * Consequence that waits for a specified amount of time
 * @author tclabon
 *
 */
public class Delay extends BrokerRealConsequence {

    /**
	 * The time unit that the quantity is specified in
	 */
	private final BrokerRealProperty<TimeUnit> unit;
	
	/**
	 * The quantity of time to wait which, combined with the unit, gives the time to wait
	 */
	private final BrokerRealProperty<Integer> amount;

	/**
	 * Create a new delay consequence
     * @param resources
	 * @param name
	 * @throws HousemateException
	 */
	public Delay(BrokerRealResources resources, String id, String name, String description) {
		super(resources, id, name, description);
        unit = new BrokerRealProperty<TimeUnit>(resources, "unit", "Unit", "the unit of time to wait for", new TimeUnitType(resources.getRealResources()), TimeUnit.MINUTES);
        amount = new BrokerRealProperty<Integer>(resources, "amount", "Amount", "the amount of time to wait", new IntegerType(resources.getRealResources()), 1);
        getProperties().add(unit);
        getProperties().add(amount);
	}
	
	@Override
	public final void _execute() {
        long delay = amount.getTypedValue() * unit.getTypedValue().getFactor();
        getLog().d("Executing delay of " + amount + " " + unit + " which is " + delay + " milliseconds");
		
		// work out when we should stop
		long end_time = System.currentTimeMillis() + delay;
		
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