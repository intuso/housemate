package com.intuso.housemate.client.proxy.api.internal;

import com.intuso.housemate.client.api.internal.UsesDriver;
import com.intuso.housemate.client.api.internal.object.Property;
import com.intuso.housemate.client.api.internal.object.Value;

/**
 * Classes implementing this are runnable objects
 * @param <DRIVER_LOADED_VALUE> the type of the value
 */
public interface ProxyUsesDriver<DRIVER_PROPERTY extends Property<?, ?, ?, ?>, DRIVER_LOADED_VALUE extends Value<?, ?, ?>> extends UsesDriver<DRIVER_PROPERTY, DRIVER_LOADED_VALUE> {
    boolean isDriverLoaded();
}