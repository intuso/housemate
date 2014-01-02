package com.intuso.housemate.server.comms;

import com.intuso.housemate.api.HousemateException;

import java.util.Arrays;
import java.util.List;

/**
 * Exception to use when a message is sent to a route that is not known
 */
public class UnknownClientRouteException extends HousemateException {
    public UnknownClientRouteException(List<String> route) {
        super("Unknown client route " + Arrays.toString(route.toArray(new String[route.size()])));
    }
}
