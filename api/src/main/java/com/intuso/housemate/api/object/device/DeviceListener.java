package com.intuso.housemate.api.object.device;

import com.intuso.housemate.api.object.primary.PrimaryListener;

/**
 *
 * Listener interface for devices
 */
public interface DeviceListener<D extends Device<?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?>> extends PrimaryListener<D> {}
