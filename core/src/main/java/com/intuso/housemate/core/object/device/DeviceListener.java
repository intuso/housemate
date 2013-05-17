package com.intuso.housemate.core.object.device;

import com.intuso.housemate.core.object.primary.PrimaryListener;

/**
 * Interface to implement to receive updates about the status of a device
 * @author tclabon
 */
public interface DeviceListener<D extends Device<?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?>> extends PrimaryListener<D> {}
