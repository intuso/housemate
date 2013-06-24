package com.intuso.housemate.api.object.subtype;

import com.intuso.housemate.api.object.HousemateObjectWrappable;
import com.intuso.housemate.api.object.NoChildrenWrappable;

/**
 *
 * Data object for a sub type
 */
public final class SubTypeWrappable extends HousemateObjectWrappable<NoChildrenWrappable> {

    private String type;

    private SubTypeWrappable() {}

    public SubTypeWrappable(String id, String name, String description, String type) {
        super(id, name, description);
        this.type = type;
    }

    /**
     * Gets the type id
     * @return the type id
     */
    public String getType() {
        return type;
    }

    @Override
    public HousemateObjectWrappable clone() {
        return new SubTypeWrappable(getId(), getName(), getDescription(), type);
    }
}
