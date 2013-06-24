package com.intuso.housemate.api.object.type;

import com.intuso.housemate.api.object.HousemateObjectWrappable;

/**
 *
 * Base data object for a type
 */
public abstract class TypeWrappable<WBL extends HousemateObjectWrappable<?>>
        extends HousemateObjectWrappable<WBL> {

    protected TypeWrappable() {}

    public TypeWrappable(String id, String name, String description) {
        super(id, name, description);
    }
}
