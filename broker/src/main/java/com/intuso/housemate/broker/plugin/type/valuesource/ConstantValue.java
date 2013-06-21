package com.intuso.housemate.broker.plugin.type.valuesource;

import com.intuso.housemate.api.object.type.TypeInstance;
import com.intuso.housemate.api.object.value.Value;
import com.intuso.housemate.broker.plugin.type.constant.ConstantInstance;
import com.intuso.housemate.object.real.RealType;

/**
 * Created with IntelliJ IDEA.
 * User: ravnroot
 * Date: 03/06/13
 * Time: 23:59
 * To change this template use File | Settings | File Templates.
 */
public class ConstantValue extends ValueSource {

    private final ConstantInstance<Object> value;

    public ConstantValue(RealType<?, ?, Object> type, TypeInstance newValue) {
        value = new ConstantInstance<Object>(type, newValue);
    }

    @Override
    public Value<?, ?> getValue() {
        return value;
    }

}
