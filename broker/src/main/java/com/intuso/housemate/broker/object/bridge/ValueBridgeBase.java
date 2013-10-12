package com.intuso.housemate.broker.object.bridge;

import com.intuso.housemate.api.object.HousemateData;
import com.intuso.housemate.api.object.type.TypeInstances;
import com.intuso.housemate.api.object.value.Value;
import com.intuso.housemate.api.object.value.ValueListener;
import com.intuso.housemate.api.object.value.ValueBaseData;
import com.intuso.utilities.listener.ListenerRegistration;

import java.util.List;

/**
 */
public class ValueBridgeBase<WBL extends ValueBaseData<SWBL>,
            SWBL extends HousemateData<?>,
            SWR extends BridgeObject<? extends SWBL, ?, ?, ?, ?>,
            V extends ValueBridgeBase<WBL, SWBL, SWR, V>>
        extends BridgeObject<WBL, SWBL, SWR, V, ValueListener<? super V>>
        implements Value<TypeBridge, V> {

    private Value proxyValue;
    private final TypeBridge type;

    public ValueBridgeBase(BrokerBridgeResources resources, WBL data, Value<?, ?> value) {
        super(resources, data);
        proxyValue = value;
        type = resources.getGeneralResources().getBridgeResources().getRoot().getTypes().get(getData().getType());
    }

    @Override
    public TypeBridge getType() {
        return type;
    }

    @Override
    public TypeInstances getTypeInstances() {
        return getData().getTypeInstances();
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
