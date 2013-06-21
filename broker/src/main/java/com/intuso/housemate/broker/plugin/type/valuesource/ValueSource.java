package com.intuso.housemate.broker.plugin.type.valuesource;

import com.intuso.housemate.api.object.value.Value;
import com.intuso.utilities.listener.ListenerRegistration;
import com.intuso.utilities.listener.Listeners;

/**
 * Created with IntelliJ IDEA.
 * User: ravnroot
 * Date: 03/06/13
 * Time: 23:56
 * To change this template use File | Settings | File Templates.
 */
public abstract class ValueSource {

    protected final Listeners<ValueAvailableListener> listeners = new Listeners<ValueAvailableListener>();

    public ListenerRegistration addValueAvailableListener(ValueAvailableListener listener) {
        return listeners.addListener(listener);
    }

    public ListenerRegistration addValueAvailableListener(ValueAvailableListener listener, boolean callForExisting) {
        ListenerRegistration result = addValueAvailableListener(listener);
        if(callForExisting) {
            if(getValue() != null)
                listener.valueAvailable(this, getValue());
            else
                listener.valueUnavailable(this);
        }
        return result;
    }

    public abstract Value<?, ?> getValue();
}
