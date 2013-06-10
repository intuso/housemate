package com.intuso.housemate.api.object.list;

import com.intuso.housemate.api.object.HousemateObjectWrappable;

import java.util.Arrays;

/**
 * Created with IntelliJ IDEA.
 * User: ravnroot
 * Date: 07/07/12
 * Time: 18:39
 * To change this template use File | Settings | File Templates.
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
