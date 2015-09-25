package com.intuso.housemate.server.comms;

import com.intuso.housemate.comms.api.internal.HousemateCommsException;

import java.util.Arrays;
import java.util.Stack;

/**
 * Exception for sending to client's when they connect with an unknown type
 */
public class UnknownClientTypeException extends HousemateCommsException {
    public UnknownClientTypeException(Stack<String> route) {
        super("Unknown client type for route " + Arrays.toString(route.toArray(new String[route.size()])));
    }
}
