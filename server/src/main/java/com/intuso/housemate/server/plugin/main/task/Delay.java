package com.intuso.housemate.server.plugin.main.task;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import com.intuso.housemate.client.real.api.internal.annotations.Property;
import com.intuso.housemate.client.real.api.internal.driver.TaskDriver;
import com.intuso.housemate.client.real.api.internal.impl.type.TimeUnit;
import com.intuso.housemate.plugin.api.internal.TypeInfo;
import com.intuso.utilities.log.Log;

/**
 * Task that waits for a specified amount of time
 *
 */
@TypeInfo(id = "delay", name = "Delay", description = "Delays for a set amount of time")
public class Delay implements TaskDriver {

    /**
	 * The time unit that the quantity is specified in
	 */
	@Property(id = "unit", name = "Unit", description = "the unit of time to wait for", typeId = "time-unit")
	private TimeUnit unit = TimeUnit.MINUTES;
	
	/**
	 * The quantity of time to wait which, combined with the unit, gives the time to wait
	 */
	@Property(id = "amount", name = "Amount", description = "the amount of time to wait", typeId = "integer")
	private Integer amount = 1;

	private final Log log;

	/**
	 * Create a new delay task
	 * @param log
	 */
    @Inject
	public Delay(Log log,
				 @Assisted TaskDriver.Callback callback) {
		this.log = log;
	}
	
	@Override
	public final void execute() {
        long delay = amount * unit.getFactor();
        log.d("Executing delay of " + amount + " " + unit + " which is " + delay + " milliseconds");
		
		// work out when we should stop
		long end_time = System.currentTimeMillis() + delay;
		
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