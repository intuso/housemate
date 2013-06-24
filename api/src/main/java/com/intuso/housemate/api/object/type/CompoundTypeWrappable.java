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

    public CompoundTypeWrappable(String id, String name, String description) {
        super(id, name, description);
    }

    @Override
    public HousemateObjectWrappable clone() {
        return new CompoundTypeWrappable(getId(), getName(), getDescription());
    }
}
