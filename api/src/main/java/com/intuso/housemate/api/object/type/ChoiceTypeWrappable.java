package com.intuso.housemate.api.object.type;

import com.intuso.housemate.api.object.HousemateObjectWrappable;
import com.intuso.housemate.api.object.list.ListWrappable;
import com.intuso.housemate.api.object.option.OptionWrappable;

/**
 *
 * Data object for a multi-choice type
 */
public final class ChoiceTypeWrappable extends TypeWrappable<ListWrappable<OptionWrappable>> {

    private ChoiceTypeWrappable() {}

    /**
     * @param id {@inheritDoc}
     * @param name {@inheritDoc}
     * @param description {@inheritDoc}
     * @param minValues {@inheritDoc}
     * @param maxValues {@inheritDoc}
     */
    public ChoiceTypeWrappable(String id, String name, String description, int minValues, int maxValues) {
        super(id, name, description, minValues, maxValues);
    }

    @Override
    public HousemateObjectWrappable clone() {
        return new ChoiceTypeWrappable(getId(), getName(), getDescription(), getMinValues(), getMaxValues());
    }
}
