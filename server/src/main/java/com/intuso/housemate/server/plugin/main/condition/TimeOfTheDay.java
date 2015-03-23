package com.intuso.housemate.server.plugin.main.condition;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import com.intuso.housemate.api.HousemateException;
import com.intuso.housemate.api.object.condition.ConditionData;
import com.intuso.housemate.object.real.RealCondition;
import com.intuso.housemate.object.real.RealProperty;
import com.intuso.housemate.object.real.factory.condition.RealConditionOwner;
import com.intuso.housemate.object.real.impl.type.Time;
import com.intuso.housemate.object.real.impl.type.TimeType;
import com.intuso.housemate.plugin.api.TypeInfo;
import com.intuso.utilities.listener.ListenersFactory;
import com.intuso.utilities.log.Log;

import java.util.Calendar;

/**
 * Condition that is true during a set period of time
 *
 */
@TypeInfo(id = "time-of-the-day", name = "Time of the Day", description = "Condition that is true for certain parts of the day")
public class TimeOfTheDay extends RealCondition {

    public final static String BEFORE_FIELD = "before";
    public final static String AFTER_FIELD = "after";
    
	/**
	 * Start of the day = midnight = 00:00:00.000
	 */
	public final static Time DAY_START = new Time(0, 0, 0);
	
	/**
	 * End of the day = midnight minus 1 millisecond = 23:59:59.999
	 */
	public final static Time DAY_END = new Time(23, 59, 59);
	
	/**
	 * Maximum time to wait for = 10 minutes
	 */
	private final static long WAIT_MAX = 10 * 60 * 1000;

    /**
     * String version of the time that, once passed, makes the condition unsatisfied. Default is the start of the day
     */
    private final RealProperty<Time> before;

    /**
     * String version of the time that, until reached, makes the condition unsatisfied
     */
    private final RealProperty<Time> after;

	/**
	 * thread that runs to tell listener when condition is (un)satisfied
	 */
	private Thread monitor;

    @Inject
	public TimeOfTheDay(Log log,
                        ListenersFactory listenersFactory,
                        @Assisted ConditionData data,
                        @Assisted RealConditionOwner owner,
                        TimeType timeType)
            throws HousemateException {
		super(log, listenersFactory, data, owner);
        before = new RealProperty<Time>(log, listenersFactory, BEFORE_FIELD, BEFORE_FIELD, "The condition is satisfied when the current time is before this time",
                timeType, DAY_END);
        after = new RealProperty<Time>(log, listenersFactory, AFTER_FIELD, AFTER_FIELD, "The condition is satisfied when the current time is after this time",
                timeType, DAY_END);
        getProperties().add(before);
        getProperties().add(after);
    }
	
	/**
	 * Check if the current time of day satisfies the condition
	 * @return true iff current time is after after and before before
	 */
	protected final boolean doesNowSatisfy() {
		Time cur_time = getTime();

        Time after = this.after.getTypedValue();
        Time before = this.before.getTypedValue();
		
		// if the after time is before the before time then return true if current time is between them
		if(after.compareTo(before) <= 0)
			return (cur_time.compareTo(after) >= 0 && cur_time.compareTo(before) <= 0);
		
		// else "satisfied" period spans midnight so has to be after after or before before
		return (cur_time.compareTo(after) >= 0 || cur_time.compareTo(before) <= 0);
	}
	
	/**
	 * parse a time string. The format is hh:mm:ss.mmm where only hh has to be given
	 * @param in the time string
	 * @return a long representing the number of milliseconds from 00:00:00.000 until the time
	 * @throws HousemateException if the time cannot be parsed
	 */
	private long parseTime(String in) throws HousemateException {
		String time = in.trim();
		int index = time.indexOf(":");
		short hours = 0, minutes = 0, seconds = 0, millis = 0;
		
		// if the first colon exists
		if(index > 0) {
			// hours = the integer up until the first colon
			hours = Short.parseShort(time.substring(0, index));
			
			// remove the hours part from the string
			time = time.substring(index + 1);
			
			// find the next colon
			index = time.indexOf(":");
			
			// if the next colon exists
			if(index > 0) {
				// read the minutes and seconds split by the colon
				minutes = Short.parseShort(time.substring(0, index));
				
				// remove the minutes part from the string
				time = time.substring(index + 1);
				
				// find a .
				index = time.indexOf(".");
				
				// if a dot exists
				if(index > 0) {
					// read the seconds and millis part of the time string
					seconds = Short.parseShort(time.substring(0, index));
					millis = Short.parseShort(time.substring(index + 1));
				// if the dot doesn't exist
				} else
					seconds = Short.parseShort(time);
			// if the second colon doesn't exist
			} else
				minutes = Short.parseShort(time);
		// if the first colon doesn't exist
		} else
			hours = Short.parseShort(time);
		
		// verify the time components
		if(hours < 0 || hours > 23) {
			getLog().e("Invalid hours value in time \"" + in + "\". Must be between 0 and 23 inclusive");
			throw new HousemateException("Invalid hours value in time \"" + in + "\". Must be between 0 and 23 inclusive");
		} else if(minutes < 0 || minutes > 59) {
			getLog().e("Invalid minutes value in time \"" + in + "\". Must be between 0 and 59 inclusive");
			throw new HousemateException("Invalid minutes value in time \"" + in + "\". Must be between 0 and 59 inclusive");
		} else if(seconds < 0 || seconds > 59) {
			getLog().e("Invalid seconds value in time \"" + in + "\". Must be between 0 and 59 inclusive");
			throw new HousemateException("Invalid seconds value in time \"" + in + "\". Must be between 0 and 59 inclusive");
		} else if(millis < 0 || millis > 999) {
			getLog().e("Invalid milliseconds value in time \"" + in + "\". Must be between 0 and 999 inclusive");
			throw new HousemateException("Invalid milliseconds value in time \"" + in + "\". Must be between 0 and 999 inclusive");
		// time is correct
		} else {
			long result = (hours * 3600000) + (minutes * 60000) + (seconds * 1000) + millis;
			getLog().d(in + " in milliseconds after midnight is " + result);
			return result;
		}
	}
	
	/**
	 * get the current time since midnight as a long
	 * @return the number of milliseconds from midnight until now
	 */
	private static final Time getTime() {
		Calendar c = Calendar.getInstance();
		return new Time(c.get(Calendar.HOUR_OF_DAY), c.get(Calendar.MINUTE), c.get(Calendar.SECOND));
	}
	
	/**
	 * Wait for the next occurrence of a time of day
	 * @param time the time to wait until
	 * @param log log to use
	 */
	protected static final void waitUntilNext(Time time, Log log) throws InterruptedException {
		
		log.d("Waiting until current time of day in milliseconds since midnight is " + time);
		
		// get the current time
		Time cur_time = getTime();
		
		// if we're already past, then wait until we go past midnight
		if(cur_time.compareTo(time) > 0) {
			
			log.d("Time to wait until is after midnight, waiting until past midnight");
			
			// save the old time, then wait a bit
			Time old_time = cur_time;
            log.d("Now = " + cur_time + ", end = " + time + ", waiting for " + Math.min(WAIT_MAX, DAY_END.minus(cur_time)));
            Thread.sleep(Math.min(WAIT_MAX, DAY_END.minus(cur_time)));
			
			// past midnight is when the current time is less than the last time we saw
			while(!Thread.currentThread().isInterrupted() && (cur_time = getTime()).compareTo(old_time) >= 0) {
                // wait for a max of 10 minutes
                log.d("Now = " + cur_time + ", end = " + time + ", waiting for " + Math.min(WAIT_MAX, DAY_END.minus(cur_time)));
                Thread.sleep(Math.min(WAIT_MAX, DAY_END.minus(cur_time)));
			}
			
			log.d("Past midnight");
		}
		
		// loop until the current time is past the given time
		while(!Thread.currentThread().isInterrupted() && (cur_time = getTime()).compareTo(time) < 0) {
			// wait for max 10 mins
            long toWait = Math.min(WAIT_MAX, time.minus(cur_time));
			log.d("Now = " + cur_time + ", end = " + time + ", waiting for " + toWait);
			Thread.sleep(Math.min(WAIT_MAX, toWait));
        }
		
		log.d("Reached time to wait until");
	}
	
	@Override
	public void start() {
		// start monitoring the time of the day
		monitor = new TimeMonitorThread();
		monitor.start();
		conditionSatisfied(doesNowSatisfy());
	}
	
	@Override
	public void stop() {
        if(monitor != null) {
		    monitor.interrupt();
		    monitor = null;
        }
	}
	
	/**
	 * Thread that monitors the time of the day and calls conditionSatisfied as appropriate based
	 * on the before and after times parsed by the super class
	 * @author Tom Clabon
	 *
	 */
	private class TimeMonitorThread extends Thread {
		@Override
		public void run() {

            try {
                // we enter the loop expecting it to be unsatisfied,
                // so check if it's already satisfied and if it is then wait until it's not
                if(doesNowSatisfy()) {

                    // wait until unsatisfied so that loop works properly
                    getLog().d("Waiting for condition to become unsatisfied");
                    waitUntilNext(before.getTypedValue(), getLog());
                    conditionSatisfied(false);
                }

                // loop until thread is stopped
                getLog().d("Entering time monitor loop");
                while(!isInterrupted()) {

                    // wait until we next go past the after time
                    getLog().d("Waiting until time of the day goes past the after time");
                    waitUntilNext(after.getTypedValue(), getLog());
                    getLog().d("Time of the day is after the after time");

                    // condition is now satisfied
                    conditionSatisfied(true);

                    // wait until we next go past the before time
                    getLog().d("Waiting until time of the day goes past the before time");
                    waitUntilNext(before.getTypedValue(), getLog());
                    getLog().d("Time of the day is after the before time");

                    // condition is now unsatisfied
                    conditionSatisfied(false);
                }
            } catch(InterruptedException e) {
                getLog().w("TimeMonitor thread interrupted");
            }
		}
	}
}