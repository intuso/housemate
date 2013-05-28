package com.intuso.housemate.api.comms;

import com.intuso.housemate.api.HousemateException;
import com.intuso.utilities.listener.Listener;

/**
 * Callback for when a message has been received
 * @author tclabon
 *
 */
public interface Receiver<T extends Message.Payload> extends Listener {
	
	/**
	 * Called when a message has been received on a path that the listener was registered to
	 * @param message
	 */
	public abstract void messageReceived(Message<T> message) throws HousemateException;
}
