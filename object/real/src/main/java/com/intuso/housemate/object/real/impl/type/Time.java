package com.intuso.housemate.object.real.impl.type;

/**
 * Simple representation of a time
 */
public class Time implements Comparable<Time> {

    private int hours, minutes, seconds;

    private Time() {
    }

    /**
     * @param hours the number of hours since midnight
     * @param minutes the number of minutes since the beginning of the hour
     * @param seconds the number of seconds since the beginning of the minute
     */
    public Time(int hours, int minutes, int seconds) {
        this.hours = hours;
        this.minutes = minutes;
        this.seconds = seconds;
    }

    /**
     * Gets the number of hours since midnight
     * @return the number of hours since midnight
     */
    public int getHours() {
        return hours;
    }

    /**
     * Gets the number of minutes since the beginning of the hour
     * @return the number of minutes since the beginning of the hour
     */
    public int getMinutes() {
        return minutes;
    }

    /**
     * Gets the number of seconds since the beginning of the minute
     * @return the number of seconds since the beginning of the minute
     */
    public int getSeconds() {
        return seconds;
    }

    /**
     * Calculates the different between this time and the given time
     * @param time the time to subtract
     * @return the number of milliseconds from the given time to this time
     */
    public long minus(Time time) {
        long result = 0;
        result += 60 * 60 * 1000 * (hours - time.hours);
        result += 60 * 1000 * (minutes - time.minutes);
        result += 1000 * (seconds - time.seconds);
        return result;
    }

    @Override
    public String toString() {
        return hours + ":" + minutes + ":" + seconds;
    }

    @Override
    public int compareTo(Time time) {
        if(time == null)
            return -1;
        if(hours != time.hours)
            return hours - time.hours;
        if(minutes != time.minutes)
            return minutes - time.minutes;
        return seconds - time.seconds;
    }
}
