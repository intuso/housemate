package com.intuso.housemate.object.broker.real;

import com.intuso.housemate.api.object.NoChildrenWrappable;
import com.intuso.housemate.api.object.value.Value;
import com.intuso.housemate.api.object.value.ValueWrappable;
import com.intuso.housemate.object.real.RealType;

/**
 * Created by IntelliJ IDEA.
 * User: tomc
 * Date: 24/05/12
 * Time: 00:21
 * To change this template use File | Settings | File Templates.
 */
public class BrokerRealValue<O>
        extends BrokerRealValueBase<ValueWrappable, NoChildrenWrappable, NoChildrenBrokerRealObject, O, BrokerRealValue<O>>
        implements Value<RealType<?, ?, O>, BrokerRealValue<O>> {

    public BrokerRealValue(BrokerRealResources resources, String id, String name, String description, RealType<?, ?, O> type, O value) {
        super(resources, new ValueWrappable(id, name, description, type.getId(), type.serialise(value)), type);
    }

    @Override
    protected BrokerRealValue<O> getThis() {
        return this;
    }
}
