package com.intuso.housemate.object.broker.real;

import com.intuso.housemate.api.object.NoChildrenWrappable;
import com.intuso.housemate.api.object.parameter.Parameter;
import com.intuso.housemate.api.object.parameter.ParameterListener;
import com.intuso.housemate.api.object.parameter.ParameterWrappable;
import com.intuso.housemate.object.real.RealType;

/**
 */
public class BrokerRealParameter<O>
        extends BrokerRealObject<ParameterWrappable, NoChildrenWrappable, NoChildrenBrokerRealObject, ParameterListener>
        implements Parameter<RealType<?, ?, O>> {

    private RealType<?, ?, O> type;

    public BrokerRealParameter(BrokerRealResources resources, String id, String name, String description, RealType<?, ?, O> type) {
        super(resources, new ParameterWrappable(id, name, description, type.getId()));
        this.type = type;
    }

    @Override
    public final RealType<?, ?, O> getType() {
        return type;
    }
}
