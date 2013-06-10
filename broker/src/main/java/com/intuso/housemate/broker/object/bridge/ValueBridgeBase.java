package com.intuso.housemate.broker.object.bridge;

import com.intuso.housemate.api.object.HousemateObjectWrappable;
import com.intuso.housemate.api.object.type.TypeInstance;
import com.intuso.housemate.api.object.value.Value;
import com.intuso.housemate.api.object.value.ValueListener;
import com.intuso.housemate.api.object.value.ValueWrappableBase;
import com.intuso.utilities.listener.ListenerRegistration;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: ravnroot
 * Date: 17/07/12
 * Time: 23:48
 * To change this template use File | Settings | File Templates.
 */
public class ValueBridgeBase<WBL extends ValueWrappableBase<SWBL>,
            SWBL extends HousemateObjectWrappable<?>,
            SWR extends BridgeObject<? extends SWBL, ?, ?, ?, ?>,
            V extends ValueBridgeBase<WBL, SWBL, SWR, V>>
        extends BridgeObject<WBL, SWBL, SWR, V, ValueListener<? super V>>
        implements Value<TypeBridge, V> {

    private Value proxyValue;
    private final TypeBridge type;

    public ValueBridgeBase(BrokerBridgeResources resources, WBL wrappable, Value<?, ?> value) {
        super(resources, wrappable);
        proxyValue = value;
        type = resources.getGeneralResources().getBridgeResources().getRoot().getTypes().get(getWrappable().getType());
    }

    @Override
    public TypeBridge getType() {
        return type;
    }

    @Override
    public TypeInstance getTypeInstance() {
        return getWrappable().getValue();
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
                getWrappable().setValue(value.getTypeInstance());
                for(ValueListener<? super V> listener : getObjectListeners())
                    listener.valueChanged(getThis());
                broadcastMessage(VALUE, value.getTypeInstance());
            }}));
        return result;
    }

}
