package com.intuso.housemate.api.object.primary;

import com.intuso.housemate.api.object.ObjectListener;

public interface PrimaryListener<PO extends PrimaryObject<?, ?, ?, ?, ?, ?, ?, ?>> extends ObjectListener {

	/**
	 * Set the entityWrapper to be in error (or not)
	 * @param primaryObject the device that is in error (or not)
	 * @param error description of the error
	 */
	public void error(PO primaryObject, String error);

	/**
	 * Called when the entityWrapper starts or stops running
	 * @param primaryObject the device that is started/stopped
	 * @param running whether it's running or not
	 */
	public void running(PO primaryObject, boolean running);
}
