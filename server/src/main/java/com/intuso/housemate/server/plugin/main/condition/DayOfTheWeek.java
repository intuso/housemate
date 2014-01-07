package com.intuso.housemate.server.plugin.main.condition;

import com.google.common.collect.Lists;
import com.google.inject.Inject;
import com.intuso.housemate.api.HousemateException;
import com.intuso.housemate.object.server.LifecycleHandler;
import com.intuso.housemate.object.server.real.ServerRealCondition;
import com.intuso.housemate.object.server.real.ServerRealConditionOwner;
import com.intuso.housemate.object.server.real.ServerRealProperty;
import com.intuso.housemate.object.real.impl.type.Day;
import com.intuso.housemate.object.real.impl.type.DaysType;
import com.intuso.utilities.log.Log;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

/**
 * Condition which is true iff the current day of the week matches
 * those specified by the user
 *
 */
public class DayOfTheWeek extends ServerRealCondition {

    private final Map<Day, Integer> DAY_MAP = new HashMap<Day, Integer>() {
        {
            put(Day.Monday, Calendar.MONDAY);
            put(Day.Tuesday, Calendar.TUESDAY);
            put(Day.Wednesday, Calendar.WEDNESDAY);
            put(Day.Thursday, Calendar.THURSDAY);
            put(Day.Friday, Calendar.FRIDAY);
            put(Day.Saturday, Calendar.SATURDAY);
            put(Day.Sunday, Calendar.SUNDAY);
        }
    };

    public static final String DAYS_FIELD = "days";
    
	/**
	 * The days that the condition is satisfied for. Left-most bit isn't used, next one is sunday,
	 * then monday etc. Right-most bit is saturday
	 */
	private final ServerRealProperty<Day> days;
	
	/**
	 * thread to monitor the day of the week
	 */
	private Thread monitor;
	
	/**
	 * Create a new day of the week condition
     * @param name
	 * @throws HousemateException 
	 */
    @Inject
	public DayOfTheWeek(Log log, String id, String name, String description,
                        ServerRealConditionOwner owner, LifecycleHandler lifecycleHandler, DaysType daysType) throws HousemateException {
		super(log, id, name, description, owner, lifecycleHandler);
        days = new ServerRealProperty<Day>(log, DAYS_FIELD, DAYS_FIELD, "The days that satisfy the condition",
                daysType, Lists.<Day>newArrayList());
        getProperties().add(days);
    }

	/**
	 * Check if the current day satisfies the condition
	 * @return true if the current day satisfies the condition
	 */
	private final boolean doesTodaySatisfy() {
        int currentDay = Calendar.getInstance().get(Calendar.DAY_OF_WEEK);
		boolean result = false;
        for(Day day : days.getTypedValues())
            result |= DAY_MAP.get(day) == currentDay;
		getLog().d("Current day of the week is " + Calendar.getInstance().get(Calendar.DAY_OF_WEEK) + " (1 is Sunday). Condition is " + (result ? "" : "un") + "satisfied");
		return result;
	}
	
	@Override
	public void start() {
		// start monitoring the day of the week
        getLog().d("Condition satisfied when day is " + days.getTypedValue());
		monitor = new DayMonitorThread();
		monitor.start();
		conditionSatisfied(doesTodaySatisfy());
	}
	
	@Override
	public void stop() {
        if(monitor != null) {
		    monitor.interrupt();
		    monitor = null;
        }
	}
	
	/**
	 * Thread that monitors the time of the day and calls conditionSatisfied() as appropriate based on
	 * the days that the super class parsed
	 * @author Tom Clabon
	 *
	 */
	private class DayMonitorThread extends Thread {
		@Override
		public void run() {

            try {
                // while we're not shutting down
                while(!isInterrupted()) {

                    // wait until the next day starts
                    getLog().d("Waiting until midnight");
                    TimeOfTheDay.waitUntilNext(TimeOfTheDay.DAY_START, getLog());

                    getLog().d("Past midnight, checking if current day is in set");

                    // check if this condition is now satisfied or not
                    conditionSatisfied(doesTodaySatisfy());
                }
            } catch(InterruptedException e) {
                getLog().w("DayMonitor thread interrupted");
            }
		}
	}
}