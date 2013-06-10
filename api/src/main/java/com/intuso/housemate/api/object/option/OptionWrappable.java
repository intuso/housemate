package com.intuso.housemate.api.object.option;

import com.intuso.housemate.api.object.HousemateObjectWrappable;
import com.intuso.housemate.api.object.list.ListWrappable;
import com.intuso.housemate.api.object.subtype.SubTypeWrappable;

/**
 * Created by IntelliJ IDEA.
 * User: tomc
 * Date: 29/05/12
 * Time: 21:05
 * To change this template use File | Settings | File Templates.
 */
public class OptionWrappable extends HousemateObjectWrappable<ListWrappable<SubTypeWrappable>> {

    protected OptionWrappable() {}

    public OptionWrappable(String id, String name, String description) {
        super(id, name, description);
    }

    @Override
    public HousemateObjectWrappable clone() {
        return new OptionWrappable(getId(), getName(), getDescription());
    }
}
