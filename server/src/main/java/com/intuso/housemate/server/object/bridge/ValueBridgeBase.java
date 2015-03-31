package com.intuso.housemate.server.object.bridge;

import com.intuso.housemate.api.object.HousemateData;
import com.intuso.housemate.api.object.type.TypeInstances;
import com.intuso.housemate.api.object.value.Value;
import com.intuso.housemate.api.object.value.ValueBaseData;
import com.intuso.housemate.api.object.value.ValueListener;
import com.intuso.utilities.listener.ListenerRegistration;
import com.intuso.utilities.listener.ListenersFactory;
import com.intuso.utilities.log.Log;

import java.util.List;

/**
 */
public abstract class ValueBridgeBase<WBL extends ValueBaseData<SWBL>,
            SWBL extends HousemateData<?>,
            SWR extends BridgeObject<? extends SWBL, ?, ?, ?, ?>,
            V extends ValueBridgeBase<WBL, SWBL, SWR, V>>
        extends BridgeObject<WBL, SWBL, SWR, V, ValueListener<? super V>>
        implements Value<TypeBridge, V> {

    private Value proxyValue;

    public ValueBridgeBase(Log log, ListenersFactory listenersFactory, WBL data, Value<?, ?> value) {
        super(log, listenersFactory, data);
        proxyValue = value;
    }

    @Override
    public String getTypeId() {
        return getData().getType();
    }

    @Override
    public TypeInstances getTypeInstances() {
        return proxyValue.getTypeInstances();
    }

    @Override
    protected List<ListenerRegistration> registerListeners() {
        List<ListenerRegistration> result = super.registerListeners();
        result.add(proxyValue.addObjectListener(new ValueListener<Value<?, ?>>() {

            @Override
            public void valueChanging(Value<?, ?> value) {
                for(ValueListener<? super V> listener : getObjectListeners())
                    listener.valueChanging(getThis());
            }

            @Override
            public void valueChanged(Value<?, ?> value) {
                getData().setTypeInstances(value.getTypeInstances());
                for(ValueListener<? super V> listener : getObjectListeners())
                    listener.valueChanged(getThis());
                broadcastMessage(VALUE_ID, value.getTypeInstances());
            }}));
        return result;
    }

}
