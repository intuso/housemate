package com.intuso.housemate.api.object.value;

import com.intuso.housemate.api.object.HousemateObjectWrappable;
import com.intuso.housemate.api.object.NoChildrenWrappable;
import com.intuso.housemate.api.object.type.TypeInstance;

/**
 *
 * Data object for a value
 */
public class ValueWrappable extends ValueWrappableBase<NoChildrenWrappable> {

    protected ValueWrappable() {}

    public ValueWrappable(String id, String name, String description, String type, TypeInstance value) {
        super(id, name,  description, type, value);
    }

    @Override
    public HousemateObjectWrappable clone() {
        return new ValueWrappable(getId(), getName(), getDescription(), getType(), getValue());
    }
}
