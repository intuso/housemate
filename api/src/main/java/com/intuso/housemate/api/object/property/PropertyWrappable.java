package com.intuso.housemate.api.object.property;

import com.intuso.housemate.api.object.HousemateObjectWrappable;
import com.intuso.housemate.api.object.command.CommandWrappable;
import com.intuso.housemate.api.object.value.ValueWrappableBase;

/**
 * Created with IntelliJ IDEA.
 * User: ravnroot
 * Date: 07/07/12
 * Time: 19:13
 * To change this template use File | Settings | File Templates.
 */
public final class PropertyWrappable extends ValueWrappableBase<CommandWrappable> {

    private PropertyWrappable() {}

    public PropertyWrappable(String id, String name, String description, String type, String value) {
        super(id, name, description, type, value);
    }

    @Override
    public HousemateObjectWrappable clone() {
        return new PropertyWrappable(getId(), getName(), getDescription(), getType(), getValue());
    }
}
