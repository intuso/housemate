package com.intuso.housemate.object.real.impl.type;

import com.intuso.housemate.api.HousemateRuntimeException;
import com.intuso.housemate.api.object.type.TypeInstance;
import com.intuso.housemate.object.real.RealResources;

/**
 * Created by IntelliJ IDEA.
 * User: tomc
 * Date: 29/05/12
 * Time: 21:27
 * To change this template use File | Settings | File Templates.
 */
public class TimeType extends RealRegexType<Time> {

    public final static String ID = "time";
    public final static String NAME = "Time";
    public final static String DESCRIPTION = "A time in 24 hour format, eg 01:23:45";
    public final static String REGEX = "([01][0-9]|2[0-3]):[0-5][0-9]:[0-5][0-9]";

    public TimeType(RealResources resources) {
        super(resources, ID, NAME, DESCRIPTION, REGEX);
    }

    @Override
    public TypeInstance serialise(Time s) {
        return s != null ? new TypeInstance(s.toString()) : null;
    }

    @Override
    public Time deserialise(TypeInstance value) throws HousemateRuntimeException {
        if(value == null || value.getValue() == null)
            return null;
        String parts[] = value.getValue().split(":");
        int hours, minutes = 0, seconds = 0;
        try {
            hours = Integer.parseInt(parts[0]);
            if(parts.length > 1)
                minutes = Integer.parseInt(parts[1]);
            if(parts.length > 2)
                seconds = Integer.parseInt(parts[2]);
        } catch(NumberFormatException e) {
            throw new HousemateRuntimeException("Failed to parse time from " + value, e);
        }
        return new Time(hours, minutes, seconds);
    }
}
