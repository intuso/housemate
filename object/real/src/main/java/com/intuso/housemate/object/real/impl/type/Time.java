package com.intuso.housemate.object.real.impl.type;

/**
 * Created with IntelliJ IDEA.
 * User: ravnroot
 * Date: 26/10/12
 * Time: 13:39
 * To change this template use File | Settings | File Templates.
 */
public class Time implements Comparable<Time> {
    private int hours, minutes, seconds;

    private Time() {
    }

    public Time(int hours, int minutes, int seconds) {
        this.hours = hours;
        this.minutes = minutes;
        this.seconds = seconds;
    }

    public int getHours() {
        return hours;
    }

    public int getMinutes() {
        return minutes;
    }

    public int getSeconds() {
        return seconds;
    }

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
