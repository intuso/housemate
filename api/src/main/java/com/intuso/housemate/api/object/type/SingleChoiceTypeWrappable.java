package com.intuso.housemate.api.object.type;

import com.intuso.housemate.api.object.HousemateObjectWrappable;
import com.intuso.housemate.api.object.list.ListWrappable;
import com.intuso.housemate.api.object.option.OptionWrappable;

/**
 *
 * Data object for a single-choice type
 */
public final class SingleChoiceTypeWrappable extends TypeWrappable<ListWrappable<OptionWrappable>> {

    private SingleChoiceTypeWrappable() {}

    public SingleChoiceTypeWrappable(String id, String name, String description) {
        super(id, name, description);
    }

    @Override
    public HousemateObjectWrappable clone() {
        return new SingleChoiceTypeWrappable(getId(), getName(), getDescription());
    }
}
