package com.intuso.housemate.core.object.list;

import com.intuso.housemate.core.object.HousemateObjectWrappable;

/**
 * Created with IntelliJ IDEA.
 * User: ravnroot
 * Date: 07/07/12
 * Time: 18:39
 * To change this template use File | Settings | File Templates.
 */
public final class ListWrappable<WBL extends HousemateObjectWrappable<?>> extends HousemateObjectWrappable<WBL> {

    private ListWrappable() {}

    public ListWrappable(String id, String name, String description) {
        super(id, name, description);
    }

    @Override
    public HousemateObjectWrappable clone() {
        return new ListWrappable(getId(), getName(), getDescription());
    }
}
