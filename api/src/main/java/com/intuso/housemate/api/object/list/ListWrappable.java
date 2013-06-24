package com.intuso.housemate.api.object.list;

import com.intuso.housemate.api.object.HousemateObjectWrappable;

import java.util.Arrays;

/**
 *
 * Data object for a list
 */
public final class ListWrappable<WBL extends HousemateObjectWrappable<?>> extends HousemateObjectWrappable<WBL> {

    private ListWrappable() {}

    public ListWrappable(String id, String name, String description, WBL ... subWrappables) {
        this(id, name, description, Arrays.asList(subWrappables));
    }

    public ListWrappable(String id, String name, String description, java.util.List<WBL> subWrappables) {
        super(id, name, description, subWrappables);
    }

    @Override
    public HousemateObjectWrappable clone() {
        return new ListWrappable(getId(), getName(), getDescription());
    }
}
