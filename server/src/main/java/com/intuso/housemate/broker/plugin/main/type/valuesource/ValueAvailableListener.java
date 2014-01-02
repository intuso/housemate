package com.intuso.housemate.broker.plugin.main.type.valuesource;

import com.intuso.housemate.api.object.value.Value;
import com.intuso.utilities.listener.Listener;

/**
 */
public interface ValueAvailableListener extends Listener {
    public void valueAvailable(ValueSource source, Value<?, ?> value);
    public void valueUnavailable(ValueSource source);
}
