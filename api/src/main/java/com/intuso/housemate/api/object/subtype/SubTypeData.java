package com.intuso.housemate.api.object.subtype;

import com.intuso.housemate.api.object.HousemateData;
import com.intuso.housemate.api.object.NoChildrenData;

/**
 *
 * Data object for a sub type
 */
public final class SubTypeData extends HousemateData<NoChildrenData> {

    private static final long serialVersionUID = -1L;

    private String type;

    public SubTypeData() {}

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

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public HousemateData clone() {
        return new SubTypeData(getId(), getName(), getDescription(), type);
    }
}
