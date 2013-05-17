package com.intuso.housemate.broker;

import com.intuso.housemate.core.HousemateException;

/**
 * Main class for the broker
 * @author tclabon
 *
 */
public class App {

	public static void main(String args[]) throws HousemateException {
        start(args);
    }

    public static BrokerServerEnvironment start(String[] args) throws HousemateException {
		BrokerServerEnvironment environment = new BrokerServerEnvironment(args);
        environment.getGeneralResources().getRealResources().getRoot().loadObjects();
        return environment;
	}
}
