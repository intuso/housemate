package com.intuso.housemate.api.object.type;

import com.intuso.housemate.api.object.HousemateObjectWrappable;
import com.intuso.housemate.api.object.NoChildrenWrappable;

/**
 *
 * Data object for an object type
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
