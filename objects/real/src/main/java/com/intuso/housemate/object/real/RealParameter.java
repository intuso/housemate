package com.intuso.housemate.object.real;

import com.intuso.housemate.api.object.NoChildrenData;
import com.intuso.housemate.api.object.parameter.Parameter;
import com.intuso.housemate.api.object.parameter.ParameterData;
import com.intuso.housemate.api.object.parameter.ParameterListener;

/**
 * @param <O> the type of the parameter's value
 */
public class RealParameter<O>
        extends RealObject<ParameterData, NoChildrenData, RealObject<NoChildrenData, ? ,?, ?>, ParameterListener>
        implements Parameter<RealType<?, ?, O>> {

    private RealType<?, ?, O> type;

    /**
     * @param resources {@inheritDoc}
     * @param id the parameter's id
     * @param name the parameter's name
     * @param description the parameter's description
     * @param type the type of the parameter's value
     */
    public RealParameter(RealResources resources, String id, String name, String description, RealType<?, ?, O> type) {
        super(resources, new ParameterData(id, name, description, type.getId()));
        this.type = type;
    }

    @Override
    public final RealType<?, ?, O> getType() {
        return type;
    }
}
