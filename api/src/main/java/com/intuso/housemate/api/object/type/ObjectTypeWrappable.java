package com.intuso.housemate.api.object.type;

import com.intuso.housemate.api.object.HousemateObjectWrappable;
import com.intuso.housemate.api.object.NoChildrenWrappable;

/**
 * Created with IntelliJ IDEA.
 * User: ravnroot
 * Date: 22/04/13
 * Time: 08:45
 * To change this template use File | Settings | File Templates.
 */
public class ObjectTypeWrappable extends TypeWrappable<NoChildrenWrappable> {

    private ObjectTypeWrappable() {}

    public ObjectTypeWrappable(String id, String name, String description) {
        super(id, name,  description);
    }

    @Override
    public HousemateObjectWrappable clone() {
        return new ObjectTypeWrappable(getId(), getName(), getDescription());
    }
}
