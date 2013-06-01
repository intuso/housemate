package com.intuso.housemate.api.comms;

import com.intuso.housemate.api.HousemateException;
import com.intuso.housemate.api.comms.message.NoPayload;
import com.intuso.housemate.api.resources.Resources;
import com.intuso.utilities.log.Log;

import java.util.Arrays;

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

    protected final void connected() {
        try {
            messageReceived(new Message<NoPayload>(new String[] {""}, CONNECTION_MADE, NoPayload.VALUE, Arrays.asList(ALL_CLIENTS_RECURSE)));
        } catch(HousemateException e) {
            getLog().e("Failed to inform clients that the connection has been made");
        };
	}

    protected final void disconnected() {
        try {
            messageReceived(new Message<NoPayload>(new String[] {""}, CONNECTION_LOST, NoPayload.VALUE, Arrays.asList(ALL_CLIENTS_RECURSE)));
        } catch(HousemateException e) {
            getLog().e("Failed to inform clients that the connection has been lost");
        };
    }
}