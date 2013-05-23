package com.intuso.housemate.api.object.root;

import com.intuso.housemate.api.object.HousemateObjectWrappable;

/**
 * Created with IntelliJ IDEA.
 * User: ravnroot
 * Date: 08/07/12
 * Time: 21:45
 * To change this template use File | Settings | File Templates.
 */
public final class RootWrappable extends HousemateObjectWrappable<HousemateObjectWrappable<?>> {

    public RootWrappable() {
        super("", null, null);
    }

    @Override
    public HousemateObjectWrappable clone() {
        return new RootWrappable();
    }
}
