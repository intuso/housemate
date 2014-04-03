package com.intuso.housemate.server.plugin.main.type.valuesource;

import com.intuso.housemate.api.object.value.Value;
import com.intuso.utilities.listener.ListenerRegistration;
import com.intuso.utilities.listener.Listeners;
import com.intuso.utilities.listener.ListenersFactory;

/**
 */
public abstract class ValueSource {

    protected final Listeners<ValueAvailableListener> listeners;

    public ValueSource(ListenersFactory listenersFactory) {
        this.listeners = listenersFactory.create();
    }

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
