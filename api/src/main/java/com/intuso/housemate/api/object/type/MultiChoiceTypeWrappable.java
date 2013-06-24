package com.intuso.housemate.api.object.type;

import com.intuso.housemate.api.object.HousemateObjectWrappable;
import com.intuso.housemate.api.object.list.ListWrappable;
import com.intuso.housemate.api.object.option.OptionWrappable;

/**
 *
 * Data object for a multi-choice type
 */
public final class MultiChoiceTypeWrappable extends TypeWrappable<ListWrappable<OptionWrappable>> {

    private MultiChoiceTypeWrappable() {}

    public MultiChoiceTypeWrappable(String id, String name, String description) {
        super(id, name, description);
    }

    @Override
    public HousemateObjectWrappable clone() {
        return new MultiChoiceTypeWrappable(getId(), getName(), getDescription());
    }
}
