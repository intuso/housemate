package com.intuso.housemate.broker.comms;

import com.intuso.housemate.api.HousemateException;

import java.util.Arrays;
import java.util.List;

/**
 */
public class UnknownClientRouteException extends HousemateException {
    public UnknownClientRouteException(List<String> route) {
        super("Unknown client route " + Arrays.toString(route.toArray(new String[route.size()])));
    }
}
