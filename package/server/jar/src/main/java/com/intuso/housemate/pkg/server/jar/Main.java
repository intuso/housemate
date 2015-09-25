package com.intuso.housemate.pkg.server.jar;

/**
 * Main class for the server
 *
 */
public class Main {

	public static void main(String args[]) {
        start(args);
    }

    public static ServerEnvironment start(String[] args) {
		return new ServerEnvironment(args);
	}
}
