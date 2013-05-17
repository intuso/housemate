package com.intuso.housemate.broker.object.real;

import com.intuso.housemate.core.object.HousemateObject;
import com.intuso.housemate.core.object.HousemateObjectWrappable;
import com.intuso.housemate.core.object.NoChildrenWrappable;
import com.intuso.housemate.core.object.value.Value;
import com.intuso.housemate.core.object.value.ValueListener;
import com.intuso.housemate.core.object.value.ValueWrappable;
import com.intuso.housemate.core.object.value.ValueWrappableBase;
import com.intuso.housemate.proxy.NoChildrenProxyObject;
import com.intuso.housemate.real.RealType;

/**
 * Created by IntelliJ IDEA.
 * User: tomc
 * Date: 24/05/12
 * Time: 00:21
 * To change this template use File | Settings | File Templates.
 */
public class BrokerRealValue<O>
        extends BrokerRealValueBase<ValueWrappable, NoChildrenWrappable, NoChildrenProxyObject, O, BrokerRealValue<O>>
        implements Value<RealType<?, ?, O>, BrokerRealValue<O>> {

    public BrokerRealValue(BrokerRealResources resources, String id, String name, String description, RealType<?, ?, O> type, O value) {
        super(resources, new ValueWrappable(id, name, description, type.getId(), type.serialise(value)), type);
    }

    @Override
    protected BrokerRealValue<O> getThis() {
        return this;
    }
}
