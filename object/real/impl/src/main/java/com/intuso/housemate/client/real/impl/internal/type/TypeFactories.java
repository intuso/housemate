package com.intuso.housemate.client.real.impl.internal.type;

import com.google.inject.Inject;
import com.intuso.housemate.client.real.impl.internal.RealParameterImpl;
import com.intuso.housemate.client.real.impl.internal.RealPropertyImpl;
import com.intuso.housemate.client.real.impl.internal.RealTypeImpl;
import com.intuso.housemate.client.real.impl.internal.RealValueImpl;

/**
 * Created by tomc on 23/05/16.
 */
public class TypeFactories<O> {

    private final RealTypeImpl<O> type;
    private final RealParameterImpl.Factory<O> parameterFactory;
    private final RealPropertyImpl.Factory<O> propertyFactory;
    private final RealValueImpl.Factory<O> valueFactory;

    @Inject
    public TypeFactories(RealTypeImpl<O> type, RealParameterImpl.Factory<O> parameterFactory, RealPropertyImpl.Factory<O> propertyFactory, RealValueImpl.Factory<O> valueFactory) {
        this.type = type;
        this.parameterFactory = parameterFactory;
        this.propertyFactory = propertyFactory;
        this.valueFactory = valueFactory;
    }

    public RealTypeImpl<O> getType() {
        return type;
    }

    public RealParameterImpl.Factory<O> getParameterFactory() {
        return parameterFactory;
    }

    public RealPropertyImpl.Factory<O> getPropertyFactory() {
        return propertyFactory;
    }

    public RealValueImpl.Factory<O> getValueFactory() {
        return valueFactory;
    }
}
