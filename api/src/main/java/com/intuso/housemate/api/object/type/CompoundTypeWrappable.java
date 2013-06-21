package com.intuso.housemate.api.object.type;

import com.intuso.housemate.api.object.HousemateObjectWrappable;
import com.intuso.housemate.api.object.list.ListWrappable;
import com.intuso.housemate.api.object.subtype.SubTypeWrappable;

/**
 * Created with IntelliJ IDEA.
 * User: ravnroot
 * Date: 18/06/13
 * Time: 22:28
 * To change this template use File | Settings | File Templates.
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
