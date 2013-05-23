package com.intuso.housemate.api.comms;

import com.intuso.housemate.api.HousemateException;
import com.intuso.housemate.api.resources.Resources;
import com.intuso.utils.log.Log;

/**
 * Abstract class for all Housemate comms. Implemented for each Platform that
 * Housemate will run on
 * @author tclabon
 *
 */
public abstract class Comms extends Router {

    protected final Log log;
	
	/**
	 * Create a new comms instance
	 * @throws HousemateException if an error occurs creating the comms instance
	 */
	protected Comms(Resources resources) {
        super(resources);
        this.log = resources.getLog();
	}

	/**
	 * Get the log to use
	 * @return the log to use
	 */
	protected final Log getLog() {
		return log;
	}

    protected final void connected(boolean connected) {
        // TODO add a list of Comms listeners for connection changes. Broker can then add itself as a comms listener
        // to call brokerConnected() etc
        //root.brokerConnected();
	}
}