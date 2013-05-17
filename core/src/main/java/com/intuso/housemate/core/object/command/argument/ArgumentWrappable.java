package com.intuso.housemate.core.object.command.argument;

import com.intuso.housemate.core.object.HousemateObjectWrappable;
import com.intuso.housemate.core.object.NoChildrenWrappable;

/**
 * Created with IntelliJ IDEA.
 * User: ravnroot
 * Date: 17/01/13
 * Time: 00:17
 * To change this template use File | Settings | File Templates.
 */
public final class ArgumentWrappable extends HousemateObjectWrappable<NoChildrenWrappable> {

    private String type;

    public ArgumentWrappable() {}

    public ArgumentWrappable(String id, String name, String description, String type) {
        super(id, name, description);
        this.type = type;
    }

    @Override
    public HousemateObjectWrappable clone() {
        return new ArgumentWrappable(getId(), getName(), getDescription(), type);
    }

    public String getType() {
        return type;
    }
}
