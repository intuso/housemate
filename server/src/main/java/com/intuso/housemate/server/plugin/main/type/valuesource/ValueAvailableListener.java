package com.intuso.housemate.server.plugin.main.type.valuesource;

import com.intuso.housemate.object.api.internal.TypeInstances;
import com.intuso.housemate.object.api.internal.Value;
import com.intuso.utilities.listener.Listener;

/**
 */
public interface ValueAvailableListener extends Listener {
    void valueAvailable(ValueSource source, Value<TypeInstances, ?> value);
    void valueUnavailable(ValueSource source);
}
