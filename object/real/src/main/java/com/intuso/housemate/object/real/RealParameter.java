package com.intuso.housemate.object.real;

import com.intuso.housemate.api.object.NoChildrenWrappable;
import com.intuso.housemate.api.object.parameter.Parameter;
import com.intuso.housemate.api.object.parameter.ParameterListener;
import com.intuso.housemate.api.object.parameter.ParameterWrappable;

/**
 * Created with IntelliJ IDEA.
 * User: ravnroot
 * Date: 10/07/12
 * Time: 00:58
 * To change this template use File | Settings | File Templates.
 */
public class RealParameter<O>
        extends RealObject<ParameterWrappable, NoChildrenWrappable, RealObject<NoChildrenWrappable, ? ,?, ?>, ParameterListener>
        implements Parameter<RealType<?, ?, O>> {

    private RealType<?, ?, O> type;

    public RealParameter(RealResources resources, String id, String name, String description, RealType<?, ?, O> type) {
        super(resources, new ParameterWrappable(id, name, description, type.getId()));
        this.type = type;
    }

    @Override
    public final RealType<?, ?, O> getType() {
        return type;
    }
}
