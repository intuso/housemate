package com.intuso.housemate.api.object.value;

import com.intuso.housemate.api.object.HousemateObjectWrappable;
import com.intuso.housemate.api.object.NoChildrenWrappable;
import com.intuso.housemate.api.object.type.TypeInstances;

/**
 *
 * Data object for a value
 */
public class ValueWrappable extends ValueWrappableBase<NoChildrenWrappable> {

    protected ValueWrappable() {}

    public ValueWrappable(String id, String name, String description, String type, TypeInstances values) {
        super(id, name,  description, type, values);
    }

    @Override
    public HousemateObjectWrappable clone() {
        return new ValueWrappable(getId(), getName(), getDescription(), getType(), getValues());
    }
}
