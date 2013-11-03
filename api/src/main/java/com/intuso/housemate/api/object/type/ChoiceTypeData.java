package com.intuso.housemate.api.object.type;

import com.intuso.housemate.api.object.HousemateData;
import com.intuso.housemate.api.object.list.ListData;
import com.intuso.housemate.api.object.option.OptionData;

/**
 * Data object for a choice type
 */
public final class ChoiceTypeData extends TypeData<ListData<OptionData>> {

    private static final long serialVersionUID = -1L;

    private ChoiceTypeData() {}

    /**
     * @param id {@inheritDoc}
     * @param name {@inheritDoc}
     * @param description {@inheritDoc}
     * @param minValues {@inheritDoc}
     * @param maxValues {@inheritDoc}
     */
    public ChoiceTypeData(String id, String name, String description, int minValues, int maxValues) {
        super(id, name, description, minValues, maxValues);
    }

    @Override
    public HousemateData clone() {
        return new ChoiceTypeData(getId(), getName(), getDescription(), getMinValues(), getMaxValues());
    }
}
