package com.intuso.housemate.api.object.parameter;

import com.intuso.housemate.api.object.HousemateObjectWrappable;
import com.intuso.housemate.api.object.NoChildrenWrappable;

/**
 * Created with IntelliJ IDEA.
 * User: ravnroot
 * Date: 17/01/13
 * Time: 00:17
 * To change this template use File | Settings | File Templates.
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

    public String getType() {
        return type;
    }
}
