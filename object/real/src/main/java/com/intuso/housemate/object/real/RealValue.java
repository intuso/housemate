package com.intuso.housemate.object.real;

import com.intuso.housemate.api.object.NoChildrenWrappable;
import com.intuso.housemate.api.object.value.ValueWrappable;

/**
* Created with IntelliJ IDEA.
* User: ravnroot
* Date: 09/05/13
* Time: 23:48
* To change this template use File | Settings | File Templates.
*/
public class RealValue<O> extends RealValueBase<ValueWrappable, NoChildrenWrappable, RealObject<NoChildrenWrappable, ?, ?, ?>, O,
        RealValue<O>> {
    public RealValue(RealResources resources, String id, String name, String description,
                     RealType<?, ?, O> type, O value) {
        super(resources,
                new ValueWrappable(id, name, description, type.getId(), value != null ? type.serialise(value) : null),
                type);
    }
}
