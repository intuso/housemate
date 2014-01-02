package com.intuso.housemate.api.comms;

import com.intuso.housemate.api.HousemateException;
import com.intuso.utilities.listener.Listener;

/**
 *
 * Receiver of messages from the server
 */
public interface Receiver<T extends Message.Payload> extends Listener {
	
	/**
	 * Notifies when a message has been received
	 * @param message the message that was received
	 */
	public abstract void messageReceived(Message<T> message) throws HousemateException;
}
