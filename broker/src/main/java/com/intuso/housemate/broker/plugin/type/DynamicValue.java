package com.intuso.housemate.broker.plugin.type;

import com.intuso.housemate.api.object.HousemateObject;
import com.intuso.housemate.api.object.ObjectLifecycleListener;
import com.intuso.housemate.api.object.root.Root;
import com.intuso.housemate.api.object.value.Value;
import com.intuso.housemate.broker.object.bridge.ValueBridge;
import com.intuso.housemate.object.real.impl.type.RealObjectType;
import com.intuso.utilities.listener.ListenerRegistration;

/**
 * Created with IntelliJ IDEA.
 * User: ravnroot
 * Date: 03/06/13
 * Time: 23:59
 * To change this template use File | Settings | File Templates.
 */
public class DynamicValue extends ValueSource implements ObjectLifecycleListener {

    private final RealObjectType.Reference<Value<?, ?>> objectReference;
    private final ListenerRegistration lifecycleListenerRegistration;

    public DynamicValue(RealObjectType.Reference<Value<?, ?>> objectReference, Root<?, ?> root) {
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
    public void objectCreated(String[] path, HousemateObject<?, ?, ?, ?, ?> object) {
        if(object instanceof ValueBridge)
            for(ValueAvailableListener listener : listeners.getListeners())
                listener.valueAvailable(this, objectReference.getObject());
    }

    @Override
    public void objectRemoved(String[] path, HousemateObject<?, ?, ?, ?, ?> object) {
        for(ValueAvailableListener listener : listeners.getListeners())
            listener.valueUnavailable(this);
    }
}
