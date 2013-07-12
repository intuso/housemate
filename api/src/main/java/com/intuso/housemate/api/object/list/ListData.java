package com.intuso.housemate.api.object.list;

import com.intuso.housemate.api.object.HousemateData;

import java.util.Arrays;

/**
 * Data object for a list
 * @param <DATA> The data object for the objects in the list
 */
public final class ListData<DATA extends HousemateData<?>> extends HousemateData<DATA> {

    private ListData() {}

    public ListData(String id, String name, String description, DATA... childData) {
        this(id, name, description, Arrays.asList(childData));
    }

    public ListData(String id, String name, String description, java.util.List<DATA> childData) {
        super(id, name, description, childData);
    }

    @Override
    public HousemateData clone() {
        return new ListData(getId(), getName(), getDescription());
    }
}
