package com.intuso.housemate.api.object.subtype;

import com.intuso.housemate.api.object.HousemateData;
import com.intuso.housemate.api.object.NoChildrenData;

/**
 *
 * Data object for a sub type
 */
public final class SubTypeData extends HousemateData<NoChildrenData> {

    private String type;

    private SubTypeData() {}

    public SubTypeData(String id, String name, String description, String type) {
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
    public HousemateData clone() {
        return new SubTypeData(getId(), getName(), getDescription(), type);
    }
}
