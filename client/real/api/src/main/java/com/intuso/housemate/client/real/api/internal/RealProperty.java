package com.intuso.housemate.client.real.api.internal;

import com.intuso.housemate.client.api.internal.object.Property;

/**
 * @param <O> the type of the property's value
 */
public interface RealProperty<O,
        TYPE extends com.intuso.housemate.client.real.api.internal.RealType<O, ?>,
        COMMAND extends RealCommand<?, ?, ?>,
        PROPERTY extends RealProperty<O, TYPE, COMMAND, PROPERTY>>
        extends com.intuso.housemate.client.real.api.internal.RealValueBase<O, TYPE, Property.Listener<? super PROPERTY>, PROPERTY>,
        Property<O, TYPE, COMMAND, PROPERTY> {}
