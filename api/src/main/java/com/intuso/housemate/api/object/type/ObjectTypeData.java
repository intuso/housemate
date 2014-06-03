package com.intuso.housemate.api.object.type;

import com.intuso.housemate.api.object.HousemateData;
import com.intuso.housemate.api.object.NoChildrenData;

/**
 *
 * Data object for an object type
 */
public final class ObjectTypeData extends TypeData<NoChildrenData> {

    private static final long serialVersionUID = -1L;

    public ObjectTypeData() {}

    /**
     * @param id {@inheritDoc}
     * @param name {@inheritDoc}
     * @param description {@inheritDoc}
     * @param minValues {@inheritDoc}
     * @param maxValues {@inheritDoc}
     */
    public ObjectTypeData(String id, String name, String description, int minValues, int maxValues) {
        super(id, name,  description, minValues, maxValues);
    }

    @Override
    public HousemateData clone() {
        return new ObjectTypeData(getId(), getName(), getDescription(), getMinValues(), getMaxValues());
    }
}
