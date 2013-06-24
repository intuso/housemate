package com.intuso.housemate.broker;

import com.intuso.housemate.api.HousemateException;

/**
 * Main class for the broker
 *
 */
public class App {

	public static void main(String args[]) throws HousemateException {
        start(args);
    }

    public static BrokerServerEnvironment start(String[] args) throws HousemateException {
		return new BrokerServerEnvironment(args);
	}
}
