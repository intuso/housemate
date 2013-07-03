package com.intuso.housemate.api.object.property;

import com.intuso.housemate.api.object.HousemateObjectWrappable;
import com.intuso.housemate.api.object.command.CommandWrappable;
import com.intuso.housemate.api.object.type.TypeInstances;
import com.intuso.housemate.api.object.value.ValueWrappableBase;

/**
 *
 * Data object for a property
 */
public final class PropertyWrappable extends ValueWrappableBase<CommandWrappable> {

    private PropertyWrappable() {}

    public PropertyWrappable(String id, String name, String description, String type, TypeInstances values) {
        super(id, name, description, type, values);
    }

    @Override
    public HousemateObjectWrappable clone() {
        return new PropertyWrappable(getId(), getName(), getDescription(), getType(), getValues());
    }
}
