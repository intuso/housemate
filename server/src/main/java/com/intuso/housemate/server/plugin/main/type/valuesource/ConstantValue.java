package com.intuso.housemate.server.plugin.main.type.valuesource;

import com.intuso.housemate.client.real.api.internal.RealType;
import com.intuso.housemate.object.api.internal.TypeInstances;
import com.intuso.housemate.object.api.internal.Value;
import com.intuso.housemate.server.plugin.main.type.constant.ConstantInstance;
import com.intuso.utilities.listener.ListenersFactory;

/**
 */
public class ConstantValue extends ValueSource {

    private final ConstantInstance value;

    public <O> ConstantValue(ListenersFactory listenersFactory, RealType<?, ?, O> type, TypeInstances newValue) {
        super(listenersFactory);
        value = new ConstantInstance<>(listenersFactory, type, newValue);
    }

    @Override
    public Value<TypeInstances, ?> getValue() {
        return value;
    }

}
