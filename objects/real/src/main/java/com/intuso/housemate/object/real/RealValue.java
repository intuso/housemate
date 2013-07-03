package com.intuso.housemate.object.real;

import com.intuso.housemate.api.object.NoChildrenWrappable;
import com.intuso.housemate.api.object.value.ValueWrappable;

import java.util.Arrays;
import java.util.List;

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
     * @param values the value's initial values
     */
    public RealValue(RealResources resources, String id, String name, String description,
                     RealType<?, ?, O> type, O ... values) {
        this(resources, id, name, description, type, Arrays.asList(values));
    }

    /**
     * @param resources {@inheritDoc}
     * @param id the value's id
     * @param name the value's name
     * @param description the value's description
     * @param type the type of the value's value
     * @param values the value's initial values
     */
    public RealValue(RealResources resources, String id, String name, String description,
                     RealType<?, ?, O> type, List<O> values) {
        super(resources,
                new ValueWrappable(id, name, description, type.getId(), RealType.serialiseAll(type, values)), type);
    }
}
