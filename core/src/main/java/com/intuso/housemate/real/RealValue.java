package com.intuso.housemate.real;

import com.intuso.housemate.core.object.NoChildrenWrappable;
import com.intuso.housemate.core.object.value.ValueWrappable;
import com.intuso.housemate.core.object.value.ValueWrappableBase;

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
        super(resources, new ValueWrappable(id, name, description, type.getId(), type.serialise(value)), type);
    }

    @Override
    protected RealValue getThis() {
        return this;
    }
}
