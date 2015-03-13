package com.intuso.housemate.api.object.hardware;

import com.intuso.housemate.api.object.HousemateObjectFactory;

/**
 *
 * Factory for devices
 */
public interface HardwareFactory<HARDWARE extends Hardware<?, ?>>
        extends HousemateObjectFactory<HardwareData, HARDWARE> {
}
