package com.intuso.housemate.broker.comms;

import com.intuso.housemate.api.HousemateException;

import java.util.Arrays;
import java.util.Stack;

/**
 * Created with IntelliJ IDEA.
 * User: ravnroot
 * Date: 19/03/13
 * Time: 08:48
 * To change this template use File | Settings | File Templates.
 */
public class UnknownClientTypeException extends HousemateException {
    public UnknownClientTypeException(Stack<String> route) {
        super("Unknown client type for route " + Arrays.toString(route.toArray(new String[route.size()])));
    }
}
