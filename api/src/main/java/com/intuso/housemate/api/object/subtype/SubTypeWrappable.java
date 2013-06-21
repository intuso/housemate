package com.intuso.housemate.api.object.subtype;

import com.intuso.housemate.api.object.HousemateObjectWrappable;
import com.intuso.housemate.api.object.NoChildrenWrappable;

/**
 * Created by IntelliJ IDEA.
 * User: tomc
 * Date: 29/05/12
 * Time: 21:05
 * To change this template use File | Settings | File Templates.
 */
public final class SubTypeWrappable extends HousemateObjectWrappable<NoChildrenWrappable> {

    private String type;

    private SubTypeWrappable() {}

    public SubTypeWrappable(String id, String name, String description, String type) {
        super(id, name, description);
        this.type = type;
    }

    public String getType() {
        return type;
    }

    @Override
    public HousemateObjectWrappable clone() {
        return new SubTypeWrappable(getId(), getName(), getDescription(), type);
    }
}
