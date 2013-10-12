package com.intuso.housemate.api.object.primary;

import com.intuso.housemate.api.object.ObjectListener;

/**
 *
 * Listener interface for primary objects
 *
 * @param <PO>
 */
public interface PrimaryListener<PO extends PrimaryObject<?, ?, ?, ?, ?, ?>> extends ObjectListener {

	/**
	 * Notifies that the primary object's error has changed
	 * @param primaryObject the primary objects that is in error (or not)
	 * @param error the description of the error
	 */
	public void error(PO primaryObject, String error);

	/**
	 * Notifies that the primary object's running status has changed
	 * @param primaryObject the primary object that was started/stopped
	 * @param running whether it's running or not
	 */
	public void running(PO primaryObject, boolean running);
}
