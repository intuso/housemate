package com.intuso.housemate.api.object.type;

import com.intuso.housemate.api.object.HousemateObjectWrappable;
import com.intuso.housemate.api.object.list.ListWrappable;
import com.intuso.housemate.api.object.subtype.SubTypeWrappable;

/**
 *
 * Data object for a compound type
 */
public class CompoundTypeWrappable extends TypeWrappable<ListWrappable<SubTypeWrappable>> {

    private CompoundTypeWrappable() {}

    /**
     * @param id {@inheritDoc}
     * @param name {@inheritDoc}
     * @param description {@inheritDoc}
     * @param minValues {@inheritDoc}
     * @param maxValues {@inheritDoc}
     */
    public CompoundTypeWrappable(String id, String name, String description, int minValues, int maxValues) {
        super(id, name, description, minValues, maxValues);
    }

    @Override
    public HousemateObjectWrappable clone() {
        return new CompoundTypeWrappable(getId(), getName(), getDescription(), getMinValues(), getMaxValues());
    }
}
