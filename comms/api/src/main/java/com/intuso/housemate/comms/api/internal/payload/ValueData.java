package com.intuso.housemate.comms.api.internal.payload;

import com.intuso.housemate.object.api.internal.TypeInstances;

/**
 *
 * Data object for a value
 */
public final class ValueData extends ValueBaseData<NoChildrenData> {

    private static final long serialVersionUID = -1L;

    public ValueData() {}

    public ValueData(String id, String name, String description, String type, TypeInstances values) {
        super(id, name,  description, type, values);
    }

    @Override
    public HousemateData clone() {
        return new ValueData(getId(), getName(), getDescription(), getType(), getTypeInstances());
    }
}
