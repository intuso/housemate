package com.intuso.housemate.core.object.value;

import com.intuso.housemate.core.object.HousemateObjectWrappable;
import com.intuso.housemate.core.object.NoChildrenWrappable;

/**
 * Created by IntelliJ IDEA.
 * User: tomc
 * Date: 24/05/12
 * Time: 00:08
 * To change this template use File | Settings | File Templates.
 */
public class ValueWrappable extends ValueWrappableBase<NoChildrenWrappable> {

    protected ValueWrappable() {}

    public ValueWrappable(String id, String name, String description, String type, String value) {
        super(id, name,  description, type, value);
    }

    @Override
    public HousemateObjectWrappable clone() {
        return new ValueWrappable(getId(), getName(), getDescription(), getType(), getValue());
    }
}
