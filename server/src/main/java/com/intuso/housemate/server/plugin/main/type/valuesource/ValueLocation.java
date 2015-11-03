package com.intuso.housemate.server.plugin.main.type.valuesource;

import com.intuso.housemate.client.real.impl.internal.type.RealObjectType;
import com.intuso.housemate.object.api.internal.*;
import com.intuso.housemate.server.object.bridge.ValueBridge;
import com.intuso.utilities.listener.ListenerRegistration;
import com.intuso.utilities.listener.ListenersFactory;

/**
 */
public class ValueLocation extends ValueSource implements ObjectLifecycleListener {

    private final RealObjectType.Reference<Value<TypeInstances, ?>> objectReference;
    private final ListenerRegistration lifecycleListenerRegistration;

    public ValueLocation(ListenersFactory listenersFactory, RealObjectType.Reference<Value<TypeInstances, ?>> objectReference, ObjectRoot<?, ?> root) {
        super(listenersFactory);
        this.objectReference = objectReference;
        lifecycleListenerRegistration = objectReference != null
                ? root.addObjectLifecycleListener(objectReference.getPath(), this)
                : null;
    }

    public RealObjectType.Reference<Value<TypeInstances, ?>> getObjectReference() {
        return objectReference;
    }

    @Override
    public Value<TypeInstances, ?> getValue() {
        return objectReference != null ? objectReference.getObject() : null;
    }

    @Override
    public void objectCreated(String[] path, BaseHousemateObject<?> object) {
        if(object instanceof ValueBridge) {
            objectReference.setObject((Value<TypeInstances, ?>)object);
            for(ValueAvailableListener listener : listeners)
                listener.valueAvailable(this, objectReference.getObject());
        }
    }

    @Override
    public void objectRemoved(String[] path, BaseHousemateObject<?> object) {
        for(ValueAvailableListener listener : listeners)
            listener.valueUnavailable(this);
    }
}
