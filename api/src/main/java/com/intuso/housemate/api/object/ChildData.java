package com.intuso.housemate.api.object;

public class ChildData extends HousemateData<NoChildrenData> {

    private ChildData() {}

    public ChildData(String id, String name, String description) {
        super(id, name, description);
    }

    @Override
    public HousemateData clone() {
        return new ChildData(getId(), getName(), getDescription());
    }
}
