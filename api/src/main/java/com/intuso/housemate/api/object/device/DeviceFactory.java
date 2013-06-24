package com.intuso.housemate.api.object.device;

import com.intuso.housemate.api.object.HousemateObjectFactory;
import com.intuso.housemate.api.resources.Resources;

/**
 *
 * Factory for devices
 */
public interface DeviceFactory<
            R extends Resources,
            D extends Device<?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?>>
        extends HousemateObjectFactory<R, DeviceWrappable, D> {
}
