package com.intuso.housemate.server.object.bridge;

import com.intuso.housemate.comms.api.internal.payload.HousemateData;
import com.intuso.housemate.comms.api.internal.payload.TypeData;
import com.intuso.housemate.comms.api.internal.payload.ValueBaseData;
import com.intuso.housemate.object.api.internal.TypeInstances;
import com.intuso.housemate.object.api.internal.ValueBase;
import com.intuso.utilities.listener.ListenerRegistration;
import com.intuso.utilities.listener.ListenersFactory;
import org.slf4j.Logger;

import java.util.List;

/**
 */
public abstract class ValueBridgeBase<WBL extends ValueBaseData<SWBL>,
            SWBL extends HousemateData<?>,
            SWR extends BridgeObject<? extends SWBL, ?, ?, ?, ?>,
            LISTENER extends ValueBase.Listener<? super VALUE>,
            VALUE extends ValueBridgeBase<WBL, SWBL, SWR, LISTENER, VALUE>>
        extends BridgeObject<WBL, SWBL, SWR, VALUE, LISTENER>
        implements ValueBase<TypeInstances, LISTENER, VALUE> {

    private ValueBase<TypeInstances, ValueBase.Listener<? super ValueBase<TypeInstances, ?, ?>>, ValueBase<TypeInstances, ?, ?>> valueBase;

    public ValueBridgeBase(Logger logger, ListenersFactory listenersFactory, WBL data, ValueBase<?, ?, ?> valueBase) {
        super(logger, listenersFactory, data);
        this.valueBase = (ValueBase<TypeInstances, Listener<? super ValueBase<TypeInstances, ?, ?>>, ValueBase<TypeInstances, ?, ?>>) valueBase;
    }

    @Override
    public String getTypeId() {
        return getData().getType();
    }

    @Override
    public TypeInstances getValue() {
        return valueBase.getValue();
    }

    @Override
    protected List<ListenerRegistration> registerListeners() {
        List<ListenerRegistration> result = super.registerListeners();
        result.add(valueBase.addObjectListener(new ValueBase.Listener<ValueBase<TypeInstances, ?, ?>>() {

            @Override
            public void valueChanging(ValueBase<TypeInstances, ?, ?> value) {
                for(ValueBase.Listener<? super VALUE> listener : getObjectListeners())
                    listener.valueChanging(getThis());
            }

            @Override
            public void valueChanged(ValueBase<TypeInstances, ?, ?> value) {
                getData().setTypeInstances(value.getValue());
                for(ValueBase.Listener<? super VALUE> listener : getObjectListeners())
                    listener.valueChanged(getThis());
                broadcastMessage(ValueBaseData.VALUE_ID, new TypeData.TypeInstancesPayload(value.getValue()));
            }}));
        return result;
    }

}
