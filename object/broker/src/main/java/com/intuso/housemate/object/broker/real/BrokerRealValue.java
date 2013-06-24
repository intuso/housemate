package com.intuso.housemate.object.broker.real;

import com.intuso.housemate.api.object.NoChildrenWrappable;
import com.intuso.housemate.api.object.value.Value;
import com.intuso.housemate.api.object.value.ValueWrappable;
import com.intuso.housemate.object.real.RealType;

/**
 */
public class BrokerRealValue<O>
        extends BrokerRealValueBase<ValueWrappable, NoChildrenWrappable, NoChildrenBrokerRealObject, O, BrokerRealValue<O>>
        implements Value<RealType<?, ?, O>, BrokerRealValue<O>> {

    public BrokerRealValue(BrokerRealResources resources, String id, String name, String description, RealType<?, ?, O> type, O value) {
        super(resources, new ValueWrappable(id, name, description, type.getId(), type.serialise(value)), type);
    }
}
