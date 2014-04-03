package com.intuso.housemate.server.plugin.main.type.valuesource;

import com.intuso.housemate.api.object.type.TypeInstances;
import com.intuso.housemate.api.object.value.Value;
import com.intuso.housemate.server.plugin.main.type.constant.ConstantInstance;
import com.intuso.housemate.object.real.RealType;
import com.intuso.utilities.listener.ListenersFactory;

/**
 */
public class ConstantValue extends ValueSource {

    private final ConstantInstance value;

    public ConstantValue(ListenersFactory listenersFactory, RealType<?, ?, ?> type, TypeInstances newValue) {
        super(listenersFactory);
        value = new ConstantInstance(listenersFactory, type, newValue);
    }

    @Override
    public Value<?, ?> getValue() {
        return value;
    }

}
