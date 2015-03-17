package com.intuso.housemate.api.object.hardware;

import com.intuso.housemate.api.object.ObjectListener;

/**
 *
 * Listener interface for devices
 */
public interface HardwareListener<HARDWARE extends Hardware<?, ?>> extends ObjectListener {
    public void deviceConnected(HARDWARE device, boolean connected);
}
