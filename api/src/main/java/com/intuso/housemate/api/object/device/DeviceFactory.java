package com.intuso.housemate.api.object.device;

import com.intuso.housemate.api.object.HousemateObjectFactory;

/**
 *
 * Factory for devices
 */
public interface DeviceFactory<
            D extends Device<?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?>>
        extends HousemateObjectFactory<DeviceData, D> {
}
