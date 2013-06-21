package com.intuso.housemate.broker.plugin.type.valuesource;

import com.intuso.housemate.api.object.value.Value;
import com.intuso.utilities.listener.Listener;

/**
 * Created with IntelliJ IDEA.
 * User: ravnroot
 * Date: 05/06/13
 * Time: 18:34
 * To change this template use File | Settings | File Templates.
 */
public interface ValueAvailableListener extends Listener {
    public void valueAvailable(ValueSource source, Value<?, ?> value);
    public void valueUnavailable(ValueSource source);
}
