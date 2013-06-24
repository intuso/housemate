package com.intuso.housemate.api.object.property;

import com.intuso.housemate.api.object.HousemateObjectWrappable;
import com.intuso.housemate.api.object.command.CommandWrappable;
import com.intuso.housemate.api.object.type.TypeInstance;
import com.intuso.housemate.api.object.value.ValueWrappableBase;

/**
 *
 * Data object for a property
 */
public final class PropertyWrappable extends ValueWrappableBase<CommandWrappable> {

    private PropertyWrappable() {}

    public PropertyWrappable(String id, String name, String description, String type, TypeInstance value) {
        super(id, name, description, type, value);
    }

    @Override
    public HousemateObjectWrappable clone() {
        return new PropertyWrappable(getId(), getName(), getDescription(), getType(), getValue());
    }
}
