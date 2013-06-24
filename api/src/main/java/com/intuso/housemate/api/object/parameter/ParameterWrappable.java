package com.intuso.housemate.api.object.parameter;

import com.intuso.housemate.api.object.HousemateObjectWrappable;
import com.intuso.housemate.api.object.NoChildrenWrappable;

/**
 *
 * Data object for a parameter
 */
public final class ParameterWrappable extends HousemateObjectWrappable<NoChildrenWrappable> {

    private String type;

    public ParameterWrappable() {}

    public ParameterWrappable(String id, String name, String description, String type) {
        super(id, name, description);
        this.type = type;
    }

    @Override
    public HousemateObjectWrappable clone() {
        return new ParameterWrappable(getId(), getName(), getDescription(), type);
    }

    /**
     * Gets the type id of the parameter
     * @return the type if of the parameter
     */
    public String getType() {
        return type;
    }
}
