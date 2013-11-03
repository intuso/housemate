package com.intuso.housemate.api.object.property;

import com.intuso.housemate.api.object.HousemateData;
import com.intuso.housemate.api.object.command.CommandData;
import com.intuso.housemate.api.object.type.TypeInstances;
import com.intuso.housemate.api.object.value.ValueBaseData;

/**
 *
 * Data object for a property
 */
public final class PropertyData extends ValueBaseData<CommandData> {

    private static final long serialVersionUID = -1L;

    private PropertyData() {}

    public PropertyData(String id, String name, String description, String type, TypeInstances values) {
        super(id, name, description, type, values);
    }

    @Override
    public HousemateData clone() {
        return new PropertyData(getId(), getName(), getDescription(), getType(), getTypeInstances());
    }
}
