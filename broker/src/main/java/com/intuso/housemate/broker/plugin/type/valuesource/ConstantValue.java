package com.intuso.housemate.broker.plugin.type.valuesource;

import com.intuso.housemate.api.object.type.TypeInstances;
import com.intuso.housemate.api.object.value.Value;
import com.intuso.housemate.broker.plugin.type.constant.ConstantInstance;
import com.intuso.housemate.object.real.RealType;

/**
 */
public class ConstantValue extends ValueSource {

    private final ConstantInstance<Object> value;

    public ConstantValue(RealType<?, ?, Object> type, TypeInstances newValue) {
        value = new ConstantInstance<Object>(type, newValue);
    }

    @Override
    public Value<?, ?> getValue() {
        return value;
    }

}
