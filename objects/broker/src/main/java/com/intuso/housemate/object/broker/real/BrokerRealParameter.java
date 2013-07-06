package com.intuso.housemate.object.broker.real;

import com.intuso.housemate.api.object.NoChildrenData;
import com.intuso.housemate.api.object.parameter.Parameter;
import com.intuso.housemate.api.object.parameter.ParameterData;
import com.intuso.housemate.api.object.parameter.ParameterListener;
import com.intuso.housemate.object.real.RealType;

/**
 * @param <O>  the type of the parameter
 */
public class BrokerRealParameter<O>
        extends BrokerRealObject<ParameterData, NoChildrenData, NoChildrenBrokerRealObject, ParameterListener>
        implements Parameter<RealType<?, ?, O>> {

    private RealType<?, ?, O> type;

    /**
     * @param resources {@inheritDoc}
     * @param id the object's id
     * @param name the object's name
     * @param description the object's description
     * @param type the type of the value
     */
    public BrokerRealParameter(BrokerRealResources resources, String id, String name, String description, RealType<?, ?, O> type) {
        super(resources, new ParameterData(id, name, description, type.getId()));
        this.type = type;
    }

    @Override
    public final RealType<?, ?, O> getType() {
        return type;
    }
}
