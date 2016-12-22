package com.intuso.housemate.client.api.internal.type;

import com.intuso.housemate.client.api.internal.annotation.Composite;
import com.intuso.housemate.client.api.internal.annotation.Id;
import com.intuso.housemate.client.api.internal.annotation.IntegerWrap;

/**
 * Simple representation of a time
 */
@Id(value = "time", name = "Time", description = "Time of day")
@Composite
public class Time implements Comparable<Time> {

    @Id(value = "hour", name = "Hour", description = "Hour of the day")
    @IntegerWrap(min = 0, max = 23)
    private int hour;

    @Id(value = "minute", name = "Minute", description = "Minute of the hour")
    @IntegerWrap(min = 0, max = 59)
    private int minute;

    @Id(value = "second", name = "Second", description = "Second of the day")
    @IntegerWrap(min = 0, max = 59)
    private int second;

    private Time() {}

    /**
     * @param hour the number of hours since midnight
     * @param minute the number of minutes since the beginning of the hour
     * @param second the number of seconds since the beginning of the minute
     */
    public Time(int hour, int minute, int second) {
        this.hour = hour;
        this.minute = minute;
        this.second = second;
    }

    /**
     * Gets the number of hours since midnight
     * @return the number of hours since midnight
     */
    public int getHour() {
        return hour;
    }

    /**
     * Gets the number of minutes since the beginning of the hour
     * @return the number of minutes since the beginning of the hour
     */
    public int getMinute() {
        return minute;
    }

    /**
     * Gets the number of seconds since the beginning of the minute
     * @return the number of seconds since the beginning of the minute
     */
    public int getSecond() {
        return second;
    }

    /**
     * Calculates the different between this time and the given time
     * @param time the time to subtract
     * @return the number of milliseconds from the given time to this time
     */
    public long minus(Time time) {
        long result = 0;
        result += 60 * 60 * 1000 * (hour - time.hour);
        result += 60 * 1000 * (minute - time.minute);
        result += 1000 * (second - time.second);
        return result;
    }

    @Override
    public String toString() {
        return hour + ":" + minute + ":" + second;
    }

    @Override
    public int compareTo(Time time) {
        if(time == null)
            return -1;
        if(hour != time.hour)
            return hour - time.hour;
        if(minute != time.minute)
            return minute - time.minute;
        return second - time.second;
    }
}
