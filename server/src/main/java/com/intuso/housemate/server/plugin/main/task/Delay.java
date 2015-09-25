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
@TypeInfo(id = "delay", name = "Delay", description = "Delays for a set amount of time")
public class Delay extends RealTask {

    /**
	 * The time unit that the quantity is specified in
	 */
	private final RealProperty<TimeUnit> unit;
	
	/**
	 * The quantity of time to wait which, combined with the unit, gives the time to wait
	 */
	private final RealProperty<Integer> amount;

	/**
	 * Create a new delay task
     * @param log
	 */
    @Inject
	public Delay(Log log,
                 ListenersFactory listenersFactory,
                 @Assisted TaskData data,
                 @Assisted RealTaskOwner owner,
                 TimeUnitType timeUnitType, IntegerType integerType) {
		super(log, listenersFactory, "delay", data, owner);
        unit = new RealProperty<>(log, listenersFactory, "unit", "Unit", "the unit of time to wait for", timeUnitType, TimeUnit.MINUTES);
        amount = new RealProperty<>(log, listenersFactory, "amount", "Amount", "the amount of time to wait", integerType, 1);
        getProperties().add(unit);
        getProperties().add(amount);
	}
	
	@Override
	public final void execute() {
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