package com.intuso.housemate.api.object.type;

import com.intuso.housemate.api.object.HousemateObjectWrappable;

/**
 * Created by IntelliJ IDEA.
 * User: tomc
 * Date: 29/05/12
 * Time: 21:05
 * To change this template use File | Settings | File Templates.
 */
public abstract class TypeWrappable<WBL extends HousemateObjectWrappable<?>>
        extends HousemateObjectWrappable<WBL> {

    protected TypeWrappable() {}

    public TypeWrappable(String id, String name, String description) {
        super(id, name, description);
    }
}
