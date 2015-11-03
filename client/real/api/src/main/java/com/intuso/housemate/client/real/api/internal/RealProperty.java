package com.intuso.housemate.client.real.api.internal;

import com.intuso.housemate.object.api.internal.Property;
import com.intuso.housemate.object.api.internal.TypeInstances;

/**
 * @param <O> the type of the property's value
 */
public interface RealProperty<O>
        extends com.intuso.housemate.client.real.api.internal.RealValueBase<O, Property.Listener<? super RealProperty<O>>, RealProperty<O>>,
        Property<TypeInstances, RealCommand, RealProperty<O>> {}
