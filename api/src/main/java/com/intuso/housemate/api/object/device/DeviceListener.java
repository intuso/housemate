package com.intuso.housemate.api.object.device;

import com.intuso.housemate.api.object.primary.PrimaryListener;

/**
 * Interface to implement to receive updates about the status of a device
 * @author tclabon
 */
public interface DeviceListener<D extends Device<?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?>> extends PrimaryListener<D> {}
