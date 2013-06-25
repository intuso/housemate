package com.intuso.housemate.object.real;

import com.intuso.housemate.api.object.NoChildrenWrappable;
import com.intuso.housemate.api.object.value.ValueWrappable;

/**
 * @param <O> the type of this value's value
 */
public class RealValue<O> extends RealValueBase<ValueWrappable, NoChildrenWrappable, RealObject<NoChildrenWrappable, ?, ?, ?>, O,
        RealValue<O>> {

    /**
     * @param resources {@inheritDoc}
     * @param id the value's id
     * @param name the value's name
     * @param description the value's description
     * @param type the type of the value's value
     * @param value the value's initial value
     */
    public RealValue(RealResources resources, String id, String name, String description,
                     RealType<?, ?, O> type, O value) {
        super(resources,
                new ValueWrappable(id, name, description, type.getId(), value != null ? type.serialise(value) : null),
                type);
    }
}
