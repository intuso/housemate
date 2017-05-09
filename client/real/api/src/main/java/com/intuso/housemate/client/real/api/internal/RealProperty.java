package com.intuso.housemate.client.real.api.internal;

import com.intuso.housemate.client.api.internal.object.Property;

/**
 * @param <O> the type of the property's value
 */
public interface RealProperty<O,
        TYPE extends RealType<O, ?>,
        COMMAND extends RealCommand<?, ?, ?>,
        PROPERTY extends RealProperty<O, TYPE, COMMAND, PROPERTY>>
        extends RealValueBase<Property.Data, O, TYPE, Property.Listener<? super PROPERTY>, PROPERTY>,
        Property<O, TYPE, COMMAND, PROPERTY> {}
