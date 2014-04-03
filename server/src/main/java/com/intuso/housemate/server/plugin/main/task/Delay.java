package com.intuso.housemate.server.plugin.main.task;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import com.intuso.housemate.api.HousemateException;
import com.intuso.housemate.api.object.task.TaskData;
import com.intuso.housemate.object.real.impl.type.IntegerType;
import com.intuso.housemate.object.real.impl.type.TimeUnit;
import com.intuso.housemate.object.real.impl.type.TimeUnitType;
import com.intuso.housemate.object.server.real.ServerRealProperty;
import com.intuso.housemate.object.server.real.ServerRealTask;
import com.intuso.housemate.object.server.real.ServerRealTaskOwner;
import com.intuso.housemate.plugin.api.TypeInfo;
import com.intuso.utilities.listener.ListenersFactory;
import com.intuso.utilities.log.Log;

/**
 * Task that waits for a specified amount of time
 *
 */
@TypeInfo(id = "delay", name = "Delay", description = "Delays for a set amount of time")
public class Delay extends ServerRealTask {

    /**
	 * The time unit that the quantity is specified in
	 */
	private final ServerRealProperty<TimeUnit> unit;
	
	/**
	 * The quantity of time to wait which, combined with the unit, gives the time to wait
	 */
	private final ServerRealProperty<Integer> amount;

	/**
	 * Create a new delay task
     * @param log
	 * @throws HousemateException
	 */
    @Inject
	public Delay(Log log,
                 ListenersFactory listenersFactory,
                 @Assisted TaskData data,
                 @Assisted ServerRealTaskOwner owner,
                 TimeUnitType timeUnitType, IntegerType integerType) {
		super(log, listenersFactory, data, owner);
        unit = new ServerRealProperty<TimeUnit>(log, listenersFactory, "unit", "Unit", "the unit of time to wait for", timeUnitType, TimeUnit.MINUTES);
        amount = new ServerRealProperty<Integer>(log, listenersFactory, "amount", "Amount", "the amount of time to wait", integerType, 1);
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