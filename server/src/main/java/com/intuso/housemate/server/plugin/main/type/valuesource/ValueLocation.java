package com.intuso.housemate.server.plugin.main.type.valuesource;

import com.intuso.housemate.api.object.HousemateObject;
import com.intuso.housemate.api.object.ObjectLifecycleListener;
import com.intuso.housemate.api.object.value.Value;
import com.intuso.housemate.object.real.impl.type.RealObjectType;
import com.intuso.housemate.server.object.bridge.RootBridge;
import com.intuso.housemate.server.object.bridge.ValueBridge;
import com.intuso.utilities.listener.ListenerRegistration;
import com.intuso.utilities.listener.ListenersFactory;

/**
 */
public class ValueLocation extends ValueSource implements ObjectLifecycleListener {

    private final RealObjectType.Reference<Value<?, ?>> objectReference;
    private final ListenerRegistration lifecycleListenerRegistration;

    public ValueLocation(ListenersFactory listenersFactory, RealObjectType.Reference<Value<?, ?>> objectReference, RootBridge root) {
        super(listenersFactory);
        this.objectReference = objectReference;
        lifecycleListenerRegistration = objectReference != null
                ? root.addObjectLifecycleListener(objectReference.getPath(), this)
                : null;
    }

    public RealObjectType.Reference<Value<?, ?>> getObjectReference() {
        return objectReference;
    }

    @Override
    public Value<?, ?> getValue() {
        return objectReference != null ? objectReference.getObject() : null;
    }

    @Override
    public void objectCreated(String[] path, HousemateObject<?, ?, ?, ?> object) {
        if(object instanceof ValueBridge) {
            objectReference.setObject((Value<?, ?>)object);
            for(ValueAvailableListener listener : listeners)
                listener.valueAvailable(this, objectReference.getObject());
        }
    }

    @Override
    public void objectRemoved(String[] path, HousemateObject<?, ?, ?, ?> object) {
        for(ValueAvailableListener listener : listeners)
            listener.valueUnavailable(this);
    }
}
