package com.intuso.housemate.api.object.value;

import com.intuso.housemate.api.object.HousemateData;
import com.intuso.housemate.api.object.NoChildrenData;
import com.intuso.housemate.api.object.type.TypeInstances;

/**
 *
 * Data object for a value
 */
public class ValueData extends ValueBaseData<NoChildrenData> {

    protected ValueData() {}

    public ValueData(String id, String name, String description, String type, TypeInstances values) {
        super(id, name,  description, type, values);
    }

    @Override
    public HousemateData clone() {
        return new ValueData(getId(), getName(), getDescription(), getType(), getTypeInstances());
    }
}
