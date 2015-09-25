package com.intuso.housemate.comms.api.internal.payload;

import com.intuso.housemate.object.api.internal.TypeInstances;

/**
 *
 * Data object for a property
 */
public final class PropertyData extends ValueBaseData<CommandData> {

    private static final long serialVersionUID = -1L;

    public final static String SET_COMMAND_ID = "set-command";
    public final static String VALUE_PARAM = "value";

    private PropertyData() {}

    public PropertyData(String id, String name, String description, String type, TypeInstances values) {
        super(id, name, description, type, values);
    }

    @Override
    public HousemateData clone() {
        return new PropertyData(getId(), getName(), getDescription(), getType(), getTypeInstances());
    }
}
