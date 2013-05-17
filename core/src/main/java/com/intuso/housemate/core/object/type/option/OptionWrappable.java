package com.intuso.housemate.core.object.type.option;

import com.intuso.housemate.core.object.HousemateObjectWrappable;
import com.intuso.housemate.core.object.NoChildrenWrappable;

/**
 * Created by IntelliJ IDEA.
 * User: tomc
 * Date: 29/05/12
 * Time: 21:05
 * To change this template use File | Settings | File Templates.
 */
public class OptionWrappable extends HousemateObjectWrappable<NoChildrenWrappable> {

    protected OptionWrappable() {}

    public OptionWrappable(String id, String name, String description) {
        super(id, name,  description);
    }

    @Override
    public HousemateObjectWrappable clone() {
        return new OptionWrappable(getId(), getName(), getDescription());
    }
}
