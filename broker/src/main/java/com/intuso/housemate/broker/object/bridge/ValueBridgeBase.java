package com.intuso.housemate.broker.object.bridge;

import com.intuso.housemate.core.comms.message.StringMessageValue;
import com.intuso.housemate.core.object.HousemateObjectWrappable;
import com.intuso.housemate.core.object.value.Value;
import com.intuso.housemate.core.object.value.ValueListener;
import com.intuso.housemate.core.object.value.ValueWrappableBase;
import com.intuso.listeners.ListenerRegistration;

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
    public String getValue() {
        return getWrappable().getValue();
    }

    @Override
    protected List<ListenerRegistration<?>> registerListeners() {
        List<ListenerRegistration<?>> result = super.registerListeners();
        result.add(proxyValue.addObjectListener(new ValueListener<Value<?, ?>>() {
            @Override
            public void valueChanged(Value<?, ?> value) {
                getWrappable().setValue(value.getValue());
                broadcastMessage(VALUE, new StringMessageValue(value.getValue()));
            }}));
        return result;
    }

}
