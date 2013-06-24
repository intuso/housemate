package com.intuso.housemate.api.object.option;

import com.intuso.housemate.api.object.HousemateObjectWrappable;
import com.intuso.housemate.api.object.list.ListWrappable;
import com.intuso.housemate.api.object.subtype.SubTypeWrappable;

/**
 *
 * Data object for an option
 */
public class OptionWrappable extends HousemateObjectWrappable<ListWrappable<SubTypeWrappable>> {

    protected OptionWrappable() {}

    public OptionWrappable(String id, String name, String description) {
        super(id, name, description);
    }

    @Override
    public HousemateObjectWrappable clone() {
        return new OptionWrappable(getId(), getName(), getDescription());
    }
}
