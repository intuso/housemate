package com.intuso.housemate.api.object.type;

import com.intuso.housemate.api.object.HousemateObjectWrappable;
import com.intuso.housemate.api.object.NoChildrenWrappable;

/**
 *
 * Data object for an object type
 */
public class ObjectTypeWrappable extends TypeWrappable<NoChildrenWrappable> {

    private ObjectTypeWrappable() {}

    /**
     * @param id {@inheritDoc}
     * @param name {@inheritDoc}
     * @param description {@inheritDoc}
     * @param minValues {@inheritDoc}
     * @param maxValues {@inheritDoc}
     */
    public ObjectTypeWrappable(String id, String name, String description, int minValues, int maxValues) {
        super(id, name,  description, minValues, maxValues);
    }

    @Override
    public HousemateObjectWrappable clone() {
        return new ObjectTypeWrappable(getId(), getName(), getDescription(), getMinValues(), getMaxValues());
    }
}
