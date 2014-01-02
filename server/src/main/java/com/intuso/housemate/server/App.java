package com.intuso.housemate.server;

import com.intuso.housemate.api.HousemateException;

/**
 * Main class for the server
 *
 */
public class App {

	public static void main(String args[]) throws HousemateException {
        start(args);
    }

    public static ServerEnvironment start(String[] args) throws HousemateException {
		return new ServerEnvironment(args);
	}
}
