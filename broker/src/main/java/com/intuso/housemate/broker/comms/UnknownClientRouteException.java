package com.intuso.housemate.broker.comms;

import com.intuso.housemate.core.HousemateException;

import java.util.Arrays;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: ravnroot
 * Date: 19/03/13
 * Time: 08:48
 * To change this template use File | Settings | File Templates.
 */
public class UnknownClientRouteException extends HousemateException {
    public UnknownClientRouteException(List<String> route) {
        super("Unknown client route " + Arrays.toString(route.toArray(new String[route.size()])));
    }
}
